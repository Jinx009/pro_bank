package com.rongdu.p2psys.user.service;

import java.util.Map;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPromotModel;

/**
 * 推荐人Service
 * 
 * @author ZhuJunjie
 */
public interface UserPromotService {

	/**
	 * 添加推荐人
	 * 
	 * @param userName
	 * @param canUseTimes
	 * @param rate
	 * @return
	 */
	Map<String, Object> userPromotAdd(String userName, int canUseTimes,
			double rate, Operator operator);

	/**
	 * 删除推荐人
	 * 
	 * @param userName
	 * @param promot
	 * @param operator
	 */
	void userPromotDelete(String userName, UserPromot promot, Operator operator);



	/**
	 * 修改推荐人信息
	 * 
	 * @param userPromot
	 */
	void userPromotEdit(UserPromot userPromot);
	
	
	
	/**
	 * 根据id获得UserPromot
	 * 
	 * @param id
	 * @return
	 */
	UserPromot getUserPromotById(long id);
	
	/**
	 * 获得推荐人列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return 推荐人列表
	 */
	PageDataList<UserPromotModel> userPromotList(int pageNumber, int pageSize,
			UserModel model);

	/**
	 * 查看推荐人下的用户
	 * 
	 * @param user
	 * @return 用户列表
	 */
	PageDataList<User> userPromotDetailList(User user, int pageNumber,
			int pageSize, String searchName);

	/**
	 * 查看所有推荐人下的用户
	 * 
	 * @param userModel
	 * @return 用户列表
	 */
	PageDataList<UserModel> userPromotAllList(UserModel userModel);

	/**
	 * 检验数据库中是否已经存在此推荐码
	 * 
	 * @param code
	 * @return boolean
	 */
	boolean hasDuplicateCode(String code);

	/**
	 * 根据邀请码获取推荐人
	 * 
	 * @param code
	 * @return 推荐人实例
	 */
	UserPromot getUserPromotByCode(String code);
	
	/**
	 * 获得推荐人列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return 推荐人列表
	 */
	PageDataList<UserPromotModel> exportUserPromotList(int pageNumber, int pageSize,
			UserModel model);
	PageDataList<UserModel> exportUserPromotAllList(UserModel userModel);

}
