package com.rongdu.p2psys.core.web.interceptor;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionInvocation;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.ip.IPUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.service.LogService;
import com.rongdu.p2psys.user.domain.User;

public class ActionInterceptor extends BaseInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ActionInterceptor.class);

	@Resource
	private LogService logService;

	private static final String MSG = "msg";
	private static final String NONE = "";

	protected HttpServletRequest request;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
//		ActionContext ctx = invocation.getInvocationContext();
		request = ServletActionContext.getRequest();
//		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);  
		String ip = IPUtil.getRemortIP(request);
		Global.IP_THREADLOCAL.set(ip);
		String actionName = invocation.getInvocationContext().getName();
		String className = invocation.getAction().getClass().getName();
		try {
			String result = invocation.invoke();
			return result;
		} catch (BussinessException e) {
			log.error(actionName, e);
			saveExceptionLog(className, actionName, e);
			if (e.getType() != BussinessException.TYPE_JSON) {
				message(e);
				return MSG; // 这里要分 前台 和 后台
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("result", false);
				data.put("msg", e.getMessage());
				printJson(JSON.toJSONString(data));
				return null;
			} 
		} catch (Exception e) {
			log.error(actionName, e);
			saveExceptionLog(className, actionName, e);
			message("系统异常联系管理员！", NONE);
			return MSG; // 这里要分 前台 和 后台
		}
	}

	public void message(BussinessException e) {
		String url = e.getUrl();
		String msg = e.getMessage();
		HttpServletRequest request = ServletActionContext.getRequest();
		String urlback = null;
		if (e.getType() == 0) {
			if (StringUtil.isNotBlank(url)) {
				urlback = "<a href='" + url + "'>返回</a>";
			} else {
				urlback = "<a href='javascript:history.go(-1)'>返回</a>";
			}
		} else {
			urlback = "<a href='javascript:window.close()'>关闭</a>";
		}
		
		request.setAttribute("backurl", urlback);
		if(msg == null){
			msg = "";
		}
		request.setAttribute("rsmsg", msg);
	}

	/**
	 * 系统操作信息保存
	 * 
	 * @param className 类命
	 * @param methodName 方法名
	 */
	public void saveLog(String className, String methodName) {
		String path = request.getServletPath();
		String ip = IPUtil.getRemortIP(request);
		Log log = new Log();
		log.setMethod(methodName);
		log.setParams(this.getAllParams(true));
		log.setRemoteAddr(ip);
		log.setRequestUri(path);
		log.setType(Log.TYPE_ACCESS);
		User user = (User) request.getSession().getAttribute(Constant.SESSION_USER);
		if (user != null) {
			log.setAddUser(user.getUserName());
			log.setException(log.getException() + ",用户：" + user.getUserName());
		}
		logService.addLog(log);
	}

	/**
	 * 异常信息保存
	 * 
	 * @param className 类命
	 * @param methodName 方法名
	 * @param e 异常信息
	 */
	public void saveExceptionLog(String className, String methodName, Exception e) {
		StringWriter sw = new StringWriter();
		Log log = new Log();
		log.setMethod(methodName);
		log.setException(sw.toString());
		log.setParams(this.getAllParams(true));
		String path = request.getServletPath();
		String ip = IPUtil.getRemortIP(request);
		log.setRemoteAddr(ip);
		log.setRequestUri(path);
		log.setType(Log.TYPE_EXCEPTION);
		Operator op = (Operator) request.getSession().getAttribute(Constant.SESSION_OPERATOR);
		if (op != null) {
			log.setAddUser(op.getName());
		}
		logService.addLog(log);
	}

	/**
	 * 提取参数
	 * 
	 * @param safety 是否对密码过滤
	 * @return 参数信息
	 */
	protected String getAllParams(boolean safety) {
		StringBuffer ps = new StringBuffer();
		Enumeration<?> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameter = (String) parameterNames.nextElement();
			String value = request.getParameter(parameter);
			if (StringUtil.isNotBlank(value)) {
				if (!safety || (safety && !parameter.contains("password") && !parameter.contains("pwd"))) { // 安全性
					ps.append(parameter + "=" + value);
					if (parameterNames.hasMoreElements()) {
						ps.append("&");
					}
				}
			}
		}
		return ps.toString();
	}

}
