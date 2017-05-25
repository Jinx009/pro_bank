package com.rongdu.p2psys.account.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.account.dao.PayOfflinebankDao;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.PayOfflinebank;
import com.rongdu.p2psys.account.model.AccountMoneyModel;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.ToPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.nb.account.dao.AccountBankDao;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.payment.dao.IChannelBankDao;
import com.rongdu.p2psys.nb.payment.dao.IChannelDao;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.user.dao.UserCacheDao;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.tpp.IpsTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.ips.model.IpsRechargeBank;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;


@Service("accountRechargeService")
public class AccountRechargeServiceImpl implements AccountRechargeService {
    private final static Logger logger = Logger.getLogger(AccountRechargeServiceImpl.class);
	@Resource
	private AccountRechargeDao accountRechargeDao;
	@Resource
	private OperationLogDao operationLogDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
	private DictDao dictDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private AccountBankDao theAccountBankDao;
//	@Resource
//	private UserService userService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private IChannelDao channelDao;
	@Resource
	private IChannelBankDao channelBankDao;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private IOrderService orderService;
//	@Resource
//	private UserService theUserService;
	@Resource
	private UserDao theUserDao;
	@Resource
	private UserCacheDao theUserCacheDao;
	@Resource
	private UserIdentifyDao theUserIdentifyDao;
	@Resource
	private PayOfflinebankDao payOfflinebankDao;

	@Override
	public AccountRecharge add(AccountRecharge r) {
		return accountRechargeDao.save(r);
	}

