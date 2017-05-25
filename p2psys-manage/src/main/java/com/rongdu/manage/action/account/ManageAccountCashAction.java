package com.rongdu.manage.action.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.model.AccountCashBatchModel;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.model.VerifyLogModel;
import com.rongdu.p2psys.core.service.OperationLogService;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 提现
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-20
 */
@SuppressWarnings("rawtypes")
public class ManageAccountCashAction extends BaseAction implements ModelDriven<AccountCashModel> {

	@Resource
	private AccountCashService accountCashService;
	@Resource
	private OperationLogService operationLogService;
	@Resource
	private VerifyLogService verifyLogService;
	
	private AccountCashModel model = new AccountCashModel();

	private Map<String, Object> data;

	public AccountCashModel getModel() {
		return model;
	}

	/**
	 * 提现记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashManager")
	public String cashManager() throws Exception {
		return "cashManager";
	}
	
	/**
	 * 客服提现审核记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyAccountCashList")
	public void verifyAccountCashList() throws Exception {
		data = new HashMap<String, Object>();
		//默认初始查询为全部查询
		String status = this.paramString("status");
		if(StringUtil.isBlank(status)){
			model.setStatus(99);
		}
		PageDataList<AccountCashModel> accountCashList = accountCashService.accountCashList(model);
		data.put("total", accountCashList.getPage().getTotal());
		data.put("rows", accountCashList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/exportExcelAccountCash")
	public void exportExcelAccountCash() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		int status = paramInt("status");
		// 第一页开始
		model.setPage(1);
		// 最多出5000条记录
		model.setRows(5000);
		// 记录取得
		List<AccountCashModel> list = accountCashService.accountCashList(model).getList();

		String title = "";
		if (status == 0) {
			title = "申请提现Excel表";
		}else if (status == 1) {
			title = "提现成功Excel表";
		}else if (status == 2) {
			title = "提现失败Excel表";
		}else if (status == 6) {
			title = "提现初审通过Excel表";
		}else if (status == 7) {
			title = "提现初审不通过Excel表";
		}else if (status == 9) {
			title = "提现复审不通过Excel表";
		}else if (status == 99) {
			title = "提现所有记录Excel表";
		}
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "银行卡尾号", "所属银行","分行名称","所属省","所属市", "提现总额", "到账金额", "手续费", "手续费承担方", "提现时间 ", "状态 ","支付通道" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "bankNo", "bank","branch","province","city", "money", "credited", "fee", "cashFeeBearName", "addTime", "statusStr", "channelName" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	/**
	 * 提现初审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashVerifyManager")
	public String cashVerifyManager() throws Exception {
		return "cashVerifyManager";
	}
	/**
	 * 提现初审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashVerifyManagerNoOnline")
	public String cashVerifyManagerNoOnline() throws Exception {
		return "cashVerifyManagerNoOnline";
	}
	
	/**
	 * 客服提现审核记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/firstVerifyAccountCashList")
	public void firstVerifyAccountCashList() throws Exception {
		data = new HashMap<String, Object>();
		model.setStatus(0);
//		System.out.println(request.getParameter("type"));
//		int type = paramInt("type");
//		model.setType(type);
		PageDataList<AccountCashModel> accountCashList = accountCashService.accountCashList(model);
		data.put("total", accountCashList.getPage().getTotal());
		data.put("rows", accountCashList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 客服提现审核记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/exportExcelCashVerify")
	public void exportExcelCashVerify() throws Exception {
		
		//sfsff
//		data = new HashMap<String, Object>();
//		model.setStatus(0);
//		//sPageDataList<AccountCashModel> accountCashList = accountCashService.accountCashList(model);
//		data.put("total", accountCashList.getPage().getTotal());
//		data.put("rows", accountCashList.getList());
//		printJson(getStringOfJpaObj(data));
		response.setContentType("application/msexcel;charset=UTF-8");                                                                                                                                                                          
		model.setPage(1);                                                                                                                                                                                                                  
		model.setRows(5000);                                                                                                                                                                                                               
		model.setStatus(0);
		List<AccountCashModel> accountCashList = accountCashService.accountCashList(model).getList();                                                                                                                                        
		String title = "提现初审列表";                                                                                                                                                                                                     
		String[] hearders = new String[] { "编号", "流水号","用户名", "真实姓名", "银行卡号", "所属银行","分行名称","所属省","所属市", "提现总额", "到账金额","手续费", "手续费承担方", "提现时间", "状态", "提现方式", "通道类型"};// 表头数组                                                  
		String[] fields = new String[] { "id", "orderNo","userName", "realName", "bankNo", "bank","branch","province","city", "money", "credited", "fee", "cashFeeBear", "addTime", "status", "cashType", "channelName"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(accountCashList, ExcelUtils.createTableHeader(hearders), fields);                                                                                                                                   
		JsGridReportBase report = new JsGridReportBase(request, response);                                                                                                                                                                 
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	/**
	 * 客服提现审核页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/kfVerifyCashEditPage")
	public String kfVerifyCashEditPage() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		saveToken("verifyCashToken");
		List<VerifyLogModel> list = verifyLogService.list("accountCash", model.getId());
		request.setAttribute("verifyLogList", list);
		request.setAttribute("accountCash", accountCash);
		request.setAttribute("user", accountCash.getUser());
		return "kfVerifyCashEditPage";
	}
	
	/**
	 * 提现初审
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyCashStatus")
	public void verifyCashStatus() throws Exception {
		checkToken("verifyCashToken");
		accountCashService.verifyCashStatus(model, getOperator());
		printResult("操作成功！", true);
	}
	
	/**
	 * 提现复审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashVerifyFullManager")
	public String cashVerifyFullManager() throws Exception {
		return "cashVerifyFullManager";
	}
	/**
	 * 提现复审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashVerifyFullManagerNoOnline")
	public String cashVerifyFullManagerNoOnline() throws Exception {
		return "cashVerifyFullManagerNoOnline";
	}
	
	/**
	 * 提现复审记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/accountCashVerifyFullList")
	public void accountCashVerifyFullList() throws Exception {
		//默认初始查询为全部查询
		data = new HashMap<String, Object>();
//		int type = paramInt("type");
//		model.setType(type);
		PageDataList<AccountCashModel> accountCashList = accountCashService.accountCashVerifyFullList(model);
		data.put("total", accountCashList.getPage().getTotal());
		data.put("rows", accountCashList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 提现复审审核页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashVerifyFullEditPage")
	public String cashVerifyFullEditPage() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		saveToken("verifyCashFullToken");
		request.setAttribute("accountCash", accountCash);
		request.setAttribute("user", accountCash.getUser());
		List<VerifyLogModel> list = verifyLogService.list("accountCash", model.getId());
		request.setAttribute("verifyLogList", list);
		return "cashVerifyFullEditPage";
	}
	
	/**
	 * 提现复审操作
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cashVerifyFullEdit")
	public void cashVerifyFullEdit() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		model.validKFVerify(accountCash);
		checkToken("verifyCashFullToken");
		if(accountCash.getTransferType()!=null&&accountCash.getTransferType()==1){//线下充值
			accountCashService.verifyOffLineCash(model, getOperator());
		}else{			
			String channelKey = accountCash.getChannelKey();
			if(StringUtil.isBlank(channelKey)){//线下充值
				accountCashService.verifyOffLineCash(model, getOperator());
			}else{				
				if(channelKey.equals("llpay_channel_key")){//连连通道
					accountCashService.verifyLLCash(model, getOperator());
				}else{//银联通道
					accountCashService.verifyYLCash(model, getOperator());
				}
			}
		}
		printResult("操作成功！", true);
	}

	/**
	 * 提现记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/accountCashList")
	public void accountCashList() throws Exception {
		//默认初始查询为全部查询
		String status = paramString("status");
		if(StringUtil.isBlank(status)){
			model.setStatus(99);
		}
		data = new HashMap<String, Object>();
		PageDataList<AccountCashModel> accountCashList = accountCashService.accountCashList(model);
		data.put("total", accountCashList.getPage().getTotal());
		data.put("rows", accountCashList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 客服提现初审列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyAccountCashManager")
	public String verifyAccountCashManager() throws Exception {
		return "verifyAccountCashManager";
	}

	/**
	 * 客服审核操作
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/kfVerifyCashEdit")
	public void kfVerifyCashEdit() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		model.validKFVerify(accountCash);
		checkToken("verifyCashToken");
		accountCash.setStatus(model.getStatus());
		accountCash.setFee(model.getFee());
		accountCash.setCredited(BigDecimalUtil.sub(accountCash.getMoney(), model.getFee()));
		Operator operator = getOperator();
		operator.setRemark(model.getRemark());
		accountCashService.kfVerifyCash(accountCash, operator);
		printResult("操作成功！", true);
	}

	/**
	 * 财务提现审核列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/fullAccountCashManager")
	public String fullAccountCashManager() throws Exception {
		return "fullAccountCashManager";
	}

	/**
	 * 财务提现审核记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/fullAccountCashList")
	public void fullAccountCashList() throws Exception {
		data = new HashMap<String, Object>();
		 if(model.getStatus()==0){
			 model.setStatus(5); //客服初审通过
		 }
		PageDataList<AccountCashModel> accountCashList = accountCashService.accountCashList(model);
		data.put("total", accountCashList.getPage().getTotal());
		data.put("rows", accountCashList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 财务提现审核页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cwFullCashEditPage")
	public String cwFullCashEditPage() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		saveToken("fullVerifyCashToken");
		request.setAttribute("accountCash", accountCash);
		request.setAttribute("user", accountCash.getUser());
		return "cwFullCashEditPage";
	}

	/**
	 * 财务提现审核操作
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cwFullCashEdit")
	public void cwFullCashEdit() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		model.validCWVerify(accountCash);
		checkToken("fullVerifyCashToken");
		accountCash.setStatus(model.getStatus());
		accountCash.setFee(model.getFee());
		accountCash.setCredited(BigDecimalUtil.sub(accountCash.getMoney(), model.getFee()));
		Operator operator = getOperator();
		operator.setRemark(model.getRemark());
		accountCashService.cwVerifyCash(accountCash, operator);
		printResult("操作成功！", true);
	}
	
	/**
	 * 提现审核
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyCashEdit")
	public void verifyCashEdit() throws Exception {
		AccountCash accountCash = accountCashService.find(model.getId());
		model.validKFVerify(accountCash);
		checkToken("verifyCashToken");
		String channelKey = accountCash.getChannelKey();
		if(channelKey.equals("llpay_channel_key")){//连连通道
			accountCashService.verifyLLCash(model, getOperator());
		}else{//银联通道
			accountCashService.verifyYLCash(model, getOperator());
		}
		printResult("操作成功！", true);
	}
	
	/**
	 * 取消提现
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/cancelCash")
	public void cancelCash() throws Exception {
		AccountCash cash = accountCashService.find(model.getId());
		
		accountCashService.doCancleCash(cash, getOperator());
		printResult("取消提现操作成功！", true);
	}
	/**
	 * 提现审核
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyCash")
	public void verifyCash() throws Exception {
		AccountCash cash = accountCashService.find(model.getId());
		String channelKey = cash.getChannelKey();
		if(channelKey.equals("llpay_channel_key")){//连连通道
			accountCashService.verifyLLCash(model, getOperator());
		}else{//银联通道
			accountCashService.verifyYLCash(model, getOperator());
		}
		printResult("确认提现操作成功！", true);
	}
	
	/**
	 * 提现批量初审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyBatchPage")
	public String verifyBatchPage() throws Exception {
		saveToken("verifyCashToken");
		String[] ids = request.getParameterValues("ids");
		List<AccountCash> list = accountCashService.list(ids[0]);
		request.setAttribute("idStr", ids[0]);
		request.setAttribute("list", list);
		return "verifyBatchPage";
	}
	
	/**
	 * 提现批量初审
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyBatch")
	public void verifyBatch() throws Exception {
		checkToken("verifyCashToken");
		//dsfsf
		String ids = paramString("ids");
		List<AccountCash> list = accountCashService.list(ids);
		//accountCashService.verifyCashStatus(model, getOperator());
		Map<String, Object> map = accountCashService.verifyCashBatchStatus(model, getOperator(), list);
		List<AccountCashBatchModel> batchList = (List<AccountCashBatchModel>) map.get("batchList");
		String message = "操作成功！";
		if(batchList != null && batchList.size() > 0){
			int errorCount = NumberUtil.getInt(map.get("errorCount").toString());
			int count = list.size();
			message += "批量审核：" + count + "条；<br/>成功：" + (count - errorCount) + "条；<br/>失败：" + errorCount + "条；错误信息如下：<br/>";
			String detail = "";
			for (int i = 0; i < batchList.size(); i++) {
				AccountCashBatchModel batchModel = batchList.get(i);
				detail += "用户：" + batchModel.getUserName() + "，提现订单号：" + batchModel.getOrderNo() + "失败信息：" + batchModel.getMessage() + "<br/>";
			}
			message += detail;
		}
		printResult(message, true);
	}
	
	/**
	 * 提现复审页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyReviewBatchPage")
	public String verifyReviewBatchPage() throws Exception {
		saveToken("verifyCashToken");
		String[] ids = request.getParameterValues("ids");
		List<AccountCash> list = accountCashService.list(ids[0]);
		request.setAttribute("idStr", ids[0]);
		request.setAttribute("list", list);
		return "verifyReviewBatchPage";
	}
	
	/**
	 * 提现批量复审
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/verifyReviewBatch")
	public void verifyReviewBatch() throws Exception {
		checkToken("verifyCashToken");
		String ids = paramString("ids");
		List<AccountCash> list = accountCashService.list(ids);
		Map<String, Object> map = accountCashService.verifyCashBatchReview(model, getOperator(), list);
		List<AccountCashBatchModel> batchList = (List<AccountCashBatchModel>) map.get("batchList");
		String message = "操作成功！";
		if(batchList != null && batchList.size() > 0){
			int errorCount = NumberUtil.getInt(map.get("errorCount").toString());
			int count = list.size();
			message += "批量审核：" + count + "条；<br/>成功：" + (count - errorCount) + "条；<br/>失败：" + errorCount + "条；错误信息如下：<br/>";
			String detail = "";
			for (int i = 0; i < batchList.size(); i++) {
				AccountCashBatchModel batchModel = batchList.get(i);
				detail += "用户：" + batchModel.getUserName() + "，提现订单号：" + batchModel.getOrderNo() + "失败信息：" + batchModel.getMessage() + "<br/>";
			}
			message += detail;
		}
		printResult(message, true);
	}
}
