package com.rongdu.p2psys.nb.vip.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Consultant;
import com.rongdu.p2psys.user.domain.User;

/**
 * 私人顾问
 * 
 * @author ysh
 * @version 2.0
 * @Date 2015年9月17日
 */
@Entity
@Table(name = ("nb_vip_consultant"))
public class VipConsultant implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	 /**
	 * 客户姓名
	 */
	private String realName;

	/*
	 *手机号 
	 **/
	private String mobilePhone;
	/*
	 * 预约产品名
	 **/
	private String productName;
	
	/*客户性别*/
	private String sex;
	
	/**
	 * 预约专家
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consultant_id")
	private Consultant consultant;
	
	/**
	 * 状态，是否已受理 1已受理 0未受理
	 */
	private int status;
	/**
	 * 预约时间一
	 */
	private String timeFirst;

	/**
	 * 预约时间二
	 */
	private String timeSecond;

	/**
	 * 预约时间
	 */
	private Date addTime;

	/**
	 * IP
	 */
	private String addIp;
	
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



	public String getProductName() {
		return productName;
	}



	public void setProductName(String productName) {
		this.productName = productName;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Consultant getConsultant() {
		return consultant;
	}

	public void setConsultant(Consultant consultant) {
		this.consultant = consultant;
	}

	public String getTimeFirst() {
		return timeFirst;
	}

	public void setTimeFirst(String timeFirst) {
		this.timeFirst = timeFirst;
	}

	public String getTimeSecond() {
		return timeSecond;
	}

	public void setTimeSecond(String timeSecond) {
		this.timeSecond = timeSecond;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

}
