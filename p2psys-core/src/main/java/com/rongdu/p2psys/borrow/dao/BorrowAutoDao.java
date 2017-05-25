package com.rongdu.p2psys.borrow.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.model.BorrowModel;

/**
 * 自动投标Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月21日
 */
public interface BorrowAutoDao extends BaseDao<BorrowAuto> {

	/**
	 * 排名
	 * 
	 * @param userId 用户Id
	 * @return int
	 */
	int rank(long userId);

	/**
	 * 计数
	 * 
	 * @return
	 */
	int count();

	/**
	 * 分页
	 * 
	 * @param borrow
	 * @param pager
	 * @param size
	 * @return
	 */
	List<BorrowAuto> list(BorrowModel model);

}
