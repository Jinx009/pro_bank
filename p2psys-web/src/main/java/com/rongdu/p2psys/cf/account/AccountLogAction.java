package com.rongdu.p2psys.cf.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 资金日志
 * @author Jinx
 *
 */
public class AccountLogAction extends BaseAction<AccountLogModel> implements ModelDriven<AccountLogModel>{

	private Map<String,Object> data;
	
	@Resource
	private AccountLogService theAccountLogService;
	
	/**
	 * 资金日志
	 * @throws IOException
	 */
	@Action(value = "/cf/user/accountLog")
	public void userAccountLog() throws IOException{
		User user = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		if(null!=user){
			List<AccountLog> list = theAccountLogService.getByUserId(user.getUserId());
			data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			data.put(ConstantUtil.ERRORMSG,list);
		}else{
			data.put(ConstantUtil.ERRORMSG,"用户登陆失效!");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	

	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
