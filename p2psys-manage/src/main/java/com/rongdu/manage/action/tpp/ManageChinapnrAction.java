package com.rongdu.manage.action.tpp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.TppGlodLog;
import com.rongdu.p2psys.account.service.TppGlodLogService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.ChinapnrTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrNetSave;
import com.rongdu.p2psys.tpp.chinapnr.service.TppPnrPayService;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;
import com.rongdu.p2psys.user.service.UserService;


/**
 * 汇付回调处理类，使用disruptor 前台回调和后台回调都处理业务,处理业务的时候进行判断
 */
@SuppressWarnings("rawtypes")
public class ManageChinapnrAction extends BaseAction {
	
	private static final Logger logger = Logger.getLogger(ManageChinapnrAction.class);
	
	@Resource
	private UserService userService;
	@Resource
	private TppPnrPayService tppPnrPayService;
	@Resource
	private TppGlodLogService tppGlodLogService;
	/**
	 * 需要在页面上打印ordid的回调方法
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value="/public/chinapnr/chinapnrBgRet",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
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
		if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(respCode) && !StringUtil.isBlank(ordId)) {
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
	
	@Action(value="/public/chinapnr/loanAndrepay",interceptorRefs = { @InterceptorRef("defaultStack"),@InterceptorRef("action")})
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
	
	@Action(value="/modules/schedule/chinapnr/scheduleManage")
	public String scheduleManage() {
		return "scheduleManage";
	}
	
	
	@SuppressWarnings("unchecked")
	@Action(value="/modules/schedule/chinapnr/scheduleList")
	public void scheduleList() throws Exception{
		//页面参数
		String ordId = paramString("ordId");
		String status = paramString("status");
		String operatetype = paramString("operateType");
		String cmdid = paramString("cmdid");
		long borrowId = this.paramLong("borrowId");
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		String payUserName = paramString("payUserName");//付款方
		String userName = paramString("userName");//收款方
		Map<String, Object> map = new HashMap<String, Object>();
		if(borrowId > 0) {
			map.put("borrowId", borrowId);
		}
		if(!StringUtil.isBlank(ordId)) {
			map.put("ordId", ordId);
		}
		if(!StringUtil.isBlank(status)) {
			map.put("status", status);
		}
		if(!StringUtil.isBlank(operatetype)) {
			map.put("operateType", operatetype);		
		}
		if (!StringUtil.isBlank(cmdid)) {
			map.put("cmdid", cmdid);
		}
		if(StringUtil.isNotBlank(startTime)){
			map.put("startTime", startTime);
		}
		if(StringUtil.isNotBlank(endTime)){
			map.put("endTime", endTime);
		}
		if(StringUtil.isNotBlank(payUserName)){
			map.put("payUserName", payUserName);
		}
		if(StringUtil.isNotBlank(userName)){
			map.put("userName", userName);
		}
		map.put("page", this.paramInt("page"));
		map.put("rows", this.paramInt("rows"));
		TPPWay way = TPPFactory.getTPPWay();
        PageDataList<TppPnrPay> page = (PageDataList<TppPnrPay>) way.getTppPay(map);
        Map<String, Object> data = new HashMap<String, Object>();
		if(page.getPage() == null){
			data.put("total", 0); // 总行数
		}else {
			data.put("total", page.getPage().getTotal()); // 总行数
		}
		data.put("rows", page.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 导出报表
	 * @throws Exception
	 */
	@Action(value="/modules/schedule/chinapnr/exportExcelSchedule")
	public void exportExcelSchedule() throws Exception {
		//页面参数
		String ordId = paramString("ordId");
		String status = paramString("status");
		String operatetype = paramString("operateType");
		String cmdid = paramString("cmdid");
		long borrowId = this.paramLong("borrowId");
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		String payUserName = paramString("payUserName");//付款方
		String userName = paramString("userName");//收款方
		Map<String, Object> map = new HashMap<String, Object>();
		if(borrowId > 0) {
			map.put("borrowId", borrowId);
		}
		if(!StringUtil.isBlank(ordId)) {
			map.put("ordId", ordId);
		}
		if(!StringUtil.isBlank(status)) {
			map.put("status", status);
		}
		if(!StringUtil.isBlank(operatetype)) {
			map.put("operateType", operatetype);		
		}
		if (!StringUtil.isBlank(cmdid)) {
			map.put("cmdid", cmdid);
		}
		if(StringUtil.isNotBlank(startTime)){
			map.put("startTime", startTime);
		}
		if(StringUtil.isNotBlank(endTime)){
			map.put("endTime", endTime);
		}
		if(StringUtil.isNotBlank(payUserName)){
			map.put("payUserName", payUserName);
		}
		if(StringUtil.isNotBlank(userName)){
			map.put("userName", userName);
		}
		map.put("page", 1);
		map.put("rows", 5000);
		TPPWay way = TPPFactory.getTPPWay();
        PageDataList<TppPnrPay> page = (PageDataList<TppPnrPay>) way.getTppPay(map);
        
	}
	
