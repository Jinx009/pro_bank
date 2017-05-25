package com.rongdu.p2psys.nb.product.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.borrow.service.BorrowService;
import com.rongdu.p2psys.nb.invest.service.FrozenProductService;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.nb.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.dao.ProductBasicDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductSet;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductSetService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.user.domain.User;

@Service("productBasicService")
public class ProductBasicServiceImpl implements ProductBasicService {

	@Resource
	private AccountService theAccountService;
	@Resource
	private BorrowService theBorrowService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private FrozenProductService frozenProductService;
	@Resource
	private FrozenUserService frozenUserService;
	@Resource
	private PpfundService thePpfundService;
	@Resource
	private ProductSetService productSetService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private PpfundInDao thePpfundInDao;
	@Resource
	private ProductBasicDao productBasicDao;
	@Resource
	private UserService theUserService;

	Logger logger = Logger.getLogger(ProductBasicServiceImpl.class);

	// 前台

	@Override
	public ProductBasic findById(Long id) {
		return productBasicDao.find(id);
	}

	@Override
	public List<ProductBasicModel> getRecommendModelList(Long userId,
			String platform) {
		List<ProductBasic> pojoList = new ArrayList<ProductBasic>();
		List<ProductBasicModel> modelList = new ArrayList<ProductBasicModel>();

		logger.error("=== 热销榜 === userId:" + userId);
		User user = theUserService.getByUserId(userId);

		// 体验标
		if (userId.equals(0L)
				|| (null != user.getAddTime() && isNewUser(user) && !theExperienceGoldService
						.checkUserIsUseGold(userId))) {
			pojoList = productBasicDao.getRecommendProduct(platform, true);
			for (ProductBasic pojo : pojoList) {
				ProductBasicModel model = ProductBasicModel.instance(pojo);

				// 获取对应的PPfund对象
				PpfundModel ppfundModel = PpfundModel.instance(thePpfundService
						.getById(pojo.getRelatedId()));
				// 加息券
				if (null != ppfundModel.getInterestRate()) {
					ppfundModel.setInterestRateValue(ppfundModel
							.getInterestRate().getRate());
				}

				model.setExperienceModel(ppfundModel);

				setDescription(model);

				modelList.add(model);
			}
		}

		// 其他非体验标
		pojoList = productBasicDao.getRecommendProduct(platform, false);
		for (ProductBasic pojo : pojoList) {
			ProductBasicModel model = ProductBasicModel.instance(pojo);

			if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND
					.equalsIgnoreCase(pojo.getProductType().getTypeCategory())) {
				// 现金类产品
				PpfundModel ppfundModel = PpfundModel.instance(thePpfundService
						.getById(pojo.getRelatedId()));
				// 加息券
				if (null != ppfundModel.getInterestRate()) {
					ppfundModel.setInterestRateValue(ppfundModel
							.getInterestRate().getRate());
				}

				model.setPpfundModel(ppfundModel);
			} else if (ProductTypeConstant.PRODUCT_CATEGORY__FIVESTAR
					.equalsIgnoreCase(pojo.getProductType().getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__VIP
							.equalsIgnoreCase(pojo.getProductType()
									.getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__BORROW
							.equalsIgnoreCase(pojo.getProductType()
									.getTypeCategory())) {
				// 非现金类产品
				BorrowModel borrowModel = BorrowModel.instance(theBorrowService
						.getById(pojo.getRelatedId()));
				// 加息券
				if (null != borrowModel.getInterestRate()) {
					borrowModel.setInterestRateValue(borrowModel
							.getInterestRate().getRate());
				}

				model.setBorrowModel(borrowModel);
			} else {
				continue;
			}
			setDescription(model);

			modelList.add(model);
		}
		return modelList;
	}

	@Override
	public List<ProductBasicModel> getModelListByFlag(Long flagId,
			String platform) {
		QueryParam param1 = QueryParam.getInstance();
		if (null != flagId) {
			param1.addParam("productTypeFlag.id", flagId);
		}
		if (Constant.COOPERATE_TYPE__PC.equals(platform)) {
			param1.addParam("showForPc", 1);
		} else if (Constant.COOPERATE_TYPE__WECHAT.equals(platform)) {
			param1.addParam("showForWechat", 1);
		}
		param1.addOrder("showOrder");
		param1.addOrder(OrderType.DESC, "recommendTime");
		param1.addOrder(OrderType.DESC, "id");
		List<ProductBasic> prodList = productBasicDao.findByCriteria(param1);

		List<ProductBasicModel> list = new ArrayList<ProductBasicModel>();
		for (ProductBasic basic : prodList) {
			ProductBasicModel model = ProductBasicModel.instance(basic);

			if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW
					.equalsIgnoreCase(model.getProductType().getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__FIVESTAR
							.equalsIgnoreCase(model.getProductType()
									.getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__VIP
							.equalsIgnoreCase(model.getProductType()
									.getTypeCategory())) {
				// 非现金类产品
				// 五星专享
				// VIP
				BorrowModel bm = BorrowModel.instance(theBorrowService
						.getById(model.getRelatedId()));
				// 加息券
				if (null != bm.getInterestRate()) {
					bm.setInterestRateValue(bm.getInterestRate().getRate());
				}

				model.setBorrowModel(bm);
			} else if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND
					.equalsIgnoreCase(model.getProductType().getTypeCategory())) {
				// 现金类产品
				PpfundModel pm = PpfundModel.instance(thePpfundService
						.getById(model.getRelatedId()));
				// 加息券
				if (null != pm.getInterestRate()) {
					pm.setInterestRateValue(pm.getInterestRate().getRate());
				}

				model.setPpfundModel(pm);
			} else {
				continue;
			}
			setDescription(model);

			list.add(model);
		}
		return list;
	}

	@Override
	public List<ProductBasicModel> getProductBasicModelListByFlag(User user,
			Long flagId, String orderField) {
		List<ProductBasicModel> productBasicModelList = new ArrayList<ProductBasicModel>();

		List<ProductBasic> productBasicList = productBasicDao.getProductByFlag(
				flagId, orderField);
		for (ProductBasic pojo : productBasicList) {
			ProductBasicModel model = ProductBasicModel.instance(pojo);

			// LOG
			logger.debug("===== ProductBasicModel.id ===== : "
					+ model.getId()
					+ " || ===== ProductBasicModel.productType.typeCategory ===== : "
					+ model.getProductType().getTypeCategory());

			if (ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE
					.equalsIgnoreCase(pojo.getProductType().getTypeCategory())) {
				if (null != user
						&& null != user.getAddTime()
						&& isNewUser(user)
						&& !theExperienceGoldService.checkUserIsUseGold(user
								.getUserId())) {
					// 体验金产品
					Ppfund ppfundPojo = thePpfundService.getById(pojo
							.getRelatedId());
					PpfundModel ppfundModel = PpfundModel.instance(ppfundPojo);
					// 加息券
					if (null != ppfundModel.getInterestRate()) {
						ppfundModel.setInterestRateValue(ppfundModel
								.getInterestRate().getRate());
					}

					// 投资总额
					model.setTotalInvestMoney(thePpfundService
							.getTotalInvestMoney(ppfundPojo));
					// 投资总收益
					model.setTotalProfitMoney(thePpfundService
							.getTotalProfitMoney(ppfundPojo));
					model.setExperienceModel(ppfundModel);
				} else {
					continue;
				}

				// LOG
				logger.debug("===== EXPERIENCE ===== : " + model.getId());

			} else if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND
					.equalsIgnoreCase(pojo.getProductType().getTypeCategory())) {
				// 现金类产品
				Ppfund ppfundPojo = thePpfundService.getById(pojo
						.getRelatedId());
				PpfundModel ppfundModel = PpfundModel.instance(ppfundPojo);
				// 加息券
				if (null != ppfundModel.getInterestRate()) {
					ppfundModel.setInterestRateValue(ppfundModel
							.getInterestRate().getRate());
				}

				// 投资总额
				model.setTotalInvestMoney(thePpfundService
						.getTotalInvestMoney(ppfundPojo));
				// 投资总收益
				model.setTotalProfitMoney(thePpfundService
						.getTotalProfitMoney(ppfundPojo));
				model.setPpfundModel(ppfundModel);

				// LOG
				logger.debug("===== PPFUND ===== : " + model.getId());

			} else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW
					.equalsIgnoreCase(pojo.getProductType().getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__FIVESTAR
							.equalsIgnoreCase(pojo.getProductType()
									.getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__VIP
							.equalsIgnoreCase(pojo.getProductType()
									.getTypeCategory())) {
				// 非现金类产品
				BorrowModel borrowModel = BorrowModel.instance(theBorrowService
						.getById(pojo.getRelatedId()));
				// 加息券
				if (null != borrowModel.getInterestRate()) {
					borrowModel.setInterestRateValue(borrowModel
							.getInterestRate().getRate());
				}

				model.setBorrowModel(borrowModel);

				// LOG
				logger.debug("===== BORROW ===== : " + model.getId());

			} else {
				continue;
			}
			productBasicModelList.add(model);
		}
		return productBasicModelList;
	}

	@Override
	public ProductBasicModel getModelById(Long id) {
		ProductBasic prod = productBasicDao.find(id);
		ProductBasicModel model = ProductBasicModel.instance(prod);

		if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(model
				.getProductType().getTypeCategory())) {
			// 现金类产品
			PpfundModel pm = PpfundModel.instance(thePpfundService
					.getById(model.getRelatedId()));
			// 加息券
			if (null != pm.getInterestRate()) {
				pm.setInterestRateValue(pm.getInterestRate().getRate());
			}

			model.setPpfundModel(pm);
		} else if (ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE
				.equals(model.getProductType().getTypeCategory())) {
			// 体验标
			PpfundModel em = PpfundModel.instance(thePpfundService
					.getById(model.getRelatedId()));
			// 加息券
			if (null != em.getInterestRate()) {
				em.setInterestRateValue(em.getInterestRate().getRate());
			}

			model.setExperienceModel(em);
		} else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW.equals(model
				.getProductType().getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__FIVESTAR.equals(model
						.getProductType().getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__VIP.equals(model
						.getProductType().getTypeCategory())) {
			// 非现金类产品
			BorrowModel bm = BorrowModel.instance(theBorrowService
					.getById(model.getRelatedId()));
			// 加息券
			if (null != bm.getInterestRate()) {
				bm.setInterestRateValue(bm.getInterestRate().getRate());
			}

			model.setBorrowModel(bm);
		} else {
			return null;
		}
		setDescription(model);

		return model;
	}

	@Override
	public List<ProductBasic> getExperienceProductList() {
		// 体验标
		QueryParam param = QueryParam.getInstance();
		param.addParam("productType.typeCategory",
				ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE);
		return productBasicDao.findByCriteria(param);
	}

	// 后台

	@Override
	public PageDataList<ProductBasicModel> getAllListForPaging(
			ProductBasicModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),
				model.getSize());
		param.addOrder("showOrder");
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");

