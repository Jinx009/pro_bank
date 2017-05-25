package com.rongdu.manage.action.column;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Article;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.SiteModel;
import com.rongdu.p2psys.core.model.TreeModel;
import com.rongdu.p2psys.core.service.ArticleService;
import com.rongdu.p2psys.core.service.SiteService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 文章管理
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class ManageArticleAction extends BaseAction<ArticleModel> implements ModelDriven<ArticleModel> {

	@Resource
	private ArticleService articleService;
	@Resource
	private SiteService siteService;

    private Map<String, Object> data;

	/**
	 * 文章列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleManager")
	public String articleManager() throws Exception {
		//所有栏目
		SiteModel siteModel = new SiteModel();
		siteModel.setStatus(1);
		List<Site> sites = siteService.siteList(siteModel);
		request.setAttribute("sites", sites);
		return "articleManager";
	}

	/**
	 * 文章列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleList")
	public void articleList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String status = request.getParameter("status");
		if (status == null) {
		    model.setStatus((byte)-1);
		}
		PageDataList<ArticleModel> articleList = articleService.articleList(model, pageNumber, pageSize);
		data.put("total", articleList.getPage().getTotal()); // 总行数
		data.put("rows", articleList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加文章页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleAddPage")
	public String articleAddPage() throws Exception {
		List<Site> list = siteService.findList();
		request.setAttribute("list", list);
		return "articleAddPage";
	}

	/**
	 * 添加文章
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleAdd")
	public void articleAdd() throws Exception {
		data = new HashMap<String, Object>();
		Article articleNew = model.prototype();
		articleNew.setAddTime(new Date());
		articleNew.setAddIp(Global.getIP());
		articleNew.setClicks(0);
		articleNew.setSite(new Site(Long.valueOf(model.getSiteId())));
		String picPath = imgUpload();
		if (picPath != null) {
	        articleNew.setPicPath(picPath);
		}
		articleService.articleAdd(articleNew);
		data.put("result", true);
		data.put("msg", "保存成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改文章页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleEditPage")
	public String articleEditPage() throws Exception {
		long id = paramLong("id");
		Article articleNew = articleService.find(id);
		Site siteNew = siteService.find(articleNew.getSite().getId());
		request.setAttribute("siteNew", siteNew);
		request.setAttribute("articleNew", articleNew);
		return "articleEditPage";
	}

	/**
	 * 修改文章
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleEdit")
	public void articleEdit() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		Article articleNew = model.prototype();
		articleNew.setSite(new Site(Long.valueOf(model.getSiteId())));
		articleNew.setId(id);
		String picPath = imgUpload();
        if (picPath != null) {
            articleNew.setPicPath(picPath);
        }
		articleService.update(articleNew);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 栏目tree列表展示
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleTreeList")
	public void articleTreePage() throws Exception {
		List<Site> siteList = siteService.findByNid(0);
		List<TreeModel> treeList = new ArrayList<TreeModel>();
		for (Site siteNew : siteList) {
			TreeModel tree = new TreeModel();
			tree.setId(siteNew.getId());
			tree.setText(siteNew.getName());
			tree.setChildren(this.getChild(siteNew.getId()));
			treeList.add(tree);
		}
		response.setContentType("text/json;charset=GBK");
		String jsonStr = "" + JSONArray.toJSONString(treeList);
		response.getWriter().write(jsonStr);
	}

	private List<TreeModel> getChild(long nid) {
		List<Site> siteList = siteService.findByNid(nid);
		List<TreeModel> childList = new ArrayList<TreeModel>();
		for (Site siteNew : siteList) {
			TreeModel tree = new TreeModel();
			tree.setId(siteNew.getId());
			tree.setText(siteNew.getName());
			tree.setChildren(this.getChild(siteNew.getId()));
			childList.add(tree);
		}
		return childList;
	}
	
	/**
	 * 删除文章
	 * @throws Exception
	 */
	@Action(value = "/modules/column/article/articleDelete")
	public void articleDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		Article article = articleService.find(id);
		article.setIsDelete((byte) 1);
		articleService.update(article);
		data.put("result", true);
		data.put("msg", "删除文章成功！");
		printJson(getStringOfJpaObj(data));
	}
}
