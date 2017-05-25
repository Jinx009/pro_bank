package com.rongdu.p2psys.nb.ppfund.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.ppfund.domain.ExperienceGold;

/**
 * 体验金信息model
 */
public class ExperienceGoldModel extends ExperienceGold {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows = Page.ROWS;

	/**
	 * 现金标id
	 */
	private long ppfund_id;
	
	/**
	 * 现金标名称
	 */
	private String name;
	
	/**
	 * 用户真实姓名
	 */
	private String realName;
	
	/**
	 * 0：正常，1：失效，2：投资中
	 */
	private int status;
	
	/**
	 * 有效天数
	 */
	private int days;
	
	/**
	 * 体验金额
	 */
	private double money;
	
	/**
	 * 发放时间
	 */
	private Date addTime;
	
	/**
	 * 投标时间
	 */
	private Date investTime;

//	public ExperienceGoldModel() {
//		super();
//	}

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
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getPpfund_id() {
		return ppfund_id;
	}

	public void setPpfund_id(long ppfund_id) {
		this.ppfund_id = ppfund_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getInvestTime() {
		return investTime;
	}

	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}

	public static ExperienceGoldModel instance(ExperienceGold eg) {
		ExperienceGoldModel egModel = new ExperienceGoldModel();
		BeanUtils.copyProperties(eg, egModel);
		return egModel;
	}
	
	public static ExperienceGold instance(ExperienceGoldModel egModel) {
		ExperienceGold eg = new ExperienceGold();
		BeanUtils.copyProperties(egModel, eg);
		return eg;
	}
}
