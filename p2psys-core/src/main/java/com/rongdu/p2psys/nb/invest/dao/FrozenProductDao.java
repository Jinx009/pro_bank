package com.rongdu.p2psys.nb.invest.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.invest.domain.FrozenProduct;

public interface FrozenProductDao extends BaseDao<FrozenProduct>
{
	public double getLockMoney(Integer productId);

	public int unLockProjectMoney(Long productId, Long productBasicId, Long userId);

	public int unLockProjectMoney(Long productId, Long userId);
}
