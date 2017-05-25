package com.rongdu.manage.action.nb.coupons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.constant.ProductTypeFlagConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.user.service.CouponCategoryService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.model.CouponCategoryModel;

/**
 * 用户加息券类型配置
 */
public class ManageUserCouponsCategoryAction extends
		BaseAction<CouponCategoryModel> implements
		ModelDriven<CouponCategoryModel> {

	@Resource
	private CouponCategoryService couponCategoryService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageUserCouponsCategoryAction.class);

	/**
	 * 用户加息券类型配置 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsCategoryManage")
	public String userCouponsCategoryManage() throws Exception {
		return "userCouponsCategoryManage";
	}

	/**
	 * 所有用户加息券类型配置数据
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsCategoryList")
	public void userCouponsCategoryList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		PageDataList<CouponCategoryModel> pageList = couponCategoryService
				.findAllPageList(model);

		data = new HashMap<String, Object>();
		data.put("total", pageList.getPage().getTotal());
		data.put("rows", pageList.getList());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户加息券类型配置 - 添加页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsCategoryAdd")
	public String productCouponsAdd() throws Exception {
		List<ProductTypeFlag> typeFlagList = productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_AND_WECHAT);
		typeFlagList.addAll(productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_ONLY));
		typeFlagList.addAll(productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_WECHAT_ONLY));
		request.setAttribute("productList", typeFlagList);

		return "userCouponsCategoryAdd";
	}

	/**
	 * 用户加息券类型配置 - 添加确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsCategoryAddConfirm")
	public void userCouponsCategoryAddConfirm() throws Exception {
		try {
			couponCategoryService.addUserCouponCategory(model);

			printResult("新增加息券类型成功", true);
		} catch (Exception e) {
			throw new BussinessException("新增加息券类型失败\n" + e.getMessage(), 1);
		}
	}

	/**
	 * 用户加息券类型配置 - 更新页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsCategoryModify")
	public String userCouponsCategoryModify() throws Exception {
		request.setAttribute("couponsCategory",
				couponCategoryService.findById(paramLong("id")));

		List<ProductTypeFlag> typeFlagList = productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_AND_WECHAT);
		typeFlagList.addAll(productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_PC_ONLY));
		typeFlagList.addAll(productTypeFlagService
				.getPojoList(ProductTypeFlagConstant.SHOW_WECHAT_ONLY));
		request.setAttribute("productList", typeFlagList);

		return "userCouponsCategoryModify";
	}

	/**
	 * 用户加息券类型配置 - 更新确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsCategoryModifyConfirm")
	public void userCouponsCategoryModifyConfirm() throws Exception {
		try {
			couponCategoryService.updateUserCouponCategory(model);

			printResult("更新加息券类型成功", true);
		} catch (Exception e) {
			throw new BorrowException("更新加息券类型失败\n" + e.getMessage(), 1);
		}
	}

}
