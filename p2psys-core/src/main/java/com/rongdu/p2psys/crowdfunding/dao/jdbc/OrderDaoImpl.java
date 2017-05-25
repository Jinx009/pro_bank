package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.crowdfunding.dao.OrderDao;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;

@Repository("cfOrderDao")
public class OrderDaoImpl extends BaseDaoImpl<InvestOrder> implements OrderDao {

	@SuppressWarnings("unchecked")
	public List<InvestOrder> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<InvestOrder> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	public PageDataList<OrderModel> getOrderList(OrderModel model,
			int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("projectBaseinfo.id",
				model.getProjectBaseinfo().getId());

		PageDataList<InvestOrder> domainList = super.findPageList(param);
		PageDataList<OrderModel> modelList = new PageDataList<OrderModel>();
		List<OrderModel> list = new ArrayList<OrderModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0)
		{
			for (InvestOrder tempDomain : domainList.getList())
			{
				OrderModel tempModel = OrderModel
						.instance(tempDomain);
				tempModel.setProjectName(tempDomain.getProjectBaseinfo().getProjectName());
				tempModel.setUserName(tempDomain.getUser().getRealName());
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

}
