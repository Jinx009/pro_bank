package com.rongdu.p2psys.util.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 新版PC端 SessionFilter
 * 
 * @author Jinx
 *
 */
public class CfSessionFilter implements Filter {
	protected String encoding = null;

	protected FilterConfig filterConfig = null;

	protected boolean ignore = false;

	protected String forwardPath = null;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// 通过检查session中的变量，过虑请求
		HttpSession session = httpServletRequest.getSession();
		User sessionUser = (User) session
				.getAttribute(ConstantUtil.SESSION_USER);

		// 当前会话用户为空而且不是请求登录，退出登录，欢迎页面和根目录则退回到应用的根目录
		String servletPath = httpServletRequest.getServletPath();
		String queryString = httpServletRequest.getQueryString();
		List<String> pathList = notNeedSessionCheck();
		if (!pathList.contains(servletPath)) {
			if (sessionUser == null) {
				String redirectURL = servletPath;
				if (StringUtil.isNotBlank(queryString)) {
					redirectURL = httpServletRequest.getContextPath()
							+ servletPath + "?"
							+ StringUtil.isNull(queryString);
				}
				redirectURL = java.net.URLEncoder.encode(redirectURL, "UTF-8");
				httpServletResponse
						.sendRedirect("/cf/login.html?timeout=1&redirectUrl="
								+ redirectURL);
				return;
			} else {

			}
		}

		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		this.forwardPath = filterConfig.getInitParameter("forwardpath");

		String value = filterConfig.getInitParameter("ignore");

		if (value == null)
			this.ignore = true;
		else if (value.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}

	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}

	private List<String> notNeedSessionCheck() {
		String[] paths = new String[] { "/pro/list.html",
				"/nb/wechat/account/800bank.action" };

		return Arrays.asList(paths);
	}

}
