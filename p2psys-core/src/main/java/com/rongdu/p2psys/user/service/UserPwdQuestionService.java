package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPwdQuestionModel;

/**
 * 密保问题Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月26日
 */
public interface UserPwdQuestionService {

	/**
	 * 列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPwdQuestion> list(long userId);

	/**
	 * 根据id和userId查询
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public UserPwdQuestion find(long id, long userId);

	/**
	 * 新增
	 * 
	 * @param model
	 */
	public void add(UserPwdQuestionModel model);

	/**
	 * 随机显示密保
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPwdQuestion> listRand(long userId);

	/**
	 * 获得用户密保问题的集合
	 * 
	 * @return
	 */
	public List<UserPwdQuestion> pwdQuestion(User user);

	/**
	 * 验证密保问题
	 * 
	 * @param model
	 */
	public void doAnswerPwdQuestion(UserModel model, User user);

	/**
	 * 设置密保问题
	 * 
	 * @param model
	 * @param user
	 */
	public void doSetPwdQuestion(UserModel model, User user);

}
