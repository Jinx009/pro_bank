package com.rongdu.p2psys.web.tpp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowAutoService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.cooperation.util.json.JSONException;
import com.rongdu.p2psys.cooperation.util.json.JSONObject;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.ChinapnrTPPWay;
import com.rongdu.p2psys.tpp.chinapnr.model.AutoTenderPlan;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCardCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCorpRegister;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCreditAssign;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrInitiativeTender;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrNetSave;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrRegister;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.chinapnr.service.TppPnrPayService;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 汇付回调处理类，前台回调和后台回调处理业务
 */
@SuppressWarnings("rawtypes")
public class ChinapnrAction extends BaseAction {

	private static final Logger logger = Logger.getLogger(ChinapnrAction.class);

	@Resource
	private UserService userService;
	@Resource
	private TppPnrPayService tppPnrPayService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private ChinapnrService chinapnrService;
	@Resource
	private BorrowAutoService borrowAutoService;

	/****************** 开户业务start *****************/
	/**
	 * 汇付开户同步回调
	 */
	@Action(value = "/public/chinapnr/userRegisterReturn", results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html") }, interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String userRegisterReturn() {
		logger.info("汇付开户同步回调--------start------");
		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("left_url", "/member/main.html"); // 账户中心
		request.setAttribute("left_msg", MessageUtil.getMessage("I10001"));
		request.setAttribute("right_url", "/member/recharge/newRecharge.html"); // 我要充值
		request.setAttribute("right_msg", MessageUtil.getMessage("I20001"));
		request.setAttribute("r_msg", MessageUtil.getMessage("I60003"));
		dealChinapnrRegister(resultFlag);
		logger.info("汇付开户同步回调--------end------");
		return "result";
	}

	/**
	 * 汇付开户异步通知
	 */
	@Action(value = "/public/chinapnr/userRegisterNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String userRegisterNotify() {
		logger.info("汇付开户异步回调--------start------");
		ChinapnrRegister reg = dealChinapnrRegister(null);
		printTrxId(reg.getTrxId());
		logger.info("汇付开户异步回调--------end------");
		return null;
	}

	/**
	 * 企业开户回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/corpRegisterNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String corpRegisterNotify() {
		logger.info("企业开户异步回调--------start------");
		ChinapnrCorpRegister reg = this.corpRegchina();
		int ret = reg.callback(); // 进行验签操作
		logger.info("用户开户验签结果：………………" + ret);
		if (ret == 0) {
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(reg.getRespCode())) {
				long userid = NumberUtil.getLong(reg.getMerPriv());// 获取开户时用户的userid
				User user = userService.getUserById(userid);
				// user.setApiId(reg.getUsrCustId()); // 用户客户号
				// user.setApiUsercustId(reg.getUsrId()); // 用户号
				ConcurrentUtil.apiCorpRegister(user, reg);
			} else {
				logger.info("企业开户处理失败…………" + reg.getRespDesc());
				throw new BussinessException(MessageUtil.getMessage("I10009")
						+ "：" + reg.getRespDesc(), 2);
			}
		} else {
			logger.info("企业开户验签失败！");
			throw new BussinessException(MessageUtil.getMessage("I10009"), 2);
		}
		logger.info("企业开户异步回调--------end------");
		return null;
	}

	/**
	 * 企业开户回调参数
	 * 
	 * @return
	 */
	public ChinapnrCorpRegister corpRegchina() {
		ChinapnrCorpRegister reg = new ChinapnrCorpRegister();
		reg.setCmdId(paramString("CmdId"));
		reg.setRespCode(paramString("RespCode"));
		reg.setRespDesc(paramString("RespDesc"));
		reg.setMerCustId(paramString("MerCustId"));
		reg.setUsrId(paramString("UsrId"));
		reg.setUsrCustId(paramString("UsrCustId"));
		reg.setAuditStat(paramString("AuditStat"));
		reg.setAuditDesc(paramString("AuditDesc"));
		reg.setOpenBankId(paramString("OpenBankId"));
		reg.setCardId(paramString("CardId"));
		reg.setBgRetUrl(paramString("BgRetUrl"));
		reg.setTrxId(paramString("TrxId"));
		try {
			reg.setUsrName(URLDecoder.decode(
					StringUtil.isNull(paramString("UsrName")), "utf-8"));
			reg.setMerPriv(URLDecoder.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		}
		reg.setChkValue(paramString("ChkValue"));
		return reg;
	}

	/**
	 * 开户业务处理
	 * 
	 * @param type
	 *            ：1=页面回调；0=后台回调
	 * @param param
	 * @return
	 */
	private ChinapnrRegister dealChinapnrRegister(String resultFlag) {
		logger.info("进入用户开户回调:" + getRequestParams());
		ChinapnrRegister reg = this.regchina();
		int ret = reg.callback(); // 进行验签操作
		logger.info("用户开户验签结果：………………" + ret);
		if (ret == 0) {
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(reg.getRespCode())) {
				long userid = NumberUtil.getLong(reg.getMerPriv());// 获取开户时用户的userid
				User user = userService.getUserById(userid);
				user.setMobilePhone(reg.getUsrMp());
				user.setRealName(reg.getUsrName());
				user.setEmail(reg.getUsrEmail());
				user.setCardId(reg.getIdNo());
				// user.setApiId(reg.getUsrCustId()); // 用户客户号
				// user.setApiUsercustId(reg.getUsrId()); // 用户号
				ConcurrentUtil.apiUserRegister(user, resultFlag);
			} else {
				logger.info("开户汇付处理失败…………" + reg.getRespDesc());
				throw new BussinessException(MessageUtil.getMessage("I10009")
						+ "：" + reg.getRespDesc(), 2);
			}
		} else {
			logger.info("开户验签失败！");
			throw new BussinessException(MessageUtil.getMessage("I10009"), 2);
		}
		return reg;
	}

