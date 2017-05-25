package com.rongdu.p2psys.cooperation.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.cooperation.domain.CooperationLogin;

public interface CooperationLoginDao extends BaseDao<CooperationLogin> {

	/**
	 * 根据openId和type查询联合登陆信息
	 * 
	 * @param openId
	 * @param type 合作登陆类型
	 * @return
	 */
	public CooperationLogin getCooperationLogin(String openId, int type);

	/**
	 * 新增联合登陆信息
	 * 
	 * @param cooperation
	 */
	public void addCooperationLogin(CooperationLogin cooperation);

}
