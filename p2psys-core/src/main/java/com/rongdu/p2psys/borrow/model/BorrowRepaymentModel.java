package com.rongdu.p2psys.borrow.model;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.user.domain.User;

/**
 * 待还信息model
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月20日
 */
public class BorrowRepaymentModel extends BorrowRepayment {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;
	/** 关联用户 */
	private User user;
	/** 关联担保公司 */
	private User vouchFirm;
	/** 关联担保公司ID */
	private long vouchFirmId;
	/** 每页数据条数 */
	private int size = Page.ROWS;
	/** 借款人 **/
	private String userName;
	/** 真实姓名 **/
	private String realName;
	/** 电话 **/
	private String mobilePhone;
	/** 借款标名称 **/
	private String borrowName;
	/** 借款标名隐藏后**/
	private String borrowNameHide;
	
	private String searchName;
	
	private String companyName;
	
	/**
	 * 时间区间
	 */
	private String timeVal;
	
	public String getBorrowNameHide() {
		if (StringUtil.isNotBlank(borrowName)&& borrowName.length()>7) {
			borrowNameHide = borrowName.substring(0, 6) + "***";
		}
		return borrowNameHide;
	}
	public void setBorrowNameHide(String borrowNameHide) {
		this.borrowNameHide = borrowNameHide;
	}
	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;
	/**日期范围：0：全部，1：最近七天 2：最近一个月  3：最近两个月，4 最近三个月**/
	private int time;
	private long realPeriod;

	private int borrowStyle;

	/** 借款总额 **/
	private double account;
	/** 实还总金额 **/
	private double accountYes;
	private int borrowStatus;
	/** 标种 **/
	private int borrowType;
	/** 标类 0月标 1天标 **/
	private int borrowTimeType;
	/** 借款期限(月标) **/
	private int timeLimit;
	/** 剩余天数 **/
	private int lastDays;
	/** 年利率 */
	private double apr;
	private long borrowId = 0;
	private boolean isLate = false;

	
	/** 待还的一些统计信息 **/
	private Map<String, Object> repayData; 
	/** 最近一笔待还款 **/
	private Date nextRepayTime;
	/**下一个待收日待还的金额 **/
	private double nextRepayAccount;
	/** 下一个待收日待还的次数 **/
	private int nextRepayCount;
	/**
	 * 状态描述
	 */
	private String statusStr;
	
	/**
	 * 借款标编码
	 */
	private String bidNo;
	
	public double getNextRepayAccount() {
		return nextRepayAccount;
	}

	public void setNextRepayAccount(double nextRepayAccount) {
		this.nextRepayAccount = nextRepayAccount;
	}

	public int getNextRepayCount() {
		return nextRepayCount;
	}

	public void setNextRepayCount(int nextRepayCount) {
		this.nextRepayCount = nextRepayCount;
	}

	public Map<String, Object> getRepayData() {
		return repayData;
	}

	public void setRepayData(Map<String, Object> repayData) {
		this.repayData = repayData;
	}

	public Date getNextRepayTime() {
		return nextRepayTime;
	}

	public void setNextRepayTime(Date nextRepayTime) {
		this.nextRepayTime = nextRepayTime;
	}

	public static BorrowRepaymentModel instance(BorrowRepayment borrowRepayment) {
		BorrowRepaymentModel borrowRepaymentModel = new BorrowRepaymentModel();
		BeanUtils.copyProperties(borrowRepayment, borrowRepaymentModel);
		return borrowRepaymentModel;
	}

	public BorrowRepayment prototype() {
		BorrowRepayment borrowRepayment = new BorrowRepayment();
		BeanUtils.copyProperties(this, borrowRepayment);
		return borrowRepayment;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public int getSize() {
		return size > 0 ? size : Page.ROWS;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getRealPeriod() {
		return realPeriod;
	}

	public void setRealPeriod(long realPeriod) {
		this.realPeriod = realPeriod;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
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
	public int getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(int borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getAccountYes() {
		return accountYes;
	}

	public void setAccountYes(double accountYes) {
		this.accountYes = accountYes;
	}

	public int getBorrowStatus() {
		return borrowStatus;
	}

	public void setBorrowStatus(int borrowStatus) {
		this.borrowStatus = borrowStatus;
	}

	public int getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(int borrowType) {
		this.borrowType = borrowType;
	}

	public int getBorrowTimeType() {
		return borrowTimeType;
	}

	public void setBorrowTimeType(int borrowTimeType) {
		this.borrowTimeType = borrowTimeType;
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

	public long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean isLate) {
		this.isLate = isLate;
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
    public User getVouchFirm() {
        return vouchFirm;
    }
    public void setVouchFirm(User vouchFirm) {
        this.vouchFirm = vouchFirm;
    }
    public long getVouchFirmId() {
        return vouchFirmId;
    }
    public void setVouchFirmId(long vouchFirmId) {
        this.vouchFirmId = vouchFirmId;
    }
    public int getLastDays() {
        return lastDays;
    }
    public void setLastDays(int lastDays) {
        this.lastDays = lastDays;
    }
	public String getStatusStr() {
		switch (getStatus()) {
		case 0:
			statusStr = "未还款";
			break;
		case 1:
			statusStr = "已还款";
			break;
		case 2:
			statusStr = "网站垫付";
			break;
		case 3:
			statusStr = "还款处理中";
			break;
		default:
			break;
		}
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getBidNo() {
		return bidNo;
	}
	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}
	public String getTimeVal() {
		return timeVal;
	}
	public void setTimeVal(String timeVal) {
		this.timeVal = timeVal;
	}

    
}
