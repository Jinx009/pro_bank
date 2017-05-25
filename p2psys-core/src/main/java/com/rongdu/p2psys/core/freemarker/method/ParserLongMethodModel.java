package com.rongdu.p2psys.core.freemarker.method;

import java.util.List;

import com.rongdu.common.util.StringUtil;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * @author lk
 */
public class ParserLongMethodModel implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		if (args == null || args.size() < 1)
			return 0;
		Object str = args.get(0);
		String tmp = StringUtil.isNull(str).replaceAll(",", "");

		long d = 0;
		d = StringUtil.toLong(tmp);
		return d;
	}
}
