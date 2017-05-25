package com.rongdu.p2psys.borrow.domain;

import java.util.Date;
import java.util.List;

/**
 * 借款抵押物扩展类
 * @author zf
 * @version 2.0
 * @since 2014-08-21
 */
public class BorrowMortgageModel {

	private double totalAssessPrice; //评估总价
	private double totalMortgagePrice; //抵押总价
	private Date updateTime; //更新时间
	private List<BorrowMortgage> inList; //入库集合
	private List<BorrowMortgage> outList; //出库集合
	private double inTotalAssessPrice; //入库总评估价
	private double inTotalMortgagePrice; //入库总抵押价
	private double outTotalAssessPrice; //出库总评估价
	private double outTotalMortgagePrice; //出库总评估价
	
    public double getTotalAssessPrice() {
        return totalAssessPrice;
    }
    public void setTotalAssessPrice(double totalAssessPrice) {
        this.totalAssessPrice = totalAssessPrice;
    }
    public double getTotalMortgagePrice() {
        return totalMortgagePrice;
    }
    public void setTotalMortgagePrice(double totalMortgagePrice) {
        this.totalMortgagePrice = totalMortgagePrice;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public List<BorrowMortgage> getInList() {
        return inList;
    }
    public void setInList(List<BorrowMortgage> inList) {
        this.inList = inList;
    }
    public List<BorrowMortgage> getOutList() {
        return outList;
    }
    public void setOutList(List<BorrowMortgage> outList) {
        this.outList = outList;
    }
    public double getInTotalAssessPrice() {
        return inTotalAssessPrice;
    }
    public void setInTotalAssessPrice(double inTotalAssessPrice) {
        this.inTotalAssessPrice = inTotalAssessPrice;
    }
    public double getInTotalMortgagePrice() {
        return inTotalMortgagePrice;
    }
    public void setInTotalMortgagePrice(double inTotalMortgagePrice) {
        this.inTotalMortgagePrice = inTotalMortgagePrice;
    }
    public double getOutTotalAssessPrice() {
        return outTotalAssessPrice;
    }
    public void setOutTotalAssessPrice(double outTotalAssessPrice) {
        this.outTotalAssessPrice = outTotalAssessPrice;
    }
    public double getOutTotalMortgagePrice() {
        return outTotalMortgagePrice;
    }
    public void setOutTotalMortgagePrice(double outTotalMortgagePrice) {
        this.outTotalMortgagePrice = outTotalMortgagePrice;
    }
	
}
