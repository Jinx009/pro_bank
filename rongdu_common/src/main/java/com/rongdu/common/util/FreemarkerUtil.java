package com.rongdu.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 工具类-freemarker的模板处理
 * 
 * @author fxx
 * @version 2.0
 * @since 2014年1月28日
 */
public class FreemarkerUtil {

	private static Logger logger = Logger.getLogger(FreemarkerUtil.class);

	public static Configuration CONFIG;

	public static String renderTemplate(String s, Map<String, Object> data) throws IOException, TemplateException {
		Template t = new Template(null, new StringReader(s), CONFIG);
		// 执行插值，并输出到指定的输出流中
		StringWriter w = new StringWriter();
		t.getConfiguration();
		try {
			t.process(data, w);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(data.get("noticeTime"));
		}

		return w.getBuffer().toString();
	}

	public static String renderFileTemplate(String file, Map<String, Object> data) throws IOException,
		TemplateException {
		Configuration cfg = CONFIG;
		cfg.setDefaultEncoding("UTF-8");
		// 取得模板文件
		Template t = cfg.getTemplate(file);
		// 执行插值，并输出到指定的输出流中
		StringWriter w = new StringWriter();
		t.getConfiguration();
		t.process(data, w);
		return w.getBuffer().toString();
	}

}