		if (model != null) {
			if (!StringUtil.isBlank(model.getProductDetail())) {
				// 模糊查询
				// [产品所属类型]或[产品名称]
				SearchFilter orFilter1 = new SearchFilter("productName",
						Operators.LIKE, model.getProductDetail());
				SearchFilter orFilter2 = new SearchFilter(
						"productTypeFlag.flagName", Operators.LIKE,
						model.getProductDetail());
				param.addOrFilter(orFilter1, orFilter2);
			} else {
				// 精确查询
				// [产品所属类型]或[产品名称]
				if (StringUtil.isNotBlank(model.getProductName())) {
					param.addParam("productName", Operators.EQ,
							model.getProductName());
				}
				if (StringUtil.isNotBlank(model.getFlagDesc())) {
					param.addParam("productTypeFlag.flagName", Operators.EQ,
							model.getFlagDesc());
				}
				if (null != model.getShowForPc() && model.getShowForPc() > -1) {
					param.addParam("showForPc", model.getShowForPc());
				}
				if (null != model.getShowForWechat()
						&& model.getShowForWechat() > -1) {
					param.addParam("showForWechat", model.getShowForWechat());
				}
			}
		}

		PageDataList<ProductBasic> pojoList = productBasicDao
				.findPageList(param);
		List<ProductBasicModel> modellist = new ArrayList<ProductBasicModel>();

