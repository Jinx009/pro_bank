package com.rongdu.p2psys.tpp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.tpp.chinapnr.model.AutoTender;
import com.rongdu.p2psys.tpp.chinapnr.model.AutoTenderPlan;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCardCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCorpRegister;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCreditAssign;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrInitiativeTender;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrLogin;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrNetSave;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrRegister;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.chinapnr.service.TppPnrPayService;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinapnrHelper;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

public class ChinapnrTPPWay extends BaseTPPWay {

	// 汇付处理全局参数---------------------------
	public static final String LOANS = "LOANS"; // 放款接口
	public static final String REPAYMENT = "REPAYMENT"; // 还款接口
	public static final String UsrFreezeBg = "UsrFreezeBg"; // 冻结
	public static final String UsrUnFreeze = "UsrUnFreeze"; // 解冻
	/** 汇付返回成功 */
	public static final String RESP_CODE_SUCC = "000";

	private User user;
	private UserModel model;
	private AccountRechargeModel accountRechargeModel;
	private String extra;
	private Borrow borrow;

	private static Logger logger = Logger.getLogger(ChinapnrTPPWay.class);

	/**
	 * 无参构造函数
	 */
	public ChinapnrTPPWay() {
	}

	public ChinapnrTPPWay(UserModel model, User user,
			AccountRechargeModel accountRechargeModel, String extra,
			Borrow borrow) {
		this.model = model;
		this.user = user;
		this.accountRechargeModel = accountRechargeModel;
		this.extra = extra;
		this.borrow = borrow;
	}

	@Override
	public ChinapnrRegister doRealname() {
		ChinapnrRegister reg = new ChinapnrRegister();
		reg.setUsrId(String.valueOf(DateUtil.dateStr7(new Date())
				+ user.getUserId()));
		reg.setUsrMp(model.getMobilePhone());
		reg.setIdType("00");
		reg.setIdNo(model.getCardId());
		reg.setUsrName(model.getRealName());
		logger.info("汇付开户真实姓名：" + model.getRealName());
		reg.setUsrEmail(model.getEmail());
		reg.setMerPriv(model.getUserId() + "");
		try {
			reg.sign();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("生成签名失败，失败原因：" + e);
		}
		return reg;
	}

	@Override
	public ChinapnrCorpRegister doCorpRegister() {
		ChinapnrCorpRegister reg = new ChinapnrCorpRegister();
		reg.setUsrId(String.valueOf(DateUtil.dateStr7(new Date())
				+ user.getUserId()));
		reg.setMerPriv(model.getUserId() + "");
		reg.setBusiCode(model.getBusinessRegistrationNumber());
		reg.setInstuCode(model.getUserCache().getZzjgCode());
		reg.setGuarType("N");
		try {
			reg.sign();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("生成签名失败，失败原因：" + e);
		}
		return reg;
	}

