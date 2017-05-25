package com.rongdu.manage.action.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.model.AccountBankModel;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class ManageAccountBankAction extends BaseAction implements ModelDriven<AccountBankModel> {

	@Resource
	private AccountBankService accountBankservice;

	private final AccountBankModel model = new AccountBankModel();

	private Map<String, Object> data;

	@Override
	public AccountBankModel getModel() {
		return model;
	}

	/**
	 * 用户银行卡列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountbank/accountBankManager")
	public String accountBankManager() throws Exception {
		return "accountBankManager";
	}

	/**
	 * 用户银行卡列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountbank/accountBankList")
	public void accountBankList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String status = paramString("status");
		if(StringUtil.isBlank(status)) {//默认查询未解绑的银行卡
			model.setStatus(1);
		}
		PageDataList<AccountBankModel> accountBankList = accountBankservice.accountBankList(model, pageNumber, pageSize);
		data.put("total", accountBankList.getPage().getTotal()); // 总行数
		data.put("rows", accountBankList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 导出银行卡列表
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountbank/exportExcelAccountBankList")
	public void exportExcelAccountBankList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		
		PageDataList<AccountBankModel> accountBankList = accountBankservice.accountBankList(model, 1, 5000);
		String title = "银行卡列表";
		
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "银行账号", "状态", "所属银行", "添加时间 ", "添加IP" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "bankNo", "statusStr", "bank", "addTime", "addIp"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(accountBankList.getList(), ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 解绑/禁用银行卡
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action("/modules/account/cash/accountbank/disableBank")
	public void disableBank() throws Exception {
		String message = accountBankservice.disable(paramLong("id"));
		if (!"success".equals(message)) {
			printResult(MessageUtil.getMessage("I10019"), false);
		} else {
			printResult(MessageUtil.getMessage("I10009"), true);
		}
	}
}
