package com.rongdu.p2psys.nb.ppfund.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.ppfund.dao.PpfundUploadDao;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;

@Repository("thePpfundUploadDao")
public class PpfundUploadDaoImpl extends BaseDaoImpl<PpfundUpload> implements PpfundUploadDao
{

	public PpfundUpload getByPpfundId(long ppfund_id)
	{
		QueryParam param = QueryParam.getInstance().addParam("ppfund.id",ppfund_id).addParam("type", PpfundUpload.PPFUND_ICON);
		List<PpfundUpload> list =  this.findByCriteria(param);
		if(list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}
	
}
