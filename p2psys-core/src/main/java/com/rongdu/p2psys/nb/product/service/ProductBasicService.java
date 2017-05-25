package com.rongdu.p2psys.nb.product.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.user.domain.User;

public interface ProductBasicService {

	// 前台

	/**
	 * 根据ID获取产品信息
	 * 
	 * @param id
	 * @return ProductBasic
	 */
	ProductBasic findById(Long id);

	/**
	 * 获取热门产品信息列表
	 * 
	 * @param platform
	 * @return List<ProductBasicModel>
	 */
	List<ProductBasicModel> getRecommendModelList(Long userId, String platform);

	/**
	 * 根据产品标签获取产品信息列表
	 * 
	 * @param flagId
	 * @param platform
	 * @return List<ProductBasicModel>
	 */
	List<ProductBasicModel> getModelListByFlag(Long flagId, String platform);

	/**
	 * 根据产品标签获取PC端产品信息列表
	 * 
	 * @param userId
	 * @param flagId
	 * @return List<ProductBasicModel>
	 */
	List<ProductBasicModel> getProductBasicModelListByFlag(User user,
			Long flagId, String orderField);

	/**
	 * 根据ID获取产品信息
	 * 
	 * @param id
	 * @return ProductBasic
	 */
	ProductBasicModel getModelById(Long id);

	/**
	 * 查询体验标
	 * 
	 * @return List<ProductBasic>
	 */
	List<ProductBasic> getExperienceProductList();

	// 后台

	/**
	 * 分页所有数据
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList
	 */
	PageDataList<ProductBasicModel> getAllListForPaging(ProductBasicModel model);

	/**
	 * 保存产品信息
	 * 
	 * @param productBasic
	 * @return
	 */
	ProductBasic saveProductBasic(ProductBasic productBasic);

	/**
	 * 更新推荐标信息
	 * 
	 * @param productBasic
	 */
	ProductBasic updateProductBasic(ProductBasic productBasic);

	/**
	 * 标隐藏状态更新
	 * 
	 * @param id
	 * @param platform
	 */
	void updateDisplayLogic(Long id, String platform);

	/**
	 * 更改微信端首页推荐
	 */
	void changeRecommend(Long id, String reason);

	// 通用

	/**
	 * 获取产品信息
	 * 
	 * @param typeId
	 * @param relatedId
	 * @return ProductBasic
	 */
	ProductBasic getProductBasicInfo(Long typeId, Long relatedId);

	/**
	 * 根据flagId 和 relatedId
	 * 
	 * @param typeId
	 * @param relatedId
	 * @return ProductBasic
	 */
	ProductBasic getProductBasicInfoByFlagId(Long flagId, Long relatedId);

	/**
	 * 校验投资是否成功
	 * 
	 * @param productBasicId
	 * @param user
	 * @param money
	 * @param subMoney
	 * @return String
	 */
	String checkInvest(Long productBasicId, User user, Double money,
			String pwd, Double subMoney);

	/**
	 * 为加息券关联限制产品提供列表
	 * 
	 * @return List<ProductBasic>
	 */
	List<ProductBasic> getAllProductListForCouponCategory();

	// TODO 以下为组合接口（待删）

	/**
	 * 获取组合产品可选清单
	 * 
	 * <p>
	 * 1. 必须是有效的产品
	 * </p>
	 * <p>
	 * 2. 排除体验标、组合表
	 * </p>
	 * 
	 * @param flagId
	 * @param platform
	 * @return List<ProductBasicModel>
	 */
	List<ProductBasicModel> getModelListForProductSet();

	/**
	 * 根据标状态、显示平台获取众筹信息模型
	 * 
	 * @param status
	 * @return List
	 */
	List<ProductBasicModel> getModelCrowdfundingByStatus(int status,
			String platform);

	/**
	 * 根据状态获取组合产品信息模型
	 * 
	 * @return List
	 */
	List<ProductBasicModel> getModelSetByStatus(int status, String platform);

	/**
	 * 根据状态获取组合产品信息模型（分页）
	 * 
	 * @return PageDataList
	 */
	PageDataList<ProductBasicModel> getModelSetPageListByStatus(int status,
			int pageNumber, int pageSize);

	/**
	 * 根据ID获取组合产品信息
	 * 
	 * @param id
	 * @return ProductBasic
	 */
	ProductBasicModel getModelSetById(Long id);

	// TODO 以下为众筹接口（待删）

	/**
	 * 根据对应的众筹类信息获取产品信息
	 * 
	 * @param baseinfo
	 * @return
	 */
	ProductBasic getInfoForCrowdfunding(ProjectBaseinfo baseinfo);
}
