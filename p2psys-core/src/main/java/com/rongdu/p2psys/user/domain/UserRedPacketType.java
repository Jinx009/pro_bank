package com.rongdu.p2psys.user.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户红包类型
 * @author zf
 * @version 2.0
 * @since 2014-10-20
 */
@Entity
@Table(name = "rd_user_red_packet_type")
public class UserRedPacketType {
	/**
	 * 注册
	 */
	public static final String REGISTER = "register"; 
	/**
	 * 实名认证
	 */
	public static final String REALNAME = "realname"; 
	/**
	 * 首次充值
	 */
	public static final String FIRST_RECHARGE = "firstRecharge"; 
	/**
	 * 首次投资
	 */
	public static final String FIRST_TENDER = "firstTender"; 
	/**
	 * 邀请好友
	 */
	public static final String INVENT_FRIEND = "inventFriend";
	
	/**
	 * 邀请好友成功投资至少1000元
	 */
	public static final String INVENT_FRIEND_INVEST = "inventFriendInvest";
	
	/**
	 * 投资成功赠送红包
	 */
	public static final String TENDER = "tender";
	
	/**
	 * 充值赠送红包
	 */
	public static final String RECHARGE = "recharge";
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 是否开启
	 */
	private boolean isOpen;
	/**
	 * 类型名称
	 */
	private String name;
	/**
	 * 类型代码
	 */
	private String nid;
	/**
	 * 赠送金额
	 */
	private double amount;
	/**
	 * 有效时间(天数)
	 */
	private int validTime;
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getValidTime() {
		return validTime;
	}
	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
