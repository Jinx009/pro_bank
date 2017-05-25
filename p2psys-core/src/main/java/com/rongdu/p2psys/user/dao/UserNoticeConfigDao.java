package com.rongdu.p2psys.user.dao;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;

/**
 *  
 * 户接收通知设置
 *  
 * @author：sj
 * @version 1.0
 * @since 2014年7月14日
 */
public interface UserNoticeConfigDao extends BaseDao<UserNoticeConfig> {
	/**
	 * 
	 * @param userId
	 * @param noticeTypeNid
	 * @return
	 */
	UserNoticeConfig getUNConfig(long userId, String noticeTypeNid);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	List<UserNoticeConfig> getAllUNConfigs(long userId);

	/**
	 * 
	 * @param uncList
	 * @param userId
	 */
	void updateUNConfigs(ArrayList<UserNoticeConfig> uncList, long userId);

}
