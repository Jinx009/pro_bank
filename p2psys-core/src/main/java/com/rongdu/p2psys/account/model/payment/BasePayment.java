package com.rongdu.p2psys.account.model.payment;

import java.text.DecimalFormat;

import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.Pay;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.util.BeanUtil;

public class BasePayment implements Payment {

	protected Pay pay;
	protected AccountRecharge recharge;
	protected String payname;
	protected String orderPrefix;
	protected String sign;
	protected String encryptSign;
	protected String tranNo;
	protected String localReturnSign;
	protected String remoteReturnSign;

	@Override
	public String payname() {
		return payname;
	}

	@Override
	public String orderPrefix() {
		return null;
	}

	@Override
	public void sign() {

	}

	@Override
	public String encryptSign() {
		return null;
	}

	@Override
	public String localReturnSign() {
		return null;
	}

	@Override
	public String encryptLocalReturnSign() {
		return null;
	}

	@Override
	public String remoteReturnSin() {
		return null;
	}

	@Override
	public Object protype() {
		return null;
	}

	@Override
	public String tranNo() {
		return null;
	}

	@Override
	public String encrypt(String sign) {
		return null;
	}

	@Override
	public boolean reponseSuccess() {
		return false;
	}

	@Override
	public void init(AccountRecharge recharge) {
		this.recharge = recharge;
		PayService payService = (PayService) BeanUtil.getBean("payService");
		this.pay = payService.findByNid(RechargeConstant.GOPAY);
	}

	public String format2Str(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        String ds = df.format(d);
        return ds;
    }

	public Pay getPay() {
		return pay;
	}

	public void setPay(Pay pay) {
		this.pay = pay;
	}

	public AccountRecharge getRecharge() {
		return recharge;
	}

	public void setRecharge(AccountRecharge recharge) {
		this.recharge = recharge;
	}

	public String getPayname() {
		return payname;
	}

	public void setPayname(String payname) {
		this.payname = payname;
	}

	public String getOrderPrefix() {
		return orderPrefix;
	}

	public void setOrderPrefix(String orderPrefix) {
		this.orderPrefix = orderPrefix;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getEncryptSign() {
		return encryptSign;
	}

	public void setEncryptSign(String encryptSign) {
		this.encryptSign = encryptSign;
	}

	public String getTranNo() {
		return tranNo;
	}

	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}

	public String getLocalReturnSign() {
		return localReturnSign;
	}

	public void setLocalReturnSign(String localReturnSign) {
		this.localReturnSign = localReturnSign;
	}

	public String getRemoteReturnSign() {
		return remoteReturnSign;
	}

	public void setRemoteReturnSign(String remoteReturnSign) {
		this.remoteReturnSign = remoteReturnSign;
	}
	
	
}
