package com.rongdu.p2psys.account.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.TppGlodLog;
import com.rongdu.p2psys.account.model.TppGlodLogModel;

/**
 * 平台账户操作记录
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年2月4日
 */
public interface TppGlodLogService {
	/**
	 * 保存记录
	 * @param glodLog
	 */
	void save(TppGlodLog glodLog);
	
	/**
	 * 修改记录
	 * @param glodLog
	 */
	void update(TppGlodLog glodLog);
	
	/**
	 * 根据订单号查找平台操作记录
	 * @param ordId
	 * @return
	 */
	TppGlodLog findByOrdId(long ordId);
	
	/**
	 * 查询操作记录
	 * @param glodLogModel
	 * @return
	 */
	PageDataList<TppGlodLogModel> list(TppGlodLogModel glodLogModel);
	
	/**
	 * 修改操作记录状态
	 */
	void setTppGlodLogStatus();
}
