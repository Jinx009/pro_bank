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

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.user.domain.User;

/**
 * 资金提现记录表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_account_cash")
public class AccountCash implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 状态 0：申请提现  1：提现成功，2：提现失败 4：用户取消提现 ，5:提现处理中 6:初审通过 7：初审不通过 9 复审不通过
	 */
	private int status;
	/**
	 * 真实姓名 
	 */
	private String realName;
	/**
	 * 提现方式 0网上提现 1 线下提现
	 */
	private int type;
	/**
	 * 银行账号
	 */
	private String bankNo;
	/**
	 * 所属银行
	 */
	private String bank;
	/**
	 * 支行
	 */
	private String branch;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 提现总额
	 */
	private double money;
	/**
	 * 到账金额
	 */
	private double credited;
	/**
	 * 手续费
	 */
	private double fee;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	/**
	 * 需收费提现金额
	 */
	private double freeCash;
	/**
	 * 提现扣除的利息金额
	 */
	private double interestCash;
	/**
	 * 提现扣除的奖励金额
	 */
	private double awardCash;
	/**
	 * 提现扣除的充值金额
	 */
	private double rechargeCash;
	/**
	 * 提现扣除的借款金额
	 */
	private double borrowCash;
	/**
	 * 提现扣除的可用回款
	 */
	private double huikuanCash;

	/**
	 * 提现扣除的可用回款利息
	 */
	private double huikuanInterestCash;
	/**
	 *  提现方式  0：T+0  1：T+1
	 */
	private int drawType;
	/**
	 * 取现订单号
	 */
	private String orderNo;
	/**
     * 提现手续费承担方:1 平台  2个人
     */
    private byte cashFeeBear;
    
    /** 备注 **/
    private String remark;
    
	private Integer transferType;
	
	/**
	 * 支付通道key
	 */
	private String channelKey;
	
	/**
	 * 证件号码
	 */
	private String cardId;
    
	public AccountCash() {
		super();
	}

	public AccountCash(User user, double money) {
		super();
		this.orderNo = OrderNoUtils.getSerialNumber();
		this.user = user;
		this.money = money;
	}

	/**
	 * 获取(隐藏一定位数的)银行账号
	 * 
	 * @return 银行账号
	 */
	public String getHideAccount() {
		if (StringUtil.isNotBlank(bankNo)) {
			return "**************" + bankNo.substring(bankNo.length() - 4, bankNo.length());
		}
		return "";
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}
	
	public Integer getTransferType() {
		return transferType;
	}

	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
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

	/**
	 * 获取状态 状态 0：申请提现  1：提现成功，2：提现失败 4：用户取消提现 ，
	 * 
	 * @return 
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态 状态 0：申请提现  1：提现成功，2：提现失败 4：用户取消提现 ，
	 * 
	 * @param status 要设置的状态 
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	/**
	 * 获取所属银行
	 * 
	 * @return 所属银行
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置所属银行
	 * 
	 * @param bank 要设置的所属银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取支行
	 * 
	 * @return 支行
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * 设置支行
	 * 
	 * @param branch 要设置的支行
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取账户总额
	 * 
	 * @return 账户总额
	 */
	public double getMoney() {
		return money;
	}
	
    /**
     * 获取账户总额显示值
     * 
     * @return 账户总额显示值
     */
    public String getMoneyDisp() {
        return String.valueOf(money).concat("元");
    }

	/**
	 * 设置账户总额
	 * 
	 * @param money 要设置的账户总额
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 获取到账金额
	 * 
	 * @return 到账金额
	 */
	public double getCredited() {
		return credited;
	}
	
    /**
     * 获取到账金额显示值
     * 
     * @return 获取到账金额显示值
     */
    public String getCreditedDisp() {
        return String.valueOf(credited).concat("元");
    }	

	/**
	 * 设置到账金额
	 * 
	 * @param credited 要设置的到账金额
	 */
	public void setCredited(double credited) {
		this.credited = credited;
	}

	/**
	 * 获取手续费
	 * 
	 * @return 手续费
	 */
	public double getFee() {
		return fee;
	}
	
    /**
     * 获取手续费显示值
     * 
     * @return 获取手续费显示值
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
	 * 获取需收费提现金额
	 * 
	 * @return 需收费提现金额
	 */
	public double getFreeCash() {
		return freeCash;
	}

	/**
	 * 设置需收费提现金额
	 * 
	 * @param freeCash 要设置的需收费提现金额
	 */
	public void setFreeCash(double freeCash) {
		this.freeCash = freeCash;
	}

	/**
	 * 获取提现扣除的利息金额
	 * 
	 * @return 提现扣除的利息金额
	 */
	public double getInterestCash() {
		return interestCash;
	}

	/**
	 * 设置提现扣除的利息金额
	 * 
	 * @param interestCash 要设置的提现扣除的利息金额
	 */
	public void setInterestCash(double interestCash) {
		this.interestCash = interestCash;
	}

	/**
	 * 获取提现扣除的奖励金额
	 * 
	 * @return 提现扣除的奖励金额
	 */
	public double getAwardCash() {
		return awardCash;
	}

	/**
	 * 设置提现扣除的奖励金额
	 * 
	 * @param awardCash 要设置的提现扣除的奖励金额
	 */
	public void setAwardCash(double awardCash) {
		this.awardCash = awardCash;
	}

	/**
	 * 获取提现扣除的充值金额
	 * 
	 * @return 提现扣除的充值金额
	 */
	public double getRechargeCash() {
		return rechargeCash;
	}

	/**
	 * 设置提现扣除的充值金额
	 * 
	 * @param rechargeCash 要设置的提现扣除的充值金额
	 */
	public void setRechargeCash(double rechargeCash) {
		this.rechargeCash = rechargeCash;
	}

	/**
	 * 获取提现扣除的借款金额
	 * 
	 * @return 提现扣除的借款金额
	 */
	public double getBorrowCash() {
		return borrowCash;
	}

	/**
	 * 设置提现扣除的借款金额
	 * 
	 * @param borrowCash 要设置的提现扣除的借款金额
	 */
	public void setBorrowCash(double borrowCash) {
		this.borrowCash = borrowCash;
	}

	/**
	 * 获取提现扣除的可用回款
	 * 
	 * @return 提现扣除的可用回款
	 */
	public double getHuikuanCash() {
		return huikuanCash;
	}

	/**
	 * 设置提现扣除的可用回款
	 * 
	 * @param huikuanCash 要设置的提现扣除的可用回款
	 */
	public void setHuikuanCash(double huikuanCash) {
		this.huikuanCash = huikuanCash;
	}

	public double getHuikuanInterestCash() {
		return huikuanInterestCash;
	}

	public void setHuikuanInterestCash(double huikuanInterestCash) {
		this.huikuanInterestCash = huikuanInterestCash;
	}
	/**
	 * 获取提现方式
	 * @return  提现方式对应数值  0：T+0  1：T+1
	 */
	public int getDrawType() {
		return drawType;
	}
	/**
	 * 设置提现的方式
	 * @param drawType 提现方式
	 */
	public void setDrawType(int drawType) {
		this.drawType = drawType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

    public byte getCashFeeBear() {
        return cashFeeBear;
    }

    public void setCashFeeBear(byte cashFeeBear) {
        this.cashFeeBear = cashFeeBear;
    }
    
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getChannelKey() {
		if (StringUtil.isNotBlank(channelKey)) {
			return channelKey;
		}
		return "";
	}

	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
     * 手续费承担方名称
     * 
     * @return 手续费承担方名称
     */
    public String getCashFeeBearName() {
        if (cashFeeBear == 1) {
            return "平台垫付";
        }else if(cashFeeBear == 2){
            return "个人承担";
        }
        return "";
    }    
}
