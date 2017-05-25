package com.rongdu.p2psys.core.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.rongdu.p2psys.core.SystemInfo;
import com.rongdu.p2psys.core.domain.SystemConfig;

public interface SystemService {
	/**
	 * @return
	 */
	SystemInfo getSystemInfo();

	/**
	 * @return
	 */
	List<SystemConfig> getSystemInfoForList();

	/**
	 * 根据模块显示系统设置信息
	 * 
	 * @return
	 */
	List<SystemConfig> getSystemInfoForListBysytle(int i);

	/**
	 * @author lijie
	 * @param list <SystemConfig>
	 */
	void updateSystemInfo(List<SystemConfig> list, ServletContext context);

	/**
	 * @author lijie
	 * @param url 网站根目录路径
	 */
	void clean(String url);

	/**
	 * @param systemConfig
	 */
	void addSystemConfig(SystemConfig systemConfig);
}
