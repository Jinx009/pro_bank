package com.rongdu.p2psys.cooperation.util.qq;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QqLoginServlet extends HttpServlet {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -1527777072775436166L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter out = resp.getWriter();
		out.println("<script>");
		out.println("window.location.href=\"" + this.getQqLoginUrl() + "\"");
		out.println("</script>");
		out.flush();
	}

	/**
	 * 获取QQ登录页面的url
	 * 
	 * @return 登录页面的url
	 */
	private String getQqLoginUrl() {

		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();

		// 获取QQ登录页面的url
		StringBuilder qqLoginUrl = new StringBuilder();

		// QQ登陆地址 用于获取Authorization Code
		qqLoginUrl.append(OpenQqConstants.QQ_LOGIN_URL);

		// 授权类型，此值固定为“code”
		qqLoginUrl.append("?response_type=code");

		// 申请QQ登录成功后，分配给应用的appid
		qqLoginUrl.append("&client_id=" + oqu.getConfigValue("qq.appid"));

		// 成功授权后的回调地址，建议设置为网站首页或网站的用户中心。
		qqLoginUrl.append("&redirect_uri=" + oqu.getConfigValue("qq.callback"));

		// 请求用户授权时向用户显示的可进行授权的列表。如果要填写多个接口名称，请用逗号隔开。
		qqLoginUrl.append("&scope=" + oqu.getConfigValue("qq.scope"));

		// client端的状态值。用于第三方应用防止CSRF攻击，成功授权后回调时会原样带回。
		qqLoginUrl.append("&state=javaSdk-code");

		// 用于展示的样式。不传则默认展示为为PC下的样式。
		// 如果传入“mobile”，则展示为mobile端下的样式。
		qqLoginUrl.append("&display=");

		return qqLoginUrl.toString();
	}

}
