package com.rongdu.p2psys.util;

public class WechatData
{

	/**
	 * test wechat
	
	public static final String A_APP_ID = "wx79e91ac19b15754b";
	public static final String A_APP_SECRET = "6e3402d7cf4cfb9c5dd53b0fe5dd404f";
	public static final String A_TOKEN = "800bank";
	public static final String OAUTH_URL_TWO = "&redirect_uri=http://jinx-wechat.dsyplus.com";
	 */
	
	/**
	 * 	
	public static final String A_APP_ID = "wxee8d92224d7cccf8";
	public static final String A_APP_SECRET = "f26d0b77496be9739b3feeb1f0e74401";
	public static final String A_TOKEN = "800bank";
	public static final String OAUTH_URL_TWO = "&redirect_uri=http://www.800bank.com.cn";
	 */
	
	/**
	 * 800众服
	 */
	public static final String A_APP_ID = "wxa37b227117def502";
	public static final String A_APP_SECRET = "30e4686c031f8020cbfc236381d73b6d";
	public static final String A_TOKEN = "800bank";
	public static final String OAUTH_URL_TWO = "&redirect_uri=http://www.800zf.cn";
	
	
	public static final String OAUTH_URL_ONE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+A_APP_ID;
	public static final String OAUTH_URL_THREE = "/nb/wechat/wechat_bind.action?redirectURL=";
	public static final String OAUTH_URL_THREE_ANOTHER = "/nb/wechat/wechat_bind.action?ui=";
	public static final String OAUTH_URL_FOUR = "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
	public static final String OAUTH_URL_FIVE = "&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";

	/**
	 * 二维码
	 */
	public static final String SHARE_URL_ONE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxee8d92224d7cccf8&redirect_uri=http://www.800bank.com.cn/nb/wechat/wechat_bind.action?ui=";
	public static final String SHARE_URL_TWO = "%26redirectURL=/nb/wechat/product/product_list.html?id=4&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect";
	
	public static final String CHAR_SET = "utf-8";
	
}
