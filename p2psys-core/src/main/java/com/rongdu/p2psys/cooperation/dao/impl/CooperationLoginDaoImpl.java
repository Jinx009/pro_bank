package com.rongdu.p2psys.cooperation.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.cooperation.dao.CooperationLoginDao;
import com.rongdu.p2psys.cooperation.domain.CooperationLogin;


@Repository("cooperationLoginDao")
public class CooperationLoginDaoImpl extends BaseDaoImpl<CooperationLogin> implements CooperationLoginDao {

	/**
	 * 根据openId和type查询联合登陆信息
	 * 
	 * @param openId
	 * @param type 合作登陆类型
	 * @return
	 */
	public CooperationLogin getCooperationLogin(String openId, int type){
		QueryParam param = QueryParam.getInstance();
		param.addParam("openId", openId);
		param.addParam("type", type);
		return super.findByCriteriaForUnique(param);
	}
	
	/**
	 * 新增联合登陆信息
	 * 
	 * @param cooperation
	 */
	public void addCooperationLogin(CooperationLogin cooperation) {
		cooperation.setAddTime(new Date());
		super.save(cooperation);
	}

}
