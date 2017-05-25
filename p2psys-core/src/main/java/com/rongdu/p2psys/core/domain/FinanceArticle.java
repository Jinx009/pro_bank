package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
* 理财商学院文章
* @author sj
* @since 2001年10月1日20:33:41
*
*/
@Entity
@Table(name = "rd_finance_article")
public class FinanceArticle implements Serializable {

	private static final long serialVersionUID = 480072572963055664L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 理财商学院栏目
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "finance_site_id")
	private FinanceSite financeSite;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 状态，0：隐藏，1：显示
	 */
	private int status;
	
	/**
	 * 理财商学院栏目内容
	 */
	private String content;
	
	/**
	 * 理财商学院栏目图片
	 */
	private String picPath;
	
	/**
	 * 文章作者
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "finance_article_expert_id")
	private FinanceArticleExpert financeArticleExpert;
	
	/**
	 * 是否删除
	 */
	private byte isDelete;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public FinanceSite getFinanceSite() {
		return financeSite;
	}
	
	public void setFinanceSite(FinanceSite financeSite) {
		this.financeSite = financeSite;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public FinanceArticleExpert getFinanceArticleExpert() {
		return financeArticleExpert;
	}

	public void setFinanceArticleExpert(FinanceArticleExpert financeArticleExpert) {
		this.financeArticleExpert = financeArticleExpert;
	}

	public String getPicPath() {
		return picPath;
	}
	
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	public byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(byte isDelete) {
		this.isDelete = isDelete;
	}

	public Date getAddTime() {
		return addTime;
	}
	
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	public String getAddIp() {
		return addIp;
	}
	
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
}
