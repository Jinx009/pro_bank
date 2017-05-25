package com.rongdu.p2psys.core.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

public class SessionInterceptor extends AbstractInterceptor {
	private static Logger logger = Logger.getLogger(SessionInterceptor.class);

	private static final long serialVersionUID = -2239644443711524657L;
	private UserDao userDao;
	private UserCacheDao userCacheDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {

		this.userDao = userDao;
	}
	
	public UserCacheDao getUserCacheDao() {
		return userCacheDao;
	}

	public void setUserCacheDao(UserCacheDao userCacheDao) {
		this.userCacheDao = userCacheDao;
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map<String, Object> session = ctx.getSession();
		User user = (User) session.get(Constant.SESSION_USER);

		boolean newlogin = false;
		if (user != null) {

			// 判断是否是最新登录，不是则视为没有登录
			if ("1".equals(Global.getValue("single_login"))) {// 配置是否启用单点登录
				// 直接取出session中保存的登录时间

				if (user != null) {
					long logintime = Long.parseLong(session.get("logintime").toString());
					String username = user.getUserName();

					long lastlogintime = 0;
					if (Global.SESSION_MAP.containsKey(username)) {
						lastlogintime = Long.parseLong(Global.SESSION_MAP.get(username).toString());
					}
					// 如果cookie中保存的登录时间小于系统中保存的用户最新登录时间，则认为是此登录已经过期
					logger.debug("当前登录时间：" + logintime + "；最新登录时间：" + lastlogintime);
					if (logintime < lastlogintime) {
						user = null;
						// 清除掉过期的cookie
						// 不能移除会话，会触发WebConfigContextListener
						// 中attributeRemoved，导致全局变量中保存的最新登录时间被清除，
						session.remove(Constant.SESSION_USER);
						session.remove("logintime");
					}

					// 会话还存在，但是登录时间不是最新登录时间，说明在别的地方登录了，提示已在别的地方登录
					if (logintime < lastlogintime) {
						newlogin = true;
						user = null;
					}
				}
			}
		}

		// 判断是不是后台跳转到前台的，这种情况下，只要会话中还有
		if (user == null) {
		    if (request.getParameter("borrowPreview") != null && request.getHeader("referer") != null) {
		        return actionInvocation.invoke();
		    }
			String servletPath = request.getServletPath();
			// 将action替换成html后缀
			servletPath = servletPath.replace(".action", ".html");
			String queryString = request.getQueryString();
			String redirectURL = servletPath;
			if (StringUtil.isNotBlank(queryString)) {
				redirectURL = request.getContextPath() + servletPath + "?" + StringUtil.isNull(queryString);
			}
			redirectURL = java.net.URLEncoder.encode(redirectURL, "UTF-8");
			String loginURl = "/user/login.html";
			String weburl = Global.getValue("weburl");
			if (newlogin) {
				response.sendRedirect(weburl + loginURl + "?newlogin=1&redirectURL="
						+ request.getContextPath() + redirectURL);
			} else {
				response.sendRedirect(weburl + loginURl + "?timeout=1&redirectURL="
						+ request.getContextPath() + redirectURL);
			}
			return null;
		}
		return actionInvocation.invoke();
	}

}
