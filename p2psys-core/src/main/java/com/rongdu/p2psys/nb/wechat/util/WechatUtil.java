package com.rongdu.p2psys.nb.wechat.util;

import java.io.IOException;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class WechatUtil
{
	/**
	 * Create menu access_token
	 * @param APP_ID
	 * @param APP_SECRET
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getAccessToken(String APP_ID,String APP_SECRET) throws ClientProtocolException,IOException
	{
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
		url = url+ "&appid="+APP_ID+"&secret="+APP_SECRET;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse;
		
		String access_token = "";
		
		httpResponse = client.execute(httpGet);

		int code = httpResponse.getStatusLine().getStatusCode();
		String strResult = EntityUtils.toString(httpResponse.getEntity(),WechatMessageData.CHAR_SET);
		if (code == 200)
		{
			JSONObject jsonObject = JSONObject.fromObject(strResult);
			String token = jsonObject.getString("access_token");
			access_token = token;
		}

		return access_token;
	}

	
	/**
	 * 模板消息
	 * @param data
	 * @param access_token
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String sendMoneyOrder(String data,String access_token) throws ParseException, IOException
	{
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(data, WechatMessageData.CHAR_SET));
		HttpResponse response = httpClient.execute(post);
		String jsonStr = EntityUtils.toString(response.getEntity(),WechatMessageData.CHAR_SET);

		System.out.println(jsonStr);
		
		return jsonStr;
	}
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		//WechatData wechatData = new WechatData("9a6177b4f6ed4392acc9c022c1e66827@123456789","fb41ce78bb4f4157bebadf96012e4ce51234567",1);
		
		//System.out.println(getRealThirdPartyId(wechatData));
		
		//System.out.println(getAccessToken(WechatData.A_APP_ID,WechatData.A_APP_SECRET));
		
		//createMenu("","");
		
		//System.out.println(getAccessToken(WechatData.A_APP_ID, WechatData.A_APP_SECRET));
		
	}
}
