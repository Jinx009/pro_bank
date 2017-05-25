package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.OperatorUser;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserFinancialModel;

/**
 * 父级管理员-子级管理员接口
 * @author yinliang
 * @version 2.0
 * @Date   2014年12月28日
 */
public interface OperatorUserService {	
	/**
	 * 获取用户列表
	 * @param model
	 * @return
	 */
	PageDataList<UserFinancialModel> getList(int pageNumber, int pageSize,UserFinancialModel model);
	
	/**
	 * 获取用户-顾问列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	PageDataList<UserFinancialModel> getFinancialUserList(int pageNumber, int pageSize,UserFinancialModel model);
	
	public void addFinancial(List<OperatorUser> list);
}
