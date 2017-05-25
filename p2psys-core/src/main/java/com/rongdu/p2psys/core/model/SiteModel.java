package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.core.domain.Site;

/**
 * 栏目Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
public class SiteModel extends Site {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;

	public static SiteModel instance(Site site, String nid) {
		if (site == null) {
			throw new BussinessException(nid + "栏目不存在！", 1);
		}
		SiteModel siteNewModel = new SiteModel();
		BeanUtils.copyProperties(site, siteNewModel);
		return siteNewModel;
	}

	public Site prototype() {
		Site site = new Site();
		BeanUtils.copyProperties(this, site);
		return site;
	}

	/**
	 * 文章列表 校验栏目类型
	 * 
	 * @return
	 */
	public int validSiteForArticleList() {
		if (getStatus() == 0) {
			throw new BussinessException("栏目不存在！", 1);
		}
//		if (getType() != 2) {
//			throw new BussinessException("栏目类型不是列表！", 1);
//		}
		return -1;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
