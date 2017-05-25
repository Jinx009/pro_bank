package com.rongdu.p2psys.core.concurrent;

import com.lmax.disruptor.EventFactory;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCorpRegister;
import com.rongdu.p2psys.tpp.ips.model.IpsRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.domain.User;

public final class ValueEvent {
	// 操作
	private String operate;
	private long value;
	private User user;
	private BorrowModel borrowModel;
	private BorrowRepayment borrowRepayment;
	private long id;
	private long userId;
	private int emailStatus;
	private int phoneStatus;
	private int realnameStatus;
	private Borrow borrow;
	private long substationId;
	private CashModel cashModel;
	private RechargeModel rechargeModel;
	private AccountLog accountLog;
	private IpsRegister ipsRegister;
	private IpsRecharge ipsRecharge;
	private String resultFlag;
	private BondModel bondModel;
	private ChinapnrCorpRegister corpRegister;
	private PpfundInModel ppfundInModel;

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BorrowModel getBorrowModel() {
		return borrowModel;
	}

	public void setBorrowModel(BorrowModel borrowModel) {
		this.borrowModel = borrowModel;
	}

	public BorrowRepayment getBorrowRepayment() {
		return borrowRepayment;
	}

	public void setBorrowRepayment(BorrowRepayment borrowRepayment) {
		this.borrowRepayment = borrowRepayment;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}

	public int getPhoneStatus() {
		return phoneStatus;
	}

	public void setPhoneStatus(int phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	public int getRealnameStatus() {
		return realnameStatus;
	}

	public void setRealnameStatus(int realnameStatus) {
		this.realnameStatus = realnameStatus;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public long getSubstationId() {
		return substationId;
	}

	public void setSubstationId(long substationId) {
		this.substationId = substationId;
	}

	public CashModel getCashModel() {
		return cashModel;
	}

	public void setCashModel(CashModel cashModel) {
		this.cashModel = cashModel;
	}

	public RechargeModel getRechargeModel() {
		return rechargeModel;
	}

	public void setRechargeModel(RechargeModel rechargeModel) {
		this.rechargeModel = rechargeModel;
	}

	public AccountLog getAccountLog() {
		return accountLog;
	}

	public void setAccountLog(AccountLog accountLog) {
		this.accountLog = accountLog;
	}

	public IpsRegister getIpsRegister() {
		return ipsRegister;
	}

	public void setIpsRegister(IpsRegister ipsRegister) {
		this.ipsRegister = ipsRegister;
	}

	public IpsRecharge getIpsRecharge() {
		return ipsRecharge;
	}

	public void setIpsRecharge(IpsRecharge ipsRecharge) {
		this.ipsRecharge = ipsRecharge;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public BondModel getBondModel() {
		return bondModel;
	}

	public void setBondModel(BondModel bondModel) {
		this.bondModel = bondModel;
	}

	public ChinapnrCorpRegister getCorpRegister() {
		return corpRegister;
	}

	public void setCorpRegister(ChinapnrCorpRegister corpRegister) {
		this.corpRegister = corpRegister;
	}

	public PpfundInModel getPpfundInModel() {
		return ppfundInModel;
	}

	public void setPpfundInModel(PpfundInModel ppfundInModel) {
		this.ppfundInModel = ppfundInModel;
	}

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
}
