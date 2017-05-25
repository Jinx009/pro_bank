package com.rongdu.p2psys.account.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.ProductsCost;
import com.rongdu.p2psys.account.model.ProductsCostModel;

public interface ProductsCostDao extends BaseDao<ProductsCost> {
	/**
	 * 分页查询
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<ProductsCostModel> list(ProductsCostModel model);
}
