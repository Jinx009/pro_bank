package com.rongdu.p2psys.nb.product.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.product.dao.ProductTypeDao;
import com.rongdu.p2psys.nb.product.domain.ProductType;

@Repository("productTypeDao")
public class ProductTypeDaoImpl extends BaseDaoImpl<ProductType> implements
		ProductTypeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductType> getProductTypeListByFlag(Long flagId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT npt.*                     ");
		sql.append("  FROM nb_product_basic npb,     ");
		sql.append("       nb_product_type npt       ");
		sql.append(" WHERE npb.type_id = npt.id      ");
		sql.append("   AND npt.is_enable = 1         ");
		sql.append("   AND npb.flag_id = :flagId     ");
		sql.append(" GROUP BY npb.type_id            ");
		sql.append(" ORDER BY npt.recommend_time DESC");

		Query query = em.createNativeQuery(sql.toString(), ProductType.class);
		query.setParameter("flagId", flagId);

		return query.getResultList();
	}

}
