package com.rongdu.p2psys.account.model;


public class AccountMoneyModel {

	private static final long serialVersionUID = 1L;
	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows;
	/** 排序 asc/desc **/
	private String order;
	/** 排序字段 **/
	private String sort;
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


	private String name;
	private double investMoney;
	private double collectionCapital;// 待收总额
	private double collectInterest;// 待收利息
	private double collectInterestRate;//待收加息券值
	private double newestCollectMoney;// 最近待收金额
	private double newestCollectDate;// 最近待收时间

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	public double getInvestMoney() {
		return investMoney;
	}


	public void setInvestMoney(double investMoney) {
		this.investMoney = investMoney;
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


	public double getCollectInterestRate() {
		return collectInterestRate;
	}


	public void setCollectInterestRate(double collectInterestRate) {
		this.collectInterestRate = collectInterestRate;
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

	
	
}
