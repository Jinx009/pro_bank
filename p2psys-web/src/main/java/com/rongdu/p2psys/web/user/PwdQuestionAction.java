package com.rongdu.p2psys.web.user;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;
import com.rongdu.p2psys.user.model.UserPwdQuestionModel;
import com.rongdu.p2psys.user.service.UserPwdQuestionService;

/**
 * 密保
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月26日
 */
public class PwdQuestionAction extends BaseAction implements ModelDriven<UserPwdQuestionModel> {

	private UserPwdQuestionModel model = new UserPwdQuestionModel();

	@Override
	public UserPwdQuestionModel getModel() {
		return model;
	}

	@Resource
	private UserPwdQuestionService userPwdQuestionService;

	private User user;

	/**
	 * 我的密保
	 * 
	 * @return
	 */
	@Action("/member/pwdquestion/detail")
	public String detail() throws Exception {
		user = getSessionUser();
		List<UserPwdQuestion> pwdQuestionList = userPwdQuestionService.list(user.getUserId());
		request.setAttribute("pwdQuestionList", pwdQuestionList);
		return "detail";
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action("/member/pwdquestion/add")
	public void add() throws Exception {
		user = getSessionUser();
		model.setUser(user);
		userPwdQuestionService.add(model);
		printWebSuccess();
	}

	/**
	 * 修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/pwdquestion/modify")
	public String modify() throws Exception {
		user = getSessionUser();
		List<UserPwdQuestion> pwdQuestionList = userPwdQuestionService.list(user.getUserId());
		request.setAttribute("pwdQuestionList", pwdQuestionList);

		return "modify";
	}

	/**
	 * 修改第一步 校验之前的密保
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/pwdquestion/doModifyStep1")
	public String doModifyStep1() throws Exception {

		return "modifyStep1";
	}

	/**
	 * 修改第二步 前往修改密保页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/pwdquestion/doModifyStep2")
	public String doModifyStep2() throws Exception {
		user = getSessionUser();
		List<UserPwdQuestion> pwdQuestionList = userPwdQuestionService.list(user.getUserId());
		request.setAttribute("pwdQuestionList", pwdQuestionList);
		return "modifyStep2";
	}

	/**
	 * 修改第三步 修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/pwdquestion/doModifyStep3")
	public void doModifyStep3() throws Exception {
		user = getSessionUser();
		model.setUser(user);
		userPwdQuestionService.add(model);
		printWebSuccess();
	}

}
