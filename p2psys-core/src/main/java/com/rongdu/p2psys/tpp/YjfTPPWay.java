package com.rongdu.p2psys.tpp;

import java.util.HashMap;
import java.util.Map;

import com.rongdu.p2psys.account.dao.AccountCashDao;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.ips.model.IpsMerchentUserInfo;
import com.rongdu.p2psys.tpp.yjf.PayModelHelper;
import com.rongdu.p2psys.tpp.yjf.model.BankCodeBindingAddInfo;
import com.rongdu.p2psys.tpp.yjf.model.BankCodeBindingRemove;
import com.rongdu.p2psys.tpp.yjf.model.ForwardConIdentify;
import com.rongdu.p2psys.tpp.yjf.model.Recharge;
import com.rongdu.p2psys.tpp.yjf.model.TradeCreatePoolReverse;
import com.rongdu.p2psys.tpp.yjf.model.YzzNewWithraw;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * 易极付接口
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月22日
 */
public class YjfTPPWay extends BaseTPPWay {
	private long userId;
	private UserModel model;
	private User user;
	private AccountRechargeModel accountRechargeModel;
	private String extra;
	private Borrow borrow;

	public YjfTPPWay(UserModel model, User user, AccountRechargeModel accountRechargeModel, String extra,Borrow borrow) {
		this.model = model;
		this.user = user;
		this.accountRechargeModel = accountRechargeModel;
		this.extra = extra;
		this.borrow = borrow;
	}

	/**
	 * 无参构造函数
	 */
	public YjfTPPWay() {
	}

	@Override
	public ForwardConIdentify doRealname() {
		/**
		 * 调用接口处理具体的实名逻辑 1、封装请求参数 2、请求接口 3、封装返回参数 4、本地处理
		 */

		ForwardConIdentify userRegister = PayModelHelper.userForwardRegister(model);
		return userRegister;
	}

	@Override
	public Recharge doRecharge() {
		Recharge recharge = PayModelHelper.recharge(user, accountRechargeModel.getMoney()+"", extra);
		return recharge;
	}

	@Override
	public Object bindBank(AccountBank bank, User user, String bankType) {
		/*
		BankCodeBindingAddInfo bba = PayModelHelper.bankCodeBindingAddInfo(bank.getBankNo(), user.getApiId(), user.getRealName(), bankType);
		if (!bba.getResultCode().contains("EXECUTE_SUCCESS")) {
			throw new AccountException("绑定银行卡失败：" + bba.getResultCode() + "/" + bba.getMessage(), "1");
		}
		*/
		return null;
	}

	@Override
	public Map<String, Object> bankBindRemove(AccountBank ab, User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "解绑银行卡成功！");
		map.put("result", true);
		/*
		BankCodeBindingRemove bb = PayModelHelper.bankCodeBindingRemove(user.getApiId(), ab.getBankNo());
		if (!bb.getResultCode().contains("EXECUTE_SUCCESS")) {
			throw new AccountException("解绑银行卡失败", 1);
		}
		*/
		return map;
	}

	@Override
	public YzzNewWithraw doNewCash(AccountCash cash, User user, int cashnum,String province, String city, String bankCode) {
		YzzNewWithraw ynw = PayModelHelper.yzzNewWithraw(cash, user, cashnum, province, city,bankCode);
		if (!ynw.getResultCode().contains("EXECUTE_SUCCESS")) {
			throw new AccountException("提现申请失败", 1);
		}
		AccountCashDao accountCashDao=(AccountCashDao)BeanUtil.getBean("accountCashDao");
		accountCashDao.updateOrderNo(cash.getId(), ynw.getOrderNo());
		AccountCash newCash=accountCashDao.find(cash.getId());
		if(newCash.getStatus()==0){
			accountCashDao.updateStatus(cash.getId(), 9, 0);
		}
		return ynw;
	}

	@Override
	public TradeCreatePoolReverse tradeCreatePoolReverse(String apiId, double money) {
		if (isOpenApi()) {
			return PayModelHelper.tradeCreatePoolReverse(apiId, money);
		} else {
			return null;
		}
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

    @Override
    public IpsMerchentUserInfo queryMerUserInfo() throws Exception {
        return null;
    }

	@Override
	public Object doBorrowAuto(BorrowAuto auto) {
		return null;
	}

	@Override
	public ChinapnrModel autoTender(User user, String[][] args, long id,
			double validAccount) {
		
		return null;
	}

	@Override
	public Object creditAssign(BondModel bondModel) {
		return null;
	}
}
