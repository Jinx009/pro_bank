package com.rongdu.p2psys.nb.borrow.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.dao.ExchangeRatePacketCaptureDao;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.VerifyLogModel;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.ExchangeRatePacketCaptureUtil;
import com.rongdu.p2psys.nb.borrow.dao.BorrowDao;
import com.rongdu.p2psys.nb.borrow.service.BorrowService;
import com.rongdu.p2psys.nb.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;

@Service("theBorrowService")
public class BorrowServiceImpl implements BorrowService {

	@Resource
	private BorrowTenderService theBorrowTenderService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private VerifyLogService verifyLogService;

	@Resource
	private BorrowDao theBorrowDao;
	@Resource
	private ExchangeRatePacketCaptureDao exchangeRatePacketCaptureDao;
	
	@Resource
	private BorrowRepaymentDao repaymentDao;

	Logger logger = Logger.getLogger(BorrowServiceImpl.class);

	@Override
	public Borrow getById(Long id) {
		return theBorrowDao.find(id);
	}

	@Override
	public Boolean isSpreadBorrow(Long id) {
		return theBorrowDao.isSpreadBorrow(id);
	}

	@Override
	public PageDataList<BorrowModel> manageList(BorrowModel model) {
		QueryParam param = QueryParam.getInstance()
				.addPage(model.getPage(), model.getSize())
				.addOrder(OrderType.DESC, "id");
		if (model != null) {
			if (!StringUtil.isBlank(model.getSearchName())) {
				// 模糊查询
				// [产品编码]或[产品名称]
				SearchFilter orFilter1 = new SearchFilter("bidNo",
						Operators.LIKE, model.getSearchName());
				SearchFilter orFilter2 = new SearchFilter("name",
						Operators.LIKE, model.getSearchName());
				param.addOrFilter(orFilter1, orFilter2);
			} else {
				// 精确查询
				// [产品编码]或[产品名称]
				if (StringUtil.isNotBlank(model.getBidNo())) {
					param.addParam("bidNo", Operators.EQ, model.getBidNo());
				}
				if (StringUtil.isNotBlank(model.getName())) {
					param.addParam("name", Operators.EQ, model.getName());
				}
			}
			// 状态
			if (model.getStatus() != Borrow.STATUS_UNRELATED) {
				param.addParam("status", model.getStatus());
			}
			// 投资进度
			if (model.getScales() > 0) {
				param.addParam("scales", model.getScales());
			}
			// 流标
			if (null != model.getExpirationTime()) {
				param.addParam("scales", Operators.LT, 100D);
				param.addParam("expirationTime", Operators.LT,
						model.getExpirationTime());
			}
		}
		PageDataList<Borrow> pojoPageList = theBorrowDao.findPageList(param);
		PageDataList<BorrowModel> modelPageList = new PageDataList<BorrowModel>();
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		modelPageList.setPage(pojoPageList.getPage());

		if (pojoPageList.getList().size() > 0) {
			for (int i = 0; i < pojoPageList.getList().size(); i++) {
				Borrow borrow = pojoPageList.getList().get(i);
				BorrowModel borrowModel = BorrowModel.instance(borrow);
				if (borrow.getRedPacket() != null) {
					borrowModel.setRedPacketName(borrow.getRedPacket()
							.getName());
				}
				// 相关产品信息
				ProductBasic prod = productBasicService.getProductBasicInfo(
						new Long(borrow.getType()), borrow.getId());
				BorrowRepayment repayment = repaymentDao.findObjByProperty("borrow.id", borrow.getId());
				if(repayment!=null)
				{
					borrowModel.setRepaymentTime(repayment.getRepaymentTime());
				}
				if (prod != null) {
					borrowModel.setFlagId(prod.getProductTypeFlag().getId());
					borrowModel.setType((int) prod.getProductType().getId()
							.longValue());
					borrowModel.setProductId(prod.getId());
				} else {
					logger.error("未找到ProductBasic记录，Borrow Id为"
							+ borrow.getId());
				}
				modelList.add(borrowModel);
			}
		}
		modelPageList.setList(modelList);
		return modelPageList;
	}
	
	
	/**
	 * 后台分页查询
	 * 
	 * @param model
	 * @return PageDataList<BorrowModel>
	 */
	@Override
	public PageDataList<BorrowModel> biddingManageList(BorrowModel model){
		String commonSql = " select b.* from rd_borrow b where b.status = 1 ";
		String vipSql = " select b.* from rd_borrow b join nb_product_type t on b.type = t.type_code " + 
						" where t.type_category = 'VIP' and b.status = 6 " ;
		QueryParam param = QueryParam.getInstance()
				.addOrder(OrderType.DESC, "id");
		if (model != null) {
			if (!StringUtil.isBlank(model.getSearchName())) {
				// 模糊查询
				// [产品编码]或[产品名称]
//				SearchFilter orFilter1 = new SearchFilter("bidNo",
//						Operators.LIKE, model.getSearchName());
//				SearchFilter orFilter2 = new SearchFilter("name",
//						Operators.LIKE, model.getSearchName());
//				param.addOrFilter(orFilter1, orFilter2);
				commonSql += " and (b.bid_no like '%" + model.getSearchName() + "%' or b.name like '%" + model.getSearchName() +"%')";
				vipSql += " and (b.bid_no like '%" + model.getSearchName() + "%' or b.name like '%" + model.getSearchName() +"%')";
			} else {
				// 精确查询
				// [产品编码]或[产品名称]
				if (StringUtil.isNotBlank(model.getBidNo())) {
//					param.addParam("bidNo", Operators.EQ, model.getBidNo());
					commonSql += " and b.bid_no like '%" + model.getBidNo() + "%'";
					vipSql += " and b.bid_no like '%" + model.getBidNo() + "%'";

				}
				if (StringUtil.isNotBlank(model.getName())) {
//					param.addParam("name", Operators.EQ, model.getName());
					commonSql += " and b.name like '%" + model.getName() + "%'";
					vipSql += " and b.name like '%" + model.getName() + "%'";
				}
			}

			// 投资进度
			if (model.getScales() > 0) {
//				param.addParam("scales", model.getScales());
				commonSql += " and b.scales >= " + model.getScales();
				vipSql += " and b.scales >= " + model.getScales();
			}
			// 流标
			if (null != model.getExpirationTime()) {
//				param.addParam("scales", Operators.LT, 100D);
//				param.addParam("expirationTime", Operators.LT,
//						model.getExpirationTime());
				commonSql += " and b.scales < 100 and b.expiration_time < " + model.getExpirationTime();
				vipSql += " and b.scales < 100 and b.expiration_time < " + model.getExpirationTime();
			}
		}
		String strSql = " from (" + commonSql + " union " + vipSql + ") b ";
		PageDataList<Borrow> pojoPageList = theBorrowDao.findPageListBySql(strSql, param, Borrow.class);
		PageDataList<BorrowModel> modelPageList = new PageDataList<BorrowModel>();
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		modelPageList.setPage(pojoPageList.getPage());
		if (pojoPageList.getList().size() > 0) {
			for (int i = 0; i < pojoPageList.getList().size(); i++) {
				Borrow borrow = pojoPageList.getList().get(i);
				BorrowModel borrowModel = BorrowModel.instance(borrow);
				if (borrow.getRedPacket() != null) {
					borrowModel.setRedPacketName(borrow.getRedPacket()
							.getName());
				}
				// 相关产品信息
				ProductBasic prod = productBasicService.getProductBasicInfo(
						new Long(borrow.getType()), borrow.getId());
				BorrowRepayment repayment = repaymentDao.findObjByProperty("borrow.id", borrow.getId());
				if(repayment!=null)
				{
					borrowModel.setRepaymentTime(repayment.getRepaymentTime());
				}
				if (prod != null) {
					borrowModel.setFlagId(prod.getProductTypeFlag().getId());
					borrowModel.setType((int) prod.getProductType().getId()
							.longValue());
					borrowModel.setProductId(prod.getId());
				} else {
					logger.error("未找到ProductBasic记录，Borrow Id为"
							+ borrow.getId());
				}
				modelList.add(borrowModel);
			}
		}
		modelPageList.setList(modelList);
		return modelPageList;
	}

