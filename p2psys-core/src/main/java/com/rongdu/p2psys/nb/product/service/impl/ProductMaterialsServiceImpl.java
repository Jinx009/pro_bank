package com.rongdu.p2psys.nb.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.product.dao.ProductMaterialsDao;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;

@Service("productMaterialsService")
public class ProductMaterialsServiceImpl implements ProductMaterialsService {

	@Resource
	private ProductMaterialsDao productMaterialsDao;

	@Override
	public List<ProductMaterials> getMaterialsByProductId(Long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("product.id", id);
		return productMaterialsDao.findByCriteria(param);
	}

	@Override
	public void addMaterials(List<ProductMaterials> list) {
		productMaterialsDao.save(list);
	}

	@Override
	public void deleteMaterials(List<ProductMaterials> list) {
		productMaterialsDao.delete(list);
	}

}