		if (pojoList.getList().size() > 0) {
			for (int i = 0; i < pojoList.getList().size(); i++) {
				ProductBasic productBasic = (ProductBasic) pojoList.getList()
						.get(i);
				ProductBasicModel productBasicModel = ProductBasicModel
						.instance(productBasic);

				if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND
						.equals(productBasic.getProductType().getTypeCategory())
						|| ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE
								.equals(productBasic.getProductType()
										.getTypeCategory())) {
					productBasicModel.setPpfundModel(PpfundModel
							.instance(thePpfundService.getById(productBasic
									.getRelatedId())));
				} else {
					productBasicModel.setBorrowModel(BorrowModel
							.instance(theBorrowService.getById(productBasic
									.getRelatedId())));
				}
				setDescription(productBasicModel);

				modellist.add(productBasicModel);
			}
		}
		PageDataList<ProductBasicModel> pageList = new PageDataList<ProductBasicModel>();
		pageList.setPage(pojoList.getPage());
		pageList.setList(modellist);
		return pageList;
	}

	@Override
	public ProductBasic saveProductBasic(ProductBasic productBasic) {
		return productBasicDao.save(productBasic);
	}

	@Override
	public ProductBasic updateProductBasic(ProductBasic productBasic) {
		return productBasicDao.update(productBasic);
	}

	@Override
	public void updateDisplayLogic(Long id, String platform) {
		ProductBasic productBasic = findById(id);

		if (Constant.COOPERATE_TYPE__PC.equals(platform)) {
			if (ConstantUtil.FLAG_TRUE.equals(productBasic.getShowForPc())) {
				productBasic.setShowForPc(ConstantUtil.FLAG_FALSE);
			} else {
				productBasic.setShowForPc(ConstantUtil.FLAG_TRUE);
			}
		} else if (Constant.COOPERATE_TYPE__WECHAT.equals(platform)) {
			if (ConstantUtil.FLAG_TRUE.equals(productBasic.getShowForWechat())) {
				productBasic.setShowForWechat(ConstantUtil.FLAG_FALSE);
			} else {
				productBasic.setShowForWechat(ConstantUtil.FLAG_TRUE);
			}
		}
		updateProductBasic(productBasic);
	}

	@Override
	public void changeRecommend(Long id, String reason) {
		productBasicDao.changeRecommend(id, reason);
	}

	// 通用

	@Override
	public ProductBasic getProductBasicInfo(Long typeId, Long relatedId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("productType.id", typeId);
		param.addParam("relatedId", relatedId);
		return productBasicDao.findByCriteriaForUnique(param);
	}

	// 通用FlagId
	@Override
	public ProductBasic getProductBasicInfoByFlagId(Long flagId, Long relatedId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("productTypeFlag.id", flagId);
		param.addParam("relatedId", relatedId);
		List<ProductBasic> productList = productBasicDao.findByCriteria(param);
		if (productList != null && productList.size() > 0) {
			return productList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String checkInvest(Long productBasicId, User user, Double money,
			String pwd, Double subMoney) {
		DecimalFormat df = new DecimalFormat("#.00");

		Double totalMoney = new Double(0.0);
		Double lockUserMoney = frozenUserService.getLockUseMoney((int) user
				.getUserId());

		List<Account> list = theAccountService.getAccountListByGroupId(user
				.getBindId());

		for (int i = 0; i < list.size(); i++) {
			totalMoney += list.get(i).getUseMoney();
		}
		if (totalMoney - lockUserMoney < subMoney) {
			return "noMoney";
		}

		ProductBasic productBasic = productBasicDao.find(productBasicId);
		if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(productBasic
				.getProductType().getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE
						.equals(productBasic.getProductType().getTypeCategory())) {
			// ppfund
			Ppfund ppfund = thePpfundService.getById(productBasic
					.getRelatedId());
			Double lockPpfundMoney = frozenProductService
					.getLockMoney((int) ppfund.getId());

			if (2046l == productBasic.getProductType().getId()) {
				ExperienceGoldModel experienceGoldModel = theExperienceGoldService
						.getEGByUserId(user.getUserId());

				if (!isNewUser(user)) {
					return "对不起，您不能投标!";
				}

				if (null != experienceGoldModel) {
					return "对不起，您不能投标!";
				}
			}
			if (money < ppfund.getLowestAccount()) {
				return "投资金额小于单笔最小投资金额:￥"
						+ df.format(ppfund.getLowestAccount()) + "!";
			}
			if (money > ppfund.getMostAccount() && 0 != ppfund.getMostAccount()) {
				return "投资金额大于单笔最大投资金额:￥" + df.format(ppfund.getMostAccount())
						+ "!";
			}
			if (money + lockPpfundMoney + ppfund.getAccountYes() > ppfund
					.getAccount() && 0 != ppfund.getAccount()) {
				return "超过此项目最大接受金额!";
			}
		} else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW
				.equals(productBasic.getProductType().getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__VIP
						.equals(productBasic.getProductType().getTypeCategory())) {
			// borrow
			Borrow borrow = theBorrowService.getById(productBasic
					.getRelatedId());
			Double lockBorrowMoney = frozenProductService
					.getLockMoney((int) borrow.getId());

			if (null != borrow.getPwd() && !"".equals(borrow.getPwd())) {
				if (!pwd.equals(borrow.getPwd())) {
					return "定向标密码不正确!";
				}
			}
			if (new Date().before(borrow.getFixedTime())) {
				return "该产品在"
						+ DateFormat.getDateInstance().format(
								borrow.getFixedTime()) + "后方可投资，好产品值得期待!";
			}
			if (money < borrow.getLowestAccount()) {
				return "投资金额小于单笔最小投资金额:￥"
						+ df.format(borrow.getLowestAccount()) + "!";
			}
			if (money > borrow.getMostAccount()) {
				return "投资金额单笔最大投资金额:￥" + df.format(borrow.getMostAccount())
						+ "!";
			}
			if (money + lockBorrowMoney + borrow.getAccountYes() > borrow
					.getAccount() && 0 != borrow.getAccount()) {
				return "超过此项目最大接受金额!";
			}
		} else {
			// 组合标
			double minMoney = getMinMoney(productBasic);
			if (minMoney > money) {
				return "此组合标最小起投金额为:￥" + df.format(minMoney) + "!";
			}
			if (!checkInvestStatus(productBasic, minMoney)) {
				return "此组合标已不能投标!";
			}
			List<ProductSet> prodSetList = productSetService
					.getProdSetList(productBasic.getId());

			for (int j = 0; j < prodSetList.size(); j++) {
				ProductBasic basic_ = productBasicDao.find(prodSetList.get(j)
						.getSubProductId());

				if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(basic_
						.getProductType().getTypeCategory())) {
					double p_money = money * prodSetList.get(j).getRate() / 100;
					Ppfund ppfund = thePpfundService.getById(basic_
							.getRelatedId());
					double lockPpfundMoney = frozenProductService
							.getLockMoney((int) ppfund.getId());

					if (p_money < ppfund.getLowestAccount()) {
						return "组合标中" + ppfund.getName() + "投资金额小于单笔最小投资金额:￥"
								+ df.format(ppfund.getLowestAccount()) + "!";
					}
					if (p_money > ppfund.getMostAccount()
							&& 0 != ppfund.getMostAccount()) {
						return "组合标中" + ppfund.getName() + "投资金额大于单笔最大投资金额:￥"
								+ df.format(ppfund.getMostAccount()) + "!";
					}
					if (p_money + lockPpfundMoney + ppfund.getAccountYes() > ppfund
							.getMostAccountTotal()
							&& 0 != ppfund.getMostAccountTotal()) {
						return "超过组合标中" + ppfund.getName() + "最大接受金额!";
					}
				} else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW
						.equals(basic_.getProductType().getTypeCategory())) {
					double b_money = money * prodSetList.get(j).getRate() / 100;
					Borrow borrow = theBorrowService.getById(basic_
							.getRelatedId());
					double lockBorrowMoney = frozenProductService
							.getLockMoney((int) borrow.getId());

					if (new Date().before(borrow.getFixedTime())) {
						return "该产品在"
								+ DateFormat.getDateInstance().format(
										borrow.getFixedTime())
								+ "后方可投资，好产品值得期待!";
					}
					if (b_money < borrow.getLowestAccount()) {
						return "组合标中" + borrow.getName() + "投资金额小于单笔最小投资金额:￥"
								+ df.format(borrow.getLowestAccount()) + "!";
					}
					if (b_money > borrow.getMostAccount()) {
						return "组合标中" + borrow.getName() + "投资金额大于单笔最大投资金额:￥"
								+ df.format(borrow.getMostAccount()) + "!";
					}
					if (b_money + lockBorrowMoney + borrow.getAccountYes() > borrow
							.getAccount() && 0 != borrow.getAccount()) {
						return "组合标中" + borrow.getName() + "超过此项目最大接受金额!";
					}
				}
			}
		}
		return "success";
	}

	/**
	 * 最小起投金额
	 * 
	 * @param productBasic
	 * @return Double
	 */
	private Double getMinMoney(ProductBasic productBasic) {
		Double minMoney = new Double(0.0);

		List<ProductSet> prodSetList = productSetService
				.getProdSetList(productBasic.getId());
		for (int j = 0; j < prodSetList.size(); j++) {
			ProductBasic pojo = productBasicDao.find(prodSetList.get(j)
					.getSubProductId());

			if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(pojo
					.getProductType().getTypeCategory())) {
				Ppfund ppfund = thePpfundService.getById(pojo.getRelatedId());

				if (ppfund.getLowestAccount() / prodSetList.get(j).getRate()
						* 100 > minMoney) {
					minMoney = ppfund.getLowestAccount()
							/ prodSetList.get(j).getRate() * 100;
				}
			} else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW.equals(pojo
					.getProductType().getTypeCategory())) {
				Borrow borrow = theBorrowService.getById(pojo.getRelatedId());

				if (borrow.getLowestAccount() / prodSetList.get(j).getRate()
						* 100 > minMoney) {
					minMoney = borrow.getLowestAccount()
							/ prodSetList.get(j).getRate() * 100;
				}
			}
		}
		return minMoney;
	}

	/**
	 * 判断标是否合法
	 * 
	 * @param productBasic
	 * @param minMoney
	 * @return Boolean
	 */
	private Boolean checkInvestStatus(ProductBasic productBasic, double minMoney) {
		List<ProductSet> prodSetList = productSetService
				.getProdSetList(productBasic.getId());
		for (int j = 0; j < prodSetList.size(); j++) {
			ProductBasic pojo = productBasicDao.find(prodSetList.get(j)
					.getSubProductId());
			if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(pojo
					.getProductType().getTypeCategory())) {
				Double pMoney = minMoney * prodSetList.get(j).getRate() / 100;
				Ppfund ppfund = thePpfundService.getById(pojo.getRelatedId());
				Double lockPpfundMoney = frozenProductService
						.getLockMoney((int) ppfund.getId());

				if (pMoney + lockPpfundMoney + ppfund.getAccountYes() > ppfund
						.getMostAccountTotal()
						&& 0 != ppfund.getMostAccountTotal()) {
					return false;
				}
			} else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW.equals(pojo
					.getProductType().getTypeCategory())) {
				Double bMoney = minMoney * prodSetList.get(j).getRate() / 100;
				Borrow borrow = theBorrowService.getById(pojo.getRelatedId());
				Double lockBorrowMoney = frozenProductService
						.getLockMoney((int) borrow.getId());

				if (bMoney + lockBorrowMoney + borrow.getAccountYes() > borrow
						.getMostAccount() && 0 != borrow.getMostAccount()) {
					return false;
				}
			}
		}
		return true;
	}

	private void setDescription(ProductBasicModel model) {
		// 获取产品类型的文字描述
		model.setTypeDesc(model.getProductType().getTypeName());

		// 获取产品的状态
		int prodStatus = model.getStatus();
		if (ProductStatusConstant.STATUS_WAITING_FOR_APPROVE == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_WAITING_FOR_APPROVE);
		} else if (ProductStatusConstant.STATUS_APPROVED == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_APPROVED);
		} else if (ProductStatusConstant.STATUS_FAIL == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_FAIL);
		} else if (ProductStatusConstant.STATUS_RECHECK_PASS == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_RECHECK_PASS);
		} else if (ProductStatusConstant.STATUS_RECHECK_FAIL == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_RECHECK_FAIL);
		} else if (ProductStatusConstant.STATUS_AUTO_CANCEL == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_AUTO_CANCEL);
		} else if (ProductStatusConstant.STATUS_REPAYMENT_START == prodStatus) {
			model.setStatusDesc(ProductStatusConstant.DESC_REPAYMENT_START);
		} else {
			model.setStatusDesc(ProductStatusConstant.DESC_UNRELATED);
		}

		// 现有对象转JSON存在问题：
		// 如果集合对象中某个对象的值在每个元素中相同，第三方库会为了节省网络开销，把后续重复的对象值指向第一个值的引用。
		// 生成一段类似于 $ref:"$.data.list[0]" 的JSON，导致前端取不到对应的值
		//
		// 解决办法：将主要信息重新塞给Model对象
		model.setFlagId(model.getProductTypeFlag().getId());
		model.setFlagDesc(model.getProductTypeFlag().getFlagName());
	}

	/**
	 * 校验用户注册时间
	 * 
	 * @param user
	 * @return boolean
	 */
	private boolean isNewUser(User user) {
		Date userDate = user.getAddTime();
		String myString = ConstantUtil.USER_ADD_TIME;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Date d;
		boolean flag = false;
		try {
			d = sdf.parse(myString);
			flag = d.before(userDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<ProductBasic> getAllProductListForCouponCategory() {
		QueryParam param = QueryParam.getInstance();

		SearchFilter waitingForApprove = new SearchFilter("status",
				ProductStatusConstant.STATUS_WAITING_FOR_APPROVE);
		SearchFilter approved = new SearchFilter("status",
				ProductStatusConstant.STATUS_APPROVED);
		SearchFilter recheckPass = new SearchFilter("status",
				ProductStatusConstant.STATUS_RECHECK_PASS);
		param.addOrFilter(waitingForApprove, approved, recheckPass);

		return productBasicDao.findByCriteria(param);
	}

	// TODO 以下为组合接口（待删）

	@Override
	public List<ProductBasicModel> getModelListForProductSet() {
		QueryParam param1 = QueryParam.getInstance();

		param1.addOrFilter(new SearchFilter("status",
				ProductStatusConstant.STATUS_APPROVED), new SearchFilter(
				"status", ProductStatusConstant.STATUS_RECHECK_PASS),
				new SearchFilter("status",
						ProductStatusConstant.STATUS_REPAYMENT_START));

		SearchFilter orFilter1 = new SearchFilter("productType.typeCategory",
				ProductTypeConstant.PRODUCT_CATEGORY__PPFUND);
		SearchFilter orFilter2 = new SearchFilter("productType.typeCategory",
				ProductTypeConstant.PRODUCT_CATEGORY__BORROW);
		param1.addOrFilter(orFilter1, orFilter2);
		param1.addOrder(OrderType.DESC, "id");
		List<ProductBasic> prodList = productBasicDao.findByCriteria(param1);

		List<ProductBasicModel> list = new ArrayList<ProductBasicModel>();
		for (ProductBasic basic : prodList) {
			ProductBasicModel model = ProductBasicModel.instance(basic);

			if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW
					.equalsIgnoreCase(model.getProductType().getTypeCategory())) {
				// 非现金类产品
				model.setBorrowModel(BorrowModel.instance(theBorrowService
						.getById(model.getRelatedId())));
			} else if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND
					.equalsIgnoreCase(model.getProductType().getTypeCategory())) {
				// 现金类产品
				model.setPpfundModel(PpfundModel.instance(thePpfundService
						.getById(model.getRelatedId())));
			}
			setDescription(model);

			list.add(model);
		}
		return list;
	}

	@Override
	public List<ProductBasicModel> getModelCrowdfundingByStatus(int status,
			String platform) {
		QueryParam param = QueryParam.getInstance();
		if (ProductStatusConstant.STATUS_UNRELATED != status) {
			param.addParam("status", status);
		}
		if (Constant.COOPERATE_TYPE__PC.equals(platform)) {
			param.addParam("showForPc", 1);
		}
		if (Constant.COOPERATE_TYPE__WECHAT.equals(platform)) {
			param.addParam("showForWechat", 1);
		}
		// typeId小于800的为非组合产品
		// typeId大于300的为众筹产品
		param.addParam("typeId", Operators.GT, 300).addParam("typeId",
				Operators.LT, 800);
		param.addOrder(OrderType.DESC, "id");
		List<ProductBasic> prodList = productBasicDao.findByCriteria(param);

		List<ProductBasicModel> list = new ArrayList<ProductBasicModel>();
		for (ProductBasic basic : prodList) {
			ProductBasicModel model = ProductBasicModel.instance(basic);

			setDescription(model);
			list.add(model);
		}
		return list;
	}

	@Override
	public List<ProductBasicModel> getModelSetByStatus(int status,
			String platform) {
		QueryParam param = QueryParam.getInstance();
		if (ProductStatusConstant.STATUS_UNRELATED != status) {
			param.addParam("status", status);
		}
		if (Constant.COOPERATE_TYPE__PC.equals(platform)) {
			param.addParam("showForPc", 1);
		}
		if (Constant.COOPERATE_TYPE__WECHAT.equals(platform)) {
			param.addParam("showForWechat", 1);
		}
		// typeId大于800的为组合产品
		param.addParam("typeId", Operators.GT, 800);
		param.addOrder(OrderType.DESC, "id");
		List<ProductBasic> prodList = productBasicDao.findByCriteria(param);

		List<ProductBasicModel> list = new ArrayList<ProductBasicModel>();
		List<ProductBasicModel> subList = null;

		for (ProductBasic basic : prodList) {
			// 取出组合产品下的所有子产品
			List<ProductSet> prodSetList = productSetService
					.getProdSetList(basic.getId());
			subList = new ArrayList<ProductBasicModel>();

			for (ProductSet prodSet : prodSetList) {
				ProductBasicModel subModel = ProductBasicModel
						.instance(productBasicDao.find(prodSet
								.getSubProductId()));
				setDescription(subModel);
				// 子产品分配比例
				subModel.setRate(prodSet.getRate());
				subList.add(subModel);
			}
			ProductBasicModel model = ProductBasicModel.instance(basic);
			model.setSubProductList(subList);
			setDescription(model);

			list.add(model);
		}
		return list;
	}

	@Override
	public PageDataList<ProductBasicModel> getModelSetPageListByStatus(
			int status, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		if (ProductStatusConstant.STATUS_UNRELATED != status) {
			param.addParam("status", status);
		}
		param.addParam("productType.typeCategory",
				ProductTypeConstant.PRODUCT_CATEGORY__SET);
		param.addOrder(OrderType.DESC, "id");
		PageDataList<ProductBasic> prodPageList = productBasicDao
				.findPageList(param);

		List<ProductBasicModel> list = new ArrayList<ProductBasicModel>();
		List<ProductBasicModel> subList = null;

		for (ProductBasic basic : prodPageList.getList()) {
			// 取出组合产品下的所有子产品
			List<ProductSet> prodSetList = productSetService
					.getProdSetList(basic.getId());
			subList = new ArrayList<ProductBasicModel>();

			for (ProductSet prodSet : prodSetList) {
				ProductBasicModel subModel = ProductBasicModel
						.instance(productBasicDao.find(prodSet
								.getSubProductId()));
				setDescription(subModel);
				// 子产品分配比例
				subModel.setRate(prodSet.getRate());
				subList.add(subModel);
			}
			ProductBasicModel model = ProductBasicModel.instance(basic);
			model.setSubProductList(subList);
			setDescription(model);

			list.add(model);
		}

		// 封装成带分页的集合
		PageDataList<ProductBasicModel> pageList = new PageDataList<ProductBasicModel>();
		pageList.setPage(prodPageList.getPage());
		pageList.setList(list);

		return pageList;
	}

	@Override
	public ProductBasicModel getModelSetById(Long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		ProductBasic prod = productBasicDao.findByCriteriaForUnique(param);

		List<ProductBasicModel> subList = null;

		// 取出组合产品下的所有子产品
		List<ProductSet> prodSetList = productSetService.getProdSetList(prod
				.getId());
		subList = new ArrayList<ProductBasicModel>();

		for (ProductSet prodSet : prodSetList) {
			ProductBasicModel subModel = ProductBasicModel
					.instance(productBasicDao.find(prodSet.getSubProductId()));
			setDescription(subModel);
			// 子产品分配比例
			subModel.setRate(prodSet.getRate());
			subList.add(subModel);
		}
		ProductBasicModel model = ProductBasicModel.instance(prod);
		model.setSubProductList(subList);
		setDescription(model);

		return model;
	}

	// TODO 以下为众筹接口（待删）

	@Override
	public ProductBasic getInfoForCrowdfunding(ProjectBaseinfo baseinfo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("productType.id", baseinfo.getType());
		param.addParam("relatedId", baseinfo.getId());
		return productBasicDao.findByCriteriaForUnique(param);
	}

}
