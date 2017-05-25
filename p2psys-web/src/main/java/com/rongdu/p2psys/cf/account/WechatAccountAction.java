package com.rongdu.p2psys.cf.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;
import com.rongdu.p2psys.crowdfunding.service.VirtualAccountService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 微信账户信息
 * @author Jinx
 *
 */
public class WechatAccountAction extends BaseAction<AccountModel> implements ModelDriven<AccountModel> {

	@Resource
	private AccountService theAccountService;
	@Resource
	private VirtualAccountService virtualAccountService;
	
	private Map<String,Object> data;

	/**
	 * 获取账户余额
	 * @throws IOException
	 */
	public void getUserAccount() throws IOException{
		User user = getNBSessionUser();
		Account account = theAccountService.getAccountByUserId(user.getUserId());
		VirtualAccount virtualAccount = virtualAccountService.findByUserId(user.getUserId());
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		//虚拟货币
		data.put("account",account);
		//真实货币
		data.put("virtual",virtualAccount);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
