package com.rongdu.p2psys.nb.recommend.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;
import com.rongdu.p2psys.nb.recommend.service.RecommedProfitService;

@Service("recommendProfitService")
public class RecommendProfitServiceImpl implements RecommedProfitService 
{
	@Resource
	private RecommendProfitDao recommendProfitDao;

	public RecommendProfit saveRecommendProfit(RecommendProfit recommendProfit)
	{
		return recommendProfitDao.save(recommendProfit);
	}

	public void updateRecommednProfit(RecommendProfit recommendProfit)
	{
		recommendProfitDao.update(recommendProfit);
	}

	public void deleteRecommendProfit(long id)
	{
		recommendProfitDao.delete(id);
	}

	public List<RecommendProfit> getList()
	{
		return recommendProfitDao.findAll();
	}

}
