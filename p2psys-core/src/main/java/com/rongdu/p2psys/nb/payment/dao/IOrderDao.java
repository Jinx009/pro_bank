package com.rongdu.p2psys.nb.payment.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.nb.payment.domain.Order;

public interface IOrderDao extends BaseDao<Order> {
	PageDataList<OrderInfo> findOrderByItem(int pageNumber,
			int pageSize, String searchName);
	
	Order loadOrderByNo(String orderNo);
	
	void updateOrder(Order order);
}
