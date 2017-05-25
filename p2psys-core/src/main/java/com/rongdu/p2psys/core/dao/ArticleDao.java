package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Article;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;

/**
 * 文章Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
public interface ArticleDao extends BaseDao<Article> {

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
	 * 获取
	 * 
	 * @param siteId
	 * @return
	 */
	Article findBySiteId(long siteId);

	/**
	 * 增加点击量
	 * 
	 * @param id
	 */
	void addClick(long id) throws Exception;

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
	 * 修改文章
	 * 
	 * @param articleNew
	 */
	void articleEdit(Article articleNew);

	/**
	 * 下一篇文章
	 * @param article
	 * @return Article
	 */
    Article findNextArticle(Article article);
    /**
     * 上一篇文章
     * @param article
     * @return Article
     */
    Article findPrevArticle(Article article);

    List<Object> getIds(Site site);

}
