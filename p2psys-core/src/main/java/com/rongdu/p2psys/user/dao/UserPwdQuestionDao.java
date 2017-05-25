package com.rongdu.p2psys.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;

/**
 * 密保问题Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月26日
 */
public interface UserPwdQuestionDao extends BaseDao<UserPwdQuestion> {

	/**
	 * 列表
	 * 
	 * @param userId
	 * @return
	 */
	List<UserPwdQuestion> list(long userId);

	/**
	 * 根据id和userId查询
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	UserPwdQuestion find(long id, long userId);

	/**
	 * 随机显示密保
	 * 
	 * @param userId
	 * @return
	 */
	List<UserPwdQuestion> listRand(long userId);

	/**
	 * 通过密保问题获得实体类
	 * @param question
	 * @param userId
	 * @return
	 */
	UserPwdQuestion findByQuestion(String question, long userId);
}
