package com.rongdu.p2psys.web.borrow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;

/**
 * 我的投资
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月17日
 */
public class MyInvestAction extends BaseAction implements ModelDriven<BorrowTenderModel> {

	private BorrowTenderModel model = new BorrowTenderModel();

	@Override
	public BorrowTenderModel getModel() {
		return model;
	}

	@Resource
	private BorrowCollectionService borrowCollectionService;
	@Resource
	private BorrowTenderService tenderService;

	private User user;
	private Map<String, Object> data;

	/**
	 * 我的投资
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/invest/mine")
	public String mine() throws Exception {

		return "mine";
	}

	/**
	 * 我的投资列表 ajax数据接口 status
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/invest/mineList")
	public void mineList() throws Exception {
		user = getSessionUser();
		model.setUser(user);
		PageDataList<BorrowTenderModel> pageDataList = tenderService.list(model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 收款明细
	 * 
	 * @return
	 */
	@Action("/member/invest/collection")
	public String collection() throws Exception {

		return "collection";
	}

	/**
	 * 我的待收列表 ajax数据接口 status=0待收 1已收 start_time end_time
	 * 
	 * @return
	 */
	@Action("/member/invest/collectionList")
	public void collectionList() throws Exception {
		user = getSessionUser();
		BorrowCollectionModel m = (BorrowCollectionModel) paramModel(BorrowCollectionModel.class);
		int status = paramInt("status");
		m.setStatus(status);
		m.setUser(user);
		m.setPage(model.getPage());
		PageDataList<BorrowCollectionModel> pageDataList = borrowCollectionService.list(m);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户投资的借款标
	 * @return
	 */
	@Action("/member/invest/myInvest")
	public String myInvest() throws Exception {
		return "myInvest";
	}
	
	/**
	 * 用户投资的借款标列表
	 * @return
	 */
	@Action("/member/invest/myInvestList")
	public void myInvestList() throws Exception {
		user = getSessionUser();
		model.setUser(user);
		PageDataList<BorrowModel> pageDataList = tenderService.getBorrowlist(model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 回款计划页面
	 * @return
	 */
	@Action("/member/invest/repayPlanPage")
	public String repayPlanPage() throws Exception {
		return "repayPlanPage";
	}
	/**
	 * 根据标ID获取借款标还款计划
	 * @return
	 */
	@Action("/member/invest/repayPlan")
	public void repayPlan() throws Exception {
		model.setUser(getSessionUser());
		PageDataList<BorrowCollectionModel> list = borrowCollectionService.getCurrentRepayPlanByModel(model);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	/**
	 * 借款标回款计划
	 * @return
	 */
	@Action("/member/invest/allRepayPlan")
	public void allRepayPlan() throws Exception {
		model.setUser(getSessionUser());
		PageDataList<BorrowCollectionModel> list = borrowCollectionService.getRepayPlanByModel(model);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	/** 
	 * 查找当前用户投资记录
	 * @return
	 */
	@Action("/member/invest/tenderList")
	public void tenderList() throws Exception {
		model.setUser(getSessionUser());
		model.setSize(5);
		PageDataList<BorrowTenderModel> pageDataList = tenderService.getTenderRecordlist(model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
