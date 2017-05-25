package com.rongdu.manage.action.home;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class ManangerAction extends BaseAction {

	@Action("/manager/editPwdPage")
	public String editPwdPage() {

		return "editPwdPage";
	}

	@Action("/manager/resource")
	public String resource() {

		return "resource";
	}

	@Action("/manager/role")
	public String role() {

		return "role";
	}
}
