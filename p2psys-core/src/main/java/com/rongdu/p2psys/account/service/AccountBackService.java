package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountDeduct;
import com.rongdu.p2psys.account.model.AccountBackModel;
import com.rongdu.p2psys.core.domain.Operator;

/**
 * 扣款service
 * 
 * @version 2.0
 */
public interface AccountBackService {

	/**
	 * 扣款记录列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<AccountBackModel> list(AccountBackModel model);

	/**
	 * 新增
	 */
	AccountDeduct add(AccountDeduct accountBack, double money, Operator operator);

	/**
	 * 根据ID获得实体类
	 * 
	 * @param id
	 * @return
	 */
	AccountDeduct find(long id);

	/**
	 * 扣款审核
	 * 
	 * @param model
	 * @param operator
	 */
	void verifyAccountBack(AccountBackModel model, Operator operator);

	/**
	 * @param userName
	 * @param realName
	 * @param status
	 * @return
	 */
	List<AccountBackModel> find(String userName, String realName, int status);

}
