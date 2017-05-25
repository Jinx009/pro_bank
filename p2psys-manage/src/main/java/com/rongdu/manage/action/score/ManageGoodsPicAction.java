package com.rongdu.manage.action.score;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.domain.GoodsPic;
import com.rongdu.p2psys.score.service.GoodsPicService;

public class ManageGoodsPicAction extends BaseAction implements ModelDriven<GoodsPic> {

	private GoodsPic model = new GoodsPic();
	
	@Override
	public GoodsPic getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Resource
	private GoodsPicService goodsPicService;
	
	@Action("/modules/user/score/deleteGoodsPic")
	public void deletePic() throws Exception{
		goodsPicService.deleteGoodsPic(model.getId());
		printJson("SUCCESS");
	}
}
