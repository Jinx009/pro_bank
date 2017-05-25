package com.rongdu.p2psys.borrow.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.model.BorrowConfigModel;

/**
 * 标种配置Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月17日
 */
public interface BorrowConfigDao extends BaseDao<BorrowConfig> {

	/**
	 * 分页
	 * 
	 * @return List
	 */
	PageDataList<BorrowConfig> list(BorrowConfigModel model);

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
