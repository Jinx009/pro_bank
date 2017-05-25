package com.rongdu.manage.action.loan;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.model.ExchangeRatePacketCaptureModel;
import com.rongdu.p2psys.core.service.OperationLogService;
import com.rongdu.p2psys.core.service.RedPacketService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.borrow.service.BorrowService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeFlagService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;
import com.rongdu.p2psys.nb.protocol.service.ProtocolService;
import com.rongdu.p2psys.nb.user.service.CouponCategoryService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.voucher.service.InterestRateService;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
import com.rongdu.p2psys.nb.wechat.util.WechatMessageData;
import com.rongdu.p2psys.user.domain.User;

/**
 * 非现金产品管理
 */
public class ManageBorrowAction extends BaseAction<BorrowModel> implements
		ModelDriven<BorrowModel> {

	@Resource
	private BorrowService theBorrowService;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private InterestRateService interestRateService;
	@Resource
	private OperationLogService operationLogService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductTypeFlagService productTypeFlagService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private ProtocolService protocolService;
	@Resource
	private RedPacketService redPacketService;
	@Resource
	private UserService theUserService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private CouponCategoryService couponCategoryService;

	@Resource
	private com.rongdu.p2psys.borrow.service.BorrowService borrowService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageBorrowAction.class);

	/**
	 * 查看产品信息
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowShowDetailPage")
	public String borrowShowDetailPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		return "borrowShowDetailPage";
	}

	/**
	 * 记录 - 投标记录弹出页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowTenderDetailPage")
	public String borrowTenderDetailPage() throws Exception {
		request.setAttribute("id", model.getId());

		return "borrowTenderDetailPage";
	}

	/**
	 * 记录 - 投标记录列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowTenderDetailList")
	public void borrowTenderDetailList() throws Exception {
		PageDataList<BorrowTenderModel> pageDataList = borrowTenderService
				.list(model.getBorrowId(), model.getPage(),
						paramInt(ConstantUtil.ROWS));

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, pageDataList.getPage().getTotal());
		data.put(ConstantUtil.ROWS, pageDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 记录 - 导出Excel
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/exportExcelTenderDetailList")
	public void exportExcelTenderDetailList() throws Exception {
		Borrow borrow = theBorrowService.getById(model.getBorrowId());
		PageDataList<BorrowTenderModel> pageDataList = borrowTenderService
				.list(model.getBorrowId(), 1, 10000);
		String title = borrow.getName() + "投资列表";
		// 表头数组
		String[] hearders = new String[] { "投资人", "投资金额（元）", "有效投资金额（元）",
				"回收本息（元）", "投资渠道", "投资时间", "IP地址" };
		// 对象属性数组
		String[] fields = new String[] { "userName", "money", "account",
				"repaymentAccount", "tenderType", "addTime", "addIp" };
		TableData td = ExcelUtils.createTableData(pageDataList.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 非现金产品管理 - 非现金产品发标 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowManagerPage")
	public String borrowManagerPage() throws Exception {
		return "borrowManagerPage";
	}

	/**
	 * 非现金产品管理 - 非现金产品发标 - 列表
	 * 
	 * 非现金产品管理 - 非现金产品初审 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowManagerList")
	public void borrowManagerList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		model.setStatus(Borrow.STATUS_PUBLISHING);
		PageDataList<BorrowModel> list = theBorrowService.manageList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 非现金产品管理 - 非现金产品发标 - 发布产品 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowAddPage")
	public String borrowAddPage() throws Exception {
		// 所有可用红包信息
		request.setAttribute("redPackets", redPacketService.findActivities());

		// 所有可用加息券信息
		request.setAttribute("interestRates",
				interestRateService.findAllValid());
		
		request.setAttribute("categories",
				couponCategoryService.findAllValid());

		// 所有可用产品标签
		request.setAttribute("productTypeFlagList",
				productTypeFlagService.findAllEnabledProductTypeFlag());

		// 所有可用产品类型
		List<ProductType> productTypeList = productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__BORROW,
						ProductTypeConstant.ENABLE_TRUE);
		productTypeList.addAll(productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__FIVESTAR,
						ProductTypeConstant.ENABLE_TRUE));

		// VIP
		productTypeList.addAll(productTypeService
				.findProductTypeListByCategory(
						ProductTypeConstant.PRODUCT_CATEGORY__VIP,
						ProductTypeConstant.ENABLE_TRUE));
		// VIP

		request.setAttribute("productTypeList", productTypeList);

		return "borrowAddPage";
	}

	/**
	 * 非现金产品管理 - 非现金产品发标 - 发布产品 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/addBorrow")
	public void addBorrow() throws Exception {
		try {
			// 验证定向标密码
			model.validDXB();

			// 红包
			model.setRedPacket(redPacketService.find(paramInt("redPacketId")));

			// 加息券
			model.setInterestRate(interestRateService
					.findById(paramLong("interestRateId")));
			
			model.setCouponCategory(couponCategoryService.findById(paramLong("couponCategoryId")));

			// 保存标信息
			theBorrowService.addBorrow(model);

			printResult("添加成功", true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BorrowException("添加非现金产品出错", BorrowException.TYPE_JSON);
		}
	}

	/**
	 * 非现金产品管理 - 非现金产品发标 - 修改产品 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowUpdatePage")
	public String borrowUpdatePage() throws Exception {
		Borrow borrow = theBorrowService.getById(model.getId());
		// 检查状态
		if (borrow.getStatus() != Borrow.STATUS_PUBLISHING) {
			throw new BorrowException("该产品已审核，不允许修改，请刷新后查看列表",
					BorrowException.TYPE_JSON);
		}

		// 所有可用红包信息
		request.setAttribute("redPackets", redPacketService.findActivities());

		// 所有可用加息券信息
		request.setAttribute("interestRates",
				interestRateService.findAllValid());
		
		request.setAttribute("categories",
				couponCategoryService.findAllValid());
		// 所有可用协议信息
		request.setAttribute("protocolList",
				protocolService.findProtocolListByType(borrow.getType()));

		setProductInfoIntoRequest(borrow.getId());

		saveToken("borrowEditToken");
		return "borrowUpdatePage";
	}

	/**
	 * 非现金产品管理 - 非现金产品发标 - 修改产品 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/updateBorrow")
	public void updateBorrow() throws Exception {
		try {
			checkToken("borrowEditToken");

			// 红包
			model.setRedPacket(redPacketService.find(paramInt("redPacketId")));

			// 加息券
			model.setInterestRate(interestRateService
					.findById(paramLong("interestRateId")));
			
			model.setCouponCategory(couponCategoryService
					.findById(paramLong("couponCategoryId")));

			// 保存标信息
			theBorrowService.updateBorrow(model);

			printResult("修改成功", true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BorrowException("修改非现金产品出错", BorrowException.TYPE_JSON);
		}
	}

	/**
	 * 非现金产品管理 - 非现金产品初审 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowConfirmManagerPage")
	public String borrowConfirmManagerPage() throws Exception {
		return "borrowConfirmManagerPage";
	}

	/**
	 * 非现金产品管理 - 非现金产品初审 - 初审产品 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowConfirmPage")
	public String borrowConfirmPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		saveToken("borrowConfirmToken");
		return "borrowConfirmPage";
	}

	/**
	 * 非现金产品管理 - 非现金产品初审 - 初审产品 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowConfirm")
	public void borrowConfirm() throws Exception {
		checkToken("borrowConfirmToken");

		Date fixed = new Date();
		String fixedTime = paramString("fixedTime");
		if (StringUtil.isNotBlank(fixedTime)) {
			fixed = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.parse(fixedTime);
		}
		model.setFixedTime(fixed);
		theBorrowService.confirmBorrow(model, getOperator());

		printResult("操作成功", true);
	}

	/**
	 * 非现金产品管理 - 招标管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowBiddingManager")
	public String borrowBiddingManager() throws Exception {
		return "borrowBiddingManager";
	}

	/**
	 * 非现金产品管理 - 招标管理 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowBiddingList")
	public void borrowBiddingList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		model.setStatus(Borrow.STATUS_TRIAL_PASSED);
		PageDataList<BorrowModel> list = theBorrowService.biddingManageList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 非现金产品管理 - 非现金满标复审 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowVerifyFullManagerPage")
	public String borrowVerifyFullManagerPage() throws Exception {
		return "borrowVerifyFullManagerPage";
	}

	/**
	 * 非现金产品管理 - 非现金满标复审 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowVerifyFullManagerList")
	public void borrowVerifyFullManagerList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		// 初审通过并且投资进度为100%
		model.setStatus(Borrow.STATUS_TRIAL_PASSED);
		model.setScales(100);
		PageDataList<BorrowModel> list = theBorrowService.manageList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 非现金产品管理 - 非现金满标复审 - 满标复审 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowVerifyFullPage")
	public String borrowVerifyFullPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		saveToken("borrowVerifyFullToken");
		return "borrowVerifyFullPage";
	}

	/**
	 * 非现金产品管理 - 非现金满标复审 - 满标复审 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowVerifyFull")
	public void borrowVerifyFull() throws Exception {
		checkToken("borrowVerifyFullToken");

		theBorrowService.verifyFullBorrow(model, getOperator());

		printResult("满标复审成功！", true);
	}

	/**
	 * 非现金产品管理 - 非现金流标管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowShowSpreadManagerPage")
	public String borrowShowSpreadManagerPage() throws Exception {
		return "borrowShowSpreadManagerPage";
	}

	/**
	 * 非现金产品管理 - 非现金流标管理 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowShowSpreadList")
	public void borrowShowSpreadList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		model.setExpirationTime(new Date());
		model.setStatus(Borrow.STATUS_TRIAL_PASSED);
		PageDataList<BorrowModel> list = theBorrowService.manageList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 非现金产品管理 - 非现金流标管理 - 导出Excel
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/exportExcleSpreadBorrowList")
	public void exportExcleSpreadBorrowList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setSize(10000);
		model.setExpirationTime(new Date());
		model.setStatus(Borrow.STATUS_TRIAL_PASSED);
		PageDataList<BorrowModel> list = theBorrowService.manageList(model);
		String title = "非现金流标管理列表";
		// 表头数组
		String[] hearders = new String[] { "编号", "产品编码", "产品名称", "状态", "产品类型",
				"借款公司名称", "有效时间（天）", "最低投资金额（元）", "最高投资金额（元）", "投资总额（元）",
				"收益率（%）", "期限", "进度(%)", "发布时间" };
		// 对象属性数组
		String[] fields = new String[] { "id", "bidNo", "name", "statusStr",
				"typeStr", "companyName", "validTime", "lowestAccount",
				"mostAccount", "account", "apr", "timeLimitStr", "scales",
				"fixedTime" };
		TableData td = ExcelUtils.createTableData(list.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 非现金产品管理 - 撤标 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowCancelPage")
	public String borrowCancelPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		return "borrowCancelPage";
	}

	/**
	 * 非现金产品管理 - 撤标 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowCancel")
	public void borrowCancel() throws Exception {
		Borrow borrow = theBorrowService.getById(model.getId());
		theBorrowService.cancelBorrow(borrow);

		OperationLog log = new OperationLog(borrow.getUser(), getOperator(),
				"stop_borrow");
		log.setOperationResult("用户名为" + getOperator().getUserName() + "（"
				+ Global.getIP() + "）的操作员对用户为" + borrow.getUser().getUserName()
				+ "的借款标（ID:" + borrow.getId() + ",标名为" + borrow.getName()
				+ "）进行撤标操作");
		operationLogService.save(log);

		printResult("撤标成功！", true);
	}

	/**
	 * 非现金产品管理 - 推送 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowRecommend")
	public void borrowRecommend() throws Exception {
		Borrow borrow = theBorrowService.getById(model.getId());
		List<User> list = theUserService
				.getWechatUserList(WechatMessageData.A_APP_ID);

		SendMsgThead sendMsgThead = new SendMsgThead(borrow, list);
		sendMsgThead.start();

		printResult("推送成功！", true);
	}

	/**
	 * 非现金产品管理 - 推送 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowSingleRecommend")
	public void borrowSingleRecommend() throws Exception {
		Borrow borrow = theBorrowService.getById(model.getId());
		NumberFormat nf = new DecimalFormat(",###.##");

		ProductBasic product = productBasicService.getProductBasicInfo(
				new Long(borrow.getType()), borrow.getId());
		String date = "0";
		String repayType = "";
		if (0 == borrow.getBorrowTimeType()) {
			date = borrow.getTimeLimit() + "个月";
		} else {
			date = borrow.getTimeLimit() + "天";
		}
		if (1 == borrow.getStyle()) {
			repayType = "等额本息（按月分期还款）";
		} else if (2 == borrow.getStyle()) {
			repayType = "到期一次性还本付息";
		} else if (3 == borrow.getStyle()) {
			repayType = "按月付息到期还本息";
		} else {
			repayType = "按照设定日期还息，到期还本";
		}

		List<User> list = new ArrayList<User>();
		User user = new User();
		User user1 = new User();
		User user2 = new User();
		user.setWechatOpenId("oTXTUsh8o-GpwB_mpopSRjm2QMAk");// 孙昕妍
		user1.setWechatOpenId("oTXTUsgQNVGN0ePjcZON4KHoCsBU");// 唐荣
		user2.setWechatOpenId("oTXTUsj-oDYYovjyHryr6m6DfaOc");// 范咏晔
		list.add(user);
		list.add(user1);
		list.add(user2);

		for (int i = 0; i < list.size(); i++) {
			WechatMessage wechatMessage = new WechatMessage();
			wechatMessage.setAppId(WechatMessageData.A_APP_ID);
			wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
			wechatMessage.setType(WechatMessageData.Project_Line);
			wechatMessage.setFirstData("新产品上线啦！");
			wechatMessage.setProductName(borrow.getName());
			wechatMessage.setUrl(WechatMessageData.A_HOST
					+ "/nb/wechat/product/productDetail.action?product_id="
					+ product.getId());
			wechatMessage.setProductDate(date);
			wechatMessage.setOpenId(list.get(i).getWechatOpenId());
			if (borrow.getType() == 122) {
				wechatMessage.setProductProfit(nf.format(borrow
						.getExpectedLow())
						+ "% ~ "
						+ nf.format(borrow.getExpectedUp()) + "%");
			} else {
				wechatMessage
						.setProductProfit(nf.format(borrow.getApr()) + "%");
			}
			// 加息券
			if (null != borrow.getInterestRate()
					&& borrow.getInterestRate().getStatus() == ConstantUtil.FLAG_TRUE) {
				wechatMessage.setProductProfit(wechatMessage.getProductProfit()
						+ " + " + nf.format(borrow.getInterestRate().getRate())
						+ "%");
			}

			wechatMessage.setRepaymentMethod(repayType);
			wechatMessage.setRemark("募集金额：" + nf.format(borrow.getAccount())
					+ "元，欲购从速哦。\\n如有疑问请拨打客服热线400-6366-800！");
			try {
				wechatCacheService.sendWechatMessage(wechatMessage);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		printResult("发送给唐荣和孙昕妍成功！", true);
	}

	/**
	 * 非现金产品管理 - 截标 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowStopPage")
	public String borrowStopPage() throws Exception {
		setProductInfoIntoRequest(model.getId());

		return "borrowStopPage";
	}

	/**
	 * 非现金产品管理 - 截标 - 确定
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowStop")
	public void borrowStop() throws Exception {
		Borrow borrow = borrowService.find(model.getId());
		theBorrowService.stopBorrow(borrow, getOperator());

		OperationLog log = new OperationLog(borrow.getUser(), getOperator(),
				"stop_borrow");
		log.setOperationResult("用户名" + getOperator().getUserName() + "（"
				+ Global.getIP() + "）的操作员对用户为" + borrow.getUser().getUserName()
				+ "的借款标（ID:" + borrow.getId() + ",标名为" + borrow.getName()
				+ "）进行截标操作");
		operationLogService.save(log);

		printResult("截标成功！", true);
	}

	/**
	 * 非现金产品管理 - 所有非现金产品管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowTotallyManagerPage")
	public String borrowTotallyManagerPage() throws Exception {
		return "borrowTotallyManagerPage";
	}

	/**
	 * 非现金产品管理 - 所有非现金产品管理 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowTotallyList")
	public void borrowTotallyList() throws Exception {
		model.setSize(paramInt(ConstantUtil.ROWS));
		model.setStatus(Borrow.STATUS_UNRELATED);
		if (StringUtil.isNotBlank(paramString("status"))) {
			model.setStatus(paramInt("status"));
		}
		PageDataList<BorrowModel> list = theBorrowService.manageList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
//		System.out.println(getStringOfJpaObj(data));
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 非现金产品管理 - 所有非现金产品管理 - 导出Excel
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/exportExcleBorrowList")
	public void exportExcleBorrowList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setSize(10000);
		model.setStatus(Borrow.STATUS_UNRELATED);
		if (StringUtil.isNotBlank(paramString("status"))) {
			model.setStatus(paramInt("status"));
		}
		PageDataList<BorrowModel> list = theBorrowService.manageList(model);
		String title = "所有非现金产品列表";
		// 表头数组
		String[] hearders = new String[] { "编号", "产品编码", "产品名称", "状态", "产品类型",
				"借款公司名称", "有效时间（天）", "最低投资金额（元）", "最高投资金额（元）", "投资总额（元）",
				"收益率（%）", "期限", "进度(%)", "已投笔数（笔）", "应还款金额（元）", "已还款金额（元）",
				"还款方式", "红包","预计还款时间", "发布时间", "复审时间" };
		// 对象属性数组
		String[] fields = new String[] { "id", "bidNo", "name", "statusStr",
				"typeStr", "companyName", "validTime", "lowestAccount",
				"mostAccount", "account", "apr", "timeLimitStr", "scales",
				"tenderTimes", "repaymentAccount", "repaymentYesAccount",
				"styleStr", "redPacketName", "repayTime","fixedTime", "reviewTime" };
		TableData td = ExcelUtils.createTableData(list.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	private void setProductInfoIntoRequest(Long borrowId) {
		Borrow borrow = theBorrowService.getById(borrowId);
		request.setAttribute("borrow", BorrowModel.instance(borrow));

		// 产品相关信息
		request.setAttribute(
				"productBasic",
				productBasicService.getProductBasicInfo(
						new Long(borrow.getType()), borrowId));

		// 协议
		ProtocolConfig protocolConfig = protocolService.findById(Long
				.valueOf(borrow.getProtocolType()));
		request.setAttribute("protocolConfig", protocolConfig);
	}

	/**
	 * 借款人金额统计
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@Action("/modules/loan/borrow/borrowingStatisticsPage")
	public String borrowingStatisticsPage() throws Exception {
		return "borrowingStatisticsPage";
	}

	/**
	 * 投资人数统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/investmentStatisticsPage")
	public String investmentStatisticsPage() throws Exception {
		return "investmentStatisticsPage";
	}

	/**
	 * 汇率管理页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/exchangeRatePacketCaptureManager")
	public String exchangeRatePacketCaptureManager() throws Exception {
		return "exchangeRatePacketCaptureManager";
	}

	/**
	 * 汇率管理
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/exchangeRatePacketCaptureList")
	public void exchangeRatePacketCaptureList() throws Exception {
		model.setSize(paramInt("rows"));
		model.setPage(paramInt("page"));
		PageDataList<ExchangeRatePacketCaptureModel> list = borrowService
				.exchangeRatePacketCaptureList(model);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 汇率修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/exchangeRatePacketCaptureEditPage")
	public String exchangeRatePacketCaptureEditPage() throws Exception {
		long id = paramLong("id");
		ExchangeRatePacketCapture exchangeRatePacketCapture = borrowService
				.getExchangeRatePacketCapture(id);
		request.setAttribute("erpc", exchangeRatePacketCapture);
		return "exchangeRatePacketCaptureEditPage";
	}

	/**
	 * 汇率修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/exchangeRatePacketCaptureEdit")
	public void exchangeRatePacketCaptureEdit() throws Exception {
		long id = paramLong("id");
		double cashPurchasePrice = paramDouble("cashPurchasePrice");
		borrowService.exchangeRatePacketCaptureEdit(id, cashPurchasePrice);
		printResult("修改成功", true);
	}

	/**
	 * 添加汇率页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/exchangeRatePacketCaptureAddPage")
	public String exchangeRatePacketCaptureAddPage() throws Exception {
		return "exchangeRatePacketCaptureAddPage";
	}

	/**
	 * 添加汇率
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/borrow/exchangeRatePacketCaptureAdd")
	public void exchangeRatePacketCaptureAdd() throws Exception {
		String borrowName = paramString("borrowName");
		Borrow borrow = borrowService.getBorrowByBorrowName(borrowName);
		if (borrow == null) {
			throw new BorrowException("没有标名为【" + borrowName + "】的标！",
					BorrowException.TYPE_JSON);
		}
		if (borrow.getStatus() != 6 && borrow.getStatus() != 8) {
			throw new BorrowException("该标的状态不能进行添加！", BorrowException.TYPE_JSON);
		}
		double cashPurchasePrice = paramDouble("cashPurchasePrice");
		ExchangeRatePacketCapture erpc = new ExchangeRatePacketCapture();
		erpc.setBorrow(borrow);
		erpc.setCashPurchasePrice(cashPurchasePrice);
		String addTime = paramString("addTime");
		erpc.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.parse(addTime));
		borrowService.exchangeRatePacketCaptureAdd(erpc);
		printResult("添加成功", true);
	}

	/**
	 * 贷后管理 - 投标管理 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowTenderManager")
	public String borrowTenderManager() throws Exception {
		return "borrowTenderManager";
	}

	/**
	 * 贷后管理 - 投标管理 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/product/borrow/borrowTenderList")
	public void borrowTenderList() throws Exception {
		model.setSize(paramInt("rows"));
		model.setPage(paramInt("page"));
		PageDataList<BorrowTenderModel> list = borrowTenderService
				.bidList(model);

		data = new HashMap<String, Object>();
		data.put(ConstantUtil.TOTAL, list.getPage().getTotal());
		data.put(ConstantUtil.ROWS, list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户清单 导出报表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/product/borrow/exportExcelBidList")
	public void exportExcelBidList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setSize(10000);
		PageDataList<BorrowTenderModel> list = borrowTenderService
				.bidList(model);
		String title = "投标信息列表";
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "产品编码", "标名",
				"状态", "投标金额", "投标时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "bidNo",
				"borrowName", "statusStr", "account", "addTime" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(list.getList(),
				ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	/**
	 * 线程发布
	 * 
	 * @author Jinx
	 *
	 */
	class SendMsgThead extends Thread {
		private List<User> list;
		private Borrow borrow;

		NumberFormat nf = new DecimalFormat(",###.##");

		public void run() {
			if (null != list && !list.isEmpty()) {

				long productId = productBasicService.getProductBasicInfo(
						new Long(borrow.getType()), borrow.getId()).getId();
				String date = "0";
				String repayType = "";
				if (0 == borrow.getBorrowTimeType()) {
					date = borrow.getTimeLimit() + "个月";
				} else {
					date = borrow.getTimeLimit() + "天";
				}
				if (1 == borrow.getStyle()) {
					repayType = "等额本息（按月分期还款）";
				} else if (2 == borrow.getStyle()) {
					repayType = "到期一次性还本付息";
				} else if (3 == borrow.getStyle()) {
					repayType = "按月付息到期还本息";
				} else {
					repayType = "按照设定日期还息，到期还本";
				}

				for (int i = 0; i < list.size(); i++) {
					WechatMessage wechatMessage = new WechatMessage();
					wechatMessage.setAppId(WechatMessageData.A_APP_ID);
					wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
					wechatMessage.setType(WechatMessageData.Project_Line);
					wechatMessage.setFirstData("新产品上线啦！");
					wechatMessage.setProductName(borrow.getName());
					wechatMessage
							.setUrl(WechatMessageData.A_HOST
									+ "/nb/wechat/product/productDetail.action?product_id="
									+ productId);
					wechatMessage.setProductDate(date);
					wechatMessage.setOpenId(list.get(i).getWechatOpenId());
					if (borrow.getType() == 122) {
						wechatMessage.setProductProfit(nf.format(borrow
								.getExpectedLow())
								+ "% ~ "
								+ nf.format(borrow.getExpectedUp()) + "%");
					} else {
						wechatMessage.setProductProfit(nf.format(borrow
								.getApr()) + "%");
					}
					// 加息券
					if (null != borrow.getInterestRate()
							&& borrow.getInterestRate().getStatus() == ConstantUtil.FLAG_TRUE) {
						wechatMessage.setProductProfit(wechatMessage
								.getProductProfit()
								+ " + "
								+ nf.format(borrow.getInterestRate().getRate())
								+ "%");
					}

					wechatMessage.setRepaymentMethod(repayType);
					wechatMessage.setRemark("募集金额："
							+ nf.format(borrow.getAccount())
							+ "元,欲购从速哦。\\n如有疑问请拨打客服热线400-6366-800！");
					try {
						wechatCacheService.sendWechatMessage(wechatMessage);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public List<User> getList() {
			return list;
		}

		public void setList(List<User> list) {
			this.list = list;
		}

		public Borrow getBorrow() {
			return borrow;
		}

		public void setBorrow(Borrow borrow) {
			this.borrow = borrow;
		}

		public SendMsgThead(Borrow borrow, List<User> list) {
			this.borrow = borrow;
			this.list = list;
		}

	}

}
