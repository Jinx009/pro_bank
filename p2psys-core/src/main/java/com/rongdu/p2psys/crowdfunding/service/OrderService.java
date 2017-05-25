package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.user.domain.User;

public interface OrderService {

	public void saveOrder(InvestOrder order);
	
	public List<OrderModel> getList(Long projectId);
	
	public void updateOrder(InvestOrder order);
	
	public PageDataList<OrderModel> getOrderList(OrderModel model, int pageNumber, int pageSize);

	public InvestOrder save(ProjectBaseinfo projectBaseinfo, Double money, User user,Integer type,Integer profitRule,double virtual);

	public List<InvestOrder> getAlready(User user, ProjectBaseinfo projectBaseinfo);

	public List<OrderModel> getBuyList(User user);

	public String payAll(Integer id);

	public Double getInvestMoney(Long userId);

	public String caclePay(Integer id);

	public void changeType();

	public InvestOrder find(Integer id);

	public List<OrderModel> getLeaderList(User user);
	
	/** ******   wehcat ***** **/
	public List<OrderModel> getBuyListForWechat(User user,int type);
	
	public List<InvestOrder> getInvestOrderById(long userId,long projectId);

	public InvestOrder saveWechatPay(ProjectBaseinfo projectBaseinfo,
			Double money, User user, int type, Integer profitRule, double virtual);
	
}
