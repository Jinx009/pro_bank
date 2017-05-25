package com.rongdu.p2psys.user.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.UserPwdQuestionDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;

@Repository("userPwdQuestionDao")
public class UserPwdQuestionDaoImpl extends BaseDaoImpl<UserPwdQuestion> implements UserPwdQuestionDao {

	@Override
	public List<UserPwdQuestion> list(long userId) {
		return findByCriteria(QueryParam.getInstance().addParam("user", new User(userId)).addOrder("sort"));
	}

	@Override
	public UserPwdQuestion find(long id, long userId) {
		return this.find(id);
	}

	@Override
	public List<UserPwdQuestion> listRand(long userId) {
		String sql = "SELECT * FROM rd_user_pwd_question WHERE user_id = :userId ORDER BY RAND()";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		String[] names = new String[] { "userId" };
		Object[] values = new Object[] { userId };
		return listBySql(sql, names, values, UserPwdQuestion.class);
	}

	@Override
	public UserPwdQuestion findByQuestion(String question, long userId) {
		QueryParam param = QueryParam.getInstance().addParam("question", question).addParam("user.userId", userId);
		return super.findByCriteriaForUnique(param);
	}

}
