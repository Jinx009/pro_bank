package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.model.BorrowConfigModel;

/**
 * 标种配置Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月17日
 */
public interface BorrowConfigService {

	/**
	 * 列表
	 * 
	 * @return List
	 */
	List<BorrowConfig> findAll();

	/**
	 * 分页
	 * 
	 * @return List
	 */
	PageDataList<BorrowConfigModel> list(BorrowConfigModel model);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	BorrowConfig find(long id);

	/**
	 * 新增
	 * 
	 * @param borrowConfig
	 */
	void add(BorrowConfig borrowConfig);

	/**
	 * 更新
	 * 
	 * @param borrowConfig
	 */
	void update(BorrowConfig borrowConfig);

	/**
	 * 列表（不包括流转标）
	 * 
	 * @return
	 */
	List<BorrowConfig> findAllOutFlow();

	/**
	 * 列表（不包括流转标和秒还标）
	 * 
	 * @return List<BorrowConfig>
	 */
	List<BorrowConfig> findAllNotFlowAndSecond();
}
