package com.rongdu.p2psys.core.web.interceptor;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserCacheModel;

public class UserStatusInterceptor extends AbstractInterceptor {
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

	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map<String, Object> session = ctx.getSession();
		User user = (User) session.get(Constant.SESSION_USER);
		
		/*if (user != null) {
			UserCache userCache = userCacheDao.getUserCacheByUserId(user.getUserId());
			// 如果用户被锁定则自动退出
			if (userCache.getStatus() == 1) {
				logger.info("用户" + user.getUserId() + "已被锁定，自动退出...");
				user = null;
				session.remove(Constant.SESSION_USER);
				session.remove("logintime");
				response.sendRedirect(Global.getValue("weburl") + "/user/login.html");
			}
			UserCacheModel cacheModel = UserCacheModel.instance(userCache);
			Enumeration names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				if("payPwd".equals(name)) {
					if(cacheModel.isPayPwdLock()) {
						throw new UserException("交易密码已被锁定", 1);
					}
				}
				if("pwd".equals(name)) {
					if(cacheModel.isPwdLock()) {
						throw new UserException("登录密码已被锁定", 1);
					}
				}
			}
		}*/
		return actionInvocation.invoke();
	}

}
