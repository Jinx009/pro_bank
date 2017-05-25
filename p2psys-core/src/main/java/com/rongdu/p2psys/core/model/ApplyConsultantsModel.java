package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.ApplyConsultants;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 私人顾问
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
public class ApplyConsultantsModel extends ApplyConsultants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 预约人真实姓名
	 */
	private String realName;

	/**
	 * 预约人手机号
	 */
	private String mobilePhone;

	/**
	 * 专家名字
	 */
	private String expertName;
	
	/**
	 * 搜索条件 专家姓名
	 */
	private String searchName;
	
	/**
	 * 申请开始时间
	 */
	private String startTime;
	
	/**
	 * 申请结束时间
	 */
	private String endTime;
	
	public static ApplyConsultantsModel instance(ApplyConsultants applyConsultants) {
		ApplyConsultantsModel model = new ApplyConsultantsModel();
		BeanUtils.copyProperties(applyConsultants, model);
		return model;
	}

	public void checkModel() {
		if (this.getTimeFirst() == null && this.getTimeSecond() == null) {
			throw new UserException("请选择预约时间", 1);
		}
		if (this.getConsultant() == null) {
			throw new UserException("请选择顾问", 1);
		}
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
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
