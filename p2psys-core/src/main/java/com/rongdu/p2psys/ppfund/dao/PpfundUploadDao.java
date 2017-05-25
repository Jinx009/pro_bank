package com.rongdu.p2psys.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;

/**
 * PPfund资料图片上传
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月22日
 */
public interface PpfundUploadDao extends BaseDao<PpfundUpload> {
	/**
	 * 获取PPfund头像图片
	 * @return
	 */
	PpfundUpload getPpfundImg(long ppfundId);
}
