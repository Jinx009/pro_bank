package com.rongdu.manage.action.nb.recommend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.recommend.model.RecommendRecordModel;
import com.rongdu.p2psys.nb.recommend.service.RecommendRecordService;

public class RecommendRecordAction extends BaseAction<RecommendRecordModel> implements ModelDriven<RecommendRecordModel> {
	
	@Resource
	private RecommendRecordService recommendRecordService;
	
	private Map<String, Object> data = null;
	
	/**
	 * 推荐纪录页面
	 * @return
	 */
	@Action("/modules/nb/recommend/recommendRecord")
	public String recommendRecordPage(){
		return "recommendRecord";
	}
	
	/**
	 * 推荐纪录列表
	 * @throws IOException 
	 */
	@Action("/modules/nb/recommend/recommendRecordList")
	public void recommendRecordList() throws IOException{
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		model.setPage(pageNumber);
		model.setSize(pageSize);
		PageDataList<RecommendRecordModel> list = recommendRecordService.getRecommendRecordList(model);
		data =new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
}
