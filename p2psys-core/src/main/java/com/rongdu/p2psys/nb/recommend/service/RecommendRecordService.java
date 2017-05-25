package com.rongdu.p2psys.nb.recommend.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.nb.recommend.model.RecommendRecordModel;

public interface RecommendRecordService
{

	/**
	 * 推荐人记录列表信息
	 * 
	 * @param model
	 * @return
	 */
	public PageDataList<RecommendRecordModel> getRecommendRecordList(RecommendRecordModel model);

	public void saveRecommendRecord(RecommendRecord recommendRecord);
}
