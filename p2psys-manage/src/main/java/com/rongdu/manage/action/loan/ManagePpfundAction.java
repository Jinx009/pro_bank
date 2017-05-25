package com.rongdu.manage.action.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.service.RedPacketService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.voucher.service.InterestRateService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.exception.PpfundException;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.model.PpfundOutModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundOutService;

/**
 * 现金产品
 */
public class ManagePpfundAction extends BaseAction<PpfundModel> implements
		ModelDriven<PpfundModel> {

	@Resource
	private InterestRateService interestRateService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private PpfundService thePpfundService;
	@Resource
	private RedPacketService redPacketService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private PpfundOutService ppfundOutService;
	@Resource
	private ProductBasicService productBasicService;

	private Map<String, Object> map;

	Logger logger = Logger.getLogger(ManagePpfundAction.class);

	private PpfundModel model = new PpfundModel();

	public PpfundModel getModel() {
		return model;
	}

	/**
	 * 查看产品信息
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundShowDetailPage")
	public String ppfundShowDetailPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		return "ppfundShowDetailPage";
	}

	/**
	 * 现金产品管理 - 现金产品发标 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundManagePage")
	public String ppfundManagePage() throws Exception {
		return "ppfundManagePage";
	}

	/**
	 * 现金产品管理 - 现金产品发标 - 列表
	 * 
	 * 现金产品管理 - 现金产品初审 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundManagerList")
	public void ppfundManagerList() throws Exception {
		model.setStatus(PpfundModel.STATUS_WAITING_FOR_APPROVE);
		PageDataList<PpfundModel> list = thePpfundService.manageList(model);

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		map.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 现金产品管理 - 现金产品发标 - 发布产品 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundAddPage")
	public String ppfundAddPage() throws Exception {
		// 所有可用红包信息
		request.setAttribute("redPackets", redPacketService.findActivities());

		// 所有可用加息券信息
		request.setAttribute("interestRates",
				interestRateService.findAllValid());

		// 所有可用产品标签
		request.setAttribute("productTypeFlagList",
				productTypeFlagService.findAllEnabledProductTypeFlag());

		// 所有可用产品类型
		List<ProductType> productTypeList = productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE,
						ProductTypeConstant.ENABLE_TRUE);
		productTypeList.addAll(productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__PPFUND,
						ProductTypeConstant.ENABLE_TRUE));
		request.setAttribute("productTypeList", productTypeList);

		return "ppfundAddPage";
	}

	/**
	 * 现金产品管理 - 现金产品发标 - 发布产品 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/addPpfund")
	public void addPpfund() throws Exception {
		try {
			// 红包
			model.setRedPacket(redPacketService.find(paramInt("redPacketId")));

			// 加息券
			model.setInterestRate(interestRateService
					.findById(paramLong("interestRateId")));

			// 保存
			thePpfundService.addPpfund(model);

			printResult("添加成功", true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new PpfundException("添加现金产品出错", PpfundException.TYPE_JSON);
		}
	}

	/**
	 * 现金产品管理 - 现金产品发标 - 修改 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundUpdatePage")
	public String ppfundUpdatePage() throws Exception {
		Ppfund ppfund = thePpfundService.getById(model.getId());
		// 检查状态
		if (ppfund.getStatus() != PpfundModel.STATUS_WAITING_FOR_APPROVE) {
			throw new PpfundException("该产品已审核，不允许修改，请刷新后查看列表",
					PpfundException.TYPE_JSON);
		}

		// 所有可用红包信息
		request.setAttribute("redPackets", redPacketService.findActivities());

		// 所有可用加息券信息
		request.setAttribute("interestRates",
				interestRateService.findAllValid());

		setProductInfoIntoRequest(ppfund.getId());

		saveToken("ppfundUpdateToken");
		return "ppfundUpdatePage";
	}

	/**
	 * 现金产品管理 - 现金产品发标 - 修改 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundUpdate")
	public void ppfundUpdate() throws Exception {
		try {
			checkToken("ppfundUpdateToken");

			// 红包
			model.setRedPacket(redPacketService.find(paramInt("redPacketId")));

			// 加息券
			model.setInterestRate(interestRateService
					.findById(paramLong("interestRateId")));

			// 保存信息
			thePpfundService.updatePpfund(model);

			printResult("修改成功", true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BorrowException("修改非现金产品出错", BorrowException.TYPE_JSON);
		}
	}

	/**
	 * 现金产品管理 - 现金产品初审 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundVerifyManagerPage")
	public String ppfundVerifyManagerPage() throws Exception {
		return "ppfundVerifyManagerPage";
	}

	/**
	 * 现金产品管理 - 现金产品初审 - 审核 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundVerifyPage")
	public String ppfundVerifyPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		saveToken("ppfundVerifyToken");
		return "ppfundVerifyPage";
	}

	/**
	 * 现金产品管理 - 现金产品初审 - 审核 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundVerify")
	public void ppfundVerify() throws Exception {
		checkToken("ppfundVerifyToken");

		thePpfundService.verifyPpfund(model, getOperator());
		printResult("添加成功", true);
	}

	/**
	 * 现金产品管理 - 募资管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundBiddingManager")
	public String ppfundBiddingManager() throws Exception {
		return "ppfundBiddingManager";
	}

	/**
	 * 现金产品管理 - 募资管理 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundBiddingList")
	public void ppfundBiddingList() throws Exception {
		model.setStatus(PpfundModel.STATUS_APPROVED);
		PageDataList<PpfundModel> list = thePpfundService.manageList(model);

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		map.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 现金产品管理 - 所有现金产品管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundTotallyManagerPage")
	public String ppfundTotallyManagerPage() throws Exception {
		return "ppfundTotallyManagerPage";
	}

	/**
	 * 现金产品管理 - 所有现金产品管理 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundTotallyManagerList")
	public void ppfundTotallyManagerList() throws Exception {
		model.setStatus(PpfundModel.STATUS_UNRELATED);
		PageDataList<PpfundModel> list = thePpfundService.manageList(model);

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		map.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 购买记录 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundInDetailPage")
	public String ppfundInDetailPage() throws Exception {
		request.setAttribute("id", model.getId());
		return "ppfundInDetailPage";
	}

	/**
	 * 购买记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundInList")
	public void ppfundInList() throws Exception {
		PageDataList<PpfundInModel> list = ppfundInService.pageList(
				model.getId(), model.getPage(), model.getRows());

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		map.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 转出记录 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundOutDetailPage")
	public String ppfundOutDetailPage() throws Exception {
		request.setAttribute("id", model.getId());
		return "ppfundOutDetailPage";
	}

	/**
	 * 转出记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundOutList")
	public void ppfundOutList() throws Exception {
		PageDataList<PpfundOutModel> list = ppfundOutService.pageDataList(
				model.getId(), model.getPage(), model.getRows());

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		map.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 截标
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundStop")
	public void ppfundStop() throws Exception {
		Ppfund ppfund = thePpfundService.getById(model.getId());
		// 新结构
		ProductBasic prod = productBasicService.getProductBasicInfo(ppfund
				.getProductType().getId(), ppfund.getId());

		if (ppfund.getAccountYes() == 0) {
			// 如果没有人投资则设置为审核失败
			ppfund.setStatus(PpfundModel.STATUS_FAIL);

			// 新结构
			prod.setStatus(ProductStatusConstant.STATUS_FAIL);
		} else {
			// 如果关闭则停止招标，且产品转出时间不能够做修改
			ppfund.setAccount(ppfund.getAccountYes());
			ppfund.setScales(100);
			ppfund.setStatus(PpfundModel.STATUS_CLOSE);
			// 处理用户转出，如果该产品关闭，转出时间未设置，则根据转出周期自动给其设定一个转出时间
			ppfundInService.doPpfundInOutTime(ppfund);

			// 新结构
			prod.setStatus(ProductStatusConstant.STATUS_RECHECK_PASS);
		}
		thePpfundService.update(ppfund);

		// 新结构
		productBasicService.updateProductBasic(prod);

		printResult("截标成功", true);
	}

	/**
	 * 贷后管理 - 现金管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundInManager")
	public String ppfundInManager() throws Exception {
		return "ppfundInManager";
	}

	/**
	 * 贷后管理 - 现金管理 - 记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/ppfundInAllList")
	public void ppfundInAllList() throws Exception {
		PpfundInModel model = (PpfundInModel) paramModel(PpfundInModel.class);
		model.setRows(paramInt("rows"));
		model.setPage(paramInt("page"));
		PageDataList<PpfundInModel> list = ppfundInService.pageList(model);

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		map.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 贷后管理 - 现金管理 - 导出
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/ppfund/exportExcelPpfundInAllList")
	public void exportExcelPpfundInAllList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		PpfundInModel model = (PpfundInModel) paramModel(PpfundInModel.class);
		model.setPage(1);
		model.setRows(10000);
		PageDataList<PpfundInModel> pageDataList = ppfundInService
				.pageList(model);
		String title = "现金产品购买记录列表";
		// 表头数组
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "产品编码", "产品名称",
				"购买金额", "有效金额", "购买时间", "预约转出时间", "是否转出" };
		// 对象属性数组
		String[] fields = new String[] { "id", "userName", "realName", "pidNo",
				"ppfundName", "money", "account", "addTime", "outTime",
				"outStr" };
		TableData td = ExcelUtils.createTableData(pageDataList.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	private void setProductInfoIntoRequest(Long ppfundId) {
		Ppfund ppfund = thePpfundService.getById(ppfundId);
		request.setAttribute("ppfund", PpfundModel.instance(ppfund));

		// 产品相关信息
		request.setAttribute("productBasic", productBasicService
				.getProductBasicInfo(ppfund.getProductType().getId(),
						ppfund.getId()));
	}
}
