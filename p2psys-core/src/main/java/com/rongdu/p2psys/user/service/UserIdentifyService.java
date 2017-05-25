package com.rongdu.p2psys.user.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.tpp.yjf.model.ForwardConIdentify;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserVipApply;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserVipApplyModel;

/**
 * 认证信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月17日17:27:02
 */
public interface UserIdentifyService {

	/**
	 * 登录到个人中心，获得用户认证的状态并显示
	 * 
	 * @param userId
	 * @return
	 */
	public UserIdentify findByUserId(long userId);

	/**
	 * 更新实名认证状态real_name_status
	 * 
	 * @param userId
	 */
	public void modifyRealnameStatus(long userId, int status, int preStatus) throws Exception;

	/**
	 * 更新实名认证状态mobile_phone_status
	 * 
	 * @param userId
	 */
	public void modifyMobilePhoneStatus(long userId, int status, int preStatus);

	/**
	 * 通过userId获得UserAttestation对象
	 * 
	 * @param userId
	 * @return
	 */
	public UserIdentify getAttestationStatus(long userId);

	/**
	 * 得到单个用户的认证状态
	 * 
	 * @param userId
	 * @return
	 */
	public UserIdentifyModel getUserIdentifyByUserId(long userId);

	/**
	 * 更改邮箱状态
	 * 
	 * @param userId
	 * @param status
	 * @param preStatus
	 */
	public void modifyEmailStatus(long userId, int status, int preStatus);

	/**
	 * 审核用户认证信息
	 * 
	 * @param id
	 * @param realNameVerifyRemark
	 * @param realNameStatus
	 * @param user
	 */
	public void userAttestationEdit(long id, String realNameVerifyRemark, int realNameStatus, Operator operator);

	/**
	 * 统计等待审核的实名认证总数
	 * 
	 * @param status
	 * @return
	 */
	public int countByRealName(int status);
	
	/**
	 * 根据状态及时间统计实名认证总数
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int countByRealName(int status, String startTime, String endTime);

	/**
	 * 通过ID查询记录
	 * 
	 * @param id
	 * @return
	 */
	public UserIdentify findById(long id);

	public PageDataList<UserIdentifyModel> findUserIdentifylist(int pageNumber, int pageSize, UserIdentify model, String searchName);

	public PageDataList<UserModel> userModelList(int pageNumber, int pageSize, UserIdentify model, String searchName);

	/**
	 * 查询用户申请vip
	 * 
	 * @param applyId
	 * @return
	 */
	public UserVipApply getUserVipApplyById(long applyId);

	/**
	 * vip审核
	 * 
	 * @param user
	 * @param userVipApply
	 */
	public void verifyVip(Operator operator, UserVipApply userVipApply);

	/*
	 * PageDataList<UserIdentifyModel> list(int pageNumber, int pageSize, UserIdentifyModel model);
	 */

	public PageDataList<UserVipApplyModel> vipApplyList(int pageNumber, int pageSize, UserVipApplyModel model);

	/**
	 * 易极付实名回调
	 * @param fci
	 * @return
	 */
	public boolean updateRealNameStatusByCallBack(ForwardConIdentify fci);
	
	/**
	 * 更新
	 * @param identify
	 */
	public void update(UserIdentify identify);
	
}
