package com.rongdu.p2psys.util;


public class HttpData {
	public static final String HTTP_CHAR_SET = "utf-8";
	
	public static final String HOST_URL_800BANK = "http://www.800bank.com.cn/";
	public static final String HOST_URL_800BANK_TEST = "http://test.e800bank.com/";
	//public static final String HOST_URL_800BANK_TEST="http://localhost:8089/";
	
	
	public static String getLoginUrl(String userName,String pwd){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(HOST_URL_800BANK_TEST);
		buffer.append("app/doLogin.html?userName=");
		buffer.append(userName);
		buffer.append("&pwd=");
		buffer.append(pwd);
		
		return buffer.toString();
	}
	
	public static String userExists(String mobilePhone){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(HOST_URL_800BANK_TEST);
		buffer.append("nb/pc/userExists.html?mobilePhone=");
		buffer.append(mobilePhone);
		
		return buffer.toString();
	}
	
	public static String registerUrl(String userName,String pwd){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(HOST_URL_800BANK_TEST);
		buffer.append("/app/doRegister.html?userName=");
		buffer.append(userName);
		buffer.append("&pwd=");
		buffer.append(pwd);
		
		return buffer.toString();
	}
	
	public static String changePwd(String userName,String pwd,String newPwd){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(HOST_URL_800BANK_TEST);
		buffer.append("app/checkpwd.html?userName=");
		buffer.append(userName);
		buffer.append("&pwd=");
		buffer.append(pwd);
		buffer.append("&newPwd=");
		buffer.append(newPwd);
		
		return buffer.toString();
	}
	
	public static String changePayPwd(String userName,String payPwd,String newPayPwd){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(HOST_URL_800BANK_TEST);
		buffer.append("app/checkPayPwd.html?userName=");
		buffer.append(userName);
		buffer.append("&payPwd=");
		buffer.append(payPwd);
		buffer.append("&newPayPwd=");
		buffer.append(newPayPwd);
		
		return buffer.toString();
	}
	
	public static String forgetPwd(String userName,String pwd){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(HOST_URL_800BANK_TEST);
		buffer.append("app/forgetpwd.html?userName=");
		buffer.append(userName);
		buffer.append("&pwd=");
		buffer.append(pwd);
		
		return buffer.toString();
	}
}
