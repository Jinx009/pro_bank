package com.rongdu.p2psys.web.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.SiteModel;
import com.rongdu.p2psys.core.service.ArticleService;
import com.rongdu.p2psys.core.service.SiteService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 文章
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月1日
 */
@SuppressWarnings("rawtypes")
public class ArticleAction extends BaseAction implements ModelDriven<ArticleModel> {

	private ArticleModel model = new ArticleModel();

	@Override
	public ArticleModel getModel() {
		return model;
	}

	@Resource
	private SiteService siteService;
	@Resource
	private ArticleService articleService;

	private Map<String, Object> data;

	/**
	 * 文章列表/详情页
	 * 
	 * @return
	 */
	@Action("/article/detail")
	public String detail() throws Exception {
		Site site = siteService.findByNid(model.getNid());
		List<Long> list = articleService.getIds(site);
		int index = list.indexOf(model.getId());
		if (index != 0 && index != -1) {
			model.setPrevId(list.get(index - 1));
		}
		if (index != list.size()-1 && index != -1) {
			model.setNextId(list.get(index + 1));
		}
		if (site == null) {
			return "notfound";
		}
		List<Site> parentList = new ArrayList<Site>();
		parentList = siteService.parentList(site, parentList);
		Map<String, Object> subListMap = siteService.subListMap(site, model);
		request.setAttribute("site", site);
		request.setAttribute("parentList", parentList);
		request.setAttribute("subListMap", subListMap);
		return "detail";
	}

	/**
	 * 文章列表 Ajax异步获取
	 * 
	 * @throws Exception
	 */
	@Action("/article/list")
	public void list() throws Exception {
		data = new HashMap<String, Object>();
		Site site = siteService.findByNid(model.getNid());
		SiteModel m = SiteModel.instance(site, model.getNid());
		m.validSiteForArticleList();
		model.setNid(site.getNid());
		model.setSize(site.getSize());
		PageDataList<ArticleModel> pageDataList = articleService.list(model);
		data.put("data", pageDataList);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 根据父级栏目获取子级栏目列表
	 * @throws Exception
	 */
	@Action("/article/siteList")
	public void siteList() throws Exception {
		data = new HashMap<String, Object>();
		Site site = siteService.findByNid(model.getNid());
		List<Site> list = siteService.list(site.getId(), 1);
		data.put("siteList", list);
		printWebJson(getStringOfJpaObj(data));
	}
}
