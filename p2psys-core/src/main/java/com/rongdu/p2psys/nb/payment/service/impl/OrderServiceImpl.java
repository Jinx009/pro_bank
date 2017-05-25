package com.rongdu.p2psys.nb.payment.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.nb.payment.dao.IOrderDao;
import com.rongdu.p2psys.nb.payment.domain.Order;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.user.domain.User;

/***
 * 订单处理
 * @author ChenGangwei
 * 2015-07-07
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {
	
	@Resource                        
	private IOrderDao orderDao;
	
	@Override
	public PageDataList<OrderInfo> findOrderByItem(int pageNumber,
			int pageSize, String searchName) {
		return orderDao.findOrderByItem(pageNumber,pageSize, searchName);
	}

	@Override
	public OrderInfo loadOrderByNo(String orderNo) {
		Order order = orderDao.loadOrderByNo(orderNo);
		if(order!=null){			
			OrderInfo model = OrderInfo.instance(order);
			return model;
		}
		return null;
	}

	@Override
	public void updateOrder(OrderInfo model) {
		Order order = OrderInfo.instance(model);
		orderDao.updateOrder(order);		
	}

	@Override
	public void saveOrder(User user,OrderInfo oi) {
		Order order = OrderInfo.instance(oi);
		order.setUser(user);
		orderDao.save(order);
	}

}
