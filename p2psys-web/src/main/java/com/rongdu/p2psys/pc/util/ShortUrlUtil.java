package com.rongdu.p2psys.pc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


@SuppressWarnings("deprecation")
public class ShortUrlUtil
{
	 public static DefaultHttpClient httpclient;
	
	static
	{  
        httpclient = new DefaultHttpClient();  
    }  
	
	public static String getShortUrl(String urlData) throws ClientProtocolException, IOException
	{
		HttpPost httpost = new HttpPost("http://dwz.cn/create.php"); 
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("url", urlData)); 
        httpost.setEntity(new UrlEncodedFormEntity(params,  "utf-8"));  
        HttpResponse response = httpclient.execute(httpost);  
        String jsonStr = EntityUtils  .toString(response.getEntity(), "utf-8");  
		
		return jsonStr;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		String a = getShortUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxee8d92224d7cccf8&redirect_uri=http://www.800bank.com.cn/nb/wechat/wechat_bind.action?ui=7u58%26redirectURL=/nb/wechat/product/product_list.html?id=4&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect");
		System.out.println(a);
	}
}
