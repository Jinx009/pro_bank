package com.rongdu.p2psys.nb.invest.service;

import com.rongdu.p2psys.nb.invest.domain.FrozenUser;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;

public interface FrozenUserService
{	
	public void updateFrozenUser(FrozenUser frozenUser);
	
	public FrozenUser saveFrozenUser(FrozenUser frozenUser);
	
	public double getLockUseMoney(Integer userId);

	public void saveFrozenUser(User user, double money, ProductBasic productBasic,ProductType productType);

	public void unLock(User user, double money, ProductBasic productBasic,
			ProductType productType);
}
