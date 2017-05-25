package com.rongdu.p2psys.core.service;

import com.rongdu.p2psys.core.domain.FinanceArticleExpert;

/**
 * 专家
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
public interface FinanceArticleExpertService {
	/**
	 * 根据ID获取顾问
	 * @param id
	 * @return
	 */
	FinanceArticleExpert getExpertById(long id);
}
