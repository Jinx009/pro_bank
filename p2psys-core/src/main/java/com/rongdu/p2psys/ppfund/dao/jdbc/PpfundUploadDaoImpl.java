package com.rongdu.p2psys.ppfund.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.ppfund.dao.PpfundUploadDao;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;

/**
 * PPfund资料图片上传
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月22日
 */
@Repository("ppfundUploadDao")
public class PpfundUploadDaoImpl extends BaseDaoImpl<PpfundUpload> implements
		PpfundUploadDao {

	@Override
	public PpfundUpload getPpfundImg(long ppfundId) {
		QueryParam param = QueryParam.getInstance().addParam("ppfund.id", ppfundId)
				.addParam("type", PpfundUpload.PPFUND_ICON);
		List<PpfundUpload> list =  this.findByCriteria(param);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