	public Borrow addBorrow(BorrowModel borrowModel) {
		Borrow borrow = borrowModel.prototype();

		// 校验
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.checkModelData();
		worker.setBorrowField();

		// Borrow
		Borrow newBorrow = theBorrowDao.save(borrow);

		// ProductBasic
		ProductBasic prod = ProductBasicModel.transBorrow(newBorrow);
		prod.setProductTypeFlag(productTypeFlagService.findById(borrowModel
				.getFlagId()));
		prod.setProductType(productTypeService.findById(Long
				.valueOf(borrowModel.getType())));
		productBasicService.saveProductBasic(prod);

		return newBorrow;
	}

	@Override
	public void updateBorrow(BorrowModel borrowModel) {
		Borrow borrow = theBorrowDao.find(borrowModel.getId());
		if (borrow.getStatus() != Borrow.STATUS_PUBLISHING) {
			throw new BorrowException("产品状态不正确！", BorrowException.TYPE_JSON);
		}

		// 校验
		BorrowWorker worker = BorrowHelper.getWorker(borrowModel.prototype());
		worker.checkModelData();
		worker.setBorrowField();

		// Borrow
		Borrow newBorrow = worker.prototype();
		theBorrowDao.update(newBorrow);

		// ProductBasic
		ProductBasic oldProd = productBasicService.getProductBasicInfo(
				new Long(newBorrow.getType()), newBorrow.getId());
		ProductBasic newProd = ProductBasicModel.transBorrow(newBorrow);
		newProd.setId(oldProd.getId());
		newProd.setProductTypeFlag(oldProd.getProductTypeFlag());
		newProd.setProductType(oldProd.getProductType());
		productBasicService.updateProductBasic(newProd);
	}

