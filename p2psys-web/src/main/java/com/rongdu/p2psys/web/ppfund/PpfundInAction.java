package com.rongdu.p2psys.web.ppfund;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.exception.PpfundException;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * PPfund资金管理产品转入
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月19日
 */
public class PpfundInAction extends BaseAction<PpfundInModel> implements
		ModelDriven<PpfundInModel> {
	@Resource
	private PpfundService ppfundService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private AccountService accountService;
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private AccountBankService accountBankService;

	private Map<String, Object> data;

	private User user;

	/**
	 * 转入
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "/ppfund/in", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") }, results = { @Result(name = "result", type = "ftl", location = "/tpp/result.html") })
	public String ppfundIn() throws Exception {
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		Ppfund ppfund = ppfundService.getPpfundById(model.getId());
		ProductType pt = ppfund.getProductType();// productTypeService.findById(ppfund.getType());
		try {
			this.checkToken("ppfundTenderToken");
			if (user.getUserCache().getUserType() == 2) {
				throw new BussinessException("借款账户不能投资");
			}

			if (ppfund != null) {
				if (ppfund
						.getProductType()
						.getTypeCategory()
						.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)) {// 判断该标为体验标
					List<AccountBank> bankList = accountBankService.list(user
							.getUserId());
					if (bankList.size() <= 0) {
						throw new BussinessException("对不起！您暂未绑定银行卡！");
					}
				}
			}

			String payPwd = MD5.encode(paramString("payPwd"));
			if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
				userCacheService.doLock(request, user.getUserId(),
						UserCacheModel.PAY_PWD_LOCK);
				throw new PpfundException("支付密码不正确!！", 2);
			}
			model.setUser(user);
			model.checkInModel(ppfund, pt.getTypeName());

			double totalPacketMoney = userRedPacketService
					.getTotalPacketMoneyByIds(model.getIds());
			if (totalPacketMoney > model.getMoney()) {
				throw new PpfundException("您的购买金额需大于红包金额！", 2);
			}
			// 统计用户累计购买金额
			double mostAccountTotal = ppfundInService
					.getMostAccountTotalByUserAndPpfund(user, ppfund);
			if (ppfund.getMostAccountTotal() > 0
					&& ppfund.getMostAccountTotal() < (mostAccountTotal + model
							.getMoney())) {
				double difference = ppfund.getMostAccountTotal()
						- mostAccountTotal;
				if (difference == 0) {
					throw new PpfundException("您对该产品的投资已达到最高限额(￥"
							+ ppfund.getMostAccountTotal() + ")，不能继续购买，购买失败！",
							2);
				}
				throw new PpfundException("购买金额不能大于最高限额，您当前还可以购买" + difference
						+ "元", 2);
			}

			Account account = accountService.findByUser(user.getUserId());
			if (account.getUseMoney() < model.getMoney()) {
				throw new PpfundException("对不起，您的账户余额不足", 2);
			}

		} catch (BussinessException e) {
			throw new BussinessException(e.getMessage(), 2);
		}

		model.setPpfund(ppfund);
		model.setProductType(pt);
		ConcurrentUtil.ppfundTender(model);

		// 获取分类ID（flag_id）
		Long flagId = productBasicService
				.getProductBasicInfo(ppfund.getProductType().getId(),
						ppfund.getId()).getProductTypeFlag().getId();

		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("left_url",
				"/ppfund/detail.html?id=" + this.model.getId() + "&flagId="
						+ flagId); // 成功返回地址
		request.setAttribute("right_url", "/member/main.html"); // 成功返回地址
		request.setAttribute("left_msg", MessageUtil.getMessage("I10008"));
		request.setAttribute("right_msg", MessageUtil.getMessage("I10001"));
		request.setAttribute("r_msg", MessageUtil.getMessage("I10007"));
		Global.RESULT_MAP.put(resultFlag, "success");
		return "result";
	}

	/**
	 * ppfund用户账户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/ppfund/ppfund", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") }, results = { @Result(name = "ppfund", type = "ftl", location = "/ppfund/ppfund.html") })
	public String ppfund() throws Exception {

		return "ppfund";
	}

	/**
	 * 我的购买记录
	 * 
	 * @throws Exception
	 */
	@Action(value = "/ppfund/myInList", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void myInList() throws Exception {
		data = new HashMap<String, Object>();
		User user = getSessionUser();
		model.setUser(user);
		PageDataList<PpfundInModel> pageDataList = ppfundInService
				.pageList(model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改预约转出时间
	 * 
	 * @throws Exception
	 */
	@Action(value = "/ppfund/updateOutTime", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void updateOutTime() throws Exception {
		PpfundIn in = ppfundInService.getById(model.getId());
		Ppfund ppfund = in.getPpfund();
		// 检测该产品是否已关闭
		if (ppfund.getStatus() != 1 && in.getOutTime() != null) {
			printWebResult("产品已关闭，不允许修改预约转出时间", false);
			return;
		}
		// 检测转出时间是否符合
		if (model.getOutTime() != null) {
			if (in.getOutTime() != null) {
				int between_day = DateUtil.daysBetween(new Date(),
						in.getOutTime());
				if (between_day <= 2) {
					printWebResult("转出时间必须提前两天预约", false);
					return;
				}
			}
			// 计算间隔天数
			int days = DateUtil
					.daysBetween(in.getAddTime(), model.getOutTime());
			if (days % ppfund.getCycle() != 0) {
				printWebResult("转出时间选择不正确", false);
				return;
			}
			in.setOutTime(model.getOutTime());
			ppfundInService.ppfundIdEdit(in);
		}
		printWebSuccess();
	}

	/**
	 * ppfund转出
	 * 
	 * @throws Exception
	 */
	@Action(value = "/ppfund/doPpfundOut", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void doPpfundOut() throws Exception {
		PpfundIn in = ppfundInService.getById(model.getId());
		Ppfund ppfund = in.getPpfund();
		// 检测产品是否支持转出
		if (ppfund.getIsFixedTerm() == 1) {
			printWebResult("固定期限产品不支持手动转出", false);
			return;
		}
		PpfundOut out = new PpfundOut();
		out.setPpfund(ppfund);
		out.setPpfundIn(in);
		out.setMoney(in.getMoney());
		ppfundInService.doOut(out);

		printWebResult("转出成功", true);
	}
}
