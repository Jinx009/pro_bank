package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.ArticleDao;
import com.rongdu.p2psys.core.dao.SiteDao;
import com.rongdu.p2psys.core.domain.Article;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.SiteModel;
import com.rongdu.p2psys.core.model.SiteTree;
import com.rongdu.p2psys.core.model.Tree;
import com.rongdu.p2psys.core.service.SiteService;

@Service("siteService")
@Transactional
public class SiteServiceImpl implements SiteService {
	@Resource
	private SiteDao siteNewDao;
	@Resource
	private ArticleDao articleNewDao;

	@SuppressWarnings("rawtypes")
	@Override
	public SiteTree initSiteTree(long pid, int status) {
		SiteTree tree = new SiteTree(null, new ArrayList<Tree>());
		List<Site> list = siteNewDao.list(pid, status);
		for (Site site : list) {
			SiteTree tree_ = new SiteTree(null, site);
			if (site.getLeaf() == 0) {
				tree_.addChild(initSiteTree(site.getId(), status));
			}
			tree.addChild(tree_);
		}
		return tree;
	}

	@Override
	public Site find(long id) {
		return siteNewDao.find(id);
	}

	@Override
	public Site findByNid(String nid) {
		return siteNewDao.findObjByProperty("nid", nid);
	}

	@Override
	public List<Site> parentList(Site site, List<Site> list) {

		if (site != null) {
			list.add(site);
			if (site.getLevel() > 1) {
				parentList(siteNewDao.find(site.getPid()), list);
			}
			Collections.sort(list, new Comparator<Site>() {
				@Override
				public int compare(Site o1, Site o2) {
					if (o1.getId() > o2.getId()) {
						return 1;
					}
					return 0;
				}
			});
		}
		return list;
	}

	@Override
	public Map<String, Object> subListMap(Site site, ArticleModel model) throws Exception {
	    
		Map<String, Object> map = new HashMap<String, Object>();

		if (site.getLeaf() == 1) {
			List<Site> subList = siteNewDao.list(site.getPid(), 1);
			map.put("subList", subList);
		} else {
			List<Site> subList = siteNewDao.list(site.getId(), 1);
			map.put("subList", subList);
		}
		Site leafChild = this.leafChild(site);
		map.put("leafChild", leafChild);
		Article article = null;
		long id = model.getId();
		if (id > 0) {
			article = articleNewDao.find(id);
            Article nextArticle = articleNewDao.find(model.getNextId());
            if (nextArticle != null) {
                map.put("nextArticle", nextArticle);
            }
            Article prevArticle = articleNewDao.find(model.getPrevId());
            if (prevArticle != null) {
                map.put("prevArticle", prevArticle);
            }
		} else if (leafChild.getType() == 1) {
			article = articleNewDao.findBySiteId(leafChild.getId());
		}
		if (article != null) {
			if (article.getStatus() == 0) {
				throw new BussinessException("您访问的内容不存在！");
			}
			ArticleModel articleModel = ArticleModel.instance(article);
			map.put("article", articleModel);
			articleNewDao.addClick(article.getId());
		}
		return map;

	}

	@Override
	public Site leafChild(Site site) {
		if (site.getLeaf() == 1) {
			return site;
		}
		List<Site> list = siteNewDao.list(site.getId(), 1);
		if (list != null && list.size() > 0) {
			return leafChild(list.get(0));
		}
		return site;
	}

	@Override
	public List<Site> list(long pid, int status) {
		return siteNewDao.list(pid, status);
	}

	@Override
	public List<Site> findList() {
		return siteNewDao.findAll();
	}

	@Override
	public List<Site> siteList(SiteModel model) {
		return siteNewDao.siteList(model);
	}

	@Override
	public void siteAdd(Site siteNew) {
		siteNewDao.save(siteNew);
	}

	@Override
	public void siteEdit(Site siteNew) {
		siteNewDao.update(siteNew);
	}

	@Override
	public List<Site> findByNid(long id) {
		QueryParam param = QueryParam.getInstance().addParam("pid", id);
		return siteNewDao.findByCriteria(param);
	}
}