	@Override
	public ChinapnrNetSave doRecharge() throws Exception {
		String dcFlag = "D";
		String ordDate = DateUtil.dateStr7(new Date());
		String money = formatMoney(accountRechargeModel.getMoney());
		// TODO zjj
		// ChinapnrNetSave save=new ChinapnrNetSave(extra, ordDate, dcFlag,
		// money, user.getApiId());
		ChinapnrNetSave save = new ChinapnrNetSave(extra, ordDate, dcFlag,
				money, String.valueOf(user.getUserId()));
		save.setMerPriv(user.getUserId() + "");
		try {
			save.sign();
		} catch (Exception e) {
			logger.error("生成签名失败！" + e);
		}
		return save;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChinapnrInitiativeTender doTender(Object obj) throws Exception {
		Map<String, Object> map = (Map<String, Object>) obj;
		double userCoinMoney = NumberUtil.getDouble(map.get("userCoinMoney")
				.toString());// 果果币
		double borrowVoucherMoney = NumberUtil.getDouble(map.get(
				"borrowVoucherMoney").toString());// 抵用券
		double interestRate = NumberUtil.getDouble(map.get("interestRate")
				.toString());// 加息券
		double vipInterestRate = NumberUtil.getDouble(map
				.get("vipInterestRate").toString());// VIP用户特有加息券

		String ordId = OrderNoUtils.getSerialNumber();
		String ordDate = DateUtil.dateStr7(new Date());
		String transAmt = XmlTool.format2Str(StringUtil.toDouble(extra));
		// TODO zjj
		// String[][] args= new String[][]{{borrow.getUser().getApiId(),
		// transAmt, formatMoney(1.00)}};
		// ChinapnrInitiativeTender tender = new
		// ChinapnrInitiativeTender(user.getApiId(), transAmt, "0.10");
		String[][] args = new String[][] { {
				String.valueOf(borrow.getUser().getUserId()), transAmt,
				formatMoney(1.00) } };
		ChinapnrInitiativeTender tender = new ChinapnrInitiativeTender(
				String.valueOf(user.getUserId()), transAmt, "0.10");
		tender.setOrdId(ordId);
		tender.setOrdDate(ordDate);
		tender.fildBorrowerDetails(args);
		tender.setMerPriv(user.getUserId() + "," + borrow.getId() + ","
				+ userCoinMoney + "," + borrowVoucherMoney + "," + interestRate
				+ "," + vipInterestRate);
		try {
			tender.sign();
		} catch (Exception e) {
			logger.error("生成签名失败，失败原因：" + e);
		}
		return tender;
	}

	@Override
	public Object doCancelBorrow(Borrow borrow) throws Exception {
		return null;
	}

	@Override
	public Object doRepayment(BorrowRepayment repayment, byte repayType) {
		ConcurrentUtil.repay(repayment);
		return null;
	}

	@Override
	public ChinapnrCashOut doNewCash(AccountCash cash, User user, int cashnum,
			String province, String city, String bankCode) {
		// TODO zjj
		// ChinapnrCashOut cashOut=new ChinapnrCashOut(user.getApiId());
		ChinapnrCashOut cashOut = new ChinapnrCashOut(String.valueOf(user
				.getUserId()));
		cashOut.setVersion("20");
		cashOut.setOrdId(cash.getOrderNo());
		cashOut.setMerPriv(user.getUserId() + "");
		cashOut.setTransAmt(XmlTool.format2Str(cash.getCredited()));
		cashOut.setOpenAcctId(cash.getBankNo());
		// 用户可免费提现次数
		int is_pay_cash = Global.getInt("is_pay_cash");
		// 平台垫付用户类型
		String[] payCashUserType = Global.getValue("pay_cash_user_type").split(
				",");
		Boolean flag = false;
		if (payCashUserType.length > 0) {
			for (String string : payCashUserType) {
				if (string.equals(user.getUserCache().getUserType() + "")) {
					flag = true;
				}
			}
		}
		if (flag && is_pay_cash > cashnum) {// 如果属于被垫付的用户类型并且垫付次数大于用户本月已成功提现次数则平台垫付手术费
			String[][] arg = new String[][] { { "M", cashOut.getMerAcctId(), "" } };// M指手续费向平台收取
			cashOut.fieldReqExt(arg);
			cashOut.setServFee(""); // 如果为平台垫付手续费则同样也免收商户手续费
		} else {
			String[][] arg = new String[][] { { "U", "", "" } };// U指手续费向用户收取
			cashOut.fieldReqExt(arg);
			cashOut.setServFee(cash.getFee() == 0 ? "" : XmlTool
					.format2Str(cash.getFee())); // 商户收取费用
		}
		try {
			cashOut.sign();
		} catch (Exception e) {
			logger.info("生成签名失败！" + e);
		}
		return cashOut;
	}

	@Override
	public ChinapnrCardCashOut bindBank(AccountBank bank, User user,
			String bankType) {
		// TODO zjj
		// ChinapnrCardCashOut card=new ChinapnrCardCashOut(user.getApiId());
		ChinapnrCardCashOut card = new ChinapnrCardCashOut(String.valueOf(user
				.getUserId()));
		card.setMerPriv(user.getUserId() + "");
		try {
			card.sign();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("绑定银行卡失败");
		}
		return card;
	}

	@Override
	public Map<String, Object> bankBindRemove(AccountBank ab, User user) {
		ChinapnrService chinapnrService = (ChinapnrService) BeanUtil
				.getBean("chinapnrService");
		// TODO zjj
		// return chinapnrService.removeAccountBank(user.getApiId(),
		// ab.getBankNo());
		return chinapnrService.removeAccountBank(
				String.valueOf(user.getUserId()), ab.getBankNo());
	}

	@Override
	public Object getTppPay(Map<String, Object> map) {
		TppPnrPayService tppPnrPayService = (TppPnrPayService) BeanUtil
				.getBean("tppPnrPayService");
		return tppPnrPayService.getTppChinapnrPay(map);
	}

	@Override
	public Object getTppPayById(int id) {
		TppPnrPayService tppPnrPayService = (TppPnrPayService) BeanUtil
				.getBean("tppPnrPayService");
		return tppPnrPayService.getTppPayById(id);
	}

	@Override
	public Boolean doTppPayTask(List<Object> taskList) {
		ChinapnrService chinapnrService = (ChinapnrService) BeanUtil
				.getBean("chinapnrService");
		return chinapnrService.doApiTask(taskList);
	}

	@Override
	public ChinapnrLogin apiLogin(User user) {
		// TODO zjj
		// ChinapnrLogin usrLogin = new ChinapnrLogin(user.getApiId());
		ChinapnrLogin usrLogin = new ChinapnrLogin(String.valueOf(user
				.getUserId()));
		return usrLogin;
	}

	public static double getRepayMoney(BorrowRepayment repayment) {
		ChinapnrService chinapnrService = (ChinapnrService) BeanUtil
				.getBean("chinapnrService");
		return chinapnrService.getRealRepayMoney(repayment);
	}

	@Override
	public Object doBorrowAuto(BorrowAuto auto) {
		String ordId = OrderNoUtils.getSerialNumber();
		String ordDate = DateUtil.dateStr7(new Date());
		// TODO zjj
		// AutoTenderPlan autoTender = new AutoTenderPlan(user.getApiId());
		AutoTenderPlan autoTender = new AutoTenderPlan(String.valueOf(user
				.getUserId()));
		autoTender.setOrdId(ordId);
		autoTender.setOrdDate(ordDate);
		autoTender.setMerPriv(user.getUserId() + "," + Long.parseLong(extra));
		try {
			autoTender.sign();
		} catch (Exception e) {
			logger.info("生成签名失败，失败原因：" + e);
		}
		return autoTender;
	}

	@Override
	public ChinapnrModel autoTender(User user, String[][] args, long id,
			double validAccount) {
		String ordId = OrderNoUtils.getSerialNumber();
		String ordDate = DateUtil.dateStr7(new Date());
		String transAmt = validAccount + "";
		// TODO zjj
		// AutoTender auto = new AutoTender(user.getApiUsercustId(), transAmt,
		// "0.01");
		AutoTender auto = new AutoTender(String.valueOf(user.getUserId()),
				transAmt, "0.01");
		auto.setOrdId(ordId);
		auto.setOrdDate(ordDate);
		auto.fildBorrowerDetails(args);
		auto.setMerPriv(user.getUserId() + "," + id);
		return doSubmit(auto);
	}

	private static ChinapnrModel doSubmit(ChinapnrModel mod) {
		try {
			mod.submit();
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return mod;
	}

	@Override
	public Object creditAssign(BondModel bondModel) {
		BorrowTenderService borrowTenderService = (BorrowTenderService) BeanUtil
				.getBean("borrowTenderService");
		ChinapnrService chinapnrService = (ChinapnrService) BeanUtil
				.getBean("chinapnrService");
		BorrowTender borrowTender = borrowTenderService
				.getTenderById((bondModel.getBorrowTenderId()));
		ChinapnrCreditAssign cca = new ChinapnrCreditAssign();
		// TODO zjj
		// cca.setSellCustId(bondModel.getUser().getApiId());
		cca.setSellCustId(String.valueOf(bondModel.getUser().getUserId()));
		// 转让本金
		String creditAmt = BaseTPPWay.formatMoney(bondModel.getMoney());
		cca.setCreditAmt(creditAmt);
		double interest = chinapnrService.getHappendInterest(bondModel);
		double manegeFee = chinapnrService.getManageFee(bondModel.getMoney(),
				interest);
		cca.setCreditDealAmt(BaseTPPWay.formatMoney(BigDecimalUtil.add(
				BigDecimalUtil.mul(bondModel.getMoney(),
						1 - BigDecimalUtil.div(bondModel.getBondApr(), 100)),
				interest)));
		// TODO zjj
		// String[][] args = new String[][] { { borrowTender.getTenderBilNo(),
		// borrowTender.getTenderBilDate(),
		// bondModel.getBorrow().getUser().getApiId(), creditAmt } };
		String[][] args = new String[][] { { borrowTender.getTenderBilNo(),
				borrowTender.getTenderBilDate(),
				String.valueOf(bondModel.getBorrow().getUser().getUserId()),
				creditAmt } };
		cca.fieldBidDetails(args);
		cca.setFee(BaseTPPWay.formatMoney(manegeFee));
		String[][] arg = new String[][] { { cca.getMerAcctId(),
				BaseTPPWay.formatMoney(manegeFee) } };
		cca.fieldDivDetails(arg);
		// TODO zjj
		// cca.setBuyCustId(user.getApiId());
		cca.setBuyCustId(String.valueOf(user.getUserId()));
		String ordId = OrderNoUtils.getSerialNumber();
		String ordDate = DateUtil.dateStr7(new Date());
		cca.setOrdId(ordId);
		cca.setOrdDate(ordDate);

		// 被使用红包的ID
		Long[] redIds = bondModel.getIds();
		StringBuffer sb = new StringBuffer();
		if (redIds != null && redIds.length > 0) {
			for (long redId : redIds) {
				sb.append(redId).append("/");
			}
		}
		cca.setMerPriv(user.getUserId() + "," + bondModel.getId() + ","
				+ sb.toString());
		try {
			cca.sign();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cca;
	}

	@Override
	public Object getTppAccount(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		// TODO zjj
		// ChinapnrModel cpm=ChinapnrHelper.queryBalanceBg(user.getApiId());
		ChinapnrModel cpm = ChinapnrHelper.queryBalanceBg(String.valueOf(user
				.getUserId()));
		if (cpm == null || !cpm.success()) {
			new BussinessException("查询用户余额失败");
		}
		map.put("apiUseMoney", cpm.getAvlBal());
		map.put("apiNoUseMoney", cpm.getFrzBal());
		map.put("acctBal", cpm.getAcctBal());
		return map;
	}
}
