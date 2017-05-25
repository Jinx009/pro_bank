package com.rongdu.p2psys.core.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.rongdu.common.util.StringUtil;

public abstract class BaseInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -2239644443711524657L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseInterceptor.class);

	public void init() {
		super.init();
	}

	public abstract String intercept(ActionInvocation ai) throws Exception;

	protected String getServletPath() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String servletPath = request.getServletPath();
		String namespace = ServletActionContext.getActionMapping().getNamespace();
		String extension = ServletActionContext.getActionMapping().getExtension();
		servletPath = servletPath.replaceFirst(namespace, "").replace("." + extension, ".html");
		return servletPath;
	}

	protected String getNamespce() {
		return ServletActionContext.getActionMapping().getNamespace();
	}

	protected void message(String msg, String url, String text) {
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("rsmsg", msg);
		String urltext = "";
		urltext = "<a href=" + request.getContextPath() + url + " >" + text + "</a>";
		request.setAttribute("backurl", urltext);
	}

	protected void message(String msg, String url) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String urltext = "";
		if (StringUtil.isNotBlank(url)) {
			urltext = "<a href=" + request.getContextPath() + url + " >返回</a>";
			request.setAttribute("backurl", urltext);
		} else {
			urltext = "<a href='javascript:history.go(-1)'>返回</a>";
		}
		request.setAttribute("backurl", urltext);
		request.setAttribute("rsmsg", msg);
//		message(msg, url, urltext);
	}

	protected Object getParam(String name) {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request.getParameter(name);
	}

	protected void printJson(String json) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}

}
