package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserCredit;

/**
 * 信用额度Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月25日
 */
public interface UserCreditDao extends BaseDao<UserCredit> {

	/**
	 * 修改信用额度
	 * 
	 * @param totalVar
	 * @param useVar
	 * @param nouseVar
	 * @param amount
	 */
	void update(double totalVar, double useVar, double nouseVar, long userId);
	
	
	public UserCredit findByUserId(long userId);

}
