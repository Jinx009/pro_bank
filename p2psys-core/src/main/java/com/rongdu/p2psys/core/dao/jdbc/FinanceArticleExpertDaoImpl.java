package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.FinanceArticleExpertDao;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;

@Repository("financeArticleExpertDao")
public class FinanceArticleExpertDaoImpl extends BaseDaoImpl<FinanceArticleExpert> implements FinanceArticleExpertDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceArticleExpert> getFinanceArticleExpertByStatus() {
		String sql = "select * from rd_finance_article_expert where is_delete=0";
		Query query = em.createNativeQuery(sql, FinanceArticleExpert.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceArticleExpert> expertIntroduce(long financeSiteId) {
		String sql = "select distinct fae.* from rd_finance_article fa, rd_finance_site fs, "
				+ "rd_finance_article_expert fae where fa.finance_site_id=fs.id "
				+ "and fa.finance_article_expert_id=fae.id and fa.status=1 and fs.status=1 and fae.is_delete=0 and fs.id=?1";
		Query query = em.createNativeQuery(sql, FinanceArticleExpert.class).setParameter(1, financeSiteId);
		return query.getResultList();
	}

	
	
}
