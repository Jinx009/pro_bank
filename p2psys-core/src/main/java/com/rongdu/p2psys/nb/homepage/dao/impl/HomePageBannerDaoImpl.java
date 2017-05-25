package com.rongdu.p2psys.nb.homepage.dao.impl;

import java.util.List;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.homepage.dao.HomePageBannerDao;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;

@Repository("homePageBannerDao")
public class HomePageBannerDaoImpl extends BaseDaoImpl<HomePageBanner>
		implements HomePageBannerDao {

	@SuppressWarnings("unchecked")
	public List<HomePageBanner> getByUseStatus(int status)
	{
		String hql = "  from  HomePageBanner  where isEnable=1 and useStatus = "+status+" order by bannerOrder  ";
		
		Query query = em.createQuery(hql);
		
		List<HomePageBanner> list = query.getResultList();
		
		return list;
	}

}
