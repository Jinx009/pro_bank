package com.rongdu.p2psys.nb.homepage.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.SystemConfigDao;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.nb.homepage.dao.HomePageBannerDao;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;
import com.rongdu.p2psys.nb.homepage.model.HomePageBannerModel;
import com.rongdu.p2psys.nb.homepage.service.HomePageBannerService;

@Service("homePageBannerService")
public class HomePageBannerServiceImpl implements HomePageBannerService {

	@Resource
	private HomePageBannerDao homePageBannerDao;
	@Resource
	private SystemConfigDao systemConfigDao;

	@Override
	public PageDataList<HomePageBannerModel> getModelPageList(int pageNumber,int pageSize, Integer status) {
		SystemConfig systemConfig = systemConfigDao.findByHql("   from SystemConfig where nid = 'con_adminurl'");
		
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		
		param.addParam("useStatus",status);
		
		param.addOrder(OrderType.ASC, "bannerOrder");
		param.addOrder(OrderType.DESC, "id");
		PageDataList<HomePageBanner> productTypeFlagPageList = homePageBannerDao
				.findPageList(param);

		List<HomePageBannerModel> list = new ArrayList<HomePageBannerModel>();
		for (HomePageBanner productTypeFlag : productTypeFlagPageList
				.getList()) {
			
			
			HomePageBannerModel model = HomePageBannerModel
					.instance(productTypeFlag);
			model.setBannerPicUrl(systemConfig.getValue() + model.getBannerPicUrl());
			list.add(model);
		}

		// 封装成带分页的集合
		PageDataList<HomePageBannerModel> pageList = new PageDataList<HomePageBannerModel>();
		pageList.setPage(productTypeFlagPageList.getPage());
		pageList.setList(list);

		return pageList;
	}

	@Override
	public HomePageBanner saveHomePageBanner(HomePageBannerModel flagModel) {
		return homePageBannerDao.save(HomePageBannerModel
				.transHomePageBanner(flagModel));
	}

	@Override
	public HomePageBanner updateHomePageBanner(HomePageBannerModel flagModel) {
		return homePageBannerDao.update(HomePageBannerModel
				.transHomePageBanner(flagModel));
	}

	@Override
	public HomePageBanner findById(Long id) {
		return homePageBannerDao.find(id);
	}

	@Override
	public void deleteHomePageBanner(Long id) {
		homePageBannerDao.delete(id);
	}

	@Override
	public List<HomePageBanner> findAllEnabledHomePageBanner() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("isEnable", 1);
		param.addParam("useStatus",0);
		param.addOrder(OrderType.ASC, "bannerOrder");
		return homePageBannerDao.findByCriteria(param);
	}

	public List<HomePageBanner> findAllWechat()
	{
		return homePageBannerDao.getByUseStatus(1);
	}

}
