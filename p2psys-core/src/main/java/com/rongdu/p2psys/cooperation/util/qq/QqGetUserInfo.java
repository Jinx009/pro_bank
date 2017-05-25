package com.rongdu.p2psys.cooperation.util.qq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.cooperation.util.json.JSONException;
import com.rongdu.p2psys.cooperation.util.json.JSONObject;

/**
 * 获取用户信息
 * 
 * @author HaoLiang
 * @version 0.1.1
 */
public class QqGetUserInfo {

	/** 日志 */
	private Logger log = Logger.getLogger(QqGetUserInfo.class);

	/** 工具类 */
	private OpenQqUtils oqu = new OpenQqUtils();

	/**
	 * 获取用户信息 返回数据都包含在Map里，具体如何取值参照官方文档。 map.get(参数名) 就能获取到对应的值 例如： 获取登陆用户的性别。 Map.get("gender");
	 * 
	 * @return 用户信息
	 * @throws IOException
	 */
	public QqGetUserInfoResultBean getUserInfo(QqGetUserInfoParamBean paramBean) throws IOException {

		// QQ互联工具类
		OpenQqUtils oqu = new OpenQqUtils();

		// 获取接口返回的数据
		String interfaceData = oqu.doGet(this.getUserInfoUrl(paramBean));

		return this.jsonToBean(interfaceData);
	}

	/**
	 * 动态拼接获取用户数据的url
	 * 
	 * @return 获取用户数据的url
	 */
	private String getUserInfoUrl(QqGetUserInfoParamBean paramBean) {

		// 动态拼接获取用户数据的url
		StringBuilder userInfoUrl = new StringBuilder();

		// 获取用户数据接口的URl
		userInfoUrl.append(OpenQqConstants.QQ_USER_INFO_URL);

		// 应用的appid
		userInfoUrl.append("?oauth_consumer_key=" + oqu.getConfigValue("qq.appid"));

		// 应用的accessToken
		userInfoUrl.append("&access_token=" + paramBean.getAccessToken());

		// 应用的OpenID
		userInfoUrl.append("&openid=" + paramBean.getOpenId());

		return userInfoUrl.toString();
	}

	/**
	 * 把接口返回的json数据转换成JavaBean
	 * 
	 * @param jsonData 接口返回的数据
	 * @return JavaBean数据
	 * @throws JSONException
	 */
	private QqGetUserInfoResultBean jsonToBean(String jsonData) {
		QqGetUserInfoResultBean resultBean = new QqGetUserInfoResultBean();

		// 接口返回的数据json
		JSONObject jsonObjRoot;
		try {
			jsonObjRoot = new JSONObject(jsonData);

			// 接口返回错误的场合
			if (jsonObjRoot.getInt("ret") != 0) {
				// 设置错误标识为真
				resultBean.setErrorFlg(true);

				// 设置错误编号
				resultBean.setErrorCode(jsonObjRoot.get("ret").toString());

				// 设置错误信息
				resultBean.setErrorMes(jsonObjRoot.getString("msg"));

				// 日志
				log.error("获取用户信息出错。错误编号：" + jsonObjRoot.get("ret").toString());
			} else {
				// 昵称
				resultBean.setNickName(jsonObjRoot.getString("nickname"));

				// 头像URL
				resultBean.setFigureUrl(jsonObjRoot.getString("figureurl"));

				// 头像URL
				resultBean.setFigureUrl1(jsonObjRoot.getString("figureurl_1"));

				// 头像URL
				resultBean.setFigureUrl2(jsonObjRoot.getString("figureurl_2"));

				// 性别
				resultBean.setGender(jsonObjRoot.getString("gender"));

				// 是否为黄钻
				resultBean.setIsVip(jsonObjRoot.getString("vip"));

				// 黄钻等级
				resultBean.setLevel(jsonObjRoot.getString("level"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// 日志
			log.error("获取用户信息出错。接口返回数据：" + jsonData);
		}

		return resultBean;
	}

}
