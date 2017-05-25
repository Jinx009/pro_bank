package com.rongdu.p2psys.web.borrow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserCreditApply;
import com.rongdu.p2psys.user.model.UserCreditApplyModel;
import com.rongdu.p2psys.user.service.UserCreditApplyService;
import com.rongdu.p2psys.user.service.UserCreditService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 我的借款
 * @author cx
 * @version 2.0
 * 2014-10-30
 */
@SuppressWarnings("rawtypes")
public class MyBorrowAction extends BaseAction implements ModelDriven<BorrowModel> {

	private BorrowModel model = new BorrowModel();

	@Override
	public BorrowModel getModel() {
		return model;
	}

	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private UserCreditService userCreditService;
	@Resource
	private UserCreditApplyService userCreditApplyService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private AccountService accountService;
	@Resource
	private UserService userService;
	private User user;
	private Map<String, Object> data;

	/**
	 * 查看正在借款项目详情
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/borrow/mine")
	public String mine() throws Exception {
		String status = request.getParameter("status");
		if(status != null){
			request.setAttribute("status", status);
		}
		request.setAttribute("isOpenAip",String.valueOf(BaseTPPWay.isOpenApi()));
		return "mine";
	}
	/**
	 * 我的借款列表 ajax数据接口 status(标状态) start_time end_time(开始结束时间) name(标名)
	 * 
	 * @return
	 */
	@Action("/member/borrow/mineList")
	public void mineList() throws Exception {
		user = getSessionUser();
		model.setUser(user);
		model.setIsNovice(99);
		PageDataList<BorrowModel> pageDataList = borrowService.getList(model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 撤标 初审之前
	 * 
	 * @throws Exception if has error
	 */
	@Action("/member/borrow/cancel")
	public void cancel() throws Exception {
		Borrow borrow = borrowService.find(model.getId());
		borrow.setRepaymentAccount(-1); //标记前台撤标
		borrowService.cancel(borrow);
		frontRedirect("/member/borrow/mine.html"); //重定向到撤标之后的页面
	}

	/**
	 * 我的待还
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member_borrow/borrow/repayment")
	public String repayment() throws Exception {
		
		request.setAttribute("borrowId", paramLong("borrowId"));
		return "repayment";
	}

	/**
	 * 我的待还列表 ajax数据接口 status 0待还 1已还 start_time end_time (发布时间) name 标名
	 * 
	 * @return
	 * @throws Exception if has error
	 */
	@Action("/member/borrow/repaymentList")
	public void repaymentList() throws Exception {
		user = getSessionUser();
		BorrowRepaymentModel m = (BorrowRepaymentModel) paramModel(BorrowRepaymentModel.class);
		m.setBorrowId(paramLong("borrowId"));
		m.setUser(user);
		int status = this.paramInt("status");
		m.setStatus(status);
		PageDataList<BorrowRepaymentModel> pageDataList = borrowRepaymentService.getList(m);
		Account account = accountService.findByUser(this.getSessionUserId());
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		data.put("account", account);
		data.put("api_code", Global.getValue("api_code"));
		data.put("is_open_deposit", Global.getValue("is_open_deposit"));
		printWebJson(getStringOfJpaObj(data));
	}
		
	/**
	 * 判断是否已设置交易密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/member/borrow/checkPayPassword")
	public String checkPayPassword() throws Exception {
		data = new HashMap<String, Object>();
		User newUser = userService.getUserByUserName(getSessionUser().getUserName());
		
		boolean result = newUser.getPayPwd() == null ? false : true;
		data.put("result", result);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 标准版正常还款
	 * @throws Exception if has error
	 */
	@Action("/member/borrow/repay")
	public void repay() throws Exception {
		User newUser = userService.getUserByUserName(getSessionUser().getUserName());
		model.checkPayPwd(newUser);
		BorrowRepayment borrowRepayment = borrowRepaymentService.findById(model.getRepaymentId());
		if(borrowRepayment.getStatus() ==2 && borrowRepayment.getWebStatus() == 3){//网站垫付
			borrowService.overduePayment(borrowRepayment);
		}else{//正常还款
			borrowService.doRepay(borrowRepayment);
		}
		printWebSuccess();
	}
	
	/**
	 * 托管正常还款
	 * @throws Exception if has error
	 */
	@Action(value="/invest/repaySkip")
	public void repaySkip() throws Exception {
    	user = getSessionUser();
//		model.checkPayPwd(user);
		BorrowRepayment repayment = borrowRepaymentService.findById(model.getRepaymentId());
		borrowService.doRepay(repayment);
		printWebSuccess();
	}
	
	/**
	 * 前台逾期垫付
	 * @throws Exception
	 */
	@Action("/member/borrow/overduePayment")
	public void overduePayment() throws Exception {
		user = getSessionUser();
//		model.checkPayPwd(user);
		BorrowRepayment borrowRepayment = borrowRepaymentService.findById(model.getRepaymentId());
		borrowService.overduePayment(borrowRepayment);
		printWebSuccess();
	}
	
	/**
	 * 提前还款
	 * @throws Exception if has error
	 */
	@Action("/member/borrow/doPriorRepay")
	public void doPriorRepay() throws Exception {
		user = getSessionUser();
		model.checkPayPwd(user);
		BorrowRepayment borrowRepayment = borrowRepaymentService.findById(model.getRepaymentId());
		borrowService.doPriorRepay(borrowRepayment);
		printWebSuccess();
	}

	/**
	 * 下载协议
	 * 
	 * @return
	 */
	@Action("/member/borrow/protocol")
	public String protocol() throws Exception {

		return null;
	}

	/**
	 * 信用额度页面
	 */
	@Action("/member_borrow/borrow/creditApply")	
	public String creditApply() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		// 申请中
		int applyIngCount = userCreditApplyService.count(userId, 2);
		// 申请记录
		request.setAttribute("applyIngCount", applyIngCount);
		return "creditApply";
	}
	/**
	 * 新增信用额度页面
	 */
	@Action("/member_borrow/borrow/addApply")	
	public String addApply() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		// 申请中
		int applyIngCount = userCreditApplyService.count(userId, 2);
		// 申请记录
		request.setAttribute("applyIngCount", applyIngCount);
		return "addApply";
	}
	

	/**
	 * 信用额度列表
	 * 
	 * @throws Exception
	 */
	@Action("/member/borrow/creditApplyList")
	public void creditApplyList() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		PageDataList<UserCreditApply> applyList = userCreditApplyService.list(userId, model);
		data = new HashMap<String, Object>();
		data.put("data", applyList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 信用额度申请
	 * 
	 * @throws Exception
	 */
	@Action("/member/borrow/doCreditApply")
	public void doCreditApply() throws Exception {
		user = getSessionUser();
		AccountModel accountModel = new AccountModel();
		accountModel.validAttestionForAmountApply(getSessionUserIdentify(), model.getAmount());
		UserCredit uCredit = userCreditService.findByUserId(user.getUserId());
		UserCreditApplyModel userCreditApplyModel=new UserCreditApplyModel();
		userCreditApplyModel.validRemarkAndContentForAmountApply(model.getRemark(), model.getContent());
		UserCreditApply CreditApply = new UserCreditApply(user, model.getAmount(), 2, uCredit, model.getContent(),
				model.getRemark());
		userCreditService.applyUserCredit(CreditApply);
		printWebSuccess();
	}
}
