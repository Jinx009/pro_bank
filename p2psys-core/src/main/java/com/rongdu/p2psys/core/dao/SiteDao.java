package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.SiteModel;

/**
 * 栏目Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
public interface SiteDao extends BaseDao<Site> {

	/**
	 * 取得所有栏目列表
	 * 
	 * @return 栏目列表
	 */
	List<Site> list();

	/**
	 * 列表
	 * 
	 * @param pid
	 * @param status
	 * @return
	 */
	List<Site> list(long pid, int status);

	/**
	 * 栏目列表
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	List<Site> siteList(SiteModel model);

}
