package com.rongdu.p2psys.tpp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.domain.TppIpsConfig;
import com.rongdu.p2psys.tpp.ips.model.IpsAddBorrow;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRepaymentSigning;
import com.rongdu.p2psys.tpp.ips.model.IpsCash;
import com.rongdu.p2psys.tpp.ips.model.IpsMerchentUserInfo;
import com.rongdu.p2psys.tpp.ips.model.IpsModel;
import com.rongdu.p2psys.tpp.ips.model.IpsRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsRechargeBank;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.tpp.ips.model.IpsRegisterGuarantor;
import com.rongdu.p2psys.tpp.ips.model.IpsRepayment;
import com.rongdu.p2psys.tpp.ips.model.IpsTenderBorrow;
import com.rongdu.p2psys.tpp.ips.model.IpsTransfer;
import com.rongdu.p2psys.tpp.ips.model.IpsTransferDetail;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.tpp.ips.service.TppIpsConfigService;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * 环讯接口
 * 
 * @author lx
 * @version 2.0
 * @since 2014年8月13日
 */
public class IpsTPPWay extends BaseTPPWay {
	private long userId;
	private UserModel model;
	private User user;
	private AccountRechargeModel accountRechargeModel;
	private String extra;
	private Borrow borrow;
	private BorrowTender borrowTender;
	
	private static Logger logger = Logger.getLogger(IpsTPPWay.class);
	
	/**
	 * 无参构造函数
	 */
	public IpsTPPWay() {
	}

	public IpsTPPWay(UserModel model, User user, AccountRechargeModel accountRechargeModel, String extra, Borrow borrow) {
		this.model = model;
		this.user = user;
		this.accountRechargeModel = accountRechargeModel;
		this.extra = extra;
		this.borrow = borrow;
	}
	
	public IpsTPPWay(UserModel model, User user, AccountRechargeModel accountRechargeModel, String extra, Borrow borrow,BorrowTender borrowTender) {
		this.model = model;
		this.user = user;
		this.accountRechargeModel = accountRechargeModel;
		this.extra = extra;
		this.borrow = borrow;
		this.borrowTender = borrowTender;
	}
	
	@Override
	public IpsRegister doRealname() {
		IpsRegister ipsRegister=new IpsRegister();
		ipsRegister.setMerBillNo(OrderNoUtils.getSerialNumber());
		ipsRegister.setIdentType("1");
		ipsRegister.setIdentNo(model.getCardId());
		ipsRegister.setRealName(model.getRealName());
		ipsRegister.setMobileNo(model.getMobilePhone());
		ipsRegister.setEmail(model.getEmail());
		ipsRegister.setMemo1(model.getUserId()+"");
		ipsRegister.setSmDate(DateUtil.dateStr7(new Date()));
		ipsRegister.setSubmitUrl(ipsRegister.getSubmitUrl()+"/CreditWeb/CreateNewIpsAcct.aspx");
		ipsRegister.setWebUrl(URL_WEB+"/public/ips/ipsRegisterReturn.html");
		ipsRegister.setS2SUrl(URL_WEBS2S+"/public/ips/ipsRegisterNotify.html");
		ipsRegister.createSign();
		return ipsRegister;
	}

	@Override
	public IpsRecharge doRecharge() throws Exception {
		IpsRecharge ipsRecharge = new IpsRecharge();
		ipsRecharge.setMerBillNo(extra);
		ipsRecharge.setSubmitUrl(ipsRecharge.getSubmitUrl()+"/CreditWeb/doDpTrade.aspx");
		ipsRecharge.setIdentNo(user.getCardId());
		// TODO zjj
		// ipsRecharge.setIpsAcctNo(user.getApiId());
		ipsRecharge.setRealName(user.getRealName());
		ipsRecharge.setTrdDate(DateUtil.dateStr7(new Date()));
		ipsRecharge.setTrdAmt(XmlTool.format2Str(accountRechargeModel.getMoney()));
		ipsRecharge.setChannelType(accountRechargeModel.getChannelType());
		ipsRecharge.setTrdBnkCode(accountRechargeModel.getBankCode());
		ipsRecharge.setMerFee("0.00");
		//是否平台支付手续费（1：平台支付2：用户支付）
		int rechargeWeb = Global.getInt("recharge_web");  //获取平台是否自己垫付充值手续费
		//投资人平台垫付
		if (rechargeWeb == 1 && user.getUserCache().getUserNature()==1) {
			//平台垫付充值手续费,不收取任何手续费
			ipsRecharge.setIpsFeeType("1");
		} else {
			//从用户自己账户扣款
			ipsRecharge.setIpsFeeType("2");
		}
		ipsRecharge.setWebUrl(URL_WEB+"/public/ips/ipsRechgargeReturn.html");
		ipsRecharge.setS2SUrl(URL_WEBS2S+"/public/ips/ipsRechgargeNotify.html");
		ipsRecharge.setMemo1(accountRechargeModel.getChannelType());
		ipsRecharge.createSign();
		return ipsRecharge;
	}

