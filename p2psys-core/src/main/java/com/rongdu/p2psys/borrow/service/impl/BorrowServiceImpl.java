package com.rongdu.p2psys.borrow.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.borrow.dao.BorrowAppointmentBidDao;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowMortgageDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.dao.BorrowUploadDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAppointmentBid;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.dao.ExchangeRatePacketCaptureDao;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.model.ExchangeRatePacketCaptureModel;
import com.rongdu.p2psys.core.rule.IndexRuleCheck;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.ExchangeRatePacketCaptureUtil;
import com.rongdu.p2psys.nb.product.dao.ProductBasicDao;
import com.rongdu.p2psys.nb.product.dao.ProductMaterialsDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.dao.UserCreditLogDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserCreditLog;
import com.rongdu.p2psys.user.exception.UserException;

@Service("borrowService")
public class BorrowServiceImpl implements BorrowService {
	Logger logger = Logger.getLogger(BorrowServiceImpl.class);

	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductTypeService productTypeService;

	@Resource
	private UserCreditDao userCreditDao;
	@Resource
	private UserCreditLogDao userCreditLogDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private BorrowRepaymentDao borrowRepaymentDao;
	@Resource
	private BorrowTenderDao borrowTenderDao;
	@Resource
	private BorrowUploadDao borrowUploadDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
	private BorrowMortgageDao borrowMortgageDao;
	@Resource
	private AutoBorrowService autoBorrowService;
	@Resource
	private ChinapnrService chinapnrService;
	@Resource
	private ExchangeRatePacketCaptureDao exchangeRatePacketCaptureDao;
	@Resource
	private BorrowCollectionDao borrowCollectionDao;
	@Resource
	private BorrowAppointmentBidDao borrowAppointmentBidDao;
	@Resource
	private UserDao userDao;
	@Resource
	private ProductBasicDao productBasicDao;
	@Resource
	private ProductMaterialsDao productMaterialsDao;
	@Resource
	private ProductMaterialsService productMaterialsService;
	@Resource
	private ProductBasicService productBasicService;

	@Resource
	private BorrowDao borrowDao;

	@Override
	public Borrow save(BorrowModel model, User user) {
		Borrow borrow = model.prototype();

		// 校验
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.checkModelData();
		// worker.setBorrowField(user);

		// 保存标信息
		// 老结构 - Borrow
		Borrow addBorrow = borrowDao.save(borrow);
		// 新结构 - ProductBasic
		ProductBasic prod = ProductBasicModel.transBorrow(addBorrow);
		prod.setProductTypeFlag(productTypeFlagService.findById(model
				.getFlagId()));
		prod.setProductType(productTypeService.findById(Long.valueOf(model
				.getType())));
		prod.setRecommendTime(new Date());

		productBasicDao.save(prod);

		// 发送通知
		Global.setTransfer("user", user);
		Global.setTransfer("borrow", addBorrow);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("publishBorrowNoticeExecuter");
		executer.execute(0, user);

		return addBorrow;
	}

	@Override
	public void updateLoan(BorrowModel model, Borrow oldBorrow,
			List<BorrowUpload> list, long[] delIds) {
		BorrowWorker worker = BorrowHelper.getWorker(model.prototype());
		worker.checkModelData();
		// worker.setBorrowField(oldBorrow.getUser());
		Borrow b = worker.prototype();
		b.setId(oldBorrow.getId());
		b.setBorrowTimeType(oldBorrow.getBorrowTimeType());
		b.setTimeLimit(model.getTimeLimit());

		// 对上传图片做物理删除
		if (null != delIds) {
			for (long id : delIds) {
				BorrowUpload bu = borrowUploadDao.find(id);
				borrowUploadDao.delete(id);
				String realPath = ServletActionContext.getServletContext()
						.getRealPath(bu.getPicPath());
				new File(realPath).delete();
			}
		}

		Borrow updatedBorrow = borrowDao.update(b);
		borrowUploadDao.save(list);

		// 保存标信息 - 新结构 - ProductBasic
		ProductBasic oldProd = productBasicService.getProductBasicInfo(
				new Long(updatedBorrow.getType()), updatedBorrow.getId());
		ProductBasic newProd = ProductBasicModel.transBorrow(updatedBorrow);
		newProd.setId(oldProd.getId());
		newProd.setProductTypeFlag(oldProd.getProductTypeFlag());
		newProd.setProductType(oldProd.getProductType());
		// newProd.setShowOrder(model.getShowOrder());
		newProd = productBasicDao.update(newProd);

	}

	@Override
	public void verify(BorrowModel model, Operator operator) {
		Borrow borrow = borrowDao.find(model.getId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);

		int status = 0;
		if (borrow.getType() != Borrow.TYPE_ENTRUST) {
			Global.setTransfer("user", borrow.getUser());
		} else {
			Global.setTransfer("user", userDao.find(1L));
		}
		Global.setTransfer("borrow", borrow);

		// 初审不通过
		if (model.getStatus() == 0) {
			status = -1;

			// 更新标状态为：初审不通过（rd_borrow）
			borrowDao
					.updateStatus(borrow.getId(), Borrow.STATUS_TRIAL_PASSLESS);
			// 更新标状态为：初审不通过（nb_product_basic）
			ProductBasic prod = productBasicService.getProductBasicInfo(
					new Long(borrow.getType()), borrow.getId());
			prod.setStatus(ProductStatusConstant.STATUS_FAIL);
			productBasicDao.update(prod);

			// 发送消息通知
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("borrowVerifyFailNoticeExecuter");
			if (borrow.getType() != Borrow.TYPE_ENTRUST) {
				executer.execute(0, borrow.getUser());
			} else {
				executer.execute(0, userDao.find(1L));
			}
		}
		// 初审通过
		else if (model.getStatus() == 1) {
			status = 1;
			worker.handleBorrowBeforePublish(borrow);
			if (model.getFixedTime() == null) {
				model.setFixedTime(new Date());
			}
			// 更新标状态为：初审通过（rd_borrow）
			model.setExpirationTime(new Date(model.getFixedTime().getTime()
					+ Long.valueOf(borrow.getValidTime()) * 24 * 60 * 60 * 1000));
			borrowDao.updatefixedTime(borrow.getId(), model);
			// 更新标状态为：初审通过（nb_product_basic）
			ProductBasic prod = productBasicService.getProductBasicInfo(
					new Long(borrow.getType()), borrow.getId());
			prod.setStatus(ProductStatusConstant.STATUS_APPROVED);
			productBasicDao.update(prod);
		}
		VerifyLog verifyLog = new VerifyLog(operator, "borrow", borrow.getId(),
				1, status, model.getRemark());
		verifyLogDao.save(verifyLog);
	}

	@Override
	public void update(Borrow borrow) {
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.handleBorrowAfterPublish(borrow);
		borrowDao.update(borrow);
	}

