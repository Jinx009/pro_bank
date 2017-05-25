package com.rongdu.p2psys.nb.product.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.product.dao.ProductTypeFlagDao;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;

@Repository("productTypeFlagDao")
public class ProductTypeFlagDaoImpl extends BaseDaoImpl<ProductTypeFlag>
		implements ProductTypeFlagDao {

	@Override
	public PageDataList<ProductTypeFlag> getPojoListForPaging(int pageNumber,
			int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");
		return findPageList(param);
	}

	@Override
	public List<ProductTypeFlag> getPojoList(Integer status) {
		QueryParam param = QueryParam.getInstance();
		if (null != status) {
			param.addParam("isEnable", status);
		}
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");
		return this.findByCriteria(param);
	}

}
