package com.rongdu.p2psys.nb.ppfund.service.impl;

import java.util.ArrayList;
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
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.VerifyLogModel;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.nb.ppfund.dao.PpfundDao;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.exception.PpfundException;
import com.rongdu.p2psys.ppfund.model.PpfundModel;

@Service("thePpfundService")
public class PpfundServiceImpl implements PpfundService {

	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private VerifyLogService verifyLogService;

	@Resource
	private PpfundDao thePpfundDao;

	Logger logger = Logger.getLogger(PpfundServiceImpl.class);

	@Override
	public Ppfund getById(Long id) {
		return thePpfundDao.find(id);
	}

	@Override
	public Double getTotalInvestMoney(Ppfund ppfund) {
		return thePpfundDao.getTotalInvestMoney(ppfund);
	}

	@Override
	public Double getTotalProfitMoney(Ppfund ppfund) {
		return thePpfundDao.getTotalProfitMoney(ppfund);
	}

	@Override
	public PageDataList<PpfundModel> manageList(PpfundModel model) {
		QueryParam param = QueryParam.getInstance()
				.addPage(model.getPage(), model.getRows())
				.addOrder(OrderType.DESC, "id");
		if (model != null) {
			if (!StringUtil.isBlank(model.getSearchName())) {
				// 模糊查询
				// [产品编码]或[产品名称]
				SearchFilter orFilter1 = new SearchFilter("pidNo",
						Operators.LIKE, model.getSearchName());
				SearchFilter orFilter2 = new SearchFilter("name",
						Operators.LIKE, model.getSearchName());
				param.addOrFilter(orFilter1, orFilter2);
			} else {
				// 精确查询
				// [产品编码]或[产品名称]
				if (StringUtil.isNotBlank(model.getPidNo())) {
					param.addParam("pidNo", Operators.EQ, model.getPidNo());
				}
				if (StringUtil.isNotBlank(model.getName())) {
					param.addParam("name", Operators.EQ, model.getName());
				}
			}
			// 状态
			if (model.getStatus() != PpfundModel.STATUS_UNRELATED) {
				param.addParam("status", model.getStatus());
			}
			// 投资进度
			if (model.getScales() > 0) {
				param.addParam("scales", model.getScales());
			}
		}
		PageDataList<Ppfund> pojoPageList = thePpfundDao.findPageList(param);
		List<PpfundModel> modelPageList = new ArrayList<PpfundModel>();
		PageDataList<PpfundModel> modelList = new PageDataList<PpfundModel>();
		modelList.setPage(pojoPageList.getPage());

		for (Ppfund ppfund : pojoPageList.getList()) {
			PpfundModel ppfundModel = PpfundModel.instance(ppfund);
			if (ppfund.getAccount() != 0) {
				ppfundModel.setScales((ppfund.getAccountYes() / ppfund
						.getAccount()) * 100);
			}
			// 相关产品信息
			ProductBasic prod = productBasicService.getProductBasicInfo(ppfund
					.getProductType().getId(), ppfund.getId());
			if (prod != null) {
				ppfundModel.setFlagId(prod.getProductTypeFlag().getId());
				ppfundModel.setTypeId(prod.getProductType().getId());
				ppfundModel.setProductType(prod.getProductType());
				ppfundModel.setProductId(prod.getId());
			} else {
				logger.debug("未找到ProductBasic记录，Ppfund Id为" + ppfund.getId());
			}
			modelPageList.add(ppfundModel);
		}
		modelList.setList(modelPageList);
		return modelList;
	}

	@Override
	public Ppfund addPpfund(PpfundModel ppfundModel) {
		// 校验
		ppfundModel.checkModel();

		// Ppfund
		ppfundModel.setAddTime(new Date());
		ppfundModel.setAddIp(Global.getIP());
		ppfundModel.setOldAccount(ppfundModel.getAccount());
		ppfundModel.setProductType(productTypeService.findById(ppfundModel
				.getTypeId()));
		Ppfund newPpfund = thePpfundDao.save(ppfundModel.prototype());

		// ProductBasic
		ProductBasic prod = ProductBasicModel.transPpfund(newPpfund);
		prod.setProductTypeFlag(productTypeFlagService.findById(ppfundModel
				.getFlagId()));
		prod.setProductType(productTypeService.findById(ppfundModel.getTypeId()));
		productBasicService.saveProductBasic(prod);

		return newPpfund;
	}

	@Override
	public void updatePpfund(PpfundModel ppfundModel) {
		Ppfund ppfund = thePpfundDao.find(ppfundModel.getId());
		if (ppfund.getStatus() != PpfundModel.STATUS_WAITING_FOR_APPROVE) {
			throw new PpfundException("产品状态不正确！", PpfundException.TYPE_JSON);
		}

		// 校验
		ppfundModel.checkModel();

		// Ppfund
		Ppfund newPpfund = ppfundModel.prototype();
		newPpfund.setProductType(ppfund.getProductType());
		newPpfund.setOldAccount(newPpfund.getAccount());
		newPpfund.setAddTime(ppfund.getAddTime());
		newPpfund.setAddIp(ppfund.getAddIp());
		thePpfundDao.update(newPpfund);

		// ProductBasic
		ProductBasic oldProd = productBasicService.getProductBasicInfo(
				newPpfund.getProductType().getId(), newPpfund.getId());
		ProductBasic newProd = ProductBasicModel.transPpfund(newPpfund);
		newProd.setId(oldProd.getId());
		newProd.setProductTypeFlag(oldProd.getProductTypeFlag());
		newProd.setProductType(oldProd.getProductType());
		productBasicService.updateProductBasic(newProd);
	}

	@Override
	public void update(Ppfund ppfund) {
		thePpfundDao.update(ppfund);
	}

	@Override
	public void verifyPpfund(PpfundModel model, Operator operator) {
		Ppfund ppfund = thePpfundDao.find(model.getId());
		if (ppfund.getStatus() != PpfundModel.STATUS_WAITING_FOR_APPROVE) {
			throw new PpfundException("产品状态不正确！", PpfundException.TYPE_JSON);
		}

		ProductBasic prod = productBasicService.getProductBasicInfo(ppfund
				.getProductType().getId(), ppfund.getId());

		int status = 0;
		if (model.getStatus() == 0) {
			// 审核不通过
			status = -1;
			ppfund.setStatus(PpfundModel.STATUS_FAIL);
			prod.setStatus(ProductStatusConstant.STATUS_FAIL);
		} else {
			// 审核通过
			status = 1;
			ppfund.setStatus(PpfundModel.STATUS_APPROVED);
			prod.setStatus(ProductStatusConstant.STATUS_APPROVED);
		}
		thePpfundDao.update(ppfund);
		productBasicService.updateProductBasic(prod);

		VerifyLog verifyLog = new VerifyLog(operator, "ppfund", ppfund.getId(),
				VerifyLogModel.VERIFY_TYPE__TRIAL, status, model.getRemark());
		verifyLogService.save(verifyLog);
	}

}
