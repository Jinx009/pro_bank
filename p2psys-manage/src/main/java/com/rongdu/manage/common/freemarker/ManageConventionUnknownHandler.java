package com.rongdu.manage.common.freemarker;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.convention.ConventionUnknownHandler;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

public class ManageConventionUnknownHandler extends ConventionUnknownHandler {

	@Inject
	public ManageConventionUnknownHandler(Configuration configuration, ObjectFactory objectFactory,
			ServletContext servletContext, Container container,
			@Inject("struts.convention.default.parent.package") String defaultParentPackageName,
			@Inject("struts.convention.redirect.to.slash") String redirectToSlash,
			@Inject("struts.convention.action.name.separator") String nameSeparator) {
		super(configuration, objectFactory, servletContext, container, defaultParentPackageName, redirectToSlash,
				nameSeparator);
	}

	@Override
	public ActionConfig handleUnknownAction(String namespace, String actionName) throws XWorkException {
		return null;
	}

	public Map<String, ResultTypeConfig> getCustomResultTypesByExtension(PackageConfig packageConfig) {
		Map<String, ResultTypeConfig> results = packageConfig.getAllResultTypeConfigs();

		Map<String, ResultTypeConfig> resultsByExtension = new HashMap<String, ResultTypeConfig>();
		resultsByExtension.put("ftl", results.get("freemarker"));
		resultsByExtension.put("html", results.get("ftl"));
		resultsByExtension.put("htm", results.get("dispatcher"));
		return resultsByExtension;
	}
}
