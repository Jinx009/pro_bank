package com.rongdu.manage.action.score;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.domain.GoodsCategory;
import com.rongdu.p2psys.score.domain.GoodsPic;
import com.rongdu.p2psys.score.model.GoodsModel;
import com.rongdu.p2psys.score.service.GoodsCategoryService;
import com.rongdu.p2psys.score.service.GoodsPicService;
import com.rongdu.p2psys.score.service.GoodsService;

public class ManageGoodsAction extends BaseAction implements ModelDriven<GoodsModel> {

	private GoodsModel model = new GoodsModel();
	
	@Override
	public GoodsModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Resource
	private GoodsService goodsService;
	@Resource
	private GoodsCategoryService goodsCategoryService;
	@Resource
	private GoodsPicService goodsPicService;
	
	private Map<String, Object> data;
	
	@Action("/modules/user/score/goodsManager")
	public String goodsManager(){
		return "goodsManager";
	}
	
	@Action("/modules/user/score/goodsList")
	public void goodsList() throws Exception{
		String status = this.paramString("status");
		if(status.length() <= 0){
			model.setStatus((byte)-99);
		}
		String type = this.paramString("type");
		if(type.length() <= 0){
			model.setType((byte)-99);	
		}
		PageDataList<GoodsModel> page = goodsService.getGoodsPage(model);
		data = new HashMap<String, Object>();
		if(page.getPage() == null){
			data.put("total", 0); // 总行数
		}else {
			data.put("total", page.getPage().getTotal()); // 总行数
		}
		data.put("rows", page.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	@Action("/modules/user/score/addGoodsPage")
	public String addGoodsPage(){
		this.saveToken("addToken");
		List<GoodsCategory> itemList =goodsCategoryService.getCategoryAll();
		request.setAttribute("itemList", itemList);
		return "addGoodsPage";
	}
	
	@Action("/modules/user/score/addGoods")
	public void addGoods() throws Exception{
		checkToken("addToken");
		long categoryId = this.paramLong("categoryId");
		model.setGoodsCategory(new GoodsCategory(categoryId));
		model.setAddOparetorId(this.getOperatorId());
		Boolean result = goodsService.addGoods(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10001"), true);
		} else {
			printResult(MessageUtil.getMessage("I10004"), false);
		}
	}
	
	@Action("/modules/user/score/editGoodsPage")
	public String editGoodsPage(){
		this.saveToken("editToken");
		List<GoodsCategory> itemList =goodsCategoryService.getCategoryAll();
		Goods goods = goodsService.getGoodsById(model.getId());
		List<GoodsPic> picList = goodsPicService.getGoodsPicByGoodsId(goods.getId());
		request.setAttribute("goods", goods);
		request.setAttribute("itemList", itemList);
		request.setAttribute("picList", picList);
		request.setAttribute("category", goods.getGoodsCategory());
		return "editGoodsPage";
	}
	
	@Action("/modules/user/score/editGoods")
	public void editGoods() throws Exception{
		checkToken("editToken");
		long categoryId = this.paramLong("categoryId");
		model.setGoodsCategory(new GoodsCategory(categoryId));
		model.setUpdateTime(new Date());;
		Boolean result = goodsService.editGoods(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10002"), true);
		} else {
			printResult(MessageUtil.getMessage("I10005"), false);
		}
	}
	
	
	@Action("/modules/user/score/verifyGoodsPage")
	public String verifyGoodsPage(){
		this.saveToken("verifyToken");
		Goods goods = goodsService.getGoodsById(model.getId());
		List<GoodsPic> picList = goodsPicService.getGoodsPicByGoodsId(goods.getId());
		request.setAttribute("goods", goods);
		request.setAttribute("picList", picList);
		request.setAttribute("category", goods.getGoodsCategory());
		return "verifyGoodsPage";
	}
	
	@Action("/modules/user/score/verifyGoods")
	public void verifyGoods() throws Exception{
		checkToken("verifyToken");
		model.setOperatorId(this.getOperatorId());
		Boolean result = goodsService.verifyGoods(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10009"), result);
		} else {
			printResult(MessageUtil.getMessage("I10019"), result);
		}
	}
	
	@Action("/modules/user/score/shelvesGoodsPage")
	public String shelvesGoodsPage(){
		this.saveToken("verifyToken");
		Goods goods = goodsService.getGoodsById(model.getId());
		List<GoodsPic> picList = goodsPicService.getGoodsPicByGoodsId(goods.getId());
		request.setAttribute("goods", goods);
		request.setAttribute("picList", picList);
		request.setAttribute("category", goods.getGoodsCategory());
		return "shelvesGoodsPage";
	}
	
	@Action("/modules/user/score/shelvesGoods")
	public void upShelvesGoods() throws Exception{
		model.setOperatorId(this.getOperatorId());
		Boolean result = goodsService.shelvesGoods(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10009"), result);
		} else {
			printResult(MessageUtil.getMessage("I10019"), result);
		}
	}
}
