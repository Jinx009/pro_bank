package com.rongdu.p2psys.web.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 前台清理系统缓存
 * @author yinliang
 * @version 2.0
 * @Date   2014年12月24日
 */
@SuppressWarnings("rawtypes")
public class SystemCacheAction extends BaseAction{
	@Resource
	private SystemConfigService systemConfigService;
	
	@Action("/system/clean")
	public String clean() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			systemConfigService.clean();
			map.put("msg", "ok");
			printJson(map);
		} catch (Exception e) {
			map.put("msg", "fail");
			printJson(map);
		}
		return null;
	}

}
