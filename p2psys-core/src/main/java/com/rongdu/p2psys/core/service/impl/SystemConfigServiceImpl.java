package com.rongdu.p2psys.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowConfigDao;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.NoticeTypeDao;
import com.rongdu.p2psys.core.dao.SystemConfigDao;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.model.SystemConfigModel;
import com.rongdu.p2psys.core.model.Tree;
import com.rongdu.p2psys.core.service.ArticleService;
import com.rongdu.p2psys.core.service.SystemConfigService;

@Service("systemConfigService")
public class SystemConfigServiceImpl implements SystemConfigService {

	@Resource
	private SystemConfigDao systemConfigDao;
	@Resource
	private BorrowConfigDao borrowConfigDao;
	@Resource
	private ArticleService articleService;
	@Resource
	private NoticeTypeDao noticeTypeDao;
	@Resource
	private SystemConfigService systemConfigService;

	private Logger logger = Logger.getLogger(SystemConfigServiceImpl.class);

	@Override
	public SystemConfigModel getSystemInfo() {
		SystemConfigModel info = new SystemConfigModel();
		List<SystemConfig> list = systemConfigDao.findAll();
		for (int i = 0; i < list.size(); i++) {
			SystemConfig sys = (SystemConfig) list.get(i);
			logger.debug(sys.getId() + " " + sys.getValue());
			info.addConfig(sys);
		}
		return info;
	}

	@Override
	public PageDataList<SystemConfigModel> list(SystemConfigModel model) {
		return systemConfigDao.list(model);
	}

	@Override
	public SystemConfig find(long id) {
		return systemConfigDao.find(id);
	}

	@Override
	public void add(SystemConfig sconfig) {
		systemConfigDao.save(sconfig);
	}

	@Override
	public void update(SystemConfig sconfig) {
		systemConfigDao.update(sconfig);
	}

	@Override
	public void clean() {
		WebApplicationContext wac = ContextLoader
				.getCurrentWebApplicationContext();
		ServletContext context = wac.getServletContext();

		Tree<Site> tree = articleService.getSiteTree();
		context.setAttribute("tree", tree);

		SystemConfigModel info = systemConfigService.getSystemInfo();
		Global.SYSTEMINFO = info;
		setWebConfig(context, info);

		List<BorrowConfig> list = borrowConfigDao.findAll();
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (int i = 0; i < list.size(); i++) {
			BorrowConfig config = (BorrowConfig) list.get(i);
			map.put(config.getId(), config);
		}
		Global.BORROWCONFIG = map;
		Global.BORROW_CONFIG_LIST = list;

		// 读取通知类型配置
		List<NoticeType> noticeTypeList = noticeTypeDao.list();
		Map<String, Object> noticeTypeMap = new HashMap<String, Object>();
		for (int i = 0; i < noticeTypeList.size(); i++) {
			NoticeType noticeType = noticeTypeList.get(i);
			noticeTypeMap.put(
					noticeType.getNid() + "_" + noticeType.getNoticeType(),
					noticeType);
		}
		Global.NOTICE_TYPE_CONFIG = noticeTypeMap;

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
	public SystemConfig findByNid(String nid) {
		String hql = "from SystemConfig where nid='" + nid + "'";

		return systemConfigDao.findByHql(hql);
	}

}
