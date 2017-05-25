package com.rongdu.p2psys.borrow.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.borrow.dao.BorrowUploadDao;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.service.BorrowUploadService;

/**
 * 上传图片
 * @author sj
 * @since 2014年8月25日
 */
@Service("borrowUploadService")
public class BorrowUploadServiceImpl implements BorrowUploadService {

	@Resource
	private BorrowUploadDao borrowUploadDao;
	
	@Override
	public List<BorrowUpload> findByBorrowId(long id) {
		QueryParam param = QueryParam.getInstance().addParam("borrow.id", id);
		List<BorrowUpload> bus = borrowUploadDao.findByCriteria(param);
		List<BorrowUpload> list = new ArrayList<BorrowUpload>();
        for (BorrowUpload bu : bus) {
            BorrowUpload upload = new BorrowUpload();
            upload.setPicPath(bu.getPicPath());
            upload.setType(bu.getType());
            list.add(upload);
        }
        return list;
	}

    @Override
    public List<BorrowUpload> findByBorrowIdAndType(long id) {
        QueryParam param = QueryParam.getInstance().addParam("borrow.id", id).addParam("type", 3);
        return borrowUploadDao.findByCriteria(param);
    }

    @Override
    public List<BorrowUpload> findByMortgageId(long id) {
        QueryParam param = QueryParam.getInstance().addParam("borrowMortgage.id", id);
        return borrowUploadDao.findByCriteria(param);
    }

    @Override
    public List<BorrowUpload> findByMortgageIdAndType(long id, long type) {
        QueryParam param = QueryParam.getInstance().addParam("borrowMortgage.id", id).addParam("type", type);
        return borrowUploadDao.findByCriteria(param);
    }

    @Override
    public void delete(long id) {
        borrowUploadDao.delete(id);
    }

	@Override
	public List<BorrowUpload> findByBorrowIdAndType(long id, long type) {
		 QueryParam param = QueryParam.getInstance().addParam("borrow.id", id).addParam("type", type);
	     return borrowUploadDao.findByCriteria(param);
	}
	
}
