package com.rongdu.p2psys.web.tpp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.domain.TppIpsConfig;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRepaymentSigning;
import com.rongdu.p2psys.tpp.ips.model.IpsCash;
import com.rongdu.p2psys.tpp.ips.model.IpsModel;
import com.rongdu.p2psys.tpp.ips.model.IpsRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.tpp.ips.model.IpsRegisterGuarantor;
import com.rongdu.p2psys.tpp.ips.model.IpsRepayment;
import com.rongdu.p2psys.tpp.ips.model.IpsTenderBorrow;
import com.rongdu.p2psys.tpp.ips.model.IpsTransfer;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.tpp.ips.service.TppIpsConfigService;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 
 * 环讯回调处理类
 * 
 * @author lx
 * @version 2.0
 * @since 2014年8月13日
 */
public class IpsAction extends BaseAction {
	private static Logger logger = Logger.getLogger(IpsAction.class);
	
	@Autowired
	private UserService userService;
	@Resource
	private TppIpsConfigService tppIpsConfigService;
	@Resource
	private IpsService ipsService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService; 
	@Resource
	private BorrowService borrowService; 
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private AccountCashService accountCashService;
	
	/******************开户业务start*****************/
	/**
	 * 环讯开户同步回调
	 * @return
	 */
	@Action(value="/public/ips/ipsRegisterReturn",results = { @Result(name = "re", type = "ftl", location = "/tpp/result.html")},
			interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsRegisterReturn(){
		logger.info("环讯开户同步通知开始-----------");
		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("left_url", "/member/main.html"); // 成功返回地址
		request.setAttribute("right_url", "/member/recharge/newRecharge.html"); // 成功返回地址
		request.setAttribute("left_msg", MessageUtil.getMessage("I10001")); 
		request.setAttribute("right_msg", MessageUtil.getMessage("I20001")); 
		request.setAttribute("back_url", "/member/security/realNameIdentify.html");// 失败返回地址
		request.setAttribute("r_msg", MessageUtil.getMessage("I60002"));
		IpsModel ips = getWebReturnIpsParam();
		dealIpsRegister(ips,resultFlag);
		// 更新用户认证状态
		UserIdentify userIdentify = getSessionUserIdentify();
		//BUG #15865,商户环迅开户点击返回时报NULl值错误，加入null处理
		if(userIdentify!=null&&userIdentify.getId()!=0)
		{
			userIdentify = userIdentifyService.findById(userIdentify.getId());
			// 更新session中real_name_status的状态
			userIdentify.setRealNameStatus(1);
			session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		}
		logger.info("环讯开户同步通知结束-----------");
		return "re";
	}
	/**
	 * 环讯开户异步通知
	 * @return
	 */
	@Action(value="/public/ips/ipsRegisterNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public void ipsRegisterNotify(){
		logger.info("环讯开户异步通知开始-----------");
		IpsModel ips = getWebReturnIpsParam();
		dealIpsRegister(ips,"");
		logger.info("环讯开户异步通知结束-----------");
	}
	
	private IpsRegister dealIpsRegister(IpsModel ips,String resultFlag) {
		IpsRegister ipsre = new IpsRegister();
		//判断是否开户成功！
		if (!"MG00000F".equals(ips.getErrCode()) && !"MG00008F".equals(ips.getErrCode())) {
			logger.error("开户失败,原因："+ips.getErrMsg());
			throw new BussinessException(MessageUtil.getMessage(ips.getErrCode()), 2);
		}
		String xml = ips.checkSign(); //验签
		//处理回调
		ipsre = ipsre.doReturnCreate(xml);
		User user = userService.getUserById(StringUtil.toLong(ipsre.getMemo1()));
		ConcurrentUtil.ipsRegister(user, ipsre,resultFlag);
		return ipsre;
	}
	
	/******************开户业务end*****************/
	
	
	/******************担保公司登记start*****************/
	/**
	 * 登记担保方同步回调
	 * @return
	 */
	@Action(value="/public/ips/ipsGuarantorReturn",results = { @Result(name = "re", type = "ftl", location = "/tpp/result.html")},
			interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsGuarantorReturn(){
		logger.info("登记担保方同步回调开始======");
		IpsModel ips = getWebReturnIpsParam();
		doIpsRegisterGuarantor(ips);
		request.setAttribute("left_url", "/member/main.html"); 
		request.setAttribute("right_url", "/member/main.html"); 
		request.setAttribute("left_msg", MessageUtil.getMessage("I70002")); 
		request.setAttribute("right_msg", MessageUtil.getMessage("I10001")); 
		request.setAttribute("back_url", "/member/main.html");
		request.setAttribute("r_msg", MessageUtil.getMessage("I70003"));
		logger.info("登记担保方同步回调结束======");
		return "re";
	}
	
	/**
	 * 登记担保方异步回调
	 * @return
	 */
	@Action(value="/public/ips/ipsGuarantorNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public void ipsGuarantorNotify(){
		logger.info("登记担保方异步回调开始======");
		IpsModel ips = getWebReturnIpsParam();
		doIpsRegisterGuarantor(ips);
		logger.info("登记担保方异步回调结束======");
	}
	
	/**
	 * 登记担保方业务处理
	 * @param ips
	 */
	private void doIpsRegisterGuarantor(IpsModel ips){
		IpsRegisterGuarantor irg =new IpsRegisterGuarantor();
		try {
			//判断是否成功
			if (!"MG00000F".equals(ips.getErrCode())) {
				logger.info("登记担保方处理异常原因："+ips.getErrMsg());
				throw new BussinessException(MessageUtil.getMessage(ips.getErrCode()), 2);
			}
			
			String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
			request.setAttribute("resultFlag", resultFlag);			
			String xml = ips.checkSign(); 
			//处理回调
			irg = irg.doReturnCreate(xml);
			BorrowModel bm = new BorrowModel();
			bm.setBidNo(irg.getBidNo());
			bm.setGuaranteeNo(irg.getMerBillNo());
			bm.setEndTime(irg.getIpsTime());
			ConcurrentUtil.doIpsRegisterGuarantor(bm, resultFlag);
			
		} catch (Exception e) {
			logger.error("登记担保方回调处理失败！原因" + e.getMessage());
			throw new BussinessException(MessageUtil.getMessage("MG04001F"), 2);
		}
	}
	/******************担保公司登记end*****************/
	
	
	/******************充值业务start*****************/
	@Action(value="/public/ips/ipsRechgargeReturn",results = { @Result(name = "re", type = "ftl", location = "/tpp/result.html")},
			interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsRechgargeReturn(){
		logger.info("充值同步通知开始-----------");
		IpsModel ips = getWebReturnIpsParam();
		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		//request.setAttribute("ok_url", "/member/recharge/log.html"); // 成功返回地址
		User u =getSessionUser();
		if(u!=null && u.getUserCache().getUserNature()==2){
			request.setAttribute("left_url", "/member/recharge/log.html"); // 成功返回地址
			request.setAttribute("right_url", "/member/recharge/newRecharge.html"); // 成功返回地址
			request.setAttribute("left_msg", MessageUtil.getMessage("I20002")); 
			request.setAttribute("right_msg", MessageUtil.getMessage("I20001")); 
		}else if(u!=null && u.getUserCache().getUserNature()==1){
			request.setAttribute("left_url", "/member/recharge/log.html"); // 成功返回地址
			request.setAttribute("right_url", "/invest/index.html"); // 成功返回地址
			request.setAttribute("left_msg", MessageUtil.getMessage("I20002")); 
			request.setAttribute("right_msg", MessageUtil.getMessage("I30002")); 
		}else{
			request.setAttribute("left_url", "/member/recharge/log.html"); // 成功返回地址
			request.setAttribute("right_url", "/member/main.html"); // 成功返回地址
			request.setAttribute("left_msg", MessageUtil.getMessage("I20002")); 
			request.setAttribute("right_msg", MessageUtil.getMessage("I10001")); 
		}

		dealIpsRecharge(ips,resultFlag);
		
		// 成功
		if ("MG00000F".equals(ips.getErrCode())) {
			request.setAttribute("r_msg", MessageUtil.getMessage("I20003"));
		// 处理中
		}else if("MG00008F".equals(ips.getErrCode())){
			request.setAttribute("r_msg", MessageUtil.getMessage("I20004"));
		// 失败
		}else{
			throw new BussinessException(MessageUtil.getMessage("I20005"), 2);
		}
		logger.info("充值同步通知结束-----------");
		return "re";
	}
	
	@Action(value="/public/ips/ipsRechgargeNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public void ipsRechgargeNotify(){
		logger.info("充值异步通知开始-----------");
		IpsModel ips = getWebReturnIpsParam();
		dealIpsRecharge(ips,"");
		logger.info("充值异步通知结束-----------");
	}
	
	/**
	 * 充值业务处理
	 * @param param
	 * @return
	 */
	private void dealIpsRecharge(IpsModel ips,String resultFlag) {
		IpsRecharge ipsRecharge = new IpsRecharge();
		String xml = ips.checkSign(); //验签
		//处理回调
		ipsRecharge.doReturnMessage(xml);
		ipsRecharge.setErrCode(ips.getErrCode());
		ipsRecharge.setErrMsg(ips.getErrMsg());
		// 成功
		if ("MG00000F".equals(ips.getErrCode())) {
			RechargeModel reModel = new RechargeModel(); // 对通用javabean进行参数封装
			reModel.setOrderAmount(ipsRecharge.getTrdAmt());
			reModel.setOrderId(ipsRecharge.getMerBillNo());
			reModel.setResultMsg(ipsRecharge.getErrMsg());
			reModel.setChannelType(ipsRecharge.getMemo1());
			reModel.setResult(ips.getErrCode());
			reModel.setSerialNo(ipsRecharge.getIpsBillNo());
			ConcurrentUtil.doRechargeBackTask(reModel,resultFlag);
		// 处理中
		}else if("MG00008F".equals(ips.getErrCode())){
			accountRechargeService.updateStatusByTradeNo(ipsRecharge.getMerBillNo(), 0, TppIpsPay.STATUS_WAIT);
		// 失败
		}else{
			logger.error("充值失败原因："+ips.getErrMsg());
			accountRechargeService.updateStatusByTradeNo(ipsRecharge.getMerBillNo(), 2, TppIpsPay.STATUS_FAIL);
		}
	}

	/******************充值业务end*****************/
	
	/******************提现业务start*****************/
	@Action(value="/public/ips/ipsCashReturn", results = { @Result(name = "re", type = "ftl", location = "/tpp/result.html")},
			interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsCashReturn(){
		logger.info("提现同步回调开始-----------");
		IpsModel ips = getWebReturnIpsParam();
		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		User user =getSessionUser();
		if(user != null){
			if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {
				request.setAttribute("left_url", "/member_borrow/account/log.html"); 
				request.setAttribute("right_url", "/member/main.html"); 
				request.setAttribute("left_msg", MessageUtil.getMessage("I30001")); 
				request.setAttribute("right_msg", MessageUtil.getMessage("I10001")); 
			}
		}else {
			request.setAttribute("left_url", "/member/cash/log.html"); 
			request.setAttribute("right_url", "/member/main.html"); 
			request.setAttribute("left_msg", MessageUtil.getMessage("I40001")); 
			request.setAttribute("right_msg", MessageUtil.getMessage("I10001")); 
		}
		//提现成功
		if(dealIpsCash(ips,resultFlag)) {
			request.setAttribute("r_msg", MessageUtil.getMessage("I40002"));
		// 提现失败
		}else{
			throw new BussinessException(MessageUtil.getMessage("I40003"), 2);
		}
		logger.info("提现同步回调结束-----------");
		
		return "re";
	}
	@Action(value="/public/ips/ipsCashNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public void ipsCashNotify(){
		logger.info("提现异步回调开始-----------");
		IpsModel ips = getWebReturnIpsParam();
		dealIpsCash(ips,"");
		logger.info("提现异步回调结束-----------");
	}
	/**
	 * 提现业务处理
	 * @param param
	 * @return
	 */
	private boolean dealIpsCash(IpsModel ips,String resultFlag) {
		// 返回值
		boolean result = false;
		IpsCash ipsCash = new IpsCash();
		String xml = ips.checkSign(); //验签
		//处理回调
		ipsCash = ipsCash.doReturnCreate(xml);
		//提现失败
		if (!"MG00000F".equals(ips.getErrCode())) {
			logger.error("提现失败原因：" + ips.getErrMsg());
			accountCashService.updateStatusByOrderNo(ipsCash.getMerBillNo(), 2, TppIpsPay.STATUS_FAIL);
			result = false;
		}else{
			try {
				CashModel cashModel = new CashModel();
				cashModel.setOrderId(ipsCash.getMerBillNo());
				cashModel.setOrderAmount(ipsCash.getTrdAmt());
				cashModel.setResult(true);
				ConcurrentUtil.doVerifyCashBackTask(cashModel,resultFlag);
				logger.error("提现回调处理成功！" );
				result = true;
			} catch (Exception e) {
				logger.error("提现回调处理失败！原因" + e.getMessage());
				result = false;
			}
		}
		
		return result;
	}
	/******************提现业务end*****************/

	
	/******************投标start*****************/
	/**
	 * 投标接口前台回调
	 * @return
	 */
	@Action(value="/public/ips/ipsTenderReturn", results = { @Result(name = "re", type = "ftl", location = "/tpp/result.html")},
			interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsTenderReturn(){
		logger.info("投标同步回调开始======");
		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		IpsModel ips = getWebReturnIpsParam();
		if(doIpsTenderBorrow(ips,resultFlag)){
			request.setAttribute("resultFlag", resultFlag);
			request.setAttribute("left_url", "/member/invest/mine.html"); 
			request.setAttribute("right_url", "/member/main.html"); 
			request.setAttribute("left_msg", MessageUtil.getMessage("I30003")); 
			request.setAttribute("right_msg", MessageUtil.getMessage("I10001")); 
			request.setAttribute("m_url", "/invest/index.html"); 
			request.setAttribute("m_msg", MessageUtil.getMessage("I30006")); 
			request.setAttribute("back_url", "/invest/index.html");
			request.setAttribute("r_msg", MessageUtil.getMessage("I30004"));
		}else{
			throw new BussinessException(MessageUtil.getMessage("I30005"), 2);
		}
		logger.info("投标同步回调结束======");
		return "re";
	}
	/**
	 * 投标接口异步回调
	 * @return
	 */
	@Action(value="/public/ips/ipsTenderNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public void ipsTenderNotify(){
		logger.info("投标接口异步回调开始======");
		IpsModel ips = getWebReturnIpsParam();
		doIpsTenderBorrow(ips,"");
		logger.info("投标接口异步回调结束======");
	}
	
	/**
	 * 处理关于投标的业务回调
	 * @param ipsBorrow
	 * @param param
	 * @return
	 */
	private boolean doIpsTenderBorrow(IpsModel ips,String resultFlag){
		// 返回值
		boolean result = false;
		try {
			//验签
			String xml = ips.checkSign(); 
			//处理回调
			IpsTenderBorrow ipsTenderBorrow = new IpsTenderBorrow();
			ipsTenderBorrow = ipsTenderBorrow.doReturnCreate(xml);
			//失败
			if (!"MG00000F".equals(ips.getErrCode())) {
				logger.error("投标失败，原因：" + ips.getErrMsg());
				result = false;			
			}else{
				// 成功,处理平台投标相关业务
				BorrowModel bm = new BorrowModel();
				bm.setBidNo(ipsTenderBorrow.getBidNo());
				bm.setTenderBilNo(ipsTenderBorrow.getMerBillNo());
				bm.setMoney(StringUtil.toDouble(ipsTenderBorrow.getTransferAmt()));
				bm.setBidStatus(ipsTenderBorrow.getStatus());
				bm.setUserId(StringUtil.toLong(ipsTenderBorrow.getMemo1()));
				bm.setId(StringUtil.toLong(ipsTenderBorrow.getMemo2()));
				bm.setAddIp(ipsTenderBorrow.getMemo3());
				bm.setEndTime(ipsTenderBorrow.getIpsTime());
				ConcurrentUtil.doAddTender(bm,resultFlag);
				result = true;
			}
		} catch (Exception e) {
			logger.error("投标回调处理失败！原因"+e.getMessage());
			throw new BussinessException(MessageUtil.getMessage("MG03011F"), 2);
		}
		return result;
	}
	/******************投标end*****************/
	
	/**
	 * 转账接口异步回调
	 * @return
	 */
	@Action(value="/public/ips/ipsTransferNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsTransferNotify(){
		logger.info("转账接口异步回调======");
		IpsModel ips = getWebReturnIpsParam();
		//回调处理
		return null;
	}
	
	
	
	/******************自动还款签约 begin*****************/
	/**
     * 环迅自动签约还款处理页面
     * @return 页面
     */
    @Action(value="/public/ips/autoRepaymentSigningPage",
            results={@Result(name="signingPage" , type = "ftl", location = "/tpp/autoRepaymentSigningPage.html")},
            interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")})
    public String autoRepaymentSigningPage(){
        this.saveToken("autoToken");
        boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
            message("环迅托管未启用！系统配置错误！");
            return MSG;
        }
        request.setAttribute("user", this.getSessionUser());
        return "signingPage";
    }
    
    /**
     * 环迅自动签约还款处理
     * @return 页面
     */
    @Action(value="/tpp/doAutoRepaymentSigning", 
            results={@Result(name="ipsAutoRepaySigning" , type = "ftl", location = "/tpp/ipsAutoRepaySigning.html")},
            interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")})
    public String doAutoRepaymentSigningPage() throws Exception{
        this.checkToken("autoToken");
        long userId = this.paramLong("userId");
        if(userId != this.getSessionUserId()){
            message("自动还款签约处理失败，请重新设置！");
        }
        TppIpsConfig config = new TppIpsConfig();
        config.setUserId(userId);
        IpsAutoRepaymentSigning repay = tppIpsConfigService.doAutoRepaymentSigning(config);
        if(repay != null && repay.getMerBillNo() != null){
            String name = Global.getValue("cooperation_interface");
            request.setAttribute(name, repay);
            return name + "AutoRepaySigning";
        } else {
            message("自动还款签约处理失败，请联系系统管理员！");
        }
        return MSG;
    }
	
    /**
     * 环迅自动签约还款处理页面
     * @return 页面
     */
    @Action(value="/public/ips/ipsDoAutoRepaySigningReturn")
    public String ipsDoAutoRepaySigningReturn(){
        logger.info("投标接口回调======");
        this.doAutoRepaySigning();
        message("环迅自动签约还款处理成功，请返回查看");
        return MSG;
    }
    
    /**
     * 环迅自动签约还款处理页面
     * @return 页面
     */
    @Action(value="/public/ips/ipsDoAutoRepaySigningNotify")
    public void ipsDoAutoRepaySigningNotify(){
        logger.info("环迅自动签约还款接口回调======");
        this.doAutoRepaySigning();
        message("环迅自动签约还款处理成功，请返回查看");
    }
    
    public void doAutoRepaySigning() {
        IpsModel ips = getWebReturnIpsParam();
        boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
            throw new BussinessException("环迅接口未启用！", "/member/main.html");
        }
        logger.info("进入操作标的接口" + getRequestParams());
        IpsAutoRepaymentSigning sign = new IpsAutoRepaymentSigning();
        TppIpsConfig con = new TppIpsConfig();
        try {
            //判断是否成功
            if (!"MG00000F".equals(ips.getErrCode())) {
                con.setAutoRepayStatus(TppIpsConfig.AUTO_REPAY_NO);
                logger.info(ips.getErrMsg());
                throw new BussinessException("环迅自动签约还款处理处理失败", "/member/index.html");
            }else{
                con.setAutoRepayStatus(TppIpsConfig.AUTO_REPAY_YES);
            }
            String xml = ips.checkSign(); //验签
            logger.info("处理标接口回调："+xml);
            //处理回调
            sign = sign.doReturnCreate(xml);
            con.setAutoRepayNum(sign.getMerBillNo());
            con.setAutoRepayNo(sign.getIpsAuthNo());
            if(sign.getValidDate() != null && sign.getValidDate().length() > 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Date date = sdf.parse(sign.getValidDate());
                con.setAutoRepayEndTime(date);
            }
            tppIpsConfigService.editAutoRepaymentSigning(con);
        } catch (Exception e) {
            logger.info("环迅自动签约还款处理处理失败！原因"+e.getMessage());
            throw new BussinessException("环迅自动签约还款处理失败！", "/member/main.html");
        }
    }
    
    /******************自动还款签约 end*****************/
    
    
    /******************还款 begin*****************/
    /**
     * 环迅还款处理页面
     * @return 页面
     */
    @Action(value="/public/ips/ipsDoRepaymentReturn",results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html")},interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
    public String ipsDoRepaymentReturn(){
        logger.info("环迅还款接口同步回调======");
        String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("right_url", "/member/main.html"); // 成功返回地址
		request.setAttribute("right_msg", "账户主页"); 
		request.setAttribute("r_msg", "恭喜您，环迅还款申请提交成功！");
       
		IpsModel ips = getWebReturnIpsParam();
        boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
            throw new BussinessException("环迅接口未启用！", "/member/main.html");
        }
        logger.info("进入操作标的接口" + getRequestParams());
        IpsRepayment repay = new IpsRepayment();
        TppIpsPay pay = new TppIpsPay();
        try {
        	logger.info(ips.getErrMsg());
            String xml = ips.checkSign(); //验签
            //处理回调
            repay = repay.doReturnCreate(xml);
            pay.setMerBillNo(repay.getMerBillNo());
            pay.setIpsBillNo(repay.getIpsBillNo());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(repay.getIpsDate());
            pay.setIpsTime(date);
            pay.setIpsFee(StringUtil.toDouble(repay.getOutIpsFee()));
            
            //判断是否成功
            if (!"MG00008F".equals(ips.getErrCode())) {
                pay.setStatus(TppIpsPay.STATUS_FAIL);
                ipsService.ipsRayManage(pay);
                throw new BussinessException(getErrMsg(ips.getErrCode(), "环迅还款处理失败"), "/member/index.html");
            }else if("MG00008F".equals(ips.getErrCode())){
            	BorrowRepayment repayment = ipsService.updateRepay(pay);
            	request.setAttribute("left_url", "/member_borrow/borrow/repayment.html?borrowId="+repayment.getBorrow().getId()); // 成功返回地址
            	request.setAttribute("left_msg", "还款详情");
                Global.RESULT_MAP.put(resultFlag, "success");
            }
        } catch (Exception e) {
            logger.info("环迅还款处理失败！原因"+e.getMessage());
        }
        return "result";
    }
    
    @Action(value="/public/ips/ipsDoRepaymentNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
    public void ipsDoRepaymentNotify(){
        logger.info("环迅还款接口异步回调======");
        this.ipsRayManage();
    }
    
    public void ipsRayManage(){
        IpsModel ips = getWebReturnIpsParam();
        boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
            throw new BussinessException("环迅接口未启用！", "/member/main.html");
        }
        logger.info("进入操作标的接口" + getRequestParams());
        IpsRepayment repay = new IpsRepayment();
        TppIpsPay pay = new TppIpsPay();
        try {
            //判断是否成功
            /*if ("MG00008F".equals(ips.getErrCode())) {
                logger.info(ips.getErrMsg());
            }else*/ if(!"MG00008F".equals(ips.getErrCode()) && !"MG00000F".equals(ips.getErrCode())){
            	pay.setStatus(TppIpsPay.STATUS_FAIL);
                logger.info(ips.getErrMsg());
                throw new BussinessException("环迅还款处理失败", "/member/index.html");
            }else{
                pay.setStatus(TppIpsPay.STATUS_SUCCESS);
            }
            String xml = ips.checkSign(); //验签
            logger.info("处理还款接口异步回调："+xml);
            //处理回调
            repay = repay.doReturnCreate(xml);
            pay.setMerBillNo(repay.getMerBillNo());
            pay.setIpsBillNo(repay.getIpsBillNo());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(repay.getIpsDate());
            pay.setIpsTime(date);
            pay.setIpsFee(StringUtil.toDouble(repay.getOutIpsFee()));
            ipsService.ipsRayManage(pay);
        } catch (Exception e) {
            logger.info("环迅还款处理失败！原因"+e.getMessage());
        }
    }

    /******************还款 end*****************/
	/**
	 * 回调函数前台处理
	 * @return
	 */
	private IpsModel getWebReturnIpsParam(){
		IpsModel ips = new IpsModel();
		String pMerCode = paramString("pMerCode");  //获取处理状态
		String pErrCode = paramString("pErrCode");  //获取处理状态
		String pErrMsg = paramString("pErrMsg");  //返回信息
		String p3DesXmlPara = paramString("p3DesXmlPara");  //加密回调参数
		String pSign = paramString("pSign");
		ips.setMerCode(pMerCode);
		ips.setErrCode(pErrCode);
		ips.setErrMsg(pErrMsg);
		ips.setDesXmlPara(p3DesXmlPara);
		ips.setSign(pSign);
		return ips;
	}
	
	/**
	 * 后台回调参数拼接 ,此方法暂时注释掉
	 * @return
	 */
	/*private IpsModel getReturnIpsNotifyParam(){
		IpsModel ips = new IpsModel();
		String resultStr = paramString("resultStr");
		logger.error("-----------"+resultStr);
		//String returnStr = paramString("returnStr");
		String pMerCode = XmlTool.getXmlNodeValue(resultStr, "pMerCode");
		String pErrCode = XmlTool.getXmlNodeValue(resultStr, "pErrCode");
		String pErrMsg = XmlTool.getXmlNodeValue(resultStr,"pErrMsg");
		String p3DesXmlPara = XmlTool.getXmlNodeValue(resultStr,"p3DesXmlPara");
		String pSign = XmlTool.getXmlNodeValue(resultStr,"pSign");
		ips.setMerCode(pMerCode);
		ips.setErrCode(pErrCode);
		ips.setErrMsg(pErrMsg);
		ips.setDesXmlPara(p3DesXmlPara);
		ips.setSign(pSign);
		return ips;
	}*/
	
	/**
	 * 回调参数拼接共用方法
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getRequestParams() {
		String params = "";
		try {
			Enumeration<String> e = (Enumeration<String>) request.getParameterNames();
			while (e.hasMoreElements()) {
				String parName = e.nextElement();
				String value = request.getParameter(parName);
				params += parName + "=" + value + "&";
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return params;
	}

	
	/**
	 * 返回系通处理的信息
	 * @return
	 * @throws Exception
	 */
	@Action("/public/ips/getResult")
	public String getTenderResult() throws Exception{
 		String result = "";
		String resultFlag = paramString("resultFlag");
		if(StringUtil.isBlank(resultFlag)){
			result="查询处理信息错误";
		}else{
			result = (String)Global.RESULT_MAP.get(resultFlag);
			Global.RESULT_MAP.remove(resultFlag);//系统消息取出来之后立即删除，保证集合为空
		}
		Map<String,String> map = new HashMap<String, String>();
		map.put("msg_data", result);
		printWebJson(JSON.toJSONString(map));
		return null;
	}
	
	public static String getErrMsg(String errCode, String defaultMsg){
		String errMsg = MessageUtil.getMessage(errCode);
		if(!StringUtil.isBlank(errMsg)){
			return errMsg;
		}else{
			return defaultMsg;
		}
	}
	
	/******************代偿 begin*****************/
    @Action(value="/public/ips/ipsCompensateNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
    public void ipsCompensateNotify(){
    	logger.info("环迅代偿接口异步回调处理开始======");
        IpsModel ips = getWebReturnIpsParam();
        TppIpsPay pay = new TppIpsPay();
        try {
            //判断是否成功
            if (!"MG00000F".equals(ips.getErrCode())) {
                pay.setStatus(TppIpsPay.STATUS_FAIL);
                logger.error("代偿异步回调失败，原因：" + ips.getErrMsg());
            }else{
                pay.setStatus(TppIpsPay.STATUS_SUCCESS);
                logger.info("环迅代偿接口异步回调成功======");
            }
            //验签
            String xml = ips.checkSign(); 
            //处理回调
            IpsTransfer ipsTransfer = new IpsTransfer();
            ipsTransfer = ipsTransfer.doTransferCreate(xml);
            pay.setMerBillNo(ipsTransfer.getMerBillNo());
            pay.setIpsBillNo(ipsTransfer.getIpsBillNo());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            pay.setIpsTime(sdf.parse(ipsTransfer.getIpsTime()));
            pay.setIpsFee(StringUtil.toDouble(ipsTransfer.getIpsFee()));
            ipsService.doCompensateSuccess(pay);
        } catch (Exception e) {
            logger.error("平台代偿处理失败！原因"+e.getMessage());
            e.printStackTrace();
        }
        logger.info("环迅代偿接口异步回调处理结束======");
    }
    
    /******************代偿 end*****************/
	
}
