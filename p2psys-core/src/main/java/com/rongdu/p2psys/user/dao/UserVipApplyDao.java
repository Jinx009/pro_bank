package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.UserVipApply;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserVipApplyModel;

/**
 * 用户vip申请Dao
 * 
 * @author 周学成
 * @version 2.0
 * @since 2014-4-30
 */
public interface UserVipApplyDao extends BaseDao<UserVipApply> {

	/**
	 * 后台管理VIP申请管理列表
	 * @param pageNumber 每页显示条数
	 * @param pageSize 当前页码
	 * @param model 用户认证信息
	 * @return 用户VIP申请model类
	 */
	PageDataList<UserVipApplyModel> vipManagerList(int pageNumber, int pageSize, UserIdentifyModel model);
}
