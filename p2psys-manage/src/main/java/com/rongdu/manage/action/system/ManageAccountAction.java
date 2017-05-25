package com.rongdu.manage.action.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinapnrHelper;
import com.rongdu.p2psys.user.domain.User;

/**
 * 用户账户管理
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-22
 */
@SuppressWarnings("rawtypes")
public class ManageAccountAction extends BaseAction implements ModelDriven<AccountModel> {

	@Resource
	private AccountService accountService;
	@Resource
	private OrderService cfOrderService;
	
	private AccountModel model = new AccountModel();

	public AccountModel getModel() {
		return model;
	}
	private static Logger logger = Logger.getLogger(ManageAccountAction.class);
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 用户账户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/account/accountManager")
	public String userCreditManager() throws Exception {
		return "accountManager";
	}
	/**
	 * 用户账户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/account/accountUserLog")
	public String userAccountManager() throws Exception {
		return "accountUserLog";
	}

	/**
	 * 用户账户列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/account/accountList")
	public void accountList() throws Exception {
		PageDataList<AccountModel> pageList = accountService.list(model);
		if(null!=pageList){
			List<AccountModel> list = pageList.getList();
			if(null!=list){
				List<AccountModel> res = new ArrayList<AccountModel>();
				for(AccountModel am:list){
					AccountModel account = am;
					account.setCollection(cfOrderService.getInvestMoney(am.getUser().getUserId()));
					res.add(account);
				}
				pageList.setList(res);
			}
		}
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}
	/**
	 * 用户账户列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/account/userAccountList")
	public void userAccountList() throws Exception {
		PageDataList<AccountModel> pageList = accountService.userList(model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}
	/**
	 * 用户账户列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/account/exportExcelAccountList")
	public void exportExcelAccountList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		// 第一页开始
		model.setPage(1);
		// 最多出5000条记录
		model.setRows(5000);
		// 记录取得
//		List<AccountModel> list = accountService.list(model).getList();
		List<AccountModel> list = accountService.exportUserList(model).getList();

		String title = "用户资金流水Excel表";

		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "账户总额（元）", "可用金额（元）", "冻结总额（元）", "待收总额（元）" };// 表头数组
		String[] fields = new String[] { "id", "username", "realname", "total", "useMoney", "noUseMoney", "collection" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	
	/**
	 * 查询用户第三方资金
	 * @throws Exception
	 */
	@Action("/modules/account/account/queryBalance")
	public String queryBalance() throws Exception {
		long userId = paramLong("id");
		Account account = accountService.findByUser(userId);
		User user = account.getUser();
		/*
		user.getApiStatus();
		if (user.getApiStatus() == 0) {
			printWebResult(MessageUtil.getMessage("MG20001F"), false);
		} else {
			TPPWay way = TPPFactory.getTPPWay(null, user, null, null, null);
			map = (Map<String, Object>) way.getTppAccount(user);
			request.setAttribute("user", user);
			request.setAttribute("account", account);
			request.setAttribute("map", map);
		}
		*/
		return "queryBalance";
	}
	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/account/exportExcelAccount")
	public void exportExcelAccountCash() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");

		// 第一页开始
		model.setPage(1);
		// 最多出5000条记录
		model.setRows(5000);
		model.setSort("id");
		model.setOrder("asc");
		// 记录取得
//		List<AccountModel> list = accountService.list(model).getList();
		List<AccountModel> list = accountService.exportList(model).getList();

		String title = "用户资金Excel表";

		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "账户总额（元）", "可用金额（元）", "冻结总额（元）", "待收总额（元）" };// 表头数组
		String[] fields = new String[] { "id", "username", "realname", "total", "useMoney", "noUseMoney", "collection" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	/**
	 * 对账查询页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/account/reconciliationQueryManager")
	public String reconciliationQueryManager() throws Exception {
		
		return "reconciliationQueryManager";
	}
	
	@Action("/modules/account/account/reconciliationQuery")
	public void reconciliationQuery() throws Exception {
		String startTime = paramString("startTime");
		if(StringUtils.isBlank(startTime)){
			startTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
		}else{
			startTime = startTime.replace("-", "");
		}
		UnionPay pay = new UnionPay(2, 1, startTime);
		SignHelper.checkOrders(pay, request, response, startTime);
	}

	/**
	 * 汇付查询类接口
	 * @return
	 */
	@Action("/modules/account/account/huifuQueryApiTest")
	public String huifuQueryApiTest(){
		String usrCustId = paramString("usrCustId");
		String ordId = paramString("ordId");
		String ordDate = paramString("ordDate");
		String querytransType = paramString("querytransType");
		String beginDate = paramString("beginDate");
		String endDate = paramString("endDate");
		logger.info("余额查询 (后台)");
		ChinapnrHelper.queryBalanceBg(usrCustId); //usrCustId
		logger.info("商户子账户信息查询");
		ChinapnrHelper.queryAcctschin();
		logger.info("交易状态查询");
		ChinapnrHelper.queryTransStat(ordId, ordDate, querytransType); //ordId,ordDate,querytransType
		logger.info("商户扣款对账");
		ChinapnrHelper.trfReconciliation(beginDate, endDate, "1", "20");//beginDate,endDate,pageNum,pageSize
		logger.info("投标对账");
		ChinapnrHelper.reconciliationchin(beginDate, endDate, "1", "20", querytransType);//beginDate, endDate, pageNum, pageSize, queryTransType
		logger.info("取现对账");
		ChinapnrHelper.cashReconciliationchin(beginDate, endDate, "1", "20");//beginDate, endDate, pageNum, pageSize
		logger.info("充值对账");
		ChinapnrHelper.saveReconchin(beginDate, endDate, "1", "20");//beginDate, endDate, pageNum, pageSize
		return null;
	}
}
