package com.rongdu.p2psys.cooperation.util.qq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.rongdu.p2psys.cooperation.util.json.JSONException;
import com.rongdu.p2psys.cooperation.util.json.JSONObject;


/**
 * QQ互联工具类 获取各个接口的URL等工具类
 */
public class OpenQqUtils {

	/** 日志 */
	private static Logger log = Logger.getLogger(OpenQqUtils.class);

	/** 资源属性 */
	private static Properties properties;

	static {
		properties = new Properties();
		try {
			// 读取配置文件
			properties.load(OpenQqUtils.class.getResourceAsStream(OpenQqConstants.QQ_CONFIG_NAME));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("读取配置文件出错，请确认message/config.properties文件已经放到src目录下。");
		}
	}

	/**
	 * 获取配置信息
	 * 
	 * @param configKey
	 * @return
	 */
	public String getConfigValue(String configKey) {
		String configValue = null;
		configValue = properties.getProperty(configKey);
		if (null == configValue || "".equals(configValue)) {
			log.info("没有获取指定key的值，请确认资源文件中是否存在【" + configKey + "】");
		}
		return configValue;
	}

	/**
	 * 链接QQ服务接口
	 * 
	 * @param interfaceUrl 接口URL
	 * @return 接口返回的数据
	 * @throws IOException
	 */
	public String doGet(String interfaceUrl) throws IOException {
		// 打印日志
		log.info("doGet:" + interfaceUrl);

		String interfaceData = "";

		// 获取默认的HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// 获取Get连接
		HttpGet httpGet = new HttpGet(interfaceUrl);

		// 请求post连接
		HttpResponse response = httpclient.execute(httpGet);

		// 打印日志
		log.info("doGet请求状态Code:" + response.getStatusLine().getStatusCode());

		// 是否请求正常
		if (200 == response.getStatusLine().getStatusCode()) {

			// 获取链接返回的数据
			HttpEntity resEntity = response.getEntity();

			// 获取返回的数据流
			BufferedReader input = new BufferedReader(new InputStreamReader(resEntity.getContent(), "UTF-8"));
			String tempStr = "";

			// 获取返回的内容
			while ((tempStr = input.readLine()) != null) {
				interfaceData += tempStr.replace("\t", "");
			}
		}
		// 关闭连接
		httpGet.abort();

		// 打印日志
		log.info("doGet返回的json数据：" + interfaceData);

		return interfaceData;
	}

	/**
	 * 发送Post请求
	 * 
	 * @param url 请求地址
	 * @param reqEntity 请求参数
	 * @return 接口返回的数据
	 * @throws IOException
	 */
	public String doPost(String url, MultipartEntity reqEntity) throws IOException {
		// 打印日志
		log.info("doPost:" + url);

		// 接口返回的json数据
		String interfaceData = "";

		// 获取默认的HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// 获取post连接
		HttpPost httpPost = new HttpPost(url);

		// 设置连接参数
		httpPost.setEntity(reqEntity);

		// 请求post连接
		HttpResponse response = httpclient.execute(httpPost);

		// 打印日志
		log.info("doPost请求状态Code:" + response.getStatusLine().getStatusCode());

		// 是否请求正常
		if (200 == response.getStatusLine().getStatusCode()) {

			// 获取链接返回的数据
			HttpEntity resEntity = response.getEntity();

			// 获取返回的数据流
			BufferedReader input = new BufferedReader(new InputStreamReader(resEntity.getContent(), "UTF-8"));
			String tempStr = "";

			// 获取返回的内容
			while ((tempStr = input.readLine()) != null) {
				interfaceData += tempStr.replace("\t", "");
			}
		}
		// 关闭连接
		httpPost.abort();

		// 打印日志
		log.info("doPost返回的json数据：" + interfaceData);

		return interfaceData;
	}

	/**
	 * 判断字符串是否为null或""
	 * 
	 * @param pStr 字符串
	 * @return false 为null 或 "" true 非空
	 */
	public boolean isNotNull(String pStr) {
		return null != pStr && !"".equals(pStr);
	}

