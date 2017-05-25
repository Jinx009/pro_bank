package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Article;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.SiteTree;

/**
 * 文章Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
public interface ArticleService {

	/**
	 * 列表
	 * 
	 * @param nid
	 * @param size
	 * @return
	 */
	List<ArticleModel> listBySize(String nid, int size);

	/**
	 * 分页
	 * 
	 * @param nid
	 * @param page
	 * @return
	 */
	PageDataList<ArticleModel> list(String nid, int page);
	/**
     * 分页
     * @param model
     * @return
     */
	PageDataList<ArticleModel> list(ArticleModel model);

	/**
	 * 根据siteId获取文章内容
	 * 
	 * @param siteId 栏目ID
	 * @return 文章内容
	 */
	Article findBySiteId(long siteId);

	/**
	 * 文章列表
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<ArticleModel> articleList(ArticleModel model, int pageNumber, int pageSize);

	/**
	 * 添加文章
	 */
	void articleAdd(Article article);

	/**
	 * 根据ID查询实体
	 * 
	 * @param id
	 * @return
	 */
	Article find(long id);

	/**
	 * 修改文章
	 * 
	 * @param article
	 */
	void articleEdit(Article article);

	/**
	 * @return
	 */
	SiteTree getSiteTree();
	/**
	 * 修改文章
	 * @param article 文章对象
	 */
	void update(Article article);

	/**
	 * 获取文章列表ID
	 * @return
	 */
    List<Long> getIds(Site site);
}
