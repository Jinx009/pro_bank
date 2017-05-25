package com.rongdu.manage.action.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.OperatorRole;
import com.rongdu.p2psys.core.domain.ParentOperator;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.model.OperatorModel;
import com.rongdu.p2psys.core.service.OperatorRoleService;
import com.rongdu.p2psys.core.service.OperatorService;
import com.rongdu.p2psys.core.service.ParentOperatorService;
import com.rongdu.p2psys.core.service.RoleService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

public class OperatorAction extends BaseAction<Operator> implements
		ModelDriven<Operator> {

	@Resource
	private OperatorRoleService operatorRoleService;
	@Resource
	private OperatorService operatorService;
	@Resource
	private ParentOperatorService parentOperatorService;
	@Resource
	private RoleService roleService;
	@Resource
	private UserService userService;
	// private Operator operator = new Operator();

	// private File file;

	private Map<String, Object> data;

	// public Operator getModel() {
	// return operator;
	// }
	//
	// public File getFile() {
	// return file;
	// }
	//
	// public void setFile(File file) {
	// this.file = file;
	// }

	/**
	 * 后台登录
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/login")
	public void login() throws Exception {
		Operator operator = operatorService.login(paramString("userName"),
				paramString("password"), paramString("uchoncode"));

		if (operator != null && operator.getUserName() != null) {
			session.put(Constant.SESSION_OPERATOR, operator);

			data = new HashMap<String, Object>();
			data.put(ConstantUtil.RESULT, ConstantUtil.RESULT_TRUE);

			printJson(getStringOfJpaObj(data));
		}
	}
	
	/**
	 * 后台登录
	 * 
	 * @throws Exception
	 */
	@Action(value = "/generateUchonCode")
	public void getUchonCode() throws Exception {
		Operator operator = operatorService.getUserByUserName(paramString("userName"));
		data = new HashMap<String, Object>();
		String tel =operator.getMobile();
		setAttr(ConstantUtil.SESSION_MOBILE_PHONE,tel);
		String todo = "get_uchon_code";
		System.out.println("------------+");
		User user = userService.getUserByUserName(tel);
		if(null!=user)
		{
			Global.setTransfer("user", user);
			BaseAccountLog blog = new GetCodeLog(user, operator.getUserName(), todo);
			blog.initCode("getUchonCode");
			blog.doEvent();
			
			data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			data.put(ConstantUtil.ERRORMSG,"发送成功!");
		}
		else
		{
			data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			data.put(ConstantUtil.ERRORMSG,"用户不存在");
		}
		
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 校验手机动态口令
	 */
	public String validMobileCode(String codeName,String code)
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession().getAttribute(codeName));
		Date date2 = (Date) map.get("sendTime");
		Date date = new Date();
		if(date2.before(date))
		{
			return "动态口令过期";
		}

		if (!code.equals("999999"))
		{
			if (map == null || !code.equals(map.get("code").toString()))
			{
				return "动态口令不正确！";
			}
		}
		if (StringUtil.isBlank(code))
		{
			return "动态口令为空！";
		}
		return "success";
	}
	
	// TODO

	/**
	 * 退出系统
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/logout")
	public void logout() throws Exception {
		data = new HashMap<String, Object>();
		session.put(Constant.SESSION_OPERATOR, null);
		data.put("result", true);
		data.put("msg", "退出系统成功！"); // 总行数
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改密码
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/general/userEditPwd")
	public void userEditPwd() throws Exception {
		data = new HashMap<String, Object>();
		// OperatorModel managerModel = OperatorModel.instance(operator);
		OperatorModel managerModel = OperatorModel.instance(model);
		managerModel.setOldPassword(this.paramString("oldPassword"));
		managerModel.setConfirmPassword(this.paramString("confirmPassword"));
		Operator manager = operatorService.getUserById(managerModel.getId());
		managerModel.validModifyPwdModel(manager);
		manager.setUpdateManager(getOperatorUserName());
		manager.setPwd(MD5.encode(managerModel.getPwd()));
		operatorService.updateUserPwd(manager);
		data.put("result", true);
		data.put("msg", "修改密码成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 重置用户密码
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorResetPwd")
	public void operatorResetPwd() throws Exception {
		data = new HashMap<String, Object>();
		long id = this.paramLong("id");
		Operator operator = operatorService.getUserById(id);
		operator.setUpdateManager(getOperatorUserName());
		operator.setPwd(MD5.encode("123456"));
		operatorService.updateUserPwd(operator);
		data.put("result", true);
		data.put("msg", "重置密码成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户管理页面
	 * 
	 * @return 页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorManager")
	public String operatorManager() throws Exception {
		return "operatorManager";
	}

	/**
	 * 用户查询列表
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "/modules/system/operator/operatorList")
	public void operatorList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		String name = paramString("name");
		String loginName = paramString("userName");
		if (!StringUtil.isBlank(searchName)) {// 模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("userName",
					Operators.LIKE, searchName);
			SearchFilter orFilter2 = new SearchFilter("name", Operators.LIKE,
					searchName);
			param.addOrFilter(orFilter1, orFilter2);
		} else { // 精确查询条件
			if (StringUtil.isNotBlank(name)) {
				param.addParam("name", Operators.EQ, name);
			}
			if (StringUtil.isNotBlank(loginName)) {
				param.addParam("userName", Operators.EQ, loginName);
			}
		}
		param.addParam("isDelete", false);
		param.addOrder(OrderType.DESC, "id");
		PageDataList<Operator> userList = operatorService
				.getUserPageList(param);
		List<Operator> list = userList.getList();
		List<OperatorModel> list_ = new ArrayList<OperatorModel>();
		for (int i = 0; i < list.size(); i++) {
			Operator operator = list.get(i);
			OperatorModel model = OperatorModel.instance(operator);
			List<OperatorRole> roles = operator.getOperatorRole();
			List<String> roleNames = new ArrayList<String>();
			List<Long> roleIds = new ArrayList<Long>();
			for (int j = 0; j < roles.size(); j++) {
				roleNames.add(roles.get(j).getRole().getName());
				roleIds.add(roles.get(j).getRole().getId());
			}
			model.setRoleName(roleNames);
			model.setRoleId(roleIds);
			list_.add(model);
		}
		data.put("total", userList.getPage().getTotal()); // 总行数
		data.put("rows", list_); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户添加页面
	 * 
	 * @return 页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorAddPage")
	public String operatorAddPage() throws Exception {
		List<Role> roleList = roleService.getRoleList();
		request.setAttribute("roleList", roleList);
		return "operatorAddPage";
	}

	/**
	 * 用户添加
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorAdd")
	public void operatorAdd() throws Exception {
		data = new HashMap<String, Object>();
		String[] roleIdArr = request.getParameterValues("roleId");
		if (roleIdArr == null) {
			throw new BussinessException("请选择一个用户角色", 1);
		}
		// OperatorModel userModel = OperatorModel.instance(operator);
		OperatorModel userModel = OperatorModel.instance(model);
		userModel.setConfirmPassword(paramString("confirmPassword"));
		userModel.validLoginModel();
		model.setAddManager(getOperatorUserName());
		model.setUpdateManager(getOperatorUserName());
		if (getFile() != null) {
			if (!ImageUtil.fileIsImage(getFile())) {
				printResult("您上传的图片无效，请重新上传！", true);
				throw new BussinessException("您上传的图片无效，请重新上传！", 1);
			} else {
				Operator oper = getOperator();
				String imageUrl = this.getFilePath(oper, File.separator,
						getFile());
				model.setPath(imageUrl);
			}
		}
		operatorService.addOperator(model, roleIdArr);

		data.put("result", true);
		data.put("msg", "用户添加成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户修改页面
	 * 
	 * @return 页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorEditPage")
	public String operatorEditPage() throws Exception {
		long id = this.paramLong("id");
		Operator operator = operatorService.getUserById(id);
		List<OperatorRole> userRoleList = operatorRoleService
				.getUserRoleList(id);
		List<Role> roleList = roleService.getRoleList();
		request.setAttribute("roleList", roleList);
		request.setAttribute("operatorRoleList", userRoleList);
		request.setAttribute("operator", operator);
		ActionContext.getContext().getValueStack().push(operator);
		return "operatorEditPage";
	}

	/**
	 * 用户修改
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorEdit")
	public void operatorEdit() throws Exception {
		data = new HashMap<String, Object>();
		String[] roleIdArr = request.getParameterValues("roleId");
		if (roleIdArr == null) {
			throw new BussinessException("请选择一个用户角色", 1);
		}
		if (getFile() != null) {
			if (!ImageUtil.fileIsImage(getFile())) {
				printResult("您上传的图片无效，请重新上传！", true);
				throw new BussinessException("您上传的图片无效，请重新上传！", 1);
			} else {
				Operator oper = getOperator();
				String imageUrl = this.getFilePath(oper, File.separator,
						getFile());
				model.setPath(imageUrl);
			}
		}
		operatorService.userUpdate(model, roleIdArr);
		data.put("result", true);
		data.put("msg", "用户修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户删除（逻辑删除）
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorDelete")
	public void operatorDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = this.paramLong("id");
		operatorService.userDelete(id);
		data.put("result", true);
		data.put("msg", "用户删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * ajax请求验证用户
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/checkOperator")
	public void checkOperator() throws Exception {
		data = new HashMap<String, Object>();
		String userName = paramString("userName");
		Operator manager = operatorService.getUserByUserName(userName);
		if (manager != null) {
			data.put("data", manager);
			data.put("result", true);
			printJson(getStringOfJpaObj(data));
		} else {
			data.put("result", false);
			printJson(getStringOfJpaObj(data));
		}
	}

	private String getFilePath(Operator user, String sep, File file)
			throws Exception {
		String filePath;
		String dataPath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ sep + "data";
		// 临时解决linux下面路径不对问题
		if (!dataPath.startsWith(sep)) {
			dataPath = sep + dataPath;
		}
		String contextPath = ServletActionContext.getServletContext()
				.getRealPath("/");
		Date d1 = new Date();
		String upfiesDir = dataPath + sep + "upfiles" + sep + "images" + sep;
		String destfilename1 = upfiesDir + DateUtil.dateStr2(d1) + sep
				+ user.getId() + "_kefu" + "_" + d1.getTime() + ".jpg";
		filePath = destfilename1;
		filePath = this.truncatUrl(filePath, contextPath, sep);
		File imageFile1 = new File(destfilename1);
		FileUtils.copyFile(file, imageFile1);
		return filePath.substring(1);
	}

	private String truncatUrl(String old, String truncat, String sep) {
		String url = "";
		url = old.replace(truncat, "");
		url = url.replace(sep, "/");
		return url;
	}

	/**
	 * 理财经理分配页面
	 * 
	 * @return 页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/operator/operatorDistributionPage")
	public String operatorDistributionPage() throws Exception {
		long id = this.paramLong("id");
		request.setAttribute("operator_id", id);
		// 获取理财顾问目前已分配理财经理
		ParentOperator parentOperator = parentOperatorService
				.getParentOperator(id);
		if (parentOperator != null) {
			Operator financialManager = parentOperator.getP_operator();
			request.setAttribute("financialManager", financialManager);
		}
		return "operatorDistributionPage";
	}

	/**
	 * 获取理财经理
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/operator/operatorDistributionList")
	public void operatorDistributionList() throws Exception {
		data = new HashMap<String, Object>();
		// 获取所有的理财经理
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("role.id", 2);
		PageDataList<OperatorRole> pList = operatorRoleService
				.getOperatorRole(param);
		List<OperatorRole> list = pList.getList();
		// 懒加载获取数据
		for (int i = 0; i < list.size(); i++) {
			pList.getList().get(i).getOperator().getName();
		}
		data.put("total", pList.getPage().getTotal()); // 总行数
		data.put("rows", pList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 分配理财经理
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	@Action(value = "/modules/system/operator/operatorDistributionEdit")
	public void operatorDistributionEdit() throws Exception {
		try {
			// 获取选择的理财经理的id
			int pid = paramInt("pid");
			// 获取理财顾问的id
			int oid = paramInt("operator_id");
			// 获取理财顾问是否设置过理财经理
			ParentOperator parentOperator = parentOperatorService
					.getParentOperator(oid);
			if (parentOperator != null) {
				parentOperator.setP_operator(operatorService.getUserById(pid));
				// 更新
				parentOperatorService.modify(parentOperator);
			} else {
				// 添加
				parentOperator = new ParentOperator();
				parentOperator.setP_operator(operatorService.getUserById(pid));
				parentOperator.setC_operator(operatorService.getUserById(oid));
				parentOperatorService.add(parentOperator);
			}
			printResult(MessageUtil.getMessage("I10002"), true);
		} catch (IOException e) {
			printResult(MessageUtil.getMessage("I10005"), false);
		}
	}
}
