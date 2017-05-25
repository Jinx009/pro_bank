package com.rongdu.manage.action.nb.recommend;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.recommend.model.RecommendProfitRecordModel;
import com.rongdu.p2psys.nb.recommend.service.RecommendProfitRecordService;

public class RecommendProfitRecordAction extends BaseAction<RecommendProfitRecordModel> implements ModelDriven<RecommendProfitRecordModel> {

	@Resource
	private RecommendProfitRecordService recommendProfitRecordService;
	
	private Map<String, Object> data = null;
	
	/**
	 * 推荐人收益记录页面
	 * @return
	 */
	@Action("/modules/nb/recommend/recommendProfitRecord")
	public String recommendProfitRecord(){
		return "recommendProfitRecord";
	}
	
    /**
     * 推荐人收益记录列表
     * @throws IOException 
     */
	@Action("/modules/nb/recommend/recommendProfitRecordList")
	public void recommendProfitRecordList() throws IOException{
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		model.setPage(pageNumber);
		model.setSize(pageSize);
		PageDataList<RecommendProfitRecordModel> list = recommendProfitRecordService.getProfitRecord(model);
		data =new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
     * 推荐人收益记录列表
     * @throws IOException 
     */
	@Action("/modules/nb/recommend/exportRecommendProfitRecordList")
	public void exportRecommendProfitRecordList() throws  Exception{
//		int pageNumber = paramInt("page");
//		int pageSize = paramInt("rows");
//		model.setPage(pageNumber);
//		model.setSize(pageSize);
		List<RecommendProfitRecordModel> list = recommendProfitRecordService.exportProfitRecord(model).getList();
		String title = "推荐人收益纪录Excel表";

		String[] hearders = new String[] { "编号", "推荐人", "投资人", "项目", "推荐人获取的收益", "投资人投资金额 ", "添加时间" };// 表头数组
		String[] fields = new String[] { "id", "inviteUserName", "userName", "projectName", "money", "account", "addTime" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
}
