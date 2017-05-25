package com.rongdu.p2psys.account.dao.jdbc;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountCashDao;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountCashModel;

@Repository("accountCashDao")
public class AccountCashDaoImpl extends BaseDaoImpl<AccountCash> implements AccountCashDao {

	@Override
	public int count(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}

	@Override
	public PageDataList<AccountCash> list(long userId, int page) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addOrder(OrderType.DESC, "addTime");
		param.addPage(page);
		return findPageList(param);
	}

	@Override
	public AccountCashModel getCashMessage(long userId) {
		Query query = em
				.createQuery("SELECT SUM(money) AS money, SUM(credited) AS credited,SUM(fee) AS fee FROM AccountCash WHERE status=1 AND user.userId = :userId");
		Object[] object = (Object[]) query.setParameter("userId", userId).getSingleResult();
		if (object != null) {
			AccountCashModel model = new AccountCashModel();
			model.setMoney(Double.parseDouble((object[0] == null ? 0 : object[0]).toString()));
			model.setCredited(Double.parseDouble((object[1] == null ? 0 : object[1]).toString()));
			model.setFee(Double.parseDouble((object[2] == null ? 0 : object[2]).toString()));
			return model;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int getSuccessAccountCash(long userId) {
		String sql = "select count(*) from rd_account_cash where user_id=?1 and  "
				+ "status in(0,1)  and month(add_time)=month(now())";
		List list = null;
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, userId);		
		list  = query.getResultList();
		Object ob = null;
		if (list != null && list.size() > 0) {
			ob = list.get(0);
		}		
		int num = StringUtil.toInt(ob.toString());
		return num;
		
	}
	
	@Override
	public void updateStatus(long id, int status, int preStatus) {
		String sql = "UPDATE AccountCash SET status = :status WHERE id = :id AND status = :preStatus";
		Query query = em.createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("id", id);
		query.setParameter("preStatus", preStatus);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new AccountException("更改提现记录状态失败！");
		}
	}
	@Override
    public void updateStatus(long id, int status) {
        String sql = "UPDATE AccountCash SET status = :status WHERE id = :id ";
        Query query = em.createQuery(sql);
        query.setParameter("status", status);
        query.setParameter("id", id);
        int result = query.executeUpdate();
        if (result != 1) {
            throw new AccountException("更改提现记录状态失败！");
        }
    }
	@Override
	public void updateOrderNo(long id, String orderNo) {
		String sql = "UPDATE AccountCash SET order_no = :orderNo WHERE id = :id ";
		Query query = em.createQuery(sql);
		query.setParameter("orderNo", orderNo);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new AccountException("更改提现交易号失败！");
		}
	}

	@Override
	public int verifyCash(AccountCash cash, int preStatus) {
		int num = updateByJpql(
				"UPDATE AccountCash SET status=:status,credited=:credited,fee=:fee WHERE id=:id and status=:status",
				new String[] { "status", "credited", "fee", "id", "status" },
				new Object[] { cash.getStatus(), cash.getCredited(), cash.getFee(), cash.getId(), preStatus });
		if (num != 1) {
			throw new AccountException("该提现已经审核或者已经取消,请勿重复操作！");
		}
		return num;
	}

	@Override
	public int count(int status) {
		QueryParam param = QueryParam.getInstance().addParam("status", 0);
		return countByCriteria(param);
	}
	
	@Override
	public double allCashMomeny(String startTime, String endTime) {
		StringBuffer sql = new StringBuffer("SELECT SUM(t1.money) FROM rd_account_cash t1, rd_user_cache t2 WHERE t1.user_id = t2.user_id AND t1.status = 1 ");
        if(StringUtil.isNotBlank(startTime)){
        	sql.append(" AND t1.add_time >= :startTime");
        }
        if(StringUtil.isNotBlank(endTime)){
        	sql.append(" AND t1.add_time <= :endTime");
        }
        Query query = em.createNativeQuery(sql.toString());
        if(StringUtil.isNotBlank(startTime)){
        	query.setParameter("startTime", startTime);
        }
        if(StringUtil.isNotBlank(endTime)){
        	query.setParameter("endTime", endTime);
        }
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public int countMonth(long userId) {
		 String sql = "SELECT count(*) FROM rd_account_cash WHERE date_format(add_time,'%Y-%m') = date_format(now(),'%Y-%m') AND user_id = :userId AND status = 1";
	        Query query = em.createNativeQuery(sql).setParameter("userId", userId);
	        Object obj = query.getSingleResult();
	        if (obj != null) {
	            return Integer.parseInt(obj.toString());
	        }
	        return 0;
	}

	@Override
	public double getAccountCashSumByDate(String date) {
		String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select sum(money) from rd_account_cash where date_format(add_time,'"+dateFormat+"') = ? and status = 1";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        double count = 0;
        if (obj != null) {
            count = ((BigDecimal) obj).doubleValue();
        }
        return count;
	}

	@Override
	public int getTodayCashCountByUserId(long userId) {
		String sql = "SELECT count(*) FROM rd_account_cash WHERE to_days(add_time) = to_days(now()) AND user_id = :userId";
        Query query = em.createNativeQuery(sql).setParameter("userId", userId);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Integer.parseInt(obj.toString());
        }
        return 0;
	}

	@Override
	public AccountCash getCashInfo(String orderNo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("orderNo", orderNo);
		return findByCriteriaForUnique(param);
	}	
}