	@Override
	public IpsTenderBorrow doTender(Object obj) throws Exception {
		IpsTenderBorrow ipsTender = new IpsTenderBorrow();
		ipsTender.setMerBillNo(OrderNoUtils.getSerialNumber());
		ipsTender.setMerDate(DateUtil.dateStr7(new Date()));
		ipsTender.setSubmitUrl(ipsTender.getSubmitUrl() + "/CreditWeb/registerCreditor.aspx");
		ipsTender.setAcctType("1");
		ipsTender.setRealName(user.getRealName());
		ipsTender.setIdentNo(user.getCardId());
		// TODO zjj
		// ipsTender.setAccount(user.getApiId());
		ipsTender.setBidNo(borrow.getBidNo());
		ipsTender.setContractNo("H" + ipsTender.getMerBillNo());
		ipsTender.setWebUrl(URL_WEB + "/public/ips/ipsTenderReturn.html");
		ipsTender.setS2SUrl(URL_WEBS2S + "/public/ips/ipsTenderNotify.html");
		ipsTender.setRegType("1");
		String tenderMoney = XmlTool.format2Str(StringUtil.toDouble(extra));
		ipsTender.setAuthAmt(tenderMoney);
		ipsTender.setTrdAmt(tenderMoney);
		ipsTender.setFee("0.00");
		ipsTender.setUse(borrow.getBorrowUse());
		ipsTender.setMemo1(user.getUserId() + "");
		ipsTender.setMemo2(borrow.getId() + "");
		ipsTender.setMemo3(accountRechargeModel.getAddIp());
		ipsTender.createSign();
		return ipsTender;
	}

	public static List<IpsRechargeBank> queryRechargeBank() throws Exception {
		List<IpsRechargeBank> list = new ArrayList<IpsRechargeBank>();
		IpsModel model = new IpsModel();
		model.setSubmitUrl(model.getSubmitUrl()+"/CreditWS/Service.asmx");
		model.createSign();
		String xml = model.doNotifySubmit(model.getSubmitUrl(), "GetBankList");
		String bankListStr = XmlTool.getXmlNodeValue(xml, "pBankList");
		String[] bankArray = bankListStr.split("#");
		for (int i = 0; i < bankArray.length; i++) {
			IpsRechargeBank irb=new IpsRechargeBank();
			String[] bankMsg = bankArray[i].split("\\|");
			irb.setBankName(bankMsg[0]);
			irb.setBankCode(bankMsg[2]);
			list.add(irb);
		}
		return list;
	}
	
	
	/**
	 * 查询第三方支付帐户信息
	 * @return
	 * @throws Exception
	 */
	@Override
	public  IpsMerchentUserInfo queryMerUserInfo() throws Exception {
	    IpsMerchentUserInfo merUserInfo = new IpsMerchentUserInfo();
	    // TODO zjj
	    // merUserInfo.setArgIpsAccount(user.getApiId());
	    merUserInfo.setSubmitUrl(merUserInfo.getSubmitUrl()+"/CreditWSQuery/Service.asmx");
	    merUserInfo.createSign();
	    logger.info("提交查询第三方支付帐户信息，URL："+merUserInfo.getSubmitUrl()+",method:QueryMerUserInfo");
	    // logger.info("参数：setArgIpsAccount："+user.getApiId());
        String xml = merUserInfo.doNotifySubmit(merUserInfo.getSubmitUrl(), "QueryMerUserInfo");    
        logger.info("返回数据："+xml);
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        merUserInfo.setpMerCode(tool.getNodeValue("pMerCode"));
        merUserInfo.setpEmail(tool.getNodeValue("pEmail"));
        //账户开通状态,01 未开户；02 开户成功未激活账户；03 开户失败；04已激活账户；05 已删除
        merUserInfo.setpStatus(tool.getNodeValue("pStatus"));
        //身份证验证状态:01 未验证；02 验证通过，03 验证不通过
        merUserInfo.setpUCardStatus(tool.getNodeValue("pUCardStatus"));      
        merUserInfo.setpBCardStatus(tool.getNodeValue("pBCardStatus"));
        merUserInfo.setpSignStatus(tool.getNodeValue("pSignStatus"));   
        merUserInfo.setpBankName(tool.getNodeValue("pBankName"));
        merUserInfo.setpBankCard(tool.getNodeValue("pBankCard"));
      
        return merUserInfo;
    }
	
