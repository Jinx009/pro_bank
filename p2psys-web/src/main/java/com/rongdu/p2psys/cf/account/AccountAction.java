package com.rongdu.p2psys.cf.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;

public class AccountAction extends BaseAction<AccountModel> implements ModelDriven<AccountModel>{

	private Map<String,Object> data;
	
	@Resource
	private OrderService cfOrderService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private AccountBankService theAccountBankService;
	
	@Action(value = "/cf/user/account")
	public void userAccount() throws IOException{
		String bankCode = "";
		String bankNo = "";
		User user = getNBSessionUser();
		Account account = theAccountService.getAccountByUserId(user.getUserId());
		Double investMoeny = cfOrderService.getInvestMoney(user.getUserId());
		List<AccountBank> list = theAccountBankService.getByUserId(user.getUserId());
		if(null!=list){
			for(int i = 0;i<list.size();i++){
				if(1==list.get(i).getStatus()){
					bankCode = list.get(i).getBankCode();
					if (StringUtil.isNullOrBlank(bankCode)) {
						bankCode = CommonRealize.bank_code_map.get(list.get(i).getBank());
					}
					bankNo = list.get(i).getBankNo();
					bankNo = bankNo.substring(0, 5) + "*******"+ bankNo.substring(12,bankNo.length());
				}
			}
		}
		
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put("useMoney",account.getUseMoney());
		data.put("investMoney",investMoeny);
		data.put("bankCode",bankCode);
		data.put("bankNo",bankNo);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
