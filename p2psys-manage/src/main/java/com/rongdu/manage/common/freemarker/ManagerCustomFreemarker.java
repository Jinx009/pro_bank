package com.rongdu.manage.common.freemarker;

import javax.servlet.ServletContext;

import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.manage.common.freemarker.directive.AuthorizeUrlDirectiveModel;
import com.rongdu.p2psys.core.freemarker.CustomFreemarkerManager;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class ManagerCustomFreemarker extends CustomFreemarkerManager {

	@Override
	protected Configuration createConfiguration(ServletContext servletContext) throws TemplateException {
		Configuration cfg = super.createConfiguration(servletContext);
		cfg.setSharedVariable("secAuthorizeUrl", new AuthorizeUrlDirectiveModel());
		FreemarkerUtil.CONFIG = cfg;
		return cfg;
	}
}
