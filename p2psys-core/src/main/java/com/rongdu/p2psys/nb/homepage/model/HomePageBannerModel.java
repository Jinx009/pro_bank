package com.rongdu.p2psys.nb.homepage.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;

public class HomePageBannerModel extends HomePageBanner {

	private static final long serialVersionUID = -9055892284967065526L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	public static HomePageBannerModel instance(HomePageBanner banner) {
		HomePageBannerModel model = new HomePageBannerModel();
		BeanUtils.copyProperties(banner, model);
		return model;
	}

	public static HomePageBanner transHomePageBanner(
			HomePageBannerModel model) {
		HomePageBanner banner = new HomePageBanner();
		BeanUtils.copyProperties(model, banner);
		return banner;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
