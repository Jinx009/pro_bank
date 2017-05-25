package com.rongdu.manage.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.OperatorRole;
import com.rongdu.p2psys.core.domain.OperatorUser;
import com.rongdu.p2psys.core.service.OperatorRoleService;
import com.rongdu.p2psys.core.service.OperatorService;
import com.rongdu.p2psys.core.service.OperatorUserService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserFinancialModel;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 业务员分配模块
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2014年12月29日
 */
public class ManageFinancialAction extends BaseAction<UserFinancialModel> implements
		ModelDriven<UserFinancialModel> {
	@Resource
	private OperatorUserService operatorUserService;
	@Resource
	private OperatorService operatorService;
	@Resource
	private UserService userService;
	@Resource
	private OperatorRoleService operatorRoleService; 
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	private UserFinancialModel model = new UserFinancialModel();
	
	public UserFinancialModel getModel() {
		return model;
	}

	/**
	 * 用户分配
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/userFinancialListPage")
	public String userFinancialListPage() throws Exception{
		return "userFinancialListPage";
	}
	
	/**
	 * 用户列表
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/userFinancialList")
	public void userFinancialList() throws Exception{
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = paramString("status");
        if (status == "") {
        	model.setStatus(99);
        }
        String fStatus = paramString("fStatus");
        if(fStatus == ""){
        	model.setfStatus(99);
        }else{
        	model.setfStatus(NumberUtil.getInt(fStatus));
        }
        String rStartTime = paramString("rStartTime");
        if(StringUtil.isNotBlank(rStartTime)){
        	model.setrStartTime(rStartTime);
        }
        String rEndTime = paramString("rEndTime");
        if(StringUtil.isNotBlank(rStartTime)){
        	model.setrEndTime(rEndTime);
        }
        //获取当前登录管理员
  		Operator operator = getOperator();
  		operator = operatorService.getUserById(operator.getId());
  		model.setOperator(operator);
		PageDataList<UserFinancialModel> pagaDataList = operatorUserService.getList(pageNumber, pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 添加理财顾问
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/addFinancial")
	public void addFinancial() throws Exception{
		try{
			//获取理财顾问
			long pid = paramLong("pid");
			Operator operator = operatorService.getUserById(pid);
			//获取用户
			String ids = paramString("ids");
			String[] id = ids.split(",");
			List<OperatorUser> list = new ArrayList<OperatorUser>();
			if(ids != null && id.length > 0 && operator != null){
				for (int i = 0; i < id.length; i++) {
					User user = userService.getUserById(NumberUtil.getLong(id[i]));
					OperatorUser operatorUser = new OperatorUser();
					operatorUser.setOperator(operator);
					operatorUser.setUser(user);
					list.add(operatorUser);
				}
				operatorUserService.addFinancial(list);
				printResult(MessageUtil.getMessage("I10002"), true);
			}
		}catch(Exception e){
			printResult(MessageUtil.getMessage("I10005"), false);
		}
	}
	
	/**
	 * 选择理财顾问页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/getFinancialPage")
	public String getFinancialPage() throws Exception{
		String[] ids = request.getParameterValues("ids");
		String idStr = "";
		if(ids != null && ids.length > 0){
			for (int i = 0; i < ids.length; i++) {
				idStr += ids[i];
			}
		}
		request.setAttribute("idStr", idStr);
		return "getFinancialPage";
	}
	
	/**
	 * 设置理财经理
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/getFinancial")
	public void getFinancial() throws Exception{
		//获取所有的理财经理
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		param.addParam("role.id", 3);
		PageDataList<OperatorRole> pList = operatorRoleService.getOperatorRole(param);
		List<OperatorRole> list = pList.getList();
		//懒加载获取数据
		for (int i = 0; i < list.size(); i++) {
			pList.getList().get(i).getOperator().getName();
		}
		data.put("total", pList.getPage().getTotal()); // 总行数
		data.put("rows", pList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 跳转理财经理、理财顾问名下客户基本信息页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/getFinancialUserListPage")
	public String getFinancialUserListPage() throws Exception{
		return "getFinancialUserListPage";
	}
	
	/**
	 * 获取理财经理、理财顾问名下客户基本信息
	 * @throws Exception
	 */
	@Action(value = "/modules/system/financial/getFinancialUserList")
	public void getFinancialUserList() throws Exception{
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = paramString("status");
        if (status == "") {
        	model.setStatus(99);
        }
        //获取当前登录管理员
  		Operator operator = getOperator();
  		operator = operatorService.getUserById(operator.getId());
  		model.setOperator(operator);
		PageDataList<UserFinancialModel> pagaDataList = operatorUserService.getFinancialUserList(pageNumber, pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
}