	/**
	 * 把时间戳转换成指定的格式
	 * 
	 * @return 格式后的日期字符串
	 */
	public String timeStampToDate(String timeStamp) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// php的时间戳需要补三个0才可以正常转换
		return df.format(Long.valueOf(timeStamp + "000"));
	}

	/**
	 * 用 authorizationCode 换取 AccessToken
	 * 
	 * @param accessTokenUrl 换取accessToken的URL
	 * @return AccessToken
	 * @throws IOException
	 */
	public String getAccessToken(String accessTokenUrl) throws IOException {

		String accessToken = "";

		// 接受返回的字符串 包含accessToken
		String tempStr = "";

		// 请求QQ接口，回去返回数据
		tempStr = this.doGet(accessTokenUrl);

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
	 * @param accessToken AccessToken
	 * @return OpenId
	 * @throws IOException
	 * @throws JSONException
	 */
	public String getOpenId(String accessToken) throws IOException {

		// 获取OpenId
		String openId = null;

		// 请求QQ接口，回去返回数据
		String interfaceData = this.doGet(this.getOpenIdUrl(accessToken));

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
	public String getAccessTokenUrl(String authorizationCode) {
		StringBuilder accessTokenUrl = new StringBuilder();

		// 通过Authorization Code获取Access Token 的URl
		accessTokenUrl.append(OpenQqConstants.QQ_ACCESS_TOKEN_URL);

		// QQ登陆地址 用于获取Authorization Code
		accessTokenUrl.append("?grant_type=authorization_code");

		// 申请QQ登录成功后，分配给应用的appid
		accessTokenUrl.append("&client_id=" + this.getConfigValue("qq.appid"));

		// 申请QQ登录成功后，分配给网站的appkey
		accessTokenUrl.append("&client_secret=" + this.getConfigValue("qq.appkey"));

		// 登陆成功后返回的authorization code
		accessTokenUrl.append("&code=" + authorizationCode);

		// client端的状态值。用于第三方应用防止CSRF攻击，成功授权后回调时会原样带回。
		accessTokenUrl.append("&state=javaSdk-token");

		// 成功授权后的回调地址，建议设置为网站首页或网站的用户中心。
		accessTokenUrl.append("&redirect_uri=" + this.getConfigValue("qq.callback"));

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

	/**
	 * 获取QQ登录页面的url
	 * 
	 * @return 登录页面的url
	 */
	public String getQqLoginUrl() {

		// 获取QQ登录页面的url
		StringBuilder qqLoginUrl = new StringBuilder();

		// QQ登陆地址 用于获取Authorization Code
		qqLoginUrl.append(OpenQqConstants.QQ_LOGIN_URL);

		// 授权类型，此值固定为“code”
		qqLoginUrl.append("?response_type=code");

		// 申请QQ登录成功后，分配给应用的appid
		qqLoginUrl.append("&client_id=" + this.getConfigValue("qq.appid"));

		// 成功授权后回调的网站地址。
		qqLoginUrl.append("&redirect_uri=" + this.getConfigValue("qq.callback"));

		// 成功授权后回调后，跳至具体的页面。
		qqLoginUrl.append(this.getConfigValue("kffw.callback"));

		// 请求用户授权时向用户显示的可进行授权的列表。如果要填写多个接口名称，请用逗号隔开。
		qqLoginUrl.append("&scope=" + this.getConfigValue("qq.scope"));

		// client端的状态值。用于第三方应用防止CSRF攻击，成功授权后回调时会原样带回。
		qqLoginUrl.append("&state=javaSdk-code");

		// 用于展示的样式。不传则默认展示为为PC下的样式。
		// 如果传入“mobile”，则展示为mobile端下的样式。
		qqLoginUrl.append("&display=");

		return qqLoginUrl.toString();
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
		userInfoUrl.append("?oauth_consumer_key=" + this.getConfigValue("qq.appid"));

		// 应用的accessToken
		userInfoUrl.append("&access_token=" + paramBean.getAccessToken());

		// 应用的OpenID
		userInfoUrl.append("&openid=" + paramBean.getOpenId());

		return userInfoUrl.toString();
	}

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
		log.error(interfaceData);
		return this.jsonToBean(interfaceData);
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
