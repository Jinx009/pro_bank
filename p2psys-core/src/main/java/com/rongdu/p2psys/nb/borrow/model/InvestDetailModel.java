package com.rongdu.p2psys.nb.borrow.model;


import java.io.Serializable;
import java.util.Date;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.user.domain.User;

public class InvestDetailModel implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;


	private User user;
	
	private long id;
	
	private Date addTime;
	
	private String productName;
	
	private double money;
	
	//1:ppfund 2:非ppfund
	private int type;
	
	private Date reviewTime;
	
	private int timeLimit;
	
	private double apr;
	
	private int borrowTimeType;
	
	private String expirationDate;
	
	private int isOut;
	
	private int isFixedTerm;
	
	//计息金额（应用体验标）其他标值为0
	private double interestAmount;
	

	private String ppfundInType;
	
	//预期收益
	private double expectProfit;

	/** 进度 **/
	private double scales;
	
	/**
	 * 状态 0投标待处理，1成功，2失败
	 */
	private int status;
	
	//标类ID
	private int typeId;
	
	/**
	 * 预期收益下线
	 */
	private double expectedLow;

	/**
	 * 预期收益上线
	 */
	private double expectedUp;
	
	/**
	 * 标签
	 */
	private String flag;
	
	/** 开始日期 **/
	private String startTime;
	/** 结束日期 **/
	private String endTime;
	
	/**
	 * 红包金额
	 */
	private double redPacketMoney;
	
	/**
	 * 红包标示
	 */
	private int nid;
	
	/**
	 * 文字说明
	 */
	private String remark;
	
	/**
	 * 加息
	 */
	private double addRate;
	
	/**
	 * 加息状态
	 */
	private int rateStatus;
	
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getScales() {
		return scales;
	}

	public void setScales(double scales) {
		this.scales = scales;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public int getBorrowTimeType() {
		return borrowTimeType;
	}

	public void setBorrowTimeType(int borrowTimeType) {
		this.borrowTimeType = borrowTimeType;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getIsOut() {
		return isOut;
	}

	public void setIsOut(int isOut) {
		this.isOut = isOut;
	}

	public int getIsFixedTerm() {
		return isFixedTerm;
	}

	public void setIsFixedTerm(int isFixedTerm) {
		this.isFixedTerm = isFixedTerm;
	}

	public double getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(double interestAmount) {
		this.interestAmount = interestAmount;
	}

	public String getPpfundInType() {
		return ppfundInType;
	}

	public void setPpfundInType(String ppfundInType) {
		this.ppfundInType = ppfundInType;
	}


	public double getExpectProfit() {
		return expectProfit;
	}

	public void setExpectProfit(double expectProfit) {
		this.expectProfit = expectProfit;
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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public double getExpectedLow() {
		return expectedLow;
	}

	public void setExpectedLow(double expectedLow) {
		this.expectedLow = expectedLow;
	}

	public double getExpectedUp() {
		return expectedUp;
	}

	public void setExpectedUp(double expectedUp) {
		this.expectedUp = expectedUp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getRedPacketMoney() {
		return redPacketMoney;
	}

	public void setRedPacketMoney(double redPacketMoney) {
		this.redPacketMoney = redPacketMoney;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public double getAddRate() {
		return addRate;
	}

	public void setAddRate(double addRate) {
		this.addRate = addRate;
	}

	public int getRateStatus() {
		return rateStatus;
	}

	public void setRateStatus(int rateStatus) {
		this.rateStatus = rateStatus;
	}
}
