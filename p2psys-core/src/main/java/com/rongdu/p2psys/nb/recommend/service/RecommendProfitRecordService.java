package com.rongdu.p2psys.nb.recommend.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.recommend.model.RecommendProfitRecordModel;
import com.rongdu.p2psys.user.domain.User;

public interface RecommendProfitRecordService
{

	/**
	 * 获取推荐人收益纪录信息
	 * 
	 * @param model
	 * @return
	 */
	public PageDataList<RecommendProfitRecordModel> getProfitRecord(RecommendProfitRecordModel model);
	public PageDataList<RecommendProfitRecordModel> exportProfitRecord(RecommendProfitRecordModel model);

	public List<RecommendProfitRecordModel> getSelfRecommendProfit(long userId);

	public PageDataList<RecommendProfitRecordModel> getProfitRecord(RecommendProfitRecordModel model, User user);

}
