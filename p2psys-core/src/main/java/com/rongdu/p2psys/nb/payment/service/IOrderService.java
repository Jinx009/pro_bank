package com.rongdu.p2psys.nb.payment.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.user.domain.User;

public interface IOrderService {
	PageDataList<OrderInfo> findOrderByItem(int pageNumber,
			int pageSize, String searchName);
	
	void updateOrder(OrderInfo model);
	
	void saveOrder(User user,OrderInfo oi);
	
	OrderInfo loadOrderByNo(String orderNo);
}
