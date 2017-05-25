package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.FinanceArticle;

public class FinanceArticleModel extends FinanceArticle {

	private static final long serialVersionUID = 7107866382778779872L;

	/**
	 * 所属栏目标题
	 */
	private String financeSiteTitle;
	
	private String autorName;
	
	private String searchName;
	
	private String articeName;
	
	private long articeId;
	
	public static FinanceArticleModel instance(FinanceArticle financeArticle) {
		FinanceArticleModel financeArticleModel = new FinanceArticleModel();
		BeanUtils.copyProperties(financeArticle, financeArticleModel);
		return financeArticleModel;
	}
	
	public FinanceArticle prototype() {
		FinanceArticle financeArticle = new FinanceArticle();
		BeanUtils.copyProperties(this, financeArticle);
		return financeArticle;
	}

	public String getFinanceSiteTitle() {
		return financeSiteTitle;
	}

	public void setFinanceSiteTitle(String financeSiteTitle) {
		this.financeSiteTitle = financeSiteTitle;
	}

	public String getAutorName() {
		return autorName;
	}

	public void setAutorName(String autorName) {
		this.autorName = autorName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getArticeName() {
		return articeName;
	}

	public void setArticeName(String articeName) {
		this.articeName = articeName;
	}

	public long getArticeId() {
		return articeId;
	}

	public void setArticeId(long articeId) {
		this.articeId = articeId;
	}

}
