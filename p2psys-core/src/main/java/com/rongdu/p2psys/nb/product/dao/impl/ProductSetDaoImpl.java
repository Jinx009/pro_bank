package com.rongdu.p2psys.nb.product.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.product.dao.ProductSetDao;
import com.rongdu.p2psys.nb.product.domain.ProductSet;

@Repository("productSetDao")
public class ProductSetDaoImpl extends BaseDaoImpl<ProductSet> implements
		ProductSetDao
{

}
