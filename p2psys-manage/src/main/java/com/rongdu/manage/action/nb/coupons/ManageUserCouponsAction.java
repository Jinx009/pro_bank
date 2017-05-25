package com.rongdu.manage.action.nb.coupons;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.CouponCategoryService;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.model.CouponModel;

/**
 * 产品加息券配置
 */
public class ManageUserCouponsAction extends BaseAction<CouponModel> implements
		ModelDriven<CouponModel> {

	@Resource
	private CouponService couponService;
	@Resource
	private CouponCategoryService couponCategoryService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageUserCouponsAction.class);

	/**
	 * 产品加息券配置 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsManage")
	public String userCouponsManage() throws Exception {
		return "userCouponsManage";
	}

	/**
	 * 所有产品加息券配置数据
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsList")
	public void userCouponsList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		PageDataList<CouponModel> pageList = couponService
				.findAllPageList(model);

		data = new HashMap<String, Object>();
		data.put("total", pageList.getPage().getTotal());
		data.put("rows", pageList.getList());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 上传TXT页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsAdd")
	public String userCouponsAdd() throws Exception {
		request.setAttribute("categoryList",
				couponCategoryService.findAllNonOverdue());

		return "userCouponsAdd";
	}

	/**
	 * 确定上传TXT
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsAddConfirm")
	public void userCouponsAddConfirm() throws Exception {
		Scanner in = null;
		try {
			File file = txtUpload();
			in = new Scanner(file);
			while (in.hasNextLine()) {
				String str = in.nextLine();
				couponService.generateCoupon(CouponModel.OPERATION_UPLOAD,
						paramLong("categoryId"), str);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				in.close();
			}
		}
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改加息券加息比例
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsModify")
	public String userCouponsModify() throws Exception {
		request.setAttribute("couponInfo",
				couponService.findCouponInfoById(paramLong("id")));

		return "userCouponsModify";
	}

	/**
	 * 确定修改加息比例
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/userCouponsModifyConfirm")
	public void userCouponsModifyConfirm() throws Exception {
		Coupon pojo = couponService.findCouponInfoById(model.getId());
		pojo.setToRateAdjust(model.getToRateAdjust());
		pojo.setUpdateTime(new Date());
		couponService.updateCouponInfo(pojo);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(data));
	}

}
