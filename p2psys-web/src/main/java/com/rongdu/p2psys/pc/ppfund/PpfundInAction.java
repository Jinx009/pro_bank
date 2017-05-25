 package com.rongdu.p2psys.pc.ppfund;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;

public class PpfundInAction extends BaseAction<PpfundInModel> implements ModelDriven<PpfundInModel> { 

	@Resource
	private PpfundInService ppfundInService;
	
	/**
	 * ppfund转出
	 * 
	 * @throws Exception
	 */
	@Action(value = "/ppfund/doPpfundOut")
	public void doPpfundOut() throws Exception {
		PpfundIn in = ppfundInService.getById(model.getId());
		double redeemMoney = paramDouble("redeemMoney");
		PpfundOut out = new PpfundOut();
		out.setMoney(redeemMoney);
//		out.setMoney(in.getMoney());
		out.setPpfund(in.getPpfund());
		out.setPpfundIn(in);
		Ppfund ppfund = in.getPpfund();
		//检测产品是否支持转出
		if (ppfund.getIsFixedTerm() == 1) {
			printWebResult("固定期限产品不支持手动转出", false);
			return;
		}
		try{
			ppfundInService.doOut(out);
		}catch(Exception e)
		{
			printWebResult(e.getMessage(), false);
		}
		
		printWebResult("转出成功", true);
	}
	
}
