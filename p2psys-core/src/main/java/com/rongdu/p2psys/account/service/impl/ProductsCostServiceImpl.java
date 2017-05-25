package com.rongdu.p2psys.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.dao.ProductsCostDao;
import com.rongdu.p2psys.account.domain.ProductsCost;
import com.rongdu.p2psys.account.model.ProductsCostModel;
import com.rongdu.p2psys.account.service.ProductsCostService;

@Service("productsCostService")
public class ProductsCostServiceImpl implements ProductsCostService{
	@Resource
	private ProductsCostDao productsCostDao;
	
	@Override
	public ProductsCost save(ProductsCost cost) {
		
		return productsCostDao.save(cost);
	}

	@Override
	public PageDataList<ProductsCostModel> list(ProductsCostModel model) {
		
		return productsCostDao.list(model);
	}
	
}
