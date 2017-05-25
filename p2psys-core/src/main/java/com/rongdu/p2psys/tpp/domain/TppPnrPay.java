package com.rongdu.p2psys.tpp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name = "tpp_pnr_pay")
public class TppPnrPay {
	/**
	 * 编号，自动增长
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/**
	 * 汇付接口类型
	 */
	@Column(name = "cmdid")
	private String cmdid;

	/**
	 * 操作金额
	 */
	@Column(name = "ordamt")
	private double ordamt;

	/**
	 * 付款方用户名
	 */
	private String payUserName;

	/**
	 * 付款方商户号
	 */
	private String payUserCustId;

	/**
	 * 收款方用户名
	 */
	private String userName;

	/**
	 * 收款方商户号
	 */
	private String usrCustId;

	/**
	 * 订单状态，1成功，2失败，0未处理
	 */
	@Column(name = "status")
	private String status;

	/**
	 * 操作时间
	 */
	@Column(name = "operate_time")
	private String operateTime;

	/**
	 * 添加时间
	 */
	@Column(name = "addtime")
	private Date addtime;

	/**
	 * 借款标ID
	 */
	@Column(name = "borrow_id")
	private String borrowId;

	/**
	 * 投标ID
	 */
	@Column(name = "tender_id")
	private String tenderId;

	/**
	 * 平台操作类型
	 */
	@Column(name = "operate_type")
	private String operateType;

	/**
	 * 返回消息
	 */
	@Column(name = "error_msg")
	private String errorMsg;

	/**
	 * 订单ID
	 */
	@Column(name = "ord_id")
	private String ordId;

	/**
	 * 订单时间
	 */
	@Column(name = "orddate")
	private String orddate;

	/**
	 * 平台重新触发提交订单号
	 */
	@Column(name = "subord_id")
	private String subordId;

	/**
	 * 重新触发时间
	 */
	@Column(name = "suborddate")
	private String suborddate;

	@Column(name = "late_time")
	private String lateTime;

	/**
	 * 分账字符串
	 */
	@Column(name = "div_details")
	private String divDetails;

	/**
	 * 投标冻结流水号
	 */
	@Column(name = "trx_id")
	private String trxId;

	/**
	 * 放款、还款手续费金额
	 */
	@Column(name = "fee")
	private String fee;

	/**
	 * 
	 */
	@Column(name = "repay_id")
	private int repayId;

	@Column(name = "risk_reserve_fee")
	private String riskReserveFee;// 放款，风险备用金

	public int getRepayId() {
		return repayId;
	}

	public void setRepayId(int repayId) {
		this.repayId = repayId;
	}

	public TppPnrPay() {
		super();
	}

	public TppPnrPay(String cmdid, double ordamt, String userName,
			String payUserName, String status, String operate_time,
			String operate_type) {
		super();
		this.cmdid = cmdid;
		this.ordamt = ordamt;
		this.userName = userName;
		this.payUserName = payUserName;
		this.status = status;
		this.operateTime = operate_time;
		this.addtime = new Date();
		this.operateType = operate_type;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getDivDetails() {
		return divDetails;
	}

	public void setDivDetails(String divDetails) {
		this.divDetails = divDetails;
	}

	public String getPayUserName() {
		return payUserName;
	}

	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}

	public String getPayUserCustId() {
		return payUserCustId;
	}

	public void setPayUserCustId(String payUserCustId) {
		this.payUserCustId = payUserCustId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUsrCustId() {
		return usrCustId;
	}

	public void setUsrCustId(String usrCustId) {
		this.usrCustId = usrCustId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public String getOrddate() {
		return orddate;
	}

	public void setOrddate(String orddate) {
		this.orddate = orddate;
	}

	public String getSubordId() {
		return subordId;
	}

	public void setSubordId(String subordId) {
		this.subordId = subordId;
	}

	public String getSuborddate() {
		return suborddate;
	}

	public void setSuborddate(String suborddate) {
		this.suborddate = suborddate;
	}

	public String getLateTime() {
		return lateTime;
	}

	public void setLateTime(String lateTime) {
		this.lateTime = lateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCmdid() {
		return cmdid;
	}

	public void setCmdid(String cmdid) {
		this.cmdid = cmdid;
	}

	public double getOrdamt() {
		return ordamt;
	}

	public void setOrdamt(double ordamt) {
		this.ordamt = ordamt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getRiskReserveFee() {
		return riskReserveFee;
	}

	public void setRiskReserveFee(String riskReserveFee) {
		this.riskReserveFee = riskReserveFee;
	}

	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getTenderId() {
		return tenderId;
	}

	public void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

}
