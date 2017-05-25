package com.rongdu.p2psys.pc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSON;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.RetBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.service.ArticleService;
import com.rongdu.p2psys.core.service.SiteService;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;
import com.rongdu.p2psys.nb.homepage.service.HomePageBannerService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.user.domain.User;

@SuppressWarnings("rawtypes")
public class IndexAction extends BaseAction
{
	private Map<String,Object> map;
	
	@Resource
	private HomePageBannerService homePageBannerService;
	@Resource
	private SystemConfigService systemConfigService;
	@Resource
	private SiteService siteService;
	@Resource
	private ArticleService articleService;
	@Resource
	private IOrderService orderService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private UserService theUserService;
	@Resource
	private AccountBankService theAccountBankService;
	
	private User user;
	
	/**
	 * 合作伙伴
	 * @return
	 */
	@Action(value="/cooperation",results={@Result(name="cooperation",type="ftl",location="/nb/pc/cooperation.html")})
	public String cooperation()
	{
		return "cooperation";
	}
	
	/**
	 * index page
	 * @return
	 */
	@Action(value="/index3",results={@Result(name="index",type="ftl",location="/nb/pc/index.html")})
	public String indexPage()
	{
		request.setAttribute("regUi",paramString("regUi"));
		
		return "index";
	}
	
	/**
	 * about us page
	 * @return
	 */
	@Action(value="/aboutUs",results={@Result(name="aboutUs",type="ftl",location="/nb/pc/aboutUs.html")})
	public String aboutUs()
	{
		return "aboutUs";
	}
	
	/**
	 * media 
	 * @return
	 */
	@Action(value="/media",results={@Result(name="media",type="ftl",location="/nb/pc/media.html")})
	public String media()
	{
		request.setAttribute("id",paramString("id"));
		return "media";
	}
	
	/**
	 * index page banner list
	 * @throws IOException
	 */
	@Action(value="/bannerList")
	public void bannerList() throws IOException
	{
		map  = new 	HashMap<String, Object>();
		
		List<HomePageBanner> list = homePageBannerService.findAllEnabledHomePageBanner();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		map.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(map));
	}


	/**
	 * 获取前后台地址
	 * @return
	 */
	@Action(value="/getHostUrl")
	public void hostUrl() throws IOException
	{
		map  = new 	HashMap<String, Object>();
		
		SystemConfig systemConfig = systemConfigService.findByNid(ConstantUtil.WEB_URL);
		SystemConfig systemConfig2 = systemConfigService.findByNid(ConstantUtil.ADMIN_URL);
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		map.put(ConstantUtil.WEB_URL,systemConfig.getValue());
		map.put(ConstantUtil.ADMIN_URL,systemConfig2.getValue());
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 问答
	 * @throws Exception
	 */
	@Action("/nb/pc/articleList")
	public void articleList() throws Exception 
	{
		map = new HashMap<String, Object>();
		
		String nid = paramString("nid");
		List<ArticleModel> articleList = articleService.listBySize(nid,10000);
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		map.put(ConstantUtil.ERRORMSG, articleList);
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	public Map<String, Object> getMap()
	{
		return map;
	}

	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}
	
	
	/**
	 * 充值支付结果异步返回
	 * @throws IOException 
	 */
	 @Action("/notify")
	public void notifyIndex() throws IOException {
		ServletInputStream sis = request.getInputStream();
		String str = CommonRealize.inputStream2String(sis);
		System.out.println("===================================================回调结果====="+ str);

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
			if (!LLPayUtil.checkSign(reqStr, pc.getYtpubKey(),
					pc.getMd5Key())) {
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
	 * @param payDataBean
	 */
	private void showPayResult(PayDataBean payDataBean) {
		System.out.println("==================回调的订单号： "+payDataBean.getNo_order());
		OrderInfo oi = orderService.loadOrderByNo(payDataBean.getNo_order());//根据订单号查询信息
		if(oi!=null){			
			user = oi.getUser();
//			OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
			String flag = payDataBean.getResult_pay();//充值结果
			String addrIp = LLPayUtil.getIpAddr(request);
			if(flag.equals("SUCCESS")){//充值成功
				oi.setStatus(1);//更改订单状态为成功
				oi.setOid_paybill(payDataBean.getOid_paybill());
				//充值成功业务处理
				accountRechargeService.doLLPay(user,1,payDataBean,addrIp);
			}else{
				oi.setStatus(2);//更改订单状态为失败
				accountRechargeService.doLLPay(user,2,payDataBean,addrIp);
			}
			orderService.updateOrder(oi);//更新订单状态
		}
	}
	
	/**
	 * 提现结果异步返回
	 * @throws IOException 
	 */
	 @Action("/notifyCash")
	public void notifyCash() throws IOException {
		ServletInputStream sis = request.getInputStream();
		String str = CommonRealize.inputStream2String(sis);
		System.out.println("===================================================提现回调结果====="+ str);

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
			if (!LLPayUtil.checkSign(reqStr, pc.getYtpubKey(),
					pc.getMd5Key())) {
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
	 * @param payDataBean
	 */
	private void showCashResult(PayDataBean payDataBean) {
		System.out.println("==================提现回调的订单号： "+payDataBean.getNo_order());
		OrderInfo oi = orderService.loadOrderByNo(payDataBean.getNo_order());//根据订单号查询信息
		if(oi!=null){			
			user = oi.getUser();
			String flag = payDataBean.getResult_pay();//提现结果
			if(flag.equals("SUCCESS")){//提现成功
				oi.setStatus(1);//更改订单状态为成功
				oi.setOid_paybill(payDataBean.getOid_paybill());
				//提现成功业务处理
				accountCashService.operllCash(user, 1, payDataBean);
			}else{
				oi.setStatus(2);//更改订单状态为失败
				accountCashService.operllCash(user, 2, payDataBean);
			}
			orderService.updateOrder(oi);//更新订单状态
		}
	}
	
	/**
	 * 银联针对单独用户打款
	 * 
	 * @return
	 */
	@Action(value="/oneToCash")
	public void oneToCash() throws Exception {
		long userId = paramLong("userId");//打/扣款的用户ID
		double money = paramDouble("money");    //提现打/扣款金额（元）
		User user = theUserService.getByUserId(userId);
		List<AccountBank> bankList = theAccountBankService.list(user.getUserId(),channelKey.unionpay.getValue());
		if(bankList!=null && bankList.size()>0){
			AccountBank ab = bankList.get(0);
			String msg = accountCashService.oneToCash(user, ab, money);
			System.out.println("==================处理结果： "+msg);
		}
	}
}

