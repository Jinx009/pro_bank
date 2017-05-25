package com.rongdu.p2psys.nb.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.nb.product.dao.ProductTypeFlagDao;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.model.ProductTypeFlagModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;

@Service("productTypeFlagService")
public class ProductTypeFlagServiceImpl implements ProductTypeFlagService {

	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private SystemConfigService systemConfigService;

	@Resource
	private ProductTypeFlagDao productTypeFlagDao;

	Logger logger = Logger.getLogger(ProductTypeFlagServiceImpl.class);

	@Override
	public PageDataList<ProductTypeFlagModel> getModelPageList(
			ProductTypeFlagModel flagModel) {
		PageDataList<ProductTypeFlag> pojoList = productTypeFlagDao
				.getPojoListForPaging(flagModel.getPage(), flagModel.getSize());

		List<ProductTypeFlagModel> modelList = new ArrayList<ProductTypeFlagModel>();
		for (ProductTypeFlag productTypeFlag : pojoList.getList()) {
			ProductTypeFlagModel model = ProductTypeFlagModel
					.instance(productTypeFlag);
			modelList.add(model);
		}

		// 封装成带分页的集合
		PageDataList<ProductTypeFlagModel> pageList = new PageDataList<ProductTypeFlagModel>();
		pageList.setPage(pojoList.getPage());
		pageList.setList(modelList);

		return pageList;
	}

	@Override
	public String getAdminUrl() {
		return systemConfigService.findByNid("con_adminurl").getValue();
	}

	@Override
	public ProductTypeFlag saveProductTypeFlag(ProductTypeFlagModel flagModel) {
		if (StringUtil.isNotBlank(flagModel.getPicUrl())) {
			flagModel.setPicUrl(getAdminUrl() + flagModel.getPicUrl());
		}
		if (StringUtil.isNotBlank(flagModel.getListUrl())) {
			flagModel.setListUrl(getAdminUrl() + flagModel.getListUrl());
		}
		if (StringUtil.isNotBlank(flagModel.getIconUrl())) {
			flagModel.setIconUrl(getAdminUrl() + flagModel.getIconUrl());
		}
		// 设置推荐时间默认值
		flagModel.setRecommendTime(new Date());
		return productTypeFlagDao.save(ProductTypeFlagModel
				.transProductTypeFlag(flagModel));
	}

	@Override
	public ProductTypeFlag findById(Long id) {
		return productTypeFlagDao.find(id);
	}

	@Override
	public ProductTypeFlag modifyProductTypeFlag(ProductTypeFlagModel flagModel) {
		ProductTypeFlag pojo = findById(flagModel.getId());
		pojo.setFlagName(flagModel.getFlagName());
		pojo.setFlagDescription(flagModel.getFlagDescription());
		pojo.setRemark(flagModel.getRemark());
		if (StringUtil.isNotBlank(flagModel.getPicUrl())) {
			pojo.setPicUrl(getAdminUrl() + flagModel.getPicUrl());
		}
		if (StringUtil.isNotBlank(flagModel.getListUrl())) {
			pojo.setListUrl(getAdminUrl() + flagModel.getListUrl());
		}
		if (StringUtil.isNotBlank(flagModel.getIconUrl())) {
			pojo.setIconUrl(getAdminUrl() + flagModel.getIconUrl());
		}
		return updateProductTypeFlag(pojo);
	}

	@Override
	public ProductTypeFlag updateProductTypeFlag(ProductTypeFlag pojo) {
		return productTypeFlagDao.update(pojo);
	}

	@Override
	public void deleteProductTypeFlag(Long id) {
		List<ProductBasicModel> list = productBasicService.getModelListByFlag(
				id, null);
		if (null != list && list.size() > 0) {
			logger.error("此标签已被产品关联，无法删除，Flag Id = " + id);
			throw new BussinessException("此标签已被产品关联，无法删除", 1);
		}
		productTypeFlagDao.delete(id);
	}

	@Override
	public List<ProductTypeFlag> getPojoList(Integer status) {
		return productTypeFlagDao.getPojoList(status);
	}

	@Override
	public List<ProductTypeFlag> findAllEnabledProductTypeFlag() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("isEnable", ProductTypeConstant.ENABLE_TRUE);
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");
		return productTypeFlagDao.findByCriteria(param);
	}

}
