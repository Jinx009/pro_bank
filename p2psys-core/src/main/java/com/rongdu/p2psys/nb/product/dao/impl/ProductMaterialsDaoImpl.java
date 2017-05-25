package com.rongdu.p2psys.nb.product.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.product.dao.ProductMaterialsDao;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;

@Repository("productMaterialsDao")
public class ProductMaterialsDaoImpl extends BaseDaoImpl<ProductMaterials>
		implements ProductMaterialsDao
{

}
