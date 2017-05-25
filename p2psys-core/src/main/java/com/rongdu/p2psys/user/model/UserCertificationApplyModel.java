package com.rongdu.p2psys.user.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.user.domain.UserCertificationApply;

/**
 * 上传资料申请model类
 * 
 * @author zf
 * @version 2.0
 * @since 2014年3月5日
 */
public class UserCertificationApplyModel extends UserCertificationApply {
	private static final long serialVersionUID = 7054696862498865303L;
	/** 当前页码 */
	private int page = 1;

	/** 每页数据条数 */
	private int rows = Page.ROWS;
	private String typeName;
	private long typeId;
	private long userId;
	private String userName;
	private String realName;
	private String startTime;
	private String endTime;
	private String searchName;
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

	public static UserCertificationApplyModel instance(UserCertificationApply apply) {
		UserCertificationApplyModel userCertificationApplyModel = new UserCertificationApplyModel();
		BeanUtils.copyProperties(apply, userCertificationApplyModel);
		return userCertificationApplyModel;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	
}
