package com.rongdu.p2psys.core.dao.jdbc;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.NoticeDao;
import com.rongdu.p2psys.core.domain.Notice;

@Repository("noticeDao")
public class NoticeDaoImpl extends BaseDaoImpl<Notice> implements NoticeDao {

	@Override
	public long getAddTimeByUserId(long userId, String email) throws ParseException {
		String sql = "SELECT max(addTime) FROM Notice WHERE receive_user = ? "
				+ "AND nid ='get_pwd_email' AND type = '2' AND result ='ok' AND receiveAddr = ?";
		Query query = em.createQuery(sql);
		query.setParameter(1, userId);
		query.setParameter(2, email);
		Date date = (Date) query.getSingleResult();
		long addTime = date.getTime();
		return addTime;
	}
	@Override
	public long getNoticeAddTimeByUserId(long userId, String addr) throws ParseException {
		String sql = "SELECT max(addTime) FROM Notice WHERE receive_user = ? AND status ='1' AND receiveAddr = ?";
		Query query = em.createQuery(sql);
		query.setParameter(1, userId);
		query.setParameter(2, addr);
		Date date = (Date) query.getSingleResult();
		long addTime = date.getTime();
		return addTime;
	}
}
