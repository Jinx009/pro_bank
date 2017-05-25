package com.rongdu.p2psys.user.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserInviteModel;

/**
 * 用户邀请Service
 * 
 * @author zf
 * @version 2.0
 * @since 2014年10月24日
 */
public interface UserInviteService {

	/**
	 * 根据邀请人查找被邀请人列表
	 * @param user
	 * @param page
	 * @return
	 */
	PageDataList<UserInviteModel> findByInviteUser(User user, int page);

	/**
	 * 根据model查找用户邀请列表
	 * @param model
	 * @return
	 */
	PageDataList<UserInviteModel> findByModel(UserInviteModel model);

	
}
