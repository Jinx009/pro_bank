package com.rongdu.p2psys.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.dao.FinanceArticleExpertDao;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;
import com.rongdu.p2psys.core.service.FinanceArticleExpertService;

/**
 * 专家
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
@Service("financeArticleExpertService")
public class FinanceArticleExpertServiceImpl implements FinanceArticleExpertService {
	@Resource
	private FinanceArticleExpertDao financeArticleExpertDao;
	
	@Override
	public FinanceArticleExpert getExpertById(long id) {
		
		return financeArticleExpertDao.find(id);
	}

}