	// 用户开户回调参数校验
	public ChinapnrRegister regchina() {
		ChinapnrRegister regist = new ChinapnrRegister();
		regist.setCertId(paramString("CmdId"));
		regist.setRespCode(paramString("RespCode"));
		regist.setRespDesc(paramString("RespDesc"));
		regist.setMerCustId(paramString("MerCustId"));
		regist.setUsrId(paramString("UsrId"));
		regist.setUsrCustId(paramString("UsrCustId"));
		regist.setBgRetUrl(paramString("BgRetUrl"));
		regist.setTrxId(paramString("TrxId"));
		regist.setRetUrl(paramString("RetUrl"));
		regist.setIdNo(paramString("IdNo"));
		regist.setUsrMp(paramString("UsrMp"));
		regist.setUsrEmail(paramString("UsrEmail"));

		try {
			regist.setUsrName(URLDecoder
					.decode(paramString("UsrName"), "utf-8"));
			regist.setMerPriv(URLDecoder
					.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.info(e);
			e.printStackTrace();
		}
		regist.setChkValue(paramString("ChkValue"));
		return regist;
	}

	/****************** 开户业务end *****************/

	/****************** 充值业务start *****************/

	/**
	 * 充值页面回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/netSaveReturn", results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html") }, interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String netSaveReturn() throws Exception {
		logger.info("充值页面同步回调--------start------");
		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);

		User user = getSessionUser();
		// 投资人
		if (user != null && (user.getUserCache().getUserType() == 1)) {
			request.setAttribute("left_url", "/member/recharge/log.html"); // 成功返回地址
			request.setAttribute("right_url", "/invest/index.html"); // 成功返回地址
			request.setAttribute("left_msg", MessageUtil.getMessage("I20002"));
			request.setAttribute("right_msg", MessageUtil.getMessage("I30002"));
			// 借款人
		} else if (user != null && (user.getUserCache().getUserType() == 2)) {
			request.setAttribute("left_url", "/member/recharge/log.html"); // 成功返回地址
			request.setAttribute("right_url",
					"/member/recharge/newRecharge.html"); // 成功返回地址
			request.setAttribute("left_msg", MessageUtil.getMessage("I20002"));
			request.setAttribute("right_msg", MessageUtil.getMessage("I20001"));
			// 投资借款人
		} else {
			request.setAttribute("left_url", "/member/recharge/log.html"); // 成功返回地址
			request.setAttribute("right_url", "/member/main.html"); // 成功返回地址
			request.setAttribute("left_msg", MessageUtil.getMessage("I20002"));
			request.setAttribute("right_msg", MessageUtil.getMessage("I10001"));
		}
		request.setAttribute("r_msg", MessageUtil.getMessage("I20003"));

		dealChinapnrNetSave(resultFlag);

		logger.info("充值页面同步回调--------end------");
		return "result";
	}

	/**
	 * 充值异步回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/netSaveNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String netSaveNotify() throws Exception {
		logger.info("充值异步回调--------start------");
		ChinapnrNetSave save = dealChinapnrNetSave(null);
		printTrxId(save.getTrxId());
		logger.info("充值异步回调--------end------");
		return null;
	}

	/**
	 * 充值业务处理
	 * 
	 * @param type
	 *            ：1=页面回调；0=后台回调
	 * @param param
	 * @return
	 */
	private ChinapnrNetSave dealChinapnrNetSave(String resultFlag)
			throws Exception {
		logger.info("进入用户充值回调:" + getRequestParams());
		ChinapnrNetSave netSave = this.chpnrNatSaveCallback();
		int ret = netSave.callback(); // 验签操作
		logger.info("用户 充值验签结果：" + ret);
		if (ret == 0) { // 校验参数
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(netSave.getRespCode())) {
				logger.info("用户充值，汇付处理成功，进入本地处理……注：成功失败都在后台处理");
				logger.info("订单号：" + netSave.getOrdId() + " 金额："
						+ netSave.getTransAmt() + " 结果："
						+ netSave.getRespDesc() + " 流水号：" + netSave.getTrxId());

				RechargeModel reModel = new RechargeModel(); // 对通用javabean进行参数封装
				reModel.setOrderAmount(netSave.getTransAmt());
				reModel.setOrderId(netSave.getOrdId());
				reModel.setResultMsg(netSave.getRespDesc());
				reModel.setResult(netSave.getRespCode());
				reModel.setFeeAmt(Double.parseDouble(netSave.getFeeAmt()));
				reModel.setSerialNo(netSave.getTrxId());

				logger.info("充值回调信息：金额" + netSave.getTransAmt() + "费率："
						+ netSave.getFeeAmt() + "扣款账户："
						+ netSave.getFeeCustId() + "扣款子账户："
						+ netSave.getFeeAcctId());
				ConcurrentUtil.doRechargeBackTask(reModel, resultFlag);
			} else {
				logger.info("用户充值汇付处理失败…………" + netSave.getRespDesc());
				throw new BussinessException(MessageUtil.getMessage("I20005"),
						2);
			}
		} else {
			logger.info("充值验签失败！");
			throw new BussinessException(MessageUtil.getMessage("I20005"), 2);
		}
		return netSave;
	}

