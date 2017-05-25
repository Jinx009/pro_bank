package com.rongdu.p2psys.nb.homepage.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;
import com.rongdu.p2psys.nb.homepage.model.HomePageBannerModel;

public interface HomePageBannerService {

	/**
	 * 所有产品标签列表(分页)
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList<ProductTypeFlagModel>
	 */
	PageDataList<HomePageBannerModel> getModelPageList(int pageNumber,
			int pageSize, Integer status);

	/**
	 * 保存产品标签
	 * 
	 * @param flagModel
	 * @return ProductTypeFlag
	 */
	HomePageBanner saveHomePageBanner(HomePageBannerModel flagModel);

	/**
	 * 修改产品标签
	 * 
	 * @param flagModel
	 * @return ProductTypeFlag
	 */
	HomePageBanner updateHomePageBanner(HomePageBannerModel flagModel);

	/**
	 * 根据ID获取产品标签
	 * 
	 * @param id
	 * @return ProductTypeFlag
	 */
	HomePageBanner findById(Long id);

	/**
	 * 删除产品标签
	 * 
	 * @param id
	 */
	void deleteHomePageBanner(Long id);

	/**
	 * 获取有效的产品标签
	 * 
	 * @return
	 */
	List<HomePageBanner> findAllEnabledHomePageBanner();
	
	public List<HomePageBanner> findAllWechat();
}
