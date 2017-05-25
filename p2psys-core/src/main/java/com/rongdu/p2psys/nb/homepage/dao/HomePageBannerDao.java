package com.rongdu.p2psys.nb.homepage.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;

/**
 * 首页bannerDAO
 */
public interface HomePageBannerDao extends BaseDao<HomePageBanner> {
	
	public List<HomePageBanner> getByUseStatus(int status);
	
}
