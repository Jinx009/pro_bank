package com.rongdu.p2psys.nb.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;

public interface PpfundUploadDao extends BaseDao<PpfundUpload>
{
	/**
	 * 通过产品主id获取产品头图
	 * 
	 * @param ppfund_id
	 * @return
	 */
	public PpfundUpload getByPpfundId(long ppfund_id);
}