    @Override
    public IpsAddBorrow doBorrow() throws Exception {
        return doCancelBorrow(borrow);
    }
	
	@Override
	public IpsAddBorrow doCancelBorrow(Borrow borrowModel) throws Exception {
		IpsAddBorrow ipsBorrow = new IpsAddBorrow();
		String ord = OrderNoUtils.getSerialNumber();
		ipsBorrow.setIdentNo(user.getCardId());
		ipsBorrow.setRealName(user.getRealName());
		// TODO zjj
		// ipsBorrow.setIpsAcctNo(user.getApiId());
		ipsBorrow.setGuaranteesAmt(XmlTool.format2Str(0));
		ipsBorrow.setTrdLendRate(XmlTool.format2Str(borrow.getApr()));
		if (borrow.getBorrowTimeType() == 0) {
			ipsBorrow.setTrdCycleType("3");
		} else {
			ipsBorrow.setTrdCycleType("1");
		}
		ipsBorrow.setTrdCycleValue(borrow.getTimeLimit() + "");
		ipsBorrow.setLendPurpose(borrow.getBorrowUse());
		//还款方式 1按月分期还款; 2一次性还款;3每月还息到期还本，系统中的还款方式要和接口中对应，最好是一一对应便于查看
		if(borrow.getStyle()==1){
			ipsBorrow.setRepayMode("1");
		}else if(borrow.getStyle()== 3){
			ipsBorrow.setRepayMode("2");
		}else{
			ipsBorrow.setRepayMode("99");
		}
		double lendFee =0;
		if(borrow.getOldAccount()>0){
		    ipsBorrow.setLendAmt(XmlTool.format2Str(borrow.getOldAccount()));
		    lendFee = BigDecimalUtil.mul(borrow.getOldAccount(), borrow.getBorrowManageRate()/100);
		}else{
		    ipsBorrow.setLendAmt(XmlTool.format2Str(borrow.getAccount()));
		    lendFee = BigDecimalUtil.mul(borrow.getAccount(), borrow.getBorrowManageRate()/100);
		}
		if(borrow.getStatus() != -1){
		    ipsBorrow.setRegDate(DateUtil.dateStr7(new Date()));
		    ipsBorrow.setMerBillNo(ord);
			ipsBorrow.setOperationType("1");
			ipsBorrow.setBidNo("B" + ord);
		}else{
		    ipsBorrow.setRegDate(DateUtil.dateStr7(borrow.getAddTime()));
		    ipsBorrow.setMerBillNo(borrow.getBidNo().substring(1));
		    ipsBorrow.setBidNo(borrow.getBidNo());
			ipsBorrow.setOperationType("2");
		}
		ipsBorrow.setLendFee(XmlTool.format2Str(lendFee));
		ipsBorrow.setAcctType("1");
		ipsBorrow.setMemo1(borrow.getId()+"");
		ipsBorrow.setSubmitUrl(ipsBorrow.getSubmitUrl()+"/CreditWeb/registerSubject.aspx");
		ipsBorrow.setWebUrl(URL_ADMIN + "/public/ips/ipsAddBorrowReturn.html");
		ipsBorrow.setS2SUrl(URL_ADMINS2S + "/public/ips/ipsAddBorrowNotify.html");
		ipsBorrow.createSign();
		return ipsBorrow;
	}
	
