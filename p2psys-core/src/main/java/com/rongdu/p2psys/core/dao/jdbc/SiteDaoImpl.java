package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.SiteDao;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.SiteModel;

@Repository("siteDao")
public class SiteDaoImpl extends BaseDaoImpl<Site> implements SiteDao {
	
	@Override
	public List<Site> list() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("pid", 0);
		param.addParam("isDelete", 0);
		param.addOrder(OrderType.ASC, "sort");
		return findByCriteria(param);
	}

	@Override
	public List<Site> list(long pid, int status) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("pid", pid).addParam("status", status).addParam("isDelete", 0).addOrder("sort");
		return findByCriteria(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Site> siteList(SiteModel model) {
		StringBuffer sql = new StringBuffer("select * from rd_site where is_delete = 0 order by id desc");
		Query query = em.createNativeQuery(sql.toString(), Site.class);
		List<Site> list = query.getResultList();
		return list;
	}

}
