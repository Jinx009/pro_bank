package com.rongdu.p2psys.core.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class AccountInfoDirectiveModel implements TemplateDirectiveModel
{

	private static Logger logger = Logger
			.getLogger(AccountInfoDirectiveModel.class);

	private static final String ID = "id";

	private UserDao userDao;

	private AccountDao accountDao;

	private UserService userService;

	public AccountInfoDirectiveModel(UserDao userDao, AccountDao accountDao)
	{
		this.userDao = userDao;
		this.accountDao = accountDao;
	}

	@Override
	public void execute(Environment environment, Map map,
			TemplateModel[] atemplatemodel,
			TemplateDirectiveBody templatedirectivebody)
			throws TemplateException, IOException
	{

		Iterator it = map.entrySet().iterator();
		String id = "";
		while (it.hasNext())
		{
			Map.Entry entry = (Entry) it.next();
			String paramName = entry.getKey().toString();
			TemplateModel paramValue = (TemplateModel) entry.getValue();
			logger.debug("name:" + paramName);
			logger.debug("r:" + paramValue);
			if (paramName.equals(ID))
			{
				id = paramValue.toString();
			}
		}
		String result = html(id);
		Writer out = environment.getOut();
		out.write(result);
	}

	private String html(String id)
	{
		StringBuffer sb = new StringBuffer();
		if (!id.equals(""))
		{
			User user = userService.getUserById(NumberUtil.getLong(id));
			double total = accountDao.getAccountByUserId(user.getUserId())
					.getTotal();
			sb.append(total);
		}
		return sb.toString();
	}

}
