package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.ExchangeRatePacketCaptureDao;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;

@Repository("exchangeRatePacketCaptureDao")
public class ExchangeRatePacketCaptureDaoImpl extends BaseDaoImpl<ExchangeRatePacketCapture> implements ExchangeRatePacketCaptureDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ExchangeRatePacketCapture> getCaptureRate(long borrowId) {
		String sql = "SELECT erpc.* FROM rd_borrow b, rd_exchange_rate_packet_capture erpc WHERE b.id=erpc.borrow_id AND b.id = ?1 ORDER BY erpc.add_time ASC";
		Query query = em.createNativeQuery(sql, ExchangeRatePacketCapture.class).setParameter(1, borrowId);
		return query.getResultList();
	}

}
