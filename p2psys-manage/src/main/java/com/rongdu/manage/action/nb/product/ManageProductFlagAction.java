package com.rongdu.manage.action.nb.product;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.constant.ProductTypeFlagConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;
import com.rongdu.p2psys.nb.product.model.ProductTypeFlagModel;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

public class ManageProductFlagAction extends BaseAction<ProductTypeFlagModel>
		implements ModelDriven<ProductTypeFlagModel> {

	@Resource
	private ProductTypeFlagService productTypeFlagService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageProductFlagAction.class);

	/**
	 * 产品标签 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagManager")
	public String productTypeFlagManager() throws Exception {
		return "productTypeFlagManager";
	}

	/**
	 * 产品标签 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagList")
	public void productTypeFlagList() throws Exception {
		model.setPage(paramInt("page"));
		model.setSize(paramInt("rows"));
		PageDataList<ProductTypeFlagModel> list = productTypeFlagService
				.getModelPageList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加标签 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagAddPage")
	public String productTypeFlagAddPage() throws Exception {
		request.setAttribute("adminUrl", productTypeFlagService.getAdminUrl());

		return "productTypeFlagAddPage";
	}

	/**
	 * 添加标签 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagAdd")
	public void productTypeFlagAdd() throws Exception {
		productTypeFlagService.saveProductTypeFlag(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 查看标签 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagViewPage")
	public String productTypeFlagViewPage() throws Exception {
		ProductTypeFlag productTypeFlag = productTypeFlagService
				.findById(paramLong("id"));

		request.setAttribute("productTypeFlag", productTypeFlag);
		return "productTypeFlagViewPage";
	}

	/**
	 * 编辑标签 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagEditPage")
	public String productTypeFlagEditPage() throws Exception {
		ProductTypeFlag productTypeFlag = productTypeFlagService
				.findById(paramLong("id"));

		request.setAttribute("adminUrl", productTypeFlagService.getAdminUrl());
		request.setAttribute("productTypeFlag", productTypeFlag);
		return "productTypeFlagEditPage";
	}

	/**
	 * 编辑标签 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagEdit")
	public void productTypeFlagEdit() throws Exception {
		productTypeFlagService.modifyProductTypeFlag(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改标签状态为全部生效
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagAllEnable")
	public void productTypeFlagAllEnable() throws Exception {
		ProductTypeFlag pojo = productTypeFlagService.findById(paramLong("id"));

		pojo.setIsEnable(ProductTypeFlagConstant.SHOW_PC_AND_WECHAT);

		productTypeFlagService.updateProductTypeFlag(pojo);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改标签状态为PC生效
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagPcEnable")
	public void productTypeFlagPcEnable() throws Exception {
		ProductTypeFlag pojo = productTypeFlagService.findById(paramLong("id"));

		pojo.setIsEnable(ProductTypeFlagConstant.SHOW_PC_ONLY);

		productTypeFlagService.updateProductTypeFlag(pojo);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改标签状态为微信生效
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagWechatEnable")
	public void productTypeFlagWechatEnable() throws Exception {
		ProductTypeFlag pojo = productTypeFlagService.findById(paramLong("id"));

		pojo.setIsEnable(ProductTypeFlagConstant.SHOW_WECHAT_ONLY);

		productTypeFlagService.updateProductTypeFlag(pojo);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改标签状态为全部失效
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagAllDisable")
	public void productTypeFlagAllDisable() throws Exception {
		ProductTypeFlag pojo = productTypeFlagService.findById(paramLong("id"));

		pojo.setIsEnable(ProductTypeFlagConstant.SHOW_NONE);

		productTypeFlagService.updateProductTypeFlag(pojo);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改标签排序
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagRecommend")
	public void productTypeFlagRecommend() throws Exception {
		ProductTypeFlag pojo = productTypeFlagService.findById(paramLong("id"));

		pojo.setRecommendTime(new Date());
		productTypeFlagService.updateProductTypeFlag(pojo);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 删除标签
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeFlagDelete")
	public void productTypeFlagDelete() throws Exception {
		productTypeFlagService.deleteProductTypeFlag(paramLong("id"));

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, true);
		data.put(ConstantUtil.MSG, "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

}
