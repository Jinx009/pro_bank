package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.model.SystemConfigModel;

/**
 * 系统设置Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月21日
 */
public interface SystemConfigDao extends BaseDao<SystemConfig> {

	/**
	 * 
	 * @param status
	 * @return
	 */
	List<SystemConfig> findSystemListByStatus(int status);

	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<SystemConfigModel> list(SystemConfigModel model);

	SystemConfig findByHql(String hql);
}
