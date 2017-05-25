package com.rongdu.p2psys.web.finance;

import javax.annotation.Resource;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.model.FinanceSiteModel;
import com.rongdu.p2psys.core.service.FinanceSiteService;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class FinanceSiteAction extends BaseAction implements ModelDriven<FinanceSiteModel> {
	
	private FinanceSiteModel model = new FinanceSiteModel();
	
	@Override
	public FinanceSiteModel getModel() {
		return model;
	}
	
	@Resource
	private FinanceSiteService financeSiteService;
	
	
	

}
