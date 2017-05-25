package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.FinanceArticle;

public interface FinanceArticleDao extends BaseDao<FinanceArticle> {

	/**
	 * 更加栏目获得文章列表
	 * @param financeSiteId
	 * @return
	 */
	public List<FinanceArticle> getFinanceArticleList(long financeSiteId, int total);

}
