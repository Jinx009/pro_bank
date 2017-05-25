package com.rongdu.p2psys.cf.member;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.rongdu.common.util.Base64Util;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.user.domain.User;

@SuppressWarnings("rawtypes")
public class CFPageAction extends BaseAction {
	@Resource
	private AccountBankService theAccountBankService;
	/**
	 * 众筹账户中心首页
	 * 
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "/cf/user/main", results = { @Result(name = "main", type = "ftl", location = "/nb/cf/user/main_list_asset.html") })
	public String userIndex() throws IOException {
		User user = getNBSessionUser();
		if(null!=user){
			request.setAttribute("x",Base64Util.getBase64(user.getUserName()));
			request.setAttribute("y",Base64Util.getBase64(user.getPwd()));
		}
		return "main";
	}

	/**
	 * 我的关注
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/attention", results = { @Result(name = "attention", type = "ftl", location = "/nb/cf/user/main_list_attention.html") })
	public String attention() {
		return "attention";
	}

	/**
	 * 我的众筹
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/crowdFunding", results = { @Result(name = "crowdFund", type = "ftl", location = "/nb/cf/user/main_list_crowdFunding.html") })
	public String crowdFunding() {
		return "crowdFund";
	}

	/**
	 * 我的发起
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/launch", results = { @Result(name = "launch", type = "ftl", location = "/nb/cf/user/main_list_launch.html") })
	public String launch() {
		return "launch";
	}

	/**
	 * 我的资料
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/infor", results = { @Result(name = "infor", type = "ftl", location = "/nb/cf/user/main_list_information.html") })
	public String information() {
		return "infor";
	}

	/**
	 * 资金动向
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/fund", results = { @Result(name = "fund", type = "ftl", location = "/nb/cf/user/main_list_fund.html") })
	public String fund() {
		return "fund";
	}

	/**
	 * 众筹-提现线上
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/cash", results = { @Result(name = "cash", type = "ftl", location = "/nb/cf/cash/main_list_cash.html") })
	public String cash() {
		return "cash";
	}

	/**
	 * 众筹-提现线下
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/offlinecash", results = { @Result(name = "offlinecash", type = "ftl", location = "/nb/cf/cash/main_list_cash_offline.html") })
	public String offlinecash() {
		return "offlinecash";
	}

	/**
	 * 众筹-充值线上
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/recharge", results = { @Result(name = "recharge1", type = "ftl", location = "/nb/cf/recharge/main_list_recharge.html"),
			@Result(name = "recharge2", type = "ftl", location = "/nb/cf/recharge/main_list_recharge_band_card.html")})
	public String recharge() {
		if(hasSessionUser()){
			User user = getNBSessionUser();
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			if(bankList!=null && bankList.size()>0){
				return "recharge1";
			}else{
				return "recharge2";
			}
		}
		return "recharge1";
	}
	
	/**
	 * 众筹-充值线上-下一步
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/rechargeNext", results = { @Result(name = "recharge", type = "ftl", location = "/nb/cf/recharge/main_list_recharge_band_two.html")})
	public String rechargeNext() {
		return "recharge";
	}

	/**
	 * 众筹-充值线下
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/offlinerecharge", results = { @Result(name = "offlinerecharge", type = "ftl", location = "/nb/cf/recharge/main_list_recharge_offline.html") })
	public String offlinerecharge() {
		return "offlinerecharge";
	}
}
