package com.rongdu.p2psys.ppfund.dao.jdbc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品转入
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月20日
 */
@Repository("ppfundInDao")
public class PpfundInDaoImpl extends BaseDaoImpl<PpfundIn> implements
		PpfundInDao {

	@Override
	public List<PpfundIn> getlist(int isOut) {
		QueryParam param = QueryParam.getInstance().addParam("isOut", isOut);
		return this.findByCriteria(param);
	}

	@Override
	public List<PpfundIn> getExpireList() {
		Date date = DateUtil.valueOf(DateUtil.dateStr2(new Date()));
		QueryParam param = QueryParam.getInstance().addParam("isOut", 0).addParam("outTime", Operators.LT, date);
		return this.findByCriteria(param);
	}

	@Override
	public double getLastYearInMoney(Date startTime, Date endTime, User user) {
		String sql = "select sum(account) from PpfundIn where addTime >= ?1 and addTime <= ?2 and user = ?3";
		Query query = em.createQuery(sql).setParameter(1, startTime).setParameter(2, endTime).setParameter(3, user);
		Object obj = query.getSingleResult();
		if(obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getCollectionCapitalByUser(User user) {
		String jql = "select sum(account) from PpfundIn where isOut = 0 and user = ?1";
		Query query = em.createQuery(jql).setParameter(1, user);
		Object obj = query.getSingleResult();
		if(obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}
	
	@Override
	public double getCollectionInterestByUser(User user) {
		String jql = "select sum(interest) from PpfundIn where isOut = 0 and user = ?1";
		Query query = em.createQuery(jql).setParameter(1, user);
		Object obj = query.getSingleResult();
		if(obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public PpfundIn getLastInByPpfundId(long id) {
		String jql = "from PpfundIn where ppfund.id = ?1 order by addTime desc limit 1";
		Query query = em.createQuery(jql, PpfundIn.class).setParameter(1, id);
		List<PpfundIn> list = query.getResultList();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public double getMostAccountTotalByUserAndPpfund(User user, Ppfund ppfund) {
		String jql = "select sum(account) from PpfundIn where user = ?1 and ppfund = ?2";
		Query query = em.createQuery(jql).setParameter(1, user).setParameter(2, ppfund);
		Object obj = query.getSingleResult();
		if(obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public List<PpfundIn> getNoOutTimePpfundIn(Ppfund ppfund) {
		QueryParam param = QueryParam.getInstance()
				.addParam("ppfund", ppfund)
				.addParam("outTime", null);
		return this.findByCriteria(param);
	}

	@Override
	public double outInterest() {
		String sql = "SELECT SUM(interest) FROM rd_ppfund_in  WHERE is_out = 1 ";
        Query query = em.createNativeQuery(sql);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public double getOutAmountByDate(String date) {
		String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select sum(money) from rd_ppfund_out where date_format(add_time,'"+dateFormat+"') = ?";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        double count = 0;
        if (obj != null) {
            count = ((BigDecimal) obj).doubleValue();
        }
        return count;
	}

	@Override
	public double getInAmountByDate(String date) {
		String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select sum(account) from rd_ppfund_in where date_format(add_time,'"+dateFormat+"') = ?";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        double count = 0;
        if (obj != null) {
            count = ((BigDecimal) obj).doubleValue();
        }
        return count;
	}
}
