package com.rongdu.p2psys.core.rule;


public class ExtractCashChargeRuleCheck extends RuleCheck {

	/*
	 * 提现规则开启状态
	 */
	private int cash_status;
	/**
	 * 最大提现金额
	 */
	private double cash_fee_max;
	/**
	 * 投资人免费提现次数
	 */
	private int tender_cash_free_times;
	/**
	 * 投资人提现收取费率方式  0：按照提现金额比例收取 1:固定金额收取  
	 */
	private int tender_cash_type;
	/**
	 * 投资人提现费率
	 */
	private double tender_cash_rate;
	/**
	 * 投资人提现费率
	 */
	private double  tender_cash_fee;
	/**
	 * 是否VIP独享， 1 是   0否 
	 */
	private int vip_alone;
	
	
	/**
     * 借款人免费提现次数
     */
    private int borrower_cash_free_times;
    /**
     * 借款人提现收取费率方式  0：按照提现金额比例收取 1:固定金额收取  
     */
    private int borrower_cash_type;
    /**
     * 借款人提现费率
     */
    private double borrower_cash_rate;
    /**
     * 借款人提现费率
     */
    private double  borrower_cash_fee;
	
	
	@Override
	public boolean checkRule() {

		return false;
	}


    public int getCash_status() {
        return cash_status;
    }


    public void setCash_status(int cash_status) {
        this.cash_status = cash_status;
    }


    public double getCash_fee_max() {
        return cash_fee_max;
    }


    public void setCash_fee_max(double cash_fee_max) {
        this.cash_fee_max = cash_fee_max;
    }


    public int getTender_cash_free_times() {
        return tender_cash_free_times;
    }


    public void setTender_cash_free_times(int tender_cash_free_times) {
        this.tender_cash_free_times = tender_cash_free_times;
    }


    public int getTender_cash_type() {
        return tender_cash_type;
    }


    public void setTender_cash_type(int tender_cash_type) {
        this.tender_cash_type = tender_cash_type;
    }


    public double getTender_cash_rate() {
        return tender_cash_rate;
    }


    public void setTender_cash_rate(double tender_cash_rate) {
        this.tender_cash_rate = tender_cash_rate;
    }


    public double getTender_cash_fee() {
        return tender_cash_fee;
    }


    public void setTender_cash_fee(double tender_cash_fee) {
        this.tender_cash_fee = tender_cash_fee;
    }


    public int getVip_alone() {
        return vip_alone;
    }


    public void setVip_alone(int vip_alone) {
        this.vip_alone = vip_alone;
    }


    public int getBorrower_cash_free_times() {
        return borrower_cash_free_times;
    }


    public void setBorrower_cash_free_times(int borrower_cash_free_times) {
        this.borrower_cash_free_times = borrower_cash_free_times;
    }


    public int getBorrower_cash_type() {
        return borrower_cash_type;
    }


    public void setBorrower_cash_type(int borrower_cash_type) {
        this.borrower_cash_type = borrower_cash_type;
    }


    public double getBorrower_cash_rate() {
        return borrower_cash_rate;
    }


    public void setBorrower_cash_rate(double borrower_cash_rate) {
        this.borrower_cash_rate = borrower_cash_rate;
    }


    public double getBorrower_cash_fee() {
        return borrower_cash_fee;
    }


    public void setBorrower_cash_fee(double borrower_cash_fee) {
        this.borrower_cash_fee = borrower_cash_fee;
    }

	
   

}
