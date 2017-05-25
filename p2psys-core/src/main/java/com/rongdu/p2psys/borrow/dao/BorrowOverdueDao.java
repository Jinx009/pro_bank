package com.rongdu.p2psys.borrow.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowOverdue;

public interface BorrowOverdueDao extends BaseDao<BorrowOverdue> {
	/**
	 * 逾期垫付列表
	 * @param model BorrowOverdue实体类
	 * @return PageDataList<BorrowOverdue>
	 */
	PageDataList<BorrowOverdue> list(BorrowOverdue model);

}
