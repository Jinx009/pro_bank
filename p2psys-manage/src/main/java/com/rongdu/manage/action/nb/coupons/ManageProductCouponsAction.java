package com.rongdu.manage.action.nb.coupons;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.voucher.model.InterestRateModel;
import com.rongdu.p2psys.nb.voucher.service.InterestRateService;

/**
 * 产品加息券配置
 */
public class ManageProductCouponsAction extends BaseAction<InterestRateModel>
		implements ModelDriven<InterestRateModel> {

	@Resource
	private InterestRateService interestRateService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageProductCouponsAction.class);

	/**
	 * 产品加息券配置 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsManage")
	public String productCouponsManage() throws Exception {
		return "productCouponsManage";
	}

	/**
	 * 所有产品加息券配置数据
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsList")
	public void productCouponsList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		PageDataList<InterestRateModel> pageList = interestRateService
				.findAllPageList(model);

		data = new HashMap<String, Object>();
		data.put("total", pageList.getPage().getTotal());
		data.put("rows", pageList.getList());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 产品加息券配置 - 添加页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsAdd")
	public String productCouponsAdd() throws Exception {
		return "productCouponsAdd";
	}

	/**
	 * 新增加息券信息
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsAddConfirm")
	public void productCouponsAddConfirm() throws Exception {
		try {
			interestRateService.addProductCoupon(model);

			printResult("新增加息券成功", true);
		} catch (Exception e) {
			throw new BussinessException("新增加息券失败\n" + e.getMessage(), 1);
		}
	}

	/**
	 * 产品加息券配置 - 更新页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsModify")
	public String productCouponsModify() throws Exception {
		request.setAttribute("interestRate",
				interestRateService.findById(paramLong("id")));

		return "productCouponsModify";
	}

	/**
	 * 更新加息券信息
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsModifyConfirm")
	public void productCouponsModifyConfirm() throws Exception {
		try {
			interestRateService.updateProductCoupon(model);

			printResult("更新加息券成功", true);
		} catch (Exception e) {
			throw new BussinessException("更新加息券失败\n" + e.getMessage(), 1);
		}
	}

	/**
	 * 更新启用状态
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/coupons/productCouponsUpdateStatus")
	public void productCouponsUpdateStatus() throws Exception {
		try {
			interestRateService.updateStatus(paramLong("id"));

			printResult("加息券状态更新成功", true);
		} catch (Exception e) {
			throw new BussinessException("加息券状态更新失败\n" + e.getMessage(), 1);
		}
	}

}
