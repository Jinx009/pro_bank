package com.rongdu.p2psys.pc.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.user.domain.User;

/**
 * 资金纪录
 * @author Chris
 *
 */
public class AccountLogAction extends BaseAction<AccountLogModel> implements ModelDriven<AccountLogModel>{

	@Resource
	private AccountLogService theAccountLogService;
	
	private User user;

	private Map<String, Object> data;
	
	
	@Action(value="/account/log",results=
		{
			@Result(name="log",type="ftl",location="/nb/pc/account/accountLog.html")
		})
	public String log(){
		return "log";
	}
	
	/**
	 * 资金纪录
	 * @throws IOException 
	 */
	@Action("/account/logList")
	public void capitalLogList() throws IOException{
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			model.setUser(user);
			PageDataList<AccountLogModel> list = theAccountLogService.getAccountLog(model);
			data.put("data", list);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
}
