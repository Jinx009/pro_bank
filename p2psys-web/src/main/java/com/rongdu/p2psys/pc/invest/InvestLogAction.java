package com.rongdu.p2psys.pc.invest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.borrow.model.InvestDetailModel;
import com.rongdu.p2psys.nb.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.user.domain.User;

@SuppressWarnings("rawtypes")
public class InvestLogAction extends BaseAction implements ModelDriven<InvestDetailModel> {

	private InvestDetailModel model = new InvestDetailModel();
	
	@Override
	public InvestDetailModel getModel() {
		return model;
	}

	private User user;
	
	@Resource
	private BorrowTenderService theBorrowTenderService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductBasicService productBasicService;

	private Map<String, Object> data;
	
	@Action(value="/invest/log",results=
		{
			@Result(name="log",type="ftl",location="/nb/pc/invest/invest.html")
		})
	public String invest(){
		return "log";
	}
	
	@SuppressWarnings("unchecked")
	@Action("/invest/record")
	public void investList() throws IOException{
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			model.setUser(user);
			PageDataList<InvestDetailModel> list = theBorrowTenderService.getInvestRecordByItem(model);
			data.put("data", list);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
}
