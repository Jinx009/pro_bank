package com.rongdu.p2psys.core.web.interceptor;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.rule.FreezeRuleCheck;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserFreeze;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.service.UserFreezeService;

/**
 * 冻结拦截器
 * 
 * @author sj
 * @version 2.0
 * @since 2014年4月22日
 */
public class FreezeInterceptor extends BaseInterceptor {

	private static final long serialVersionUID = 1008901298342362080L;

	private FreezeRuleCheck freezeRuleCheck = (FreezeRuleCheck) Global.getRuleCheck("freeze");

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		if (freezeRuleCheck != null) {// 启用
			ServletContext context = ServletActionContext.getServletContext();
			ApplicationContext actx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
			String nameSpace = getNamespce();
			String servletPath = getServletPath();
			StringBuffer url = new StringBuffer();// 请求的url
			url.append(nameSpace);
			url.append(servletPath);

			UserFreezeService freezeService = (UserFreezeService) actx.getBean("freezeService");
			UserFreeze freeze = null;// 冻结信息

			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			User user = (User) session.get(Constant.SESSION_USER);
			if (user == null && "/user/doLogin.html".equals(url.toString())) {
				String userName = getParam("userName") + "";
				freeze = freezeService.getByUserName(userName);
			} else if (user != null) {
				freeze = freezeService.getByUserId(user.getUserId());
			}
			if (freeze != null) {
				if (freeze.getStatus() == 1 && !StringUtil.isBlank(freeze.getMark())) {// 启用
					String[] marks = freeze.getMark().split(",");
					String markUrl = "";
					for (String mark : marks) {
						if ("login".equals(mark)) {
							markUrl = freezeRuleCheck.login;
						} else if ("borrow".equals(mark)) {
							markUrl = freezeRuleCheck.borrow;
						} else if ("tender".equals(mark)) {
							markUrl = freezeRuleCheck.tender;
						} else if ("recharge".equals(mark)) {
							markUrl = freezeRuleCheck.recharge;
						} else if ("cash".equals(mark)) {
							markUrl = freezeRuleCheck.cash;
						}
						if (markUrl.equalsIgnoreCase(url.toString())) {
							throw new UserException("该操作暂时无法访问，请联系管理员");
						}
					}
				}
			}
		}
		String result = invocation.invoke();
		return result;
	}

}
