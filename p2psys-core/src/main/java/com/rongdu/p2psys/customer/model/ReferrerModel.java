package com.rongdu.p2psys.customer.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.exception.PpfundException;

/**
 * PPfund（资金管理产品）model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
public class ReferrerModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	
	/** 每页总数 **/
	private int rows = 12;
	
	private int id;

	private String saleCode;
	
	private String saleName;
	
	private String realName;
	
	private String phone;
	
	private String recommendCode;
	
	private String referrer;
	
	private String referrerPhone;
	
	private String referreredPerson;//被推荐人
	
	private String referreredPhone;//被推荐人手机号
	
	private Integer codeUsedTimes;
	
	private String activityCode;
	
	private String searchName;
	
	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getReferrerPhone() {
		return referrerPhone;
	}

	public void setReferrerPhone(String referrerPhone) {
		this.referrerPhone = referrerPhone;
	}

	public String getReferreredPerson() {
		return referreredPerson;
	}

	public void setReferreredPerson(String referreredPerson) {
		this.referreredPerson = referreredPerson;
	}

	public String getReferreredPhone() {
		return referreredPhone;
	}

	public void setReferreredPhone(String referreredPhone) {
		this.referreredPhone = referreredPhone;
	}

	public Integer getCodeUsedTimes() {
		return codeUsedTimes;
	}

	public void setCodeUsedTimes(Integer codeUsedTimes) {
		this.codeUsedTimes = codeUsedTimes;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}


	
}
