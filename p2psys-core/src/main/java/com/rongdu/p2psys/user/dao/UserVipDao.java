package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.UserVipRule;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserVip;

public interface UserVipDao extends BaseDao<UserVip> {

	public UserVip getLateYearInvestByUser(User user);

	/**
	 *  根据最近一年投资金额区间对用户进行VIP评级
	 * @param money1
	 * @param money2
	 * @param rule UserVipRule
	 */
	public void updateUserVip(double money1, double money2, UserVipRule rule);
	
	/**
	 * 根据最近一年投资金额区间对用户进行VIP评级
	 * @param money
	 * @param rule UserVipRule
	 */
	public void updateUserVip(double money, UserVipRule rule);
}
