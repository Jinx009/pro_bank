 package com.rongdu.p2psys.wechat;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;

public class WechatPpfundInAction extends BaseAction<PpfundInModel> implements ModelDriven<PpfundInModel> { 

	@Resource
	private PpfundInService ppfundInService;
	
	/**
	 * ppfund转出
	 * 
	 * @throws Exception
	 */
	@Action(value = "/nb/wechat/account/doPpfundOut")
	public void doPpfundOut() throws Exception 
	{
		PpfundIn in = ppfundInService.getById(model.getId());
		Ppfund ppfund = in.getPpfund();
		
		//检测产品是否支持转出
		if (ppfund.getIsFixedTerm() == 1) 
		{
			printWebResult("固定期限产品不支持手动转出", false);
			return;
		}
		
		PpfundOut out = new PpfundOut();
		out.setPpfund(ppfund);
		out.setPpfundIn(in);
		out.setMoney(in.getMoney());
		
		try{
			ppfundInService.doOut(out);
		}catch(Exception e)
		{
			printWebResult(e.getMessage(), false);
		}
		
		printWebResult("转出成功", true);
	}
	
}
