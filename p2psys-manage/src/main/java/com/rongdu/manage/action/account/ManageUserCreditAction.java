package com.rongdu.manage.action.account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserCreditApply;
import com.rongdu.p2psys.user.domain.UserCreditLog;
import com.rongdu.p2psys.user.model.UserCreditApplyModel;
import com.rongdu.p2psys.user.service.UserCreditApplyService;
import com.rongdu.p2psys.user.service.UserCreditService;

/**
 * 信用额度申请action
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-24
 */
@SuppressWarnings("rawtypes")
public class ManageUserCreditAction extends BaseAction implements ModelDriven<UserCreditApplyModel> {

	@Resource
	private UserCreditApplyService userCreditApplyService;
	@Resource
	private UserCreditService userCreditService;
	private Map<String, Object> data = new HashMap<String, Object>();
	private UserCreditApplyModel model = new UserCreditApplyModel();

	/**
	 * 信用额度申请列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/userCredit/userCreditApplyManager")
	public String UserCreditApplyManager() throws Exception {
		return "userCreditApplyManager";
	}

	/**
	 * 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/userCredit/userCreditApplyList")
	public void userCreditApplyList() throws Exception {
		// 进入页面展示待审核的列表
		if (model.getStatus() == 0) {
			model.setStatus(2);
		}
		PageDataList<UserCreditApplyModel> pageList = userCreditApplyService.list(model);
		int totalPage = pageList.getPage().getTotal();
		data.put("total", totalPage);
		data.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 审核页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/account/userCredit/userCreditApplyEditPage")
	public String userCreditApplyEditPage() throws Exception {
		UserCreditApply userCreditApply = userCreditApplyService.findById(model.getId());
		request.setAttribute("userCreditApply", userCreditApply);
		return "userCreditApplyEditPage";
	}

	/**
	 * 审核
	 * 
	 * @return
	 * @throws Exception if has error
	 */
	@Action(value = "/modules/account/userCredit/userCreditApplyEdit")
	public void userCreditApplyEdit() throws Exception {
		UserCreditApply userCreditApply = userCreditApplyService.findById(model.getId());
		UserCredit userCredit = userCreditService.findByUserId(userCreditApply.getUser().getUserId());
		if (userCredit != null) {
			userCreditApply.setAccountOld(userCredit.getCredit());
		}
		if (model.getStatus() == 1) {
			userCreditApply.setAccountNew(userCreditApply.getAccount() + userCreditApply.getAccountOld());
		} else {
			userCreditApply.setAccountNew(userCreditApply.getAccountOld());
		}
		userCreditApply.setStatus(model.getStatus());
		userCreditApply.setVerifyRemark(model.getVerifyRemark());
		userCreditApply.setVerifyUser(getOperator().getId());
		userCreditApply.setVerifyTime(new Date());
		UserCreditLog userCreditLog = fillUserCreditLog(userCredit, userCreditApply);
		userCreditService.verifyApplyUserCredit(userCreditApply, userCreditLog, getOperator());
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	private UserCreditLog fillUserCreditLog(UserCredit userCredit, UserCreditApply userCreditApply) {
		UserCreditLog userCreditLog = new UserCreditLog();
		userCreditLog.setUser(userCredit.getUser());
		if (userCreditApply.getStatus() == 1) {
			userCreditService.update(userCreditApply.getAccount(), userCreditApply.getAccount(), 0, userCreditApply
					.getUser().getUserId());
			userCreditLog.setAccount(userCreditApply.getAccount());
			userCreditLog.setAccountAll(BigDecimalUtil.add(userCredit.getCredit(), userCreditApply.getAccount()));
			userCreditLog.setAccountUse(BigDecimalUtil.add(userCredit.getCreditUse(), userCreditApply.getAccount()));
			userCreditLog.setAccountNoUse(userCredit.getCreditNouse());
			userCreditLog.setType("apply_add");
			userCreditLog.setRemark("申请额度审核通过");
		} else {
			userCreditLog.setAccount(userCreditApply.getAccount());
			userCreditLog.setAccountAll(userCredit.getCredit());
			userCreditLog.setAccountUse(userCredit.getCreditUse());
			userCreditLog.setAccountNoUse(userCredit.getCreditNouse());
			userCreditLog.setType("apply_add");
			userCreditLog.setRemark("申请额度审核不通过");
		}
		return userCreditLog;
	}

	@Override
	public UserCreditApplyModel getModel() {
		return model;
	}

}