	@Override
	public PageDataList<BorrowModel> list(BorrowModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (model.getUser() != null && model.getUser().getUserId() > 0) {
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if (model.getStatus() != 99) {
				switch (model.getStatus()) {
				case 1:
					param.addParam("status", 1);
					if (model.getScales() == 100) {
						param.addParam("accountYes", Operators.PROPERTY_EQ,
								"account");
					} else {
						param.addParam("accountYes", Operators.PROPERTY_NOTEQ,
								"account");
					}
					break;
				case 19:
					param.addParam("status", 1);
					param.addParam("accountYes", Operators.PROPERTY_EQ,
							"account");
					break;
				case 49:
					SearchFilter orFilter1 = new SearchFilter("status",
							Operators.EQ, 4);
					SearchFilter orFilter2 = new SearchFilter("status",
							Operators.EQ, 49);
					param.addOrFilter(orFilter1, orFilter2);
					break;
				case 59:
					SearchFilter orFilter3 = new SearchFilter("status",
							Operators.EQ, 5);
					SearchFilter orFilter4 = new SearchFilter("status",
							Operators.EQ, 59);
					param.addOrFilter(orFilter3, orFilter4);
					break;
				default:
					param.addParam("status", model.getStatus());
					break;
				}
			}
			if (model.getScales() == 99) {
				param.addParam("account", Operators.PROPERTY_GT, "accountYes");
				// param.addParam("type", Operators.NOTEQ, 110); 删除对流转标的限制
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ,
						model.getUserName());
			}
			Date d = DateUtil.getDate(System.currentTimeMillis() / 1000 + "");
			if (model.getTime() == 7) {
				param.addParam("addTime", Operators.GTE,
						DateUtil.rollDay(d, -7));
				param.addParam("addTime", Operators.LTE, d);
			} else if (model.getTime() > 0 && model.getTime() < 4) {
				param.addParam("addTime", Operators.GTE,
						DateUtil.rollMon(d, -model.getTime()));
				param.addParam("addTime", Operators.LTE, d);
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime()
						+ " 00:00:00");
				param.addParam("addTime", Operators.GT, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			if (model.getType() != 100 && model.getType() != 0) {
				param.addParam("type", model.getType());
			}
			searchParam(param, model);
			param.addPage(model.getPage(), model.getSize());
		}
		PageDataList<Borrow> pageDataList = borrowDao.findPageList(param);
		PageDataList<BorrowModel> pageDataList_ = new PageDataList<BorrowModel>();
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		pageDataList_.setPage(pageDataList.getPage());
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");

		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Borrow borrow = (Borrow) pageDataList.getList().get(i);
				BorrowModel bm = BorrowModel.instance(borrow);
				if (borrow.getUser() != null) {
					bm.setUserName(borrow.getUser().getUserName());
					bm.setUserId(borrow.getUser().getUserId());
				} else {
					if (borrow.getType() == Borrow.TYPE_ENTRUST) {
						bm.setUserName("平台");
					} else {
						bm.setUserName("<font color='red'>该用户被删除</font>");
					}
					bm.setUserId(0);
				}
				if (borrow.getRegisterTime() != null
						&& DateUtil.getNowTime() > DateUtil.rollMinute(
								borrow.getRegisterTime(), 35).getTime() / 1000) {
					bm.setIsShowRegister(1);
				}
				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
				if (log != null) {
					Date d = log.getTime(); // 初审时间
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					c.add(Calendar.DATE, borrow.getValidTime());
					Calendar now = Calendar.getInstance();
					now.setTime(new Date());
					bm.setFlow(c.before(now));
					bm.setStartTime(DateUtil.dateStr2(log.getTime()));
				}
				bm.setUser(null);
				list.add(bm);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	/**
	 * 前台分页
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public PageDataList<BorrowModel> getList(BorrowModel model) {
		PageDataList<Borrow> pageDataList = borrowDao.getList(model);
		PageDataList<BorrowModel> pageDataList_ = new PageDataList<BorrowModel>();
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		pageDataList_.setPage(pageDataList.getPage());
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");

		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Borrow borrow = (Borrow) pageDataList.getList().get(i);
				BorrowModel bm = BorrowModel.instance(borrow);
				// 获取借款标头像
				List<BorrowUpload> borrowImgs = borrowUploadDao
						.findByBorrowIdAndType(borrow.getId(), 5);
				if (borrowImgs != null && borrowImgs.size() > 0) {
					bm.setBorrowImg(borrowImgs.get(0).getPicPath());
				}
				int tenderCount = borrowTenderDao
						.getTenderCountByBorrow(borrow);
				bm.setTenderCount(tenderCount);
				try {
					if (model.getScales() != 101) {
						bm.setUserName(borrow.getUser().getUserName());
						bm.setRealName(borrow.getUser().getRealName());
						bm.setUserId(borrow.getUser().getUserId());
					}
				} catch (Exception e) {
					bm.setUserName("<font color='red'>该用户被删除</font>");
					bm.setUserId(0);
				}

				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
				if (log != null) {
					Date d = log.getTime(); // 初审时间
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					c.add(Calendar.DATE, borrow.getValidTime());
					Calendar now = Calendar.getInstance();
					now.setTime(new Date());
					bm.setFlow(c.before(now));
					bm.setStartTime(DateUtil.dateStr4(log.getTime()));
					// RedPacket redPacket = borrow.getRedPacket();
					// if(redPacket != null && borrow.getRedPacket().getId() >
					// 0) {
					// bm.setRedPacketName(redPacket.getName());
					// }
				}
				bm.setBorrowId(borrow.getId());
				bm.setUser(null);
				list.add(bm);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public void cancel(Borrow borrow) throws Exception {
		if (borrow == null) {
			throw new BorrowException("借款标的状态异常！");
		}
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.revokeBorrow();
		borrow.setRepaymentYesAccount(-borrow.getRepaymentYesAccount());
		borrow.setRepaymentYesInterest(-borrow.getRepaymentYesInterest());
		if (borrow.getRepaymentAccount() == -1) {
			borrow.setStatus(Borrow.STATUS_USER_CANCEL);
			borrow.setRepaymentAccount(0);
			borrowDao.modifyBorrowAndRepay(borrow);
			Global.setTransfer("user", borrow.getUser());
			Global.setTransfer("borrow", borrow);
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("borrowCancelNoticeExcuter");
			executer.execute(0, borrow.getUser());
		} else {
			borrow.setStatus(Borrow.STATUS_MANAGER_CANCEL);
			borrowDao.modifyBorrowAndRepay(borrow);
			ConcurrentUtil.autoCancel(borrow);
		}
		List<BorrowTender> tenderList = borrowTenderDao.findByProperty(
				"borrow.id", borrow.getId());
		if (tenderList.size() > 0) {
			borrowTenderDao.updateStatus(borrow.getId(), 2, 0);
		}
	}

	@Override
	public void borrowCancel(Borrow borrow) throws Exception {
		if (borrow.getStatus() != 9) {
			throw new BorrowException("标状态异常，请刷新后操作", 1);
		}
		borrowDao.updateStatus(borrow.getId(), 5, 9);
	}

	@Override
	public void doRepay(BorrowRepayment borrowRepayment) {
		/*
		 * Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());
		 * if(borrow.getType() != Borrow.TYPE_ENTRUST){ Account account =
		 * accountDao.findObjByProperty("user.userId",
		 * borrow.getUser().getUserId()); BorrowWorker worker =
		 * BorrowHelper.getWorker(borrow);
		 * worker.validBeforeRepayment(borrowRepayment, account); }
		 */
		if (borrowRepayment == null || borrowRepayment.getStatus() == 1) {
			throw new BorrowException("该期借款已经还款,请不要重复操作！", 1);
		}
		if (borrowRepayment.getBorrow().getStatus() != 6
				&& borrowRepayment.getBorrow().getStatus() != 7) {
			throw new BorrowException("当前借款标的状态不能进行还款操作！", 1);
		}
		boolean hasAhead = borrowRepaymentDao.hasRepaymentAhead(borrowRepayment
				.getPeriod(), borrowRepayment.getBorrow().getId());
		if (hasAhead) {
			throw new BorrowException("还有尚未还款的借款！", 1);
		}
		// 设置webstatus=1
		borrowRepayment.setWebStatus(1);
		borrowRepayment = borrowRepaymentDao.update(borrowRepayment);

		ConcurrentUtil.repay(borrowRepayment);
//		autoBorrowService.autoBorrowRepay(borrowRepayment);
	}
	
	@Override
	public void doVipRepay() {
		ConcurrentUtil.vipRepay();
	}
	
