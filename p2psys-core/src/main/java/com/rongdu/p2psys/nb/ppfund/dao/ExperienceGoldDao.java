package com.rongdu.p2psys.nb.ppfund.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.ppfund.domain.ExperienceGold;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.user.domain.User;

public interface ExperienceGoldDao extends BaseDao<ExperienceGold> {

	PageDataList<ExperienceGoldModel> findExperienceGoldByItem(int pageNumber,int pageSize, String searchName);

	ExperienceGold loadExperienceGoldById(long id);

	void deleteExperienceGoldById(long id);

	void updateExperienceGold(ExperienceGold eg);

	List<ExperienceGold> list(long userId);
	
	public boolean getCanExperienceGold(User user);

	/**
	 * 查询该用户是否有体验金
	 * 
	 * @param userId
	 * @return Boolean
	 */
	Boolean checkUserIsUseGold(long userId);

	/**
	 * 获取该用户的体验金记录
	 * 
	 * @param userId
	 * @return
	 */
	ExperienceGold getEGByUserId(long userId);
}
