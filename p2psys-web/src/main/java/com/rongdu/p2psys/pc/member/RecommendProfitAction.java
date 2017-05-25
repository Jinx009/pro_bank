package com.rongdu.p2psys.pc.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.recommend.model.RecommendProfitRecordModel;
import com.rongdu.p2psys.nb.recommend.service.RecommendProfitRecordService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

public class RecommendProfitAction extends BaseAction<RecommendProfitRecordModel> implements ModelDriven<RecommendProfitRecordModel>
{
	private Map<String,Object> map;
	
	@Resource
	private RecommendProfitRecordService recommendProfitRecordService;

	@Action(value = "/nb/pc/member/recommendProfit")
	public String recommendProfitRecord() throws IOException
	{
		return "recommendProfit";
	}
	
	
	  /**
     * 推荐人收益记录列表
     * @throws IOException 
     */
	@Action("/nb/pc/recommendProfitRecordList")
	public void recommendProfitRecordList() throws IOException
	{
		if(hasSessionUser()){
			map =new HashMap<String, Object>();
			User user = getNBSessionUser();
			
			int pageNumber = paramInt("page");
			int pageSize = 12;
			
			model.setPage(pageNumber);
			model.setSize(pageSize);
			
			PageDataList<RecommendProfitRecordModel> list = recommendProfitRecordService.getProfitRecord(model,user);
			
			map.put(ConstantUtil.ERRORMSG,list);
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		}
		else
		{
			map = getErrorMap();
		}
		
		printJson(getStringOfJpaObj(map));
	}
}
