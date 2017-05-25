package com.rongdu.p2psys.core.executer;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.Global;

/**
 * 事件管理器 初始化系统自定对象
 * 
 * @author zxc
 */
public class ExecuterHelper {
	static Logger logger = Logger.getLogger(ExecuterHelper.class);

	public static void init() {
		Properties prop = new Properties();
		InputStream in = ExecuterHelper.class.getResourceAsStream("executers.properties");
		try {
			prop.load(in);
			Set<Object> keySet = prop.keySet();
			logger.info("系统指定初始化bean:.......");
			for (Object object : keySet) {
				String key = object.toString();
				String value = prop.getProperty(key).trim();
				logger.info("bean:" + key + "------->" + value);
				AbstractExecuter executer = (AbstractExecuter) Class.forName(value).newInstance();
				Global.EXECUTOR_MAP.put(key, executer);
			}
		} catch (Exception e) {
			logger.error("初始化系统指定bean报错..", e);
			throw new RuntimeException("初始化系统指定bean报错..", e);
		}
	}

	/**
	 * 根据key获取，逻辑bean
	 * 
	 * @param key properties中的key
	 * @return 逻辑bean
	 */
	
	public static AbstractExecuter doExecuter(String key) {
		AbstractExecuter executer = Global.EXECUTOR_MAP.get(key);
		return executer;
	}

}
