package com.rongdu.p2psys.cooperation.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongdu.p2psys.cooperation.domain.CooperationLogin;

/**
 * 联合登陆方式
 * 
 * @author sj
 * @version 2.0
 * @since 2014年5月28日
 */
public class CooperationLoginFactory {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String style;
	private String code;

	public CooperationLoginFactory() {

	}

	public CooperationLoginFactory(HttpServletRequest request, HttpServletResponse response, String style, String code) {
		this.request = request;
		this.response = response;
		this.style = style;
		this.code = code;
	}

	public CooperationLoginWay cooperationLogin() throws Exception {
		// QQ联合登录
		if (CooperationLogin.TYPE_QQ.equals(style)) {
			return new CooperationQQLoginWay(request, response);
			// 新浪微博联合登录
		} else if (CooperationLogin.TYPE_SINA.equals(style)) {
			return new CooperationSinaLoginWay(request, response);
		}
		return null;
	}

	public CooperationLoginWay cooperationLoginSuccess() throws Exception {
		// QQ联合登录
		if (CooperationLogin.TYPE_QQ.equals(style)) {
			return new CooperationQQLoginWay(request, response, code);
			// 新浪微博联合登录
		} else if (CooperationLogin.TYPE_SINA.equals(style)) {
			return new CooperationSinaLoginWay(request, response);
		}
		return null;
	}

}
