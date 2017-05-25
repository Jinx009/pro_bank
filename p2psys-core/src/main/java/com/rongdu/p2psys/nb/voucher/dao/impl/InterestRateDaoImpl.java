package com.rongdu.p2psys.nb.voucher.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.voucher.dao.InterestRateDao;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;

@Repository("interestRateDao")
public class InterestRateDaoImpl extends BaseDaoImpl<InterestRate> implements
		InterestRateDao {

}
