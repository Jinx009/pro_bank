package com.rongdu.p2psys.wx;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品转入
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月19日
 */
public class WxPpfundInAction extends BaseAction<PpfundInModel> implements
		ModelDriven<PpfundInModel> {
	@Resource
	private PpfundService ppfundService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private AccountService accountService;
	@Resource
	private AccountBankService accountBankService;

	private User user;

	/**
	 * 转入
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "/wx/account/ppfundin", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") }, results = { @Result(name = "result", type = "ftl", location = "/wechat/result.html")
			,@Result(name = "msg", type = "ftl", location = "/wechat/msg.html")})
	public String ppfundIn() throws Exception {
	
		user = getSessionUser();
		Ppfund ppfund = ppfundService.getPpfundById(model.getId());
		try {
			this.checkToken("ppfundTenderToken");
			if (user.getUserCache().getUserType() == 2) {
				throw new BussinessException("借款账户不能投资");
			}
			// 检测转出时间是否符合
			if (model.getOutTime() != null) {
				// 计算间隔天数
				int days = DateUtil.daysBetween(new Date(), model.getOutTime());
				if (days % ppfund.getCycle() != 0) {
					throw new BussinessException("转出时间选择不正确", 2);
				}
			}
		} catch (BussinessException e) {
			throw new BussinessException(e.getMessage(), 2);
		}
		// if(ppfund.getStatus() != 1){
		// throw new BussinessException("请求错误", 2);
		// }
		model.setUser(user);
		model.setPpfund(ppfund);
		ConcurrentUtil.ppfundTender(model);
		String resultFlag = System.currentTimeMillis() + "" + Math.random();
		Global.RESULT_MAP.put(resultFlag, "success");
		request.setAttribute("resultFlag", resultFlag);
		
		PayDetailReq payDetailReq = new PayDetailReq();
		payDetailReq.setProjectId(ppfund.getId());
		payDetailReq.setProjectName(ppfund.getName());
		if(model.getMoney()==0){
			payDetailReq.setInvestPrice(model.getMoney()+"");
		}else{
			payDetailReq.setInvestPrice(model.getAccount()+"");
		}
		payDetailReq.setProjectType("ppfund");
		request.setAttribute("data", payDetailReq);
		return "result";
	}

	
}