    @Override
    public IpsRegisterGuarantor registerGuarantor(Borrow borrow) {
        IpsRegisterGuarantor irg =new IpsRegisterGuarantor();
        String ord = OrderNoUtils.getSerialNumber();
        irg.setMerBillNo(ord);
        irg.setMerDate(DateUtil.dateStr7(borrow.getAddTime()));
        irg.setBidNo(borrow.getBidNo());
        irg.setAmount(XmlTool.format2Str(borrow.getGuarantorMoney()));
        irg.setMarginAmt("0.00");
        irg.setProFitAmt(XmlTool.format2Str(borrow.getGuaranteeFee()));
        irg.setAcctType("0");
        // TODO zjj
        // irg.setFromIdentNo(borrow.getVouchFirm().getApiId());
        // irg.setAccountName(borrow.getVouchFirm().getApiUsercustId());
        // irg.setAccount(borrow.getVouchFirm().getApiId());
        irg.setSubmitUrl(irg.getSubmitUrl()+"/CreditWeb/registerGuarantor.aspx");
        irg.setWebUrl(URL_WEB + "/public/ips/ipsGuarantorReturn.html");
        irg.setS2SUrl(URL_WEBS2S + "/public/ips/ipsGuarantorNotify.html");
        irg.createSign();
        return irg;
    }
	
	@Override
    public Object doRepayment(BorrowRepayment repay, byte repayType) {
	    IpsService ipsService = (IpsService) BeanUtil.getBean("ipsService");
	    IpsRepayment ipsR = ipsService.repaySkip(repay, repayType);
	    return ipsR;
    }
	