	private ChinapnrNetSave chpnrNatSaveCallback() {
		ChinapnrNetSave n = new ChinapnrNetSave();
		n.setTransAmt(paramString("TransAmt"));
		n.setRespCode(paramString("RespCode"));
		n.setRespDesc(paramString("RespDesc"));
		try {
			n.setMerPriv(URLDecoder.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		} // 此参数用户获取到用户的id，便于回调时查找到具体的用户
		n.setTrxId(paramString("TrxId")); // 钱管家交易唯一标示
		n.setChkValue(paramString("ChkValue")); // 签名信息
		n.setOrdId(paramString("OrdId")); // 订单号
		n.setMerCustId(paramString("MerCustId"));
		n.setOrdDate(paramString("OrdDate"));
		n.setRetUrl(paramString("RetUrl"));
		n.setBgRetUrl(paramString("BgRetUrl"));
		n.setCmdId(paramString("CmdId"));
		n.setUsrCustId(paramString("UsrCustId"));
		n.setGateBusiId(paramString("GateBusiId"));
		n.setGateBankId(paramString("GateBankId"));
		n.setFeeAmt(paramString("FeeAmt"));
		n.setFeeAcctId(paramString("FeeAcctId"));
		n.setFeeCustId(paramString("FeeCustId"));
		n.setOpenAcctId(paramString("OpenAccId"));
		return n;
	}

	/****************** 充值业务end *****************/

	/****************** 投标业务start *****************/

	/**
	 * 投标同步回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/pnrTenderReturn", results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html") }, interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String autoTenderReturn() throws Exception {
		logger.info("投标同步回调--------start------");
		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("left_url", "/member/invest/mine.html");
		request.setAttribute("right_url", "/member/main.html");
		request.setAttribute("left_msg", MessageUtil.getMessage("I30003"));
		request.setAttribute("right_msg", MessageUtil.getMessage("I10001"));
		request.setAttribute("m_url", "/invest/index.html");
		request.setAttribute("m_msg", MessageUtil.getMessage("I30006"));
		request.setAttribute("back_url", "/invest/index.html");
		request.setAttribute("r_msg", MessageUtil.getMessage("I30004"));

		dealChinapnrAutoTender(resultFlag);
		logger.info("投标同步回调--------end------");
		return "result";
	}

	/**
	 * 投标异步回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/pnrTenderNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String autoTenderNotify() throws Exception {
		logger.info("投标异步回调--------start------");
		ChinapnrInitiativeTender auto = dealChinapnrAutoTender(null);
		printTrxId(auto.getOrdId());
		logger.info("投标异步回调--------end------");
		return null;
	}

	/**
	 * 用户投标业务处理方法
	 * 
	 * @param type
	 * @param param
	 * @return
	 */
	private ChinapnrInitiativeTender dealChinapnrAutoTender(String resultFlag)
			throws Exception {
		logger.info("进入投标回调处理…………返回参数：" + getRequestParams());
		ChinapnrInitiativeTender pnrTender = this.tendercall();
		int ret = pnrTender.callback();
		if (ret == 0) {
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(pnrTender.getRespCode())) {
				String merPiv = pnrTender.getMerPriv(); // 获取投标传递参数，以","隔开，一般是用户id，borrowid
				int checkMer = merPiv.indexOf(",");
				if (checkMer != -1) {
					String[] args = merPiv.split(",");
					long userId = NumberUtil.getLong(args[0]); // 投标用户id
					long borrowId = NumberUtil.getLong(args[1]); // 获取所投标的id

					// 成功,处理平台投标相关业务
					BorrowModel bm = new BorrowModel();

					// 被使用红包的ID
					// if(args.length > 2){
					// String ids = args[2];
					// String[] redIds = ids.split("/");
					// long[] num = new long[redIds.length];
					// for (int idx = 0; idx < redIds.length; idx++) {
					// num[idx] = Long.parseLong(redIds[idx]);
					// }
					// bm.setIds(num);
					String userCoinMoney = args[2];
					String interestRate = args[3];
					bm.setBorrowInterestRateValue(Double
							.parseDouble(interestRate));
					String vipInterestRate = args[4];
					bm.setVipInterestRateValue(NumberUtil
							.getDouble(vipInterestRate));
					// }
					bm.setBidNo(pnrTender.getOrdId());
					bm.setTenderBilNo(pnrTender.getOrdId());
					bm.setTenderBilDate(pnrTender.getOrdDate());
					bm.setMoney(StringUtil.toDouble(pnrTender.getTransAmt()));
					bm.setUserId(userId);
					bm.setId(borrowId);

					ConcurrentUtil.doAddTender(bm, resultFlag);
				} else {
					logger.info("投标传输回调参数异常");
					throw new BussinessException(
							MessageUtil.getMessage("I30005"), 2);
				}
			} else {
				logger.info("投标汇付处理失败，原因：" + pnrTender.getRespDesc());
				throw new BussinessException(MessageUtil.getMessage("I30005"),
						2);
			}
		} else {
			logger.info("投标验签失败");
			throw new BussinessException(MessageUtil.getMessage("I30005"), 2);
		}
		return pnrTender;
	}

