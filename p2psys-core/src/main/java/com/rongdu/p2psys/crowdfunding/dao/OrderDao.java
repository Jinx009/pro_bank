package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;

public interface OrderDao extends BaseDao<InvestOrder> {

	public List<InvestOrder> getByHql(String hql);

	public PageDataList<OrderModel> getOrderList(OrderModel model,
			int pageNumber, int pageSize);

}
