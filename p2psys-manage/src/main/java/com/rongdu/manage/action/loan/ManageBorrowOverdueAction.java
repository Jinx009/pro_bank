package com.rongdu.manage.action.loan;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowOverdue;
import com.rongdu.p2psys.borrow.service.BorrowOverdueService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 逾期垫付
 * @author zf
 * @version 2.0
 * @since 2014年8月6日
 */
@SuppressWarnings("rawtypes")
public class ManageBorrowOverdueAction extends BaseAction implements ModelDriven<BorrowOverdue> {

    private BorrowOverdue model = new BorrowOverdue();

	public BorrowOverdue getModel() {
		return model;
	}

	
    @Resource
	private BorrowOverdueService borrowOverdueService;

	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 逾期垫付展示
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/modules/loan/borrowOverdue/borrowOverdueManager")
	public String borrowOverdueManager() throws Exception {

		return "borrowOverdueManager";
	}

	/**
	 * 逾期垫付列表
	 * @throws Exception if has error
	 */
	@Action("/modules/loan/borrowOverdue/borrowOverdueList")
	public void borrowOverdueList() throws Exception {
		PageDataList<BorrowOverdue> list = borrowOverdueService.list(model, page, startTime, endTime);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	private int page;
    private Date startTime;
    private Date endTime;

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