	// 用户投标回调参数
	public ChinapnrInitiativeTender tendercall() {
		ChinapnrInitiativeTender tender = new ChinapnrInitiativeTender();
		tender.setCmdId(paramString("CmdId"));
		tender.setRespCode(paramString("RespCode"));
		tender.setRespDesc(paramString("RespDesc"));
		tender.setMerCustId(paramString("MerCustId"));
		tender.setOrdId(paramString("OrdId"));
		tender.setOrdDate(paramString("OrdDate"));
		tender.setTransAmt(paramString("TransAmt"));
		tender.setUsrCustId(paramString("UsrCustId"));
		tender.setTrxId(paramString("TrxId"));
		tender.setRetUrl(paramString("RetUrl"));
		tender.setBgRetUrl(paramString("BgRetUrl"));
		tender.setChkValue(paramString("ChkValue"));
		try {
			tender.setMerPriv(URLDecoder
					.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.info(e);
			e.printStackTrace();
		}
		return tender;
	}

	/****************** 投标业务end *****************/

	/****************** 用户绑卡start *****************/
	/**
	 * 用户绑卡异步回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/cardCashOutNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String cardCashOutNotify() {
		logger.info("用户绑卡异步回调--------start------");
		ChinapnrCardCashOut card = dealChinapnrCardCashOut();
		logger.info("绑卡回调订单号:" + card.getTrxId());
		printTrxId(card.getTrxId());
		logger.info("用户绑卡异步回调--------end------");
		return null;
	}

	/**
	 * 绑卡业务逻辑处理
	 * 
	 * @return
	 */
	private ChinapnrCardCashOut dealChinapnrCardCashOut() {
		logger.info("进入绑卡回调处理…………返回参数： " + getRequestParams());
		ChinapnrCardCashOut cardcash = this.cardcall();
		int ret = cardcash.callback();
		logger.info("绑卡验签结果：" + ret);
		if (ret == 0) {
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(cardcash.getRespCode())) {
				logger.info("绑卡汇付处理成功，进入本地处理…………汇付id： "
						+ cardcash.getUsrCustId());
				chinapnrService.addAccountBank(cardcash);
				logger.info("绑卡添加入disruptor成功");
			} else {
				logger.info("绑卡失败，原因：" + cardcash.getRespDesc());
				throw new BussinessException(MessageUtil.getMessage("I20006"),
						2);
			}
		} else {
			logger.info("验签失败！");
			throw new BussinessException(MessageUtil.getMessage("I20006"), 2);
		}
		return cardcash;
	}

