package com.rongdu.p2psys.cooperation.util.qq;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.cooperation.util.json.JSONException;
import com.rongdu.p2psys.cooperation.util.json.JSONObject;

/**
 * QQ互联回调Servlet 登陆成功后调用的文件，获取登陆用户的个人信息。 并把openid、AccessToken和用户信息存入到request中。
 * 
 * @author HaoLiang
 * @version 0.1.1
 */
public class QqCallbackServlet extends HttpServlet {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2673913230227418920L;

	/** 日志 */
	private Logger log = Logger.getLogger(QqCallbackServlet.class);

	/** QQ互联工具类 */
	private OpenQqUtils oqu = new OpenQqUtils();

	/**
	 * 第三方接口调用
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 动态拼接换取accessToken的URL
		String accessTokenUrl = this.getAccessTokenUrl(req.getParameter("code"));

		// AccessToken
		String accessToken = this.getAccessToken(accessTokenUrl);

		// OpenId
		String openId = this.getOpenId(accessToken);

		// 获取session
		HttpSession session = req.getSession();

		// 把accessToken存入session中 用于其他接口请求
		session.setAttribute("accessToken", accessToken);

		// 把openId存入session中 用于其他接口请求
		session.setAttribute("openId", openId);

		// 调转到网站页面接收页面
		resp.sendRedirect(oqu.getConfigValue("kffw.callback"));

	}

	/**
	 * 用 authorizationCode 换取 AccessToken
	 * 
	 * @param accessTokenUrl 换取accessToken的URL
	 * @return AccessToken
	 * @throws IOException
	 */
	private String getAccessToken(String accessTokenUrl) throws IOException {

		String accessToken = "";

		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();

		// 接受返回的字符串 包含accessToken
		String tempStr = "";

		// 请求QQ接口，回去返回数据
		tempStr = oqu.doGet(accessTokenUrl);

		// 获取accessToken失败的场合
		if (tempStr.indexOf("access_token") >= 0) {
			accessToken = tempStr.split("&")[0].split("=")[1];
			log.info("access_token:" + accessToken);
			return accessToken;
		} else {
			log.info("access_token获取失败。返回值：" + tempStr);
			return "";
		}
	}

	/**
	 * 根据AccessToken获取OpenId
	 * 
	 * @param accessToken 第三方Token
	 * @return OpenId
	 * @throws IOException IO异常
	 */
	private String getOpenId(String accessToken) throws IOException {

		// 获取OpenId
		String openId = null;

		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();

		// 请求QQ接口，回去返回数据
		String interfaceData = oqu.doGet(this.getOpenIdUrl(accessToken));

		// 接口返回的数据json
		JSONObject jsonObjRoot;
		try {
			// 去掉多余的字符串
			String jsonStr = interfaceData.substring(interfaceData.indexOf("{"), interfaceData.indexOf("}") + 1);

			// 获取json对象
			jsonObjRoot = new JSONObject(jsonStr);

			// 获取OpenId
			openId = jsonObjRoot.get("openid").toString();

			// 日志
			log.info("openid:" + openId);
		} catch (JSONException e) {
			e.printStackTrace();
			log.error("获取OpenId失败。返回数据：" + interfaceData);
		}

		return openId;
	}

	/**
	 * 动态拼接换取accessToken的URL
	 * 
	 * @param authorizationCode Authorization Code
	 * @return 换取accessToken的URL
	 */
	private String getAccessTokenUrl(String authorizationCode) {
		StringBuilder accessTokenUrl = new StringBuilder();

		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();

		// 通过Authorization Code获取Access Token 的URl
		accessTokenUrl.append(OpenQqConstants.QQ_ACCESS_TOKEN_URL);

		// QQ登陆地址 用于获取Authorization Code
		accessTokenUrl.append("?grant_type=authorization_code");

		// 申请QQ登录成功后，分配给应用的appid
		accessTokenUrl.append("&client_id=" + oqu.getConfigValue("qq.appid"));

		// 申请QQ登录成功后，分配给网站的appkey
		accessTokenUrl.append("&client_secret=" + oqu.getConfigValue("qq.appkey"));

		// 登陆成功后返回的authorization code
		accessTokenUrl.append("&code=" + authorizationCode);

		// client端的状态值。用于第三方应用防止CSRF攻击，成功授权后回调时会原样带回。
		accessTokenUrl.append("&state=javaSdk-token");

		// 成功授权后的回调地址，建议设置为网站首页或网站的用户中心。
		accessTokenUrl.append("&redirect_uri=" + oqu.getConfigValue("qq.callback"));

		// 日志
		log.info("获取AccessToken的url：" + accessTokenUrl.toString());

		return accessTokenUrl.toString();
	}

	/**
	 * 动态拼接换取OpenId的URL
	 * 
	 * @param accessToken AccessToken
	 * @return 换取OpenId的URL
	 */
	private String getOpenIdUrl(String accessToken) {

		// 换取OpenId的URL
		StringBuilder openIdUrl = new StringBuilder();

		// 使用Access Token来获取用户的OpenID 的URl
		openIdUrl.append(OpenQqConstants.QQ_OPEN_ID_URL);

		// ACCESS_TOKEN
		openIdUrl.append("?access_token=" + accessToken);

		// 日志
		log.info("获取OpenId的url：" + openIdUrl.toString());

		return openIdUrl.toString();
	}

}
