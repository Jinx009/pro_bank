package com.rongdu.p2psys.tpp.chinapnr.model;

import com.rongdu.p2psys.user.domain.User;

public class ChinapnrWebPayModel {

	private double money;  //支付金额
	 
	private User payUser;   //接收人
	
	public ChinapnrWebPayModel(){
		
	}
	public ChinapnrWebPayModel(User user ,double money){
		this.money = money;
		this.payUser = user;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public User getPayUser() {
		return payUser;
	}

	public void setPayUser(User payUser) {
		this.payUser = payUser;
	}
	
}
