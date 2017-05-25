package com.rongdu.p2psys.borrow.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.dao.BorrowOverdueDao;
import com.rongdu.p2psys.borrow.domain.BorrowOverdue;

@Repository("borrowOverdueDao")
public class BorrowOverdueDaoImpl extends BaseDaoImpl<BorrowOverdue> implements BorrowOverdueDao {

	@Override
	public PageDataList<BorrowOverdue> list(BorrowOverdue model) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
