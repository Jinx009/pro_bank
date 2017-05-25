package com.rongdu.p2psys.pc.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.constant.ProductTypeFlagConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserService;

public class ProductAction extends BaseAction<ProductBasicModel> implements
		ModelDriven<ProductBasicModel> {

	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductMaterialsService productMaterialsService;
	@Resource
	private PpfundService ppfundService;
	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private AccountService theAccountService;

	private Map<String, Object> data;

	/**
	 * 显示所有分类标签（PC）
	 * 
	 * @throws Exception
	 */
	@Action("/product/showProductTypeFlagListForPc")
	public void showProductTypeFlagListForPc() throws Exception {
		List<ProductTypeFlag> dataList = productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_AND_WECHAT);
		dataList.addAll(productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_ONLY));

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 显示所有分类标签（微信）
	 * 
	 * @throws Exception
	 */
	@Action("/product/showProductTypeFlagList")
	public void showProductTypeFlagList() throws Exception {
		List<ProductTypeFlag> dataList = productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_AND_WECHAT);
		dataList.addAll(productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_WECHAT_ONLY));

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 显示产品详情
	 * 
	 * @throws Exception
	 */
	@Action("/product/showProductDetail")
	public void showProductDetail() throws Exception {
		Long id = paramLong("prodId");
		ProductBasicModel model = productBasicService.getModelById(id);

		// 素材
		List<ProductMaterials> list = productMaterialsService
				.getMaterialsByProductId(id);

		data = new HashMap<String, Object>();
		data.put("data", model);
		data.put("matList", list);
		data.put("systemDate", new Date().getTime());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 产品列表（PC） - 页面
	 * 
	 * @since 1.8
	 * @throws Exception
	 */
	@Action("/nb/pc/product/productList")
	public String productList() throws Exception {
		request.setAttribute("id", paramLong("id"));
		return "productList";
	}

	/**
	 * 产品列表（PC） - 页面
	 * 
	 * @since 2.0
	 * @throws Exception
	 */
	@Action(value = "/nb/pc/product/product_list")
	public String product_list() throws Exception {
		request.setAttribute("id", paramLong("id"));
		return "product_list";
	}

	/**
	 * 安全保障(PC)-页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action(value = "/nb/pc/safetyControl")
	public String safetyControl() throws Exception {
		return "safetyControl";
	}

	/**
	 * 现金类产品详情（PC） - 页面
	 * 
	 * @since 2.0
	 * @return String
	 * @throws Exception
	 */
	@Action(value = "/nb/pc/product/ppfund_detail")
	public String ppfundDetail() throws Exception {
		request.setAttribute("prodId", paramLong("productId"));
		request.setAttribute("flagId", paramLong("flagId"));
		request.setAttribute("ppfundId", paramLong("ppfundId"));
		Ppfund ppfund = ppfundService.getPpfundById(paramLong("ppfundId"));
		// 判断该标为体验标
		if (ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE.equals(ppfund
				.getProductType().getTypeCategory())) {
			User user = getNBSessionUser();
			// 当前用户体验金
			boolean em = theExperienceGoldService.getCanExperienceGold(user);

			request.setAttribute("isEGold", em);
			List<AccountBank> ablist = theAccountBankService.list(user
					.getUserId());
			if (ablist == null || ablist.size() <= 0) {
				// 该用户未绑卡，是否线下充值认证？
				UserIdentify userIdentify = theUserIdentifyService
						.getUserIdentifyByUserId(user.getUserId());
				Account account = theAccountService.getAccountByUserId(user
						.getUserId());
				if (userIdentify.getRealNameStatus() == 1
						|| account.getTotal() > 0) {
					request.setAttribute("isBindC", 1);
				} else {
					request.setAttribute("isBindC", 0);
				}
			} else {
				request.setAttribute("isBindC", 1);
			}
		}
		return "ppfund_detail";
	}

	/**
	 * 非现金类产品详情（PC） - 页面
	 * 
	 * @since 2.0
	 * @throws Exception
	 */
	@Action(value = "/nb/pc/product/borrow_detail")
	public String borrowDetail() throws Exception {
		request.setAttribute("prodId", paramLong("productId"));
		request.setAttribute("flagId", paramLong("flagId"));
		request.setAttribute("investId", paramLong("investId"));
		return "borrow_detail";
	}

	/**
	 * 显示热门产品列表（PC）
	 * 
	 * @throws Exception
	 */
	@Action("/nb/pc/product/showPopularProductListForPc")
	public void showPopularProductListForPc() throws Exception {
		User user = getNBSessionUser();
		Long userId = user != null ? user.getUserId() : 0L;

		List<ProductBasicModel> dataList = productBasicService
				.getRecommendModelList(userId, Constant.COOPERATE_TYPE__PC);

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		data.put("systemDate", new Date().getTime());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 根据分类标签显示产品列表（PC）
	 * 
	 * @throws Exception
	 */
	@Action("/nb/pc/product/showProductListByFlagForPc")
	public void showProductListByFlagForPc() throws Exception {
		List<ProductBasicModel> dataList = productBasicService
				.getProductBasicModelListByFlag(getNBSessionUser(),
						paramLong("id"), paramString("orderField"));

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		data.put("listUrl", productTypeFlagService.findById(paramLong("id"))
				.getListUrl());
		data.put("systemDate", new Date().getTime());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 根据分类标签显示产品列表（微信）
	 * 
	 * @throws Exception
	 */
	@Action("/product/showProductListByFlag")
	public void showProductListByFlag() throws Exception {
		List<ProductBasicModel> dataList = productBasicService
				.getModelListByFlag(paramLong("id"),
						Constant.COOPERATE_TYPE__WECHAT);

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		data.put("systemDate", new Date().getTime());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 显示热门产品列表（微信）
	 * 
	 * @throws Exception
	 */
	@Action("/product/showPopularProductList")
	public void showPopularProductList() throws Exception {
		User user = getNBSessionUser();
		Long userId = user != null ? user.getUserId() : 0L;

		List<ProductBasicModel> dataList = productBasicService
				.getRecommendModelList(userId, Constant.COOPERATE_TYPE__WECHAT);

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		data.put("systemDate", new Date().getTime());
		printWebJson(getStringOfJpaObj(data));
	}

}
