package com.rongdu.p2psys.core.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.ArticleDao;
import com.rongdu.p2psys.core.domain.Article;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;

@Repository("articleDao")
public class ArticleDaoImpl extends BaseDaoImpl<Article> implements ArticleDao {

	@Override
	public List<ArticleModel> listBySize(String nid, int size) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("site.nid", nid);
		param.addParam("status", 1);
		param.addParam("isDelete", 0);
		param.addOrder(OrderType.DESC, "isTop");
		param.addOrder(OrderType.DESC, "isRecommend");
		param.addOrder(OrderType.ASC, "sort");
		param.addOrder(OrderType.DESC, "id");
		List<Article> list = super.findByCriteria(param, 0, size);
		List<ArticleModel> mlist = new ArrayList<ArticleModel>();
		for (Article articleNew : list) {
			ArticleModel anm = ArticleModel.instance(articleNew);
			anm.setNid(articleNew.getSite().getNid());
			anm.setPicPath(Global.getValue("adminurl") + articleNew.getPicPath());
			anm.setSite(null);
			mlist.add(anm);
		}
		return mlist;
	}

	@Override
	public PageDataList<ArticleModel> list(String nid, int page) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("site.nid", nid);
		param.addParam("status", 1);
		param.addOrder(OrderType.DESC, "isTop");
		param.addOrder(OrderType.DESC, "isRecommend");
		param.addOrder(OrderType.ASC, "sort");
		param.addOrder(OrderType.DESC, "id");
		param.addPage(page);
		PageDataList<Article> pageDataList = super.findPageList(param);
		PageDataList<ArticleModel> pageDataList_ = new PageDataList<ArticleModel>();
		List<ArticleModel> list = new ArrayList<ArticleModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Article an = (Article) pageDataList.getList().get(i);
				ArticleModel anm = ArticleModel.instance(an);
				anm.setNid(an.getSite().getNid());
				list.add(anm);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public Article findBySiteId(long siteId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("site.id", siteId);
		param.addParam("status", 1);
		param.addOrder(OrderType.DESC, "isTop");
		param.addOrder(OrderType.DESC, "isRecommend");
		param.addOrder(OrderType.ASC, "sort");
		return super.findByCriteriaForUnique(param);
	}

	@Override
	public void addClick(long id) throws Exception {
		Article article = super.find(id);
		article.setClicks(article.getClicks() + 1);
		super.update(article);
	}

	@Override
	public PageDataList<ArticleModel> articleList(ArticleModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("title", Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("site.name", Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2);
		}else{ //精确查询条件
			if (model.getStatus() != -1) {
				param.addParam("status", model.getStatus());
			}
			if (!StringUtil.isBlank(model.getTitle())) {
				param.addParam("title", Operators.EQ, model.getTitle());
			}
			if(!StringUtil.isBlank(model.getArticleType())){
				param.addParam("site.name", Operators.EQ, model.getArticleType());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		param.addParam("isDelete", 0);
		PageDataList<Article> pageDataList = super.findPageList(param);
		PageDataList<ArticleModel> pageDataList_ = new PageDataList<ArticleModel>();
		List<ArticleModel> list = new ArrayList<ArticleModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Article articleNew = (Article) pageDataList.getList().get(i);
				ArticleModel articleNewModel = ArticleModel.instance(articleNew);
				articleNewModel.setName(articleNew.getSite().getName());
				list.add(articleNewModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public void articleEdit(Article articleNew) {
		String jpql = "Update Article set site.id = :siteId, title = :title,status = :status,sort = :sort,"
				+ "isRecommend = :isRecommend, isTop = :isTop,introduction = :introduction,content = :content where id = :id";
		String[] names = new String[] { "siteId", "title", "status", "sort", "isRecommend", "isTop", "introduction",
				"content", "id" };
		Object[] values = new Object[] { articleNew.getSite().getId(), articleNew.getTitle(), articleNew.getStatus(),
				articleNew.getSort(), articleNew.getIsRecommend(), articleNew.getIsTop(), articleNew.getIntroduction(),
				articleNew.getContent(), articleNew.getId() };
		super.updateByJpql(jpql, names, values);

	}

    @Override
    public Article findNextArticle(Article article) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("site", article.getSite());
        param.addParam("status", 1);
        param.addParam("id", Operators.GT, article.getId());
        param.addPage(1, 1);
        return findByCriteriaForUnique(param);
    }

    @Override
    public Article findPrevArticle(Article article) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("site", article.getSite());
        param.addParam("status", 1);
        param.addParam("id", Operators.LT, article.getId());
        param.addPage(1, 1);
        return findByCriteriaForUnique(param);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getIds(Site site) {
        return em.createNativeQuery("select id from rd_article where site_id=? and status=1 and is_delete=0 order by is_top desc, "
                + "is_recommend desc, sort asc, id desc")
                .setParameter(1, site.getId())
                .getResultList();
    }

}
