package com.rongdu.p2psys.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.dao.UserFreezeDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserFreeze;
import com.rongdu.p2psys.user.model.UserFreezeModel;
import com.rongdu.p2psys.user.service.UserFreezeService;
import com.rongdu.p2psys.user.service.UserService;

@Service("freezeService")
public class UserFreezeServiceImpl implements UserFreezeService
{

	@Resource
	private UserFreezeDao userFreezeDao;
	@Resource
	private UserService userService;
	@Resource
	private OperationLogDao operationLogDao;

	@Override
	public PageDataList<UserFreezeModel> freezeList(int pageNumber,
			int pageSize, UserFreezeModel model)
	{
		return userFreezeDao.freezeList(pageNumber, pageSize, model);
	}

	@Override
	public Map<String, Object> freezeAdd(UserFreezeModel model,
			Operator operator)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (model.getMark() == null)
		{
			map.put("result", false);
			map.put("msg", "冻结点不能为空！");
			return map;
		}
		if (!StringUtil.isBlank(model.getUserName()))
		{
			model.setMark(model.getMark().replaceAll(" ", ""));
			User user = userService.getUserByUserName(model.getUserName());
			if (user == null)
			{
				map.put("result", false);
				map.put("msg", "该用户(" + model.getUserName() + ")不存在,添加冻结信息失败！");
				return map;
			}
			if (userFreezeDao.isExistsByUserName(model.getUserName()))
			{
				map.put("result", false);
				map.put("msg", "该用户(" + model.getUserName()
						+ ")的冻结信息已存在,请勿重复添加！");
				return map;
			}
			model.setUser(user);
		}
		model.setVerifyUser(operator);
		model.setAddTime(new Date());
		model.setAddIp(Global.getIP());
		UserFreeze freeze = model.prototype();
		userFreezeDao.save(freeze);

		User user = userService.getUserByUserName(model.getUserName());
		OperationLog operationLog = new OperationLog(user, operator,
				Constant.FREEZE);
		operationLog.setOperationResult("用户名为" + operator.getUserName()
				+ "的操作员冻结" + user.getUserName() + "的" + model.getMark() + "操作");
		operationLogDao.save(operationLog);
		map.put("result", true);
		map.put("msg", "保存成功！");
		return map;

	}

	@Override
	public UserFreeze find(long id)
	{
		return userFreezeDao.find(id);
	}

	@Override
	public String freezeEdit(UserFreezeModel model, Operator operator)
	{
		if (model.getMark() == null)
		{
			return "冻结点不能为空！";
		}
		model.setMark(model.getMark().replaceAll(" ", ""));
		UserFreeze userFreeze = userFreezeDao.find(model.getId());
		userFreeze.setMark(model.getMark());
		userFreeze.setStatus(model.getStatus());
		userFreeze.setRemark(model.getRemark());
		// userFreezeDao.freezeEdit(model);
		User user = userService.getUserByUserName(model.getUserName());
		OperationLog operationLog = new OperationLog(user, operator,
				Constant.FREEZE);
		operationLog.setOperationResult("用户名为" + operator.getUserName()
				+ "的操作员修改冻结" + user.getUserName() + "的" + model.getMark()
				+ "操作");
		operationLogDao.save(operationLog);
		return "SUCCESS";
	}

	@Override
	public void freezeDelete(long id, int status)
	{

		userFreezeDao.freezeDelete(id, status);
	}

	@Override
	public UserFreeze getByUserName(String userName)
	{
		return userFreezeDao.getByUserName(userName);
	}

	@Override
	public UserFreeze getByUserId(long userId)
	{
		return userFreezeDao.getByUserId(userId);
	}
}
