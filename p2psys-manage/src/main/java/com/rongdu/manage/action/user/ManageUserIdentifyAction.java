package com.rongdu.manage.action.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.rule.RealNameAttestationRuleCheck;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;

/**
 * 认证用户管理
 * 
 * @author zf
 * @version 2.0
 * @since 2014年5月16日
 */
public class ManageUserIdentifyAction extends BaseAction<UserIdentify> implements ModelDriven<UserIdentify> {

	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private UserCacheService userCacheService;

	private Operator operator;

	private Map<String, Object> data;

	/**
	 * 认证查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userIdentifyManager")
	public String userIdentifyManager() throws Exception {
		return "userIdentifyManager";
	}

	/**
	 * 认证查询
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userIdentifyList")
	public void userIdentifyList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		if (paramString("emailStatus").equals("")) {
			model = null;
		}
		PageDataList<UserIdentifyModel> pagaDataList = userIdentifyService.findUserIdentifylist(pageNumber, pageSize,model, searchName);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 认证导出
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/exportExcelIdentifyList")
	public void exportExcelIdentifyList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setVipStatus(-2);
		// 记录取得
		PageDataList<UserIdentifyModel> pagaDataList = userIdentifyService.findUserIdentifylist(1, 5000,model, searchName);

		String title = "用户认证Excel表";

		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "邮箱认证状态", "手机认证状态", "视频认证状态", "实名认证状态", "实名认证时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "emailStatus", "mobilePhoneStatus", "videoStatus", "realNameStatus", "realNameVerifyTime" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(pagaDataList.getList(), ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	/**
	 * 获得用户认证信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userRealNameManager")
	public String userRealNameManager() throws Exception {
		return "userRealNameManager";
	}

	/**
	 * 获得用户认证信息列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userRealNameList")
	public void userRealNameList() throws Exception {
		try {
			UserIdentify model = (UserIdentify) paramModel(UserIdentify.class, "userIdentify");
			data = new HashMap<String, Object>();
			int pageNumber = paramInt("page");// 当前页数
			int pageSize = paramInt("rows");// 每页总数
			PageDataList<UserModel> pagaDataList = userIdentifyService.userModelList(pageNumber, pageSize, model,searchName);
			int totalPage = pagaDataList.getPage().getTotal();// 总页数
			data.put("total", totalPage);
			data.put("rows", pagaDataList.getList());
			printJson(getStringOfJpaObj(data));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 审核用户认证信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userRealNameEditPage")
	public String userRealNameEditPage() throws Exception {
		UserIdentify userIdentify = userIdentifyService.findById(model.getId());
		UserCache userCache = userCacheService.findByUserId(userIdentify.getUser().getUserId());
		// 实名(身份证)校验
		RealNameAttestationRuleCheck realCardCheck = (RealNameAttestationRuleCheck) Global
				.getRuleCheck("realNameAttestation");
		if (!realCardCheck.style().equals("ID5")) {
			request.setAttribute("showPic", "showPic");
		}
		request.setAttribute("userIdentify", userIdentify);
		request.setAttribute("userCache", userCache);
		return "userRealNameEditPage";
	}

	/**
	 * 审核用户认证信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userRealNameEdit")
	public void userRealNameEdit() throws Exception {
		operator = getOperator();
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		String realNameVerifyRemark = paramString("realNameVerifyRemark");
		int realNameStatus = paramInt("realNameStatus");
		userIdentifyService.userAttestationEdit(id, realNameVerifyRemark, realNameStatus, operator);
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 视频认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userVideoManager")
	public String userVideoManager() throws Exception {
		return "userVideoManager";
	}
	
	/**
	 * 视频认证列表
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userVideoList")
	public void userVideoList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		model.setRealNameStatus(-2);
		model.setVipStatus(-2);
		model.setMobilePhoneStatus(-2);
		model.setEmailStatus(-2);
		model.setVideoStatus(2);
		PageDataList<UserIdentifyModel> pagaDataList = userIdentifyService.findUserIdentifylist(pageNumber, pageSize,model, searchName);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 视频认证审核页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userVideoVerifyPage")
	public String userVideoVerifyPage() throws Exception {
		UserIdentify userIdentify = userIdentifyService.findById(model.getId());
		request.setAttribute("userIdentify", userIdentify);
		return "userVideoVerifyPage";
	}
	
	/**
	 * 视频认证审核
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/userIdentify/userVideoVerify")
	public void userVideoVerify() throws Exception {
		data = new HashMap<String, Object>();
		UserIdentify userIdentify = userIdentifyService.findById(model.getId());
		int status = paramInt("videoStatus");
		String videoVerifyRemark = paramString("videoVerifyRemark");
		userIdentify.setVideoStatus(status);
		userIdentify.setVideoVerifyTime(new Date());
		userIdentifyService.update(userIdentify);
		// 添加审核日志
		VerifyLog verifyLog = new VerifyLog(getOperator(), "video", model.getId(), 2, status, videoVerifyRemark);
		verifyLogService.save(verifyLog);
		printResult(MessageUtil.getMessage("I10009"), true);
	}
}
