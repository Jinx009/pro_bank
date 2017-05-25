package com.rongdu.p2psys.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserPromotModel;

/**
 *  推广人功能
 * @author sj
 *
 */
public interface UserPromotDao extends BaseDao<UserPromot> {

	/**
	 * 推广人列表数据
	 * @param param
	 * @return
	 */
	public PageDataList<UserPromot> userPromotList(QueryParam param);

	public UserPromot getUserPromotByUserId(User user);

	public boolean hasPromotByCouponCode(String code);
	
	/**
	 * 根据邀请码获取推荐人
	 * 
	 * @param code
	 * @return
	 */
	public UserPromot getUserPromotByCode(String code);
	
	/**
	 * 导出推广人列表数据
	 * @param param
	 * @return
	 */
	public List<UserPromotModel> exportUserPromotList(int status);
	
	public PageDataList<UserPromot> userPromotList(String sql,QueryParam param);
	
}
