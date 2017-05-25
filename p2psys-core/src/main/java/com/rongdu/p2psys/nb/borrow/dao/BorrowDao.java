package com.rongdu.p2psys.nb.borrow.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.Borrow;

public interface BorrowDao extends BaseDao<Borrow> {

	/**
	 * 判断是否流标
	 * 
	 * @param borrowId
	 * @return 是否流标
	 */
	Boolean isSpreadBorrow(Long borrowId);

	boolean isSkipReview(Borrow borrow);

}
