package com.rongdu.manage.action.account;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.PayOfflinebank;
import com.rongdu.p2psys.account.service.PayOfflinebankService;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class ManagePayOfflinebankAction extends BaseAction implements ModelDriven<PayOfflinebank> {

	@Resource
	private PayOfflinebankService payOfflinebankService;

	private PayOfflinebank payOfflinebank = new PayOfflinebank();

	private Map<String, Object> data;

	public PayOfflinebank getModel() {
		return payOfflinebank;
	}

	/**
	 * 线下充值银行账户管理页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/recharge/offlinebank/payOfflinebankManager")
	public String payOfflinebankManager() throws Exception {
		return "payOfflinebankManager";
	}

	/**
	 * 线下充值银行账户管理列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/recharge/offlinebank/payOfflinebankList")
	public void payOfflinebankList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		PageDataList<PayOfflinebank> payOfflinebankList = payOfflinebankService.payOfflinebankList(payOfflinebank,
				pageNumber, pageSize, searchName);
		data.put("total", payOfflinebankList.getPage().getTotal()); // 总行数
		data.put("rows", payOfflinebankList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/recharge/offlinebank/offlinebankAddPage")
	public String offlinebankAddPage() throws Exception {
		return "offlinebankAddPage";
	}

	/**
	 * 添加
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/recharge/offlinebank/offlinebankAdd")
	public void offlinebankAdd() throws Exception {
		data = new HashMap<String, Object>();
		payOfflinebankService.offlinebankAdd(payOfflinebank);
		data.put("result", true);
		data.put("msg", "添加线下充值银行账户成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/recharge/offlinebank/offlinebankEditPage")
	public String offlinebankEditPage() throws Exception {
		long id = paramLong("id");
		PayOfflinebank lb = payOfflinebankService.find(id);
		request.setAttribute("lb", lb);
		return "offlinebankEditPage";
	}

	/**
	 * 编辑
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/recharge/offlinebank/offlinebankEdit")
	public void offlinebankEdit() throws Exception {
		data = new HashMap<String, Object>();
		payOfflinebankService.offlinebankEdit(payOfflinebank);
		data.put("result", true);
		data.put("msg", "修改线下充值银行账户成功！");
		printJson(getStringOfJpaObj(data));
	}

}
