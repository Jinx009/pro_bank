package com.rongdu.p2psys.util;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class HttpUtil {
	/**
	 * post请求Form数据
	 * @param 
	 * @param dataStr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "resource"})
	public static String postParams(String httpUrl)throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();  
        HttpPost post = new HttpPost(httpUrl);

        HttpResponse response = httpClient.execute(post); 
        String jsonStr = EntityUtils.toString(response.getEntity(),"utf-8"); 
		return jsonStr;
	}
	
	/**
	 * post请求json
	 * @param httpUrl
	 * @param dataStr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "resource"})
	public static String postJson(String httpUrl,String jsonStr)throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();  
		HttpPost post = new HttpPost(httpUrl);

		post.setEntity(new StringEntity(jsonStr,"utf-8"));
		HttpResponse response = httpClient.execute(post);
        String resultStr = EntityUtils.toString(response.getEntity(),"utf-8"); 
		return resultStr;
	}
	
	/**
	 * get请求
	 * @param httpUrl
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "resource"})
	public static String get(String httpUrl)throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(httpUrl);
		HttpResponse httpResponse;
		httpResponse = client.execute(httpGet);
		String jsonStr = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
		return jsonStr;
	}
	
	/**
	 * 数组转json
	 * @param list
	 * @return
	 */
	public static String getListJson(List<?> list){
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}
	
	/**
	 * 返回json
	 * @param object
	 * @return
	 */
	public static String getObjectJson(Object object){
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
	}
}
