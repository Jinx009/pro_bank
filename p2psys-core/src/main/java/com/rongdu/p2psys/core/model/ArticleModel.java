package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.core.domain.Article;

/**
 * 文章Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
public class ArticleModel extends Article {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;
	/** 上一篇ID */
	private long prevId;
	/** 下一篇ID*/
	private long nextId;
	
	/** 每页数据条数 */
    private int size = Page.ROWS;
    
	/** 栏目标识 */
	private String nid;

	/** 名称 */
	private String name;

	/** 栏目ID */
	private String siteId;
	
	/** 名称 */
	private String articleType;
	
	/** 条件查询 */
	private String searchName;

	/**
	 * 
	 * @param article
	 * @return
	 */
	public static ArticleModel instance(Article article) {
		ArticleModel articleModel = new ArticleModel();
		BeanUtils.copyProperties(article, articleModel);
		return articleModel;
	}

	public Article prototype() {
		Article article = new Article();
		BeanUtils.copyProperties(this, article);
		return article;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getPrevId() {
        return prevId;
    }

    public void setPrevId(long prevId) {
        this.prevId = prevId;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
}
