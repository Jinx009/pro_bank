package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.VerifyLogModel;

/**
 * 审核日志
 * 
 * @version 1.0
 * @since 2014-4-14
 */
public interface VerifyLogService {

	/**
	 * 新增
	 * 
	 * @param verifyLog
	 */
	void save(VerifyLog verifyLog);

	/**
	 * 根据类型，数据ID查询
	 * 
	 * @param fid
	 * @param type
	 * @param verifyType
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
