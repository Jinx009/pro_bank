package com.rongdu.p2psys.nb.invest.service.impl;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.invest.dao.FrozenUserDao;
import com.rongdu.p2psys.nb.invest.domain.FrozenUser;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;

@Service("frozenUserService")
public class FrozenUserServiceImpl implements FrozenUserService
{

	@Resource
	private FrozenUserDao frozenUserDao;

	public void updateFrozenUser(FrozenUser frozenUser)
	{
		frozenUserDao.update(frozenUser);
	}

	public FrozenUser saveFrozenUser(FrozenUser frozenUser)
	{
		return frozenUserDao.save(frozenUser);
	}
	
	public double getLockUseMoney(Integer userId)
	{
		return frozenUserDao.getLockUseMoney(userId);
	}

	public void saveFrozenUser(User user,double money, ProductBasic productBasic,ProductType productType)
	{
		FrozenUser frozenUser = new FrozenUser();
		
		frozenUser.setProductId(Integer.valueOf(productBasic.getRelatedId().toString()));
		frozenUser.setUser(user);
		frozenUser.setAddTime(new Date());
		frozenUser.setMoney(money);
		frozenUser.setProductBasic(productBasic);
		frozenUser.setStatus(0);
		frozenUser.setProductType(productType);
		
		frozenUserDao.save(frozenUser);
	}
	@Override
    public void unLock(User user,double money, ProductBasic productBasic,ProductType productType)
	{
		FrozenUser frozenUser = new FrozenUser();
		
		frozenUser.setUser(user);
		frozenUser.setAddTime(new Date());
		frozenUser.setMoney(money);
		frozenUser.setProductBasic(productBasic);
		frozenUser.setStatus(1);
		frozenUser.setProductType(productType);
		
		frozenUserDao.update(frozenUser);
	}
}
