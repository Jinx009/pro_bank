package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.model.UserInviteModel;
import com.rongdu.p2psys.user.service.UserInviteService;

@Service("userInviteService")
public class UserInviteServiceImpl implements UserInviteService
{

	@Resource
	private UserInviteDao userInviteDao;

	@Override
	public PageDataList<UserInviteModel> findByInviteUser(User user, int page)
	{
		QueryParam param = QueryParam.getInstance();
		
		param.addParam("inviteUser", user);
		param.addOrder(OrderType.DESC,"id");
		param.addPage(page);
		
		PageDataList<UserInvite> pageDataList = userInviteDao.findPageList(param);
		PageDataList<UserInviteModel> pageDataList_ = new PageDataList<UserInviteModel>();
		List<UserInviteModel> list = new ArrayList<UserInviteModel>();
		pageDataList_.setPage(pageDataList.getPage());
		
		if (pageDataList.getList().size() > 0)
		{
			for (UserInvite ui : pageDataList.getList())
			{
				UserInviteModel uim = UserInviteModel.instance(ui);
				uim.setInviteUserName(ui.getInviteUser().getUserName());
				;
				uim.setUserName(ui.getUser().getUserName());
				uim.setInviteUser(null);
				uim.setUser(null);
				list.add(uim);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PageDataList<UserInviteModel> findByModel(UserInviteModel model)
	{
		QueryParam param = QueryParam.getInstance();
		if (StringUtil.isNotBlank(model.getSearchName()))
		{
			SearchFilter orFilter1 = new SearchFilter("user.userName",
					Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("user.realName",
					Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2);
		}
		if (model.getStatus() == 1)
		{
			param.addParam("isGift", false);
		} 
		else if (model.getStatus() == 2)
		{
			param.addParam("isGift", true);
		}
		if (model.getRealNameStatus() == 1)
		{
			param.addParam("user.realName", Operators.NOTEQ,
					SearchFilter.EMPTY_AND_NULL);
		} 
		else if (model.getRealNameStatus() == 2)
		{
			param.addParam("user.realName", SearchFilter.EMPTY_AND_NULL);
		}
		if (StringUtil.isNotBlank(model.getUserName()))
		{
			param.addParam("user.userName", Operators.LIKE, model.getUserName());
		}
		if (StringUtil.isNotBlank(model.getInviteUserName()))
		{
			param.addParam("inviteUser.userName", Operators.LIKE,
					model.getInviteUserName());
		}
		if (StringUtil.isNotBlank(model.getRegStartTime()))
		{
			param.addParam("user.addTime", Operators.GT,
					DateUtil.valueOf(model.getRegStartTime()));
		}
		if (StringUtil.isNotBlank(model.getRegEndTime()))
		{
			Date date = DateUtil.valueOf(model.getRegEndTime());
			param.addParam("user.addTime", Operators.LT,
					DateUtil.rollDay(date, 1));
		}
		param.addPage(model.getPage(), model.getRows());
		PageDataList<UserInvite> pageDataList = userInviteDao.findPageList(param);
		PageDataList<UserInviteModel> pageDataList_ = new PageDataList<UserInviteModel>();
		List<UserInviteModel> list = new ArrayList<UserInviteModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0)
		{
			for (UserInvite ui : pageDataList.getList())
			{
				User user = ui.getUser();
				UserInviteModel uim = UserInviteModel.instance(ui);
				uim.setInviteUserName(ui.getInviteUser().getUserName());
				uim.setInviteRealName(ui.getInviteUser().getRealName());
				uim.setUserName(user.getUserName());
				uim.setRealName(user.getRealName());
				uim.setRegTime(user.getAddTime());
				uim.setInviteUser(null);
				uim.setUser(null);
				list.add(uim);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

}