	@Override
    public Object doAutoRepaymentSigning() {
        UserDao userDao = (UserDao) BeanUtil.getBean("userDao");
        User user = userDao.find(this.user.getUserId());
	    IpsAutoRepaymentSigning auto = new IpsAutoRepaymentSigning();
	    auto.setMerBillNo("A"+OrderNoUtils.getSerialNumber());
	    auto.setSigningDate(DateUtil.dateStr7(new Date()));
	    auto.setIdentType("1");
	    auto.setIdentNo(user.getCardId());
	    auto.setRealName(user.getRealName());
	    // TODO zjj
	    // auto.setIpsAcctNo(user.getApiId());
	    auto.setValidType("N");
	    auto.setValidDate("0");
	    auto.setWebUrl(URL_WEB + "/public/ips/ipsDoAutoRepaySigningReturn.html");
	    auto.setS2SUrl(URL_WEBS2S + "/public/ips/ipsDoAutoRepaySigningNotify.html");
	    auto.setSubmitUrl(auto.getSubmitUrl() + "/CreditWeb/RepaymentSigning.aspx");
	    auto.createSign();
        return auto;
    }
	/**
	 * 转账
	 * @param bidNo 标号
	 * @param transferType 转账类型
	 * @param oriMerBillNo 原商户订单号
	 * @param trdAmt 转账金额
	 * @param fromApiId 转出人
	 * @param toApiId 转入人
	 * @return 转账结果
	 */
	public static IpsTransfer transfer(String bidNo, String transferType, 
			String oriMerBillNo, String trdAmt, String fromApiId, String toApiId,String manageFee) {
		IpsTransfer ipsTransfer = new  IpsTransfer();
		ipsTransfer.setMerBillNo(OrderNoUtils.getSerialNumber());
		ipsTransfer.setBidNo(bidNo);
		ipsTransfer.setDate(DateUtil.dateStr7(new Date()));
		ipsTransfer.setTransferType(transferType);
		ipsTransfer.setTransferMode("1");
		ipsTransfer.setS2SUrl(Global.getValue("weburl") + "/public/ips/ipsTransferNotify.html");
		StringBuffer details = new StringBuffer();
		IpsTransferDetail row=new IpsTransferDetail();
		row.setfAcctType("1");
		row.setfIpsAcctNo(fromApiId);
		row.settIpsAcctNo(toApiId);
		row.setfTrdFee("0.00");
		row.settTrdFee(manageFee);
		row.setOriMerBillNo(oriMerBillNo);
		row.settAcctType("1");
		row.setTrdAmt(trdAmt);
        details.append(row.createSign(row.getParamNames()));
		ipsTransfer.setDetails(details.toString());
		ipsTransfer.createSign();
		try {
			String res = ipsTransfer.doNotifySubmit(row.WS_URL, "Transfer");
			String pErrCode = XmlTool.getXmlNodeValue(res, "pErrCode");
			ipsTransfer.setErrCode(pErrCode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ipsTransfer;
	}
	
	@Override
    public Object doAutoRecharge(User user, String ipsFeeType, String acctType, double trdAmt, double merFee) {
	    AccountRecharge ar = new AccountRecharge(user, trdAmt, "online_recharge", AccountRecharge.TYPE_AUTO_RECHARGE, null);
	    AccountRechargeDao accountRechargeDao = (AccountRechargeDao) BeanUtil.getBean("accountRechargeDao");
	    accountRechargeDao.save(ar);
	    IpsAutoRecharge ips = new IpsAutoRecharge(user, ipsFeeType, acctType, trdAmt, merFee);
	    ips.setMerBillNo(ar.getTradeNo());
	    ips.createSign();
	    try {
            String res = ips.doNotifySubmit(ips.WS_URL, "coDp");
            String pErrCode = XmlTool.getXmlNodeValue(res, "pErrCode");
            ips.setErrCode(pErrCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	    return ips;
    }

    @Override
	public IpsCash doNewCash (AccountCash cash, User user, int cashnum,String province, String city, String bankCode) {
		IpsCash ipsCash = new IpsCash();
		ipsCash.setMerBillNo(cash.getOrderNo());
		ipsCash.setAcctType("1");
		ipsCash.setOutType("1");
		ipsCash.setBidNo("");
		ipsCash.setContractNo("");
		ipsCash.setDwTo("");
		ipsCash.setIdentNo(user.getCardId());
		ipsCash.setRealName(user.getRealName());
		// TODO zjj
		// ipsCash.setIpsAcctNo(user.getApiId());
		ipsCash.setDwDate(DateUtil.dateStr7(new Date()));
		ipsCash.setTrdAmt(XmlTool.format2Str(cash.getCredited()));
		//ipsCash.setMerFee(XmlTool.format2Str(cash.getFee()));
		ipsCash.setMerFee("0.00");
		//正常取现 每笔5元
		//1：平台支付
	    //2：提现方支付
		ipsCash.setIpsFeeType(cash.getCashFeeBear()+"");
		ipsCash.setWebUrl(URL_WEB + "/public/ips/ipsCashReturn.html");
		ipsCash.setS2SUrl(URL_WEBS2S + "/public/ips/ipsCashNotify.html");
		ipsCash.setSubmitUrl(ipsCash.getSubmitUrl() + "/CreditWeb/DoDwTrade.aspx");
		ipsCash.setMemo1(cash.getId()+"");
		ipsCash.createSign();
		return ipsCash;
	}
    
    @Override
    public void addUserTppConfig(long userId) {
        TppIpsConfigService tppIpsConfigService = (TppIpsConfigService) BeanUtil.getBean("tppIpsConfigService");
        TppIpsConfig conf = new TppIpsConfig();
        conf.setUserId(userId);
        conf.setAddTime(new Date());
        tppIpsConfigService.addTppIpsConfig(conf);
    }

    public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public UserModel getModel() {
		return model;
	}

	public void setModel(UserModel model) {
		this.model = model;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getExtra() {
		return extra;
	}
	
	public void setExtra(String extra) {
		this.extra = extra;
	}

	public AccountRechargeModel getAccountRechargeModel() {
		return accountRechargeModel;
	}

	public void setAccountRechargeModel(AccountRechargeModel accountRechargeModel) {
		this.accountRechargeModel = accountRechargeModel;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public BorrowTender getBorrowTender() {
		return borrowTender;
	}

	public void setBorrowTender(BorrowTender borrowTender) {
		this.borrowTender = borrowTender;
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
