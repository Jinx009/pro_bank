package com.rongdu.manage.action.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.TppGlodLogService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.TotalShow;
import com.rongdu.p2psys.core.model.VerifyLogModel;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.service.OperationLogService;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 线下充值管理
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-22
 */
@SuppressWarnings("rawtypes")
public class ManageAccountRechargeAction extends BaseAction implements ModelDriven<AccountRechargeModel> {

	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private DictService dictService;
	@Resource
	private UserService userService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private OperationLogService operationLogService;
	@Resource
	private TppGlodLogService tppGlodLogService;
	@Resource
	private ChinapnrService chinapnrService;

	private AccountRechargeModel model = new AccountRechargeModel();
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 充值记录列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeManager")
	public String accountRechargeManager() throws Exception {
		return "accountRechargeManager";
	}

	/**
	 * 充值记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeList")
	public void accountRechargeList() throws Exception {
		String status = paramString("status");
		if (StringUtil.isBlank(status)) {
			model.setStatus(99);
		}
		model.setType(99); // 标记为后台审核充值
		PageDataList<AccountRechargeModel> pageList = accountRechargeService.list(model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 充值记录初审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeVerifyManager")
	public String accountRechargeVerifyManager() throws Exception {
		return "accountRechargeVerifyManager";
	}
	
	/**
	 * 充值记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeVerifyList")
	public void accountRechargeVerifyList() throws Exception {
		model.setStatus(0);
		model.setType(3); // 标记为后台审核充值
		PageDataList<AccountRechargeModel> pageList = accountRechargeService.list(model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 充值管理-初审操作页面
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeVerifyEditPage")
	public String accountRechargeVerifyEditPage() throws Exception {
		AccountRecharge accountRecharge = accountRechargeService.find(model.getId());
		saveToken("verifyRechargeVerifyToken");
		request.setAttribute("accountRecharge", accountRecharge);
		request.setAttribute("user", accountRecharge.getUser());
		List<VerifyLogModel> list = verifyLogService.list("verifyAccountRecharge", model.getId());
		request.setAttribute("verifyLogList", list);
		return "accountRechargeVerifyEditPage";
	}
	
	/**
	 * 充值管理-审核操作
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeVerifyEdit")
	public void accountRechargeVerifyEdit() throws Exception {
		checkToken("verifyRechargeVerifyToken");
//		model.validVerifyAccountRecharge();
		accountRechargeService.accountRechargeVerifyEdit(model, getOperator());
		printResult("操作成功！", true);
	}
	
	/**
	 * 充值记录复审页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeVerifyFullManager")
	public String accountRechargeVerifyFullManager() throws Exception {
		List<VerifyLogModel> list = verifyLogService.list("verifyAccountRecharge", model.getId());
		request.setAttribute("verifyLogList", list);
		return "accountRechargeVerifyFullManager";
	}
	
	/**
	 * 充值记录复审List
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeVerifyFullList")
	public void accountRechargeVerifyFullList() throws Exception {
		PageDataList<AccountRechargeModel> pageList = accountRechargeService.accountRechargeVerifyFullList(model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 充值管理-审核操作页面
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeEditPage")
	public String accountRechargeEditPage() throws Exception {
		AccountRecharge accountRecharge = accountRechargeService.find(model.getId());
		saveToken("verifyRechargeToken");
		request.setAttribute("accountRecharge", accountRecharge);
		request.setAttribute("user", accountRecharge.getUser());
		List<VerifyLogModel> list = verifyLogService.list("verifyAccountRecharge", model.getId());
		request.setAttribute("verifyLogList", list);
		return "accountRechargeEditPage";
	}

	/**
	 * 充值管理-审核操作
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeEdit")
	public void accountRechargeEdit() throws Exception {
		checkToken("verifyRechargeToken");
		model.validVerifyAccountRecharge();
		accountRechargeService.verifyAccountRecharge(model, getOperator());
		printResult("操作成功！", true);
	}


	 /** 添加线下充值页面
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeAddPage")
	public String accountRechargeAddPage() throws Exception {
		saveToken("rechargeToken");
		List<Dict> dictList = dictService.list("offline_recharge_type");
		request.setAttribute("dictList", dictList);
		return "accountRechargeAddPage";
	}

	/**
	 * 添加线下充值
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeAdd")
	public void accountRechargeAdd() throws Exception {
		model.validAccountRecharge();
		checkToken("rechargeToken");
		User user = userService.getUserByUserName(model.getUserName());
		if (user == null) {
			printResult("该用户名不存在！", false);
		} else {
			AccountRecharge recharge = new AccountRecharge(user, model.getMoney(),
					"back_recharge", model.getType(), model.getRemark());
			recharge = accountRechargeService.add(recharge);
			VerifyLog verifyLog = new VerifyLog("accountRecharge", recharge.getId());
			verifyLog.setRemark(getOperator().getUserName() + "（" + this.getRequestIp() + "）" + "的操作员申请后台线下充值"
					+ user.getUserName() + " " + model.getMoney() + "元成功，等待审核");
			verifyLogService.save(verifyLog);
			printResult("充值成功，等待审核！", true);
		}
	}

	
	public AccountRechargeModel getModel() {
		return model;
	}
	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/exportExcelRecharge")
	public void exportExcelRecharge() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		int status = paramInt("status");
		// 第一页开始
		model.setPage(1);
		// 最多出5000条记录
		model.setRows(5000);
		model.setSort("id");
		model.setOrder("desc");
		// 记录取得
		List<AccountRechargeModel> list = accountRechargeService.list(model).getList();

		String title = "";
		if (status == 0) {
			title = "待审核Excel表";
		}else if (status == 1) {
			title = "充值成功Excel表";
		}else if (status == 2) {
			title = "充值失败Excel表";
		}else if (status == 99) {
			title = "充值所有记录Excel表";
		}
		String[] hearders = new String[] { "编号", "流水号", "用户名", "真实姓名", "充值金额", "到账金额", "手续费金额", "手续费承担方", "充值方式", "线下帐号", "充值时间", "状态", "通道类型","连连支付单号" };// 表头数组
		String[] fields = new String[] { "id", "tradeNo", "userName", "realName", "money", "amountIn", "fee", "rechargeFeeBearName", "typeStr", "payOfflinebankInfo", "addTime", "statusStr", "channelName","oid_paybill"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}	
	
	/**
	 * 取消充值
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/cancelRecharge")
	public void cancelRecharge() throws Exception {
		AccountRecharge ar=accountRechargeService.find(model.getId());
		accountRechargeService.cancelCash(ar);
		OperationLog log = new OperationLog(ar.getUser(), getOperator(), "cancel_cash");
		log.setOperationResult("用户名为" + getOperator().getUserName() + "（" + Global.getIP() + "）的操作员对用户为"
				+ar.getUser().getUserName()+"的充值ID为"+ar.getId()+"）进行取消充值操作");
		operationLogService.save(log);
		printResult("取消充值操作成功！", true);
	}
	/**
	 * 设置充值成功
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/verifyRecharge")
	public void verifyRecharge() throws Exception {
		AccountRecharge ar=accountRechargeService.find(model.getId());
		accountRechargeService.verifyRecharge(ar);
		OperationLog log = new OperationLog(ar.getUser(), getOperator(), "cancel_cash");
		log.setOperationResult("用户名为" + getOperator().getUserName() + "（" + Global.getIP() + "）的操作员对用户为"
				+ar.getUser().getUserName()+"的充值ID为"+ar.getId()+"）进行充值确认操作");
		operationLogService.save(log);
		printResult("确认充值操作成功！", true);
	}
	
	/**
	 * 统计充值记录列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeTotalManager")
	public String accountRechargeTotalManager() throws Exception {
		return "accountRechargeTotalManager";
	}

	/**
	 * 统计 充值记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeTotalList")
	public void accountRechargeTotalList() throws Exception {
		PageDataList<AccountRechargeModel> pageList = accountRechargeService.accountRechargeTotalList(model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		AccountRechargeModel arm = accountRechargeService.sumAmount(model);
		TotalShow totalShow = new TotalShow();
		totalShow.setAmountIn(arm.getAmountIn() + "元");
		totalShow.setFee(arm.getFee() + "元");
		totalShow.setUserName("Total:");
		List<TotalShow> totalShowList = new ArrayList<TotalShow>();
		totalShowList.add(totalShow);
		map.put("footer", totalShowList);
		printJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/exportExcelTotalRecharge")
	public void exportExcelTotalRecharge() throws Exception {
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		model.setPage(pageNumber);
		model.setRows(pageSize);
		List<AccountRechargeModel> list = accountRechargeService.accountRechargeTotalList(model).getList();

		String title = "充值成功Excel表";
		String[] hearders = new String[] { "编号", "流水号", "用户名", "真实姓名", "到账金额", "手续费金额", "充值时间", "状态" };// 表头数组
		String[] fields = new String[] { "id", "tradeNo", "userName", "realName", "money", "fee",  "addTime", "statusStr"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}	
	
}
