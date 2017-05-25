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
import com.rongdu.p2psys.account.constant.AccountLogTypeName;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * 资金操作日志表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_account_log")
public class AccountLog implements Serializable {

	/** 支付方式-网银直联 */
	@SuppressWarnings("unused")
	private static final String TYPE_ONLINE = "网银直联";
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
	 * 类型 产生这条记录的操作
	 */
	private String type;
	/**
	 * 账户总额
	 */
	private double total;
	/**
	 * 操作金额
	 */
	private double money;
	/**
	 * 可用金额
	 */
	private double useMoney;
	/**
	 * 冻结金额
	 */
	private double noUseMoney;
	/**
	 * 待收金额
	 */
	private double collection;
	/**
	 * 交易对方的user_id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user")
	private User toUser;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	/**
	 * 收支明细 0是不变  1是收入  2是支出
	 */
	private byte paymentsType;
	
	public AccountLog() {
		super();
	}

	public AccountLog(User user, String type, User toUser) {
		super();
		this.user = user;
		this.type = type;
		this.toUser = toUser;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	public AccountLog(User user, String type, double total, double money, double useMoney, double noUseMoney,
			double collection, User toUser, String remark) {
		super();
		this.user = user;
		this.type = type;
		this.total = total;
		this.money = money;
		this.useMoney = useMoney;
		this.noUseMoney = noUseMoney;
		this.collection = collection;
		this.toUser = toUser;
		this.remark = remark;
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
	 * 获取类型 产生这条记录的操作
	 * 
	 * @return 类型 产生这条记录的操作
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型 产生这条记录的操作
	 * 
	 * @param type 要设置的类型 产生这条记录的操作
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取账户总额
	 * 
	 * @return 账户总额
	 */
	public double getTotal() {
		return total;
	}
	
    /**
     * 获取账户总额显示值
     * 
     * @return 账户总额显示值
     */
    public String getTotalDisp() {
        return String.valueOf(total).concat("元");
    }

	/**
	 * 设置账户总额
	 * 
	 * @param total 要设置的账户总额
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * 获取操作金额
	 * 
	 * @return 操作金额
	 */
	public double getMoney() {
		return money;
	}
	
    /**
     * 获取操作金额显示值
     * 
     * @return 操作金额显示值
     */
    public String getMoneyDisp() {
        return String.valueOf(money).concat("元");
    }	

	/**
	 * 设置操作金额
	 * 
	 * @param money 要设置的操作金额
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 获取可用金额
	 * 
	 * @return 可用金额
	 */
	public double getUseMoney() {
		return useMoney;
	}
	
    /**
     * 获取可用金额显示值
     * 
     * @return 可用金额显示值
     */
    public String getUseMoneyDisp() {
        return String.valueOf(useMoney).concat("元");
    }	

	/**
	 * 设置可用金额
	 * 
	 * @param useMoney 要设置的可用金额
	 */
	public void setUseMoney(double useMoney) {
		this.useMoney = useMoney;
	}

	/**
	 * 获取冻结金额
	 * 
	 * @return 冻结金额
	 */
	public double getNoUseMoney() {
		return noUseMoney;
	}
	
    /**
     * 获取冻结总额显示值
     * 
     * @return 冻结总额显示值
     */
    public String getNoUseMoneyDisp() {
        return String.valueOf(noUseMoney).concat("元");
    }	

	/**
	 * 设置冻结金额
	 * 
	 * @param noUseMoney 要设置的冻结金额
	 */
	public void setNoUseMoney(double noUseMoney) {
		this.noUseMoney = noUseMoney;
	}

	/**
	 * 获取待收金额
	 * 
	 * @return 待收金额
	 */
	public double getCollection() {
		return collection;
	}
	
    /**
     * 获取待收总额显示值
     * 
     * @return 待收总额显示值
     */
    public String getCollectionDisp() {
        return String.valueOf(collection).concat("元");
    }	

	/**
	 * 设置待收金额
	 * 
	 * @param collection 要设置的待收金额
	 */
	public void setCollection(double collection) {
		this.collection = collection;
	}

	/**
	 * 获取交易对方的user_id
	 * 
	 * @return 交易对方的user_id
	 */
	public User getToUser() {
		return toUser;
	}

	/**
	 * 设置交易对方的user_id
	 * 
	 * @param toUser 要设置的交易对方的user_id
	 */

	public void setToUser(User toUser) {
		this.toUser = toUser;
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

    public byte getPaymentsType() {
        return paymentsType;
    }

    public void setPaymentsType(byte paymentsType) {
        this.paymentsType = paymentsType;
    }
    
    /**
     * 导出报表去除a标签
     * @return
     */
    public String getDoremark(){
    	if(StringUtil.isBlank(remark)){
    		return remark;
    	}else{
			int firstBeginIndex = remark.indexOf("<");
			if(firstBeginIndex == -1)
				return remark;
			return remark.substring(0,firstBeginIndex)+remark.substring(remark.indexOf(">")+1, remark.lastIndexOf("<"))+remark.substring(remark.lastIndexOf(">")+1);
    	}
    }
    
    /**
     * 获取资金记录类型
     * @return
     */
    public String getTypeName(){
		return  AccountLogTypeName.getInstance().typeNameMap.get(type);
	}
}
