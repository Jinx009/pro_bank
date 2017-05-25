package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.NoticeConfig;
/**
 * 
 *  
 * TODO 类说明</p>
 *  
 * @author：Administrator 
 * @version 1.0
 * @since 2014年7月14日
 */
public interface NoticeConfigDao extends BaseDao<NoticeConfig> {

	/**
	 * 
	 * @param page
	 * @return
	 */
	PageDataList<NoticeConfig> list(int page);

}
