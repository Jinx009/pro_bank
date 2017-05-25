package com.rongdu.p2psys.pc.account;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.service.MessageService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

/**
 * 账户中心资金业务
 * @author Chris
 *
 */
@SuppressWarnings("rawtypes")
public class UserMemberAction extends BaseAction {
	
	
	
	@Resource
	private AccountService theAccountService;
	@Resource
	private MessageService messageService;
	@Resource
	private UserService theUserService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private BorrowCollectionService theBorrowCollectionService;
	@Resource
	private FrozenUserService frozenUserService;
	@Resource
	private AccountRechargeService accountRechargeService;
	
	
	private User user;
	
	private Map<String, Object> data;
	
	/**
	 * 账户中心首页
	 * @return
	 */
	@Action(value="/user/center",results=
		{
			@Result(name="main",type="ftl",location="/nb/pc/member/main.html")
		})
	public String accountMain(){
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		request.setAttribute("randTime", sim.format(new Date()));
		return "main";
	}
	
	/**
	 * 当前用户资金统计信息
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@Action("/user/capitalStatis")
	public void  capitalStatis() throws IOException{
		data= new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
		    List<User> userList = theUserService.getByGroupId(user.getBindId());
			if(userList.size() > 0){
				double total =0,useMoney=0,noUseMoney=0,collection=0,sumInMoney=0;
				for (User user_:userList) {
					Account account_ = theAccountService.getAccountByUserId(user_.getUserId());
					//被锁住金额
					double lockUserMoney = frozenUserService.getLockUseMoney(Integer.parseInt(String.valueOf(user_.getUserId())));
					// 在投金额
					double sumInMoney_ = theBorrowCollectionService.inInvestAmount(user_, 0);
					//总资产
					total += account_.getTotal();
					//可用余额
					useMoney += (account_.getUseMoney()-lockUserMoney);
					//冻结金额
					noUseMoney += account_.getNoUseMoney();
					//待收金额
					collection +=account_.getCollection();
					sumInMoney += sumInMoney_;
				}
				data.put("total", total);
				data.put("useMoney", useMoney);
				data.put("noUseMoney", noUseMoney);
				data.put("collection", collection);
				data.put("sumInMoney", sumInMoney);
			}else{
				Account account = theAccountService.getAccountByUserId(user.getUserId());
				//被锁住金额
				double lockUserMoney = frozenUserService.getLockUseMoney(Integer.parseInt(String.valueOf(user.getUserId())));
				// 在投金额
				double sumInMoney_ = theBorrowCollectionService.inInvestAmount(user, 0);
				
				data.put("total", account.getTotal());
				data.put("useMoney", (account.getUseMoney()-lockUserMoney));
				data.put("noUseMoney",account.getNoUseMoney());
				data.put("collection",account.getCollection());
				data.put("sumInMoney", sumInMoney_);
			}
			// 累计净收益(多身份)
			double netProfit = theBorrowCollectionService.netProfit(user);
			data.put("netProfit", netProfit);
			//更新用户认证状态
			UserIdentify userIdentify  = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			data.put(Constant.SESSION_USER_IDENTIFY, userIdentify);

			//银行卡数量
			int bankAccount = theAccountBankService.count(user.getUserId());
			data.put("bankAccount", bankAccount);
			data.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 充值记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/recharge/log")
	public String rechargeLog() throws Exception
	{
		AccountRechargeModel recharge = accountRechargeService
				.getRechargeSummary(getSessionUser().getUserId());
		Account account = theAccountService.getAccountByUserId(this.getSessionUserId());
		request.setAttribute("recharge", recharge);
		request.setAttribute("account", account);
		return "log";
	}
	
	
}
