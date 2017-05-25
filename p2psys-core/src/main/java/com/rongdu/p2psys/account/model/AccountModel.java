package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.user.domain.UserIdentify;

public class AccountModel extends Account {

	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows;
	/** 排序 asc/desc **/
	private String order;
	/** 排序字段 **/
	private String sort;
	// 我的账户 待收待还详情
	private double collectionCapital;// 待收总额
	private double collectInterest;// 待收利息
	private double newestCollectMoney;// 最近待收金额
	private double newestCollectDate;// 最近待收时间
	private double borrowTotal;// 借款总额
	private double repayTotal;// 待还本息
	private double repayInterest;// 待还利息
	private double repayTime;// 最近待还时间
	private double repayAccount;// 待还金额

	private String bank;
	private String bankaccount;
	private String bankname;
	private String branch;
	private String provinceName;
	private String cityName;
	private String areaName;
	private int province;
	private int city;
	private int area;
	private String addtime;
	private String addip;
	private String username;
	private String realname;
	private String modifyUsername;
	private long bank_id;
	private SearchParam param;
	private String startTime;
	private String endTime;
	private String accountType;
	
	/** 条件查询 */
	private String searchName;

	public static AccountModel instance(Account account) {
		AccountModel model = new AccountModel();
		BeanUtils.copyProperties(account, model);
		return model;
	}

	/**
	 * 信用额度申请 校验用户认证状态
	 * 
	 * @param attestation
	 */
	public void validAttestionForAmountApply(UserIdentify attestation, int amount) {
		if (attestation.getRealNameStatus() != 1) {
			throw new AccountException("您还没有通过实名认证，请先通过实名认证！", 1);
		}
		if (amount > 3000000 || amount <= 0) {
			throw new AccountException("信用额度每次最多申请300万！", 1);
		}
	}

	public long getBank_id() {
		return bank_id;
	}

	public void setBank_id(long bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAddtime() {
		return addtime;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getModifyUsername() {
		return modifyUsername;
	}

	public void setModifyUsername(String modifyUsername) {
		this.modifyUsername = modifyUsername;
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	// 业务方法
	/**
	 * 冻结资金
	 * 
	 * @param money
	 */
	public synchronized void freeze(double money) {
		this.setNoUseMoney(this.getNoUseMoney() + money);
		this.setUseMoney(this.getUseMoney() - money);
	}

	public double getCollectionCapital() {
		return collectionCapital;
	}

	public void setCollectionCapital(double collectionCapital) {
		this.collectionCapital = collectionCapital;
	}

	public double getCollectInterest() {
		return collectInterest;
	}

	public void setCollectInterest(double collectInterest) {
		this.collectInterest = collectInterest;
	}

	public double getNewestCollectMoney() {
		return newestCollectMoney;
	}

	public void setNewestCollectMoney(double newestCollectMoney) {
		this.newestCollectMoney = newestCollectMoney;
	}

	public double getNewestCollectDate() {
		return newestCollectDate;
	}

	public void setNewestCollectDate(double newestCollectDate) {
		this.newestCollectDate = newestCollectDate;
	}

	public double getBorrowTotal() {
		return borrowTotal;
	}

	public void setBorrowTotal(double borrowTotal) {
		this.borrowTotal = borrowTotal;
	}

	public double getRepayTotal() {
		return repayTotal;
	}

	public void setRepayTotal(double repayTotal) {
		this.repayTotal = repayTotal;
	}

	public double getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(double repayInterest) {
		this.repayInterest = repayInterest;
	}

	public double getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(double repayTime) {
		this.repayTime = repayTime;
	}

	public double getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(double repayAccount) {
		this.repayAccount = repayAccount;
	}

	public SearchParam getParam() {
		return param;
	}

	public void setParam(SearchParam param) {
		this.param = param;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
}
