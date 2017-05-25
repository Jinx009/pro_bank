package com.rongdu.p2psys.ppfund.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.ppfund.dao.PpfundUploadDao;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.service.PpfundUploadService;

/**
 * PPfund资料图片上传
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月22日
 */
@Service("ppfundUploadService")
public class PpfundUploadServiceImpl implements PpfundUploadService {
	@Resource
	private PpfundUploadDao ppfundUploadDao;

	@Override
	public List<PpfundUpload> findByPpfundIdAndType(long id, long type) {
		QueryParam param = QueryParam.getInstance().addParam("ppfund.id", id)
				.addParam("type", type);
		return ppfundUploadDao.findByCriteria(param);
	}

	@Override
	public void delete(long id) {
		ppfundUploadDao.delete(id);
	}

	@Override
	public PpfundUpload getPpfundImg(long ppfundId) {
		return ppfundUploadDao.getPpfundImg(ppfundId);
	}

}
