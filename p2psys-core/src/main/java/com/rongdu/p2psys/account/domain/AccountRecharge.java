package com.rongdu.p2psys.account.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.user.domain.User;

/**
 * 充值记录表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_account_recharge")
public class AccountRecharge implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/** 充值类型：自动代扣充值 */
	public static final byte TYPE_AUTO_RECHARGE = 27;
	
	/** 充值手续费承担方:2个人承担 */
    public static final byte FEE_BEAR_ALONE = 2;
    
    /** 充值手续费承担方:1平台垫付手续费 */
    public static final byte FEE_BEAR_COMPANY = 1;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 订单号
	 */
	private String tradeNo;
	/**
	 * 用户ID
	 */
	@JSONField
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offlinebank_id")
	private PayOfflinebank payOfflinebank;
	/**
	 * 状态 0等待审核 1充值成功 2充值失败 5处理中 6初审通过 7初审不通过
	 */

	private int status;
	/**
	 * 真实姓名 
	 */
	private String realName;
	/** 
	 * 银行卡号 
	 */
	private String bankNo;
	/**
	 * 金额
	 */
	private double money;
	/**
	 * 充值入口（微信充值，pc充值..）
	 */
	private String payment;
	/**
	 * 
	 */
	private String returnMsg;
	/**
	 * 充值方式 1网上充值 2 网上支付 3线下充值 4后台线下充值 21银行充值 22线下充值奖励 23回款续投奖励 24调单充值 25活动奖励 26其他充值27自动代扣充值
	 */
	private int type;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 手续费
	 */
	private double fee;
	/**
	 * 实际到账金额
	 */
	private double amountIn;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	/**
	 * 充值手续费承担方:1平台垫付手续费，2个人承担
	 */
	private byte rechargeFeeBear;
	
	/**
	 * 充值通道key
	 */
	private String channelKey;
	
	/**
	 * 连连钱包支付单号
	 */
	private String oid_paybill;
	/**
	 * 证件号码
	 */
	private String cardId;
	
	public AccountRecharge() {
		super();
	}

	public AccountRecharge(User user, double fee) {
		super();
		this.user = user;
		this.fee = fee;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	public AccountRecharge(User user, double money, String payment, int type, String remark) {
		super();
		this.user = user;
		this.fee = 0;
//		this.tradeNo = RechargeUtil.generateTradeNO(user.getUserId(), "E"); 换成下面这种写法生成易极付要的16位字符串
		this.tradeNo = OrderNoUtils.getSerialNumber();
		this.money = money;
		this.type = type;
		this.remark = remark;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}
	
	public AccountRecharge(String tradeNo, User user, int status, double money, int type) {
		super();
		this.tradeNo = tradeNo;
		this.user = user;
		this.status = status;
		this.money = money;
		this.type = type;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取订单号
	 * 
	 * @return 订单号
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置订单号
	 * 
	 * @param tradeNo 要设置的订单号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param userId 要设置的用户ID
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	/**
	 * 获取状态 1充值成功 2充值失败
	 * 
	 * @return 状态 1充值成功 2充值失败
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态 1充值成功 2充值失败
	 * 
	 * @param status 要设置的状态 1充值成功 2充值失败
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取金额
	 * 
	 * @return 金额
	 */
	public double getMoney() {
		return money;
	}

	 /**
     * 获取金额显示值
     * 
     * @return 金额显示值
     */
    public String getMoneyDisp() {
        return String.valueOf(amountIn).concat("元");
    }

	/**
	 * 设置金额
	 * 
	 * @param money 要设置的金额
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getReturnMsg() {
		return returnMsg;
	}

	/**
	 * 设置
	 * 
	 * @param returnMsg 要设置的
	 */
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	/**
	 * 获取充值方式 1网上充值 2 网上支付 3线下充值 4后台线下充值 21银行充值 22线下充值奖励 23回款续投奖励 24调单充值 25活动奖励 26其他充值27自动代扣充值
	 * 
	 * @return 
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置充值方式 1网上充值 2 网上支付 3线下充值 4后台线下充值 21银行充值 22线下充值奖励 23回款续投奖励 24调单充值 25活动奖励 26其他充值27自动代扣充值
	 * 
	 * @param type 
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 要设置的备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 手续费
	 * 
	 * @return
	 */
	public double getFee() {
		return fee;
	}
	
    /**
    * 手续费显示值
    * 
    * @return 手续费显示值
    */
   public String getFeeDisp() {
       return String.valueOf(fee).concat("元");
   }

	/**
	 * 设置手续费
	 * 
	 * @param fee 要设置的手续费
	 */
	public void setFee(double fee) {
		this.fee = fee;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加IP
	 * 
	 * @return 添加IP
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * 
	 * @param addIp 要设置的添加IP
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取实际到账金额
	 * @return double
	 */
	public double getAmountIn() {
		return amountIn;
	}
	/**
	 * 设置实际到账金额
	 * @param amountIn 实际到账金额
	 */
	public void setAmountIn(double amountIn) {
		this.amountIn = amountIn;
	}

    public byte getRechargeFeeBear() {
        return rechargeFeeBear;
    }

    public void setRechargeFeeBear(byte rechargeFeeBear) {
        this.rechargeFeeBear = rechargeFeeBear;
    }
    
    public PayOfflinebank getPayOfflinebank() {
		return payOfflinebank;
	}

	public void setPayOfflinebank(PayOfflinebank payOfflinebank) {
		this.payOfflinebank = payOfflinebank;
	}

	
	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getRechargeFeeBearName() {
        
        if(rechargeFeeBear == FEE_BEAR_COMPANY){
            return "平台";
        }else if(rechargeFeeBear == FEE_BEAR_ALONE){
            return "个人";
        }
        return "无";
    }    
}
