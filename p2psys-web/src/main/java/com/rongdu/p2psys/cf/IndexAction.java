package com.rongdu.p2psys.cf;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSON;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.RetBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * @author Jinx
 *
 */
@SuppressWarnings("rawtypes")
public class IndexAction extends BaseAction {

	@Resource
	private IOrderService orderService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private AccountCashService accountCashService;

	private User user;
	
	private Map<String,Object> data;

	/**
	 * 上传图片
	 * @throws IOException
	 */
	@Action(value = "/cf/upload")
	public void uploadImg() throws IOException{
		data = new HashMap<String,Object>();
		String path = imgUpload();
		printWebJson(getStringOfJpaObj(path));
	}
	
	/**
	 * 发起梦想
	 * @return
	 */
	@Action(value = "/dream",results = {@Result(name = "dream",type = "ftl",location = "/nb/cf/dream.html")})
	public String dream(){
		return "dream";
	}
	
	/**
	 * 发起梦想--审核
	 * @return
	 */
	@Action(value = "/dreamApply",results = {@Result(name = "dreamApply",type = "ftl",location = "/nb/cf/dreamApply.html")})
	public String dreamApply(){
		return "dreamApply";
	}
	
	/**
	 * index page
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@Action(value = "/index", results = { @Result(name = "index", type = "ftl", location = "/nb/cf/index.html"),@Result(name = "active", type = "ftl", location = "/nb/active/cf-index.htm") })
	public String indexPage() throws ParseException {
		User user = getNBSessionUser();
		if(null!=user){
			request.setAttribute("session_username",getSessionString(ConstantUtil.HIDE_SESSION_USERNAME));
		}else{
			request.setAttribute("session_username","");
		}
		Date now = new Date();
		String myString = "2016-03-22 16:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(myString);
		if(d.after(now)){
			return "active";
		}
		return "index";
	}

	/**
	 * index page
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/index", results = { @Result(name = "index", type = "ftl", location = "/nb/cf/index.html") })
	public String indexUserPage() {
		User user = getNBSessionUser();
		if (null != user) {
			request.setAttribute("session_username",
					user.getUserName());
		} else {
			request.setAttribute("session_username", "");
		}
		return "index";
	}

	/**
	 * help page
	 * 
	 * @return
	 */
	@Action(value = "/cf/help", results = { @Result(name = "help", type = "ftl", location = "/nb/cf/help.html") })
	public String helpPage() {
		return "help";
	}

	/**
	 * about us page
	 * 
	 * @return
	 */
	@Action(value = "/cf/aboutUs", results = { @Result(name = "aboutUs", type = "ftl", location = "/nb/cf/aboutUs.html") })
	public String aboutUsPage() {
		return "aboutUs";
	}

	/**
	 * login page
	 * 
	 * @return
	 */
	@Action(value = "/cf/login", results = { @Result(name = "login", type = "ftl", location = "/nb/cf/login.html") })
	public String loginPage() {
		request.setAttribute("redirectUrl", paramString("redirectUrl"));

		return "login";
	}

	/**
	 * register page
	 * 
	 * @return
	 */
	@Action(value = "/cf/register", results = { @Result(name = "register", type = "ftl", location = "/nb/cf/register.html") })
	public String registerPage() {
		request.setAttribute("redirectUrl", paramString("redirectUrl"));

		return "register";
	}
	
	/**
	 * forget page
	 * 
	 * @return
	 */
	@Action(value = "/cf/forget", results = { @Result(name = "forget", type = "ftl", location = "/nb/cf/forget.html") })
	public String forgetPwdPage() {
		request.setAttribute("redirectUrl", paramString("redirectUrl"));

		return "forget";
	}

	/**
	 * register page
	 * 
	 * @return
	 */
	@Action(value = "/cf/user/main", results = { @Result(name = "main", type = "ftl", location = "/nb/cf/user/main.html") })
	public String mainPage() {
		return "main";
	}

