package com.rongdu.p2psys.web.borrow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.ips.model.IpsRegisterGuarantor;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 我的担保
 * @author zhangyz
 *
 */
public class MyGuaranteeAction extends BaseAction<BorrowModel> implements ModelDriven<BorrowModel> {
	
	private static Logger logger = Logger.getLogger(MyBorrowAction.class);

	@Resource
	private BorrowService borrowService;
	@Resource
	private UserService userService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private IpsService ipsService;
	private User user;
	private Map<String, Object> data;	
	
	/**
	 * 担保公司用户信息
	 * @throws Exception
	 */
	@Action("/member_guarantee/getGuaranteerInfo")
	public void getGuaranteerInfo() throws Exception {

		data = new HashMap<String, Object>();
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		data.put("email", user.getEmail());
		data.put("realName", user.getRealName());
		// TODO zjj
		// data.put("apiId", user.getApiId());
		// data.put("apiName", user.getApiUsercustId());
		printWebJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 我担保的项目
	 * 
	 * @return
	 */
	@Action("/member_guarantee/getGuaranteeProject")
	public void getGuaranteeProject() throws Exception {
		// 担保公司用户ID
		long userId = getSessionUserId();
		
		data = new HashMap<String, Object>();
		// 正在担保项目个数
		data.put("guaranteeingCount", borrowService.getGuaranteeingCount(userId));
		// 正在担保项目金额
		data.put("guaranteeingAccount", borrowService.getGuaranteeingAccount(userId));
		// 待登记项目个数
		data.put("needGuaranteeRegisteCount", borrowService.getNeedGuaranteeRegisteCount(userId));
		// 待登记项目金额
		data.put("needGuaranteeRegisteAccount", borrowService.getNeedGuaranteeRegisteAccount(userId));
		// 催收项目个数
		data.put("urgeCount", borrowRepaymentService.getUrgeCount(userId));
		// 逾期项目个数
		data.put("overdueCount", borrowService.getOverdueCount(userId));
		
		printWebJson(getStringOfJpaObj(data));
	}
	/**
	 * 担保项目页面
	 * @throws Exception
	 */
	@Action("/member_guarantee/guarantee/guaranteeList")
	public String guaranteeList() throws Exception {
		return "guaranteeList";
	}
	/**
	 * 担保项目数据
	 * @throws Exception
	 */
	@Action("/member_guarantee/guarantee/guaranteeListJSON")
	public void guaranteeListJSON() throws Exception {
		user = getSessionUser();
		data = new HashMap<String, Object>();
		model.setVouchFirm(user);
		PageDataList<BorrowModel> list = borrowService.getGuaranteeingList(model);
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	/**
	 * 催收项目页面
	 * @throws Exception
	 */
	@Action("/member_guarantee/collection/collectionList")
	public String collectionList() throws Exception {
		return "collectionList";
	}
	/**
	 * 催收项目数据
	 * @throws Exception
	 */
	@Action("/member_guarantee/collection/collectionListJSON")
	public void collectionListJSON() throws Exception {
		data = new HashMap<String, Object>();
		model.setVouchFirmId(getSessionUserId());
		PageDataList<BorrowRepaymentModel> list = borrowRepaymentService.getCollectionList(model);
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 获取担保公司待登记标列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_guarantee/getNeedGuaranteeRegisterList")
	public void getNeedGuaranteeRegisterList() throws Exception {
		user = getSessionUser();
		List<BorrowModel> list = borrowService.getNeedGuaranteeRegisteList(user.getUserId());
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 逾期项目页面
	 * @throws Exception
	 */
	@Action("/member_guarantee/overdue/overdueList")
	public String overdueList() throws Exception {
		return "overdueList";
	}	
	
	/**
	 * 获取担保公司逾期的标的信息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_guarantee/overdue/getOverdueGuaranteeList")
	public void getOverdueGuaranteeList() throws Exception {
		model.setVouchFirmId(getSessionUserId());
		PageDataList<BorrowRepaymentModel> list = borrowService.getOverdueGuaranteeList(model);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 已经代偿项目页面
	 * @throws Exception
	 */
	@Action("/member_guarantee/compensatory/compensatoryList")
	public String compensatoryList() throws Exception {
		return "compensatoryList";
	}		
	
	/**
	 * 获取担保公司已经代偿的标的信息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_guarantee/compensatory/getCompensatedList")
	public void getCompensatedList() throws Exception {
		model.setVouchFirmId(getSessionUserId());
		PageDataList<BorrowRepaymentModel> list = borrowService.getCompensatedList(model);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 还款明细页面
	 * @throws Exception
	 */
	@Action("/member_guarantee/repayment/repaymentDetail")
	public String repaymentDetail() throws Exception {
		return "repaymentDetail";
	}		
	
	/**
	 * 获取标的还款明细列表
	 * .
	 * @return
	 * @throws Exception
	 */
	@Action("/member_guarantee/repayment/getRepayMentDetailList")
	public void getRepayMentDetailList() throws Exception {
		BorrowRepaymentModel brm = new BorrowRepaymentModel();
		brm.setBorrowId(model.getBorrowId());
		brm.setStatus(99);
		brm.setPage(model.getPage());
		brm.setSize(model.getSize());
		PageDataList<BorrowRepaymentModel> list = borrowRepaymentService.list(brm,null);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 担保公司登记
	 * @return
	 * @throws Exception
	 */
	@Action(value="/member_guarantee/registerGuarantee", results = { @Result(name = "ipsGuarantee", type = "ftl", location = "/tpp/ipsGuarantee.html")})
	public String registerGuarantee() throws Exception {
		long borrowId = paramLong("borrowId");
		Borrow borrow=borrowService.find(borrowId);
		if(borrow.getStatus()!=11 ){
			throw new BussinessException(MessageUtil.getMessage("E30016"),  2);
		}
		if(!StringUtil.isBlank(borrow.getGuaranteeNo())){
			throw new BussinessException(MessageUtil.getMessage("E30017"), 2);
		}
        IpsRegisterGuarantor irg =ipsService.registerGuarantor(borrow);
        request.setAttribute("ips", irg);
        return "ipsGuarantee";
	}
	
	/**
	 * 担保公司代偿
	 * @throws Exception if has error
	 */
	@Action(value="/member_guarantee/repayment/compensate")
	public void compensate() throws Exception {
		data = new HashMap<String, Object>();
	    boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(!isOpenApi || !(TPPWay.API_CODE == TPPWay.API_CODE_IPS)){
            logger.info("项目第三方资金托管非环迅接口！");
    		data.put("result", false);
    		data.put("msg", MessageUtil.getMessage("I60001"));
    		printWebJson(getStringOfJpaObj(data));
    		return;
        }

		BorrowRepayment repayment = borrowRepaymentService.findById(model.getRepaymentId());
		int currPeriod = borrowRepaymentService.getCurrPeriod(repayment.getBorrow().getId());
		if(currPeriod != repayment.getPeriod()){

    		data.put("result", false);
    		data.put("msg", MessageUtil.getMessage("I10004"));
    		printWebJson(getStringOfJpaObj(data));
    		return;
		}
	    IpsService ipsService = (IpsService) BeanUtil.getBean("ipsService");
	    ipsService.doCompensate(repayment);

		data.put("result", true);
		data.put("msg", MessageUtil.getMessage("I70001"));
		printWebJson(getStringOfJpaObj(data));
	}
}
