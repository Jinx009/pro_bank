package com.rongdu.manage.action.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.model.BorrowInterestRateModel;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.customer.model.AccountLogModel;
import com.rongdu.p2psys.customer.model.CustomerBaseinfoModel;
import com.rongdu.p2psys.customer.model.CustomerProductModel;
import com.rongdu.p2psys.customer.model.CustomerRedPacketModel;
import com.rongdu.p2psys.customer.model.ReferrerModel;
import com.rongdu.p2psys.customer.service.CustomerService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPromotModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 用户管理
 * 
 * @author sj
 * @version 2.0
 * @since 2014年4月17日
 */
public class ReferrerAction extends BaseAction<ReferrerModel> implements
		ModelDriven<ReferrerModel> {

	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCertificationService attestationService;
	@Resource
	private CustomerService customerService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private AccountService accountService;
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;

	private Map<String, Object> data;

	
	/**
	 * 获得用户产品列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/customer/referrer")
	public String referrer() throws Exception {
		return "referrer";
	}

	
	/**
	 * 导出报表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/customer/exportExcelReferrer")
	public void exportExcelRecharge() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		int status = paramInt("status");
		// 第一页开始
		// 记录取得
		List<ReferrerModel> list =  customerService.referrerList(1, 5000, model).getList();

		String title = "推荐人表";
		String[] hearders = new String[] {  "编号", "销售代码", "销售姓名", "客户姓名", "客户电话",  "推荐码", "推荐人", "推荐人手机号", "推荐码使用次数" };// 表头数组
		String[] fields = new String[] { "id", "saleCode", "saleName", "realName", "phone", "recommendCode", "referrer", "referrerPhone", "codeUsedTimes"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}	
	
	
	/**
	 * 获得用户清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/customer/referrerList")
	public void referrerList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = request.getParameter("status");
//		if (status == null) {
//			model.setStatus(99);
//		}
		PageDataList<ReferrerModel> pagaDataList = customerService.referrerList(pageNumber, pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
	

	
}
