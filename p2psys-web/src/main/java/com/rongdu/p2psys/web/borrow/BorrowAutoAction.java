package com.rongdu.p2psys.web.borrow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowAutoModel;
import com.rongdu.p2psys.borrow.service.BorrowAutoService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;

/**
 * 自动投标
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月14日
 */
public class BorrowAutoAction extends BaseAction<BorrowAutoModel> implements ModelDriven<BorrowAutoModel> {

	@Resource
	private BorrowAutoService borrowAutoService;
	@Resource
	private AccountService accountService;

	private User user;

	/**
	 * 我的自动投标设置
	 * 
	 * @return
	 */
	@Action("/member/auto/detail")
	public String detail() throws Exception {
		user = getSessionUser();
		Account account = accountService.findByUser(user.getUserId());
		BorrowAuto auto = borrowAutoService.init(user);
		request.setAttribute("auto", auto);
		request.setAttribute("account", account);
		List<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<= 36; i++) {
			list.add(i);
		}
		request.setAttribute("list", list);
		return "detail";
	}

	/**
	 * 修改自动投标设置
	 * 
	 * @return
	 */
	@Action(value = "/member/auto/modify", 
			results = {@Result(name = "pnrBorrowAuto", type = "ftl", location = "/tpp/chinapnr/pnrBorrowAuto.html")})
	public String modify() throws Exception {
		BorrowAuto auto = model.prototype();
		auto.setUser(getSessionUser());
		auto.setEnable(false);
		borrowAutoService.update(auto);
		String name = Global.getValue("cooperation_interface");
	    TPPWay way = TPPFactory.getTPPWay(null, auto.getUser(), null, auto.getId() + "", null);
        Object borrowAuto = way.doBorrowAuto(auto);
        request.setAttribute(name, borrowAuto);
        return name + "BorrowAuto";
	}

	/**
	 * 修改自动投标状态
	 * 
	 * @return
	 */
	@Action("/member/auto/modifyStatus")
	public void modifyStatus() throws Exception {
		Account account = accountService.findByUser(getSessionUserId());
		BorrowAuto auto = borrowAutoService.findByUser(getSessionUser());
		if (model.getEnable()) {
			// 开启自动投标不能小于最小可用余额
			double minUseMoney = Global.getDouble("auto_tender_min_usemoney");
			if (account.getUseMoney() < minUseMoney) {
				throw new BorrowException("账户可用余额不足，先充值才能体验自动投资", BussinessException.TYPE_JSON);
			}
		} else {
			auto.setEnable(false);
		}
		borrowAutoService.update(auto);
		printWebSuccess();
	}

}
