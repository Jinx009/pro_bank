package com.rongdu.p2psys.nb.ppfund.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.ppfund.domain.ExperienceGold;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.domain.User;

public interface ExperienceGoldService {

	PageDataList<ExperienceGoldModel> findExperienceGoldByItem(int pageNumber,
			int pageSize, String searchName);

	ExperienceGoldModel loadExperienceGoldById(Long id);

	void deleteExperienceGoldById(Long id);

	void updateExperienceGold(ExperienceGoldModel model);

	void saveExperienceGold(ExperienceGoldModel model);

	void addExperienceGold(User user);

	List<ExperienceGold> getExperienceGoldByUserId(Long userId);
	
	public boolean getCanExperienceGold(User user);
	
	public void addPpfundExperienceGold(User user,Ppfund ppfund);

	/**
	 * 查找体验金列表
	 * 
	 * @param model
	 * @return PageDataList<ExperienceGoldModel>
	 */
	PageDataList<ExperienceGoldModel> findByModel(ExperienceGoldModel model);

	/**
	 * 查询该用户是否投资了该标
	 * 
	 * @param userId
	 * @return true已投资，false未投资
	 */
	Boolean checkUserIsUseGold(Long userId);

	/**
	 * 获取该用户的体验金记录
	 * 
	 * @param userId
	 * @return ExperienceGoldModel
	 */
	ExperienceGoldModel getEGByUserId(Long userId);

	/**
	 * 获取该用户的体验金额
	 * 
	 * @param userId
	 * @return Double
	 */
	Double getGoldByUserId(Long userId);
}
