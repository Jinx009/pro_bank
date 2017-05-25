package com.rongdu.p2psys.ppfund.dao.jdbc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.ppfund.dao.PpfundOutDao;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund转出
 * @author yinliang
 * @version 2.0
 * @Date   2015年3月21日
 */
@Repository("ppfundOutDao")
public class PpfundOutDaoImpl extends BaseDaoImpl<PpfundOut> implements PpfundOutDao {

	@Override
	public double dayOutMoney(User user) {
		// TODO Auto-generated method stub
		String sqlStr = " select sum(money) from rd_ppfund_out o where date(o.add_time) = CURDATE() and  o.user_id = " + user.getUserId();
		Query query = em.createNativeQuery(sqlStr);
		Double sumMoney = 0.00;
		Object o = query.getSingleResult();
		if(o!=null)
		{
//			sumMoney = (Double)o;
			sumMoney = Double.valueOf(o.toString());
		}
		return sumMoney;
	}
	
	@Override
	public double ppfundOutMoney(PpfundIn in) {
		// TODO Auto-generated method stub
		String sqlStr = " select sum(money) from rd_ppfund_out o where ppfund_in_id = " + in.getId();
		Query query = em.createNativeQuery(sqlStr);
		Double sumMoney = 0.00;
		Object o = query.getSingleResult();
		if(o!=null)
		{
//			sumMoney = (Double)o;
			sumMoney = Double.valueOf(o.toString());
		}
		return sumMoney;
	}
	

}
