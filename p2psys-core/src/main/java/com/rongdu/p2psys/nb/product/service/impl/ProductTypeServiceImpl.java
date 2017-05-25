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
import com.rongdu.p2psys.core.dao.SystemConfigDao;
import com.rongdu.p2psys.nb.product.dao.ProductBasicDao;
import com.rongdu.p2psys.nb.product.dao.ProductTypeDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.model.ProductTypeModel;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;

@Service("productTypeService")
public class ProductTypeServiceImpl implements ProductTypeService {

	@Resource
	private ProductBasicDao productBasicDao;
	@Resource
	private ProductTypeDao productTypeDao;
	@Resource
	private SystemConfigDao systemConfigDao;

	Logger logger = Logger.getLogger(ProductTypeServiceImpl.class);

	@Override
	public PageDataList<ProductTypeModel> getModelPageList(int pageNumber,
			int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");
		PageDataList<ProductType> productTypePageList = productTypeDao
				.findPageList(param);

		List<ProductTypeModel> list = new ArrayList<ProductTypeModel>();
		for (ProductType productType : productTypePageList.getList()) {
			ProductTypeModel model = ProductTypeModel.instance(productType);
			list.add(model);
		}

		// 封装成带分页的集合
		PageDataList<ProductTypeModel> pageList = new PageDataList<ProductTypeModel>();
		pageList.setPage(productTypePageList.getPage());
		pageList.setList(list);

		return pageList;
	}

	@Override
	public String getAdminUrl() {
		return systemConfigDao.findByHql(
				" from SystemConfig where nid = 'con_adminurl'").getValue();
	}

	@Override
	public ProductType saveProductType(ProductTypeModel typeModel) {
		// 设置推荐时间默认值
		typeModel.setRecommendTime(new Date());
		return productTypeDao
				.save(ProductTypeModel.transProductType(typeModel));
	}

	@Override
	public ProductType modifyProductType(ProductTypeModel typeModel) {
		ProductType pojo = findById(typeModel.getId());
		pojo.setTypeCode(typeModel.getTypeCode());
		pojo.setTypeName(typeModel.getTypeName());
		pojo.setTypeDescription(typeModel.getTypeDescription());
		pojo.setRemark(typeModel.getRemark());
		return updateProductType(pojo);
	}

	@Override
	public ProductType updateProductType(ProductType pojo) {
		return productTypeDao.update(pojo);
	}

	@Override
	public void deleteProductType(Long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("productType.id", id);
		List<ProductBasic> prodList = productBasicDao.findByCriteria(param);
		if (null != prodList && prodList.size() > 0) {
			logger.error("此类别已被产品关联，无法删除，Type Id = " + id);
			throw new BussinessException("此类别已被产品关联，无法删除", 1);
		}
		productTypeDao.delete(id);
	}

	@Override
	public ProductType findById(Long id) {
		return productTypeDao.find(id);
	}

	@Override
	public List<ProductType> findProductTypeListByCategory(String category,
			int status) {
		QueryParam param = QueryParam.getInstance();
		if (status > -1) {
			param.addParam("isEnable", status);
		}
		param.addParam("typeCategory", category);
		return productTypeDao.findByCriteria(param);
	}

	@Override
	public List<ProductType> getProductTypeListByFlag(Long flagId) {
		return productTypeDao.getProductTypeListByFlag(flagId);
	}

}
