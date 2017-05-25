package com.rongdu.p2psys.tpp.yjf.web;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.util.YjfUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.tpp.yjf.model.ForwardConIdentify;
import com.rongdu.p2psys.tpp.yjf.model.Recharge;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.tpp.yjf.model.YzzNewWithraw;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

@Namespace("/public")
public class YjfAction extends BaseAction {

	private static final Logger logger = Logger.getLogger(YjfAction.class);

	@Autowired
	private UserService userservice;
	@Autowired
	private UserIdentifyService userIdentifyService;

	/**
	 * 易极付实名认证页面回调(新四合一实名认证接口)
	 */
	@Action("forwardRegisterReturn")
	public String forwardRegisterReturn() throws Exception {
		User user = getSessionUser();
		UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
		if (userIdentify.getRealNameStatus() == 0) {
			userIdentifyService.modifyRealnameStatus(user.getUserId(), 2, 0);
			// 实名认证未通过的场合
		} 
		message("实名认证申请成功，请到账户中心查看","/member/main.html");
		return MSG;
	}

	/**
	 * 易极付实名认证异步回调(新四合一实名认证接口)
	 */
	@Action("forwardRegisterNotify")
	public String forwardRegisterNotify() throws Exception {
		logger.info(getRequestParams() + " --- 易极付客服 实名认证异步回调");
		String userId = paramString("userId");
		String isSuccess = paramString("isSuccess");
		String orderNo = paramString("orderNo");
		String notifyTime = paramString("notifyTime");
		String signType = paramString("signType");
		String sign = paramString("sign");
		String realName = paramString("realName");
		String certNo = paramString("certNo");
		String mobile = paramString("mobile");
		String resultCode = paramString("resultCode");
		String resultMessage = paramString("resultMessage");
		ForwardConIdentify fci = new ForwardConIdentify();
		fci.setUserId(userId);
		fci.setIsSuccess(isSuccess);
		fci.setOrderNo(orderNo);
		fci.setNotifyTime(notifyTime);
		fci.setSignType(signType);
		fci.setSign(sign);
		fci.setRealName(realName);
		fci.setCertNo(certNo);
		fci.setMobile(mobile);
		fci.setResultCode(resultCode);
		fci.setResultMessage(resultMessage);

		if (StringUtil.isBlank(userId) || StringUtil.isBlank(isSuccess) || StringUtil.isBlank(orderNo)
				|| StringUtil.isBlank(notifyTime) || StringUtil.isBlank(signType) || StringUtil.isBlank(sign)
				|| StringUtil.isBlank(realName) || StringUtil.isBlank(certNo) || StringUtil.isBlank(mobile)
				|| StringUtil.isBlank(resultCode) || StringUtil.isBlank(resultMessage)) {
			throw new BussinessException("参数错误...");
		}
		checkSignByYjf();
		userIdentifyService.updateRealNameStatusByCallBack(fci);
		PrintWriter p = response.getWriter(); // 返回通知
		p.print("success");
		return null;
	}

	/**
	 * 提现异步回调
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("cashNotify")
	public String cashNotify() throws Exception {
		logger.info(getRequestParams() + ".....提现异步回调进来");
		String notifyTime = paramString("notifyTime");
		String sign = paramString("sign");
		String payType = paramString("payType");
		String amount = paramString("amount");
		String amountIn = paramString("amountIn");
		String outBizNo = paramString("outBizNo");
		String success = paramString("success");
		String resultMessage = paramString("resultMessage");
		String payNo = paramString("payNo");
		String resultCode = paramString("resultCode");

		if (StringUtil.isBlank(notifyTime) || StringUtil.isBlank(sign) 
				|| StringUtil.isBlank(payType)
				|| StringUtil.isBlank(amount) || StringUtil.isBlank(outBizNo) || StringUtil.isBlank(success)
				|| StringUtil.isBlank(resultMessage) || StringUtil.isBlank(payNo)) {
			throw new BussinessException("参数错误...");
		}
		YzzNewWithraw awd = new YzzNewWithraw();
		awd.setSign(sign);
		awd.setPayMode(payType);
		awd.setMoney(amount);
		awd.setOutBizNo(outBizNo);
		awd.setSuccess(success);
		awd.setResultMessage(resultMessage);
		awd.setOrderNo(payNo);
		awd.setResultCode(resultCode);
		awd.setAmountIn(amountIn);
		checkSignByYjf();
		logger.info("提现验证签名成功... orderNo:" + outBizNo);
		CashModel cashModel = new CashModel();
		cashModel.setOrderId(awd.getOutBizNo());
		cashModel.setOrderAmount(awd.getMoney());
		// 提现手续费
		cashModel.setFeeAmt(StringUtil.toDouble(awd.getMoney()) - StringUtil.toDouble(awd.getAmountIn()));
		// 包含"WITHDRAW_SUCCESS" 即为成功。
		cashModel.setResult(awd.getResultCode().contains("WITHDRAW_SUCCESS"));
		ConcurrentUtil.doVerifyCashBackTask(cashModel,"");
		PrintWriter p = response.getWriter(); // 返回通知
		p.print("success");
		return null;
	}
	/**
	 * 充值异步回调
	 * @throws Exception if has error
	 */
	@Action("rechargeNotify")
	public void rechargeNotify() throws Exception {

		Recharge re = new Recharge();
		re.setSign(paramString("sign"));
		re.setSignType(paramString("signType"));
		re.setDepositAmount(paramString("depositAmount"));
		re.setNotifyTime(paramString("notifyTime"));
		re.setOrderNo(paramString("orderNo"));
		re.setNotifyType(paramString("notifyType"));
		re.setDepositId(paramString("depositId"));
		re.setIsSuccess(paramString("isSuccess"));
		String amountIn = paramString("amountIn");
		logger.info("充值回调进来..." + getRequestParams());
		checkSignByYjf();
		String orderNo = re.getOrderNo(); // 订单号。
		logger.info("订单号：" + re.getOrderNo() + " 金额：" + re.getDepositAmount() + " 结果：" + re.getIsSuccess() + " 流水号："
				+ re.getDepositId());
		try {
			RechargeModel reModel = new RechargeModel(); // 对通用javabean进行参数封装
			reModel.setOrderAmount(re.getDepositAmount());
			reModel.setOrderId(orderNo);
			reModel.setResultMsg(re.getIsSuccess());
			reModel.setResult(re.getIsSuccess());
			reModel.setSerialNo(re.getDepositId());
			reModel.setAmountIn(amountIn);
			ConcurrentUtil.doRechargeBackTask(reModel,"");
		} catch (Exception e) {
			logger.error(e);
			logger.error("充值失败：" + orderNo + "   " + e);
		}
		PrintWriter p = response.getWriter(); // 返回通知
		p.print("success");
	}
	/**
	 * 验证易极付签名
	 */
	public void checkSignByYjf() {
		logger.info("进入易极付验证签名");
		logger.info("返回参数:" + getRequestParams());
		YjfUtil.check(getRequestParamsToMap());
		logger.info("进入易极付验证签名成功");
	}

	public String getRequestParams() {
		String params = "";
		try {
			Enumeration e = (Enumeration) request.getParameterNames();
			while (e.hasMoreElements()) {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				params += parName + "=" + value + "&";
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return params;
	}

	public Map<String, String> getRequestParamsToMap() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Enumeration e = (Enumeration) request.getParameterNames();
			while (e.hasMoreElements()) {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				map.put(parName, value);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
}
