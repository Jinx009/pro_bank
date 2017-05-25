package com.rongdu.p2psys.user.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.user.domain.UserCache;

/**
 * 个人资料-基本信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月18日15:14:10
 */
public class UserCacheModel extends UserCache {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 交易密码锁定
	 */
	public static final String PAY_PWD_LOCK = "payPwdLock";
	
	public static final String PWD_LOCK = "pwdLock";

	private String mobilePhone;

	private String email;
	
	private String contactsCardId;
	
	private String contactsRealName;
	
	private String contactsPhone;
	
	/**
	 * 是否已激活邮箱并初始化密码
	 */
	private boolean emailStatus;
	
	/**
	 * 交易密码是否锁定
	 */
	private boolean isPayPwdLock;
	
	/**
	 * 登录密码是否锁定
	 */
	private boolean isPwdLock;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactsCardId() {
        return contactsCardId;
    }

    public void setContactsCardId(String contactsCardId) {
        this.contactsCardId = contactsCardId;
    }

    public String getContactsRealName() {
		return contactsRealName;
	}

	public void setContactsRealName(String contactsRealName) {
		this.contactsRealName = contactsRealName;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public boolean isEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(boolean emailStatus) {
        this.emailStatus = emailStatus;
    }

    // 裁剪后的图像大小
	private double cropX;
	private double cropY;
	private double cropW;
	private double cropH;
	private String uploadFileName;

	public static UserCacheModel instance(UserCache userCache) {
		UserCacheModel userCacheModel = new UserCacheModel();
		BeanUtils.copyProperties(userCache, userCacheModel);
		return userCacheModel;
	}

	public UserCache prototype() {
		UserCache userCache = new UserCache();
		BeanUtils.copyProperties(this, userCache);
		return userCache;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public double getCropX() {
		return cropX;
	}

	public void setCropX(double cropX) {
		this.cropX = cropX;
	}

	public double getCropY() {
		return cropY;
	}

	public void setCropY(double cropY) {
		this.cropY = cropY;
	}

	public double getCropW() {
		return cropW;
	}

	public void setCropW(double cropW) {
		this.cropW = cropW;
	}

	public double getCropH() {
		return cropH;
	}

	public void setCropH(double cropH) {
		this.cropH = cropH;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public boolean isPayPwdLock() {
		//24小时内锁定
		if(this.getLockPayTime() != null && DateUtil.rollDay(this.getLockPayTime(), 1).after(new Date()) && this.getPayPwdStatus() == 1){
			return true;
		}
		return false;
	}

	public void setPayPwdLock(boolean isPayPwdLock) {
		this.isPayPwdLock = isPayPwdLock;
	}

	public boolean isPwdLock() {
		//24小时内锁定
		if(this.getLockTime() != null && DateUtil.rollDay(this.getLockTime(), 1).after(new Date()) && this.getLoginPwdStatus() == 1){
			return true;
		}
		return false;
	}

	public void setPwdLock(boolean isPwdLock) {
		this.isPwdLock = isPwdLock;
	}

}
