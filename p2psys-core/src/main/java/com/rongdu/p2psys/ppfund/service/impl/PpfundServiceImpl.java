package com.rongdu.p2psys.ppfund.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.nb.product.dao.ProductBasicDao;
import com.rongdu.p2psys.nb.product.dao.ProductMaterialsDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.ppfund.dao.PpfundDao;
import com.rongdu.p2psys.ppfund.dao.PpfundUploadDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.service.PpfundService;

/**
 * 现金管理产品（PPfund）
 */
@Service("ppfundService")
public class PpfundServiceImpl implements PpfundService {

	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductTypeService productTypeService;

	@Resource
	private PpfundDao ppfundDao;
	@Resource
	private PpfundUploadDao ppfundUploadDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductBasicDao productBasicDao;
	@Resource
	private ProductMaterialsService productMaterialsService;
	@Resource
	private ProductMaterialsDao productMaterialsDao;

	@Override
	public PageDataList<PpfundModel> list(PpfundModel model) {
		QueryParam param = QueryParam.getInstance()
				.addPage(model.getPage(), model.getRows())
				.addParam("isHide", 0).addParam("status", Operators.NOTEQ, 0)
				.addParam("status", Operators.NOTEQ, 2)
				.addOrder(OrderType.DESC, "isRecommend")
				.addOrder(OrderType.ASC, "scales")
				.addOrder(OrderType.DESC, "addTime");
		PageDataList<Ppfund> pageList = ppfundDao.findPageList(param);
		List<PpfundModel> list = new ArrayList<PpfundModel>();
		PageDataList<PpfundModel> pageList_ = new PageDataList<PpfundModel>();
		for (Ppfund ppfund : pageList.getList()) {
			PpfundModel ppfundModel = PpfundModel.instance(ppfund);
			PpfundUpload ppfundImag = ppfundUploadDao.getPpfundImg(ppfund
					.getId());
			if (ppfundImag != null) {
				ppfundModel.setPpfundImg(ppfundImag.getPicPath());
			}
			list.add(ppfundModel);
		}
		pageList_.setList(list);
		pageList_.setPage(pageList.getPage());
		return pageList_;
	}

	@Override
	public PageDataList<PpfundModel> manageList(PpfundModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),
				model.getRows());
		param.addOrder(OrderType.DESC, "addTime");
		if (StringUtil.isNotBlank(model.getSearchName())) {
			param.addParam("name", Operators.LIKE, model.getSearchName());
		} else {
			if (StringUtil.isNotBlank(model.getName())) {
				param.addParam("name", Operators.LIKE, model.getName());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}
			if (model.getStatus() != 99) {
				param.addParam("status", model.getStatus());
			}
			if (StringUtil.isNotBlank(model.getPidNo())) {
				param.addParam("pidNo", model.getPidNo());
			}
		}
		PageDataList<Ppfund> pageList = ppfundDao.findPageList(param);
		List<PpfundModel> list = new ArrayList<PpfundModel>();
		PageDataList<PpfundModel> pageList_ = new PageDataList<PpfundModel>();
		for (Ppfund ppfund : pageList.getList()) {
			PpfundModel ppfundModel = PpfundModel.instance(ppfund);
			PpfundUpload ppfundImag = ppfundUploadDao.getPpfundImg(ppfund
					.getId());
			if (ppfundImag != null) {
				ppfundModel.setPpfundImg(ppfundImag.getPicPath());
			}
			if (ppfund.getAccount() != 0) {
				ppfundModel.setScales((ppfund.getAccountYes() / ppfund
						.getAccount()) * 100);
			}

			// 对应产品信息
			ProductBasic prod = productBasicService.getProductBasicInfo(ppfund
					.getProductType().getId(), ppfund.getId());
			ppfundModel.setProductId(prod.getId());

			list.add(ppfundModel);
		}
		pageList_.setList(list);
		pageList_.setPage(pageList.getPage());
		return pageList_;
	}

	@Override
	public void addPpfund(PpfundModel ppfundModel, List<PpfundUpload> list) {
		// 保存产品信息
		ppfundModel.setOldAccount(ppfundModel.getAccount());
		Ppfund newPpfund = ppfundDao.save(ppfundModel.prototype());
		// 新结构 - ProductBasic
		ProductBasic prod = ProductBasicModel.transPpfund(newPpfund);
		prod.setProductTypeFlag(productTypeFlagService.findById(ppfundModel
				.getFlagId()));
		prod.setProductType(productTypeService.findById(ppfundModel.getTypeId()));
		// prod.setShowOrder(ppfundModel.getShowOrder());
		productBasicDao.save(prod);

		// 保存上传文件信息
		List<PpfundUpload> li = new ArrayList<PpfundUpload>();
		List<ProductMaterials> newList = new ArrayList<ProductMaterials>();
		for (PpfundUpload upload : list) {
			upload.setPpfund(newPpfund);
			li.add(upload);
		}
		ppfundUploadDao.save(li);
		productMaterialsDao.save(newList);
	}

	@Override
	public Ppfund getPpfundById(long id) {
		return ppfundDao.find(id);
	}

	@Override
	public void verifyPpfund(PpfundModel model, Operator operator) {
		Ppfund ppfund = ppfundDao.find(model.getId());
		// 新结构 - ProductBasic
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
		ppfundDao.update(ppfund);
		productBasicDao.update(prod);

		VerifyLog verifyLog = new VerifyLog(operator, "ppfund", ppfund.getId(),
				1, status, model.getRemark());
		verifyLogDao.save(verifyLog);
	}

	@Override
	public void updatePpfund(PpfundModel model, List<PpfundUpload> list,
			String[] delIds) {
		Ppfund ppfund = model.prototype();
		Ppfund newPpfund = ppfundDao.update(ppfund);
		// 新结构 - ProductBasic
		ProductBasic oldProd = productBasicService.getProductBasicInfo(
				newPpfund.getProductType().getId(), newPpfund.getId());
		ProductBasic newProd = ProductBasicModel.transPpfund(newPpfund);
		newProd.setId(oldProd.getId());
		newProd.setProductTypeFlag(oldProd.getProductTypeFlag());
		newProd.setProductType(oldProd.getProductType());
		// newProd.setShowOrder(model.getShowOrder());
		newProd = productBasicDao.update(newProd);

		if (delIds != null) {
			for (String id : delIds) {
				PpfundUpload upload = ppfundUploadDao.find(NumberUtil
						.getLong(id));
				ppfundUploadDao.delete(NumberUtil.getLong(id));
				String realPath = ServletActionContext.getServletContext()
						.getRealPath(upload.getPicPath());
				new File(realPath).delete();
			}
		}
		ppfundUploadDao.save(list);
	}

	@Override
	public PpfundModel getLastPpfund() {
		return ppfundDao.getLastPpfund();
	}

	@Override
	public Ppfund update(Ppfund ppfund) {
		return ppfundDao.update(ppfund);
	}

	@Override
	public Object[] countByFinish() {

		return ppfundDao.countByFinish();
	}

}
