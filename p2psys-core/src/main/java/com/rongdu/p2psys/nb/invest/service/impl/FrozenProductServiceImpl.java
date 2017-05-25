package com.rongdu.p2psys.nb.invest.service.impl;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.invest.dao.FrozenProductDao;
import com.rongdu.p2psys.nb.invest.domain.FrozenProduct;
import com.rongdu.p2psys.nb.invest.service.FrozenProductService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;

@Service("frozenProductService")
public class FrozenProductServiceImpl implements FrozenProductService
{

	@Resource
	private FrozenProductDao frozenProductDao;
	
	public void updateFrozenProduct(FrozenProduct frozenProduct)
	{
		frozenProductDao.update(frozenProduct);
	}

	public FrozenProduct saveFrozenProduct(FrozenProduct frozenProduct)
	{
		return frozenProductDao.save(frozenProduct);
	}

	public double getLockMoney(Integer productId)
	{
		return frozenProductDao.getLockMoney(productId);
	}

	public void saveFrozenProduct(User user, ProductBasic productBasic,ProductType productType, double money)
	{
		FrozenProduct frozenProduct = new FrozenProduct();
		
		frozenProduct.setAddTime(new Date());
		frozenProduct.setMoney(money);
		frozenProduct.setProductBasic(productBasic);
		frozenProduct.setProductId(Integer.valueOf(productBasic.getRelatedId().toString()));
		frozenProduct.setProductType(productType);
		frozenProduct.setStatus(0);
		frozenProduct.setUser(user);
		
		frozenProductDao.save(frozenProduct);
	}
	@Override
	public void unLock (User user, ProductBasic productBasic,ProductType productType, double money)
	{
        FrozenProduct frozenProduct = new FrozenProduct();
		
		frozenProduct.setAddTime(new Date());
		frozenProduct.setMoney(money);
		frozenProduct.setProductBasic(productBasic);
		frozenProduct.setProductId(Integer.valueOf(productBasic.getRelatedId().toString()));
		frozenProduct.setProductType(productType);
		frozenProduct.setStatus(1);
		frozenProduct.setUser(user);
		
		frozenProductDao.update(frozenProduct);
	}

}
