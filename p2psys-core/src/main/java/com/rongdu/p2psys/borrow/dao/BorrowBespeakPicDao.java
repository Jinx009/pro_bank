package com.rongdu.p2psys.borrow.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowBespeakPic;

public interface BorrowBespeakPicDao extends BaseDao<BorrowBespeakPic> {

	public void addBorrowBespeakPic(List<BorrowBespeakPic> list);

	
	
}