	@Override
	public void doPriorRepay(BorrowRepayment borrowRepayment) {
		Borrow borrow = borrowRepayment.getBorrow();
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		if (worker.isLastPeriod(borrowRepayment.getPeriod())) {
			throw new BussinessException("你正在操作的提前还款为最后一期还款，不能进行提前还款操作！");
		}
		if (borrowRepayment == null || borrowRepayment.getStatus() == 1
				|| borrowRepayment.getWebStatus() == 1) {
			throw new BorrowException("该期借款已经还款,请不要重复操作！", 1);
		}
		if (borrow.getStatus() != 6 && borrow.getStatus() != 7) {
			throw new BorrowException("当前借款标的状态不能进行还款操作！", 1);
		}
		boolean hasAhead = borrowRepaymentDao.hasRepaymentAhead(borrowRepayment
				.getPeriod(), borrowRepayment.getBorrow().getId());
		if (hasAhead) {
			throw new BorrowException("还有尚未还款的借款！", 1);
		}
		// 设置webstatus=1
		borrowRepayment.setWebStatus(1);
		borrowRepayment = borrowRepaymentDao.update(borrowRepayment);
		User user = borrowRepayment.getBorrow().getUser();
		Account act = accountDao.findObjByProperty("user.userId",
				user.getUserId());
		double waitRemainderRepayCapital = borrowRepaymentDao
				.getRemainderCapital(borrow.getId()); // 计算剩余待还本金
		double waitRemainderRepayInterest = borrowRepaymentDao
				.getwaitRpayInterest(borrow.getId(),
						borrowRepayment.getPeriod()); // 本次提前还款待还利息总和
		double waitRepayAccount = waitRemainderRepayCapital
				+ waitRemainderRepayInterest;
		if (act.getUseMoney() < waitRepayAccount) {
			throw new BussinessException("您账户的可用余额为：" + act.getUseMoney()
					+ ",小于提前还款总额：" + waitRepayAccount + "，请充值再进行提前还款操作");
		}

		// 还款不需要提前冻结资金，所以注释掉。by zhangyz
		/*
		 * double freezeVal = waitRepayAccount; Global.setTransfer("money",
		 * freezeVal); Global.setTransfer("borrow", borrow);
		 * Global.setTransfer("repay", borrowRepayment); AbstractExecuter
		 * executer = ExecuterHelper.doExecuter("borrowRepayFreezeExecuter");
		 * executer.execute(freezeVal, borrow.getUser());
		 */

		ConcurrentUtil.doPriorRepay(borrowRepayment);
	}

	public List<BorrowModel> getBorrowList() {
		BorrowModel model = new BorrowModel();
		IndexRuleCheck indexRule = (IndexRuleCheck) Global
				.getRuleCheck(EnumRuleNid.INDEX.getValue());
		model.setStatus(indexRule.borrow.status_type);
		List<BorrowModel> indexList = new ArrayList<BorrowModel>();
		// 组合启用，指定标种显示
		List<Integer> borrow_type = indexRule.borrow.borrow_type;
		// 组合启用，指定标种显示的个数
		List<Integer> borrow_num = indexRule.borrow.borrow_num;
		// 是否组合 或者全表排序
		if (indexRule.borrow.display == 1) {
			List<BorrowModel> bList = new ArrayList<BorrowModel>();
			for (int i = 0; i < borrow_type.size(); i++) {
				int type = (Integer) borrow_type.get(i); // 标种类型
				model.setType(type);
				Borrow borrow = BorrowHelper.getWorker(model).prototype();
				model = BorrowModel.instanceCurr(borrow, model);
				model.setSize(borrow_num.get(i));
				bList = borrowDao.getIndexList(model);
				indexList.addAll(bList);
			}
		} else { // 按照指定排序规则读取
			List<BorrowModel> bList = new ArrayList<BorrowModel>();
			bList = borrowDao.getIndexList(model);
			indexList = bList;
		}
		return indexList;
	}

	public int unfinshJinBorrowCount(long userId) {
		return borrowDao.unfinshJinBorrowCount(userId);
	}

	@Override
	public double getRepayTotalWithJin(long userId) {
		return borrowDao.getRepayTotalWithJin(userId);
	}

	@Override
	public Borrow find(long id) {
		return borrowDao.find(id);
	}

	@Override
	public void addBorrow(Borrow b) {
		borrowDao.update(b);
	}

	@Override
	public void verifyFull(BorrowModel model, Operator operator,
			String cashPurchasePrice) throws Exception {
		Borrow borrow = borrowDao.find(model.getId());
		borrow.setReviewTime(new Date());
		int status = 0;
		if (model.getStatus() == 0) {
			// 审核不通过
			status = -1;
			borrowDao.updateStatus(borrow.getId(),
					Borrow.STATUS_RECHECK_PASSLESS, Borrow.STATUS_TRIAL_PASSED);
			// 审核不通过（nb_product_basic）
			ProductBasic prod = productBasicService.getProductBasicInfo(
					new Long(borrow.getType()), borrow.getId());
			prod.setStatus(ProductStatusConstant.STATUS_RECHECK_FAIL);
			productBasicDao.update(prod);

			model = BorrowModel.instance(borrow);
			ConcurrentUtil.autoVerifyFullFail(model);
		} else if (model.getStatus() == 1) {
			// 审核通过
			status = 1;
			borrowDao.updateStatus(borrow.getId(), Borrow.STATUS_RECHECK_PASS,
					Borrow.STATUS_TRIAL_PASSED);
			// 审核不通过（nb_product_basic）
			ProductBasic prod = productBasicService.getProductBasicInfo(
					new Long(borrow.getType()), borrow.getId());
			prod.setStatus(ProductStatusConstant.STATUS_RECHECK_PASS);
			productBasicDao.update(prod);

			if (!StringUtil.isBlank(cashPurchasePrice)) {
				ExchangeRatePacketCapture erpc = new ExchangeRatePacketCapture();
				erpc.setBorrow(borrow);
				erpc.setCashPurchasePrice(Double.parseDouble(cashPurchasePrice));
				erpc.setAddTime(new Date());
				exchangeRatePacketCaptureDao.save(erpc);
			}
			model = BorrowModel.instance(borrow);
			ConcurrentUtil.autoVerifyFullSuccess(model);
		}
		VerifyLog verifyLog = new VerifyLog(operator, "borrow", borrow.getId(),
				2, status, model.getRemark());
		verifyLogDao.save(verifyLog);
	}

	@Override
	public void updateStatus(long id, int status, int preStatus) {
		borrowDao.updateStatus(id, status, preStatus);
	}

	@Override
	public Borrow getBorrowById(long id) {
		return borrowDao.find(id);
	}

	@Override
	public int trialCount(int status) {
		return borrowDao.trialCount(status);
	}

	@Override
	public int fullCount(int status) {
		return borrowDao.fullCount(status);
	}

	@Override
	public Object[] countByFinish() {
		return borrowDao.countByFinish();
	}

	@Override
	public void stopBorrow(Borrow borrow) {
		borrow.setScales(-1);
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.stopBorrow();
		worker.skipReview();
		borrowDao.update(worker.prototype());
	}

	@Override
	public List<BorrowModel> spreadBorrowList(BorrowModel model) {
		return borrowDao.spreadBorrowList(model);
	}

