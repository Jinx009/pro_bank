package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.FinanceArticleDao;
import com.rongdu.p2psys.core.domain.FinanceArticle;

@Repository("financeArticleDao")
public class FinanceArticleDaoImpl extends BaseDaoImpl<FinanceArticle> implements FinanceArticleDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceArticle> getFinanceArticleList(long financeSiteId, int total) {
		String sql = "select * from rd_finance_article where status=1  and is_delete=0 and finance_site_id=?1 order by add_time desc limit 0, ?2";
		Query query = em.createNativeQuery(sql, FinanceArticle.class).setParameter(1, financeSiteId).setParameter(2, total);
		return query.getResultList();
	}

}