	@Action(value="/modules/schedule/chinapnr/schedule")
	public void schedule() throws Exception{
		String[] ids = request.getParameterValues("ids");
		if(ids != null && ids.length > 0){			
			List<Object> taskList = new ArrayList<Object>();
			for(int i=0;i<ids.length;i++){
				int id = NumberUtil.getInt(ids[i]);
				logger.info("当前处理" + id);
				if(id!=0){
					TPPWay way = TPPFactory.getTPPWay();
			        TppPnrPay item = (TppPnrPay) way.getTppPayById(id);
					taskList.add(item);
				}
			}
			TPPWay way = TPPFactory.getTPPWay();
	        way.doTppPayTask(taskList);
		}
		printSuccess();
	}
	
	/**
	 * 平台充值同步回调
	 * @throws Exception  
	 */
	@Action(value="/modules/schedule/chinapnr/glodRechargeReturn")	
	public String glodRechargeReturn() throws Exception {
		logger.info("平台充值页面同步回调--------start------");
		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		this.dealChinapnrWebNetSave(resultFlag);
		logger.info("平台充值页面同步回调--------end------");
		printWebResult(MessageUtil.getMessage("I10009"), true);
		return null;
	}
	
	/**
	 * 平台充值异步回调
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/schedule/chinapnr/glodRechargeNotify")
	public String glodRechargeNotify() throws Exception {
		logger.info("平台充值异步回调--------start");
		ChinapnrNetSave netSave = this.dealChinapnrWebNetSave(null);
		printTrxId(netSave.getTrxId());
		logger.info("平台充值异步回调--------end");
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
	private ChinapnrNetSave dealChinapnrWebNetSave(String resultFlag)
			throws Exception {
		logger.info("进入平台充值回调:" + getRequestParams());
		ChinapnrNetSave netSave = this.chpnrNatSaveCallback();
		int ret = netSave.callback(); // 验签操作
		logger.info("用户 充值验签结果：" + ret);
		if (ret == 0) { // 校验参数
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(netSave.getRespCode())) {
				logger.info("用户充值，汇付处理成功，进入本地处理……注：成功失败都在后台处理");
				logger.info("订单号：" + netSave.getOrdId() + " 金额："
						+ netSave.getTransAmt() + " 结果："
						+ netSave.getRespDesc() + " 流水号：" + netSave.getTrxId());

				//查找记录
				TppGlodLog tppGlodLog = tppGlodLogService.findByOrdId(NumberUtil.getLong(netSave.getOrdId()));
				//判断是否已被处理
				if (tppGlodLog.getStatus() == 1) {
					logger.info("平台充值订单已处理");
					return null;
				}
				tppGlodLog.setStatus((byte) 1);
				tppGlodLog.setMemo(netSave.getRespDesc());
				tppGlodLog.setFee(Double.parseDouble(netSave.getFeeAmt()));
				tppGlodLogService.update(tppGlodLog);

				logger.info("充值回调信息：金额" + netSave.getTransAmt() + "费率："
						+ netSave.getFeeAmt() + "扣款账户："
						+ netSave.getFeeCustId() + "扣款子账户："
						+ netSave.getFeeAcctId());
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
	
	/**
	 * 平台取现同步回调
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/schedule/chinapnr/glodCashReturn")
	public String glodCashReturn() throws Exception {
		logger.info("平台提现页面同步回调-------start");
		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		this.dealChinapnrCash(resultFlag);
		printWebResult(MessageUtil.getMessage("I10009"), true);
		logger.info("平台提现页面同步回调-------end");
		return null;
	}
	
	/**
	 * 平台取现异步回调
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/schedule/chinapnr/glodCashNotify")
	public String glodCashNotify() throws Exception {
		logger.info("平台提现页面异步回调-------start");
		ChinapnrCashOut cash = dealChinapnrCash(null);
		printTrxId(cash.getOrdId());
		logger.info("平台提现页面异步回调-------end");
		return null;
	}
	
	/**
	 * 取现业务逻辑处理
	 * 
	 * @param type
	 * @param parm
	 * @return
	 */
	private ChinapnrCashOut dealChinapnrCash(String resultFlag) throws Exception {
		logger.info("进入取现回调…………" + getRequestParams());
		ChinapnrCashOut cash = this.cashcall();
		int ret = cash.callback();// 验签操作
		if (ret == 0) {
			if (ChinapnrTPPWay.RESP_CODE_SUCC.equals(cash.getRespCode())) {
				logger.info("取现汇付处理成功，进入本地处理…………" + cash.getOrdId() + "-------");
				//查找记录
				TppGlodLog tppGlodLog = tppGlodLogService.findByOrdId(NumberUtil.getLong(cash.getOrdId()));
				//判断是否已被处理
				if (tppGlodLog.getStatus() == 1) {
					logger.info("平台提现订单已处理");
					return null;
				}
				tppGlodLog.setStatus((byte) 1);
				tppGlodLog.setMemo(cash.getRespDesc());
				tppGlodLog.setFee(Double.parseDouble(cash.getFeeAmt()));
				tppGlodLogService.update(tppGlodLog);
			} else {
				logger.info("取现失败，失败原因：" + URLDecoder.decode(cash.getRespDesc(), "utf-8"));
				throw new BussinessException(MessageUtil.getMessage("I40003"), 2);
			}
		} else {
			logger.info("取现验签失败   " + "orderId:" + cash.getOrdId());
			throw new BussinessException(MessageUtil.getMessage("I40003"), 2);
		}
		return cash;
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
	
	/******************以下为共用方法*****************/

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
			p.flush();
			p.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
}
