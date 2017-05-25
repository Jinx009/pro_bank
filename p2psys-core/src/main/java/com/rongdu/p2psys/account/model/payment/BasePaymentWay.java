package com.rongdu.p2psys.account.model.payment;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.ip.IPUtil;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 充值方式
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月7日
 */
public abstract class BasePaymentWay implements PaymentWay {

	protected User user;
	protected AccountRecharge recharge;
	protected BasePayment payment;
	private boolean result;

	/**
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public BasePayment paymentRequest(HttpServletRequest request, AccountRechargeModel model) throws Exception {
		double rechargefee = 0;
		double fee = BigDecimalUtil.mul(model.getMoney(), rechargefee);
		user = (User) request.getSession().getAttribute(Constant.SESSION_USER);
		recharge = model.prototype(user, fee);
		recharge.setAddIp(IPUtil.getRemortIP(request));
		payment = payment(request);
		payment.sign();
		request.setAttribute("payment", payment);
		return payment;
	}



	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public void callbackRequest(HttpServletRequest request) throws Exception {
		String reponseMsg = "";
		this.result = false;
		AccountService accountService = (AccountService) BeanUtil.getBean("accountService");
		payment = callback(request);
		//payment.encryptLocalReturnSign();
		String tranNo = payment.getTranNo();
		String returnText = payment.getRemoteReturnSign();
		try {
			if (StringUtil.isBlank(tranNo)) {
				throw new Exception("重复回调！");
			}
//			if (Global.TRADE_NO_SET.add(tranNo)) {
//				throw new Exception("重复回调！");
//			}
			if (payment.reponseSuccess()) {
				accountService.newRecharge(tranNo, returnText, payment.getPay());
				reponseMsg = responseSuccess();
				this.result = true;
			} else {
				accountService.failRecharge(tranNo, returnText);
				reponseMsg = responseFail();
			}
		} catch (Exception e) {
			accountService.failRecharge(tranNo, returnText);
			reponseMsg = responseFail();
		} finally {
			//Global.TRADE_NO_SET.remove(tranNo);
		}
//		HttpServletResponse response = ServletActionContext.getResponse();
//		PrintWriter p = response.getWriter();
//		p.print(reponseMsg);
//		p.flush();
//		p.close();
	}

	/**
	 * @return
	 */
	@Override
	public AccountRecharge getRecharge() {
		return recharge;
	}

	@Override
	public String responseSuccess() throws Exception {
		return null;
	}

	@Override
	public String responseFail() throws Exception {
		return null;
	}
	
	@Override
	public boolean isSuccess() throws Exception {
		return result;
	}

}
