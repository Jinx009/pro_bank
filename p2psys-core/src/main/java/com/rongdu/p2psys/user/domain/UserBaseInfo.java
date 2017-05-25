package com.rongdu.p2psys.user.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * rd_user_base_info 实体类 用户基本信息
 * 
 * @author wzh
 * @version 2.0
 * @since 2014-11-04
 */
@Entity
@Table(name = "rd_user_base_info")
public class UserBaseInfo {

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/** 用户 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/** 出生年月日 */
	private Date birthday;

	/** 学历 1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他 */
	private int education;

	/** 婚姻状况 0:未婚；1：已婚；2：离异；3：丧偶 */
	private int maritalStatus;

	/** 户籍省 */
	private String province;
	
	/** 户籍市 */
	private String city;

	/** 工作年限 */
	private int workExperience;
	
	/** 月收入范围 */
	private int monthIncomeRange;

	/** 车产 0无；1有*/
	private int carStatus;

	/** 房产 0无；1有*/
	private int houseStatus;

	public UserBaseInfo() {
		super();
	}

	public UserBaseInfo(User user) {
		super();
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getEducation() {
		return education;
	}

	public void setEducation(int education) {
		this.education = education;
	}

	public int getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(int maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(int workExperience) {
		this.workExperience = workExperience;
	}

	public int getMonthIncomeRange() {
		return monthIncomeRange;
	}

	public void setMonthIncomeRange(int monthIncomeRange) {
		this.monthIncomeRange = monthIncomeRange;
	}

	public int getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(int carStatus) {
		this.carStatus = carStatus;
	}

	public int getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(int houseStatus) {
		this.houseStatus = houseStatus;
	}
}