	@Override
	public void confirmBorrow(BorrowModel model, Operator operator) {
		Borrow borrow = theBorrowDao.find(model.getId());
		if (borrow.getStatus() != Borrow.STATUS_PUBLISHING) {
			throw new BorrowException("产品状态不正确！", BorrowException.TYPE_JSON);
		}
		ProductBasic prod = productBasicService.getProductBasicInfo(new Long(
				borrow.getType()), borrow.getId());

		int verifyStatus = 0;
		if (model.getStatus() == 1) {
			// 初审通过
			verifyStatus = 1;

			// Borrow
			borrow.setStatus(Borrow.STATUS_TRIAL_PASSED);
			borrow.setFixedTime(model.getFixedTime());
			borrow.setExpirationTime(new Date(model.getFixedTime().getTime()
					+ Long.valueOf(borrow.getValidTime()) * 24L * 60L * 60L
					* 1000L));
			// ProductBasic
			prod.setStatus(ProductStatusConstant.STATUS_APPROVED);

			// VIP
			if (ProductTypeConstant.PRODUCT_CATEGORY__VIP.equalsIgnoreCase(prod
					.getProductType().getTypeCategory())) {
				borrow.setStatus(Borrow.STATUS_REPAYMENT_START);
				prod.setStatus(ProductStatusConstant.STATUS_REPAYMENT_START);
			}

		} else {
			// 初审不通过
			verifyStatus = -1;

			// Borrow
			borrow.setStatus(Borrow.STATUS_TRIAL_PASSLESS);
			// ProductBasic
			prod.setStatus(ProductStatusConstant.STATUS_FAIL);
		}

		theBorrowDao.update(borrow);
		productBasicService.updateProductBasic(prod);

		VerifyLog verifyLog = new VerifyLog(operator, "borrow", borrow.getId(),
				VerifyLogModel.VERIFY_TYPE__TRIAL, verifyStatus,
				model.getRemark());
		verifyLogService.save(verifyLog);
	}

