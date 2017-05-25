package com.rongdu.manage.action.loan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowBespeak;
import com.rongdu.p2psys.borrow.domain.BorrowBespeakPic;
import com.rongdu.p2psys.borrow.model.BorrowBespeakModel;
import com.rongdu.p2psys.borrow.service.BorrowBespeakService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 预约借款
 * @author sj
 * @since 2014-8-20
 *
 */
public class ManageBorrowBespeakAction  extends BaseAction<BorrowBespeak> implements ModelDriven<BorrowBespeak> {
	
	@Resource
	private BorrowBespeakService borrowBespeakService;
	@Resource
	private UserCacheService userCacheService;
	
	@Resource
	private UserService userService;
	
	private BorrowBespeakModel borrowBespeak = new BorrowBespeakModel();
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	@Override
	public BorrowBespeakModel getModel() {
		return borrowBespeak;
	}
	
	/**
	 * 预约借款展示
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowBespeak/borrowBespeakManager")
	public String borrowBespeakManager() throws Exception {
		int status = paramInt("status");
		request.setAttribute("flag", status);
		return "borrowBespeakManager";
	}
	
	/**
	 * 预约借款列表
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowBespeak/borrowBespeakList")
	public void borrowBespeakList() throws Exception {
		PageDataList<BorrowBespeakModel> list = borrowBespeakService.borrowBespeakList(borrowBespeak, paramInt("page"), paramInt("rows"),searchName);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 编辑预约借款页面
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowBespeak/borrowBespeakEditPage")
	public String borrowBespeakEditPage() throws Exception {
		long id = paramLong("id");
		BorrowBespeak borrowBespeak = borrowBespeakService.find(id);
		request.setAttribute("borrowBespeak", borrowBespeak);
		List<BorrowBespeakPic> bbpList = borrowBespeakService.findBorrowBespeakPicByBorrowBespeak(borrowBespeak);
		request.setAttribute("bbpList", bbpList);
		return "borrowBespeakEditPage";
	}
	
	/**
	 * 编辑预约借款
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowBespeak/borrowBespeakEdit")
	public void borrowBespeakEdit() throws Exception {
		long id = paramLong("id");
		BorrowBespeak borrowBespeak = borrowBespeakService.find(id);
		borrowBespeak.setDoTime(new Date());
		borrowBespeak.setStatus(paramInt("status"));
		borrowBespeak.setRemark(paramString("remark"));
		borrowBespeakService.borrowBespeakEdit(borrowBespeak);
		data.put("result", true);
		data.put("msg", "回访处理成功！");
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 查看预约借款页面
	 * @throws Exception
	 */
	@Action("/modules/loan/borrowBespeak/borrowBespeakViewPage")
	public String borrowBespeakViewPage() throws Exception {
		long id = paramLong("id");
		BorrowBespeak borrowBespeak = borrowBespeakService.find(id);
		request.setAttribute("borrowBespeak", borrowBespeak);
		List<BorrowBespeakPic> bbpList = borrowBespeakService.findBorrowBespeakPicByBorrowBespeak(borrowBespeak);
		request.setAttribute("bbpList", bbpList);
		return "borrowBespeakViewPage";
	}
}
