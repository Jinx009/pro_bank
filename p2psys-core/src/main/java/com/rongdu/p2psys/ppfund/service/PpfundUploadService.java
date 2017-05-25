package com.rongdu.p2psys.ppfund.service;

import java.util.List;

import com.rongdu.p2psys.ppfund.domain.PpfundUpload;

/**
 * PPfund资料图片上传
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月22日
 */
public interface PpfundUploadService {
	/**
	 * 根据PPfund ID及类型获取图片列表
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	public List<PpfundUpload> findByPpfundIdAndType(long id, long type);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(long id);
	
	/**
	 * 获取PPfund头像图片
	 * @return
	 */
	PpfundUpload getPpfundImg(long ppfundId);
}
