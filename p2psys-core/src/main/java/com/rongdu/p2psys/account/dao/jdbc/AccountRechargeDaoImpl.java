package com.rongdu.p2psys.account.dao.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.model.AccountMoneyModel;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.user.domain.User;

@Repository("accountRechargeDao")
public class AccountRechargeDaoImpl extends BaseDaoImpl<AccountRecharge> implements AccountRechargeDao {

	@Override
	public int updateRechargeByStatus(int status, String returnText, String tradeNo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", status);
		param.addParam("returnMsg", returnText);
		param.addParam("verifyTime", new Date());
		param.addParam("tradeNo", tradeNo);
		return countByCriteria(param);
	}

	@Override
	public AccountRecharge getRechargeByTradeno(String tradeNo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("tradeNo", tradeNo);
		return findByCriteriaForUnique(param);
	}

	@Override
	public int updateRecharge(int status, String returnText, String tradeNo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", status);
		param.addParam("returnMsg", returnText);
		param.addParam("verifyTime", new Date());
		param.addParam("tradeNo", tradeNo);
		//param.addParam("status", 0);
		return countByCriteria(param);
	}

	@Override
	public void updateRechargeFee(double fee, long id) {
		updateByJpql("UPDATE AccountRecharge SET fee=:fee where id=:id", new String[] { "fee", "id" }, new Object[] {
				fee, id });
	}

	@Override
	public AccountRechargeModel getRechargeSummary(long userId) {
		AccountRechargeModel model = new AccountRechargeModel();
		String rechargeTotalSql = "SELECT SUM(money) AS num FROM rd_account_recharge WHERE status=1 AND money > 0 AND user_id = :userId";
		Query query = em.createNativeQuery(rechargeTotalSql).setParameter("userId", userId);
		Object totalObj = query.getSingleResult();
		if (totalObj != null) {
			double rechargeTotal = Double.parseDouble(query.getSingleResult().toString());
			model.setRechargeTotal(rechargeTotal);
		}
		String onlineRechargeTotalSql = "SELECT SUM(money) AS num FROM rd_account_recharge WHERE status=1 AND (type=1 OR type=2) AND user_id = :userId";
		Query onlineQuery = em.createNativeQuery(onlineRechargeTotalSql).setParameter("userId", userId);
		Object onlineObj = onlineQuery.getSingleResult();
		if (onlineObj != null) {
			double onlineRechargeTotal = Double.parseDouble(onlineQuery.getSingleResult().toString());
			model.setOnlineRechargeTotal(onlineRechargeTotal);
		}
		String offlineRechargeTotalSql = "SELECT SUM(money) AS num FROM rd_account_recharge WHERE status=1 AND type=3 AND user_id = :userId";
		Query offlineQuery = em.createNativeQuery(offlineRechargeTotalSql).setParameter("userId", userId);
		Object offlineObj = offlineQuery.getSingleResult();
		if (offlineObj != null) {
			double offlineRechargeTotal = Double.parseDouble(offlineQuery.getSingleResult().toString());
			model.setOfflineRechargeTotal(offlineRechargeTotal);
		}
		return model;
	}

