package com.rongdu.manage.action.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 借贷管理-待还信息管理
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月17日
 */
public class ManageRepaymentAction extends BaseAction<BorrowRepaymentModel> implements ModelDriven<BorrowRepaymentModel> {

	BorrowRepaymentModel model = new BorrowRepaymentModel();

	public BorrowRepaymentModel getModel() {
		return model;
	}

	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private BorrowService borrowService;

	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 待还信息展示
	 * 
	 * @return
	 */
	@Action("/modules/loan/repayment/repaymentManager")
	public String repaymentManager() throws Exception {

		return "repaymentManager";
	}

	/**
	 * 待收信息列表
	 * 
	 * @return
	 */
	@Action("/modules/loan/repayment/repaymentList")
	public void repaymentList() throws Exception {
		model.setSize(paramInt("rows"));
		PageDataList<BorrowRepaymentModel> list = borrowRepaymentService.list(model, searchName);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 待收利息列表导出
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/repayment/exportExcelRepayment")
	public void exportExcelRepayment() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setSize(5000);
		List<BorrowRepaymentModel> list = borrowRepaymentService.list(model, searchName).getList();
		String title = "还款信息列表";
		String[] hearders = new String[] { "编号", "用户名", "借款人姓名", "产品编码", "标名", "期数", "预还时间","实还时间", "预还金额", "已还金额", "本金", "利息", "状态"};// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "bidNo", "borrowName", "period", "repaymentTime", "repaymentYesTime", "repaymentAccount", "repaymentYesAccount", "capital", "interest", "statusStr"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}

	// ----------zf start-------------
	/**
	 * 垫付信息展示
	 * 
	 * @return
	 */
	@Action("/modules/loan/repayment/advanceManager")
	public String advanceManager() throws Exception {

		return "advanceManager";
	}

	/**
	 * 垫付信息列表
	 * 
	 * @return
	 */
	@Action("/modules/loan/repayment/advanceList")
	public void advanceList() throws Exception {

		model.setSize(paramInt("rows"));
		model.setStatus(2);
		PageDataList<BorrowRepaymentModel> list = borrowRepaymentService.list(model, searchName);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	// ----------zf end-------------
	
	/**
	 * 待还信息展示(浮动收益类产品)
	 * 
	 * @return
	 */
	@Action("/modules/loan/repayment/repaymentEntrustManager")
	public String repaymentEntrustManager() throws Exception {
		
		return "repaymentEntrustManager";
	}
	
	/**
	 * 待收信息列表(浮动收益类产品)
	 * 
	 * @return
	 */
	@Action("/modules/loan/repayment/repaymentEntrustList")
	public void repaymentEntrustList() throws Exception {
		model.setSize(paramInt("rows"));
		String searchName = paramString("searchName");
		PageDataList<BorrowRepaymentModel> list = borrowRepaymentService.repaymentEntrustList(model, searchName);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 设置预期收益率
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/repayment/repaymentEntrustEditPage")
	public String repaymentEntrustEditPage() throws Exception {
		long repaymentId = paramLong("id");
		request.setAttribute("repaymentId", repaymentId);
		return "repaymentEntrustEditPage";
	}
	
	/**
	 * 设置预期收益率
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/repayment/repaymentEntrustEdit")
	public void repaymentEntrustEdit() throws Exception {
		long repaymentId = paramLong("repaymentId");
		double expectedRate = paramDouble("expectedRate");  
		borrowService.repaymentEntrustEdit(repaymentId, expectedRate);
		printResult("设置预期收益率成功", true);
	}
	
	/**
	 * 还款
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/repayment/repaymentEntrust")
	public void repaymentEntrust() throws Exception {
		long repaymentId = paramLong("id");
		BorrowRepayment repayment = borrowRepaymentService.findById(repaymentId);
		borrowService.doRepay(repayment);
		printResult("还款成功", true);	
	}
	
}
