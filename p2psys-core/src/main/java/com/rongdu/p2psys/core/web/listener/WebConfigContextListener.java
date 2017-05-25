package com.rongdu.p2psys.core.web.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowConfigDao;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.LogTemplateDao;
import com.rongdu.p2psys.core.dao.NoticeTypeDao;
import com.rongdu.p2psys.core.domain.LogTemplate;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.domain.Rule;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.model.SystemConfigModel;
import com.rongdu.p2psys.core.model.Tree;
import com.rongdu.p2psys.core.protocol.ProtocolHelper;
import com.rongdu.p2psys.core.rule.RuleCheck;
import com.rongdu.p2psys.core.service.ArticleService;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.service.RuleService;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;

public class WebConfigContextListener implements ServletContextListener, HttpSessionAttributeListener {
	private static Logger logger = Logger.getLogger(WebConfigContextListener.class);
	private Object lock = new Object();

	private String rulePackageName = "com.rongdu.p2psys.core.rule";

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

		SystemConfigService systemConfigService = (SystemConfigService) BeanUtil.getBean("systemConfigService");
		NoticeService noticeService = (NoticeService) ctx.getBean("noticeService");
		LogTemplateDao logTemplateDao = (LogTemplateDao) ctx.getBean("logTemplateDao");
		// checkVersion();
		ArticleService articleService = (ArticleService) ctx.getBean("articleService");
		Tree<Site> tree = articleService.getSiteTree();
		context.setAttribute("tree", tree);

		SystemConfigModel info = systemConfigService.getSystemInfo();
		Global.SYSTEMINFO = info;
		setWebConfig(context, info);
		BorrowConfigDao borrowConfigDao = (BorrowConfigDao) ctx.getBean("borrowConfigDao");
		List<BorrowConfig> list = borrowConfigDao.findAll();
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (int i = 0; i < list.size(); i++) {
			BorrowConfig config = (BorrowConfig) list.get(i);
			map.put(config.getId(), config);
		}
		Global.BORROWCONFIG = map;
		Global.BORROW_CONFIG_LIST = list;
		
		// 读取通知类型配置
		NoticeTypeDao noticeTypeDao = (NoticeTypeDao) ctx.getBean("noticeTypeDao");
		List<NoticeType> noticeTypeList = noticeTypeDao.list();
		Map<String, Object> noticeTypeMap = new HashMap<String, Object>();
		for (int i = 0; i < noticeTypeList.size(); i++) {
			NoticeType noticeType = noticeTypeList.get(i);
			noticeTypeMap.put(noticeType.getNid() + "_" + noticeType.getNoticeType(), noticeType);
		}
		Global.NOTICE_TYPE_CONFIG = noticeTypeMap;

		/*
		 * userCreditDao userCreditDao = (UserCreditDao) ctx.getBean("userCreditDao"); List<CreditRank> creditRankList =
		 * userCreditDao.getCreditRankList(); Global.ALL_CREDIT_RANK = creditRankList;
		 */

		// 规则约束
		Map<String, RuleCheck> ruleCheckMap = new HashMap<String, RuleCheck>();
		RuleService ruleService = (RuleService) ctx.getBean("ruleService");
		List<Rule> ruleList = ruleService.findAll();
		for (int i = 0; i < ruleList.size(); i++) {
			Rule r = ruleList.get(i);
			try {
				Class<?> clazz = null;
				clazz = Class.forName(rulePackageName + "." + StringUtil.firstCharUpperCase(r.getNid()) + "RuleCheck");
				Object jsonObj = JSON.parseObject(r.getRuleCheck(), clazz);
				if (jsonObj == null) {
					jsonObj = clazz.newInstance();
				} else {
					ruleCheckMap.put(r.getNid(), (RuleCheck) jsonObj);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		Global.RULECHECKS = ruleCheckMap;

		Map<String, LogTemplate> logTemplateMap = new HashMap<String, LogTemplate>();
		List<LogTemplate> logTemplateList = logTemplateDao.findAll();
		for (int i = 0; i < logTemplateList.size(); i++) {
			LogTemplate logT = logTemplateList.get(i);
			if (logT != null) {
				String mapKey = logT.getType() + "_" + logT.getNid();
				logTemplateMap.put(mapKey, logT);
			}
		}
		Global.LOG_TEMPLATE_MAP = logTemplateMap;
		// 消息发送
		//NoticeJobQueue.init(noticeService);
		// 初始化系统指定对象
		ExecuterHelper.init();
		// 初始化协议指定对象
		ProtocolHelper.init();
	}

	private void setWebConfig(ServletContext context, SystemConfigModel info) {
		String[] webinfo = Global.SYSTEMNAME;
		for (String s : webinfo) {
			context.setAttribute(s, info.getValue(s));
			if (s.equals("theme_dir") && StringUtil.isBlank(info.getValue(s))) {
				context.setAttribute(s, "/themes/soonmes_default");
			}
		}
		context.setAttribute("webroot", context.getContextPath());
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		User user = getSessionUser(event);
		if (user != null) {
			synchronized (lock) {
				// 刷新登录时间
				Global.SESSION_MAP.put(user.getUserName(), System.currentTimeMillis());
			}
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		User user = getSessionUser(event);
		if (user != null) {
			synchronized (lock) {
				if (Global.SESSION_MAP.containsKey(user.getUserName())) {
				}
			}
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		User user = getSessionUser(event);
		if (user == null) {
			synchronized (lock) {
				if (Global.SESSION_MAP.containsKey(event.getName()) && event.getValue() == null) {
					Global.SESSION_MAP.remove(event.getName());
				}
			}
		}

	}

	public User getSessionUser(HttpSessionBindingEvent event) {
		if (StringUtil.isNull(event.getName()).equals(Constant.SESSION_USER)) {
			Object obj = event.getValue();
			if (obj != null) {
				return (User) obj;
			}
		}
		return null;
	}

	public void checkVersion() {
		String dbVersion = Global.getVersion();
		String sysVersion = Global.VERSION;
		logger.info("数据库版本：" + dbVersion);
		logger.info("系统版本:" + sysVersion);
		if (!Global.getVersion().equals(Global.VERSION)) {
			throw new RuntimeException("数据库版本与系统版本不一致，请更新数据库！");
		}
	}
}
