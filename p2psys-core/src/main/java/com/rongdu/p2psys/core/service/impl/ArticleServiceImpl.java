package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.p2psys.core.dao.ArticleDao;
import com.rongdu.p2psys.core.dao.SiteDao;
import com.rongdu.p2psys.core.domain.Article;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.SiteTree;
import com.rongdu.p2psys.core.model.Tree;
import com.rongdu.p2psys.core.service.ArticleService;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Resource
	private ArticleDao articleDao;

	@Resource
	private SiteDao siteDao;

	@Override
	public List<ArticleModel> listBySize(String nid, int size) {
		return articleDao.listBySize(nid, size);
	}

	@Override
	public PageDataList<ArticleModel> list(String nid, int page) {
		return articleDao.list(nid, page);
	}
	@Override
	public PageDataList<ArticleModel> list(ArticleModel model) {
	    QueryParam param = QueryParam.getInstance();
        param.addParam("site.nid", model.getNid());
        param.addParam("status", 1);
        param.addParam("isDelete", 0);
        param.addOrder(OrderType.DESC, "isTop");
        param.addOrder(OrderType.DESC, "isRecommend");
        param.addOrder(OrderType.ASC, "sort");
        param.addOrder(OrderType.DESC, "id");
        param.addPage(model.getPage(), model.getSize());
        PageDataList<Article> pageDataList = articleDao.findPageList(param);
        PageDataList<ArticleModel> pageDataList_ = new PageDataList<ArticleModel>();
        List<ArticleModel> list = new ArrayList<ArticleModel>();
        List<Article> articles =  pageDataList.getList();
        pageDataList_.setPage(pageDataList.getPage());
        if (articles.size() > 0) {
            for (int i = 0; i < articles.size(); i++) {
                Article an = articles.get(i);
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
		return null;
	}

	@Override
	public PageDataList<ArticleModel> articleList(ArticleModel model, int pageNumber, int pageSize) {
		return articleDao.articleList(model, pageNumber, pageSize);
	}

	@Override
	public void articleAdd(Article article) {
		articleDao.save(article);
	}

	@Override
	public Article find(long id) {
		return articleDao.find(id);
	}

	@Override
	public void articleEdit(Article article) {
		articleDao.articleEdit(article);
	}

	@SuppressWarnings("rawtypes")
    @Override
	public SiteTree getSiteTree() {
		List<Site> list = siteDao.list();
		SiteTree tree = new SiteTree(null, new ArrayList<Tree>());
		for (Site s : list) {
			SiteTree secTree = new SiteTree(s, new ArrayList<Tree>());
			List<Site> sublist = siteDao.list(s.getId(), s.getStatus());
			for (Site ss : sublist) {
				SiteTree subTree = new SiteTree(ss, null);
				secTree.addChild(subTree);
			}
			tree.addChild(secTree);
		}
		return tree;
	}

	@Override
	public void update(Article article) {
		articleDao.merge(article);
	}

    @Override
    public List<Long> getIds(Site site) {
        List<Long> longs = new ArrayList<Long>();
        List<Object> list = articleDao.getIds(site);
        for (Object o : list) {
            longs.add((long)((Integer)o));
        }
        return longs;
    }

}
