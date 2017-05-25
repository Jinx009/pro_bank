package com.rongdu.p2psys.account.dao;

import com.rongdu.common.dao.BaseDao;
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
public interface TppGlodLogDao extends BaseDao<TppGlodLog> {
	/**
	 * 查询操作记录
	 * 
	 * @param glodLogModel
	 * @return
	 */
	PageDataList<TppGlodLogModel> list(TppGlodLogModel glodLogModel);
}
