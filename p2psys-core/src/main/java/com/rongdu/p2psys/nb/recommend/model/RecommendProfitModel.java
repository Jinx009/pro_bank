package com.rongdu.p2psys.nb.recommend.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;

public class RecommendProfitModel extends RecommendProfit
{
	private static final long serialVersionUID = -1413374160071603990L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;
	
	public static RecommendProfitModel instance(RecommendProfit recommendProfit)
	{
		RecommendProfitModel model = new RecommendProfitModel();
		BeanUtils.copyProperties(recommendProfit, model);
		return model;
	}

	public RecommendProfit protoType()
	{
		RecommendProfit recommendProfit = new RecommendProfit();
		BeanUtils.copyProperties(this, recommendProfit);
		return recommendProfit;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}
	
	
	
	
}
