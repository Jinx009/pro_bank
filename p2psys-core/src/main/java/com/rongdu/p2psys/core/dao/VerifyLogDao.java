package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.VerifyLogModel;

/**
 * 日志审核处理dao
 * 
 * @version 1.0
 * @since 2014-4-14
 */
public interface VerifyLogDao extends BaseDao<VerifyLog> {

	/**
	 * 根据类型，数据ID查询
	 * 
	 * @param fid
	 * @param type
	 * @return
	 */
	VerifyLog findByType(long fid, String type, int verifyType);

	/**
	 * 根据类型，数据ID查询
	 * @param type 类型
	 * @param fid 数据ID
	 * @return
	 */
	List<VerifyLogModel> list(String type, long fid);
}