	private QueryParam searchParam(QueryParam param, BorrowModel model) {
		// 利率
		if (model.getAprSearch() != -1) {
			switch (model.getAprSearch()) {
			case 1:
				param.addParam("apr", Operators.LTE, 6);
				break;
			case 2:
				param.addAddFilter("apr", Operators.GT, 6, "apr",
						Operators.LTE, 12);
				break;
			case 3:
				param.addAddFilter("apr", Operators.GT, 12, "apr",
						Operators.LTE, 18);
				break;
			case 4:
				param.addAddFilter("apr", Operators.GT, 18, "apr",
						Operators.LTE, 20);
				break;
			case 5:
				param.addParam("apr", Operators.GT, 20);
				break;
			default:
				break;
			}
		}
		// 金额
		if (model.getMoneySearch() != -1) {
			switch (model.getMoneySearch()) {
			case 2:
				param.addParam("account", Operators.LTE, 10000);
				break;
			case 3:
				param.addAddFilter("account", Operators.GT, 10000, "account",
						Operators.LTE, 50000);
				break;
			case 4:
				param.addAddFilter("account", Operators.GT, 50000, "account",
						Operators.LTE, 100000);
				break;
			case 5:
				param.addAddFilter("account", Operators.GT, 100000, "account",
						Operators.LTE, 200000);
				break;
			case 6:
				param.addAddFilter("account", Operators.GT, 200000, "account",
						Operators.LTE, 500000);
				break;
			case 7:
				param.addParam("account", Operators.GT, 500000);
				break;
			default:
				break;
			}
		}
		// 期限
		if (model.getTimeSearch() != -1) {
			switch (model.getTimeSearch()) {
			case 2:
				SearchFilter dayFilter = new SearchFilter("borrowTimeType", 1);
				SearchFilter monthFilter = new SearchFilter("timeLimit",
						Operators.LTE, 1);
				param.addOrFilter(dayFilter, monthFilter);
				break;
			case 3:
				param.addAddFilter("timeLimit", Operators.GT, 1, "timeLimit",
						Operators.LTE, 3);
				break;
			case 4:
				param.addAddFilter("timeLimit", Operators.GT, 3, "timeLimit",
						Operators.LTE, 6);
				break;
			case 5:
				param.addAddFilter("timeLimit", Operators.GT, 6, "timeLimit",
						Operators.LTE, 12);
				break;
			case 6:
				param.addParam("timeLimit", Operators.GT, 12);
				break;
			default:
				break;
			}
		}

		// 排序 1金额 2利率 3进度 4信用
		switch (model.getOrder()) {
		case -1:
			param.addOrder(OrderType.ASC, "account");
			break;
		case 1:
			param.addOrder(OrderType.DESC, "account");
			break;
		case -2:
			param.addOrder(OrderType.ASC, "apr");
			break;
		case 2:
			param.addOrder(OrderType.DESC, "apr");
			break;
		case -3:
			param.addOrder(OrderType.ASC, "scales");
			break;
		case 3:
			param.addOrder(OrderType.DESC, "scales");
			break;
		case -4:
			break;
		case 4:
			break;
		default:
			param.addOrder(OrderType.DESC, "id");
			break;
		}
		return param;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List searchParam(BorrowModel model) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		if (model.getType() != 100 && model.getType() != 0) {
			sb.append(" and borrow.type=?)");
			params.add(model.getType());
		} else {
			sb.append(" or borrow.type =110 and borrow.status = 1 and borrow.scales!=100) ");
		}
		if (StringUtil.isNotBlank(model.getName())) {
			sb.append(" and borrow.name like ?");
			params.add("%" + model.getName() + "%");
		}
		// 利率
		if (model.getAprSearch() != -1) {
			/* params.add(model.getAprSearch()); */
			switch (model.getAprSearch()) {
			case 1:
				sb.append(" and borrow.apr<6");
				break;
			case 2:
				sb.append(" and borrow.apr between 6 and 12");
				break;
			case 3:
				sb.append(" and borrow.apr between 12 and 18");
				break;
			case 4:
				sb.append(" and borrow.apr between 18 and 20");
				break;
			case 5:
				sb.append(" and borrow.apr>20");
				break;
			default:
				break;
			}
		}
		// 金额
		if (model.getMoneySearch() != -1) {
			/* params.add(model.getMoneySearch()); */
			switch (model.getMoneySearch()) {
			case 2:
				sb.append(" and borrow.account<10000");
				break;
			case 3:
				sb.append(" and borrow.account between 10000 and 50000");
				break;
			case 4:
				sb.append(" and borrow.account between 50000 and 100000");
				break;
			case 5:
				sb.append(" and borrow.account between 100000 and 200000");
				break;
			case 6:
				sb.append(" and borrow.account between 200000 and 500000");
				break;
			case 7:
				sb.append(" and borrow.account>500000");
				break;
			default:
				break;
			}
		}
		// 期限
		if (model.getTimeSearch() != -1) {
			/* params.add(model.getTimeSearch()); */
			switch (model.getTimeSearch()) {
			case 2:
				sb.append(" and borrow.time_limit <= 1");
				break;
			case 3:
				sb.append(" and borrow.time_limit between 1 and 3 and borrow_time_type=0");
				break;
			case 4:
				sb.append(" and borrow.time_limit between 3 and 6");
				break;
			case 5:
				sb.append(" and borrow.time_limit between 6 and 12");
				break;
			case 6:
				sb.append(" and borrow.timeLimit>12");
				break;
			default:
				break;
			}
		}
		sb.append(" group by borrow.id");
		// 排序 1金额 2利率 3进度 4信用
		switch (model.getOrder()) {
		case -1:
			sb.append(" order by account");
			break;
		case 1:
			sb.append(" order by account desc");
			break;
		case -2:
			sb.append(" order by apr");
			break;
		case 2:
			sb.append(" order by apr desc");
			break;
		case -3:
			sb.append(" order by scales");
			break;
		case 3:
			sb.append(" order by scales desc");
			break;
		case -4:
			break;
		case 4:
			break;
		default:
			sb.append(" order by id desc");
			break;
		}
		params.add(sb);
		return params;
	}

	/**
	 * 校验用户是否借款人
	 * 
	 * @param borrow
	 * @param user
	 */
	@Override
	public boolean isBorrowUser(long borrowId, long userId) {
		PageDataList<Borrow> borrowList = borrowDao.findAllPageList(QueryParam
				.getInstance().addParam("id", borrowId)
				.addParam("user.userId", userId));
		boolean isBorrowUser = borrowList != null
				&& borrowList.getList() != null
				&& borrowList.getList().size() > 0;
		if (isBorrowUser) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验用户是否投资人
	 * 
	 * @param borrow
	 * @param user
	 */
	@Override
	public boolean isTenderUser(long borrowId, long userId) {
		PageDataList<BorrowTender> tenderList = borrowTenderDao
				.findAllPageList(QueryParam.getInstance()
						.addParam("borrow.id", borrowId)
						.addParam("user.userId", userId).addParam("status", 1));
		boolean isTenderUser = tenderList != null
				&& tenderList.getList() != null
				&& tenderList.getList().size() > 0;
		if (isTenderUser) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public PageDataList<BorrowModel> getInviteList(BorrowModel model) {

		PageDataList<Borrow> pageDataList;
		if (model.getType() != Borrow.TYPE_FLOW) {
			pageDataList = borrowDao.getInviteList(model, searchParam(model));
		} else {
			QueryParam param = QueryParam.getInstance().addPage(
					model.getPage(), model.getSize());
			param.addParam("status", 1);
			param.addParam("scales", Operators.NOTEQ, 100);
			param.addParam("type", Borrow.TYPE_FLOW);
			// if (StringUtil.isNotBlank(model.getName())) {
			// param.addParam("name", Operators.EQ, model.getName());
			// }
			searchParam(param, model);
			pageDataList = borrowDao.findPageList(param);
		}
		List<Borrow> borrows = pageDataList.getList();
		PageDataList<BorrowModel> pageList = new PageDataList<BorrowModel>();
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		if (borrows.size() > 0) {
			for (Borrow borrow : borrows) {
				BorrowModel bm = BorrowModel.instance(borrow);
				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
				if (log != null) {
					bm.setStartTime(DateUtil.dateStr2(log.getTime()));
				}
				list.add(bm);
			}
		}
		pageList.setList(list);
		pageList.setPage(pageDataList.getPage());
		return pageList;
	}

	@Override
	public Borrow findNotFlow(long borrowId) {
		Borrow borrow = borrowDao.find(borrowId);
		try {
			if (borrow.getStatus() == 1) {
				borrow = borrowDao.findNotFlow(borrowId);
			}
		} catch (Exception e) {
			throw new BorrowException("当前借款标不存在或已流标");
		}
		return borrow;
	}

	@Override
	public boolean allowPublish(User user, BorrowModel model) {
		BorrowWorker worker = BorrowHelper.getWorker(model.prototype());
		boolean allowPublish;
		try {
			allowPublish = worker.allowPublish(user);
		} catch (BussinessException e) {
			throw new BorrowException(e.getMessage());
		}
		return allowPublish;
	}

	@Override
	public void overduePayment(BorrowRepayment borrowRepayment) {
		Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());
		Account account = accountDao.findObjByProperty("user.userId", borrow
				.getUser().getUserId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.validBeforeRepayment(borrowRepayment, account);
		borrowRepayment.setWebStatus(1);
		borrowRepayment = borrowRepaymentDao.update(borrowRepayment);
		// 还款冻结资金
		double repayMoney = borrowRepayment.getRepaymentAccount();
		double lateMoney = borrowRepayment.getLateInterest();
		// 定时算展期利息
		double extensionInterest = borrowRepayment.getExtensionInterest();
		double freezeVal = BigDecimalUtil.add(repayMoney, lateMoney,
				extensionInterest);

		Global.setTransfer("money", freezeVal);
		Global.setTransfer("borrow", borrow);
		Global.setTransfer("repay", borrowRepayment);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("borrowRepayFreezeExecuter");
		executer.execute(freezeVal, borrow.getUser());
		ConcurrentUtil.overduePayment(borrowRepayment);
	}

	@Override
	public List<BorrowUpload> findPicByBorrowId(long id) {
		QueryParam param = QueryParam.getInstance().addParam("borrow.id", id);
		return borrowUploadDao.findByCriteria(param);
	}

	@Override
	public List<Borrow> businessBid(User user) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", user.getUserId());
		param.addParam("status", 1);
		param.addOrder(OrderType.DESC, "addTime");
		List<Borrow> list = borrowDao.findByCriteria(param, 0, 3);
		return list;
	}

	@Override
	public List<BorrowModel> businessRepayment(User user) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", user.getUserId());
		SearchFilter filter1 = new SearchFilter("status", Operators.EQ, 6);
		SearchFilter filter2 = new SearchFilter("status", Operators.EQ, 7);
		param.addOrFilter(filter1, filter2);
		param.addOrder(OrderType.DESC, "addTime");
		List<Borrow> list = borrowDao.findByCriteria(param, 0, 4);
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		for (Borrow item : list) {
			BorrowModel model = BorrowModel.instance(item);
			int currPeriod = borrowRepaymentDao.getCurrPeriod(item.getId());
			model.setCurrPeriod(currPeriod);
			modelList.add(model);
		}
		return modelList;
	}

