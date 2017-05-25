package com.rongdu.manage.action.account;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.CompBank;
import com.rongdu.p2psys.account.domain.CompCash;
import com.rongdu.p2psys.account.model.CompCashModel;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.CompBankService;
import com.rongdu.p2psys.account.service.CompCashService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 对公付款
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
public class ManageCompCashAction extends BaseAction<CompCashModel> implements
		ModelDriven<CompCashModel> {
	@Resource
	private CompCashService compCashService;
	@Resource
	private CompBankService compBankService;
	
	private CompCashModel model = new CompCashModel();

	public CompCashModel getModel() {
		return model;
	}
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * 对公付款管理 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashManager")
	public String compCashManager() throws Exception {
		
		return "compCashManager";
	}
	
	/**
	 * 对公付款记录
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashList")
	public void compCashList() throws Exception {
		String tppStatus = paramString("tppStatus");
		if (StringUtil.isBlank(tppStatus)) {
			model.setTppStatus(99);
		}
		String webStatus = paramString("webStatus");
		if (StringUtil.isBlank(webStatus)) {
			model.setWebStatus(99);
		}
//		String startTime=paramString("startTime");
//		String endTime=paramString("endTime");
//		if (StringUtil.isNotBlank(startTime)) {
//			model.setStartTime(startTime);
//		}
//		if (StringUtil.isNotBlank(endTime)) {
//			model.setStartTime(endTime);
//		}
		PageDataList<CompCashModel> pageList = compCashService.pageDataList(model);
		int totalPage = pageList.getPage().getTotal();
		data.put("total", totalPage);
		data.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 对公付款导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/exportExcleCompCashList")
	public void exportExcleCompCashList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		String tppStatus = paramString("tppStatus");
		if (StringUtil.isBlank(tppStatus)) {
			model.setTppStatus(99);
		}
		String webStatus = paramString("webStatus");
		if (StringUtil.isBlank(webStatus)) {
			model.setWebStatus(99);
		}
//		String startTime=paramString("startTime");
//		String endTime=paramString("endTime");
//		if (StringUtil.isNotBlank(startTime)) {
//			model.setStartTime(startTime);
//		}
//		if (StringUtil.isNotBlank(endTime)) {
//			model.setStartTime(endTime);
//		}
		
		model.setPage(1);
		model.setRows(5000);
		PageDataList<CompCashModel> pageList = compCashService.pageDataList(model);
		
		String title = "对公付款Excel表";

		String[] hearders = new String[] { "编号", "提现金额(元)", "申请状态","银联状态", "银行卡号", "账户名称", "开户行名称", "申请人", "审核人", "添加时间", "提现IP" };// 表头数组
		String[] fields = new String[] { "id", "amount", "webStatusStr", "tppStatusStr", "cardNo", "accName", "bankName", "addOpName", "verifyOpName", "addTime", "addIp" };// 对象属性数组
		TableData td = ExcelUtils.createTableData(pageList.getList(), ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	
	/**
	 * 对公付款 页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashAddPage")
	public String compCashAddPage() throws Exception {
		List<CompBank> list = compBankService.list();
		//入账商户
		UnionPayRet inRet = SignHelper.queryProv(1);
		request.setAttribute("inProvAmount", inRet.getAmount());
		//出账商户
		UnionPayRet outRet = SignHelper.queryProvForCash(1);
		request.setAttribute("outProvAmount", outRet.getAmount());
		request.setAttribute("bankList", list);
		return "compCashAddPage";
	}
	
	/**
	 * 对公付款
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashAdd")
	public void compCashAdd() throws Exception {
		CompBank bank = compBankService.findById(paramLong("bankId"));
		if (bank != null) {
			CompCash cash = new CompCash(CompCashModel.WEB_PROCESSING, CompCashModel.TPP_PROCESSING, model.getAmount(), bank.getCardNo(), 
					bank.getAccName(), bank.getBankName(), bank.getBankCode(), bank.getProvince(), bank.getCity(), model.getRemark());
			cash.setAddOperator(getOperator());
			compCashService.save(cash);
		}
		printResult(MessageUtil.getMessage("I10001"), true);
	}
	
	/**
	 * 对公付款待审页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashVerifyManage")
	public String compCashVerifyManage() throws Exception {
		
		return "compCashVerifyManage";
	}
	
	/**
	 * 对公付款待审列表
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashVerifyList")
	public void compCashVerifyList() throws Exception {
		model.setWebStatus(CompCashModel.WEB_PROCESSING);
		PageDataList<CompCashModel> pageList = compCashService.pageDataList(model);
		int totalPage = pageList.getPage().getTotal();
		data.put("total", totalPage);
		data.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 审核通过
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashVerifySuccess")
	public void compCashVerifySuccess() throws Exception {
		CompCash compCash = compCashService.findById(model.getId());
		UnionPay pay = new UnionPay(compCash.getCardNo(), compCash.getAccName(), compCash.getBankName(), compCash.getBankCode(), 
				(long)compCash.getAmount()*100, 1, compCash.getProvince(), compCash.getCity(), compCash.getRemark());
		pay.setOrderNo(compCash.getOrderNo());
		if (compCash.getWebStatus() != 0) {
			printResult(MessageUtil.getMessage("订单已审核，请勿重复操作"), false);
			return;
		}
		UnionPayRet ret = SignHelper.paymentForComp(pay);
		if(ret.getOrderStatus() == 2) {
			compCash.setTppStatus(CompCashModel.TPP_SUCCESS);
		} else if(ret.getOrderStatus() == 3) {
			compCash.setTppStatus(CompCashModel.TPP_FAIL);
		} else if(!"0000".equals(ret.getRetCode())) {//失败
			compCash.setTppStatus(CompCashModel.TPP_FAIL);
		}
		compCash.setWebStatus(CompCashModel.WEB_SUCCESS);
		compCash.setVerifyOperator(getOperator());
		compCash.setVerifyTime(new Date());
		compCashService.update(compCash);
		printResult(MessageUtil.getMessage("I10009"), true);
	}
	
	/**
	 * 审核不通过
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/compCashVerifyFail")
	public void compCashVerifyFail() throws Exception {
		CompCash compCash = compCashService.findById(model.getId());
		compCash.setWebStatus(CompCashModel.WEB_FAIL);
		compCash.setTppStatus(CompCashModel.TPP_FAIL);
		compCash.setVerifyOperator(getOperator());
		compCash.setVerifyTime(new Date());
		compCashService.update(compCash);
		printResult(MessageUtil.getMessage("I10009"), true);
	}
	
	/**
	 * 备付金查询
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compCash/queryProv")
	public void queryProv() throws Exception {
		//入账商户号
		UnionPayRet inRet = SignHelper.queryProv(1);
		data.put("inProvAmount", inRet.getAmount());
		//投资人提现商户号
		UnionPayRet outRet = SignHelper.queryProvForCash(1);
		data.put("outProvAmount", outRet.getAmount());
		printJson(getStringOfJpaObj(data));
	}
}
