package com.rongdu.manage.action.nb.homepage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;
import com.rongdu.p2psys.nb.homepage.model.HomePageBannerModel;
import com.rongdu.p2psys.nb.homepage.service.HomePageBannerService;

public class ManageBannerAction extends BaseAction<HomePageBannerModel>
		implements ModelDriven<HomePageBannerModel> {

	@Resource
	private HomePageBannerService homePageBannerService;

	private Map<String, Object> data = null;

	/**
	 * 首页 banner
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerManager")
	public String bannerManager() throws Exception {
		return "bannerManager";
	}
	
	/**
	 * 首页微信 banner
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerWechat")
	public String bannerWechat() throws Exception {
		return "bannerWechat";
	}

	/**
	 * 首页头图列表PC
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerList")
	public void bannerList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<HomePageBannerModel> list = homePageBannerService.getModelPageList(pageNumber, pageSize,0);
		model.setSize(pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 首页头图列表微信
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerWechatList")
	public void bannerWechatList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		model.setUseStatus(1);
		PageDataList<HomePageBannerModel> list = homePageBannerService.getModelPageList(pageNumber, pageSize,1);
		model.setSize(pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerAddPage")
	public String bannerAddPage() throws Exception {
		request.setAttribute("useStatus",paramString("useStaus"));
		return "bannerAddPage";
	}

	/**
	 * 添加类别 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerAdd")
	public void bannerAdd() throws Exception {
		String imagePath = imgUpload();
		if (!StringUtil.isBlank(imagePath)) {
			model.setBannerPicUrl(imagePath);
		}
		homePageBannerService.saveHomePageBanner(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 查看类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerViewPage")
	public String bannerViewPage() throws Exception {
		HomePageBanner banner = homePageBannerService.findById(paramLong("id"));

		request.setAttribute("banner", banner);
		return "bannerViewPage";
	}

	/**
	 * 编辑类别 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerEditPage")
	public String bannerEditPage() throws Exception {
		HomePageBanner banner = homePageBannerService.findById(paramLong("id"));
		request.setAttribute("useStatus",paramString("useStaus"));
		request.setAttribute("banner", banner);
		return "bannerEditPage";
	}

	/**
	 * 编辑类别 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerEdit")
	public void bannerEdit() throws Exception {
		String imagePath = imgUpload();
		if (!StringUtil.isBlank(imagePath)) {
			HomePageBanner banner = homePageBannerService
					.findById(paramLong("id"));
			model.setBannerPicUrl(imagePath);
			model.setIsEnable(banner.getIsEnable());
		} else {
			HomePageBanner banner = homePageBannerService
					.findById(paramLong("id"));
			model.setBannerPicUrl(banner.getBannerPicUrl());
			model.setIsEnable(banner.getIsEnable());
		}
		homePageBannerService.updateHomePageBanner(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更改类别有效状态
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerEnable")
	public void bannerEnable() throws Exception {
		HomePageBanner banner = homePageBannerService.findById(paramLong("id"));
		Integer enableFlag = banner.getIsEnable();
		if (1 == enableFlag) {
			banner.setIsEnable(0);
		} else {
			banner.setIsEnable(1);
		}

		homePageBannerService.updateHomePageBanner(HomePageBannerModel
				.instance(banner));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	
	/**
	 * 更改头图显示位置
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerUse")
	public void bannerUse() throws Exception {
		HomePageBanner banner = homePageBannerService.findById(paramLong("id"));
		Integer useStatus = banner.getUseStatus();
		if (1 == useStatus) {
			banner.setUseStatus(0);
		} else {
			banner.setUseStatus(1);
		}

		homePageBannerService.updateHomePageBanner(HomePageBannerModel.instance(banner));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	
	/**
	 * 删除类别
	 * 
	 * @throws Exception
	 */
	@Action("/modules/nb/homepage/bannerDelete")
	public void bannerDelete() throws Exception {
		homePageBannerService.deleteHomePageBanner(paramLong("id"));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

}
