package com.rongdu.p2psys.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.Pay;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrWebPayModel;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinaPnrType;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.service.UserRedPacketService;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Resource
	private AccountDao accountDao;
	@Resource
	private AccountRechargeDao accountRechargeDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private UserDao userDao;
	@Resource
	private ChinapnrService chinapnrService;

	@Override
	public PageDataList<AccountModel> list(AccountModel model) {
		return accountDao.list(model);
	}
	
	@Override
	public PageDataList<AccountModel> userList(AccountModel model) {
		return accountDao.userList(model);
	}
	@Override
	public PageDataList<AccountModel> exportUserList(AccountModel model) {
		return accountDao.exportUserList(model);
	}
	public Account findByUser(long userId) {
		return accountDao.findObjByProperty("user.userId", userId);
	}

	public double getAccountUseMoney(long userId) {
		return accountDao.getAccountUseMoney(userId);
	}

	@Override
	public AccountModel getUserCollectionAccount(long userId) throws Exception {
		AccountModel accountModel = null;
		/*
		 * borrowCollectionDao.getCollectionCapitalAndInterest(userId); BorrowCollection borrowCollection =
		 * borrowCollectionDao.getNewestCollection(userId);
		 * accountModel.setNewestCollectMoney(borrowCollection.getRepay_account() );
		 * accountModel.setNewestCollectDate(borrowCollection.getRepay_time());
		 */

		return accountModel;
	}

	public void updateAccount(Account act) {
		accountDao.modify(act);
	}

	@Override
	public synchronized void newRecharge(String orderId, String returnText, Pay pay) {
		AccountRecharge existRecharge = accountRechargeDao.getRechargeByTradeno(orderId);
		if (existRecharge == null) {
			return;
		}
		if (existRecharge.getStatus() == 0 || existRecharge.getStatus() == 2) {
			// 修改订单状态
			existRecharge.setStatus(1);
			existRecharge.setReturnMsg(returnText);
			accountRechargeDao.update(existRecharge);
			
			// 资金记录表
			Global.setTransfer("recharge", existRecharge);
			Global.setTransfer("user", existRecharge.getUser());
			AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
			executer.execute(existRecharge.getMoney(), existRecharge.getUser());

			double recharge_fee = getRechargeFee(existRecharge, pay);
			if (recharge_fee > 0) {
				double fee = BigDecimalUtil.mul(existRecharge.getMoney(), recharge_fee);
				if (fee >= 0.01) {
					existRecharge.setFee(fee);
					accountRechargeDao.updateRechargeFee(fee, existRecharge.getId());
					Global.setTransfer("payment", "");
					Global.setTransfer("deduct", fee);
					executer = ExecuterHelper.doExecuter("deductRechargeFeeExecuter");
					executer.execute(fee, existRecharge.getUser());
				}
			}
		}
	}

	private double getRechargeFee(AccountRecharge existRecharge, Pay pay) {
		double recharge_fee = 0.00;
		if (pay != null) {
			recharge_fee = pay.getRechargeFee();
		}
		return recharge_fee;
	}

	public void failRecharge(String orderId, String returnText) {
		AccountRecharge existRecharge = accountRechargeDao.getRechargeByTradeno(orderId);
		if (existRecharge == null) {
			return;
		}
		if (existRecharge.getStatus() == 0) {
			existRecharge.setStatus(2);
			existRecharge.setReturnMsg(returnText);
			accountRechargeDao.update(existRecharge);
		}
	}

	@Override
	public AccountModel getAccount(long userId) {
		// return accountDao.getAccount(userId);
		return null;
	}

	@Override
	public void doRechargeTask(RechargeModel re) {
		newRecharge(re);
	}

	@Override
	public void newRecharge(RechargeModel rem) {
		List<Object> taskList = new ArrayList<Object>();
		AccountRecharge existRecharge = accountRechargeDao.getRechargeByTradeno(rem.getOrderId());
		if (existRecharge == null) {
			throw new BussinessException("订单号不存在, orderNo:" + rem.getOrderId());
		}
		User user = existRecharge.getUser();
		UserCache uc = userCacheDao.findByUserId(user.getUserId());
		QueryParam param = QueryParam.getInstance().addParam("user", user).addParam("status", 1);
		double fee = rechargeFee(rem,existRecharge);
		int rechargeWeb = Global.getInt("recharge_web"); // 获取平台是否自己垫付充值手续费
		if (existRecharge.getStatus() == 0 || existRecharge.getStatus() == 2) {
		    double amountIn =existRecharge.getMoney();
			// 修改订单状态
			//existRecharge.setMoney(Double.parseDouble(rem.getOrderAmount()));
			existRecharge.setStatus(1); // 设置状态
			existRecharge.setReturnMsg(rem.getSerialNo()); // 返回参数
			String[] rechargeWebUserType = Global.getValue("recharge_web_user_type").split(",");//获取平台垫付用户类型
			//校验用户是否属于平台垫付手续费类型
			Boolean flag = false;
			if(rechargeWebUserType.length > 0){
				for (String string : rechargeWebUserType) {
					if(string.equals(uc.getUserType()+"")){
						flag = true;
					}
				}
			}
			if(rechargeWeb == 1 && flag){// 平台垫付
				existRecharge.setAmountIn(amountIn);
				existRecharge.setRemark(XmlTool.format(existRecharge.getMoney()) + "元,手续费：0元");
				existRecharge.setRechargeFeeBear((byte)1);
				existRecharge.setFee(0);
				//平台垫付充值手续费
				ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
				payModel.setMoney(fee);
				payModel.setPayUser(user);
				doWebPayMoney(payModel, taskList);
				chinapnrService.doApiTask(taskList);
			}else {
				existRecharge.setAmountIn(BigDecimalUtil.sub(amountIn, rem.getFeeAmt()));
				existRecharge.setRemark(XmlTool.format(existRecharge.getMoney()) + "元,手续费：" + XmlTool.formatCeil2Str(fee, 2)+"元");
				existRecharge.setRechargeFeeBear((byte)2);
				existRecharge.setFee(fee);
			}
			accountRechargeDao.update(existRecharge);
			Global.setTransfer("recharge", existRecharge);
			Global.setTransfer("user", existRecharge.getUser());
			Global.setTransfer("ip", existRecharge.getAddIp());
			AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
			executer.execute(existRecharge.getAmountIn(), existRecharge.getUser());
		}
	}
	
	public void doWebPayMoney(ChinapnrWebPayModel payModel,List<Object> taskList) {
		TppPnrPay cppModel = new TppPnrPay(ChinaPnrType.TRANSFER, payModel.getMoney(), "1",
				String.valueOf(payModel.getPayUser().getUserId()), "0", null, ChinaPnrType.WEBPAY);
		// TODO zjj
		// cppModel.setPayUserCustId(payModel.getPayUser().getApiId());
		cppModel.setUsrCustId("");
		taskList.add(cppModel);
	}

	@Override
	public void failRecharge(RechargeModel rem) {
		AccountRecharge existRecharge = accountRechargeDao.getRechargeByTradeno(rem.getOrderId());
		if (existRecharge == null) {
			return;
		}
		existRecharge.setTradeNo(rem.getSerialNo());
		if (existRecharge.getStatus() == 0) {
			accountRechargeDao.updateRecharge(2, rem.getResultMsg(), rem.getSerialNo());
		}
	}

	/**
	 * 充值手续费工具方法
	 * @return double
	 * @param re RechargeModel
	 */
	private double rechargeFee(RechargeModel re, AccountRecharge existRecharge) {
		int apiType = TPPWay.API_CODE;
		switch (apiType) {
			case  2://环讯手续费计算方式
				String ipsFeeRate = Global.getValue("recharge_fee");
				double ipsfee = 0;
				if(re.getChannelType().equals("1")){
				    if (!StringUtil.isBlank(ipsFeeRate)) {
				        ipsfee = StringUtil.toDouble(ipsFeeRate)/100;
				    }
				    return XmlTool.formatCeil2Str(BigDecimalUtil.mul(existRecharge.getMoney(),ipsfee),2);
				}else{
				    return Global.getDouble("borrow_recharge_fee");
				}
			case 3: // 汇付手续费从回调信息中取得
				return re.getFeeAmt();
			default:
				return 0;
		}
	}
	
	@Override
	public double getAllUseMoney() {
	    return accountDao.getAllUseMoney();
	}
	
	@Override
    public double getSumAccount(long user_id)
	{
		 return accountDao.getSumAccount(user_id);
	}

	@Override
	public double getAllTotal() {
		return accountDao.getAllTotal();
	}

	@Override
	public PageDataList<AccountModel> exportList(AccountModel model) {
		// TODO Auto-generated method stub
		return accountDao.exportList(model);
	}
	
}
