package com.rongdu.p2psys.web.finance;

import javax.annotation.Resource;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.model.FinanceArticleModel;
import com.rongdu.p2psys.core.service.FinanceArticleService;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class FinanceArticleAction extends BaseAction implements ModelDriven<FinanceArticleModel> {

	private FinanceArticleModel model = new FinanceArticleModel();
	
	@Override
	public FinanceArticleModel getModel() {
		return model;
	}
	
	@Resource
	private FinanceArticleService financeArticleService;
	
	
	
}
