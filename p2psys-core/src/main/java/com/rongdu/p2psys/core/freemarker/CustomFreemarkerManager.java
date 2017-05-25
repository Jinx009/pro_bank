package com.rongdu.p2psys.core.freemarker;

import javax.servlet.ServletContext;

import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.core.dao.ArticleDao;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.freemarker.directive.AccountInfoDirectiveModel;
import com.rongdu.p2psys.core.freemarker.directive.AttestationDirectiveModel;
import com.rongdu.p2psys.core.freemarker.directive.DicDirectiveModel;
import com.rongdu.p2psys.core.freemarker.directive.SiteDirectiveModel;
import com.rongdu.p2psys.core.freemarker.method.AttestationTypeNameModel;
import com.rongdu.p2psys.core.freemarker.method.DateMethodModel;
import com.rongdu.p2psys.core.freemarker.method.DateRollMethodModel;
import com.rongdu.p2psys.core.freemarker.method.DicMethodModel;
import com.rongdu.p2psys.core.freemarker.method.InterestMethodModel;
import com.rongdu.p2psys.core.freemarker.method.ParserDoubleMethodModel;
import com.rongdu.p2psys.core.freemarker.method.ParserLongMethodModel;
import com.rongdu.p2psys.user.dao.UserCertificationDao;
import com.rongdu.p2psys.user.dao.UserDao;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class CustomFreemarkerManager extends FreemarkerManager {

	@Override
	protected Configuration createConfiguration(ServletContext servletContext) throws TemplateException {
		Configuration cfg = super.createConfiguration(servletContext);
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		DictDao dictDao = (DictDao) ctx.getBean("dictDao");
		UserCertificationDao userCertificationDao = (UserCertificationDao) ctx.getBean("userCertificationDao");
		UserDao userDao = (UserDao) ctx.getBean("userDao");
		ArticleDao articleDao = (ArticleDao) ctx.getBean("articleDao");
		AccountDao accountDao = (AccountDao) ctx.getBean("accountDao");

		// 计算利息的自定义方法
		cfg.setSharedVariable("dateformat", new DateMethodModel());
		cfg.setSharedVariable("dateroll", new DateRollMethodModel());
		cfg.setSharedVariable("interest", new InterestMethodModel());
		// 新增自定义标签
		cfg.setSharedVariable("linkage", new DicDirectiveModel(dictDao));

		cfg.setSharedVariable("attestation", new AttestationDirectiveModel(userCertificationDao));
		cfg.setSharedVariable("Typet", new AttestationTypeNameModel(dictDao, userDao));

		cfg.setSharedVariable("siteDirect", new SiteDirectiveModel(articleDao));

		cfg.setSharedVariable("getLinkage", new DicMethodModel(dictDao));
		cfg.setSharedVariable("parseDouble", new ParserDoubleMethodModel());
		cfg.setSharedVariable("parseLong", new ParserLongMethodModel());
		cfg.setSharedVariable("accountType", new AttestationTypeNameModel(dictDao, userDao));
		cfg.setSharedVariable("accountInfo", new AccountInfoDirectiveModel(userDao, accountDao));
		FreemarkerUtil.CONFIG = cfg;

		return cfg;
	}
}
