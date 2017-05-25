package com.rongdu.manage.action.account;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.ProductsCostModel;
import com.rongdu.p2psys.account.service.ProductsCostService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

public class ManageProductCostAction extends BaseAction<ProductsCostModel>
		implements ModelDriven<ProductsCostModel> {
	@Resource
	private ProductsCostService productsCostService;
	
	ProductsCostModel model = new ProductsCostModel();
	
	public ProductsCostModel getModel() {
		return model;
	}
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * 产品收费记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/productCost/productCostManager")
	public String productCostManager() throws Exception {
		
		return "productCostManager";
	}
	
	/**
	 * 产品收费记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/productCost/productCostList")
	public void productCostList() throws Exception {
		PageDataList<ProductsCostModel> pageDataList = productsCostService.list(model);
		data.put("total", pageDataList.getPage().getTotal());
		data.put("rows", pageDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	@Action("/modules/productCost/exportExcelProductCostList")
	public void exportExcelProductCostList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		model.setPage(1);
		model.setRows(5000);
		PageDataList<ProductsCostModel> pageDataList = productsCostService.list(model);
		String title = "产品收费记录";
		String[] hearders = new String[] { "编号", "产品名称", "产品编码", "管理费（元）", "风险备用金（元）", "添加时间"};//表头数组
		String[] fields = new String[] { "id", "name", "code", "manageFee", "riskReserveFee", "addTime"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(pageDataList.getList(), ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
}