	@Override
	public int findByStatusAndUserId(long userId, int status1, int status2) {
		return borrowDao.findByStatusAndUserId(userId, status1, status2);
	}

	@Override
	public double findAccountTotalByStatus(long userId, int status1, int status2) {
		return borrowDao.findAccountTotalByStatus(userId, status1, status2);
	}

	@Override
	public List<BorrowModel> investList(User user) {
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");
		List<Borrow> itemList = borrowDao.getMemberInvestList(user);
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		if (itemList != null && itemList.size() > 0) {
			for (int i = 0; i < itemList.size(); i++) {
				Borrow borrow = itemList.get(i);
				BorrowModel bm = BorrowModel.instance(borrow);
				try {
					bm.setUserName(borrow.getUser().getUserName());
					bm.setUserId(borrow.getUser().getUserId());
				} catch (Exception e) {
					bm.setUserName("<font color='red'>该用户被删除</font>");
					bm.setUserId(0);
				}

				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
				if (log != null) {
					bm.setStartTime(DateUtil.dateStr4(log.getTime()));
				}
				bm.setUser(null);

				// 对应产品信息
				ProductBasic prod = productBasicService.getProductBasicInfo(
						new Long(borrow.getType()), borrow.getId());
				bm.setFlagId(prod.getProductTypeFlag().getId());

				modelList.add(bm);
			}
		}
		return modelList;
	}

	@Override
	public boolean isExistDealNo(String dealNo) {
		Borrow b = borrowDao.findObjByProperty("dealNo", dealNo);
		if (b == null) {
			return false;
		}
		return true;
	}

	@Override
	public void updatePic(List<BorrowUpload> list, long[] delIds) {
		borrowUploadDao.save(list);
		if (delIds != null) {
			for (long id : delIds) {
				BorrowUpload bu = borrowUploadDao.find(id);
				borrowUploadDao.delete(id);
				String realPath = ServletActionContext.getServletContext()
						.getRealPath(bu.getPicPath());
				new File(realPath).delete();
			}
		}
	}

	@Override
	public Double getBorrowAccountByDate(String date) {

		return borrowDao.getBorrowAccountByDate(date);
	}

	@Override
	public int count() {
		return borrowDao.count();
	}

	@Override
	public int getGuaranteeingCount(long userId) {

		return borrowDao.getGuaranteeingCount(userId);
	}

	@Override
	public double getGuaranteeingAccount(long userId) {

		return borrowDao.getGuaranteeingAccount(userId);
	}

	@Override
	public int getNeedGuaranteeRegisteCount(long userId) {

		return borrowDao.getNeedGuaranteeRegisteCount(userId);
	}

	@Override
	public double getNeedGuaranteeRegisteAccount(long userId) {
		return borrowDao.getNeedGuaranteeRegisteAccount(userId);
	}

	@Override
	public int getOverdueCount(long userId) {
		return borrowRepaymentDao.getOverdueCount(userId);
	}

	@Override
	public List<BorrowModel> getNeedGuaranteeRegisteList(long userId) {

		List<Borrow> itemList = borrowDao.getNeedGuaranteeRegisteList(userId);
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		if (itemList != null && itemList.size() > 0) {
			for (Borrow borrow : itemList) {
				BorrowModel bm = BorrowModel.instance(borrow);
				try {
					bm.setUserName(borrow.getUser().getUserName());
					bm.setUserId(borrow.getUser().getUserId());
				} catch (Exception e) {
					bm.setUserName("");
					bm.setUserId(0);
				}
				bm.setUser(null);
				modelList.add(bm);
			}
		}
		return modelList;
	}