	/**
	 * 充值支付结果异步返回
	 * 
	 * @throws IOException
	 */
	@Action("/cf/notify")
	public void notifyIndex() throws IOException {
		ServletInputStream sis = request.getInputStream();
		String str = CommonRealize.inputStream2String(sis);
		System.out
				.println("===================================================回调结果====="
						+ str);

		response.setCharacterEncoding("UTF-8");
		System.out.println("进入支付异步通知数据接收处理");
		RetBean retBean = new RetBean();
		String reqStr = LLPayUtil.readReqStr(request);

		if (null == reqStr || "".equals(reqStr)) {
			reqStr = str;
		}

		if (LLPayUtil.isnull(reqStr)) {
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			response.getWriter().write(JSON.toJSONString(retBean));
			response.getWriter().flush();
			return;
		}
		System.out.println("接收支付异步通知数据：【" + reqStr + "】");
		try {
			PartnerConfig pc = new PartnerConfig();
			if (!LLPayUtil.checkSign(reqStr, pc.getYtpubKey(), pc.getMd5Key())) {
				retBean.setRet_code("9999");
				retBean.setRet_msg("交易失败");
				response.getWriter().write(JSON.toJSONString(retBean));
				response.getWriter().flush();
				System.out.println("支付异步通知验签失败");
				return;
			}
		} catch (Exception e) {
			System.out.println("异步通知报文解析异常：" + e);
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			response.getWriter().write(JSON.toJSONString(retBean));
			response.getWriter().flush();
			return;
		}
		retBean.setRet_code("0000");
		retBean.setRet_msg("交易成功");
		response.getWriter().write(JSON.toJSONString(retBean));
		response.getWriter().flush();
		System.out.println("支付异步通知数据接收处理成功");
		// 解析异步通知对象
		PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		showPayResult(payDataBean);
	}

	/**
	 * 支付成功异步实现方法
	 * 
	 * @param payDataBean
	 */
	private void showPayResult(PayDataBean payDataBean) {
		System.out.println("==================回调的订单号： "
				+ payDataBean.getNo_order());
		OrderInfo oi = orderService.loadOrderByNo(payDataBean.getNo_order());// 根据订单号查询信息
		if (oi != null) {
			user = oi.getUser();
			// OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
			String flag = payDataBean.getResult_pay();// 充值结果
			String addrIp = LLPayUtil.getIpAddr(request);
			if (flag.equals("SUCCESS")) {// 充值成功
				oi.setStatus(1);// 更改订单状态为成功
				oi.setOid_paybill(payDataBean.getOid_paybill());
				// 充值成功业务处理
				accountRechargeService.doLLPay(user, 1, payDataBean, addrIp);
			} else {
				oi.setStatus(2);// 更改订单状态为失败
				accountRechargeService.doLLPay(user, 2, payDataBean, addrIp);
			}
			orderService.updateOrder(oi);// 更新订单状态
		}
	}

	/**
	 * 提现结果异步返回
	 * 
	 * @throws IOException
	 */
	@Action("/cf/notifyCash")
	public void notifyCash() throws IOException {
		ServletInputStream sis = request.getInputStream();
		String str = CommonRealize.inputStream2String(sis);
		System.out
				.println("===================================================提现回调结果====="
						+ str);

		response.setCharacterEncoding("UTF-8");
		System.out.println("进入提现异步通知数据接收处理");
		RetBean retBean = new RetBean();
		String reqStr = LLPayUtil.readReqStr(request);

		if (null == reqStr || "".equals(reqStr)) {
			reqStr = str;
		}

		if (LLPayUtil.isnull(reqStr)) {
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			response.getWriter().write(JSON.toJSONString(retBean));
			response.getWriter().flush();
			return;
		}
		System.out.println("接收提现异步通知数据：【" + reqStr + "】");
		try {
			PartnerConfig pc = new PartnerConfig();
			if (!LLPayUtil.checkSign(reqStr, pc.getYtpubKey(), pc.getMd5Key())) {
				retBean.setRet_code("9999");
				retBean.setRet_msg("交易失败");
				response.getWriter().write(JSON.toJSONString(retBean));
				response.getWriter().flush();
				System.out.println("提现异步通知验签失败");
				return;
			}
		} catch (Exception e) {
			System.out.println("异步通知报文解析异常：" + e);
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			response.getWriter().write(JSON.toJSONString(retBean));
			response.getWriter().flush();
			return;
		}
		retBean.setRet_code("0000");
		retBean.setRet_msg("交易成功");
		response.getWriter().write(JSON.toJSONString(retBean));
		response.getWriter().flush();
		System.out.println("提现异步通知数据接收处理成功");
		// 解析异步通知对象
		PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		showCashResult(payDataBean);
	}

	/**
	 * 提现成功异步实现方法
	 * 
	 * @param payDataBean
	 */
	private void showCashResult(PayDataBean payDataBean) {
		System.out.println("==================提现回调的订单号： "
				+ payDataBean.getNo_order());
		OrderInfo oi = orderService.loadOrderByNo(payDataBean.getNo_order());// 根据订单号查询信息
		if (oi != null) {
			user = oi.getUser();
			String flag = payDataBean.getResult_pay();// 提现结果
			if (flag.equals("SUCCESS")) {// 提现成功
				oi.setStatus(1);// 更改订单状态为成功
				oi.setOid_paybill(payDataBean.getOid_paybill());
				// 提现成功业务处理
				accountCashService.operllCash(user, 1, payDataBean);
			} else {
				oi.setStatus(2);// 更改订单状态为失败
				accountCashService.operllCash(user, 2, payDataBean);
			}
			orderService.updateOrder(oi);// 更新订单状态
		}
	}
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
