package com.rongdu.manage.action.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountDeduct;
import com.rongdu.p2psys.account.model.AccountBackModel;
import com.rongdu.p2psys.account.service.AccountBackService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 线下扣款管理
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-23
 */
@SuppressWarnings("rawtypes")
public class ManageAccountBackAction extends BaseAction implements ModelDriven<AccountBackModel> {

	@Resource
	private UserService userService;
	@Resource
	private AccountBackService accountBackService;
	@Resource
	private AccountService accountService;

	private AccountBackModel model = new AccountBackModel();
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 扣款列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/accountBackManager")
	public String accountBackManager() throws Exception {
		return "accountBackManager";
	}

	/**
	 * 扣款列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/accountBackList")
	public void accountBackList() throws Exception {
		PageDataList<AccountBackModel> pageList = accountBackService.list(model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/exportExcelAccountBack")
	public void exportExcelAccountBack() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		String userName = paramString("userName");
		String realName = paramString("realName");
		int status = paramInt("status");
		List<AccountBackModel> list = accountBackService.find(userName, realName, status);
		String title = "";
		if (status == 0) {
			title = "扣款待审核Excel表";
		} else if (status == 1) {
			title = "扣款成功Excel表";
		}else if (status == 2) {
			title = "扣款失败Excel表";
		}
		String[] hearders = new String[] { "编号", "流水号", "用户名", "真实姓名", "类型", "扣款金额", "备注", "扣款时间", "状态", "审核人" };// 表头数组
		String[] fields = new String[] { "id", "tradeNo", "userName", "realName", "typeName", "moneyString", "remark",
				"addTime", "statusName", "verifyUserName" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 添加扣款页面
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/accountBackAddPage")
	public String accountBackAddPage() throws Exception {
		saveToken("backToken");
		return "accountBackAddPage";
	}

	/**
	 * 添加扣款
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/accountBackAdd")
	public void accountBackAdd() throws Exception {
		User user = userService.getUserByUserName(model.getUserName());
		if (user == null) {
			printResult("该用户名不存在！", false);
			return;
		}
		Account account = accountService.findByUser(user.getUserId());
		if (model.getMoney() <= 0) {
			printResult("扣款金额过小,请重新输入金额！", false);
			return;
		}
		if (model.getMoney() >= 100000000) {
			printResult("你充值的金额过大,目前系统仅支持千万级别的充值！", false);
			return;
		}
		if (model.getMoney() > account.getUseMoney()) {
			printResult("可用金额不足！", false);
			return;
		}
		checkToken("backToken");
		AccountDeduct accountBack = new AccountDeduct(user, model.getMoney(), model.getRemark());
		accountBack = accountBackService.add(accountBack, model.getMoney(), getOperator());
		printResult("扣款成功,等待管理员审核！", true);
	}

	/**
	 * 扣款管理-审核操作页面
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/accountBackEditPage")
	public String accountBackEditPage() throws Exception {
		AccountDeduct accountBack = accountBackService.find(model.getId());
		saveToken("verifyBackToken");
		request.setAttribute("accountBack", accountBack);
		request.setAttribute("user", accountBack.getUser());
		return "accountBackEditPage";
	}

	/**
	 * 扣款管理-审核操作
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountBack/accountBackEdit")
	public void accountBackEdit() throws Exception {
		checkToken("verifyBackToken");
		Account account = accountService.findByUser(paramLong("userId"));
		String msg = model.validAccountBack(account);
		if (StringUtil.isNotBlank(msg)) {
			printResult(msg, false);
			return;
		}
		accountBackService.verifyAccountBack(model, getOperator());
		printResult("操作成功！", true);
	}

	public AccountBackModel getModel() {
		return model;
	}

}