	@Override
	public PageDataList<BorrowModel> getGuaranteeingList(BorrowModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addPage(model.getPage());
		// if (StringUtil.isNotBlank(model.getName())) {
		// param.addParam("name", Operators.LIKE, model.getName());
		// }HCB
		if (StringUtil.isNotBlank(model.getUserName())) {
			param.addParam("user.userName", Operators.EQ, model.getUserName());
		}
		if (model.getTime() == 7) {
			param.addParam("addTime", Operators.GT,
					DateUtil.rollDay(new Date(), -model.getTime()));
		} else if (model.getTime() != 0) {
			param.addParam("addTime", Operators.GT,
					DateUtil.rollMon(new Date(), -model.getTime()));
		}
		if (StringUtil.isNotBlank(model.getStartTime())) {
			Date startTime = DateUtil.valueOf(model.getStartTime()
					+ " 00:00:00");
			param.addParam("addTime", Operators.GT, startTime);
		}
		if (StringUtil.isNotBlank(model.getEndTime())) {
			Date endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
			param.addParam("addTime", Operators.LT, endTime);
		}
		param.addParam("vouchFirm", model.getVouchFirm());
		param.addParam("guaranteeNo", Operators.NOTEQ,
				SearchFilter.EMPTY_AND_NULL); // 非空字符串和NULL
		param.addParam("guaranteeRate", Operators.GT, 0);
		SearchFilter orFilter1 = new SearchFilter("status", 1);
		SearchFilter orFilter2 = new SearchFilter("status", 3);
		SearchFilter orFilter3 = new SearchFilter("status", 6);
		SearchFilter orFilter4 = new SearchFilter("status", 7);
		param.addOrFilter(orFilter1, orFilter2, orFilter3, orFilter4);
		param.addOrder(OrderType.DESC, "id");
		PageDataList<Borrow> pageDataList = borrowDao.findPageList(param);
		PageDataList<BorrowModel> pageDataList_ = new PageDataList<BorrowModel>();
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Borrow borrow = (Borrow) pageDataList.getList().get(i);
				BorrowModel borrowModel = BorrowModel.instance(borrow);
				borrowModel.setUserName(borrow.getUser().getUserName());
				borrowModel.setUser(null);
				list.add(borrowModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PageDataList<BorrowRepaymentModel> getOverdueGuaranteeList(
			BorrowModel model) {

		PageDataList<BorrowRepayment> pageDataList = borrowRepaymentDao
				.getOverdueGuaranteeList(model);
		PageDataList<BorrowRepaymentModel> pageDateList = new PageDataList<BorrowRepaymentModel>();
		List<BorrowRepaymentModel> list = new ArrayList<BorrowRepaymentModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowRepayment r = (BorrowRepayment) pageDataList.getList()
						.get(i);
				BorrowRepaymentModel rm = BorrowRepaymentModel.instance(r);
				Borrow borrow = r.getBorrow();
				User user = r.getUser();

				rm.setAccount(borrow.getAccount());
				rm.setApr(borrow.getApr());
				rm.setAddTime(borrow.getAddTime());
				rm.setBorrowId(borrow.getId());
				rm.setBorrowName(borrow.getName());
				rm.setUserName(user.getUserName());
				rm.setRealName(user.getRealName());
				rm.setMobilePhone(user.getMobilePhone());
				rm.setTimeLimit(borrow.getTimeLimit());
				rm.setType(borrow.getType());
				rm.setBorrowType(borrow.getType());
				rm.setBorrowTimeType(borrow.getBorrowTimeType());
				rm.setBorrowStyle(borrow.getStyle());
				rm.setRepaymentAccount(BigDecimalUtil.add(r.getCapital(),
						r.getInterest(), r.getLateInterest()));
				rm.setBorrow(null);
				rm.setUser(null);
				list.add(rm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}

	@Override
	public PageDataList<BorrowRepaymentModel> getCompensatedList(
			BorrowModel model) {

		PageDataList<BorrowRepayment> pageDataList = borrowRepaymentDao
				.getCompensatedList(model);
		PageDataList<BorrowRepaymentModel> pageDateList = new PageDataList<BorrowRepaymentModel>();
		List<BorrowRepaymentModel> list = new ArrayList<BorrowRepaymentModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowRepayment r = (BorrowRepayment) pageDataList.getList()
						.get(i);
				BorrowRepaymentModel rm = BorrowRepaymentModel.instance(r);
				Borrow borrow = r.getBorrow();
				User user = r.getUser();

				rm.setAddTime(borrow.getAddTime());
				rm.setBorrowId(borrow.getId());
				rm.setBorrowName(borrow.getName());
				rm.setUserName(user.getUserName());
				rm.setRealName(user.getRealName());
				rm.setMobilePhone(user.getMobilePhone());
				rm.setTimeLimit(borrow.getTimeLimit());
				rm.setType(borrow.getType());
				rm.setBorrowStyle(borrow.getStyle()); // 设置还款方式
				rm.setAccount(borrow.getAccount());
				rm.setApr(borrow.getApr());
				rm.setBorrow(null);
				rm.setUser(null);
				list.add(rm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}

	@Override
	@Deprecated
	public void verifyBz(BorrowModel model, Operator operator) {
		Borrow borrow = borrowDao.find(model.getId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		int status = 0;
		Global.setTransfer("user", borrow.getUser());
		Global.setTransfer("borrow", borrow);
		if (model.getStatus() == 0) {
			status = -1;
			borrowDao.updateStatus(borrow.getId(), 2);
			if (borrow.getType() == Borrow.TYPE_CREDIT) {
				UserCredit ua = userCreditDao.findObjByProperty("user.userId",
						borrow.getUser().getUserId());
				double amount = borrow.getAccount();
				userCreditDao.update(0, amount, -amount, borrow.getUser()
						.getUserId());
				UserCreditLog amountLog = new UserCreditLog();
				amountLog.setUser(ua.getUser());
				amountLog.setAccount(amount);
				amountLog.setAccountAll(ua.getCredit());
				amountLog.setAccountUse(ua.getCreditUse() + amount);
				amountLog.setAccountNoUse(ua.getCreditNouse() - amount);
				amountLog.setType("credit");
				amountLog.setRemark("返还冻结信用额度");
				userCreditLogDao.save(amountLog);
			}
			// 发送消息通知
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("borrowVerifyFailNoticeExecuter");
			executer.execute(0, borrow.getUser());
		} else if (model.getStatus() == 1) {
			// 秒标初审通过 冻结解冻资金（利息和奖励）
			status = 1;
			worker.handleBorrowBeforePublish(borrow);
			// 判断是否允许自动投标
			int enableAutoTender = Global.getInt("enableAutoTender");
			// 当前标非秒还标，未设置定向密码且允许自动投标...
			if (StringUtil.isBlank(borrow.getPwd()) && enableAutoTender == 1
					&& borrow.getType() != Borrow.TYPE_SECOND) {
				try {
					// 先将标状态改为19，标记为先处理自动投标
					borrowDao.updateStatus(borrow.getId(), 19, 0);
					BorrowModel borrowModel = BorrowModel.instance(borrow);
					// borrowModel.setStatus(19);
					borrowModel.setUserId(borrow.getUser().getUserId());
					ConcurrentUtil.autoTender(borrowModel);
				} catch (Exception e) {
					logger.error("触发disruptor自动投标异常");
				}
			} else {
				borrowDao.updateStatus(borrow.getId(), 1);
			}
			// 发送消息通知
			// AbstractExecuter executer =
			// ExecuterHelper.doExecuter("borrowVerifySuccNoticeExecuter");
			// executer.execute(0, borrow.getUser());
		}
		VerifyLog verifyLog = new VerifyLog(operator, "borrow", borrow.getId(),
				1, status, model.getRemark());
		verifyLogDao.save(verifyLog);
	}

	@Override
	public int getAllCount(int status, String startTime, String endTime) {
		return borrowDao.getAllCount(status, startTime, endTime);
	}

	@Override
	public double getAllMomeny() {
		return borrowDao.getAllMomeny();
	}

	@Override
	public int getAllOverduedCount(String startTime, String endTime) {
		return borrowDao.getAllOverduedCount(startTime, endTime);
	}

	@Override
	public int getCountByStatus(int status) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", status);
		return borrowDao.countByCriteria(param);
	}

	@Override
	public double getMomenyByStatus(int status) {
		return borrowDao.getMomenyByStatus(status);
	}

	@Override
	public int getVerifyFullCount() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		param.addParam("scales", 100);
		return borrowDao.countByCriteria(param);
	}

	@Override
	public int getVerifyFullCount(String startTime, String endTime) {
		return borrowDao.getVerifyFullCount(startTime, endTime);
	}

	@Override
	public double getVerifyFullMomeny() {
		return borrowDao.getVerifyFullMomeny();
	}

	@Override
	public double getVerifyFullMoney(String startTime, String endTime) {
		return borrowDao.getVerifyFullMoney(startTime, endTime);
	}

	@Override
	public int getAllOverdueingCount(String startTime, String endTime) {
		return borrowDao.getAllOverdueingCount(startTime, endTime);
	}

	@Override
	public double getAllOverduedMomeny(String startTime, String endTime) {
		return borrowDao.getAllOverduedMomeny(startTime, endTime);
	}

	@Override
	public double getAllOverdueingMomeny() {
		return borrowDao.getAllOverdueingMomeny();
	}

	@Override
	public double getAllOverdueingMoney(String startTime, String endTime) {
		return borrowDao.getAllOverdueingMoney(startTime, endTime);
	}

	@Override
	public PageDataList<BorrowModel> getConfirmedBorrowList(BorrowModel model) {

		PageDataList<Borrow> pageDataList = borrowDao
				.getConfirmedBorrowList(model);
		PageDataList<BorrowModel> pageDataList_ = new PageDataList<BorrowModel>();
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		pageDataList_.setPage(pageDataList.getPage());
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");

		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Borrow borrow = (Borrow) pageDataList.getList().get(i);
				BorrowModel bm = BorrowModel.instance(borrow);
				try {
					bm.setUserName(borrow.getUser().getUserName());
					bm.setUserId(borrow.getUser().getUserId());
				} catch (Exception e) {
					bm.setUserName("<font color='red'>该用户被删除</font>");
					bm.setUserId(0);
				}
				if (borrow.getRedPacket() != null) {
					bm.setRedPacketName(borrow.getRedPacket().getName());
				}
				if (borrow.getRegisterTime() != null
						&& DateUtil.getNowTime() > DateUtil.rollMinute(
								borrow.getRegisterTime(), 35).getTime() / 1000) {
					bm.setIsShowRegister(1);
				}
				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
				if (log != null) {
					Date d = log.getTime(); // 初审时间
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					c.add(Calendar.DATE, borrow.getValidTime());
					Calendar now = Calendar.getInstance();
					now.setTime(new Date());
					bm.setFlow(c.before(now));
					bm.setStartTime(DateUtil.dateStr2(log.getTime()));
				}
				bm.setUser(null);
				list.add(bm);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public long ipsTrialCount() {

		return borrowDao.ipsTrialCount();
	}

	@Override
	public Borrow getLastBorrow() {

		return borrowDao.getLastBorrow();
	}

	@Override
	public int getInviteCount() {

		return borrowDao.getInviteCount();
	}

	@Override
	public int getInviteCount(String startTime, String endTime) {
		return borrowDao.getInviteCount(startTime, endTime);
	}

	@Override
	public double getInviteMoney() {

		return borrowDao.getInviteMoney();
	}

	@Override
	public double getInviteMoney(String startTime, String endTime) {
		return borrowDao.getInviteMoney(startTime, endTime);
	}

	@Override
	public int getBorrowCount(String startTime, String endTime, int... status) {

		return borrowDao.getBorrowCount(startTime, endTime, status);
	}

	@Override
	public double getBorrowAccount(String startTime, String endTime,
			int... status) {

		return borrowDao.getBorrowAccount(startTime, endTime, status);
	}

	@Override
	public int getBorrowUserCount() {
		return borrowDao.getBorrowUserCount();
	}

	@Override
	public void doExchangeRatePacketCapture() {
		ExchangeRatePacketCapture erpc = new ExchangeRatePacketCapture();
		String cashPurchasePrice = ExchangeRatePacketCaptureUtil
				.getCashPurchasePrice();
		List<Borrow> list = borrowDao.getBorrowListByStatusAndType();
		if (list != null && list.size() > 0) {
			for (Borrow borrow : list) {
				List<ExchangeRatePacketCapture> erpcList = exchangeRatePacketCaptureDao
						.getCaptureRate(borrow.getId());
				// 更新BorrowTender表中的float_rate和BorrowCollection的float_income
				List<BorrowTender> btList = borrowTenderDao
						.getTenderByBorrowId(borrow.getId());
				for (BorrowTender borrowTender : btList) {
					double floatRate = BigDecimalUtil.div(BigDecimalUtil.sub(
							Double.parseDouble(cashPurchasePrice), erpcList
									.get(0).getCashPurchasePrice()), erpcList
							.get(0).getCashPurchasePrice());
					if (floatRate <= 0) {
						floatRate = 0;
					}
					borrowTender.setFloatRate(BigDecimalUtil.round(
							BigDecimalUtil.mul(floatRate, 100), 6));
					borrowTenderDao.update(borrowTender);
					BorrowCollection bc = borrowCollectionDao
							.getBorrowCollectionByTenderId(borrowTender.getId());
					bc.setFloatIncome(BigDecimalUtil.round(BigDecimalUtil.div(
							BigDecimalUtil.mul(BigDecimalUtil.round(
									BigDecimalUtil.mul(floatRate, 100), 2),
									borrowTender.getInterest()), borrow
									.getApr()), 2));
					borrowCollectionDao.update(bc);
				}
				erpc.setBorrow(borrow);
				erpc.setCashPurchasePrice(Double.parseDouble(cashPurchasePrice));
				erpc.setAddTime(new Date());
				exchangeRatePacketCaptureDao.save(erpc);
			}
		}
	}

	@Override
	public List<ExchangeRatePacketCapture> getCaptureRate(long borrowId) {
		return exchangeRatePacketCaptureDao.getCaptureRate(borrowId);
	}

	@Override
	public void appointmentBid(User user, Borrow borrow, double money) {
		if (borrow.getFixedTime() != null) {
			if (new Date().after(borrow.getFixedTime())) {
				throw new UserException("标的定时时间已过，不能进行预约投标！", 1);
			}
		}
		double sumBidMoney = borrowAppointmentBidDao.sumBidMoney(borrow);
		if (borrow.getAccount() - sumBidMoney < money) {
			throw new BorrowException("已超过预约投标的金额，不能进行预约投标！", 1);
		}
		BorrowAppointmentBid bab = new BorrowAppointmentBid();
		bab.setUser(user);
		bab.setBorrow(borrow);
		bab.setStatus(2);// 未投标
		bab.setMoney(money);
		bab.setAddTime(new Date());
		borrowAppointmentBidDao.save(bab);
		// 冻结预约投标的金额
		Global.setTransfer("borrow", borrow);
		Global.setTransfer("money", money);
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(borrow.getType()), borrow.getId());
		Global.setTransfer("flag", productBasic.getProductTypeFlag());
		AbstractExecuter borrowAppointmentBidExecuter = ExecuterHelper
				.doExecuter("borrowAppointmentBidExecuter");
		borrowAppointmentBidExecuter.execute(money, user, borrow.getUser());
	}

	@Override
	public List<BorrowAppointmentBid> getBorrowAppointmentBidByBorrowId(
			long borrowId) {
		return borrowAppointmentBidDao
				.getBorrowAppointmentBidByBorrowId(borrowId);
	}

	@Override
	public double sumBidMoney(BorrowModel borrow) {
		return borrowAppointmentBidDao.sumBidMoney(borrow);
	}

	@Override
	public void doAppointmentBid() throws Exception {
		List<BorrowAppointmentBid> list = borrowAppointmentBidDao
				.getBorrowAppointmentBid();
		if (list != null && list.size() > 0) {
			logger.info("开始进行预约投资... 当前时间：" + DateUtil.dateStr4(new Date()));
			for (BorrowAppointmentBid borrowAppointmentBid : list) {
				borrowAppointmentBid.setStatus(1);
				borrowAppointmentBidDao.update(borrowAppointmentBid);
				BorrowModel model = new BorrowModel();
				User user = borrowAppointmentBid.getUser();
				Borrow borrow = borrowAppointmentBid.getBorrow();
				model.setBidLogo("appointmentBid");
				model.setMoney(borrowAppointmentBid.getMoney());
				model.setUser(userDao.find(user.getUserId()));
				model.setAddIp(Global.getIP());
				model.setTenderType((byte) 3);// 预约投资
				borrow = borrowDao.find(borrow.getId());
				ConcurrentUtil.tender(model, borrow);
			}
		}
	}

	@Override
	public void repaymentEntrustEdit(long repaymentId, double expectedRate) {
		BorrowRepayment br = borrowRepaymentDao.find(repaymentId);
		Borrow borrow = borrowDao.find(br.getBorrow().getId());
		List<BorrowTender> list = borrowTenderDao.getBorrowTenderByBorrowId(br
				.getBorrow().getId());
		for (BorrowTender borrowTender : list) {
			borrowTender.setFloatRate(expectedRate);
			borrowTenderDao.update(borrowTender);
			BorrowCollection bc = borrowCollectionDao
					.getBorrowCollectionByTenderId(borrowTender.getId());
			double floatIncome = 0;
			if (borrow.getBorrowTimeType() == 0) {// 月标
				floatIncome = BigDecimalUtil.round(BigDecimalUtil.mul(
						borrowTender.getAccount(), borrow.getTimeLimit(),
						BigDecimalUtil.div(expectedRate, 100),
						BigDecimalUtil.div(1, 12)));
			} else if (borrow.getBorrowTimeType() == 1) {// 天标
				floatIncome = BigDecimalUtil.round(BigDecimalUtil.mul(
						borrowTender.getAccount(), borrow.getTimeLimit(),
						BigDecimalUtil.div(expectedRate, 100),
						BigDecimalUtil.div(1, 365)));
			}
			bc.setFloatIncome(floatIncome);
			borrowCollectionDao.update(bc);
		}

	}

	@Override
	public void doAppointmentSendSms() {
		List<Borrow> list = borrowDao.getAppointmentBorrow();
		if (list != null && list.size() > 0) {
			for (Borrow borrow : list) {
				List<BorrowAppointmentBid> babList = borrowAppointmentBidDao
						.getBorrowAppointmentBidByBorrowId(borrow.getId());
				for (BorrowAppointmentBid borrowAppointmentBid : babList) {
					Global.setTransfer("borrow", borrow);
					Global.setTransfer("borrowAppointmentBid",
							borrowAppointmentBid);
					AbstractExecuter executer = ExecuterHelper
							.doExecuter("appointmentBidExecuter");
					executer.execute(0, borrowAppointmentBid.getUser());
				}
			}
		}
	}
	
	@Override
	public void doRemindFailAndFullBids() {
		List<Borrow> list = borrowDao.getFullAndFailBorrow();
		if (list != null && list.size() > 0) {
			for (Borrow borrow : list) {
				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
			    Global.setTransfer("borrow", borrow);
//			    User user = new User();
//			    user.setMobilePhone(log.getVerifyUser().getMobile());
//			    user.setRealName(log.getVerifyUser().getName());
//			    Global.setTransfer("user", user);
			    Global.setTransfer("realName", log.getVerifyUser().getName());
			    Global.setTransfer("mobilePhone", log.getVerifyUser().getMobile());
				AbstractExecuter executer = ExecuterHelper
						.doExecuter("fullAndFailBidsRemindExecuter");
				executer.execute(0, borrow.getUser());
			}
		}
	}
	

	@Override
	public BorrowModel getLastBorrow(int borrowType) {
		BorrowModel model = borrowDao.getLastBorrow(borrowType);
		if (model == null) {
			model = borrowDao.getLastUnRecommendBorrow(borrowType);
		}
		return model;
	}

	@Override
	public PageDataList<ExchangeRatePacketCaptureModel> exchangeRatePacketCaptureList(
			BorrowModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),
				model.getSize());
		if (StringUtil.isNotBlank(model.getSearchName())) {
			param.addParam("borrow.name", Operators.LIKE, model.getSearchName());
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<ExchangeRatePacketCapture> pageDataList = exchangeRatePacketCaptureDao
				.findPageList(param);
		PageDataList<ExchangeRatePacketCaptureModel> pageDataList_ = new PageDataList<ExchangeRatePacketCaptureModel>();
		List<ExchangeRatePacketCaptureModel> list = new ArrayList<ExchangeRatePacketCaptureModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				ExchangeRatePacketCapture exchangeRatePacketCapture = (ExchangeRatePacketCapture) pageDataList
						.getList().get(i);
				ExchangeRatePacketCaptureModel exchangeRatePacketCaptureModel = ExchangeRatePacketCaptureModel
						.instance(exchangeRatePacketCapture);
				Borrow borrow = borrowDao.find(exchangeRatePacketCapture
						.getBorrow().getId());
				exchangeRatePacketCaptureModel.setBorrowName(borrow.getName());
				list.add(exchangeRatePacketCaptureModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public ExchangeRatePacketCapture getExchangeRatePacketCapture(long id) {
		return exchangeRatePacketCaptureDao.find(id);
	}

	@Override
	public void exchangeRatePacketCaptureEdit(long id, double cashPurchasePrice) {
		ExchangeRatePacketCapture erpc = exchangeRatePacketCaptureDao.find(id);
		erpc.setCashPurchasePrice(cashPurchasePrice);
		exchangeRatePacketCaptureDao.update(erpc);
	}

	@Override
	public void middleReapy() {
		List<BorrowRepayment> list = borrowRepaymentDao.middleReapy();
		if (list != null && list.size() > 0) {
			for (BorrowRepayment borrowRepayment : list) {
				Borrow borrow = borrowRepayment.getBorrow();
				borrow = borrowDao.find(borrow.getId());
				List<BorrowTender> btList = borrowTenderDao
						.getBorrowTenderByBorrowId(borrow.getId());
				double sumInterest = 0;
				for (BorrowTender borrowTender : btList) {
					double repayInterest = borrowTender.getInterest()
							* borrow.getMiddleDay() / borrow.getTimeLimit();
					borrowTender.setRepaymentAccount(repayInterest);
					borrowTender.setRepaymentYesAccount(repayInterest);
					borrowTender.setRepaymentYesInterest(repayInterest);
					borrowTender.setWaitAccount(borrowTender
							.getRepaymentAccount() - repayInterest);
					borrowTender.setWaitInterest(borrowTender.getInterest()
							- repayInterest);
					borrowTenderDao.update(borrowTender);

					BorrowCollection bc = borrowCollectionDao
							.getBorrowCollectionByTenderId(borrowTender.getId());
					bc.setRepaymentYesAccount(repayInterest);
					// 归还投资人利息
					if (repayInterest > 0) {
						double borrow_fee = Global.getDouble("borrow_fee");
						double borrowFee = BigDecimalUtil.mul(repayInterest,
								borrow_fee);
						borrowFee = BigDecimalUtil.round(borrowFee);
						// 收回利息
						Global.setTransfer("borrow", borrow);
						ProductBasic productBasic = productBasicService
								.getProductBasicInfo(
										new Long(borrow.getType()),
										borrow.getId());
						Global.setTransfer("flag",
								productBasic.getProductTypeFlag());
						Global.setTransfer("money", repayInterest);
						Global.setTransfer("borrowFee", borrowFee);
						AbstractExecuter repayTenderInterestExecuter = ExecuterHelper
								.doExecuter("borrowRepayTenderInterestExecuter");
						repayTenderInterestExecuter.execute(repayInterest,
								borrowTender.getUser(), borrow.getUser());
						// 扣除投资人利息管理费
						if (borrowFee > 0) {
							Global.setTransfer("money", borrowFee);
							AbstractExecuter manageFeeExecuter = ExecuterHelper
									.doExecuter("deductManageFeeExecuter");
							manageFeeExecuter.execute(borrowFee, borrowTender
									.getUser(), new User(Constant.ADMIN_ID));
							bc.setManageFee(borrowFee);
						}
					}
					borrowCollectionDao.update(bc);

					sumInterest += repayInterest;
				}
				borrow.setRepaymentYesAccount(sumInterest);
				borrow.setRepaymentYesInterest(sumInterest);
				borrowDao.update(borrow);

				borrowRepayment.setRepaymentYesAccount(sumInterest);
				borrowRepaymentDao.update(borrowRepayment);
			}
		}
	}

	@Override
	public Borrow getBorrowByBorrowName(String borrowName) {
		return borrowDao.getBorrowByName(borrowName);
	}

	@Override
	public void exchangeRatePacketCaptureAdd(ExchangeRatePacketCapture erpc) {
		exchangeRatePacketCaptureDao.save(erpc);
	}
}
