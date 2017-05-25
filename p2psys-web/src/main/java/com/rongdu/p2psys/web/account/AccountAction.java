package com.rongdu.p2psys.web.account;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;

/**
 * 资金
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月17日
 */
@SuppressWarnings("rawtypes")
public class AccountAction extends BaseAction implements ModelDriven<AccountModel> {

	private AccountModel model = new AccountModel();

	@Override
	public AccountModel getModel() {
		return model;
	}

	@Resource
	private AccountService accountService;
	@Resource
	private AccountLogService accountLogService;

	private User user;
	private Map<String, Object> data;

	/**
	 * 资金记录
	 * 
	 * @return
	 */
	@Action(value="/member/account/log",results = { @Result(name = "log", type = "ftl", location = "/member/account/log.html"),
			@Result(name = "log_firm", type = "ftl", location = "/member_borrow/account/log.html")})
	public String log() throws Exception {
		user = getSessionUser();
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2 || user.getUserCache().getUserType() == 4) {
			return "log_firm";
		}
		return "log";
	}

	/**
	 * 资金记录
	 * 
	 * @return
	 */
	@Action("/member/account/logList")
	public void logList() throws Exception {
		user = getSessionUser();
		AccountLogModel m = (AccountLogModel) paramModel(AccountLogModel.class);
		m.setUser(user);
		PageDataList<AccountLogModel> list = accountLogService.list(m);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}

}
