package com.rongdu.manage.action.tpp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.model.IpsAddBorrow;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsModel;
import com.rongdu.p2psys.tpp.ips.model.IpsTransfer;
import com.rongdu.p2psys.tpp.ips.model.TppIpsPayModel;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.tpp.ips.service.TppIpsConfigService;
import com.rongdu.p2psys.tpp.ips.service.TppIpsPayService;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 
 * 环讯回调处理类
 * 
 * @author lx
 * @version 2.0
 * @since 2014年8月13日
 */
public class ManageIpsAction extends BaseAction<TppIpsPayModel> implements ModelDriven<TppIpsPayModel> {
	private static Logger logger = Logger.getLogger(ManageIpsAction.class);
	
	@Autowired
	private UserService userService;
	@Resource
	private TppIpsConfigService tppIpsConfigService;
	@Resource
	private TppIpsPayService tppIpsPayService;
	@Resource
	private IpsService ipsService;
	@Resource
	private AccountService accountservice;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private BorrowService borrowService; 
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	/******************调度任务start*****************/
	/**
	 * 接口调度记录
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/modules/loan/borrow/ipsPayManager")
	public String ipsPayManager() throws Exception {
		return "ipsPayManager";
	}

	/**
	 * 接口调度列表
	 * 
	 * @throws Exception if has error
	 */
	@Action("/modules/loan/borrow/ipsPayList")
	public void ipsPayList() throws Exception {
		model.setSize(paramInt("rows"));
		PageDataList<TppIpsPayModel> list = tppIpsPayService.list(model);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	@Action("/modules/loan/borrow/schedule")
	public String schedule() throws Exception {
		//页面参数
		int page = paramInt("page");
		//int pernum = paramInt("pernum")==0?2*Page.ROWS:paramInt("pernum");
		String borrowid = paramString("borrowid");
		String period = paramString("period");
		String orderno = paramString("orderno");
		String status = paramString("status");
		String operatetype = paramString("operatetype");
		String service = paramString("service");
		String ids = request.getParameter("ids");
		String[] values = null;
		if(!StringUtil.isBlank(ids)){			
			values = ids.substring(0, ids.length()-1).split(",");
			List<Object> taskList = new ArrayList<Object>();
			logger.info(" 重复处理ids : " + ids);
			for(int i=0;i<values.length;i++){
				int id = StringUtil.toInt(values[i].trim());
				logger.info("当前处理" + id);
				if(id!=0){
					//Object apiPay = tppIpsPayService.find(id);
					Object apiPay =null;
					taskList.add(apiPay);
				}					
			}
			ipsService.doIpsTask(taskList);
		}
		//查询
		//SearchParam sp = SearchParam.getInstance();
		//sp.addPage(page, pernum);

		//String msg = findApiAllList(sp, borrowid, period, orderno, status, operatetype, service,apiType);
		String msg ="";
		if(!StringUtil.isBlank(msg)){
			return msg;
		}else{
			message("系统异常！");
			return MSG;
		}
	}

	/******************调度任务end*****************/
	
	/******************发标start*****************/
	/**
	 * 标登记接口前台回调
	 * @return
	 */
	@Action(value="/public/ips/ipsAddBorrowReturn",results = { @Result(name = "msg", type = "ftl", location = "/modules/tpp/msg.html")},
			interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsAddBorrowReturn(){
		logger.info("标登记接口同步回调开始======");
		IpsModel ips = getWebReturnIpsParam();
		request.setAttribute("back_url", "<a href='/home.html'>返回</a>");
		if(doIpsAddBorrow(ips)){
			if(ips.getErrCode().equals("MG02503F")){
				request.setAttribute("r_msg", "撤标申请成功，请返回查看！");
			}else{
				request.setAttribute("r_msg", "标登记成功，请返回查看！");
			}
		}else{
			request.setAttribute("r_msg", "标操作失败！");
		}
		logger.info("标登记接口同步回调结束======");
		return "msg";
	}
	
	/**
	 * 发标接口后台回调
	 * @return
	 */
	@Action(value="/public/ips/ipsAddBorrowNotify",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public void ipsAddBorrowNotify(){
		logger.info("标登记接口异步回调开始======");
		IpsModel ips = getWebReturnIpsParam();	
		doIpsAddBorrow(ips);
		logger.info("标登记接口异步回调结束======");
	}
	
	/**
	 * 处理关于登记表的业务回调
	 * @param ipsBorrow
	 * @param param
	 * @return
	 */
	private boolean doIpsAddBorrow(IpsModel ips){
		IpsAddBorrow ipsBorrow = new IpsAddBorrow();
		try {
			//判断是否成功
			if (!"MG02501F".equals(ips.getErrCode()) && !"MG02500F".equals(ips.getErrCode()) && !"MG00000F".equals(ips.getErrCode()) &&
					!"MG02503F".equals(ips.getErrCode()) && !"MG02504F".equals(ips.getErrCode()) && !"MG02505F".equals(ips.getErrCode())) {
				logger.error("标操作处理异常原因："+ips.getErrMsg());
				return false;
			}
			String xml = ips.checkSign(); 
			logger.info("环讯回调XML：" + xml);
			//处理回调
			ipsBorrow = ipsBorrow.doReturnCreate(xml);
			BorrowModel bm = new BorrowModel();
			bm.setBidNo(ipsBorrow.getBidNo());
			bm.setBidStatus(ipsBorrow.getBidStatus());
			bm.setId(StringUtil.toLong(ipsBorrow.getMemo1()));
			bm.setErrMsg(ips.getErrCode());
			ConcurrentUtil.doAddBorrow(bm);
			
		} catch (Exception e) {
			logger.error("登记标回调处理失败！原因" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/******************发标end*****************/
	
	/**
	 * 转账接口异步回调
	 * @return
	 */
	@Action(value="/public/ips/ipsTransferNotify", interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
	public String ipsTransferNotify(){
		
		logger.info("转账接口异步回调" + getRequestParams());
		IpsTransfer ipsTransfer = new IpsTransfer();
		IpsModel ips = getWebReturnIpsParam();
		int status = 0;
		try {
			//判断是否成功
			if ( !"MG00000F".equals(ips.getErrCode())) {
				logger.error("转账接口异步回调异常，原因："+ips.getErrMsg());
				status = TppIpsPay.STATUS_FAIL;
			}else{
				status = TppIpsPay.STATUS_SUCCESS;
			}
			String xml = ips.checkSign(); 
			//处理回调
			ipsTransfer = ipsTransfer.doTransferCreate(xml);

			// 成功时处理
			if ( "MG00000F".equals(ips.getErrCode())) {
				long borrowId = StringUtil.toLong(ipsTransfer.getMemo1());
				Borrow borrow = borrowService.getBorrowById(borrowId);
				
				// 投资时处理满标复审相关处理
				if("1".equals(ipsTransfer.getTransferType()) && borrow.getStatus() == 3){
					BorrowModel bm = new BorrowModel();
					bm.setBidNo(ipsTransfer.getBidNo());
					bm.setId(StringUtil.toLong(ipsTransfer.getMemo1()));
					bm.setErrMsg(ips.getErrCode());
					ConcurrentUtil.autoVerifyFullSuccess(bm);
				}
			}
			
			// 更新环讯资金日志
			TppIpsPay pay = new TppIpsPay();
			pay.setMerBillNo(ipsTransfer.getMerBillNo());
			pay.setIpsBillNo(ipsTransfer.getIpsBillNo());
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			try{
				pay.setIpsTime(format.parse(ipsTransfer.getIpsTime()));
			}catch(ParseException e){
				pay.setIpsTime(new Date());
			}
			pay.setIpsFee(Double.parseDouble(ipsTransfer.getIpsFee()));
			pay.setStatus((byte)status);
			tppIpsPayService.updateTppIpsPayByMerBillNo(pay);

		} catch (Exception e) {
			logger.error("转账接口异步回调处理失败！" );
			logger.error(e.getMessage());
		}
		
		logger.info("转账接口异步回调结束======");
		return null;
	}
	
	/*********************    自动代扣充值 begin     ***************************/
    
	@Action(value="/public/ips/ipsDoAutoRechargeNotify")
    public void ipsDoAutoRechargeNotify(){
        logger.info("环迅自动充值代扣接口回调======");
        IpsModel ips = getWebReturnIpsParam();
        boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
            throw new BussinessException("环迅接口未启用！", "/member/main.html");
        }
        logger.info("进入充值回调" + getRequestParams());
        IpsAutoRecharge ipsRecharge = new IpsAutoRecharge();
        String xml = ips.checkSign(); //验签
        //处理回调
        ipsRecharge.doNotifyCreate(xml);
        ipsRecharge.setErrCode(ips.getErrCode());
        ipsRecharge.setErrMsg(ips.getErrMsg());
        if (!"MG00000F".equals(ips.getErrCode()) && !"MG00008F".equals(ips.getErrCode())) {
            logger.info("充值失败原因："+ips.getErrMsg());
            throw new BussinessException("充值失败，请检查您输入的信息是否正确", "/member/recharge/newRecharge.html");
        }
        if ("MG00000F".equals(ips.getErrCode()) ) {
            RechargeModel reModel = new RechargeModel(); // 对通用javabean进行参数封装
            reModel.setOrderId(ipsRecharge.getMerBillNo());
            reModel.setOrderAmount(ipsRecharge.getTrdAmt());
            reModel.setGateBankId(ipsRecharge.getTrdBnkCode());
            reModel.setResult("true");
            reModel.setFeeAmt(Double.parseDouble(ipsRecharge.getIpsFee()));
            reModel.setResultMsg(ipsRecharge.getErrMsg());
            reModel.setSerialNo(ipsRecharge.getIpsBillNo());
            ConcurrentUtil.doRechargeBackTask(reModel,"");
        }
    
        message("充值处理成功，请返回查看");
    }
    
    /**
	 * 接口前台回调
	 * @return
	 */
	@Action(value="/modules/account/account/autoRecharge",
			results = { @Result(name = "ipsAutoRecharge", type = "ftl", location = "/modules/tpp/ipsAutoRecharge.html")})
	public String autoRecharge(){
		boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
            throw new BussinessException("环迅接口未启用！", "/member/main.html");
        }
        this.saveToken("autoToken");
        long userId = this.paramLong("id");
        User user = userService.getUserById(userId);
        Account account = accountservice.findByUser(userId);
        request.setAttribute("account", account);
        request.setAttribute("user", user);
		String name = Global.getValue("cooperation_interface");
        return name + "AutoRecharge";
	}
	
	public void doAutoRechaarge(){
		this.checkToken("autoToken");
		long userId = this.paramLong("id");
	    User user = userService.getUserById(userId);
	    double rechargeMoney = this.paramDouble("rechargeMoney");
        AccountRecharge ar = new AccountRecharge(user, rechargeMoney,"online_recharge", AccountRecharge.TYPE_AUTO_RECHARGE, "");
        ipsService.doAutoRecharge(ar);
	}
	/*********************     自动代扣充值 end    ***************************/
    
    
    
    
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
	
	public static String getErrMsg(String errCode, String defaultMsg){
		String errMsg = MessageUtil.getMessage(errCode);
		if(!StringUtil.isBlank(errMsg)){
			return errMsg;
		}else{
			return defaultMsg;
		}
	}
}
