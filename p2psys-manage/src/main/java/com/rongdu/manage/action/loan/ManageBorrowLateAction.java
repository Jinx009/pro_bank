package com.rongdu.manage.action.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.service.BorrowConfigService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;

/**
 * 借贷管理-逾期信息管理
 * 
 * @author cx
 * @version 2.0 2014-11-17
 */
@SuppressWarnings("rawtypes")
public class ManageBorrowLateAction extends BaseAction implements
		ModelDriven<BorrowRepaymentModel> {

	BorrowRepaymentModel model = new BorrowRepaymentModel();
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private BorrowConfigService borrowConfigService;

	private Map<String, Object> data = new HashMap<String, Object>();

	// 是否启用接口
	public static final String isOpenAip = String.valueOf(BaseTPPWay
			.isOpenApi());

	/**
	 * 逾期信息展示
	 * 
	 * @return
	 */
	@Action("/modules/loan/borrowLate/borrowLateManager")
	public String borrowLateManager() throws Exception {
		List<BorrowConfig> borrowConfigList = borrowConfigService
				.findAllNotFlowAndSecond();
		request.setAttribute("borrowConfigList", borrowConfigList);
		request.setAttribute("isOpenAip", isOpenAip);
		return "borrowLateManager";
	}

	/**
	 * 逾期信息列表
	 * 
	 * @return
	 */
	@Action("/modules/loan/borrowLate/borrowLateList")
	public void borrowLateList() throws Exception {
		model.setSize(paramInt("rows"));
		if ("".equals(paramString("status"))) {
			model.setStatus(99);
		}
		// 是否逾期
		model.setLate(true);
		PageDataList<BorrowRepaymentModel> list = borrowRepaymentService
				.list(model, searchName);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 逾期信息列表报表
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowLate/exportExcelBorrowLate")
	public void exportExcelBorrowLate() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setSize(5000);
		if ("".equals(paramString("status"))) {
			model.setStatus(99);
		}
		// 是否逾期
		model.setLate(true);
		List<BorrowRepaymentModel> list = borrowRepaymentService.list(model, searchName).getList();
		String title = "逾期信息列表";
		String[] hearders = new String[] { "编号", "用户名", "借款人姓名", "标名", "期数", "预还时间","实还时间", "预还金额", "已还金额", "本金", "利息", "逾期天数", "罚息", "状态"};// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "borrowName", "period", "repaymentTime", "repaymentYesTime", "repaymentAccount", "repaymentYesAccount", "capital", "interest", "lateDays", "lateInterest", "statusStr"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	/**
	 * 逾期信息展示
	 * 
	 * @return
	 */
	@Action("/modules/loan/borrowLate/borrowLateEditPage")
	public String borrowLateEditPage() throws Exception {
		BorrowRepayment borrowRepayment = borrowRepaymentService.findById(model
				.getId());
		request.setAttribute("borrowRepayment", borrowRepayment);
		return "borrowLateEditPage";
	}

	/**
	 * 逾期垫付
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowLate/overdue")
	public void overdue() throws Exception {
		BorrowRepayment borrowRepayment = borrowRepaymentService.findById(model
				.getId());
		borrowRepaymentService.overdue(borrowRepayment, getOperator());
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	public BorrowRepaymentModel getModel() {
		return model;
	}

}
