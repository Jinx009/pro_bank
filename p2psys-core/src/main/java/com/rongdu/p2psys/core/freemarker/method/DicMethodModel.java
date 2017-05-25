package com.rongdu.p2psys.core.freemarker.method;

import java.util.List;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.domain.Dict;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 根据ID获取证件类型 或者获取证件列表(用户类型等)
 * 
 * @author lijie
 */
public class DicMethodModel implements TemplateMethodModel {
	private DictDao dictDao;

	public DicMethodModel(DictDao dictDao) {
		this.dictDao = dictDao;
	}

	@Override
	public Object exec(List arg) throws TemplateModelException {
		String nid = "", type = "", value = "";
		if (arg.size() == 3) {
			nid = StringUtil.isNull(arg.get(0));
			type = StringUtil.isNull(arg.get(1));
			value = StringUtil.isNull(arg.get(2));
		} else {
			return "Illegal arguments";
		}
		Dict l = null;
		if (type.equals("id")) {
			long id = StringUtil.toLong(value);
			l = dictDao.find(id);
		} else {
			l = dictDao.find(nid, value);
		}
		return l;
	}

}
