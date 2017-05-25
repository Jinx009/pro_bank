package com.rongdu.p2psys.cooperation.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongdu.p2psys.cooperation.domain.CooperationLogin;
import com.rongdu.p2psys.cooperation.service.CooperationLoginService;
import com.rongdu.p2psys.cooperation.util.qq.OpenQqUtils;
import com.rongdu.p2psys.cooperation.util.qq.QqGetUserInfoParamBean;
import com.rongdu.p2psys.cooperation.util.qq.QqGetUserInfoResultBean;

/**
 * 联合登陆方式 --QQ登陆
 * 
 * @author sj
 * @version 2.0
 * @since 2014年5月28日
 */
public class CooperationQQLoginWay implements CooperationLoginWay {

	private CooperationLoginService cooperationLoginService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String code;

	public CooperationQQLoginWay() {

	}

	public CooperationQQLoginWay(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public CooperationQQLoginWay(HttpServletRequest request, HttpServletResponse response, String code) {
		this.request = request;
		this.response = response;
		this.code = code;
	}

	public void cooperationLogin() throws Exception {
		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();
		request.setAttribute("qqLoginUrl", oqu.getQqLoginUrl());
		response.sendRedirect(oqu.getQqLoginUrl());

	}

	public CooperationLogin cooperationLoginSuccess() throws Exception {

		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();
		// 动态拼接换取accessToken的URL
		String accessTokenUrl = oqu.getAccessTokenUrl(code);
		// 获得AccessToken
		String accessToken = oqu.getAccessToken(accessTokenUrl);
		// 获得OpenId
		String openId = oqu.getOpenId(accessToken);

		if (openId == null || openId.length() <= 0 || accessToken == null || accessToken.length() <= 0) {
			request.getRequestDispatcher("/user/userBind.html").forward(request, response);
		}
		/** 获取登录用户信息 */
		QqGetUserInfoParamBean paramBean = new QqGetUserInfoParamBean();
		paramBean.setAccessToken(accessToken);
		paramBean.setOpenId(openId);
		QqGetUserInfoResultBean userInfoResultBean = oqu.getUserInfo(paramBean);
		// 判断qq合作登录的接口 获取合作登录会员基本信息失败操作 QQ合作登录_QQ登录信息验证失败
		if (userInfoResultBean.getErrorFlg()) {
			request.getRequestDispatcher("/user/userBind.html").forward(request, response);
		}

		String getNickname = userInfoResultBean.getNickName();
		if (getNickname != null && getNickname.length() > 0) {
			request.setAttribute("nickname", getNickname);
		}

		// 封装联合登陆信息
		CooperationLogin cooperation = new CooperationLogin();
		cooperation.setOpenId(openId);
		cooperation.setOpenKey(accessToken);
		cooperation.setType(1);// 1 QQ登录
		cooperationLoginService.addCooperationLogin(cooperation);

		return cooperationLoginService.getCooperationLogin(openId, 1);

	}

}
