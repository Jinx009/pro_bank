package com.rongdu.manage.action.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.model.BorrowInterestRateModel;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPromotModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserPromotService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 用户管理
 * 
 * @author sj
 * @version 2.0
 * @since 2014年4月17日
 */
public class ManageUserAction extends BaseAction<UserModel> implements
		ModelDriven<UserModel> {

	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCertificationService attestationService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private AccountService accountService;
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private UserPromotService userPromotService;

	private Map<String, Object> data;

	/**
	 * 获得用户清单页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userManager")
	public String userManager() throws Exception {
		return "userManager";
	}

	/**
	 * 获得用户清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userList")
	public void userList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = request.getParameter("status");
		if (status == null) {
			model.setStatus(99);
		}
		PageDataList<UserModel> pagaDataList = userService.userList(pageNumber,
				pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户清单 导出报表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/exportExcelUserList")
	public void exportExcelUserList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		int realNameStatus = paramInt("realNameStatus");
		model.setRealNameStatus(realNameStatus);
		int status = paramInt("status");
		model.setStatus(status);
		model.setSign(1);
		PageDataList<UserModel> pagaDataList = userService.userList(1, 10000,
				model);
		String title = "用户信息列表";
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "性别", "电子邮件",
				"手机号码", "身份证号码", "实名状态", "锁定状态", "注册时间" };// 表头数组
		String[] fields = new String[] { "userId", "userName", "realName",
				"sexStr", "email", "mobilePhone", "cardId",
				"realNameStatusStr", "statusStr", "addTime" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(pagaDataList.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 锁定用户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userLockEditPage")
	public String userLockEditPage() throws Exception {
		long userId = paramLong("id");
		UserCache userCache = userCacheService.findByUserId(userId);
		request.setAttribute("userCache", userCache);
		return "userLockEditPage";
	}

	/**
	 * 锁定用户 1锁定 0未锁定
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userLockEdit")
	public void userLockEdit() throws Exception {
		data = new HashMap<String, Object>();
		UserCache userCache = userCacheService.findById(model.getId());
		userCache.setStatus(model.getStatus());
		userCache.setPayPwdStatus(model.getPayPwdStatus());
		userCache.setLoginPwdStatus(model.getLoginPwdStatus());
		userCache.setRemark(paramString("remark"));
		userCache.setLoginFailTimes(0);
		userCache.setLockTime(new Date());
		//update by Chris
		if(model.getPayPwdStatus() ==0){
			userCache.setPayFailTimes(0);
		}
		userCacheService.update(userCache);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userEditPage")
	public String userEditPage() throws Exception {
		long userId = paramLong("id");
		User user = userService.getUserById(userId);
		UserIdentify userIdentify = userIdentifyService.findByUserId(userId);
		UserCache userCache = userCacheService.findByUserId(userId);
		request.setAttribute("user", user);
		request.setAttribute("userAttestation", userIdentify);
		request.setAttribute("userCache", userCache);
		return "userEditPage";
	}

	/**
	 * 获得加息劵信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/borrowInterestRateManager")
	public String borrowInterestRateManager() throws Exception {
		return "borrowInterestRateManager";
	}

	/**
	 * 获得加息劵信息清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/borrowInterestRateList")
	public void borrowInterestRateList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<BorrowInterestRateModel> pagaDataList = borrowInterestRateService
				.borrowInterestRateList(pageNumber, pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 提交修改
	 * 
	 * @throws Exception
	 */
	@Action("/modules/user/user/user/borrowInterestRateEdit")
	public void borrowInterestRateEdit() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		BorrowInterestRate borrowInterestRate = borrowInterestRateService
				.find(id);
		borrowInterestRate.setStatus(2);
		borrowInterestRateService.update(borrowInterestRate);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 获得用户列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/user/user/getUserListPage")
	public String getUserListPage() throws Exception {
		return "getUserListPage";
	}

	/**
	 * 获得推广人信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotManager")
	public String userPromotManager() throws Exception {
		return "userPromotManager";
	}

	/**
	 * 获得推广人信息列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotList")
	public void userPromotList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<UserPromotModel> pagaDataList = userPromotService
				.userPromotList(pageNumber, pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 导出推荐人信息列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/exportExcelUserPromotList")
	public void exportExcelUserPromotList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");

		// 第一页开始
		model.setPage(1);
		// 记录取得
		/*PageDataList<UserPromotModel> pagaDataList = userPromotService
				.userPromotList(1, 5000, model);*/
		PageDataList<UserPromotModel> pagaDataList =userPromotService.exportUserPromotList(1, 5000, model);

		String title = "推广人Excel表";

		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "状态", "优惠码",
				"可使用次数", "已使用次数", "收益比例(%)", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName",
				"status", "couponCode", "canUseTimes", "usedTimes", "rate",
				"addTime" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(pagaDataList.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 添加推广人页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotAddPage")
	public String userPromotAddPage() throws Exception {
		return "userPromotAddPage";
	}

	/**
	 * 添加推广人
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotAdd")
	public void userPromotAdd() throws Exception {
		data = new HashMap<String, Object>();
		String userName = paramString("userName");
		int canUseTimes = paramInt("canUseTimes");
		double rate = paramDouble("rate");
		data = userPromotService.userPromotAdd(userName, canUseTimes, rate,
				getOperator());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 编辑推广人页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotEditPage")
	public String userPromotEditPage() throws Exception {
		long id = paramLong("id");
		UserPromot userPromot = userPromotService.getUserPromotById(id);
		request.setAttribute("userPromot", userPromot);
		return "userPromotEditPage";
	}

	/**
	 * 编辑推广人
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotEdit")
	public void userPromotEdit() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		UserPromot userPromot = userPromotService.getUserPromotById(id);
		userPromot.setStatus(paramInt("status"));
		userPromot.setCanUseTimes(paramInt("canUseTimes"));
		userPromot.setRate(paramDouble("rate"));
		userPromotService.userPromotEdit(userPromot);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 删除推广人
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotDelete")
	public void userPromotDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		String userName = paramString("userName");
		UserPromot userPromot = userPromotService.getUserPromotById(id);
		userPromotService.userPromotDelete(userName, userPromot, getOperator());
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 查看推广人下的用户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotDetailPage")
	public String userPromotDetailPage() throws Exception {
		long id = paramLong("id");
		request.setAttribute("userPromotId", id);
		return "userPromotDetailPage";
	}

	/**
	 * 查看推广人下的用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotDetailList")
	public void userPromotDetailList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		long userPromotId = paramLong("userPromotId");
		String searchName = paramString("searchName");
		UserPromot userPromot = userPromotService
				.getUserPromotById(userPromotId);
		PageDataList<User> pList = userPromotService.userPromotDetailList(
				userPromot.getUser(), pageNumber, pageSize, searchName);
		data.put("total", pList.getPage().getTotal());
		data.put("rows", pList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 查看所有推广人页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotAllPage")
	public String userPromotAllPage() throws Exception {

		return "userPromotAllPage";
	}

	/**
	 * 查看所有推广人的用户
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/userPromotAllList")
	public void userPromotAllList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		model.setPage(pageNumber);
		model.setSize(pageSize);
		PageDataList<UserModel> pList = userPromotService
				.userPromotAllList(model);
		data.put("total", pList.getPage().getTotal());
		data.put("rows", pList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/user/exportExceluserPromotAllList")
	public void exportExceluserPromotAllList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setSize(5000);
		/*PageDataList<UserModel> pList = userPromotService
				.userPromotAllList(model);*/
		PageDataList<UserModel> pList = userPromotService
				.exportUserPromotAllList(model);
		String title = "被推荐人列表";
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "推荐人用户名",
				"推荐人真实姓名", "邮箱地址", "手机号码", "推荐时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName",
				"inviteUserName", "inviteRealName", "email", "mobilePhone",
				"inviteTime" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(pList.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
}
