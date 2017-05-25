package com.rongdu.p2psys.nb.invest.service;

import com.rongdu.p2psys.nb.invest.domain.FrozenProduct;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;

public interface FrozenProductService 
{
	public void updateFrozenProduct(FrozenProduct frozenProduct);
	
	public FrozenProduct saveFrozenProduct(FrozenProduct frozenProduct);
	
	public double getLockMoney(Integer productId);

	public void saveFrozenProduct(User user, ProductBasic productBasic,ProductType productType, double money);

	public void unLock(User user, ProductBasic productBasic, ProductType productType,
			double money);
	
}
