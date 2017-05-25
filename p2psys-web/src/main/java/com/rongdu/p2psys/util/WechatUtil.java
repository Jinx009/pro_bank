package com.rongdu.p2psys.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("deprecation")
public class WechatUtil {
	/**
	 * Parse XML
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) {
		InputStream inputStream = null;
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			inputStream = request.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();

			@SuppressWarnings("unchecked")
			List<Element> elementList = root.elements();
			for (Element e : elementList)
				map.put(e.getName(), e.getText());
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * Create menu access_token
	 * 
	 * @param APP_ID
	 * @param APP_SECRET
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getAccessToken(String APP_ID, String APP_SECRET)
			throws ClientProtocolException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
		url = url + "&appid=" + APP_ID + "&secret=" + APP_SECRET;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse;

		String access_token = "";

		httpResponse = client.execute(httpGet);

		int code = httpResponse.getStatusLine().getStatusCode();
		String strResult = EntityUtils.toString(httpResponse.getEntity(),
				WechatData.CHAR_SET);
		if (code == 200) {
			JSONObject jsonObject = JSONObject.fromObject(strResult);
			String token = jsonObject.getString("access_token");
			access_token = token;
		}

		return access_token;
	}

	/**
	 * 获取openid
	 * 
	 * @param appId
	 * @param appSecret
	 * @param code
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getOpenid(String code, String appId, String appSecret)
			throws ClientProtocolException, IOException {
		StringBuffer buffer = new StringBuffer();
		String openId = "";

		buffer.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
		buffer.append(appId);
		buffer.append("&secret=");
		buffer.append(appSecret);
		buffer.append("&code=");
		buffer.append(code);
		buffer.append("&grant_type=authorization_code");

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(buffer.toString());
		HttpResponse httpResponse;
		httpResponse = client.execute(httpGet);
		int httpCode = httpResponse.getStatusLine().getStatusCode();
		String strResult = EntityUtils.toString(httpResponse.getEntity(),
				WechatData.CHAR_SET);

		if (httpCode == 200) {
			JSONObject jsonObject = JSONObject.fromObject(strResult);
			if (jsonObject.has("openid")) {
				openId = jsonObject.getString("openid");
			}
		}

		return openId;
	}

	/**
	 * Get UserInfo First step
	 * 
	 * @param code
	 * @param APP_ID
	 * @param APP_SECRET
	 * @return
	 */
	public static JSONObject getUserInfoFirst(String code, String APP_ID,
			String APP_SECRET) {

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ APP_ID + "&secret=" + APP_SECRET + "&code=" + code
				+ "&grant_type=authorization_code";

		String result = "";
		JSONObject result2 = null;
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			connection.connect();

			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			JSONObject json = JSONObject.fromObject(result);

			if (null != json.get("openid").toString()&& !"".equals(json.get("openid").toString())) {
				result2 = json;
			} else {
				result2 = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result2;
	}
	
	/**
	 * Get Real UserInfo
	 * 
	 * @param code
	 * @param APP_ID
	 * @param APP_SECRET
	 * @return
	 */
	public static JSONObject getRealUserInfo(String ACCESS_TOKEN, String OPEND_ID) {

		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ ACCESS_TOKEN + "&openid=" + OPEND_ID + "&lang=zh_CN";

		String result = "";
		JSONObject result2 = null;
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			connection.connect();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			JSONObject json = JSONObject.fromObject(result);

			if (null != json.get("openid")) {
				result2 = json;
			} else {
				result2 = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result2;
	}

	/**
	 * Create Menu
	 * 
	 * @param token
	 * @param menuStr
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void createMenu(String token, String menuStr)
			throws ClientProtocolException, IOException {

		token = "SOWAm7XzS57kutVIk3At6u4GonQWg7miRwrdGI2mT6gzZ5xwrSMj8iHG1CwZGVPo7fANd6uExwe0hdct0J0gfStmpJeQormOzmRwFTWfqj4";

		menuStr = "{"
				+ "\"button\":["
				+ "{\"name\":\"众筹\","
				+ "\"type\":\"view\","
				+ "\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9fed4446a63b6c96&redirect_uri=http://new.teamserver.cn/wechat/wechat/productList.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect\""
				+ "},"
				+ "{\"name\":\"测试\","
				+ "\"type\":\"view\","
				+ "\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9fed4446a63b6c96&redirect_uri=http://new.teamserver.cn/wechat/wechat/storage.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect\""
				+ "},"
				+ "{\"name\":\"平台简介\","
				+ "\"type\":\"view\","
				+ "\"url\":\"http://java.dsyplus.com/EasyLifeAdminWeb/app/we-chat-data!appIndex.action\""
				+ "}," + "]" + "}";

		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
				+ token;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(menuStr, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);

		System.out.println(jsonStr);
	}

	/**
	 * Get pay id
	 * 
	 * @param xml
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getPrePay_id(String xml)
			throws ClientProtocolException, IOException {
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		@SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(xml, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);

		return jsonStr;
	}

	/**
	 * Encryption id util
	 * 
	 * @return
	 */
	public static String getNonce_str() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * Get java script ticket
	 * 
	 * @param appId
	 * @param appsecret
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public static String getJSApiTicket(String appId, String appsecret)
			throws ClientProtocolException, IOException {

		String currentJSApiTicket = null;
		Map<String, String> tokenMap;

		String accessToken = getAccessToken(appId, appsecret);

		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ accessToken + "&type=jsapi";

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse;

		httpResponse = client.execute(httpGet);

		int code = httpResponse.getStatusLine().getStatusCode();
		String strResult = EntityUtils.toString(httpResponse.getEntity(),
				WechatData.CHAR_SET);
		if (code == 200) {
			tokenMap = JSONObject.fromObject(strResult);
			if (tokenMap.get("errmsg").toString().equals("ok")) {
				currentJSApiTicket = tokenMap.get("ticket").toString();
			}
		}

		return currentJSApiTicket;
	}

	/**
	 * 取回菜单
	 * 
	 * @param token
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String retriveMenu(String token)
			throws ClientProtocolException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="
				+ token;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	/**
	 * 查询菜单
	 * 
	 * @param token
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String queryMenu(String token) throws ParseException,
			IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="
				+ token;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse;

		httpResponse = client.execute(httpGet);
		String jsonStr = EntityUtils.toString(httpResponse.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	/**
	 * 获取卡券
	 * 
	 * @param token
	 * @param postStr
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getTicket(String token, String postStr)
			throws ClientProtocolException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
				+ token;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(postStr, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	/**
	 * 关注用户列表
	 * 
	 * @param access_token
	 * @param postStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getAllWeChatUserGroup(String access_token,
			String postStr) throws ParseException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token="
				+ access_token;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(postStr, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	/**
	 * 向腾讯服务器传送文件
	 * 
	 * @param accessToken
	 * @param fileType
	 * @param httpFileUrl
	 * @return
	 */
	public static String sendFileToWeChatServer(String accessToken,
			String fileType, String httpFileUrl) {
		String weChatUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="
				+ accessToken + "&type=" + fileType + "";
		URL urlObj = null;
		String result = null;
		try {
			urlObj = new URL(weChatUrl);
			HttpURLConnection conn = (HttpURLConnection) urlObj
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", WechatData.CHAR_SET);

			URL httpFileUrlObj = new URL(httpFileUrl);
			URLConnection httpFileUrlConn = httpFileUrlObj.openConnection();
			String fileName = httpFileUrl.substring(
					httpFileUrl.lastIndexOf("/") + 1, httpFileUrl.length());

			String BOUNDARY = "----------" + System.currentTimeMillis();
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
					+ fileName + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			byte[] head = sb.toString().getBytes(WechatData.CHAR_SET);

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			out.write(head);

			InputStream in = httpFileUrlConn.getInputStream();
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();

			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n")
					.getBytes(WechatData.CHAR_SET);

			out.write(foot);
			out.flush();
			out.close();

			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			try {

				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				if (result == null) {
					result = buffer.toString();
				}
			} catch (IOException e) {
				System.out.println("发送POST请求出现异常！" + e);
				e.printStackTrace();
				throw new IOException("数据读取异常");
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("resource")
	public static String uploadNews(String token, String articleStr)
			throws ClientProtocolException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token="
				+ token;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(articleStr, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	@SuppressWarnings("resource")
	public static String sendGroupMessage(String accessToken, String message)
			throws ParseException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
				+ accessToken;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(message, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	@SuppressWarnings("resource")
	public static String getWeChatUserList(String accessToken)
			throws ClientProtocolException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="
				+ accessToken;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);
		return jsonStr;
	}

	/**
	 * 模板消息
	 * 
	 * @param data
	 * @param access_token
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String sendMoneyOrder(String data, String access_token)
			throws ParseException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
				+ access_token;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(data, WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);

		System.out.println(jsonStr);

		return jsonStr;
	}
	
	
	/**
	 * 解析获取支付id
	 * 
	 * @param xml
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getPrePayId(String xml)
			throws ClientProtocolException, IOException {
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		@SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(xml,WechatData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),
				WechatData.CHAR_SET);

		System.out.println(new StringBuffer().append("解析支付id:").append(jsonStr)
				.toString());

		return jsonStr;
	}

	/**
	 * Parse XML
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> parseXml(String xmlStr) {
		InputStream inputStream = null;
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			Document document = DocumentHelper.parseText(xmlStr);
			Element root = document.getRootElement();

			@SuppressWarnings("unchecked")
			List<Element> elementList = root.elements();
			for (Element e : elementList)
				map.put(e.getName(), e.getText());
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return map;
	}


	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		// WechatData wechatData = new
		// WechatData("9a6177b4f6ed4392acc9c022c1e66827@123456789","fb41ce78bb4f4157bebadf96012e4ce51234567",1);

		// System.out.println(getRealThirdPartyId(wechatData));

		// System.out.println(getAccessToken(WechatData.A_APP_ID,WechatData.A_APP_SECRET));

		// createMenu("","");

		// System.out.println(getAccessToken(WechatData.A_APP_ID,
		// WechatData.A_APP_SECRET));

		String access_token = getAccessToken(WechatData.A_APP_ID,
				WechatData.A_APP_SECRET);
		String openId = "oTXTUsrHHrl2yt4jbWUnn0B52Sv0";
		String msg = " { " + "\"touser\":\""
				+ openId
				+ "\","
				+ "\"template_id\":\"ISb5fDQ3z0hRIf0Cy0LFsxVtYwX6fR3VIiO1tX1rPwA\", "
				+ "\"url\":\"http://jinx-wechat.dsyplus.com/nb/wechat/account/800bank.action\","
				+ "\"topcolor\":\"#FF0000\"," + "\"data\":{ " + "\"first\": {"
				+ "\"value\":\"Jinx发给您的测试信息:\"," + "  \"color\":\"#173177\" "
				+ " }," + "\"productType\": {" + "\"value\":\"编号:001\","
				+ "  \"color\":\"#173177\" " + " }," + "\"time\": {"
				+ "\"value\":\"" + new Date() + "\","
				+ "  \"color\":\"#173177\" " + " }," + "\"remark\": {"
				+ "\"value\":\"备注:吾虽浪迹天涯，却从未迷失本心!\","
				+ "  \"color\":\"#173177\" " + " }" + " }" + "  }";

		sendMoneyOrder(msg, access_token);
	}
	
	
	
}