	@Override
	public PageDataList<AccountRechargeModel> list(AccountRechargeModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{//精确条件查询
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			if (model != null && model.getStatus() != 99) {
				param.addParam("status", model.getStatus());
			}
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime());
	            param.addParam("addTime", Operators.GTE, start);
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime());
	            param.addParam("addTime", Operators.LTE, end);
	        }
	        if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		param.addPage(model.getPage(), model.getRows());
		if (model.getOrder().equals("desc")) {
			param.addOrder(OrderType.DESC, model.getSort());
		} else {
			param.addOrder(OrderType.ASC, model.getSort());
		}
		PageDataList<AccountRecharge> pageDateList = accountRechargeDao.findPageList(param);
		PageDataList<AccountRechargeModel> pageDateList_ = new PageDataList<AccountRechargeModel>();
		List<AccountRechargeModel> list = new ArrayList<AccountRechargeModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountRecharge recharge = (AccountRecharge) pageDateList.getList().get(i);
				AccountRechargeModel arm = AccountRechargeModel.instance(recharge);
				VerifyLog verifyLog = verifyLogDao.findByType(recharge.getId(), "verifyAccountRecharge", 1);
				if (verifyLog != null) {
					arm.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
				}
				try{
    				arm.setUserName(recharge.getUser().getUserName());
    				String accName = recharge.getRealName();
    				if(StringUtil.isBlank(recharge.getRealName())){
    					accName = recharge.getUser().getRealName();
    				}
    				arm.setRealName(accName);
					PayOfflinebank pay = recharge.getPayOfflinebank();
					if (pay != null) {
	    				arm.setPayOfflinebankInfo(pay.getBankNo()+":"+pay.getBank()+pay.getBranch());
					}
    				list.add(arm);
				} catch(Exception e){
                    e.printStackTrace();
                    logger.error(e);
				}
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public PageDataList<AccountRechargeModel> list(long userId, AccountRechargeModel model) {
		QueryParam param = QueryParam.getInstance();
		if(model != null){
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			Date nowdate = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
			if (model.getTime() == 7) {
				param.addParam("addTime", Operators.GTE,DateUtil.rollDay(nowdate, -7));
				param.addParam("addTime", Operators.LTE, nowdate);
			} else if (model.getTime() > 0 && model.getTime() < 4){
				param.addParam("addTime", Operators.GTE,DateUtil.rollMon(nowdate, -model.getTime()));
				param.addParam("addTime", Operators.LTE, nowdate);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			if (model.getStatus() != 9) {
				param.addParam("status", model.getStatus());
			}
		}
		param.addParam("user.userId", userId);
		param.addPage(model.getPage());
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<AccountRecharge> pageDateList = accountRechargeDao.findPageList(param);
		PageDataList<AccountRechargeModel> pageDateList_ = new PageDataList<AccountRechargeModel>();
		List<AccountRechargeModel> list = new ArrayList<AccountRechargeModel>();
		pageDateList_.setPage(pageDateList.getPage());
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil.getBean("verifyLogDao");
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountRecharge recharge = (AccountRecharge) pageDateList.getList().get(i);
				AccountRechargeModel arm = AccountRechargeModel.instance(recharge);
				VerifyLog verifyLog = verifyLogDao.findByType(recharge.getId(), "verifyAccountRecharge", 1);
				String remark=(verifyLog != null)?verifyLog.getRemark():"";
				arm.setVerifyRemark(remark);
				String typeStr = "";
				switch (recharge.getType()) {
					case 1:
						typeStr = "网上充值";
						break;
					case 2:
						typeStr = "网上支付";
						break;
					case 3:
						typeStr = "线下充值";
						break;
					case 4:
						typeStr = "后台线下充值";
						break;
					default:
						Dict d = dictDao.find("offline_recharge_type",recharge.getType()+"");
						if (d != null) {
							typeStr = d.getName();
						} else {
							typeStr = "线下充值";
						}
						break;
				}
				/*//支付方式展示的汉字需要修改，由于目前不知道支付方式对应哪些字
				if(recharge.getPayment().equals(Constant.AL_BACK_RECHARGE)){
					arm.setPayment("线下支付");
				}else{
					arm.setPayment("线上支付");
				}*/
				arm.setAddTimeStr(DateUtil.dateStr4(recharge.getAddTime()));
				arm.setTypeStr(typeStr);
				list.add(arm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public AccountRechargeModel getRechargeSummary(long userId) {
		return accountRechargeDao.getRechargeSummary(userId);
	}

	@Override
	public int count(int status) {
		return accountRechargeDao.count(status);
	}

	@Override
	public AccountRecharge find(long id) {
		return accountRechargeDao.find(id);
	}

	@Override
	public void verifyAccountRecharge(AccountRechargeModel model, Operator operator) {
		AccountRecharge accountRecharge = this.find(model.getId());
		VerifyLog verifyLog = new VerifyLog(operator, "verifyAccountRecharge", accountRecharge.getId(), 1,
				model.getStatus(), model.getRemark());
		accountRecharge.setTradeNo(OrderNoUtils.getSerialNumber());
		if (model.getStatus() == 1) { // 通过
			accountRecharge.setAmountIn(model.getMoney());
			Global.setTransfer("deduct", Math.abs(model.getMoney()));
			Global.setTransfer("recharge", accountRecharge);
			Global.setTransfer("user", accountRecharge.getUser());
			if (accountRecharge.getType() == 1 || accountRecharge.getType() == 2) {// 线上充值
				AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
				executer.execute(model.getMoney(), accountRecharge.getUser().getUserId(), operator);
			} else if (accountRecharge.getType() == 3) { // 线下充值
				AbstractExecuter executer = ExecuterHelper.doExecuter("offRechargeSuccessExecuter");
				executer.execute(model.getMoney(), accountRecharge.getUser().getUserId(), operator);
			} else if (accountRecharge.getType() == 4) {// 后台线下充值
				AbstractExecuter executer = ExecuterHelper.doExecuter("backRechargeSuccessExecuter");
				executer.execute(model.getMoney(), accountRecharge.getUser().getUserId(), operator);
			}
			// 后台充值奖励设置
			double offrechargeAward = Global.getDouble("offrecharge_award"); // 线下充值奖励费率，费率大于0是启用线下充值！
			double offrechargeAwardLimit = Global.getDouble("offrecharge_award_limit");
			if (offrechargeAward > 0 && accountRecharge.getType() != 4
					&& model.getMoney() >= offrechargeAwardLimit) {
				double offrechargeAwardValue = model.getMoney() * offrechargeAward;
				Global.setTransfer("award", offrechargeAwardValue);
				AbstractExecuter executer = ExecuterHelper.doExecuter("awardOffRechargeExecuter");
				executer.execute(offrechargeAwardValue, accountRecharge.getUser().getUserId(), operator);
			}
			accountRecharge.setStatus(model.getStatus());
			/*User user = theUserService.getByUserId(accountRecharge.getUser().getUserId());
			user.setRealName(accountRecharge.getRealName());
			theUserService.updateUser(user);
			UserIdentify identify = userIdentifyService.findByUserId(accountRecharge.getUser().getUserId());
			identify.setRealNameStatus(1);
			identify.setRealNameVerifyTime(new Date());
			userIdentifyService.update(identify);*/
			
			UserIdentify identify = userIdentifyService.findByUserId(accountRecharge.getUser().getUserId());
			if(identify.getRealNameStatus()!=1){	
				User user = theUserDao.find(accountRecharge.getUser().getUserId());
				user.setRealName(accountRecharge.getRealName());
				user.setCardId(accountRecharge.getCardId());
				theUserDao.update(user);
				
				identify.setRealNameStatus(1);
				identify.setRealNameVerifyTime(new Date());
				userIdentifyService.update(identify);
			}
		} else if(model.getStatus() == 2) { // 不通过
			if (accountRecharge.getType() == 1 || accountRecharge.getType() == 2) {
				OperationLog operationLog = new OperationLog(accountRecharge.getUser(), operator,
						Constant.ONLINE_RECHARGE_FAIL);
				operationLog.setOperationResult("（" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
						+ "的操作员审核网上充值" + accountRecharge.getUser().getUserName() + model.getMoney() + "元失败");
				operationLogDao.save(operationLog);
			} else if (accountRecharge.getType() == 3) {
				OperationLog operationLog = new OperationLog(accountRecharge.getUser(), operator,
						Constant.LINE_RECHARGE_FAIL);
				operationLog.setOperationResult("（" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
						+ "的操作员审核后台线下充值" + accountRecharge.getUser().getUserName() + model.getMoney() + "元失败");
				operationLogDao.save(operationLog);
			}
			accountRecharge.setStatus(model.getStatus());
			accountRechargeDao.update(accountRecharge);
		} else {//撤回
			accountRecharge.setStatus(0);
		}
		verifyLogDao.save(verifyLog);
		accountRechargeDao.update(accountRecharge);
	}
	@Override
	public List<IpsRechargeBank> getIpsRechargeBankList() { 
		List<IpsRechargeBank> list = new ArrayList<IpsRechargeBank>();
		try {
			list = IpsTPPWay.queryRechargeBank();
		} catch (Exception e) {
			throw new BussinessException("查询充值银行失败，请稍后尝试");
		}
		return list;
	}
	
	@Override
    public void setAccountRecharge(){
        try {
            Date d=new Date();
            long now=d.getTime();
            int dayTime=24*3600*1000;
            QueryParam param = QueryParam.getInstance();
            param.addParam("type", Operators.NOTEQ, 3);
            List<AccountRecharge> list=accountRechargeDao.findByCriteria(param);
            for (AccountRecharge ac:list) {
                if((now-ac.getAddTime().getTime())>= dayTime && ac.getStatus()==0){
                    accountRechargeDao.updateStatus(ac.getId(), 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@Override
    public void cancelCash(AccountRecharge accountRecharge){
	    accountRechargeDao.updateStatus(accountRecharge.getId(), 2,0);
	}
	
	@Override
    public void verifyRecharge(AccountRecharge accountRecharge){
	    accountRechargeDao.updateStatus(accountRecharge.getId(), 1,0);
	    int rechargeWeb = Global.getInt("recharge_web"); // 获取平台是否自己垫付充值手续费
	    // 充值手续费
	    double fee = getRechargeFee(accountRecharge);
        if (rechargeWeb == 0) { // 不扣手续费
            accountRecharge.setRemark(accountRecharge.getRemark());
        } else if (rechargeWeb == 1 && accountRecharge.getUser().getUserCache().getUserNature()==1) { // 平台垫付充值手续费，备注要说明
            accountRecharge.setRemark(XmlTool.format(accountRecharge.getMoney()) + "元,平台垫付手续费：" + XmlTool.formatCeil2Str(fee, 2)+"元");
            accountRecharge.setRechargeFeeBear((byte)1);
        } else {
            accountRecharge.setRemark(XmlTool.format(accountRecharge.getMoney()) + "元,手续费：" + XmlTool.formatCeil2Str(fee, 2)+"元");
            accountRecharge.setRechargeFeeBear((byte)2);
        }
	    accountRecharge.setFee(fee);
	    accountRecharge.setStatus(1);
	    accountRecharge.setAmountIn(accountRecharge.getMoney());
	    accountRechargeDao.update(accountRecharge);
	    Global.setTransfer("recharge", accountRecharge);
        Global.setTransfer("user", accountRecharge.getUser());
        AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
        executer.execute(accountRecharge.getMoney(), accountRecharge.getUser());
	}
	
    /**
     * 取得充值手续费
     * @param existRecharge
     * @return double
     */
    private double getRechargeFee(AccountRecharge existRecharge) {
        int apiType = TPPWay.API_CODE;
        switch (apiType) {
            case  2://环讯手续费计算方式
                String ipsFeeRate = Global.getValue("recharge_fee");
                double ipsfee = 0;
                if(AccountRecharge.TYPE_AUTO_RECHARGE == existRecharge.getType()){
                    return Global.getDouble("borrow_recharge_fee");
                }else{
                    if (!StringUtil.isBlank(ipsFeeRate)) {
                        ipsfee = StringUtil.toDouble(ipsFeeRate)/100;
                    }
                    return XmlTool.formatCeil2Str(BigDecimalUtil.mul(existRecharge.getMoney(),ipsfee),2);
                }
            default:
                return 0;
        }
    }	
	
	@Override
	public int rechargedUserCount() {
	    return accountRechargeDao.rechargedUserCount();
	}
	
	@Override
	public int rechargedUserCount(String startTime, String endTime) {
	    return accountRechargeDao.rechargedUserCount(startTime, endTime);
	}
	
	@Override
	public double rechargedAllMomeny(String startTime, String endTime) {
	    return accountRechargeDao.rechargedAllMomeny(startTime, endTime);
	}
	
    @Override
    public void updateStatusByTradeNo(String tradeNo, int status, byte ipsStatus) {
        
        AccountRecharge accountRecharge = accountRechargeDao.getRechargeByTradeno(tradeNo);
        if(accountRecharge != null && accountRecharge.getStatus() != 1){
            accountRecharge.setStatus(status);
            accountRechargeDao.update(accountRecharge);
        }
    }	
    
     @Override
    public void updatePayMentByTradeNo(String tradeNo, String payMent){
        AccountRecharge accountRecharge = accountRechargeDao.getRechargeByTradeno(tradeNo);
        if(accountRecharge != null){
            accountRecharge.setPayment(payMent);
            accountRechargeDao.update(accountRecharge);
        }
    }
    
    @Override
    public void verifyRecharge(AccountRecharge accountRecharge, Operator operator){
		OperationLog log = new OperationLog(accountRecharge.getUser(), operator, "cancel_cash");
		log.setOperationResult("用户名为" + operator.getUserName() + "（" + Global.getIP() + "）的操作员对用户为"
				+accountRecharge.getUser().getUserName()+"的充值ID为"+accountRecharge.getId()+"）进行充值确认操作");
		operationLogDao.save(log);
	    accountRechargeDao.updateStatus(accountRecharge.getId(), 1,0);
	    int rechargeWeb = Global.getInt("recharge_web"); // 获取平台是否自己垫付充值手续费
	    // 充值手续费
	    double fee = getRechargeFee(accountRecharge);
        if (rechargeWeb == 0) { // 不扣手续费
            accountRecharge.setRemark(accountRecharge.getRemark());
        } else if (rechargeWeb == 1 && accountRecharge.getUser().getUserCache().getUserNature()==1) { // 平台垫付充值手续费，备注要说明
            accountRecharge.setRemark(XmlTool.format(accountRecharge.getMoney()) + "元,平台垫付手续费：" + XmlTool.formatCeil2Str(fee, 2)+"元");
            accountRecharge.setRechargeFeeBear((byte)1);
        } else {
            accountRecharge.setRemark(XmlTool.format(accountRecharge.getMoney()) + "元,手续费：" + XmlTool.formatCeil2Str(fee, 2)+"元");
            accountRecharge.setRechargeFeeBear((byte)2);
        }
	    accountRecharge.setFee(fee);
	    accountRecharge.setStatus(1);
	    accountRecharge.setAmountIn(accountRecharge.getMoney());
	    accountRechargeDao.update(accountRecharge);
	    Global.setTransfer("recharge", accountRecharge);
        Global.setTransfer("user", accountRecharge.getUser());
        AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
        executer.execute(accountRecharge.getMoney(), accountRecharge.getUser());
	}

	@Override
	public void saveBackRecharge(AccountRecharge recharge, Operator operator) {
		accountRechargeDao.save(recharge);
		
		Global.setTransfer("recharge", recharge);
		Global.setTransfer("user", recharge.getUser());
		Global.setTransfer("ip", recharge.getAddIp());
		AbstractExecuter executer = ExecuterHelper.doExecuter("backRechargeSuccessExecuter");
		executer.execute(recharge.getAmountIn(), recharge.getUser().getUserId(), operator);
	}

	@SuppressWarnings("null")
	@Override
	public Map<String, Object> doUnionPay(String payment,AccountRechargeModel model,AccountBank ab) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = model.getUser();

		/*AccountBank ab = null;
		List<AccountBank> abList = theAccountBankService.list(user.getUserId(),channelKey.unionpay.getValue());
		if(abList.size()>0){				
			ab = theAccountBankService.list(user.getUserId()).get(0);
		}*/
		
		//截取两位小数
		double money = BigDecimalUtil.decimal(model.getMoney(),2);
		
		if (money <= 0) {
			map.put("result", false);
			map.put("message", "充值金额不正确！");
			return map;
		}

		boolean flag = false;
		String message = "充值失败";
		if(ab!=null){			
			//封装充值处理参数
			UnionPay pay = new UnionPay(ab.getBindId(), (long) (money * 100), "RMB" + model.getMoney());
			
			//保存充值记录
			AccountRecharge ar = new AccountRecharge(pay.getOrderNo(), user, 0, money, model.getType());
			ar.setChannelKey(channelKey.unionpay.getValue());
			ar.setPayment(payment);
			accountRechargeDao.save(ar);
			
			// 将订单信息保存数据
			OrderInfo order = LianlPayHelper.createOrder(0,payment,String.valueOf(money));
			order.setNo_order(pay.getOrderNo());
			order.setUser(user);
			order.setStatus(0);
			orderService.saveOrder(user,order);//保存订单信息
			
			//执行充值
			UnionPayRet ret = SignHelper.singlePay(pay);
			if(ret == null){
				map.put("result", flag);
				map.put("message", "银联暂无响应，请稍后重试");
				return map;
			}
			if ("0000".equals(ret.getRetCode())) {
				// 处理成功，更新充值记录
				ar = accountRechargeDao.getRechargeByTradeno(ret.getOrderNo());
				if (ret.getOrderStatus() == 2) {// 处理成功
					flag = true;
					double fee = doRechargeFee(ar.getMoney());
					ar.setStatus(1);
					ar.setAmountIn(BigDecimalUtil.sub(ar.getMoney(), fee));
					ar.setFee(fee);
					if(fee > 0) {//如果手续费大于0，则为垫付方为用户
						ar.setRechargeFeeBear((byte) 2);
					} else {
						ar.setRechargeFeeBear((byte) 1);
					}
					ar.setRemark("充值成功，入账金额：" + BigDecimalUtil.sub(ar.getMoney(), fee));
					accountRechargeDao.update(ar);
					
					// 资金记录表
					Global.setTransfer("recharge", ar);
					Global.setTransfer("user", ar.getUser());
					AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
					executer.execute(ar.getMoney(), ar.getUser());
					
					//扣除充值手续费
					if(fee > 0) {
						Global.setTransfer("money", fee);
						AbstractExecuter feeExecuter = ExecuterHelper.doExecuter("deductRechargeFeeExecuter");
						feeExecuter.execute(fee, user);
					}
					message = "充值成功";
					order.setStatus(1);//成功
				} else if (ret.getOrderStatus() == 3) {// 处理失败
					flag = false;
					message = ret.getRetDesc();
					
					ar.setStatus(2);
					accountRechargeDao.update(ar);
					
					order.setStatus(2);//失败
				} else if(ret.getOrderStatus() == 1) {//处理中
					flag = true;
					message = "银联处理中"+ret.getRetDesc();
					ar.setStatus(5);
					accountRechargeDao.update(ar);
					
					order.setStatus(3);//处理中
					//TODO 启动一个定时线程去查询银联处理结果
					selectYLRechargeResult(ar);
				}
			} else {
				flag = false;
				message = ret.getRetDesc();
				String str1[] = message.split(",");
				if(str1.length>1 && str1[1].contains("]")){				
					String str2 = (str1[1].split("]"))[1];
					message = str1[0]+"！商户"+str2+"！";
				}
				ar.setStatus(2);
				accountRechargeDao.update(ar);
				
				order.setStatus(2);//失败
			}
			orderService.updateOrder(order);//更新订单信息
			map.put("result", flag);
			map.put("message", message);
		}else{
			map.put("result", false);
			map.put("message", "您暂未绑卡！");
			return map;
		}
		return map;
	}
	
	//存放请求查询充值结果线程信息
	HashMap<Object, Thread> rechargeThreadsMap = new HashMap<Object, Thread>();
		/**
		 * 查询银联充值处理结果线程
		 * @param cash
		 */
		public void selectYLRechargeResult (final AccountRecharge ar){
			Thread t = new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					try {
						//TODO 请求查询银联充值的结果
						boolean bool = true;
				        while(bool){
							UnionPayRet ret = SignHelper.queryOrder(ar.getTradeNo());
							int orderStatus = ar.getStatus();
							logger.info("================银联充值前==订单状态："+orderStatus);
							OrderInfo order = orderService.loadOrderByNo(ar.getTradeNo());
							if (ret.getOrderStatus() == 2){//处理成功
								logger.info("============银联充值处理成功，订单号："+ar.getTradeNo());
								if(orderStatus!=1){
									logger.info("============银联充值处理成功，进行成功充值操作");
									double fee = doRechargeFee(ar.getMoney());
									ar.setStatus(1);
									ar.setAmountIn(BigDecimalUtil.sub(ar.getMoney(), fee));
									ar.setFee(fee);
									if(fee > 0) {//如果手续费大于0，则为垫付方为用户
										ar.setRechargeFeeBear((byte) 2);
									} else {
										ar.setRechargeFeeBear((byte) 1);
									}
									ar.setRemark("充值成功，入账金额：" + BigDecimalUtil.sub(ar.getMoney(), fee));
									accountRechargeDao.update(ar);
									
									// 资金记录表
									Global.setTransfer("recharge", ar);
									Global.setTransfer("user", ar.getUser());
									AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
									executer.execute(ar.getMoney(), ar.getUser());
									
									//扣除充值手续费
									if(fee > 0) {
										Global.setTransfer("money", fee);
										AbstractExecuter feeExecuter = ExecuterHelper.doExecuter("deductRechargeFeeExecuter");
										feeExecuter.execute(fee, ar.getUser());
									}
									order.setStatus(1);//成功
									orderService.updateOrder(order);//更新订单信息
								}
								bool = false;
								this.stop();
							} else if(ret.getOrderStatus() == 3){//处理失败
								logger.info("============银联充值处理失败，订单号："+ar.getTradeNo());
								if(orderStatus!=2){
									logger.info("============银联充值处理失败，进行失败充值操作");
									ar.setStatus(2);
									accountRechargeDao.update(ar);
									order.setStatus(2);//失败
									orderService.updateOrder(order);//更新订单信息
								}
								bool = false;
								this.stop();
							} else if(ret.getOrderStatus() == 1){//银联处理中
								logger.info("============银联充值处理中，订单号："+ar.getTradeNo());
								Thread.sleep(60*1000);//间隔1分钟后执行
							}
				        }
					} catch (Exception e) {
						this.stop();
					}
				}
			};
			t.start();
			boolean b = rechargeThreadsMap.containsKey(ar.getTradeNo());
			if(b){
				rechargeThreadsMap.get(ar.getTradeNo()).stop();
				rechargeThreadsMap.remove(ar.getTradeNo());
			}
			rechargeThreadsMap.put(ar.getTradeNo(),t);
		}
	
	/**
	 * 计算用户充值手续费
	 * @param money
	 * @return
	 */
	public double doRechargeFee(double money) {
		double fee = 0;
		double fee_rate = Global.getDouble("recharge_fee");
		double fee_lowest = Global.getDouble("recharge_fee_lowest");
		double fee_hight = Global.getDouble("con_recharge_fee_hight");
		fee = money * fee_rate;
		if (fee < fee_lowest)
			fee = fee_lowest;
		if (fee > fee_hight && fee_hight != 0)
			fee = fee_hight;
		return fee;
	}

	@Override
	public void doRechargeWait() {
		QueryParam param = QueryParam.getInstance().addParam("status", 5);
		List<AccountRecharge> list = accountRechargeDao.findByCriteria(param);
		if(list != null && list.size() > 0){
			for (AccountRecharge ar : list) {
				UnionPayRet ret = SignHelper.queryOrder(ar.getTradeNo());
				if(ret.getOrderStatus() == 2){//处理成功
					double fee = doRechargeFee(ar.getMoney());
					ar.setStatus(1);
					ar.setAmountIn(BigDecimalUtil.sub(ar.getMoney(), fee));
					ar.setFee(fee);
					if(fee > 0) {//如果手续费大于0，则为垫付方为用户
						ar.setRechargeFeeBear((byte) 2);
					} else {
						ar.setRechargeFeeBear((byte) 1);
					}
					ar.setRemark("充值成功，入账金额：" + BigDecimalUtil.sub(ar.getMoney(), fee));
					accountRechargeDao.update(ar);

					// 资金记录表
					Global.setTransfer("recharge", ar);
					Global.setTransfer("user", ar.getUser());
					AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
					executer.execute(ar.getMoney(), ar.getUser());
					
					//扣除充值手续费
					if(fee > 0) {
						Global.setTransfer("money", fee);
						AbstractExecuter feeExecuter = ExecuterHelper.doExecuter("deductRechargeFeeExecuter");
						feeExecuter.execute(fee, ar.getUser());
					}
				} else if (ret.getOrderStatus() == 3) {// 处理失败
					ar.setStatus(2);
					ar.setRemark("银联处理失败");
					accountRechargeDao.update(ar);
				} else if(ret.getOrderStatus() == 1) {//处理中
					ar.setStatus(5);
					accountRechargeDao.update(ar);
				}
			}
		}
	}
	
	@Override
	public PageDataList<AccountRechargeModel> accountRechargeTotalList(AccountRechargeModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{//精确条件查询
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
	            param.addParam("addTime", Operators.GTE, start);
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
	            param.addParam("addTime", Operators.LTE, end);
	        }
		}
		param.addParam("status", 1);
		param.addPage(model.getPage(), model.getRows());
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<AccountRecharge> pageDateList = accountRechargeDao.findPageList(param);
		PageDataList<AccountRechargeModel> pageDateList_ = new PageDataList<AccountRechargeModel>();
		List<AccountRechargeModel> list = new ArrayList<AccountRechargeModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountRecharge recharge = (AccountRecharge) pageDateList.getList().get(i);
				AccountRechargeModel arm = AccountRechargeModel.instance(recharge);
				VerifyLog verifyLog = verifyLogDao.findByType(recharge.getId(), "verifyAccountRecharge", 1);
				if (verifyLog != null) {
					arm.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
				}
				try{
    				arm.setUserName(recharge.getUser().getUserName());
    				arm.setRealName(recharge.getUser().getRealName());
					PayOfflinebank pay = recharge.getPayOfflinebank();
					if (pay != null) {
	    				arm.setPayOfflinebankInfo(pay.getBankNo()+":"+pay.getBank()+pay.getBranch());
					}
    				list.add(arm);
				} catch(Exception e){
                    e.printStackTrace();
                    logger.error(e);
				}
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public AccountRechargeModel sumAmount(AccountRechargeModel model) {
		return accountRechargeDao.sumAmount(model);
	}

	@Override
	public double getAccountRechargeSumByDate(String date) {
		
		return accountRechargeDao.getAccountRechargeSumByDate(date);
	}
	@Override
	public double getNewAccountRechargeSumByDate(String startTime,String endTime) {
		return accountRechargeDao.getNewRechargeTotal(startTime, endTime);
	}

	@Override
	public void accountRechargeVerifyEdit(AccountRechargeModel model,
			Operator operator) {
		AccountRecharge accountRecharge = this.find(model.getId());
		//封装审核日志
		VerifyLog verifyLog = new VerifyLog(operator, "verifyAccountRecharge", accountRecharge.getId(), 1, accountRecharge.getStatus(),model.getRemark());
		if (model.getStatus() == 1) { // 通过
			accountRecharge.setStatus(6);
			verifyLog.setResult(6);
			OperationLog operationLog = new OperationLog(accountRecharge.getUser(), operator, Constant.RECHARGE_SUCCESS);
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员初审通过");
			operationLogDao.save(operationLog);
		} else { // 不通过
			accountRecharge.setStatus(7);
			verifyLog.setResult(7);
			OperationLog operationLog = new OperationLog(accountRecharge.getUser(), operator, Constant.RECHARGE_FAIL);
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员初审不通过");
			operationLogDao.save(operationLog);
		}
		accountRechargeDao.update(accountRecharge);
		verifyLogDao.save(verifyLog);
	}

	@Override
	public PageDataList<AccountRechargeModel> accountRechargeVerifyFullList(
			AccountRechargeModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{//精确条件查询
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			SearchFilter orFilter3 = new SearchFilter("status", Operators.EQ, 6);
    		SearchFilter orFilter4 = new SearchFilter("status", Operators.EQ, 7);
    		param.addOrFilter(orFilter3,orFilter4);
			param.addParam("type", 3);
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime());
	            param.addParam("addTime", Operators.GTE, start);
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime());
	            param.addParam("addTime", Operators.LTE, end);
	        }
	        if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		param.addPage(model.getPage(), model.getRows());
		if (model.getOrder().equals("desc")) {
			param.addOrder(OrderType.DESC, model.getSort());
		} else {
			param.addOrder(OrderType.ASC, model.getSort());
		}
		PageDataList<AccountRecharge> pageDateList = accountRechargeDao.findPageList(param);
		PageDataList<AccountRechargeModel> pageDateList_ = new PageDataList<AccountRechargeModel>();
		List<AccountRechargeModel> list = new ArrayList<AccountRechargeModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountRecharge recharge = (AccountRecharge) pageDateList.getList().get(i);
				AccountRechargeModel arm = AccountRechargeModel.instance(recharge);
				VerifyLog verifyLog = verifyLogDao.findByType(recharge.getId(), "verifyAccountRecharge", 1);
				if (verifyLog != null) {
					arm.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
				}
				try{
					arm.setUserName(recharge.getUser().getUserName());
					if(recharge.getRealName()!=null&&!recharge.getRealName().equals(""))
					{
						arm.setRealName(recharge.getRealName());
					}else
					{
						arm.setRealName(recharge.getUser().getRealName());
					}
    				
					PayOfflinebank pay = recharge.getPayOfflinebank();
					if (pay != null) {
	    				arm.setPayOfflinebankInfo(pay.getBankNo()+":"+pay.getBank()+pay.getBranch());
					}
    				list.add(arm);
				} catch(Exception e){
                    e.printStackTrace();
                    logger.error(e);
				}
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}
	
	@Override
	public Map<String, Object> doLLPay(User user,int type,PayDataBean payDataBean,String addrIp) {
		String oid_paybill = payDataBean.getOid_paybill();
		int count = accountRechargeDao.getInfoByLLOrder(oid_paybill);
		if(count==0){			
			//增加该用户的充值记录
			AccountRecharge ar = new AccountRecharge();
			String money = payDataBean.getMoney_order();//充值金额
			ar.setAmountIn(Double.valueOf(money));
			ar.setMoney(Double.valueOf(money));
			ar.setUser(user);
			ar.setType(1);
			ar.setTradeNo(LLPayUtil.getCurrentDateTimeStr());
			ar.setAddIp(addrIp);
			ar.setAddTime(new Date());
			ar.setChannelKey(ConstantUtil.channelKey.llpay.getValue());
			ar.setPayment("连连充值");
			
			if(type<2){		
				ar.setStatus(1);
				ar.setRemark("充值成功，入账金额：" + Double.valueOf(money));
				ar.setOid_paybill(payDataBean.getOid_paybill());
				//保存实名信息			
				UserIdentify identify = userIdentifyService.findByUserId(user.getUserId());
				if(null!=identify){
					logger.info("==============绑卡成功后姓名："+payDataBean.getAcct_name()+",身份证号："+payDataBean.getId_no());
					if(identify.getRealNameStatus()!=1){//未实名认证				
						logger.info("==============绑卡成功实名开始");
						identify.setRealNameStatus(1);
						identify.setRealNameVerifyTime(new Date());
						userIdentifyService.update(identify);
						logger.info("=================绑卡成功实名成功");
					}
				}else{
					identify = new UserIdentify(user);
					identify.setRealNameStatus(1);
					identify.setMobilePhoneStatus(1);
					identify.setMobilePhoneVerifyTime(new Date());
					theUserIdentifyDao.save(identify);
				}
				User nb_user = theUserDao.find(user.getUserId());
				//成功后更新用户认证名称
				if(StringUtil.isBlank(nb_user.getRealName()) || StringUtil.isBlank(nb_user.getCardId())){
					logger.info("==============绑卡成功保存姓名信息开始");
					nb_user.setRealName(payDataBean.getAcct_name());
					nb_user.setCardId(payDataBean.getId_no());
					theUserDao.update(nb_user);
					logger.info("==============绑卡成功保存姓名信息成功");
				}
				
				String hidcardno = payDataBean.getCard_no();
				String bankCode = payDataBean.getBank_code();
				String noAgree = payDataBean.getNo_agree();
				AccountBank ab = theAccountBankDao.findByDDY(user.getUserId(), hidcardno,channelKey.llpay.getValue());
				if(ab!=null){
					logger.info("===========================获取到用户连连支付的银行卡信息，协议号："+ab.getBindId()+",认证状态："+ab.getStatus());
					if(StringUtil.isBlank(ab.getBindId()) || ab.getStatus()!=1){
						ab.setBindId(noAgree);
						ab.setBankCode(bankCode);
						ab.setStatus(1);	
						theAccountBankDao.update(ab);//更新绑定号
					}
					//判断该卡是否保存在通道用户表中，没有则新增
					List<ChannelBank> cbList = channelBankService.list(user.getUserId(), channelKey.llpay.getValue());
					if(cbList==null || cbList.size()==0){
						channelBankService.saveChannelBank(user, ab, channelKey.llpay.getValue());
					}
				}
				// 资金记录表
				Global.setTransfer("recharge", ar);
				Global.setTransfer("user", user);
				AbstractExecuter executer = ExecuterHelper.doExecuter("onlineRechargeSuccessExecuter");
				executer.execute(Double.parseDouble(money), user);
				logger.info("==============绑卡成功最后核实姓名："+nb_user.getRealName()+",身份证号："+nb_user.getCardId());
			}else{
				ar.setStatus(2);
				ar.setRemark("充值失败，金额：" + Double.valueOf(money));
			}
			//保存充值记录
			accountRechargeDao.update(ar);
		}
		return null;
	}

	public LianlPay setllpay(HttpServletRequest request,User user,String no_agree,String accname,String accid,String mobile,String cardno) {
		LianlPay llpay = new LianlPay();
		PartnerConfig pc = new PartnerConfig();
        llpay.setVersion(pc.getVersion());
        llpay.setOidPartner(pc.getOidPartner());
        llpay.setUserId(String.valueOf(user.getUserId()));
        llpay.setSignType(pc.getSignType());
        llpay.setBusiPartner(pc.getBusiPartner());
        llpay.setNotifyUrl(pc.getNotifyUrl());
        llpay.setUrlReturn(pc.getUrlReturn());
        llpay.setUserreqIp(LLPayUtil.getIpAddr(request));
        llpay.setUrlOrder("");
        llpay.setValidOrder("100800");// 单位分钟，可以为空，默认7天
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        if(StringUtil.isBlank(user.getAddTime())){
        	user.setAddTime(new Date());
        }
        llpay.setRiskItem(ToPay.createRiskItem(accname,String.valueOf(user.getUserId()),dataFormat.format(user.getAddTime()),accid));
        llpay.setTimestamp(LLPayUtil.getCurrentDateTimeStr());
        llpay.setBackUrl("http://www.lianlianpay.com/");
        llpay.setIdType("0");
        llpay.setIdNo(accid);
        llpay.setAccId(accid);
        llpay.setAccName(accname);
        llpay.setAddTime(dataFormat.format(user.getAddTime()));
        
        if(StringUtil.isBlank(no_agree)){
	        llpay.setCardNo(cardno);
	        
	        List<AccountBank> bankList = theAccountBankService.list(user.getUserId(),channelKey.llpay.getValue());
			if(bankList==null || bankList.size()==0){				
				//保存实名信息
				String bankJosn = LianlPayHelper.queryCardBin(cardno);
				HashMap<String, String> vmap = CommonRealize.toHashMap(bankJosn);
				String bankname = CommonRealize.subString(vmap.get("bank_name"));
				AccountBank ab = new AccountBank(user, cardno, bankname, "/data/bank/mini/"+bankname+".png");
				ab.setMobile(mobile);
				ab.setBankNo(cardno);
				ab.setChannelKey(channelKey.llpay.getValue());
				ab.setStatus(0);
				theAccountBankService.saveAccountBank(ab);
			}
				
			user.setRealName(accname);
			user.setCardId(accid);
			if(StringUtil.isBlank(user.getMobilePhone())){
				user.setMobilePhone(mobile);
			}
			logger.info("==================保存用户姓名信息，姓名："+accname+"，身份证："+accid);
			theUserDao.update(user);
			logger.info("==================保存用户姓名信息完成，姓名："+user.getRealName()+"，身份证："+user.getCardId());
			
			int length = accid.length();
		    String sexNum;
		    if (length == 15) {
		        sexNum = accid.substring(length - 1);
		    } else {
		        sexNum = accid.substring(length - 2, length - 1);
		    }
		    
		    // 获取性别 1:男,0:女
		    int sex = Integer.parseInt(sexNum) % 2;
		    UserCache userCache = user.getUserCache();
		    userCache.setSex(sex);
		    theUserCacheDao.updateUserCache(userCache);
		}else{
			llpay.setNoAgree(no_agree);
		}
		return llpay;
	}

	@Override
	public double getAccessAccontMoney(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return accountRechargeDao.getAccessAcountMoneyTotal(startTime, endTime);
	}

	@Override
	public PageDataList<AccountMoneyModel> getBorrowCollectionMoney(String startTime, String endTime,int pageNo,int rowCount) {
		// TODO Auto-generated method stub
		return accountRechargeDao.getBorrowCollectionMoney(startTime, endTime,pageNo,rowCount);
	}
	@Override
	public PageDataList<AccountMoneyModel> getBorrowCollectionMoney2(String startTime, String endTime,int pageNo,int rowCount) {
		// TODO Auto-generated method stub
		return accountRechargeDao.getBorrowCollectionMoney2(startTime, endTime,pageNo,rowCount);
	}

	@Override
	public PageDataList<AccountMoneyModel> getPpfundCollectionMoney(
			String startTime, String endTime, int pageNo, int rowCount) {
		// TODO Auto-generated method stub
		return accountRechargeDao.getPpfundCollectionMoney(startTime, endTime, pageNo, rowCount);
	}

	@Override
	public PageDataList<AccountMoneyModel> getRedPacketMoney(String startTime,
			String endTime, int pageNo, int rowCount) {
		// TODO Auto-generated method stub
		return accountRechargeDao.getRedPacketMoney(startTime, endTime, pageNo, rowCount);
	}

	@Override
	public PageDataList<AccountMoneyModel> getRecommendMoney(String startTime,
			String endTime, int pageNo, int rowCount) {
		// TODO Auto-generated method stub
		return accountRechargeDao.getRecommendMoney(startTime, endTime, pageNo, rowCount);
	}

	@Override
	public boolean offLineRecharge(AccountRechargeModel model,User user,String type) {
		boolean flag = true;		
		try {
			double offlineRechargefee = 0; // Global.getValue("online_rechargefee"),
			model.setType(3);//线下充值
			// 待完善
			double fee = BigDecimalUtil.mul(model.getMoney(), offlineRechargefee);
			// 创建订单
			OrderInfo order = LianlPayHelper.createOrder(0,type, String.valueOf(model.getMoney()));
			
			AccountRecharge recharge = model.prototype(user, fee);
			// 将订单信息保存数据
			order.setNo_order(recharge.getTradeNo());
			orderService.saveOrder(user,order);
			recharge.setPayOfflinebank(payOfflinebankDao.find(model.getPayOfflinebankId()));
			recharge.setType(3);// 线下充值
			recharge.setAmountIn(BigDecimalUtil.sub(recharge.getMoney(), fee));
			recharge.setBankNo(recharge.getBankNo());
			String realname = recharge.getRealName();
			String cardid = recharge.getCardId();
			UserIdentify userIdentify = theUserIdentifyDao.getUserIdentifyByUserId(user.getUserId());
			if(userIdentify.getRealNameStatus() == 1){
				realname = user.getRealName();
				cardid = user.getCardId();
			}
			recharge.setRealName(realname);
			recharge.setCardId(cardid);
			recharge.setRemark("姓名："+realname+",身份证号："+cardid+",银行卡号："+recharge.getBankNo());
			recharge.setPayment(type);
			recharge.setChannelKey(ConstantUtil.OFFLINE_RECHARGE);
			accountRechargeDao.save(recharge);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
