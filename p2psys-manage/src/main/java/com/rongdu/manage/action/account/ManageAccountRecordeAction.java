package com.rongdu.manage.action.account;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountRecorde;
import com.rongdu.p2psys.account.model.AccountRecordeModel;
import com.rongdu.p2psys.account.service.AccountRecordeService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 资金汇总记录
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月4日
 */
public class ManageAccountRecordeAction extends BaseAction<AccountRecordeModel>
		implements ModelDriven<AccountRecordeModel> {
	@Resource
	private AccountRecordeService accountRecordeService;
	
	private AccountRecordeModel model = new AccountRecordeModel();
	
	public AccountRecordeModel getModel() {
		return model;
	}
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * 资金汇总记录页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecorde/accountRecordeManager")
	public String accountRecordeManager() throws Exception {
		
		return "accountRecordeManager";
	}
	
	/**
	 * 资金汇总记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecorde/accountRecordeList")
	public void accountRecordeList() throws Exception {
		PageDataList<AccountRecorde> pageList = accountRecordeService.pageDataList(model);
		int totalPage = pageList.getPage().getTotal();
		data.put("total", totalPage);
		data.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecorde/exportExcleAccountRecordeList")
	public void exportExcleAccountRecordeList() throws Exception {
		model.setPage(1);
		model.setRows(5000);
		PageDataList<AccountRecorde> pageList = accountRecordeService.pageDataList(model);
		
		String title = "资金汇总Excel表";

		String[] hearders = new String[] { "编号", "账户总额", "可用总额", "冻结总额", "待收总额", "统计时间"};// 表头数组
		String[] fields = new String[] { "id", "total", "useMoney", "noUseMoney", "collection", "addTime"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(pageList.getList(), ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
}
