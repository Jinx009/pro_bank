package com.rongdu.manage.action.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.TppGlodLog;
import com.rongdu.p2psys.account.model.TppGlodLogModel;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.TppGlodLogService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrAccts;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrNetSave;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinaPnrType;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinapnrHelper;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 平台账户
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年2月4日
 */
public class ManageTppGlodLogAction extends BaseAction<TppGlodLogModel>
		implements ModelDriven<TppGlodLogModel> {
	@Resource
	private TppGlodLogService tppGlodLogService;
	@Resource
	private UserService userService;
	@Resource
	private AccountRechargeService accountRechargeService;
	
	private TppGlodLogModel model = new TppGlodLogModel();
	
	public TppGlodLogModel getModel(){
		return model;
	}
	
	private Map<String, Object> data;
	
	private static Logger logger = Logger.getLogger(ManageTppGlodLogAction.class);

	/**
	 * 平台资金管理页面
	 * 
	 * @return
	 */
	@Action("/modules/account/tppGlodLog/glodLogManager")
	public String glodLogManager() throws Exception {
		return "glodLogManager";
	}

	/**
	 * 平台资金充值页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/glodRechargePage")
	public String glodRechargePage() throws Exception {
		saveToken("rechargeToken");
		return "glodRechargePage";
	}

	/**
	 * 金账户充值
	 * 
	 * @return
	 */
	@Action(value = "/modules/account/tppGlodLog/glodRecharge", results = { @Result(name = "huifu", type = "ftl", location = "/modules/tpp/netSave.html") })
	public String glodRecharge() throws Exception {
		model.validGlodRecharge();
		checkToken("rechargeToken");
		String custId = Global.getValue("tpp_cust_id");
		String ordId = OrderNoUtils.getSerialNumber();
		String ordDate = DateUtil.dateStr7(new Date());
		String web_type = "recharge";

		// 保存平台充值信息
		TppGlodLog glodLog = new TppGlodLog(getOperator(), "1", "1",
				model.getMoney(), 0, (byte) 0, ChinaPnrType.RECHARGE, ordId,
				"待处理");
		tppGlodLogService.save(glodLog);

		// 提交汇付处理
		ChinapnrNetSave save = new ChinapnrNetSave(ordId, ordDate, "C",
				BaseTPPWay.formatMoney(model.getMoney()), custId);
		save.setRetUrl(Global.getValue("adminurl") + "/modules/schedule/chinapnr/glodRechargeReturn.html");
		save.setBgRetUrl(Global.getValue("adminurl") + "/modules/schedule/chinapnr/glodRechargeNotify.html");
		save.setMerPriv(getOperatorId() + "," + web_type);
		try {
			save.sign();
		} catch (Exception e) {
			logger.error("生成签名失败！" + e);
		}
		request.setAttribute("pnr", save);
		return "huifu";
	}

	/**
	 * 平台账户间转账页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/glodTransferPage")
	public String glodTransferPage() throws Exception {
		List<ChinapnrAccts> accts = this.getAccts();
		request.setAttribute("accts", accts);
		return "glodTransferPage";
	}

	/**
	 * 获取平台商户子账户信息
	 * @return
	 */
	public List<ChinapnrAccts> getAccts() throws Exception {
		// 获取商户子账户信息
		String resp = ChinapnrHelper.queryAcctschin().submit();

		// 截取商户子账户信息Json字符串
		String jsonStr = resp.substring(resp.indexOf("["),
				resp.lastIndexOf("]") + 1);

		// 解析Json字符串并封装子账户信息对象
		JSONArray array = JSON.parseArray(jsonStr);
		List<ChinapnrAccts> accts = new ArrayList<ChinapnrAccts>();
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject object = array.getJSONObject(i);
				ChinapnrAccts chinapnrAccts = new ChinapnrAccts();
				chinapnrAccts.setAcctBal(object.getDouble("AcctBal"));
				chinapnrAccts.setAcctType(object.getString("AcctType"));
				chinapnrAccts.setAvlBal(object.getDouble("AvlBal"));
				chinapnrAccts.setFrzBal(object.getDouble("FrzBal"));
				chinapnrAccts.setSubAcctId(object.getString("SubAcctId"));
				accts.add(chinapnrAccts);
			}
		}
		return accts;
	}
	
	/**
	 * 账户间互转
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/glodTransfer")
	public void glodTransfer() throws Exception {
		if (StringUtil.isBlank(model.getAccount())) {
			printWebResult("付款账户不能为空", false);
			return;
		}
		if (StringUtil.isBlank(model.getPayAccount())) {
			printWebResult("收款账户不能为空", false);
			return;
		}
		if (model.getMoney() <= 0) {
			printWebResult("转账金额有误", false);
			return;
		}
		// 封装操作记录信息
		String ordId = OrderNoUtils.getSerialNumber();
		TppGlodLog glodLog = new TppGlodLog(getOperator(), model.getAccount(), model.getPayAccount(),
				model.getMoney(), 0, (byte) 0, ChinaPnrType.TRANSFER, ordId,"待处理");
		// 提交汇付处理
		ChinapnrModel chinapnrModel = ChinapnrHelper.transfer(
				BaseTPPWay.formatMoney(model.getMoney()),
				Global.getValue("tpp_cust_id"), ordId, glodLog.getPayAccount(),
				glodLog.getAccount());
		if ("000".equals(chinapnrModel.getRespCode())) {// 成功
			glodLog.setStatus((byte) 1);
			glodLog.setMemo(chinapnrModel.getRespDesc());
			glodLog.setOrdId(chinapnrModel.getOrdId());
			tppGlodLogService.save(glodLog);
			printWebResult(MessageUtil.getMessage("I10009"), true);
		} else {
			printWebResult(chinapnrModel.getRespDesc(), false);
		}
	}
	
	/**
	 * 平台账户提现页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/glodCashPage")
	public String glodCashPage() throws Exception {
		return "glodCashPage";
	}

	/**
	 * 平台账户提现
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/account/tppGlodLog/glodCash", results = { @Result(name = "huifu", type = "ftl", location = "/modules/tpp/cash.html") })
	public String glodCash() throws Exception {
		//判断提现金额
		if(model.getMoney() <= 0){
			printWebResult("提现金额有误", false);
			return null;
		}
		//生产订单号
		String ordId = OrderNoUtils.getSerialNumber();
		
		// 保存平台提现信息
		TppGlodLog glodLog = new TppGlodLog(getOperator(), "1", "1",
				model.getMoney(), 0, (byte) 0, ChinaPnrType.CASH, ordId,
				"待处理");
		tppGlodLogService.save(glodLog);
		//封装提现信息
		//提现用户客户号为平台
		ChinapnrCashOut cashOut = new ChinapnrCashOut(Global.getValue("tpp_cust_id"));
		cashOut.setVersion("20");
		cashOut.setOrdId(ordId);
		cashOut.setTransAmt(BaseTPPWay.formatMoney(model.getMoney()));
		cashOut.setServFee("");
		cashOut.setServFeeAcctId("");
		cashOut.setMerPriv("");
		cashOut.setRetUrl(Global.getValue("adminurl") + "/modules/schedule/chinapnr/glodCashReturn.html");
		cashOut.setBgRetUrl(Global.getValue("adminurl") + "/modules/schedule/chinapnr/glodCashNotify.html");
		try {
			cashOut.sign();
		} catch (Exception e) {
			logger.error("生成签名失败！" + e);
		}
		request.setAttribute("pnr", cashOut);
		return "huifu";
	}
	
	/**
	 * 平台账户操作记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/glodLogList")
	public void glodLogList() throws Exception {
		String status = paramString("status");
		if(StringUtil.isBlank(status)){
			model.setStatus((byte)99);
		}
		PageDataList<TppGlodLogModel> pageList = tppGlodLogService.list(model);
		int totalPage = pageList.getPage().getTotal();
		data = new HashMap<String, Object>();
		data.put("total", totalPage);
		data.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 平台给用户充值页面
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/webRechargePage")
	public String webRechargePage() throws Exception {
		List<ChinapnrAccts> accts = this.getAccts();
		request.setAttribute("accts", accts);
		return "webRechargePage";
	}
	
	/**
	 * 平台给用户充值
	 * @throws Exception
	 */
	@Action("/modules/account/tppGlodLog/webRecharge")
	public void webRecharge() throws Exception {
		if (StringUtil.isBlank(model.getAccount())) {
			printWebResult("付款账户不能为空", false);
			return;
		}
		if (StringUtil.isBlank(model.getPayAccount())) {
			printWebResult("收款账户不能为空", false);
			return;
		}
		User user = userService.getUserByUserName(model.getPayAccount());
		if(user == null){
			printWebResult("收款账户不存在", false);
			return;
		}
		// TODO zjj
		/*
		if(user.getApiStatus() != 1){
			printWebResult("收款账户没有开户", false);
			return;
		}
		*/
		if (model.getMoney() <= 0) {
			printWebResult("转账金额有误", false);
			return;
		}
		// 封装操作记录信息
		String ordId = OrderNoUtils.getSerialNumber();
		TppGlodLog glodLog = new TppGlodLog(getOperator(), model.getAccount(), model.getPayAccount(),
				model.getMoney(), 0, (byte) 0, ChinaPnrType.WEBRECHARGE, ordId,"待处理");
		// 提交汇付处理
		ChinapnrModel chinapnrModel = ChinapnrHelper.transfer(
				BaseTPPWay.formatMoney(model.getMoney()),
				/*user.getApiId()*/"", ordId, "",model.getAccount());
		if ("000".equals(chinapnrModel.getRespCode())) {// 成功
			glodLog.setStatus((byte) 1);
			glodLog.setMemo(chinapnrModel.getRespDesc());
			glodLog.setOrdId(chinapnrModel.getOrdId());
			tppGlodLogService.save(glodLog);
			
			//保存充值记录
			AccountRecharge recharge = new AccountRecharge(user, NumberUtil.getDouble(chinapnrModel.getTransAmt()),
					"back_recharge", 4, "平台充值");
			recharge.setRechargeFeeBear((byte)1);//平台垫付
			recharge.setStatus(1);
			recharge.setAmountIn(NumberUtil.getDouble(chinapnrModel.getTransAmt()));
			accountRechargeService.saveBackRecharge(recharge, getOperator());
			
			printWebResult(MessageUtil.getMessage("I10009"), true);
		} else {
			printWebResult(chinapnrModel.getRespDesc(), false);
		}
	}
}
