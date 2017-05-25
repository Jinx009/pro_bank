package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;

public interface FinanceArticleExpertDao extends BaseDao<FinanceArticleExpert> {

	/**
	 * 根据status获得FinanceArticleExpert
	 * @return
	 */
	public List<FinanceArticleExpert> getFinanceArticleExpertByStatus();

	/**
	 * 专家介绍
	 * @param financeSiteId
	 * @return
	 */
	public List<FinanceArticleExpert> expertIntroduce(long financeSiteId);

}
