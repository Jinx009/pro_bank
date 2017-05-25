package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.rongdu.p2psys.user.domain.User;

/**
 * 众筹订单
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_order"))
public class InvestOrder implements Serializable
{
	private static final long serialVersionUID = 442399593546114498L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//购买用户
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	//对应产品
	@OneToOne
	@JoinColumn(name = "project_id")
	private ProjectBaseinfo projectBaseinfo;
	//投资金额
	private Double money;
	//添加时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date addTime;
	//支付时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date payTime;
	//支付状态 0已经违约 1 预约付款 2 全额购买 3 已经取消
	private Integer payStatus;
	//当前支付金额
	private Double payMoney;
	//收益规则
	@OneToOne
	@JoinColumn(name = "profit_rule_id")
	private ProfitRule profitRule;
	//收货人姓名
	private String realName;
	//联系方式
	private String mobilePhone;
	//地址
	private String address;
	//邮编
	private Integer postNum;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ProjectBaseinfo getProjectBaseinfo() {
		return projectBaseinfo;
	}
	public void setProjectBaseinfo(ProjectBaseinfo projectBaseinfo) {
		this.projectBaseinfo = projectBaseinfo;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public ProfitRule getProfitRule() {
		return profitRule;
	}
	public void setProfitRule(ProfitRule profitRule) {
		this.profitRule = profitRule;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPostNum() {
		return postNum;
	}
	public void setPostNum(Integer postNum) {
		this.postNum = postNum;
	}
	
}
