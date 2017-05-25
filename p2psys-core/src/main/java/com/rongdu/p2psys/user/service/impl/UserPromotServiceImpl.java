package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.RandomUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPromotModel;
import com.rongdu.p2psys.user.service.UserPromotService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 推荐人Service
 * 
 * @author ZhuJunjie
 */
@Service("userPromotService")
public class UserPromotServiceImpl implements UserPromotService
{

	@Resource
	private UserDao userDao;
	@Resource
	private UserPromotDao userPromotDao;
	@Resource
	private UserInviteDao userInviteDao;
	@Resource
	private OperationLogDao operationLogDao;
	@Resource
	private UserService userService;

	private Logger logger = Logger.getLogger(UserPromotServiceImpl.class);

	@Override
	public Map<String, Object> userPromotAdd(String userName, int canUseTimes,
			double rate, Operator operator)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userService.getUserByUserName(userName);
		if (user == null)
		{
			map.put("result", false);
			map.put("msg", "该用户(" + userName + ")不存在,添加推荐人失败！");
			return map;
		} else
		{
			if (user.getUserCache().getUserType() == 2)
			{
				map.put("result", false);
				map.put("msg", "该用户(" + userName + ")是借款人不能添加为推荐人，添加推荐人失败！");
				return map;
			}
			UserPromot userPromot = userPromotDao.getUserPromotByUserId(user);
			if (userPromot != null)
			{
				map.put("result", false);
				map.put("msg", "该用户(" + userName + ")已经是推荐人,添加推荐人失败！");
				return map;
			}
		}
		UserPromot up = new UserPromot();
		up.setStatus(1);
		up.setUser(user);
		up.setCouponCode(generateCouponCode(4));
		up.setCanUseTimes(canUseTimes);
		up.setUsedTimes(0);
		up.setRate(rate);
		up.setAddTime(new Date());
		userPromotDao.save(up);
		OperationLog operationLog = new OperationLog(user, operator,
				Constant.ADD_USER_PROMOT);
		operationLog.setOperationResult("用户名为" + operator.getUserName()
				+ "的操作员添加推荐人" + user.getUserName() + "的操作成功！");
		operationLogDao.save(operationLog);
		map.put("result", true);
		map.put("msg", "保存成功！");
		return map;
	}

	@Override
	public void userPromotDelete(String userName, UserPromot promot,
			Operator operator)
	{
		List<UserPromot> list = new ArrayList<UserPromot>();
		list.add(promot);
		userPromotDao.delete(list);
		// 操作Log
		User user = userService.getUserByUserName(userName);
		OperationLog operationLog = new OperationLog(user, operator,
				Constant.DELETE_USER_PROMOT);
		operationLog.setOperationResult("用户名为" + operator.getUserName()
				+ "的操作员删除推荐人" + user.getUserName() + "的操作成功！");
		operationLogDao.save(operationLog);
	}

	@Override
	public void userPromotEdit(UserPromot userPromot)
	{
		userPromotDao.update(userPromot);
	}

	@Override
	public UserPromot getUserPromotById(long id)
	{
		return userPromotDao.find(id);
	}

	@Override
	public PageDataList<UserPromotModel> userPromotList(int pageNumber,int pageSize, UserModel model)
	{
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,pageSize);
		String sql = "   from rd_user_promot where user_id   in (select user_id from rd_user where user_name is not null) ORDER BY user_id desc ";
		if (model.getStatus() != 0)
		{
			param.addParam("status", Operators.EQ, model.getStatus());
		}
		PageDataList<UserPromot> plist = userPromotDao.userPromotList(sql,param);
		PageDataList<UserPromotModel> uList = new PageDataList<UserPromotModel>();
		uList.setPage(plist.getPage());
		List<UserPromotModel> list = new ArrayList<UserPromotModel>();
		if (plist.getList().size() > 0)
		{
			for (int i = 0; i < plist.getList().size(); i++)
			{
				UserPromot userPromot = (UserPromot) plist.getList().get(i);
				UserPromotModel userPromotModel = UserPromotModel.instance(userPromot);
				User user = userService.getUserById(userPromot.getUser().getUserId());
			
				userPromotModel.setUserName(user.getUserName());
				userPromotModel.setRealName(user.getRealName());
				list.add(userPromotModel);
			}
		}
		uList.setList(list);
		return uList;
	}

	@Override
	public PageDataList<User> userPromotDetailList(User user, int pageNumber,
			int pageSize, String searchName)
	{
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addOrder(OrderType.DESC, "u.user_id");
		if (!StringUtils.isBlank(searchName))
		{
			param.addParam("u.user_name", Operators.LIKE, searchName);
		}
		PageDataList<User> plist = userDao
				.findPageListBySql(
						"from rd_user u, rd_user_invite ui where u.user_id=ui.user_id and ui.invite_user="
								+ user.getUserId(), param);

		return plist;
	}

	@Override
	public PageDataList<UserModel> userPromotAllList(UserModel userModel)
	{
		QueryParam param = QueryParam.getInstance().addPage(
				userModel.getPage(), userModel.getSize());
		param.addOrder(OrderType.DESC, "u.user_id");
		if (!StringUtils.isBlank(userModel.getSearchName()))
		{
			UserInvite invite = userInviteDao.findObjByProperty(
					"inviteUser.userName", userModel.getSearchName());
			param.addParam("ui.invite_user", Operators.EQ, invite
					.getInviteUser().getUserId());
		} else
		{
			if (StringUtil.isNotBlank(userModel.getInviteUserName()))
			{
				UserInvite invite = userInviteDao.findObjByProperty(
						"inviteUser.userName", userModel.getInviteUserName());
				param.addParam("ui.invite_user", Operators.EQ, invite
						.getInviteUser().getUserId());
			}
			if (StringUtil.isNotBlank(userModel.getInviteRealName()))
			{
				UserInvite invite = userInviteDao.findObjByProperty(
						"inviteUser.realName", userModel.getInviteRealName());
				param.addParam("ui.invite_user", Operators.EQ, invite
						.getInviteUser().getUserId());
			}
		}
		PageDataList<User> plist = userDao
				.findPageListBySql(
						"from rd_user u, rd_user_invite ui where u.user_id=ui.user_id ",
						param);
		PageDataList<UserModel> plist_ = new PageDataList<UserModel>();
		List<UserModel> list = new ArrayList<UserModel>();
		if (plist.getList() != null && plist.getList().size() > 0)
		{
			for (User user : plist.getList())
			{
				UserModel model = UserModel.instance(user);
				UserInvite invite = userInviteDao.findObjByProperty(
						"user.userId", user.getUserId());
				if (invite != null)
				{
					model.setInviteUserName(invite.getInviteUser()
							.getUserName());
					model.setInviteRealName(invite.getInviteUser()
							.getRealName());
					model.setInviteTime(invite.getInviteTime());
				}
				list.add(model);
			}
		}
		plist_.setPage(plist.getPage());
		plist_.setList(list);
		return plist_;
	}

	@Override
	public boolean hasDuplicateCode(String code)
	{
		return userPromotDao.hasPromotByCouponCode(code);
	}

	@Override
	public UserPromot getUserPromotByCode(String code)
	{
		return userPromotDao.getUserPromotByCode(code);
	}

	private String generateCouponCode(int length)
	{
		String result = "";
		do
		{
			result = RandomUtil.getSpecialRandomCode(length);
		} while (hasDuplicateCode(result));
		return result;
	}

	@Override
	public PageDataList<UserPromotModel> exportUserPromotList(int pageNumber,
			int pageSize, UserModel model) {
		PageDataList<UserPromotModel> uList = new PageDataList<UserPromotModel>();
		uList.setPage(null);
		List<UserPromotModel> list=userPromotDao.exportUserPromotList(model.getStatus());
		uList.setList(list);
		
		return uList;
	}

	@Override
	public PageDataList<UserModel> exportUserPromotAllList(UserModel userModel) {
		Map<String,Object> param=new HashMap<String,Object>();
		
		if (!StringUtils.isBlank(userModel.getSearchName()))
		{
			
			param.put("inviteUser.userName", userModel.getSearchName());
		} else
		{
			if (StringUtil.isNotBlank(userModel.getInviteUserName()))
			{
				param.put("inviteUser.userName", userModel.getInviteUserName());
			}
			if (StringUtil.isNotBlank(userModel.getInviteRealName()))
			{
				param.put("inviteUser.realName", userModel.getInviteRealName());
			}
		}
		PageDataList<UserModel> plist_ = new PageDataList<UserModel>();
		List<UserModel> list=userDao.getUserModels(param);
		plist_.setPage(null);
		plist_.setList(list);
		return plist_;
	}
}