	// 用户绑卡回调参数拼接
	public ChinapnrCardCashOut cardcall() {
		ChinapnrCardCashOut ca = new ChinapnrCardCashOut();
		ca.setCmdId(paramString("CmdId"));
		ca.setRespCode(paramString("RespCode"));
		ca.setMerCustId(paramString("MerCustId"));
		ca.setOpenAcctId(paramString("OpenAcctId"));
		ca.setOpenBankId(paramString("OpenBankId"));
		ca.setUsrCustId(paramString("UsrCustId"));
		ca.setTrxId(paramString("TrxId"));
		ca.setBgRetUrl(paramString("BgRetUrl"));
		try {
			ca.setMerPriv(URLDecoder.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.info(e);
			e.printStackTrace();
		}
		ca.setChkValue(paramString("ChkValue"));
		return ca;
	}

	/****************** 用户绑卡end *****************/

	/****************** 用户取现start *****************/

	/**
	 * 取现同步回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/cashReturn", results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html") }, interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String cashReturn() throws Exception {
		logger.info("取现同步回调--------start------");
		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);

		request.setAttribute("left_url", "/member/cash/log.html");
		request.setAttribute("right_url", "/member/main.html");
		request.setAttribute("left_msg", MessageUtil.getMessage("I40001"));
		request.setAttribute("right_msg", MessageUtil.getMessage("I10001"));

		ChinapnrCashOut cash = dealChinapnrCash(resultFlag);
		if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(cash.getRespCode())) {
			request.setAttribute("r_msg", MessageUtil.getMessage("I40002"));
		} else if ("999".equals(cash.getRespCode())) {
			request.setAttribute("r_msg", MessageUtil.getMessage("I40004"));
		} else {
			request.setAttribute("r_msg", MessageUtil.getMessage("I40003"));
		}
		logger.info("取现同步回调--------end------");
		return "result";
	}

	/**
	 * 取现异步回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/cashNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String cashNotify() throws Exception {
		logger.info("取现异步回调--------start------");
		ChinapnrCashOut cash = dealChinapnrCash(null);
		printTrxId(cash.getOrdId());
		logger.info("取现异步回调--------end------");
		return null;
	}

	/**
	 * 取现业务逻辑处理
	 * 
	 * @param type
	 * @param parm
	 * @return
	 */
	private ChinapnrCashOut dealChinapnrCash(String resultFlag)
			throws Exception {
		logger.info("进入取现回调…………" + getRequestParams());
		ChinapnrCashOut cash = this.cashcall();
		int ret = cash.callback();// 验签操作
		if (ret == 0) {
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(cash.getRespCode())) {
				logger.info("取现汇付处理成功，进入本地处理…………" + cash.getOrdId() + "-------");
				CashModel cashModel = new CashModel();
				cashModel.setOrderId(cash.getOrdId());
				cashModel.setOrderAmount(cash.getTransAmt());
				cashModel.setFeeAmt(StringUtil.toDouble(cash.getFeeAmt()));
				cashModel.setUserId(StringUtil.toLong(cash.getMerPriv()));
				cashModel.setPayModel(getFeeObjFlag(URLDecoder.decode(
						cash.getReqExt(), "utf-8")));
				cashModel.setCardNo(cash.getOpenAcctId());// 保存用户实际选择的银行卡卡号
				cashModel.setResult(true);
				ConcurrentUtil.doVerifyCashBackTask(cashModel, resultFlag);
			} else {
				logger.info("取现失败，失败原因："
						+ URLDecoder.decode(cash.getRespDesc(), "utf-8"));
				throw new BussinessException(MessageUtil.getMessage("I40003"),
						2);
			}
		} else {
			logger.info("取现验签失败   " + "orderId:" + cash.getOrdId());
			throw new BussinessException(MessageUtil.getMessage("I40003"), 2);
		}
		return cash;
	}

