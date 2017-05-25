package com.rongdu.p2psys.core.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.dao.ArticleDao;
import com.rongdu.p2psys.core.domain.Site;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 各种下拉框的html输出
 * 
 * @author fuxingxing usage: <@site name="pid" value=1></@site>
 * @param name 表单名字,String类型,不能为空
 * @param id 表单的id,String类型，可以为空
 * @param clazz 表单的class,String类型，可以为空
 * @param pid 选择地区的父编码,Number类型
 * @param value 表单的默认值，String类型。
 * @return 返回拼装出来的html字符串
 */
public class SiteDirectiveModel implements TemplateDirectiveModel {

	private static Logger logger = Logger.getLogger(SiteDirectiveModel.class);

	private static final String NAME = "name";
	private static final String ID = "id";
	private static final String CLASS = "class";
	private static final String PID = "pid";
	private static final String DEFUALT = "default";

	private ArticleDao dao;

	public SiteDirectiveModel(ArticleDao dao) {
		super();
		this.dao = dao;
	}

	public SiteDirectiveModel() {
		super();
	}

	@Override
	public void execute(Environment env, Map map, TemplateModel[] loopVars, TemplateDirectiveBody body)
		throws TemplateException, IOException {
		Iterator it = map.entrySet().iterator();
		String name = "", id = "", clazz = "", value = "", pid = "", defaultValue = "";
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			String paramName = entry.getKey().toString();
			TemplateModel paramValue = (TemplateModel) entry.getValue();
			logger.debug("name:" + paramName);
			logger.debug("r:" + paramValue);
			if (paramName.equals(NAME)) {
				name = paramValue.toString();
			} else if (paramName.equals(ID)) {
				id = paramValue.toString();
			} else if (paramName.equals(CLASS)) {
				clazz = paramValue.toString();
			} else if (paramName.equals(PID)) {
				pid = paramValue.toString();
			} else if (paramName.equals(DEFUALT)) {
				defaultValue = paramValue.toString();
			}
		}
		String result = html(name, defaultValue);
		Writer out = env.getOut();
		out.write(result);
	}

	/**
	 * @return 返回拼装出来的html字符串
	 */
	private String html(String name, String value) {
		List<Site> list = new ArrayList<Site>();

		// list = dao.getSiteList();
		StringBuffer sb = new StringBuffer();
		sb.append("<select name=\"").append(name);
		sb.append("\">");
		//
		sb.append("<option value=\"0\">请选择</option>");
		for (Site s : list) {
			sb.append(option(s.getId(), s.getName(), value));
		}
		sb.append("</select>");
		return sb.toString();
	}

	private String option(long id, String name, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append("<option value=\"").append(id).append("\"");
		if (value.equals(id + "")) {
			sb.append(" selected=\"selected\" ");
		}
		sb.append(">").append(name).append("</option>");
		return sb.toString();
	}

}
