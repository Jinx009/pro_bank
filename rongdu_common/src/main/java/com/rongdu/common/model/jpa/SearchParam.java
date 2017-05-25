package com.rongdu.common.model.jpa;

import java.util.Date;

import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;

/**
 * 查询Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月4日
 */
public class SearchParam {

	/** 状态，99代表全部 */
	private int status = 99;
	/** 类型，99代表全部 */
	private int type = 99;
	/** 名称 */
	private String name;
	/** 用户名 */
	private String userName;
	/** 实名 */
	private String realName;
	/** Email */
	private String email;
	/** 手机号 */
	private String mobilePhone;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;

	public QueryParam initQueryParam() {
		QueryParam param = QueryParam.getInstance();
		if (getStatus() != 99) {
			param.addParam("status", Operators.LIKE, getStatus());
		}
		if (StringUtil.isNotBlank(getName())) {
			param.addParam("name", Operators.LIKE, getName());
		}

		if (StringUtil.isNotBlank(getStartTime())) {
			Date start = DateUtil.valueOf(getStartTime() + " 00:00:00");
			param.addParam("addTime", Operators.GTE, start);
		}
		if (StringUtil.isNotBlank(getEndTime())) {
			Date end = DateUtil.valueOf(getEndTime() + " 23:59:59");
			param.addParam("addTime", Operators.LTE, end);
		}

		return param;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
