package com.rongdu.manage.action.nb.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.constant.ProductMaterialsTypeConstant;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

/**
 * 产品及组合产品管理
 */
public class ManageProductAction extends BaseAction<ProductBasicModel>
		implements ModelDriven<ProductBasicModel> {

	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductMaterialsService productMaterialsService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private SystemConfigService systemConfigService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageProductAction.class);

	/**
	 * 素材与产品展示规则 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/productMaterialManage")
	public String productMaterialManage() throws Exception {
		return "productMaterialManage";
	}

	/**
	 * 所有产品数据
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/allProductBasicData")
	public void allProductBasicData() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		PageDataList<ProductBasicModel> dataAll = productBasicService
				.getAllListForPaging(model);

		data = new HashMap<String, Object>();
		data.put("total", dataAll.getPage().getTotal());
		data.put("rows", dataAll.getList());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 调整排序序号 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/productShowOrderPage")
	public String productShowOrderPage() throws Exception {
		Long id = paramLong("id");
		request.setAttribute("showOrder", productBasicService.findById(id)
				.getShowOrder());
		request.setAttribute("id", id);
		return "productShowOrderPage";
	}

	/**
	 * 调整排序序号 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/productShowOrder")
	public void productShowOrder() throws Exception {
		ProductBasic pojo = productBasicService.findById(model.getId());

		pojo.setShowOrder(model.getShowOrder());
		pojo.setRecommendTime(new Date());
		productBasicService.updateProductBasic(pojo);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 推荐为微信首页 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/recommendProductPage")
	public String recommendProductPage() throws Exception {
		Long id = paramLong("id");
		request.setAttribute("productName", productBasicService.findById(id)
				.getProductName());
		request.setAttribute("id", id);
		return "recommendProductPage";
	}

	/**
	 * 推荐为微信首页 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/recommendProduct")
	public void recommendProduct() throws Exception {
		Long id = paramLong("id");

		// 推荐产品ID
		SystemConfig configRecommendId = systemConfigService
				.findByNid("recommendId");
		configRecommendId.setValue(String.valueOf(id));
		systemConfigService.update(configRecommendId);

		// 推荐产品特色 - 1
		SystemConfig configRecommendProp1 = systemConfigService
				.findByNid("recommendProp1");
		configRecommendProp1
				.setValue(addProductProp(paramString("recommendProp1")));
		systemConfigService.update(configRecommendProp1);

		// 推荐产品特色 - 2
		SystemConfig configRecommendProp2 = systemConfigService
				.findByNid("recommendProp2");
		configRecommendProp2
				.setValue(addProductProp(paramString("recommendProp2")));
		systemConfigService.update(configRecommendProp2);

		// 推荐产品特色 - 3
		SystemConfig configRecommendProp3 = systemConfigService
				.findByNid("recommendProp3");
		configRecommendProp3
				.setValue(addProductProp(paramString("recommendProp3")));
		systemConfigService.update(configRecommendProp3);

		// 重置现有首页推荐
		productBasicService.changeRecommend(id, paramString("recommendReason"));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 素材（Borrow非现金） - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrowMaterialPage")
	public String borrowMaterialPage() throws Exception {
		Long id = paramLong("id");
		request.setAttribute("id", id);

		// 封装已上传的素材
		encapsulateMaterialsToRequest(productMaterialsService
				.getMaterialsByProductId(id));

		return "borrowMaterialPage";
	}

	/**
	 * 素材（Ppfund现金） - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfundMaterialPage")
	public String ppfundMaterialPage() throws Exception {
		Long id = paramLong("id");
		request.setAttribute("id", id);

		// 封装已上传的素材
		encapsulateMaterialsToRequest(productMaterialsService
				.getMaterialsByProductId(id));

		return "ppfundMaterialPage";
	}

	/**
	 * 素材 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/uploadMaterial")
	public void uploadMaterial() throws Exception {
		try {
			// 获取已有素材
			ProductBasic pojo = productBasicService.findById(paramLong("id"));

			List<ProductMaterials> materialsList = productMaterialsService
					.getMaterialsByProductId(pojo.getId());
			if (null != materialsList && materialsList.size() > 0) {
				productMaterialsService.deleteMaterials(materialsList);
			}
			materialsList = encapsulateMaterialsFromRequest(pojo);

			productMaterialsService.addMaterials(materialsList);

			printResult("素材添加成功", true);
		} catch (Exception e) {
			throw new BorrowException(e.getMessage(), 1);
		}
	}

	/**
	 * 推荐为热门并更改标签排序
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/hotProduct")
	public void hotProduct() throws Exception {
		ProductBasic pojo = productBasicService.findById(paramLong("id"));

		Integer enableFlag = pojo.getHotProduct();
		if (ConstantUtil.FLAG_TRUE.equals(enableFlag)) {
			pojo.setHotProduct(ConstantUtil.FLAG_FALSE);
		} else {
			pojo.setHotProduct(ConstantUtil.FLAG_TRUE);
			pojo.setRecommendTime(new Date());
		}
		productBasicService.updateProductBasic(pojo);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改产品显示状态
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/updateProductDisplaying")
	public void updateProductDisplaying() throws Exception {
		productBasicService.updateDisplayLogic(paramLong("id"),
				paramString("platform"));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 将素材封装成对象
	 * 
	 * @param productBasic
	 * @return List<ProductMaterials>
	 */
	private List<ProductMaterials> encapsulateMaterialsFromRequest(
			ProductBasic productBasic) {
		List<ProductMaterials> list = new ArrayList<ProductMaterials>();
		ProductMaterials pojo = null;

		// 微信产品头图
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_HEAD_PIC);
		pojo.setMaterial(paramString("wechatHeadPic"));
		list.add(pojo);

		// 微信产品亮点
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_LIGHTSPOT);
		pojo.setMaterial(paramString("wechatLightspot"));
		list.add(pojo);

		// 微信小贴士
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_TIPS);
		pojo.setMaterial(paramString("wechatTips"));
		list.add(pojo);

		// 微信产品概要
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_PRODUCT_DIGEST);
		pojo.setMaterial(paramString("wechatProductDigest"));
		list.add(pojo);

		// 微信产品详情
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_PRODUCT_DETAIL);
		pojo.setMaterial(paramString("wechatProductDetail"));
		list.add(pojo);

		// 微信收益时间概要
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_REFUND_DIGEST);
		pojo.setMaterial(paramString("wechatRefundDigest"));
		list.add(pojo);

		// 微信收益时间详情
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_REFUND_DETAIL);
		pojo.setMaterial(paramString("wechatRefundDetail"));
		list.add(pojo);

		// 微信安全保障概要
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_SAFEGUARD_DIGEST);
		pojo.setMaterial(paramString("wechatSafeguardDigest"));
		list.add(pojo);

		// 微信安全保障详情
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_SAFEGUARD_DETAIL);
		pojo.setMaterial(paramString("wechatSafeguardDetail"));
		list.add(pojo);

		// 微信微信分享标题
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_SHARE_TITLE);
		String titleString = paramString("wechatShareTitle");
		if (StringUtil.isBlank(titleString)) {
			if ("随投随享".equals(productBasic.getProductTypeFlag().getFlagName())) {
				titleString = "秒杀某宝的利器来了";
			} else if ("800精选".equals(productBasic.getProductTypeFlag()
					.getFlagName())) {
				titleString = "精挑细选只为给你最适合的产品";
			} else if ("五星专享".equals(productBasic.getProductTypeFlag()
					.getFlagName())) {
				titleString = "不是自己人我不推荐哦";
			} else if ("银行信托".equals(productBasic.getProductTypeFlag()
					.getFlagName())) {
				titleString = "不用排队也能买到的好产品";
			} else {
				titleString = "800bank投资产品年化收益7-11%，快来投";
			}
		}
		pojo.setMaterial(titleString);
		list.add(pojo);

		// 微信微信分享描述
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.WECHAT_SHARE_DESC);
		String descString = paramString("wechatShareTitle");
		if (StringUtil.isBlank(descString)) {
			if ("随投随享".equals(productBasic.getProductTypeFlag().getFlagName())) {
				descString = "年化6%神器，当天起息，赎回灵活。乃懒人理财的必备良品。";
			} else if ("800精选".equals(productBasic.getProductTypeFlag()
					.getFlagName())) {
				descString = "迅速找到年化收益8%－13%的产品，总有一款适合你！";
			} else if ("五星专享".equals(productBasic.getProductTypeFlag()
					.getFlagName())) {
				descString = "偷偷给你加息0.5%，因为我们都是五星人！";
			} else if ("银行信托".equals(productBasic.getProductTypeFlag()
					.getFlagName())) {
				descString = "还在排队抢银行信托的产品？何不动动手指，我们早已为你留好额度。";
			} else {
				descString = "优质投资产品，灵活投资方式，尽在800bank。";
			}
		}
		pojo.setMaterial(descString);
		list.add(pojo);

		// PC产品头图
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.PC_HEAD_PIC);
		pojo.setMaterial(paramString("pcHeadPic"));
		list.add(pojo);

		// PC产品详情
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.PC_PRODUCT_DETAIL);
		pojo.setMaterial(paramString("pcProductDetail"));
		list.add(pojo);

		// PC产品资料
		pojo = new ProductMaterials();
		pojo.setProduct(productBasic);
		pojo.setMaterialType(ProductMaterialsTypeConstant.PC_PRODUCT_MATERIALS);
		pojo.setMaterial(paramString("pcProductMaterials"));
		list.add(pojo);

		return list;
	}

	/**
	 * 封装已上传的素材
	 * 
	 * @param materialsList
	 */
	private void encapsulateMaterialsToRequest(
			List<ProductMaterials> materialsList) {
		for (ProductMaterials material : materialsList) {
			if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_HEAD_PIC)) {
				// 微信产品头图
				request.setAttribute("wechatHeadPic", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_LIGHTSPOT)) {
				// 微信产品亮点
				request.setAttribute("wechatLightspot", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_TIPS)) {
				// 微信小贴士
				request.setAttribute("wechatTips", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_PRODUCT_DIGEST)) {
				// 微信产品概要
				request.setAttribute("wechatProductDigest",
						material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_PRODUCT_DETAIL)) {
				// 微信产品详情
				request.setAttribute("wechatProductDetail",
						material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_REFUND_DIGEST)) {
				// 微信收益时间概要
				request.setAttribute("wechatRefundDigest",
						material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_REFUND_DETAIL)) {
				// 微信收益时间详情
				request.setAttribute("wechatRefundDetail",
						material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_SAFEGUARD_DIGEST)) {
				// 微信安全保障概要
				request.setAttribute("wechatSafeguardDigest",
						material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_SAFEGUARD_DETAIL)) {
				// 微信安全保障详情
				request.setAttribute("wechatSafeguardDetail",
						material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_SHARE_TITLE)) {
				// 微信微信分享标题
				request.setAttribute("wechatShareTitle", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.WECHAT_SHARE_DESC)) {
				// 微信微信分享描述
				request.setAttribute("wechatShareDesc", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.PC_HEAD_PIC)) {
				// PC产品头图
				request.setAttribute("pcHeadPic", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.PC_PRODUCT_DETAIL)) {
				// PC产品详情
				request.setAttribute("pcProductDetail", material.getMaterial());
			} else if (material.getMaterialType().equals(
					ProductMaterialsTypeConstant.PC_PRODUCT_MATERIALS)) {
				// PC产品资料
				request.setAttribute("pcProductMaterials",
						material.getMaterial());
			}
		}
	}

	private String addProductProp(String str) {
		if ("灵活短期限".equals(str)) {
			str = "活" + str;
		} else if ("稳健高收益".equals(str)) {
			str = "稳" + str;
		} else if ("浮动保本金".equals(str)) {
			str = "高" + str;
		} else if ("本息保护".equals(str) || "多重安全保障".equals(str)) {
			str = "保" + str;
		} else if ("优质债权标".equals(str) || "优选信托系".equals(str)) {
			str = "优" + str;
		} else {
			str = "";
		}
		return str;
	}

	// TODO 以下为组合接口（待删）

	/**
	 * 组合发标 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/showProductSetManager")
	public String showProductSetManager() throws Exception {
		List<ProductType> typeList = productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__SET, -1);
		request.setAttribute("typeList", typeList);
		return "showProductSetManager";
	}

	/**
	 * 组合发标 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/showProductSetList")
	public void showProductSetList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProductBasicModel> list = productBasicService
				.getModelSetPageListByStatus(
						ProductStatusConstant.STATUS_UNRELATED, pageNumber,
						pageSize);
		model.setSize(pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 组合发标 - 子页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/releaseProductSetManager")
	public String releaseProductSetManager() throws Exception {
		// 产品标签
		request.setAttribute("productTypeFlagList",
				productTypeFlagService.findAllEnabledProductTypeFlag());

		// 产品类型
		request.setAttribute("productTypeList", productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__SET,
						ProductTypeConstant.ENABLE_TRUE));

		// 可选产品列表
		request.setAttribute("prodList",
				productBasicService.getModelListForProductSet());

		return "releaseProductSetManager";
	}

	/**
	 * 组合发标 - 发标确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/confirmProductSet")
	public void confirmProductSet() throws Exception {
		// 保存产品信息
		model.setStatus(ProductStatusConstant.STATUS_WAITING_FOR_APPROVE);
		ProductBasic prod = productBasicService
				.saveProductBasic(ProductBasicModel.transSetModel(model));
		model.setId(prod.getId());

		// 保存素材
		// productMaterialsService.addMaterials(model);

		// 保存组合配置
		// productSetService.saveProductSet(ProductSetModel.transSetList(model));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 组合发标 - 修改标页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/modifyProductSetManager")
	public String modifyProductSetManager() throws Exception {
		// 产品标签
		request.setAttribute("productTypeFlagList",
				productTypeFlagService.findAllEnabledProductTypeFlag());

		// 产品类型
		request.setAttribute("productTypeList", productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__SET,
						ProductTypeConstant.ENABLE_TRUE));

		// 可选产品列表
		request.setAttribute("prodList",
				productBasicService.getModelListForProductSet());

		// 待修改的组合产品
		request.setAttribute("mod",
				productBasicService.getModelSetById(paramLong("id")));

		return "modifyProductSetManager";
	}

	/**
	 * 组合发标 - 修改标确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/modifyProductSet")
	public void modifyProductSet() throws Exception {
		// 更新产品信息
		ProductBasic prod = productBasicService.findById(model.getId());
		ProductBasicModel.transSetModel(prod, model);
		productBasicService.updateProductBasic(prod);

		// 保存素材
		// productMaterialsService.addMaterials(model);

		// 删除组合配置
		// List<ProductSet> list =
		// productSetService.getProdSetList(model.getId());
		// productSetService.deleteProductSet(list);
		// 保存组合配置
		// productSetService.saveProductSet(ProductSetModel.transSetList(model));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 组合发标 - 查询组合标页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/viewProductSet")
	public String viewProductSet() throws Exception {
		Long id = paramLong("id");
		ProductBasicModel mod = productBasicService.getModelSetById(id);
		request.setAttribute("mod", mod);
		return "viewProductSet";
	}

	/**
	 * 组合发标 - 审核页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/verifyProductSetManager")
	public String verifyProductSetManager() throws Exception {
		Long id = paramLong("id");
		ProductBasicModel mod = productBasicService.getModelSetById(id);
		request.setAttribute("mod", mod);
		return "verifyProductSetManager";
	}

	/**
	 * 组合发标 - 审核确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/verifyProductSet")
	public void verifyProductSet() throws Exception {
		ProductBasic prod = productBasicService.findById(model.getId());
		prod.setStatus(model.getStatus());
		prod.setVerifyRemark(model.getVerifyRemark());
		productBasicService.updateProductBasic(prod);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

}
