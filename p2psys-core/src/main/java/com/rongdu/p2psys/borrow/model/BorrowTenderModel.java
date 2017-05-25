package com.rongdu.p2psys.borrow.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowTender;

public class BorrowTenderModel extends BorrowTender {

	private static final long serialVersionUID = -5725618997271926667L;

	/**
	 * 当前页面
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	/**
	 * 借款标ID
	 **/
	private long borrowId;

	/**
	 * 借款标名
	 **/
	private String borrowName;

	/**
	 * 借款标名隐藏后
	 **/
	private String borrowNameHide;

	/**
	 * 实还总金额
	 **/
	private double accountYes;

	/**
	 * 借款总额
	 **/
	private double account;

	/**
	 * 投资人ID
	 **/
	private long userId;

	/**
	 * 投资人用户名
	 **/
	private String userName;

	/**
	 * 借款人用户名
	 **/
	private String borrowUserName;

	/**
	 * 还款方式
	 **/
	private int borrowStyle;

	/**
	 * 借款期限
	 **/
	private int timeLimit;

	/**
	 * 利率
	 **/
	private double apr;

	/**
	 * 总投资
	 **/
	private double totalTender;

	/**
	 * 已回款
	 **/
	private double yesRepay;

	/**
	 * 待收款
	 **/
	private double waitCollection;

	/**
	 * 预计还款时间
	 **/
	private int repayTime;

	/**
	 * 已经还款时间
	 **/
	private int repayYesTime;

	/**
	 * 预还金额
	 **/
	private int repayAccount;

	/**
	 * 应收开始日期
	 **/
	private String startTime;

	/**
	 * 应收结束开始日期
	 **/
	private String endTime;

	/**
	 * 日期范围
	 * 
	 * <p>
	 * 0:全部
	 * </p>
	 * <p>
	 * 1:最近七天
	 * </p>
	 * <p>
	 * 2:最近一个月
	 * </p>
	 * <p>
	 * 3:最近两个月
	 * </p>
	 * <p>
	 * 4:最近三个月
	 * </p>
	 **/
	private int time;

	/**
	 * 按投标金额比例(0.1-0.6)
	 **/
	private double partAccount;

	/**
	 * 还款结束后奖励
	 **/
	private double lateAward;

	/**
	 * 进度
	 **/
	private double scales;

	/**
	 * 计息起始日
	 **/
	private Date startDate;

	/**
	 * 结束日
	 **/
	private Date endDate;

	/**
	 * 待还期数
	 **/
	private int period;

	private String realName;

	private String statusStr;

	/**
	 * 产品编码
	 **/
	private String bidNo;

	/**
	 * 借款到期日
	 **/
	private String expirationDate;

	/**
	 * 预期收益下线
	 */
	private double expectedLow;

	/**
	 * 预期收益上线
	 */
	private double expectedUp;

	/**
	 * 产品类型
	 */
	private int borrowType;

	/**
	 * 产品标签
	 */
	private Long flagId;

	
	//预期收益
	private double expectProfit;
	
	//是否可以展示(0:否 1:是)
	public int flag;
	
	/**
	 * 0:投标待处理
	 */
	public static final int STATUS_WAITING_FOR_PROCESS = 0;

	/**
	 * 1:成功
	 */
	public static final int STATUS_SUCCESS = 1;

	/**
	 * 2:失败
	 */
	public static final int STATUS_FAILURE = 2;

	public String getBorrowNameHide() {
		if (StringUtil.isNotBlank(borrowName) && borrowName.length() > 7) {
			borrowNameHide = borrowName.substring(0, 6) + "***";
		} else {
			borrowNameHide = borrowName;
		}
		return borrowNameHide;
	}

	public void setBorrowNameHide(String borrowNameHide) {
		this.borrowNameHide = borrowNameHide;
	}

	public static BorrowTenderModel instance(BorrowTender borrowTender) {
		BorrowTenderModel model = new BorrowTenderModel();
		BeanUtils.copyProperties(borrowTender, model);
		return model;
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

	public double getAccountYes() {
		return accountYes;
	}

	public void setAccountYes(double accountYes) {
		this.accountYes = accountYes;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public int getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(int repayTime) {
		this.repayTime = repayTime;
	}

	public int getRepayYesTime() {
		return repayYesTime;
	}

	public void setRepayYesTime(int repayYesTime) {
		this.repayYesTime = repayYesTime;
	}

	public int getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(int repayAccount) {
		this.repayAccount = repayAccount;
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

	public double getLateAward() {
		return lateAward;
	}

	public void setLateAward(double lateAward) {
		this.lateAward = lateAward;
	}

	public double getScales() {
		return scales;
	}

	public void setScales(double scales) {
		this.scales = scales;
	}

	public double getPartAccount() {
		return partAccount;
	}

	public void setPartAccount(double partAccount) {
		this.partAccount = partAccount;
	}

	public String getBorrowUserName() {
		return borrowUserName;
	}

	public void setBorrowUserName(String borrowUserName) {
		this.borrowUserName = borrowUserName;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public double getTotalTender() {
		return totalTender;
	}

	public void setTotalTender(double totalTender) {
		this.totalTender = totalTender;
	}

	public double getYesRepay() {
		return yesRepay;
	}

	public void setYesRepay(double yesRepay) {
		this.yesRepay = yesRepay;
	}

	public double getWaitCollection() {
		return waitCollection;
	}

	public void setWaitCollection(double waitCollection) {
		this.waitCollection = waitCollection;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getStatusStr() {
		switch (getStatus()) {
		case 1:
			statusStr = "投标成功";
			break;
		case 2:
			statusStr = "投标失败";
			break;
		default:
			statusStr = "投标状态异常";
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
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

	public int getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(int borrowType) {
		this.borrowType = borrowType;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public double getExpectProfit() {
		return expectProfit;
	}

	public void setExpectProfit(double expectProfit) {
		this.expectProfit = expectProfit;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
