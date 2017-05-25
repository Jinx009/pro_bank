package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.dao.UserPwdQuestionDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPwdQuestionModel;
import com.rongdu.p2psys.user.service.UserPwdQuestionService;

@Service
public class UserPwdQuestionServiceImpl implements UserPwdQuestionService {

	@Resource
	private UserPwdQuestionDao userPwdQuestionDao;

	@Override
	public List<UserPwdQuestion> list(long userId) {
		return userPwdQuestionDao.list(userId);
	}

	@Override
	public UserPwdQuestion find(long id, long userId) {
		return userPwdQuestionDao.find(id, userId);
	}

	@Override
	public void add(UserPwdQuestionModel model) {
		// 提交验证
		model.validForm();
		List<UserPwdQuestion> list = new ArrayList<UserPwdQuestion>();
		UserPwdQuestion userPwdQuestion1 = new UserPwdQuestion(model.getUser());
		UserPwdQuestion userPwdQuestion2 = new UserPwdQuestion(model.getUser());
		UserPwdQuestion userPwdQuestion3 = new UserPwdQuestion(model.getUser());

		userPwdQuestion1.setId(model.getId1());
		userPwdQuestion1.setQuestion(model.getQuestion1());
		userPwdQuestion1.setAnswer(model.getAnswer1());
		userPwdQuestion1.setSort(1);
		list.add(userPwdQuestion1);

		userPwdQuestion2.setId(model.getId2());
		userPwdQuestion2.setQuestion(model.getQuestion2());
		userPwdQuestion2.setAnswer(model.getAnswer2());
		userPwdQuestion2.setSort(2);
		list.add(userPwdQuestion2);

		userPwdQuestion3.setId(model.getId3());
		userPwdQuestion3.setQuestion(model.getQuestion3());
		userPwdQuestion3.setAnswer(model.getAnswer3());
		userPwdQuestion3.setSort(3);
		list.add(userPwdQuestion3);

		userPwdQuestionDao.save(list);
	}

	@Override
	public List<UserPwdQuestion> listRand(long userId) {
		return userPwdQuestionDao.listRand(userId);
	}

	@Override
	public List<UserPwdQuestion> pwdQuestion(User user) {
		return userPwdQuestionDao.findByProperty("user.userId", user.getUserId());
	}

	@Override
	public void doAnswerPwdQuestion(UserModel model, User user) {
		UserPwdQuestion upq1 = userPwdQuestionDao.findByQuestion(model.getQuestion1(), user.getUserId());
		UserPwdQuestion upq2 = userPwdQuestionDao.findByQuestion(model.getQuestion2(), user.getUserId());
		UserPwdQuestion upq3 = userPwdQuestionDao.findByQuestion(model.getQuestion3(), user.getUserId());
		if (!upq1.getAnswer().equals(model.getAnswer1())) {
			throw new UserException("问题一的密保答案错误！", 1);
		} else if (!upq2.getAnswer().equals(model.getAnswer2())) {
			throw new UserException("问题二的密保答案错误！", 1);
		} else if (!upq3.getAnswer().equals(model.getAnswer3())) {
			throw new UserException("问题三的密保答案错误！", 1);
		}
	}

	@Override
	public void doSetPwdQuestion(UserModel model, User user) {
		// 提交验证
		model.validForm();
		// 删除原来密保问题中的记录
		List<UserPwdQuestion> l = userPwdQuestionDao.findByProperty("user.userId", user.getUserId());
		if (l != null && l.size() > 0) {
			userPwdQuestionDao.delete(l);
		}
		// 保存新增的数据
		List<UserPwdQuestion> list = new ArrayList<UserPwdQuestion>();
		UserPwdQuestion userPwdQuestion1 = new UserPwdQuestion(user);
		UserPwdQuestion userPwdQuestion2 = new UserPwdQuestion(user);
		UserPwdQuestion userPwdQuestion3 = new UserPwdQuestion(user);

		userPwdQuestion1.setQuestion(model.getQuestion1());
		userPwdQuestion1.setAnswer(model.getAnswer1());
		userPwdQuestion1.setSort(1);
		userPwdQuestion1.setAddTime(new Date());
		userPwdQuestion1.setAddIp(Global.getIP());
		list.add(userPwdQuestion1);

		userPwdQuestion2.setQuestion(model.getQuestion2());
		userPwdQuestion2.setAnswer(model.getAnswer2());
		userPwdQuestion2.setSort(2);
		userPwdQuestion2.setAddTime(new Date());
		userPwdQuestion2.setAddIp(Global.getIP());
		list.add(userPwdQuestion2);

		userPwdQuestion3.setQuestion(model.getQuestion3());
		userPwdQuestion3.setAnswer(model.getAnswer3());
		userPwdQuestion3.setSort(3);
		userPwdQuestion3.setAddTime(new Date());
		userPwdQuestion3.setAddIp(Global.getIP());
		list.add(userPwdQuestion3);

		userPwdQuestionDao.save(list);
	}

}
