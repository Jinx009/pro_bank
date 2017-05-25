package com.rongdu.p2psys.cooperation.util.qq;

/**
 * 获取登录用户信息
 */
public class QqGetUserInfoResultBean {

	/** 错误标识 */
	private boolean errorFlg = false;

	/** 错误编号 */
	private String errorCode;

	/** 错误信息 */
	private String errorMes;

	/** 昵称 */
	private String nickName;

	/** 头像URL */
	private String figureUrl;

	/** 头像URL */
	private String figureUrl1;

	/** 头像URL */
	private String figureUrl2;

	/** 性别 */
	private String gender;

	/** 是否是黄钻 */
	private String isVip;

	/** 黄钻等级 */
	private String level;

	/**
	 * @return the errorFlg
	 */
	public boolean getErrorFlg() {
		return errorFlg;
	}

	/**
	 * @param errorFlg the errorFlg to set
	 */
	public void setErrorFlg(boolean errorFlg) {
		this.errorFlg = errorFlg;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMes
	 */
	public String getErrorMes() {
		return errorMes;
	}

	/**
	 * @param errorMes the errorMes to set
	 */
	public void setErrorMes(String errorMes) {
		this.errorMes = errorMes;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the figureUrl
	 */
	public String getFigureUrl() {
		return figureUrl;
	}

	/**
	 * @param figureUrl the figureUrl to set
	 */
	public void setFigureUrl(String figureUrl) {
		this.figureUrl = figureUrl;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the isVip
	 */
	public String getIsVip() {
		return isVip;
	}

	/**
	 * @param isVip the isVip to set
	 */
	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the figureUrl1
	 */
	public String getFigureUrl1() {
		return figureUrl1;
	}

	/**
	 * @param figureUrl1 the figureUrl1 to set
	 */
	public void setFigureUrl1(String figureUrl1) {
		this.figureUrl1 = figureUrl1;
	}

	/**
	 * @return the figureUrl2
	 */
	public String getFigureUrl2() {
		return figureUrl2;
	}

	/**
	 * @param figureUrl2 the figureUrl2 to set
	 */
	public void setFigureUrl2(String figureUrl2) {
		this.figureUrl2 = figureUrl2;
	}

}
