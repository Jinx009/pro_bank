package com.rongdu.p2psys.core.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.FinanceArticle;
import com.rongdu.p2psys.core.domain.FinanceSite;

public class FinanceSiteModel extends FinanceSite {

	private static final long serialVersionUID = -3768893630867364454L;

	private List<FinanceArticle> list;
	
	private String searchName;

	public static FinanceSiteModel instance(FinanceSite financeSite) {
		FinanceSiteModel financeSiteModel = new FinanceSiteModel();
		BeanUtils.copyProperties(financeSite, financeSiteModel);
		return financeSiteModel;
	}
	
	public FinanceSite prototype() {
		FinanceSite financeSite = new FinanceSite();
		BeanUtils.copyProperties(this, financeSite);
		return financeSite;
	}

	public List<FinanceArticle> getList() {
		return list;
	}

	public void setList(List<FinanceArticle> list) {
		this.list = list;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
}
