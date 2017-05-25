package com.rongdu.p2psys.nb.vip.dao;



import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthUserModel;

public interface WealthUserDao extends BaseDao<WealthUser>
{
	public PageDataList<WealthUserModel> dataList(WealthUser model,int pageNumber,int pageSize);
	public List<WealthUser> findByWealthUserId(String sql);
	public WealthUser getByWealthUserId(String sql);
	public WealthUser getWealthUser(String sql);
}
