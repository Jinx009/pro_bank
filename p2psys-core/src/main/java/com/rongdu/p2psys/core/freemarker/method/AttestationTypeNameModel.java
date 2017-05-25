package com.rongdu.p2psys.core.freemarker.method;

import java.util.List;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.user.dao.UserDao;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 根据ID获取证件类型 或者获取证件列表(用户类型等)
 * 
 * @author lijie
 */
public class AttestationTypeNameModel implements TemplateMethodModel {
	private DictDao dictDao;
	private UserDao userDao;

	public AttestationTypeNameModel(DictDao dictDao, UserDao userdao) {
		this.dictDao = dictDao;
		this.userDao = userdao;
	}

	@Override
	public Object exec(List arg0) throws TemplateModelException {
		if (arg0.size() > 1) {
			if (arg0.get(0).equals("list")) {
				if (arg0.get(1).equals("usertype")) {
					return null;
					// return userDao.getAllUserType();
				}
			}
			if (arg0.get(1).equals("name")) {
				Dict dic = dictDao.find(StringUtil.toLong(arg0.get(0).toString()));
				if (dic != null) {
					return dic.getName();
				}
				return "";
			}
			if (arg0.get(1).equals("area")) {
				// return dictDao.getAreaByPid(arg0.get(0).toString());
				return null;
			}
			if (arg0.get(1).equals("usertype")) {
				return null;
				// return userDao.getUserTypeByid(arg0.get(0).toString());
			}
			if (arg0.get(1).equals("account_type")) {
				String value = arg0.get(0).toString();
				return dictDao.find(arg0.get(1).toString(), value);
			}
		}
		return "-";
	}

}
