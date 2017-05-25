package com.rongdu.manage.action.account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.account.domain.CompBank;
import com.rongdu.p2psys.account.model.CompBankModel;
import com.rongdu.p2psys.account.service.CompBankService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 对公付款银行卡
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
public class ManageCompBankAction extends BaseAction<CompBankModel> implements
		ModelDriven<CompBankModel> {
	@Resource
	private CompBankService compBankService;

	private CompBankModel model = new CompBankModel();

	public CompBankModel getModel() {

		return model;
	}

	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 对公付款银行管理 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankManager")
	public String compBankManager() throws Exception {

		return "compBankManager";
	}

	/**
	 * 对公付款银行卡列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankList")
	public void compBankList() throws Exception {
		PageDataList<CompBankModel> pageList = compBankService.pageDataList(model);
		int totalPage = pageList.getPage().getTotal();
		data.put("total", totalPage);
		data.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 对公付款银行卡添加 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankAddPage")
	public String compBankAddPage() throws Exception {

		return "compBankAddPage";
	}

	/**
	 * 对公付款银行卡添加
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankAdd")
	public void compBankAdd() throws Exception {
		model.setOperator(getOperator());
		model.setAddTime(new Date());
		model.setAddIp(Global.getIP());
		compBankService.save(model.prototype());
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	/**
	 * 对公付款银行卡编辑 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankEditPage")
	public String compBankEditPage() throws Exception {
		CompBank compBank = compBankService.findById(model.getId());
		request.setAttribute("compBank", compBank);
		return "compBankEditPage";
	}

	/**
	 * 对公付款银行卡编辑
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankEdit")
	public void compBankEdit() throws Exception {
		model.setOperator(getOperator());
		compBankService.update(model);
		printResult(MessageUtil.getMessage("I10002"), true);
	}

	/**
	 * 对公付款银行卡删除
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/compBank/compBankDelete")
	public void compBankDelete() throws Exception {
		compBankService.delete(model.getId());
		printResult(MessageUtil.getMessage("I10003"), true);
	}
}
