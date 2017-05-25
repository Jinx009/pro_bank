package com.rongdu.p2psys.user.model;

import javax.persistence.Entity;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 用户认证信息Model
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月19日
 */
@Entity
public class UserIdentifyModel extends UserIdentify {
	/** 用户名 */
	private String userName;
	/** 用户类型 */
	private String userType;

	/** 真实姓名 */
	private String realName;
	/** 身份证号码 */
	private String cardId;

	/** 邮箱 */
	private String email;
	/** 手机号码 */
	private String mobilePhone;
	/** 性别 */
	private int sex;
	/** 生日 */
	private int birthday;
	/** 用户婚姻状态 */
	private int marriageStatus;
	/** 住宅状态 */
	private int residenceType;
	/** 用户住宅地址 */
	private String residenceAddress;
	/** 就业状态 */
	private int employmentStatus;
	/** 是否购车 */
	private int hasBuyCar;
	/** 起始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/**
	 * 状态
	 */
	private int status;
	private User user;

	public static UserIdentifyModel instance(UserIdentify userIdentify) {
		UserIdentifyModel userIdentifyModel = new UserIdentifyModel();
		BeanUtils.copyProperties(userIdentify, userIdentifyModel);
		return userIdentifyModel;
	}
	
	public static UserIdentify instance(UserIdentifyModel um) {
		UserIdentify userIdentify = new UserIdentify();
		BeanUtils.copyProperties(um, userIdentify);
		return userIdentify;
	}

	public UserIdentify prototype() {
		UserIdentify userIdentify = new UserIdentify();
		BeanUtils.copyProperties(this, userIdentify);
		return userIdentify;
	}

	public void validRealNameStatus(UserIdentify attestation) {
		if (attestation.getRealNameStatus() != 1) {
			throw new UserException("请先进行实名认证", 1);
		}
	}

	/**
	 * 绑定邮箱 校验用户邮箱认证状态
	 * 
	 * @return
	 */
	public int validAttestationForBindEmail() {
		if (getEmailStatus() == 1) {
			throw new UserException("您已通过邮箱认证！不能重复绑定！", 1);
		}
		return -1;
	}

	/**
	 * 绑定手机 校验用户手机认证状态
	 * 
	 * @return
	 */
	public int validAttestationForBindPhone() {
		if (getMobilePhoneStatus() == 1) {
			throw new UserException("您已通过手机认证！不能重复绑定！", 1);
		}
		return -1;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
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

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getBirthday() {
		return birthday;
	}

	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	public int getMarriageStatus() {
		return marriageStatus;
	}

	public void setMarriageStatus(int marriageStatus) {
		this.marriageStatus = marriageStatus;
	}

	public int getResidenceType() {
		return residenceType;
	}

	public void setResidenceType(int residenceType) {
		this.residenceType = residenceType;
	}

	public String getResidenceAddress() {
		return residenceAddress;
	}

	public void setResidenceAddress(String residenceAddress) {
		this.residenceAddress = residenceAddress;
	}

	public int getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(int employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public int getHasBuyCar() {
		return hasBuyCar;
	}

	public void setHasBuyCar(int hasBuyCar) {
		this.hasBuyCar = hasBuyCar;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
