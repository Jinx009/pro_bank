package com.rongdu.manage.action.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 借贷管理-待收信息管理
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月17日
 */
@SuppressWarnings("rawtypes")
public class ManageCollectionAction extends BaseAction implements ModelDriven<BorrowCollectionModel> {

	BorrowCollectionModel model = new BorrowCollectionModel();

	public BorrowCollectionModel getModel() {
		return model;
	}

	@Resource
	private BorrowCollectionService borrowCollectionService;

	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 待收信息展示
	 * 
	 * @return
	 */
	@Action("/modules/loan/collection/collectionManager")
	public String collectionManager() throws Exception {

		return "collectionManager";
	}

	/**
	 * 待收信息列表
	 * 
	 * @return
	 */
	@Action("/modules/loan/collection/collectionList")
	public void collectionList() throws Exception {
		model.setSize(paramInt("rows"));
		PageDataList<BorrowCollectionModel> list = borrowCollectionService.list(model);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 待收信息报表
	 * @throws Exception
	 */
	@Action("/modules/loan/collection/exportExcelCollection")
	public void exportExcelCollection() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		// 第一页开始
		model.setPage(1);
		// 最多出5000条记录
		model.setSize(5000);
		List<BorrowCollectionModel> list = borrowCollectionService.list(model).getList();
		String title = "待收信息列表";
		String[] hearders = new String[] { "编号", "用户名", "投资人姓名","产品编码", "标名", "期数", "预收时间", "预收金额", "本金", "利息"};// 表头数组
		String[] fields = new String[] { "id", "investUserName", "investRealName", "bidNo", "borrowName", "period", "repaymentTime", "repaymentAccount", "capital", "interest"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
}
