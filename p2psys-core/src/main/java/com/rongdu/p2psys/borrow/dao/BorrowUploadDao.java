package com.rongdu.p2psys.borrow.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;

/**
 * 上传图片
 * 
 * @author sj
 * @version 2.0
 * @since 2014年4月16日
 */
public interface BorrowUploadDao extends BaseDao<BorrowUpload> {
	/**
	 * 查到当前标ID所有BorrowUpload对象
	 * @param id 标ID
	 * @return List<BorrowUpload>
	 */
	List<BorrowUpload> findPicByBorrowId(long id);
	/**
	 * 查到当前抵押物ID所有BorrowUpload对象
	 * @param id 标ID
	 * @return List<BorrowUpload>
	 */
	List<BorrowUpload> findByMortgageId(long id);
	
	List<BorrowUpload> findByBorrowIdAndType(long id, int type);

}
