package com.rongdu.p2psys.core.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.model.SystemConfigModel;

/**
 * 系统设置Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月21日
 */
public interface SystemConfigService {

	/**
	 * @return
	 */
	SystemConfigModel getSystemInfo();

	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<SystemConfigModel> list(SystemConfigModel model);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	SystemConfig find(long id);

	/**
	 * 新增
	 * 
	 * @param sconfig
	 */
	void add(SystemConfig sconfig);

	/**
	 * 更新
	 * 
	 * @param sconfig
	 */
	void update(SystemConfig sconfig);

	/**
	 * 情况系统缓存
	 */
	void clean();

	/**
	 * 根据nid即key值那对应的配置信息
	 */
	SystemConfig findByNid(String nid);
}
