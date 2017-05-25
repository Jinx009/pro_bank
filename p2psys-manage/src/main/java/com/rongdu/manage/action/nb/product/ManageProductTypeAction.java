package com.rongdu.manage.action.nb.product;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.model.ProductTypeModel;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;

public class ManageProductTypeAction extends BaseAction<ProductTypeModel>
		implements ModelDriven<ProductTypeModel> {

	@Resource
	private ProductTypeService productTypeService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageProductTypeAction.class);

	/**
	 * 产品类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeManager")
	public String productTypeManager() throws Exception {
		return "productTypeManager";
	}

	/**
	 * 产品类别 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeList")
	public void productTypeList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProductTypeModel> list = productTypeService
				.getModelPageList(pageNumber, pageSize);
		model.setSize(pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeAddPage")
	public String productTypeAddPage() throws Exception {
		request.setAttribute("adminUrl", productTypeService.getAdminUrl());

		return "productTypeAddPage";
	}

	/**
	 * 添加类别 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeAdd")
	public void productTypeAdd() throws Exception {
		productTypeService.saveProductType(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 查看类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeViewPage")
	public String productTypeViewPage() throws Exception {
		ProductType productType = productTypeService.findById(paramLong("id"));

		request.setAttribute("productType", productType);
		return "productTypeViewPage";
	}

	/**
	 * 编辑类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeEditPage")
	public String productTypeEditPage() throws Exception {
		ProductType productType = productTypeService.findById(paramLong("id"));

		request.setAttribute("adminUrl", productTypeService.getAdminUrl());
		request.setAttribute("productType", productType);
		return "productTypeEditPage";
	}

	/**
	 * 编辑类别 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeEdit")
	public void productTypeEdit() throws Exception {
		productTypeService.modifyProductType(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改类别有效状态
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeEnable")
	public void productTypeEnable() throws Exception {
		ProductType pojo = productTypeService.findById(paramLong("id"));

		Integer enableFlag = pojo.getIsEnable();
		if (ProductTypeConstant.ENABLE_TRUE == enableFlag) {
			pojo.setIsEnable(ProductTypeConstant.ENABLE_FALSE);
		} else {
			pojo.setIsEnable(ProductTypeConstant.ENABLE_TRUE);
		}
		productTypeService.updateProductType(pojo);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改标签排序
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeRecommend")
	public void productTypeRecommend() throws Exception {
		ProductType pojo = productTypeService.findById(paramLong("id"));

		pojo.setRecommendTime(new Date());
		productTypeService.updateProductType(pojo);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 删除类别
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/productSort/productTypeDelete")
	public void productTypeDelete() throws Exception {
		productTypeService.deleteProductType(paramLong("id"));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

}
