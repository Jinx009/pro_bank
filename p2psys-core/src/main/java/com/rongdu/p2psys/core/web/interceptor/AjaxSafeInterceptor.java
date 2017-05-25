package com.rongdu.p2psys.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpURL;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * ajax拦截器 禁止跨域访问 禁止url直接访问
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月14日
 */
public class AjaxSafeInterceptor extends BaseInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();

		String requestType = request.getHeader("X-Requested-With");
		if (("XMLHttpRequest").equals(requestType)) {// Ajax请求
			String refererUrl = request.getHeader("Referer");
			/*HttpURL url = new HttpURL(refererUrl);
			if (!request.getServerName().equals(url.getHost())) {// 禁止跨域访问
				message("抱歉，您访问的页面不存在！", null);
				return "msg";
			}*/
		} else {// 禁止url直接访问
			message("抱歉，您访问的页面不存在！", null);
			return "msg";
		}

		return invocation.invoke();
	}

}
