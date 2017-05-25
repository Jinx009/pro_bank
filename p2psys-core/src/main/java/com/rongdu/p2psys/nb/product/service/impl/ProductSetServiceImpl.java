package com.rongdu.p2psys.nb.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.product.dao.ProductSetDao;
import com.rongdu.p2psys.nb.product.domain.ProductSet;
import com.rongdu.p2psys.nb.product.service.ProductSetService;

@Service("productSetService")
public class ProductSetServiceImpl implements ProductSetService
{

	@Resource
	private ProductSetDao productSetDao;

	public List<ProductSet> getProdSetList(Long productId)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("productId", productId);
		return productSetDao.findByCriteria(param);
	}

	@Override
	public void saveProductSet(List<ProductSet> list)
	{
		productSetDao.save(list);
	}

	@Override
	public void deleteProductSet(List<ProductSet> list)
	{
		productSetDao.delete(list);
	}


}
