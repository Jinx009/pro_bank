package com.rongdu.p2psys.nb.invest.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.invest.domain.FrozenUser;

public interface FrozenUserDao extends BaseDao<FrozenUser>
{
	public double getLockUseMoney(Integer userId);

	public int unLockUserMoney(Long productId, Long productBasicId, Long userId);
}
