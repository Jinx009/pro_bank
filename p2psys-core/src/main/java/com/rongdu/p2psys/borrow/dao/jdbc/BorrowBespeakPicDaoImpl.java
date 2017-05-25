package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.borrow.dao.BorrowBespeakPicDao;
import com.rongdu.p2psys.borrow.domain.BorrowBespeakPic;

@Repository("borrowBespeakPicDao")
public class BorrowBespeakPicDaoImpl extends BaseDaoImpl<BorrowBespeakPic> implements BorrowBespeakPicDao {

	@Override
	public void addBorrowBespeakPic(List<BorrowBespeakPic> list) {
		for (BorrowBespeakPic borrowBespeakPic : list) {
			super.save(borrowBespeakPic);
		}
	}

}
