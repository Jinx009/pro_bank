package com.rongdu.p2psys.core.service;

import java.util.List;
import java.util.Map;

import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.SiteModel;
import com.rongdu.p2psys.core.model.SiteTree;

/**
 * 栏目Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
public interface SiteService {

	/**
	 * 初始化栏目树
	 * 
	 * @return
	 */
	SiteTree initSiteTree(long pid, int status);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	Site find(long id);

	/**
	 * 获取
	 * 
	 * @param nid
	 * @return
	 */
	Site findByNid(String nid);

	/**
	 * 获取父栏目集合
	 * 
	 * @param site
	 * @return
	 */
	List<Site> parentList(Site site, List<Site> list);

	/**
	 * 获取子栏目及单页内容Map
	 * 
	 * @param site
	 * @return
	 */
	Map<String, Object> subListMap(Site site, ArticleModel model) throws Exception;

	/**
	 * 获取（第一子栏目）最底级子栏目
	 * 
	 * @param site
	 * @return
	 */
	Site leafChild(Site site);

	/**
	 * 列表
	 * 
	 * @param pid
	 * @param status
	 * @return
	 */
	List<Site> list(long pid, int status);

	/**
	 * 获得所有栏目
	 * 
	 * @return
	 */
	List<Site> findList();

	/**
	 * 栏目列表
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	List<Site> siteList(SiteModel model);

	/**
	 * 添加栏目
	 * 
	 * @param siteNew
	 */
	void siteAdd(Site siteNew);

	/**
	 * 修改栏目
	 * 
	 * @param siteNew
	 */
	void siteEdit(Site siteNew);

	/**
	 * @param id
	 * @return
	 */
	List<Site> findByNid(long nid);

}
