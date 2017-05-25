package com.rongdu.p2psys.user.service;

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserVip;

/**
 * vip规则Service
 * @author sj
 *
 */
public interface UserVipService {

	/**
	 * 根据平台设定的VIP金额修改用户VIP等级
	 */
	public void doUserVip();

	public UserVip getUserVipRuleByUser(User user);

	/**
	 * 根据用户投标金额VIP评级
	 * 
	 * @param money
	 * @param user
	 */
	public void doUserVip(double money, User user);
	
	/**
	 * 检测用户最近一年VIP等级
	 */
	public void doUserVipCheck();
}
