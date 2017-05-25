package com.rongdu.p2psys.ppfund.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.ppfund.dao.PpfundEarningsDao;
import com.rongdu.p2psys.ppfund.domain.PpfundEarnings;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;

/**
 * PPfund资金管理产品
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
@Repository("ppfundEarningsDao")
public class PpfundEarningsDaoImpl extends BaseDaoImpl<PpfundEarnings>
		implements PpfundEarningsDao {

	@Override
	public double getEarningsSum(PpfundIn in) {
		String sql = "SELECT SUM(money) FROM rd_ppfund_earnings  WHERE ppfund_in_id = :id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", in.getId());
        List list = query.getResultList();
		if(list.size() > 0){
			return Double.parseDouble(list.get(0).toString());
		}
        return 0;
	}
	
	/**
	 * 获取用户收益总和
	 * 
	 * @param in
	 * @return
	 */
	public double getUserEarningsSum(PpfundIn in)
	{
		String sql = "SELECT SUM(money) FROM rd_ppfund_earnings  WHERE ppfund_in_id = :id and user_id = :userId ";
	    Query query = em.createNativeQuery(sql);
	    query.setParameter("id", in.getId());
	    query.setParameter("userId", in.getUser().getUserId());
	    List list = query.getResultList();
		if(list.size() > 0&&list.get(0)!=null){
			return Double.parseDouble(list.get(0).toString());
		}
        return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public double getLastEarningsInterest(long ppfundInId) {
		String sql = "SELECT money FROM rd_ppfund_earnings where ppfund_in_id = :id order by add_time desc limit 1";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id", ppfundInId);
		List list = query.getResultList();
		if(list.size() > 0){
			return Double.parseDouble(list.get(0).toString());
		}
		return 0;
	}

}
