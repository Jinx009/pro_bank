package com.rongdu.p2psys.core.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 工具类-spring bean
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月11日
 */
public class BeanUtil {
	private static WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

	/**
	 * 根据bean名称获取
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return ctx.getBean(name);
	}
}
