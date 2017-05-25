package com.rongdu.p2psys.cooperation.util.qq;

/**
 * 获取登录用户信息
 */
public class QqGetUserInfoParamBean {

	/** session中的accessToken */
	private String accessToken;

	/** session中的openId */
	private String openId;

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
