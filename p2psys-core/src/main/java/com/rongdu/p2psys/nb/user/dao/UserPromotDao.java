package com.rongdu.p2psys.nb.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;

public interface UserPromotDao extends BaseDao<UserPromot>
{
	/**
	 * 保存
	 * 
	 * @param userPromot
	 * @return
	 */
	public UserPromot saveUserPromot(UserPromot userPromot);
	
	/**
	 * 更新
	 * 
	 * @param userPromot
	 */
	public void updateUserPromot(UserPromot userPromot);
	
	/**
	 * 根据邀请码获取邀请人
	 * 
	 * @param code
	 * @return
	 */
	public UserPromot getUserPromotByCode(String code); 
	
	/**
	 * 判断邀请码是否存在
	 * 
	 * @param code
	 * @return
	 */
	public boolean alreadyHaveCode(String code);

	/**
	 * 通过hql查找对象
	 * @param hql
	 * @return
	 */
	public UserPromot findByHql(String hql);
	
	public void savePcUserPromot(User user);
}
