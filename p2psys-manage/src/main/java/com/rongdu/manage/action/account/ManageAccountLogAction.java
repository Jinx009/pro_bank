package com.rongdu.manage.action.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.model.TotalShow;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class ManageAccountLogAction extends BaseAction implements ModelDriven<AccountLogModel> {

	@Resource
	private AccountLogService accountLogService;
	@Resource
	private DictService dictService;
	@Resource
	private AccountRechargeService accountRechargeService;

	private AccountLogModel model = new AccountLogModel();

	private Map<String, Object> data;

	public AccountLogModel getModel() {
		return model;
	}

	/**
	 * 资金记录列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/accountLogManager")
	public String accountLogManager() throws Exception {
		List<Dict> dicts = dictService.list("account_type");
		request.setAttribute("dicts", dicts);
		return "accountLogManager";
	}

	/**
	 * 资金记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/accountLogList")
	public void accountLogList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		PageDataList<AccountLogModel> accountLogList = accountLogService.accountLogList(model, pageNumber, pageSize);
		data.put("total", accountLogList.getPage().getTotal()); // 总行数
		data.put("rows", accountLogList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/exportExcelAccountLog")
	public void exportExcelAccountLog() throws Exception {

		// 记录取得
		List<AccountLogModel> list = accountLogService.accountLogList(model, 1, 5000).getList();

		String title = "资金日志Excel表";

		String[] hearders = new String[] { "编号", "用户名","真实姓名", "类型", "账户总额（元）", "操作金额（元）", "可用金额（元）", "冻结金额（元）", "待收金额（元）", "交易对方名称", "备注", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "typeName", "total", "money", "useMoney", "noUseMoney", "collection", "toUserName", "remark", "addTime" };// 对象属性数组
		//处理资金记录备注
		List<AccountLogModel> list_ = new ArrayList<AccountLogModel>(); 
		for (int i = 0; i < list.size(); i++) {
			AccountLogModel model = list.get(i);
			model.setRemark(model.getDoremark());
			list_.add(model);
		}
		TableData td = ExcelUtils.createTableData(list_, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	//********************************利息管理费 start******************************************
	
	/**
	 * 利息管理费显示页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/manageFeeManager")
	public String manageFeeManager() throws Exception {
		return "manageFeeManager";
	}
	
	/**
	 * 利息管理费显示列表数据
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/manageFeeList")
	public void manageFeeList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		model.setAccountType("manage_fee");//利息管理费
		PageDataList<AccountLogModel> accountLogList = accountLogService.accountLogSingleList(model, pageNumber, pageSize);
		double accountSingleTotal = accountLogService.getAccountSingleTotal(model);
		data.put("total", accountLogList.getPage().getTotal()); // 总行数
		data.put("rows", accountLogList.getList()); // 集合对象
		TotalShow totalShow = new TotalShow();
		totalShow.setTypeName(accountSingleTotal + "元");
		totalShow.setUserName("Total:");
		List<TotalShow> totalShowList = new ArrayList<TotalShow>();
		totalShowList.add(totalShow);
		data.put("footer", totalShowList);
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 利息管理费导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/exportExcelManageFeeAccountLog")
	public void exportExcelManageFeeAccountLog() throws Exception {
		model.setAccountType("manage_fee");
		// 记录取得
		List<AccountLogModel> list = accountLogService.accountLogSingleList(model, 1, 5000).getList();
		
		String title = "资金日志利息管理费Excel表";

		String[] hearders = new String[] { "编号", "用户名", "类型", "账户总额（元）", "操作金额（元）", "可用金额（元）", "冻结金额（元）", "待收金额（元）", "交易对方名称", "备注", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "typeName", "total", "money", "useMoney", "noUseMoney", "collection", "toUserName", "remark", "addTime" };// 对象属性数组
		//处理资金记录备注
		List<AccountLogModel> list_ = new ArrayList<AccountLogModel>(); 
		for (int i = 0; i < list.size(); i++) {
			AccountLogModel model = list.get(i);
			model.setRemark(model.getDoremark());
			list_.add(model);
		}
		TableData td = ExcelUtils.createTableData(list_, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	//********************************利息管理费 end********************************************
	
	//********************************VIP会员费 start******************************************
	
	
	
	//********************************VIP会员费 end********************************************
	
	//********************************红包 start**********************************************
	
	
	
	//********************************红包 end************************************************
	
	//********************************加息劵利息 start******************************************
	
	/**
	 * 加息劵利息显示页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/interestRateCollectManager")
	public String interestRateCollectManager() throws Exception {
		return "interestRateCollectManager";
	}
	
	/**
	 * 加息劵利息显示列表数据
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/interestRateCollectList")
	public void interestRateCollectList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		model.setAccountType("interest_rate_collect");//加息劵利息
		PageDataList<AccountLogModel> accountLogList = accountLogService.accountLogSingleList(model, pageNumber, pageSize);
		double accountSingleTotal = accountLogService.getAccountSingleTotal(model);
		data.put("total", accountLogList.getPage().getTotal()); // 总行数
		data.put("rows", accountLogList.getList()); // 集合对象
		TotalShow totalShow = new TotalShow();
		totalShow.setTypeName(accountSingleTotal + "元");
		totalShow.setUserName("Total:");
		List<TotalShow> totalShowList = new ArrayList<TotalShow>();
		totalShowList.add(totalShow);
		data.put("footer", totalShowList);
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 加息劵利息导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/exportExcelInterestRateCollectAccountLog")
	public void exportExcelInterestRateCollectAccountLog() throws Exception {
		model.setAccountType("interest_rate_collect");
		// 记录取得
		List<AccountLogModel> list = accountLogService.accountLogSingleList(model, 1, 5000).getList();
		
		String title = "资金日志加息劵利息Excel表";

		String[] hearders = new String[] { "编号", "用户名", "类型", "账户总额（元）", "操作金额（元）", "可用金额（元）", "冻结金额（元）", "待收金额（元）", "交易对方名称", "备注", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "typeName", "total", "money", "useMoney", "noUseMoney", "collection", "toUserName", "remark", "addTime" };// 对象属性数组
		//处理资金记录备注
		List<AccountLogModel> list_ = new ArrayList<AccountLogModel>(); 
		for (int i = 0; i < list.size(); i++) {
			AccountLogModel model = list.get(i);
			model.setRemark(model.getDoremark());
			list_.add(model);
		}
		TableData td = ExcelUtils.createTableData(list_, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	//********************************加息劵利息 end********************************************
	
	//********************************浮动收益 start*******************************************
	
	/**
	 * 浮动收益显示页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/floatIncomeManager")
	public String floatIncomeManager() throws Exception {
		return "floatIncomeManager";
	}
	
	/**
	 * 浮动收益显示列表数据
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/floatIncomeList")
	public void floatIncomeList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		model.setAccountType("float_income");//浮动收益
		PageDataList<AccountLogModel> accountLogList = accountLogService.accountLogSingleList(model, pageNumber, pageSize);
		double accountSingleTotal = accountLogService.getAccountSingleTotal(model);
		data.put("total", accountLogList.getPage().getTotal()); // 总行数
		data.put("rows", accountLogList.getList()); // 集合对象
		TotalShow totalShow = new TotalShow();
		totalShow.setTypeName(accountSingleTotal + "元");
		totalShow.setUserName("Total:");
		List<TotalShow> totalShowList = new ArrayList<TotalShow>();
		totalShowList.add(totalShow);
		data.put("footer", totalShowList);
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 浮动收益导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/exportExcelFloatIncomeAccountLog")
	public void exportExcelFloatIncomeAccountLog() throws Exception {
		model.setAccountType("float_income");
		// 记录取得
		List<AccountLogModel> list = accountLogService.accountLogSingleList(model, 1, 5000).getList();
		
		String title = "资金日志浮动收益Excel表";

		String[] hearders = new String[] { "编号", "用户名", "类型", "账户总额（元）", "操作金额（元）", "可用金额（元）", "冻结金额（元）", "待收金额（元）", "交易对方名称", "备注", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "typeName", "total", "money", "useMoney", "noUseMoney", "collection", "toUserName", "remark", "addTime" };// 对象属性数组
		//处理资金记录备注
		List<AccountLogModel> list_ = new ArrayList<AccountLogModel>(); 
		for (int i = 0; i < list.size(); i++) {
			AccountLogModel model = list.get(i);
			model.setRemark(model.getDoremark());
			list_.add(model);
		}
		TableData td = ExcelUtils.createTableData(list_, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	//********************************浮动收益 end*********************************************
	
	/**
	 * 收入支出统计显示页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/accountTotalManager")
	public String accountTotalManager() throws Exception {
		return "accountTotalManager";
	}
	
	/**
	 * 收入支出统计
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/accountTotalSum")
	public void accountTotalSum() throws Exception {
		// 转换JSON字符串用map
		Map<String, Object> data = new HashMap<String, Object>();
		
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		
		AccountRechargeModel arm = new AccountRechargeModel();
		arm.setStartTime(startTime);
		arm.setEndTime(endTime);
		arm = accountRechargeService.sumAmount(arm);
		//充值手续费
		double sumFee = arm.getFee();
		data.put("sumFee", sumFee);
		//充值
		double sumAmountIn = arm.getAmountIn();
		data.put("sumAmountIn", sumAmountIn);
		//平台收入
		double platformIncome = accountLogService.getAccountSingleTotal(startTime, endTime, "'manage_fee'");
		data.put("platformIncome", platformIncome + sumFee + sumAmountIn);
		//利息管理费
		double manageFee = accountLogService.getAccountSingleTotal(startTime, endTime, "'manage_fee'");
		data.put("manageFee", manageFee);
		//平台支出
		double platformSpend = accountLogService.getAccountSingleTotal(startTime, endTime, "'interest_rate_collect', 'float_income'");
		data.put("platformSpend", platformSpend);
		//加息劵利息
		double interestRateCollect = accountLogService.getAccountSingleTotal(startTime, endTime, "'interest_rate_collect'");
		data.put("interestRateCollect", interestRateCollect);
		//浮动收益
		double floatIncome = accountLogService.getAccountSingleTotal(startTime, endTime, "'float_income'");
		data.put("floatIncome", floatIncome);
		
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 投标记录资金日志列表 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/tenderAccountLogManager")
	public String tenderAccountLogManager() throws Exception {
		
		return "tenderAccountLogManager";
	}
	
	/**
	 * 投标记录资金日志
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/tenderAccountLogList")
	public void tenderAccountLogList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		model.setAccountType("tender");//浮动收益
		PageDataList<AccountLogModel> accountLogList = accountLogService.accountLogSingleList(model, pageNumber, pageSize);
		double accountSingleTotal = accountLogService.getAccountSingleTotal(model);
		data.put("total", accountLogList.getPage().getTotal()); // 总行数
		data.put("rows", accountLogList.getList()); // 集合对象
		TotalShow totalShow = new TotalShow();
		totalShow.setTypeName(accountSingleTotal + "元");
		totalShow.setUserName("Total:");
		List<TotalShow> totalShowList = new ArrayList<TotalShow>();
		totalShowList.add(totalShow);
		data.put("footer", totalShowList);
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 投标日志导出
	 * @throws Exception
	 */
	@Action("/modules/account/accountlog/exportExcelTenderAccountLogList")
	public void exportExcelTenderAccountLogList() throws Exception {
		model.setAccountType("tender");
		// 记录取得
		List<AccountLogModel> list = accountLogService.accountLogSingleList(model, 1, 5000).getList();
		
		String title = "投标日志Excel表";

		String[] hearders = new String[] { "编号", "用户名", "类型", "账户总额（元）", "操作金额（元）", "可用金额（元）", "冻结金额（元）", "待收金额（元）", "交易对方名称", "备注", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "typeName", "total", "money", "useMoney", "noUseMoney", "collection", "toUserName", "remark", "addTime" };// 对象属性数组
		//处理资金记录备注
		List<AccountLogModel> list_ = new ArrayList<AccountLogModel>(); 
		for (int i = 0; i < list.size(); i++) {
			AccountLogModel model = list.get(i);
			model.setRemark(model.getDoremark());
			list_.add(model);
		}
		TableData td = ExcelUtils.createTableData(list_, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
}
