package com.rongdu.p2psys.nb.payment.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.nb.payment.dao.IOrderDao;
import com.rongdu.p2psys.nb.payment.domain.Order;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDaoImpl<Order>  implements IOrderDao {

	@Override
	public PageDataList<OrderInfo> findOrderByItem(int pageNumber,
			int pageSize, String searchName) {
		PageDataList<OrderInfo> pageDataList = new PageDataList<OrderInfo>();
		StringBuffer sql =new StringBuffer("select no.* from nb_order as no left join rd_user ru on no.user_id=ru.user_id where 1=1 ");
		if(!StringUtil.isBlank(searchName)){
			sql.append("AND ru.real_name like '%"+searchName+"%'");
		} 
		sql.append(" order by no.id desc");
		Query query = em.createNativeQuery(sql.toString(),Order.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Order> list = query.getResultList();
        List<OrderInfo> list_ =new ArrayList<OrderInfo>();
        for (Order order : list) {
			OrderInfo model = OrderInfo.instance(order);
			model.setRealName(order.getUser().getRealName());
			list_.add(model);
		}
        pageDataList.setList(list_);
        pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public Order loadOrderByNo(String orderNo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("no_order", orderNo);
		List<Order> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (Order) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateOrder(Order Order) {
		em.merge(Order);
	}

}
