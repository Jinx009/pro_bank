package com.rongdu.p2psys.user.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.user.domain.UserCreditApply;
import com.rongdu.p2psys.user.model.UserCreditApplyModel;

/**
 * 信用额度申请Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月25日
 */
public interface UserCreditApplyService {

	/**
	 * 根据用户ID计数
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public int count(long userId);

	/**
	 * 根据用户ID及状态计数
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public int count(long userId, int status);

	/**
	 * 分页
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	public PageDataList<UserCreditApply> list(long userId, BorrowModel model);

	/**
	 * 新增
	 * 
	 * @param userCreditApply
	 */
	public void save(UserCreditApply userCreditApply);

	/**
	 * 后台申请额度列表
	 * 
	 * @param model
	 * @return
	 */
	public PageDataList<UserCreditApplyModel> list(UserCreditApplyModel model);

	/**
	 * 通过ID查询记录
	 * 
	 * @param id
	 * @return
	 */
	public UserCreditApply findById(long id);

}
