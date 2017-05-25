package com.rongdu.p2psys.borrow.model;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;

/**
 * @author sj
 * @version 2.0
 * @since 2014年2月25日18:32:09
 */
public class BorrowCollectionModel extends BorrowCollection {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;

	/** 每页数据条数 */
	private int size = Page.ROWS;

	/** 用户名 **/
	private String userName;
	/** 投资者用户名 **/
	private String investUserName;

	/** 投资人姓名 */
	private String investRealName;
	/** 名称 **/
	private String name;
	/** 借款标ID **/
	private long borrowId;
	/** 借款标名 **/
	private String borrowName;
	/** 借款标名隐藏后 **/
	private String borrowNameHide;
	/** 还款方式 **/
	private int borrowStyle;
	/** 借款期限 **/
	private int timeLimit;
	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;
	/** 日期范围：0：全部，1：最近七天 2：最近一个月 3：最近两个月，4 最近三个月 **/
	private int time;
	/** 实际利息 **/
	private double actualInterest;
	/** 利率 **/
	private double apr;
	/** 待还的一些统计信息 **/
	private Map<String, Object> collectData;

	/** 下一次待收的时间 **/
	private Date nextCollectTime;
	/** 下一个待收日待收的金额 **/
	private double nextCollextAccount;
	/** 下一个待收日待收的次数 **/
	private int nextCollextCount;

	/** 条件查询 */
	private String searchName;
	/** 计息起始日 */
	private Date startDate;
	/** 投资金额 **/
	private double totalCapital;

	/** 产品编码 **/
	private String bidNo;

	/**
	 * 产品标签
	 */
	private Long flagId;

	public double getNextCollextAccount() {
		return nextCollextAccount;
	}

	public void setNextCollextAccount(double nextCollextAccount) {
		this.nextCollextAccount = nextCollextAccount;
	}

	public int getNextCollextCount() {
		return nextCollextCount;
	}

	public void setNextCollextCount(int nextCollextCount) {
		this.nextCollextCount = nextCollextCount;
	}

	public static BorrowCollectionModel instance(
			BorrowCollection borrowCollection) {
		BorrowCollectionModel borrowCollectionModel = new BorrowCollectionModel();
		BeanUtils.copyProperties(borrowCollection, borrowCollectionModel);
		return borrowCollectionModel;
	}

	public BorrowCollection prototype() {
		BorrowCollection borrowCollection = new BorrowCollection();
		BeanUtils.copyProperties(this, borrowCollection);
		return borrowCollection;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size > 0 ? size : Page.ROWS;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public int getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(int borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
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

	public double getActualInterest() {
		return actualInterest;
	}

	public void setActualInterest(double actualInterest) {
		this.actualInterest = actualInterest;
	}

	public Map<String, Object> getCollectData() {
		return collectData;
	}

	public void setCollectData(Map<String, Object> collectData) {
		this.collectData = collectData;
	}

	public Date getNextCollectTime() {
		return nextCollectTime;
	}

	public void setNextCollectTime(Date nextCollectTime) {
		this.nextCollectTime = nextCollectTime;
	}

	public String getBorrowNameHide() {
		if (StringUtil.isNotBlank(borrowName) && borrowName.length() > 7) {
			borrowNameHide = borrowName.substring(0, 6) + "***";
		}
		return borrowNameHide;
	}

	public void setBorrowNameHide(String borrowNameHide) {
		this.borrowNameHide = borrowNameHide;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getInvestUserName() {
		return investUserName;
	}

	public void setInvestUserName(String investUserName) {
		this.investUserName = investUserName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getInvestRealName() {
		return investRealName;
	}

	public void setInvestRealName(String investRealName) {
		this.investRealName = investRealName;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public double getTotalCapital() {
		return totalCapital;
	}

	public void setTotalCapital(double totalCapital) {
		this.totalCapital = totalCapital;
	}

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}
}
