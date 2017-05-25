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
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.nb.recommend.model.RecommendRecordModel;
import com.rongdu.p2psys.nb.recommend.service.RecommendRecordService;
import com.rongdu.p2psys.nb.user.service.UserService;

@Service("recommendRecordService")
public class RecommendRecordServiceImpl implements RecommendRecordService
{

	@Resource
	private RecommendRecordDao recommendRecordDao;
	@Resource
	private UserService theUserService;

	@Override
	public PageDataList<RecommendRecordModel> getRecommendRecordList(
			RecommendRecordModel model)
	{
		QueryParam param = QueryParam.getInstance();
		if (null != model)
		{
			if (!StringUtil.isBlank(model.getInviteUserName()))
			{
				// 模糊查询条件
				SearchFilter orFilter1 = new SearchFilter(
						"inviteUser.realName", Operators.LIKE,
						model.getInviteUserName());
				SearchFilter orFilter2 = new SearchFilter(
						"inviteUser.userName", Operators.LIKE,
						model.getInviteUserName());
				param.addOrFilter(orFilter1, orFilter2);
			}
			param.addOrder(OrderType.DESC, "id");
			param.addPage(model.getPage(), model.getSize());
		}
		PageDataList<RecommendRecord> pageDataList = recommendRecordDao
				.findPageList(param);
		PageDataList<RecommendRecordModel> pageDataList_ = new PageDataList<RecommendRecordModel>();
		List<RecommendRecordModel> list = new ArrayList<RecommendRecordModel>();
		pageDataList_.setPage(pageDataList.getPage());
		for (int i = 0; i < pageDataList.getList().size(); i++)
		{
			RecommendRecord record = pageDataList.getList().get(i);
			RecommendRecordModel recordModel = RecommendRecordModel
					.instance(record);
			recordModel.setInviteUserName(record.getInviteUser().getRealName());
			recordModel.setUserName(record.getUser().getRealName());
			list.add(recordModel);
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	public void saveRecommendRecord(RecommendRecord recommendRecord)
	{
		recommendRecordDao.save(recommendRecord);
	}

}
