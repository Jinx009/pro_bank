package com.rongdu.p2psys.nb.recommend.service;

import java.util.List;

import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;

public interface RecommedProfitService
{
	public RecommendProfit saveRecommendProfit(RecommendProfit  recommendProfit);
	
	public void updateRecommednProfit(RecommendProfit recommendProfit);
	
	public void deleteRecommendProfit(long id);
	
	public List<RecommendProfit> getList();
}
