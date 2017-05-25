package com.rongdu.p2psys.crowdfunding.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;

/**
 * 权益规则
 * @author Jinx
 *
 */
public class ProfitRuleModel extends ProfitRule {

	private static final long serialVersionUID = 2714115469770013630L;
	// 当前页码
	private int page;
	// 行数
	private int rows = Page.ROWS;
	//支持者数
	private int ruleNum;
	// 权益规则
	private List<ProfitRuleModel> ruleList;

	public static ProfitRuleModel instance(ProfitRule profitRule) {
		ProfitRuleModel profitRuleModel = new ProfitRuleModel();
		BeanUtils.copyProperties(profitRule, profitRuleModel);
		return profitRuleModel;
	}

	public ProfitRule prototype() {
		ProfitRule profitRule = new ProfitRule();
		BeanUtils.copyProperties(this, profitRule);
		return profitRule;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public List<ProfitRuleModel> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<ProfitRuleModel> ruleList) {
		this.ruleList = ruleList;
	}

	public int getRuleNum() {
		return ruleNum;
	}

	public void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
	}

}
