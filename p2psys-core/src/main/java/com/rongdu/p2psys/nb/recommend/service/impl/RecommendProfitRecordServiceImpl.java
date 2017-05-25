package com.rongdu.p2psys.nb.recommend.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitRecordDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;
import com.rongdu.p2psys.nb.recommend.model.RecommendProfitRecordModel;
import com.rongdu.p2psys.nb.recommend.service.RecommendProfitRecordService;
import com.rongdu.p2psys.user.domain.User;

@Service("recommendProfitRecordService")
public class RecommendProfitRecordServiceImpl implements RecommendProfitRecordService
{

	@Resource
	private RecommendProfitRecordDao recommendProfitRecordDao;

	@Override
	public PageDataList<RecommendProfitRecordModel> getProfitRecord(
			RecommendProfitRecordModel model)
	{
		QueryParam param = QueryParam.getInstance();
		if (null != model)
		{
			if (!StringUtil.isBlank(model.getInviteUserName()))
			{
				/**
				 * 模糊查询
				 */
				SearchFilter orFilter1 = new SearchFilter("inviteUser.realName", Operators.LIKE,model.getInviteUserName());
				SearchFilter orFilter2 = new SearchFilter("inviteUser.userName", Operators.LIKE,model.getInviteUserName());
				
				param.addOrFilter(orFilter1, orFilter2);
			}
			param.addOrder(OrderType.DESC, "id");
			param.addPage(model.getPage(), model.getSize());
		}
		PageDataList<RecommendProfitRecord> pageDataList = recommendProfitRecordDao.findPageList(param);
		PageDataList<RecommendProfitRecordModel> pageDataList_ = new PageDataList<RecommendProfitRecordModel>();
		List<RecommendProfitRecordModel> list = new ArrayList<RecommendProfitRecordModel>();
		pageDataList_.setPage(pageDataList.getPage());
		for (int i = 0; i < pageDataList.getList().size(); i++)
		{
			RecommendProfitRecord profitRecord = pageDataList.getList().get(i);
			RecommendProfitRecordModel profitRecordModel = RecommendProfitRecordModel.instance(profitRecord);
			profitRecordModel.setInviteUserName(profitRecord.getInviteUser().getRealName());
			profitRecordModel.setUserName(profitRecord.getUser().getRealName());
			profitRecordModel.setProjectName(profitRecord.getBorrow().getName());
			list.add(profitRecordModel);
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}
	@Override
	public PageDataList<RecommendProfitRecordModel> exportProfitRecord(
			RecommendProfitRecordModel model)
	{
		QueryParam param = QueryParam.getInstance();
		if (null != model)
		{
			if (!StringUtil.isBlank(model.getInviteUserName()))
			{
				/**
				 * 模糊查询
				 */
				SearchFilter orFilter1 = new SearchFilter("inviteUser.realName", Operators.LIKE,model.getInviteUserName());
				SearchFilter orFilter2 = new SearchFilter("inviteUser.userName", Operators.LIKE,model.getInviteUserName());
				
				param.addOrFilter(orFilter1, orFilter2);
			}
			param.addOrder(OrderType.DESC, "id");
//			param.addPage(model.getPage(), model.getSize());
		}
		PageDataList<RecommendProfitRecord> pageDataList = recommendProfitRecordDao.findAllPageList(param);
		PageDataList<RecommendProfitRecordModel> pageDataList_ = new PageDataList<RecommendProfitRecordModel>();
		List<RecommendProfitRecordModel> list = new ArrayList<RecommendProfitRecordModel>();
		pageDataList_.setPage(null);
		for (int i = 0; i < pageDataList.getList().size(); i++)
		{
			RecommendProfitRecord profitRecord = pageDataList.getList().get(i);
			RecommendProfitRecordModel profitRecordModel = RecommendProfitRecordModel.instance(profitRecord);
			profitRecordModel.setInviteUserName(profitRecord.getInviteUser().getRealName());
			profitRecordModel.setUserName(profitRecord.getUser().getRealName());
			profitRecordModel.setProjectName(profitRecord.getBorrow().getName());
			list.add(profitRecordModel);
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	public List<RecommendProfitRecordModel> getSelfRecommendProfit(long userId)
	{
		String hql = " from RecommendProfitRecord where inviteUser.userId = "+userId+"  ";
		
		List<RecommendProfitRecord> list = recommendProfitRecordDao.findByHql(hql);
		List<RecommendProfitRecordModel> list2 = new ArrayList<RecommendProfitRecordModel>();
		
		if(null!=list&&!list.isEmpty())
		{
			for(int i = 0;i<list.size();i++)
			{
				RecommendProfitRecordModel recommendProfitRecordModel = RecommendProfitRecordModel.instance(list.get(i));
				
				String userName = list.get(i).getUser().getUserName();
				String realName = userName.substring(0,userName.length()-(userName.substring(3)).length())+"****"+userName.substring(7);
				recommendProfitRecordModel.setUserName(realName);
				recommendProfitRecordModel.setMobilePhone(list.get(i).getUser().getUserName());
				
				list2.add(recommendProfitRecordModel);
			}
		}
		return list2;
	}

	@Override
	public PageDataList<RecommendProfitRecordModel> getProfitRecord(
			RecommendProfitRecordModel model, User user)
	{
		QueryParam param = QueryParam.getInstance();
		if (null != model)
		{
			param.addOrder(OrderType.DESC, "id");
			param.addPage(model.getPage(), model.getSize());
			param.addParam("inviteUser.userId", user.getUserId());
		}
		PageDataList<RecommendProfitRecord> pageDataList = recommendProfitRecordDao.findPageList(param);
		PageDataList<RecommendProfitRecordModel> pageDataList_ = new PageDataList<RecommendProfitRecordModel>();
		List<RecommendProfitRecordModel> list = new ArrayList<RecommendProfitRecordModel>();
		pageDataList_.setPage(pageDataList.getPage());
		for (int i = 0; i < pageDataList.getList().size(); i++)
		{
			RecommendProfitRecord profitRecord = pageDataList.getList().get(i);
			RecommendProfitRecordModel profitRecordModel = RecommendProfitRecordModel.instance(profitRecord);
			profitRecordModel.setInviteUserName(profitRecord.getInviteUser().getRealName());
			profitRecordModel.setUserName(profitRecord.getUser().getRealName());
			profitRecordModel.setProjectName(profitRecord.getBorrow().getName());
			profitRecordModel.setMobilePhone(profitRecord.getUser().getMobilePhone());
			profitRecordModel.setAddTime(profitRecord.getAddTime());
			list.add(profitRecordModel);
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

}
