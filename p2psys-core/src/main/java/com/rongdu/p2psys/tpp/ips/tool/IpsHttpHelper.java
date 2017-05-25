package com.rongdu.p2psys.tpp.ips.tool;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class IpsHttpHelper {
	
	private String url="";
	
	private NameValuePair[] params;
	
	private String charset="UTF-8";
	
	private HttpClient httpClient = new HttpClient();

	public IpsHttpHelper(String url, String[][] params, String charset) {
		super();
		this.url = url;
		this.params = wrapParam(params);;
		this.charset = charset;
	}

	public IpsHttpHelper(String url, String[][] params) {
		this(url,params,"UTF-8");
	}
	
	private NameValuePair[] wrapParam(String[][] params){
		NameValuePair[] data=new NameValuePair[params.length];
		for(int i=0;i<params.length;i++){
			data[i]=new NameValuePair(params[i][0],params[i][1]);
		}
		return data;
	}
	
	public String execute() throws HttpException, IOException{
		//GetMethod postMethod = new GetMethod(url);
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestBody(params);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
		int statusCode = 0;
		String respString="";
		try {
			statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK){
				respString = org.apache.commons.lang.StringUtils.trim((new String(postMethod.getResponseBody(),charset)));
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return respString;
	}
	

}
