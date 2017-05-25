package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;

/**
 * 汇率抓包
 * @author sj
 *
 */
public interface ExchangeRatePacketCaptureDao extends BaseDao<ExchangeRatePacketCapture> {

	public List<ExchangeRatePacketCapture> getCaptureRate(long borrowId);

}