	/**
	 * 解析reqExt，获取提现手续费收取方
	 * 
	 * @param str
	 * @return
	 */
	public String getFeeObjFlag(String str) {
		try {
			if (!StringUtil.isBlank(str)) {
				// 由于返回Json字符串带有中括号，所以需要先进行截取，将中括号去除
				String jsonStr = str.substring(str.indexOf("[") + 1,
						str.lastIndexOf("]"));
				JSONObject json = new JSONObject(jsonStr);
				String feeObjFlag = json.getString("FeeObjFlag");
				return feeObjFlag;
			} else {
				return "U";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "U";// 默认为用户自己支付
	}

	// 用户取现操作回调参数拼接
	public ChinapnrCashOut cashcall() {
		ChinapnrCashOut cash = new ChinapnrCashOut();
		cash.setCmdId(paramString("CmdId"));
		cash.setRespCode(paramString("RespCode"));
		cash.setMerCustId(paramString("MerCustId"));
		cash.setOrdId(paramString("OrdId"));
		cash.setUsrCustId(paramString("UsrCustId"));
		cash.setTransAmt(paramString("TransAmt"));
		cash.setOpenAcctId(paramString("OpenAcctId"));
		cash.setOpenBankId(paramString("OpenBankId"));
		cash.setFeeAmt(paramString("FeeAmt"));
		cash.setFeeCustId(paramString("FeeCustId"));
		cash.setFeeAcctId(paramString("FeeAcctId"));
		cash.setServFee(paramString("ServFee"));
		cash.setServFeeAcctId(paramString("ServFeeAcctId"));
		cash.setRetUrl(paramString("RetUrl"));
		cash.setBgRetUrl(paramString("BgRetUrl"));
		try {
			cash.setMerPriv(URLDecoder.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.info(e);
			e.printStackTrace();
		}
		cash.setChkValue(paramString("ChkValue"));
		cash.setRespDesc(paramString("RespDesc"));
		cash.setReqExt(paramString("RespExt"));
		return cash;
	}

	/****************** 用户取现end *****************/

	/****************** 自动投标start *****************/

	/**
	 * 自动投标页面回调
	 * 
	 * @return
	 */
	@Action(value = "/public/chinapnr/autoTenderPlanReturn")
	public String autoTenderPlanReturn() throws Exception {
		logger.info("进入自动投标任务回调处理…………返回参数：" + getRequestParams());
		AutoTenderPlan atp = this.autoTenderPlanCall();
		int ret = atp.callback();
		if (ret == 0) {
			if ("000".equals(atp.getRespCode())) {
				String merPiv = atp.getMerPriv(); // 获取投标传递参数，以","隔开
				int checkMer = merPiv.indexOf(",");
				if (checkMer != -1) {
					String[] args = merPiv.split(",");
					long userid = NumberUtil.getLong(args[0]); // 投标用户id
					int autoTenderId = NumberUtil.getInt(args[1]); // 获取所投标的id
					BorrowAuto ba = borrowAutoService
							.getBorrowAutoById(autoTenderId);
					if (ba.getUser().getUserId() == userid) {
						ba.setEnable(true);
						borrowAutoService.updateBorrowAuto(ba);
						message("自动投标设置成功！", "/member/auto/detail.html");
						return MSG;
					} else {
						throw new BussinessException("自动投标设置异常");
					}
				} else {
					logger.info("自动投标设置异常：参数异常");
					throw new BussinessException("自动投标设置异常，请联系管理员…………");
				}
			} else {
				logger.info("自动投标设置汇付处理失败，原因：" + atp.getRespDesc());
				throw new BussinessException(atp.getRespDesc());
			}
		} else {
			logger.info("设置自动投标验签失败！");
		}
		return "";
	}

	public AutoTenderPlan autoTenderPlanCall() {
		AutoTenderPlan auto = new AutoTenderPlan();
		auto.setCmdId(paramString("CmdId"));
		auto.setRespCode(paramString("RespCode"));
		auto.setRespDesc(paramString("RespDesc"));
		auto.setUsrCustId(paramString("UsrCustId"));
		auto.setTenderPlanType(paramString("TenderPlanType"));
		auto.setTransAmt(paramString("TransAmt"));
		auto.setRetUrl(paramString("RetUrl"));
		try {
			auto.setMerPriv(URLDecoder.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		}
		auto.setChkValue(paramString("ChkValue"));
		return auto;
	}

	/****************** 自动投标end *****************/

	/****************** 债权转让投标start *****************/

	@SuppressWarnings("unchecked")
	@Action(value = "/public/chinapnr/creditAssignReturn", results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html") })
	public String creditAssignReturn() throws Exception {
		logger.info("进入债权转让投标任务回调处理…………返回参数：" + getRequestParams());
		ChinapnrCreditAssign cca = this.creditAssign();
		int ret = cca.callback();
		if (ret == 0) {
			if ("000".equals(cca.getRespCode())) {
				String merPiv = cca.getMerPriv(); // 获取投标传递参数，以","隔开，一般是用户id，bondId
				int checkMer = merPiv.indexOf(",");
				if (checkMer != -1) {
					String[] args = merPiv.split(",");
					long userId = NumberUtil.getLong(args[0]); // 投标用户id
					long bondId = NumberUtil.getLong(args[1]); // 获取所投债权的id

					// 成功,处理平台投标相关业务
					BondModel bm = new BondModel();

					// 被使用红包的ID
					if (args.length > 2) {
						String ids = args[2];
						String[] redIds = ids.split("/");
						Long[] num = new Long[redIds.length];
						for (int idx = 0; idx < redIds.length; idx++) {
							num[idx] = Long.parseLong(redIds[idx]);
						}
						bm.setIds(num);
					}

					bm.setId(bondId);
					bm.setUser(userService.getUserById(userId));
					bm.setMoney(BigDecimalUtil.round(cca.getCreditAmt()));
					bm.setOrderId(cca.getOrdId());
					bm.setOrderDate(cca.getOrdDate());
					ConcurrentUtil.bondTender(bm);

					String resultFlag = System.currentTimeMillis() + ""
							+ Math.random() * 10000;
					request.setAttribute("resultFlag", resultFlag);
					request.setAttribute("left_url", "/bond/detail.html?id="
							+ bondId);
					request.setAttribute("right_url", "/member/main.html");
					request.setAttribute("left_msg",
							MessageUtil.getMessage("I50001"));
					request.setAttribute("right_msg",
							MessageUtil.getMessage("I10001"));
					request.setAttribute("r_msg",
							MessageUtil.getMessage("I50002"));
					Global.RESULT_MAP.put(resultFlag, "success");
					return "result";
				} else {
					logger.info("债权转让传输回调参数异常");
					throw new BussinessException("债权转让投标汇付处理失败", 2);
				}
			} else {
				logger.info("债权转让投标汇付处理失败，原因：" + cca.getRespDesc());
				throw new BussinessException(cca.getRespDesc(), 2);
			}
		} else {
			logger.info("债权转让投标投标验签失败！");
			throw new BussinessException("债权转让投标投标验签失败", 2);
		}
	}

	@Action(value = "/public/chinapnr/creditAssignNotify", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String creditAssignNotify() throws Exception {
		logger.info("债权转让投标异步回调--------start------");
		ChinapnrCreditAssign cca = this.creditAssign();
		int ret = cca.callback();
		if (ret == 0) {
			printTrxId(cca.getOrdId());
		}
		logger.info("债权转让投标异步回调--------end------");
		return null;
	}

	private ChinapnrCreditAssign creditAssign() {
		ChinapnrCreditAssign cca = new ChinapnrCreditAssign();
		cca.setCmdId(paramString("CmdId"));
		cca.setRespCode(paramString("RespCode"));
		cca.setRespDesc(paramString("RespDesc"));
		cca.setMerCustId(paramString("MerCustId"));
		cca.setSellCustId(paramString("SellCustId"));
		cca.setCreditAmt(paramString("CreditAmt"));
		cca.setCreditDealAmt(paramString("CreditDealAmt"));
		cca.setFee(paramString("Fee"));
		cca.setBuyCustId(paramString("BuyCustId"));
		cca.setOrdId(paramString("OrdId"));
		cca.setOrdDate(paramString("OrdDate"));
		cca.setRetUrl(paramString("RetUrl"));
		cca.setBgRetUrl(paramString("BgRetUrl"));
		cca.setChkValue(paramString("ChkValue"));
		try {
			cca.setMerPriv(URLDecoder.decode(paramString("MerPriv"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		}
		cca.setRespExt(paramString("ReqExt"));
		return cca;
	}

	/****************** 债权转让投标end *****************/

	/**
	 * 需要在页面上打印ordid的回调方法
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/public/chinapnr/chinapnrBgRet", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String chinapnrBgRet() throws IOException {
		logger.info("进入后台汇付处理打印ordid处理");
		String respCode = this.paramString("RespCode");
		String ordId = this.paramString("OrdId");
		String cmdid = this.paramString("CmdId");
		String merCustId = this.paramString("MerCustId");
		String usercustid = this.paramString("UsrCustId");
		logger.info("请求回调打印的ordid" + "merCustId:" + merCustId + "cmdid" + cmdid
				+ "respCode" + respCode + "ordId" + ordId + "" + usercustid);
		PrintWriter p = response.getWriter();
		if (respCode.equals("000") && !StringUtil.isBlank(ordId)) {
			logger.info(this.paramString("MerPriv"));
			logger.info("ordId" + ordId);
			p.print("RECV_ORD_ID_" + ordId);
		} else {
			p.print("ERROR");
		}
		p.flush();
		p.close();
		return null;
	}

	@Action(value = "/public/chinapnr/loanAndrepay", interceptorRefs = {
			@InterceptorRef("defaultStack"), @InterceptorRef("action") })
	public String loanAndrepay() {
		logger.info("放款还款进入后台处理方法");
		String respCode = this.paramString("RespCode");
		String ordId = this.paramString("OrdId");
		String cmdid = this.paramString("CmdId");
		String respDesc = paramString("RespDesc");
		logger.info("接口：" + cmdid + "订单号：" + ordId + "处理结果:" + respCode
				+ "返回信息");
		tppPnrPayService.dealChinapnrBack(ordId, respCode, respDesc);
		printTrxId(ordId);
		return null;
	}

	/**
	 * 汇付回调打印掉报文
	 * 
	 * @param trxId
	 * @throws Exception
	 */
	private void printTrxId(String order) {
		try {
			PrintWriter p = response.getWriter();
			p.print("RECV_ORD_ID_" + order);
			logger.info("汇付回调打印报文:  RECV_ORD_ID_" + order);
			p.flush();
			p.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

}