	@Override
	public void verifyFullBorrow(BorrowModel model, Operator operator) {
		Borrow borrow = theBorrowDao.find(model.getId());
		if (borrow.getStatus() != Borrow.STATUS_TRIAL_PASSED) {
			throw new BorrowException("产品状态不正确！", BorrowException.TYPE_JSON);
		}
		ProductBasic prod = productBasicService.getProductBasicInfo(new Long(
				borrow.getType()), borrow.getId());

		int verifyStatus = 0;
		if (model.getStatus() == 1) {
			// 审核通过
			verifyStatus = 1;

			// Borrow
			borrow.setStatus(Borrow.STATUS_RECHECK_PASS);
			// ProductBasic
			prod.setStatus(ProductStatusConstant.STATUS_RECHECK_PASS);

			// 海外投资特殊处理
			handlingExchangeRate(borrow);
		} else {
			// 审核不通过
			verifyStatus = -1;

			// Borrow
			borrow.setStatus(Borrow.STATUS_RECHECK_PASSLESS);
			// ProductBasic
			prod.setStatus(ProductStatusConstant.STATUS_RECHECK_FAIL);
		}

		borrow.setReviewTime(new Date());
		theBorrowDao.update(borrow);
		productBasicService.updateProductBasic(prod);

		model = BorrowModel.instance(borrow);
		try {
			if (verifyStatus == 1) {
				ConcurrentUtil.autoVerifyFullSuccess(model);
			} else {
				ConcurrentUtil.autoVerifyFullFail(model);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BorrowException("满标复审出错！", BorrowException.TYPE_JSON);
		}

		VerifyLog verifyLog = new VerifyLog(operator, "borrow", borrow.getId(),
				VerifyLogModel.VERIFY_TYPE__RECHECK, verifyStatus,
				model.getRemark());
		verifyLogService.save(verifyLog);
	}

	@Override
	public void cancelBorrow(Borrow borrow) {
		ProductType type = productTypeService.findById(Long.valueOf(borrow.getType()));
		if (borrow.getStatus() != Borrow.STATUS_TRIAL_PASSED
			&&!type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP)
			||borrow.getStatus() != Borrow.STATUS_REPAYMENT_START
			&&type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP)) {
			throw new BorrowException("产品状态不正确！", BorrowException.TYPE_JSON);
		}
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.cancelBorrow();
		borrow = worker.prototype();

		theBorrowDao.update(borrow);
		ConcurrentUtil.autoCancel(borrow);

		theBorrowTenderService.cancelBorrowTender(borrow.getId());
	}

	@Override
	public void stopBorrow(Borrow borrow, Operator operator) {
		ProductType type = productTypeService.findById(Long.valueOf(borrow.getType()));
		if (borrow.getStatus() != Borrow.STATUS_TRIAL_PASSED
				&&!type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP)
				||borrow.getStatus() != Borrow.STATUS_REPAYMENT_START
				&&type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP)) {
			throw new BorrowException("产品状态不正确！", BorrowException.TYPE_JSON);
		}
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.stopBorrow();
		borrow = worker.prototype();

		handlingExchangeRate(borrow);
		theBorrowDao.update(borrow);

		VerifyLog verifyLog = new VerifyLog(operator, "borrow", borrow.getId(),
				VerifyLogModel.VERIFY_TYPE__STOP,
				VerifyLogModel.VERIFY_STATUS__PASS, "截标");
		verifyLogService.save(verifyLog);
	}
	
	@Override
	public boolean isSkipReview(Borrow borrow) {
		return theBorrowDao.isSkipReview(borrow); 
	}

	/**
	 * 满标复审时，海外投资产品的特殊处理
	 * 
	 * @param borrow
	 */
	private void handlingExchangeRate(Borrow borrow) {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (borrow.getType() == Borrow.TYPE_ESTATE && hour >= 9) {
			String cashPurchasePrice = ExchangeRatePacketCaptureUtil
					.getCashPurchasePrice();
			if (!StringUtil.isBlank(cashPurchasePrice)) {
				ExchangeRatePacketCapture erpc = new ExchangeRatePacketCapture();
				erpc.setBorrow(borrow);
				erpc.setCashPurchasePrice(Double.parseDouble(cashPurchasePrice));
				erpc.setAddTime(new Date());
				exchangeRatePacketCaptureDao.save(erpc);
			}
		}
	}

}
