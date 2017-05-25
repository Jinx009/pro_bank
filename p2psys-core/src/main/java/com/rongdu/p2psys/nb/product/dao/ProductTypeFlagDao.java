package com.rongdu.p2psys.nb.product.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;

/**
 * 产品标示DAO
 */
public interface ProductTypeFlagDao extends BaseDao<ProductTypeFlag> {

	PageDataList<ProductTypeFlag> getPojoListForPaging(int pageNumber,
			int pageSize);

	List<ProductTypeFlag> getPojoList(Integer status);

}
