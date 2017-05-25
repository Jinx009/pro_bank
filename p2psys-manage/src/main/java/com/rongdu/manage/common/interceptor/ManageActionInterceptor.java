package com.rongdu.manage.common.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionInvocation;
import com.rongdu.common.util.ip.IPUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.domain.Menu;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.service.LogService;
import com.rongdu.p2psys.core.service.MenuService;
import com.rongdu.p2psys.core.web.interceptor.ActionInterceptor;

public class ManageActionInterceptor extends ActionInterceptor {
	private static final long serialVersionUID = 1008901298342362080L;
	private static final Logger log = Logger.getLogger(ManageActionInterceptor.class);
	private static final String NONE = "";

	@Resource
	private LogService logService;

	@Resource
	private MenuService menuService;
	//ipsAddBorrowNotify.html 环迅登记标异步回调不能进行拦截，拦截导致无法进入action处理
	private final static String[] interceptorArray = { "index.html", "login.html","ipsAddBorrowNotify.html", "ipsTransferNotify.html","generateUchonCode.html" };

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			super.request = ServletActionContext.getRequest();
			String ip = IPUtil.getRemortIP(request);
			Global.IP_THREADLOCAL.set(ip);
		} catch (Exception e1) {
			printJson("访问异常！");
		}
		String actionName = invocation.getInvocationContext().getName();
		String className = invocation.getAction().getClass().getName();
		try {
			String servletPath = getServletPath().replaceFirst("/", "");
			Operator op = (Operator) request.getSession().getAttribute(Constant.SESSION_OPERATOR);
			if(op != null){
				boolean flag = menuService.getMenuPermission(op.getId(), this.getManagePath());
				if(!flag){
					printJson("您没有此操作的权限！");
					return null;
				}
			}
			boolean isIn = true; // 默认进入拦截
			for (String array : interceptorArray) {
				if (array.equals(servletPath)) {
					isIn = false;
					break;
				}
			}
			if (op == null && isIn) {// 缓存不存在直接跳到登陆界面
				return "index";
			} else {// 首页，登陆等请求，跳过拦截
				String result = invocation.invoke();
				saveOperatorLog(className, actionName);
				return result;
			}
		} catch (Exception e) {
			log.error(actionName, e);
			message(NONE, "系统异常联系管理员！");
			saveExceptionLog(className, actionName, e);
			Map<String, Object> data = new HashMap<String, Object>();
            data.put("result", false);
            data.put("msg", e.getMessage());
            printJson(JSON.toJSONString(data));
			return null;
		}
	}

	/**
	 * 系统操作信息保存
	 * 
	 * @param className 类命
	 * @param methodName 方法名
	 */
	public void saveOperatorLog(String className, String methodName) {
		String path = request.getServletPath();
		String ip = IPUtil.getRemortIP(request);
		boolean result = this.isOperatorLog(path);
		if (result) {
			Menu menu = menuService.getMenuByHref(path);
			Log log = new Log();
			log.setMethod(methodName);
			if (menu != null) {
				log.setException(menu.getRemark());
			}
			log.setParams(this.getAllParams(true));
			log.setRemoteAddr(ip);
			log.setRequestUri(path);
			log.setType(Log.TYPE_OPERATOR);
			Operator op = (Operator) request.getSession().getAttribute(Constant.SESSION_OPERATOR);
			if (op != null) {
				log.setAddUser(op.getName());
				log.setException(log.getException() + ",系统操作员：" + op.getName() + ",账号：" + op.getUserName());
			}
			logService.addLog(log);
		}
	}

	public String getManagePath(){
		String path = request.getServletPath();
		return path.replace(".action", ".html");
	}
	
	
	/**
	 * 此次请求，是否保存操作日志信息
	 * 
	 * @param path 路径
	 * @return 结果
	 */
	public boolean isOperatorLog(String path) {
		String[] urlArr = { "Add.action", "Edit.action", "Delete.action" };
		for (int i = 0; i < urlArr.length; i++) {
			String url = urlArr[i];
			int size = path.lastIndexOf(url);
			if (size > 0) {
				return true;
			}
		}
		return false;
	}
}
