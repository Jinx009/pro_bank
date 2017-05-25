package com.rongdu.p2psys.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserUpload;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * @author sj
 * @version 2.0
 * @since 2014年2月18日15:14:10
 */
public interface UserCacheService {

	/**
	 * 我的基本信息
	 * 
	 * @param userCacheModel
	 * @return
	 */
	public UserCacheModel getUserCache(long userId);

	/**
	 * 获取用户附属信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserCache findByUserId(long userId);

	/**
	 * 通过userId获得证件号码
	 * 
	 * @return String
	 */
	public String getUserCard_id(long userId);

	/**
	 * 实名认证
	 * 
	 * @param userId
	 * @param rule
	 */
	public Object doRealname(User user, UserModel model) throws Exception;
	
	/**
	 * 企业开户
	 * @param user
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object doCorpRegister(User user, UserModel model) throws Exception;

	/**
	 * 实名认证更新证件号码
	 * 
	 * @param userId real_name
	 * @return
	 */
	public void modify(long userId, String card_id);

	/**
	 * 实名认证更新证件号码 证件号码正反面
	 * 
	 * @param userId real_name
	 * @return
	 */
	public void modify(long userId, String card_id, String card_positive, String card_opposite);
	/**
	 * 重载实名认证更新证件号码 证件号码正反面
	 * 
	 * @param userId real_name
	 * @return 
	 * @return
	 */
	void modify(long userId, UserModel model, String card_positive, String card_opposite);
	
	/**
	 * 锁定用户
	 * 
	 * @param userId
	 */

	public void userLockEdit(long userId, int status);

	/**
	 * 申请vip
	 * 
	 * @param user
	 */
	public UserIdentify applyVip(User user);

	public void update(UserCache userCache);
	
	public void update(UserCache userCache, List<UserUpload> list, long[] delIds);

	public PageDataList<UserCache> userCacheList(int pageNumber, int pageSize, UserCache model);

	public void updateStatus();
	/**
	 * 环讯开户回调处理
	 * @param user 开户的用户
	 * @param ips  接口返回回来的结果
	 */
	void ipsRegisterCall(User user, IpsRegister ips);

	/**
	 * 保存UserCache对象
	 * @param model UserCache对象
	 * @param list 实物照集合
	 */
	void save(UserCache model, List<UserUpload> list);
	/**
	 * 保存UserCache对象
	 * @param pageNumber 第几页
	 * @param pageSize 每页个数
	 * @param model UserCache对象
	 * @return PageDataList<UserCacheModel>
	 */
	PageDataList<UserCacheModel> userList(int pageNumber, int pageSize, UserCache model);

	/**
	 * 获取UserCache对象
	 * @param id id
	 * @return UserCache 
	 */
	UserCache findById(long id);

	/**
	 * 更新修改密码时间
	 * 
	 * @param userId
	 */
	void modifyPwdTime(long userId);
	
	/**
	 * 更新修改交易密码时间
	 * 
	 * @param userId
	 */
	void modifyPayPwdTime(long userId);
	
	/**
	 * 锁定
	 * 
	 * @param userId
	 * @param type
	 */
	boolean doLock(HttpServletRequest request, long userId, String type);
}