	@Override
	public double getLastRechargeSum(long userId, int type, long startTime, long endTime) {
		if (userId <= 0) {
			return 0;
		}
		StringBuffer sql = new StringBuffer(
				"select sum(money) as num from rd_account_recharge where user_id =:userId and status=1 ");
		// 充值类型
		if (type > 0) {
			sql.append(" and type = :type ");
		}

		Query query = em.createNativeQuery(sql.toString()).setParameter("userId", userId);
		// 充值类型
		if (type > 0) {
			query.setParameter("type", type);
		}
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public int count(int status) {
		QueryParam param = QueryParam.getInstance().addParam("status", 0);
		return countByCriteria(param);
	}

    @Override
    public void updateStatus(long id, int status) {
        String sql = "UPDATE AccountRecharge SET status = :status WHERE id = :id ";
        Query query = em.createQuery(sql);
        query.setParameter("status", status);
        query.setParameter("id", id);
        int result = query.executeUpdate();
        if (result != 1) {
            throw new AccountException("更改充值记录状态失败！");
        }
    }
    @Override
    public void updateStatus(long id, int status, int preStatus) {
        String sql = "UPDATE AccountRecharge SET status = :status WHERE id = :id AND status = :preStatus";
        Query query = em.createQuery(sql);
        query.setParameter("status", status);
        query.setParameter("id", id);
        query.setParameter("preStatus", preStatus);
        int result = query.executeUpdate();
        if (result != 1) {
            throw new AccountException("更改充值记录状态失败！");
        }
    }
    
    @Override
    public int rechargedUserCount() {
        String sql = "SELECT COUNT(DISTINCT t.user_id) FROM rd_account_recharge t";
        Query query = em.createNativeQuery(sql);
        Object count = query.getSingleResult();
        if(count != null){
            return Integer.parseInt(count.toString());
        }
        return 0;        
    }
    
    @Override
	public int rechargedUserCount(String startTime, String endTime) {
    	StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT t.user_id) FROM rd_account_recharge t WHERE 1=1 ");
		if (StringUtil.isNotBlank(startTime)) {
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime)) {
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime)) {
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime)) {
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null) {
			return Integer.parseInt(count.toString());
		}
		return 0;
	}
    
    @Override
    public double rechargedAllMomeny(String startTime, String endTime) {
        StringBuffer sql = new StringBuffer("SELECT SUM(t1.money) FROM rd_account_recharge t1, rd_user_cache t2 WHERE t1.user_id = t2.user_id AND t1.status = 1");
        if (StringUtil.isNotBlank(startTime)) {
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime)) {
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime)) {
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime)) {
			query.setParameter("endTime", endTime);
		}
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }

	@Override
	public double getRechargeTotal(long userId) {
		String sql = "SELECT SUM(money) AS num FROM rd_account_recharge WHERE status=1 AND money > 0 AND user_id = :userId";
		Query query = em.createNativeQuery(sql).setParameter("userId", userId);
		Object totalObj = query.getSingleResult();
		double rechargeTotal = 0;
		if (totalObj != null) {
			rechargeTotal = Double.parseDouble(query.getSingleResult().toString());
		}
		return rechargeTotal;
	}

	@Override
	public double getRechargeTotal(long userId, String startTime, String endTime) {
		StringBuffer sql = new StringBuffer("SELECT SUM(money) AS num FROM rd_account_recharge WHERE status=1 AND money > 0 AND user_id = :userId");
		if(StringUtil.isNotBlank(startTime)){
			sql.append(" AND add_time >= :startTime ");
		}
		if(StringUtil.isNotBlank(endTime)){
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString()).setParameter("userId", userId);
		if(StringUtil.isNotBlank(startTime)){
			startTime = startTime + " 00:00:00";
			query.setParameter("startTime", startTime);
		}
		if(StringUtil.isNotBlank(endTime)){
			endTime = endTime + "  23:59:59";
			query.setParameter("endTime", endTime);
		}
		Object totalObj = query.getSingleResult();
		double rechargeTotal = 0;
		if (totalObj != null) {
			rechargeTotal = Double.parseDouble(query.getSingleResult().toString());
		}
		return rechargeTotal;
	}

	@Override
	public int getRechargeCountByUser(User user, int status) {
		QueryParam param = QueryParam.getInstance().addParam("status", status).addParam("user", user);
		return countByCriteria(param);
	}

	@Override
	public AccountRechargeModel sumAmount(AccountRechargeModel model) {
		String sumAmountSql = "select sum(ar.amount_in) from rd_account_recharge ar, rd_user u where ar.user_id=u.user_id and ar.`status`=1 ";
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			sumAmountSql += " and u.user_name like '%" + model.getSearchName() + "%' or u.real_name like '%" + model.getSearchName() + "%'";
		}else{//精确条件查询
			if (StringUtil.isNotBlank(model.getUserName())) {
				sumAmountSql += " and u.user_name=" + model.getUserName();
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				sumAmountSql += " and u.real_name=" + model.getRealName();
			}
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime());
	            sumAmountSql += " and ar.add_time >= '" + model.getStartTime() + "'";
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime());
	            sumAmountSql += " and ar.add_time <= '" + model.getEndTime()+ "'";
	        }
		}
		Query query = em.createNativeQuery(sumAmountSql);
		Object sumAmountObj = query.getSingleResult();
		if (sumAmountObj != null) {
			double sumAmount = Double.parseDouble(query.getSingleResult().toString());
			model.setAmountIn(sumAmount);
		}
		
		String sumAmountFeeSql = "select sum(ar.fee) from rd_account_recharge ar, rd_user u where ar.user_id=u.user_id and ar.`status`=1 ";
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			sumAmountFeeSql += " and u.user_name like '%" + model.getSearchName() + "%' or u.real_name like '%" + model.getSearchName() + "%'";
		}else{//精确条件查询
			if (StringUtil.isNotBlank(model.getUserName())) {
				sumAmountFeeSql += " and u.user_name=" + model.getUserName();
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				sumAmountFeeSql += " and u.real_name=" + model.getRealName();
			}
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime());
	            sumAmountFeeSql += " and ar.add_time >= '" + model.getStartTime() + "'";
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime());
	            sumAmountFeeSql += " and ar.add_time <= '" + model.getEndTime() + "'";
	        }
		}
		Query q = em.createNativeQuery(sumAmountFeeSql);
		Object sumAmountFeeObj = query.getSingleResult();
		if (sumAmountFeeObj != null) {
			double sumAmountFee = Double.parseDouble(q.getSingleResult().toString());
			model.setFee(sumAmountFee);
		}
		return model;
	}

	@Override
	public double getAccountRechargeSumByDate(String date) {
		String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select sum(money) from rd_account_recharge where date_format(add_time,'"+dateFormat+"') = ? and status = 1";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        double count = 0;
        if (obj != null) {
            count = ((BigDecimal) obj).doubleValue();
        }
        return count;
	}

	@Override
	public double getNewRechargeTotal(String startTime, String endTime) {
		StringBuffer sql=new StringBuffer("select sum(r.amount_in) from rd_account_recharge r where r.status=1");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and r.add_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and r.add_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        Query query = em.createNativeQuery(sql.toString());
		Object totalObj = query.getSingleResult();
		double rechargeTotal = 0;
		if (totalObj != null) {
			rechargeTotal = Double.parseDouble(query.getSingleResult().toString());
		}
		return rechargeTotal;
	}

	@Override
	public double getAccessAcountMoneyTotal(String startTime, String endTime) {
		StringBuffer sql=new StringBuffer("select sum(c.credited) from rd_account_cash c where c.status=1");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and c.add_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and c.add_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        Query query = em.createNativeQuery(sql.toString());
		Object totalObj = query.getSingleResult();
		double rechargeTotal = 0;
		if (totalObj != null) {
			rechargeTotal = Double.parseDouble(query.getSingleResult().toString());
		}
		return rechargeTotal;
	}

	public PageDataList<AccountMoneyModel> getBorrowCollectionMoney(String startTime, String endTime,int pageNo,int rowCount) {
		StringBuffer sql=new StringBuffer("select "
				+ "a.borrow_id,b.name,account_yes,sum_capital,"
				+ "sum_interest,sum_interest_rate_yes,a.add_time from "
				+ "(select borrow_id,sum(capital) sum_capital,"
				+ "sum(interest) sum_interest ,"
				+ "sum(interest_rate_yes) sum_interest_rate_yes,"
				+ "c.add_time from rd_borrow_collection c where c.status=0 "
				+ "GROUP BY borrow_id"
				+ ") a,rd_borrow b where "
				+ "a.borrow_id=b.id");
		StringBuffer sqlCount=new StringBuffer("select "
				+ "count(*) from "
				+ "(select borrow_id,sum(capital) sum_capital,"
				+ "sum(interest) sum_interest ,"
				+ "sum(interest_rate_yes) sum_interest_rate_yes,"
				+ "c.add_time from rd_borrow_collection c where c.status=0 "
				+ "GROUP BY borrow_id"
				+ ") a,rd_borrow b where "
				+ "a.borrow_id=b.id");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and a.add_time >='"+startTime+"'");
        	sqlCount=sqlCount.append(" and a.add_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and a.add_time<='"+endTime+"'");
        	sqlCount=sqlCount.append(" and a.add_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        sql=sql.append(" limit "+pageNo+" , "+rowCount);
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sqlCount.toString());
        List<AccountMoneyModel> list=new ArrayList<AccountMoneyModel>();
        List rows = query.getResultList();
        Object countTmp=(Object)queryCount.getSingleResult();
        Integer count=new Integer(countTmp.toString());
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        AccountMoneyModel accountModel=new AccountMoneyModel();
	        accountModel.setName(cells[1]==null?"":cells[1].toString());
	        accountModel.setInvestMoney(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        accountModel.setCollectionCapital(Double.parseDouble(cells[3]==null?"0":cells[3].toString()));
	        accountModel.setCollectInterest(Double.parseDouble(cells[4]==null?"0":cells[4].toString()));
	        accountModel.setCollectInterestRate(Double.parseDouble(cells[5]==null?"0":cells[5].toString()));
	        list.add(accountModel);
	    } 
		
		PageDataList<AccountMoneyModel> pageDateList_ = new PageDataList<AccountMoneyModel>();
		Page page=new Page(count,pageNo,rowCount);
		pageDateList_.setPage(page);
		pageDateList_.setList(list);
		return pageDateList_;
	}
	public PageDataList<AccountMoneyModel> getBorrowCollectionMoney2(String startTime, String endTime,int pageNo,int rowCount) {
		StringBuffer sql=new StringBuffer("select b.name,sum(capital),sum(interest) ,"
				+ " sum(interest_rate_yes) "
				+ " from rd_borrow_collection c,rd_borrow b where  c.status=1"
				+ " and c.borrow_id=b.id");
		StringBuffer sqlCount=new StringBuffer("select "
				+ "count(*)  from rd_borrow_collection c,rd_borrow b where  c.status=1"
				+ " and c.borrow_id=b.id");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and c.add_time >='"+startTime+"'");
        	sqlCount=sqlCount.append(" and c.add_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and c.add_time<='"+endTime+"'");
        	sqlCount=sqlCount.append(" and c.add_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        sql=sql.append(" GROUP BY borrow_id limit "+pageNo+" , "+rowCount);
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sqlCount.toString());
        List<AccountMoneyModel> list=new ArrayList<AccountMoneyModel>();
        List rows = query.getResultList();
        Object countTmp=(Object)queryCount.getSingleResult();
        Integer count=new Integer(countTmp.toString());
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        AccountMoneyModel accountModel=new AccountMoneyModel();
	        accountModel.setName(cells[0]==null?"":cells[0].toString());
//	        accountModel.setInvestMoney(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        accountModel.setCollectionCapital(Double.parseDouble(cells[1]==null?"0":cells[1].toString()));
	        accountModel.setCollectInterest(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        accountModel.setCollectInterestRate(Double.parseDouble(cells[3]==null?"0":cells[3].toString()));
	        list.add(accountModel);
	    } 
		
		PageDataList<AccountMoneyModel> pageDateList_ = new PageDataList<AccountMoneyModel>();
		Page page=new Page(count,pageNo,rowCount);
		pageDateList_.setPage(page);
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public PageDataList<AccountMoneyModel> getPpfundCollectionMoney(
			String startTime, String endTime, int pageNo, int rowCount) {
		StringBuffer sql=new StringBuffer("select a.name,a.account_yes,sum(o.account) from "
				+ " (select id,name,account_yes from rd_ppfund p where p.`status`=1) a left join "
				+ " rd_ppfund_in o on a.id=o.ppfund_id and o.is_out=1");
		StringBuffer sqlCount=new StringBuffer("select "
				+ "count(*)  from (select id,name,account_yes from rd_ppfund p where p.`status`=1) a left join "
				+ " rd_ppfund_in o on a.id=o.ppfund_id and o.is_out=1 ");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and o.out_time >='"+startTime+"'");
        	sqlCount=sqlCount.append(" and o.out_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and o.out_time<='"+endTime+"'");
        	sqlCount=sqlCount.append(" and o.out_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        sql=sql.append(" GROUP BY a.id limit "+pageNo+" , "+rowCount);
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sqlCount.toString());
        List<AccountMoneyModel> list=new ArrayList<AccountMoneyModel>();
        List rows = query.getResultList();
        Object countTmp=(Object)queryCount.getSingleResult();
        Integer count=new Integer(countTmp.toString());
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        AccountMoneyModel accountModel=new AccountMoneyModel();
	        accountModel.setName(cells[0]==null?"":cells[0].toString());
//	        accountModel.setInvestMoney(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        accountModel.setCollectionCapital(Double.parseDouble(cells[1]==null?"0":cells[1].toString()));
	        accountModel.setCollectInterest(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        //accountModel.setCollectInterestRate(Double.parseDouble(cells[3]==null?"0":cells[3].toString()));
	        list.add(accountModel);
	    } 
		
		PageDataList<AccountMoneyModel> pageDateList_ = new PageDataList<AccountMoneyModel>();
		Page page=new Page(count,pageNo,rowCount);
		pageDateList_.setPage(page);
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public PageDataList<AccountMoneyModel> getRedPacketMoney(String startTime,
			String endTime, int pageNo, int rowCount) {
		StringBuffer sql=new StringBuffer("select b.name,sum(p.amount) from rd_user_red_packet p,rd_borrow_tender t,rd_borrow b where p.is_used =0 "
				+ " and p.tender_id is not null "
				+ " and p.tender_id=t.id and t.borrow_id=b.id ");
		StringBuffer sqlCount=new StringBuffer("select "
				+ "count(*)  from rd_user_red_packet p,rd_borrow_tender t,rd_borrow b where p.is_used =0 "
				+ " and p.tender_id is not null and p.tender_id=t.id and t.borrow_id=b.id ");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and p.used_time >='"+startTime+"'");
        	sqlCount=sqlCount.append(" and p.used_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and p.used_time<='"+endTime+"'");
        	sqlCount=sqlCount.append(" and p.used_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        sql=sql.append(" GROUP BY t.borrow_id limit "+pageNo+" , "+rowCount);
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sqlCount.toString());
        List<AccountMoneyModel> list=new ArrayList<AccountMoneyModel>();
        List rows = query.getResultList();
        Object countTmp=(Object)queryCount.getSingleResult();
        Integer count=new Integer(countTmp.toString());
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        AccountMoneyModel accountModel=new AccountMoneyModel();
	        accountModel.setName(cells[0]==null?"":cells[0].toString());
//	        accountModel.setInvestMoney(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        accountModel.setCollectionCapital(Double.parseDouble(cells[1]==null?"0":cells[1].toString()));
	       // accountModel.setCollectInterest(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        //accountModel.setCollectInterestRate(Double.parseDouble(cells[3]==null?"0":cells[3].toString()));
	        list.add(accountModel);
	    } 
		
		PageDataList<AccountMoneyModel> pageDateList_ = new PageDataList<AccountMoneyModel>();
		Page page=new Page(count,pageNo,rowCount);
		pageDateList_.setPage(page);
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public PageDataList<AccountMoneyModel> getRecommendMoney(String startTime,
			String endTime, int pageNo, int rowCount) {
		StringBuffer sql=new StringBuffer("select b.name,sum(money) from nb_recommend_profit_record r,rd_borrow b where r.borrow_id=b.id  ");
		StringBuffer sqlCount=new StringBuffer("select count(*) from nb_recommend_profit_record r,rd_borrow b where r.borrow_id=b.id   ");
        if (StringUtil.isNotBlank(startTime)) {
//            Date start = DateUtil.valueOf(startTime);
        	sql=sql.append(" and r.add_time >='"+startTime+"'");
        	sqlCount=sqlCount.append(" and r.add_time >='"+startTime+"'");
        }
        if (StringUtil.isNotBlank(endTime)) {
        	sql=sql.append(" and r.add_time<='"+endTime+"'");
        	sqlCount=sqlCount.append(" and r.add_time<='"+endTime+"'");
//            Date end = DateUtil.valueOf(endTime);
        }
        sql=sql.append(" GROUP BY r.borrow_id limit "+pageNo+" , "+rowCount);
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sqlCount.toString());
        List<AccountMoneyModel> list=new ArrayList<AccountMoneyModel>();
        List rows = query.getResultList();
        Object countTmp=(Object)queryCount.getSingleResult();
        Integer count=new Integer(countTmp.toString());
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        AccountMoneyModel accountModel=new AccountMoneyModel();
	        accountModel.setName(cells[0]==null?"":cells[0].toString());
//	        accountModel.setInvestMoney(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        accountModel.setCollectionCapital(Double.parseDouble(cells[1]==null?"0":cells[1].toString()));
	       // accountModel.setCollectInterest(Double.parseDouble(cells[2]==null?"0":cells[2].toString()));
	        //accountModel.setCollectInterestRate(Double.parseDouble(cells[3]==null?"0":cells[3].toString()));
	        list.add(accountModel);
	    } 
		
		PageDataList<AccountMoneyModel> pageDateList_ = new PageDataList<AccountMoneyModel>();
		Page page=new Page(count,pageNo,rowCount);
		pageDateList_.setPage(page);
		pageDateList_.setList(list);
		return pageDateList_;
	}
	

	@Override
	public int getInfoByLLOrder(String oid_paybill) {
		QueryParam param = QueryParam.getInstance().addParam("oid_paybill", oid_paybill);
		int count = countByCriteria(param);
        return count;   
	}

}
