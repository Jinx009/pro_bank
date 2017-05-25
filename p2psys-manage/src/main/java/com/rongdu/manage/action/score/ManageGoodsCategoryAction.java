package com.rongdu.manage.action.score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.domain.GoodsCategory;
import com.rongdu.p2psys.score.model.GoodsCategoryModel;
import com.rongdu.p2psys.score.service.GoodsCategoryService;

public class ManageGoodsCategoryAction extends BaseAction implements ModelDriven<GoodsCategoryModel> {

	private GoodsCategoryModel model = new GoodsCategoryModel();
	
	@Override
	public GoodsCategoryModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Resource
	private GoodsCategoryService goodsCategoryService;
	
	private Map<String, Object> data;
	
	@Action("/modules/user/score/goodsCategoryManager")
	public String goodsCategoryManager(){
		return "goodsCategoryManager";
	}
	
	@Action("/modules/user/score/goodsCategoryList")
	public void goodsCategoryList() throws Exception{
		List<GoodsCategory> list = goodsCategoryService.getCategoryList(model);
		data = new HashMap<String, Object>();
		data.put("total", 0); // 总行数
		data.put("rows", list); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	@Action("/modules/user/score/addGoodsCategoryPage")
	public String addGoodsCategoryPage(){
		this.saveToken("addToken");
		return "addGoodsCategoryPage";
	}
	
	@Action("/modules/user/score/addGoodsCategory")
	public void addGoodsCategory() throws Exception{
		checkToken("addToken");
		model.setAddOparetor(this.getOperatorUserName());
		Boolean result = goodsCategoryService.addCategory(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10001"), true);
		} else {
			printResult(MessageUtil.getMessage("I10004"), false);
		}
	}
	
	@Action("/modules/user/score/editGoodsCategoryPage")
	public String editGoodsCategoryPage(){
		this.saveToken("editToken");
		GoodsCategory category = goodsCategoryService.getCategoryById(model.getId());
		request.setAttribute("category", category);
		return "editGoodsCategoryPage";
	}
	
	@Action("/modules/user/score/editGoodsCategory")
	public void editGoodsCategory() throws Exception{
		checkToken("editToken");
		Boolean result = goodsCategoryService.editCategory(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10002"), true);
		} else {
			printResult(MessageUtil.getMessage("I10005"), false);
		}
	}
}
