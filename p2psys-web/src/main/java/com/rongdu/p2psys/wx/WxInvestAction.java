package com.rongdu.p2psys.wx;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.borrow.service.BorrowUploadService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.service.UserBaseInfoService;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserUploadService;
import com.rongdu.p2psys.user.service.UserVipService;

/**
 * 我要投资
 * 
 * @author cx
 * @version 2.0
 */
public class WxInvestAction extends BaseAction<BorrowModel> implements
		ModelDriven<BorrowModel> {

	@Resource
	private BorrowService borrowService;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private UserCertificationService userCertificationService;
	@Resource
	private UserCertificationApplyService userCertificationApplyService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private AccountService accountService;
	@Resource
	private UserService userService;
	@Resource
	private UserUploadService userUploadService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserBaseInfoService userBaseInfoService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private UserVipService userVipService;

	private User user;
	private Map<String, Object> data;

	public static final boolean isOpenAip = BaseTPPWay.isOpenApi();

	/**
	 * 投标 较为特殊需要跳转页面
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@Action(value = "/wx/account/tender", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") }, results = {
			@Result(name = "ipsTender", type = "ftl", location = "/tpp/ipstender.html"),
			@Result(name = "pnrTender", type = "ftl", location = "/tpp/chinapnr/initiativeTende.html"),
			@Result(name = "result", type = "ftl", location = "/wechat/result.html"),
			@Result(name = "msg", type = "ftl", location = "/wechat/msg.html") })
	public String tender() throws Exception {
		user = getSessionUser();
		try {
			this.checkToken("tenderToken");
			if (user.getUserCache().getUserType() == 2) {
				throw new BussinessException("借款账户不能投资");
			}
		} catch (BussinessException e) {
			throw new BussinessException(e.getMessage(), 2);
		}
		Borrow borrow = borrowService.find(this.model.getId());
		if (borrow.getFixedTime() != null) {
			if (new Date().before(borrow.getFixedTime())) {
				throw new UserException("标还未到定时时间", 2);
			}
		}
		if (borrow.getIsNovice() == 1) {// 如果是新手标则校验用户是否为新手
			borrowTenderService.checkNovice(user.getUserId());
		}
		// 投标前model值校验
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		BorrowModel m = BorrowModel.instance(worker.prototype());

		m.setPayPwd(model.getPayPwd());
		m.setBorrowInterestRateValue(model.getBorrowInterestRateValue());
		m.setMoney(model.getMoney());
		System.out.println(model.getPayPwd());
		m.checkTenderModel(borrow, user);

		worker.checkTender(m, model.getMoney(), 0, user,
				StringUtil.isNull(model.getPwd()));

		double sumBidMoney = borrowService.sumBidMoney(m);
		if (borrow.getAccount() - sumBidMoney < model.getMoney()) {
			throw new BorrowException("您对该标的投资已达到最多投标总额(￥"
					+ (borrow.getAccount() - sumBidMoney) + ")，不能继续投标，投标失败！",
					"/invest/detail.html?id=" + model.getId());
		}

		// 投标处理
		model.setUser(user);
		model.setAddIp(Global.getIP());
		ConcurrentUtil.tender(model, borrow);
		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);

		PayDetailReq payDetailReq = new PayDetailReq();
		payDetailReq.setProjectId(borrow.getId());
		payDetailReq.setProjectName(borrow.getName());
		if (model.getMoney() == 0) {
			payDetailReq.setInvestPrice(model.getMoney() + "");
		} else {
			payDetailReq.setInvestPrice(model.getAccount() + "");
		}
		payDetailReq.setProjectType("ppfund");
		request.setAttribute("data", payDetailReq);
		return "result";
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
