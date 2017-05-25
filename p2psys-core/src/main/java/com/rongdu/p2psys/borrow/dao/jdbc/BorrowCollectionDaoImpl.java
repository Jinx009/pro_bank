package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.ppfund.domain.PpfundEarnings;
import com.rongdu.p2psys.user.domain.User;

@SuppressWarnings("unchecked")
@Repository("borrowCollectionDao")
public class BorrowCollectionDaoImpl extends BaseDaoImpl<BorrowCollection> implements BorrowCollectionDao {

	@Override
	public double getReceivedInterestSum(long userId) {
		String jpal = "SELECT SUM(interest) FROM BorrowCollection WHERE (status = 1 OR (status = 0 AND repaymentYesAccount<>0)) AND user.userId = :userId";
		Query query = em.createQuery(jpal);
		query.setParameter("userId", userId);
		Object obj = query.getSingleResult();
		if (obj == null) {
			obj = 0;
		}
		return Double.parseDouble(obj.toString());
	}

	@Override
	public List<BorrowCollection> list(long borrowId, int period) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 0);
		param.addParam("borrow.id", borrowId);
		param.addParam("period", period);
		return super.findByCriteria(param);
	}
	
	@Override
	public List<BorrowCollection> list(long borrowId, int period, int status) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", status);
		param.addParam("borrow.id", borrowId);
		param.addParam("period", period);
		return super.findByCriteria(param);
	}	

	@Override
	public double getRemainderCapital(long tenderId) {
		String jpql = "select sum(capital) from BorrowCollection bc where bc.status=0  and bc.tender.id= ?1 ";
		Query q = em.createQuery(jpql).setParameter(1, tenderId);
		Object ret = q.getSingleResult();
		if (ret == null)
			return 0;
		return Double.parseDouble(ret.toString());
	}

	@Override
	public double getRemainderInterest(long tenderId) {
		String jpql = "select sum(interest + interestRate + floatIncome) from BorrowCollection bc where bc.status=0  and bc.tender.id= ?1 ";
		Query q = em.createQuery(jpql).setParameter(1, tenderId);
		Object ret = q.getSingleResult();
		if (ret == null)
			return 0;
		return Double.parseDouble(ret.toString());
	}
	
	@Override
	public List<BorrowCollection> vipRepayList(){
		String date = DateUtil.dateStr2(new Date()) + " 23:59:59";
		String sql = " select * from rd_borrow_collection where status = 0 "
				+ " and borrow_id in (select b.id from rd_borrow b join nb_product_type n on "
				+ " b.type = n.id where n.type_category = 'VIP') " 
				+ " and repayment_time < '" + date +"'" ;
		Query query = em.createNativeQuery(sql, BorrowCollection.class);
		return query.getResultList();
	}
	
	@Override
	public double getInterestByBorrowAndPeriod(long borrowId, int period) {
        String jpql = "select sum(interest) from BorrowCollection bc where bc.status=0  and bc.borrow.id= ?1 and bc.period= ?2 ";
        Query query = em.createQuery(jpql);
        query.setParameter(1, borrowId);
        query.setParameter(2, period);
        Object ret = query.getSingleResult();
        if (ret == null)
            return 0;
        return Double.parseDouble(ret.toString());
	}

	@Override
	public BorrowCollection getCollectionByTenderAndPeriod(long tenderId, int period) {
		String jpql = "from BorrowCollection bc where bc.status=0  and bc.tender.id= ?1 and bc.period = ?2";
		Query query =em.createQuery(jpql);
		query.setParameter(1, tenderId);
		query.setParameter(2,period);
		List<BorrowCollection> list = query.getResultList();
		
		return (BorrowCollection)list.get(0);
	}
	
	@Override
	public BorrowCollection getNextCollectionByUserId(long userId){
		String jpql = "from BorrowCollection bc where bc.status=0  and repaymentTime>=now() and "
						+ "((bc.borrow.status in (3,6,7) and bc.borrow.type != "+ Borrow.TYPE_FLOW +") "
						+ "or(bc.borrow.status in(1,8) and bc.borrow.type = "+ Borrow.TYPE_FLOW 
						+ ")) and bc.user.userId= ?1 AND bc.interest!=bc.bondInterest order by repaymentTime asc";
		Query query =em.createQuery(jpql);
		query.setParameter(1, userId);
		List<BorrowCollection> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public int countCollect(long userId,int status) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", status);
		param.addParam("user.userId", userId);
		SearchFilter orFilter1 = new SearchFilter("borrow.status", Operators.EQ, 3);
		SearchFilter orFilter2 = new SearchFilter("borrow.status", Operators.EQ, 6);
		SearchFilter orFilter3 = new SearchFilter("borrow.status", Operators.EQ, 7);
		SearchFilter orFilter4 = new SearchFilter("borrow.status", Operators.EQ, 8);
		param.addOrFilter(orFilter1, orFilter2, orFilter3, orFilter4);
		return countByCriteria(param);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public BorrowCollectionModel getCollectStatistics(long userId){
		BorrowCollectionModel bm = new BorrowCollectionModel();
		HashMap data = new HashMap();
		List list = new ArrayList();
		//获取本月待收sql
		String jpqlMonth = "select ifnull(sum(bc.repayment_account),0),count(bc.id) from rd_borrow_collection bc,rd_borrow rb where bc.status=0  and bc.user_id= ?1 "
				+ "and month(bc.repayment_time)=month(now()) AND year(bc.repayment_time)=year(now()) and rb.status in(3,6,7) and bc.borrow_id=rb.id";
		Query q = em.createNativeQuery(jpqlMonth).setParameter(1, userId);
		list = q.getResultList();
		double monthAccount = 0;
		int monthCount = 0;
		if (list != null && list.size() > 0) {
			Iterator iterator = list.iterator(); 
			while (iterator.hasNext()) { 
				Object[] row = (Object[]) iterator.next();	
				monthAccount= Double.parseDouble(row[0].toString());
				monthCount = Integer.parseInt(row[1].toString());
			}
		}
		data.put("monthAccount", monthAccount);
		data.put("monthCount", monthCount);
		//获取下月待收sql
		String jpqlNextMonth = "select ifnull(sum(bc.repayment_account),0),count(bc.id) from rd_borrow_collection bc,rd_borrow rb where bc.status=0  and bc.user_id= ?1 "
				+ " and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( bc.repayment_time, '%Y%m' ) ) =-1 and rb.status in(3,6,7) and bc.borrow_id=rb.id";
		q = em.createNativeQuery(jpqlNextMonth).setParameter(1, userId);
		list = q.getResultList();
		double nextMonthAccount = 0;
		int nextMonthCount = 0;
		if (list != null && list.size() > 0) {
			Iterator iterator = list.iterator(); 
			while (iterator.hasNext()) { 
				Object[] row = (Object[]) iterator.next();	
				nextMonthAccount= Double.parseDouble(row[0].toString());
				nextMonthCount = Integer.parseInt(row[1].toString());
			}
		}
		data.put("nextMonthAccount", nextMonthAccount);
		data.put("nextMonthCount", nextMonthCount);
		//获取剩余待收sql
		String jpqlOther = "select ifnull(sum(bc.repayment_account),0),count(bc.id) from rd_borrow_collection bc,rd_borrow rb where bc.status=0  and bc.user_id= ?1 "
				+ " and (PERIOD_DIFF( date_format( bc.repayment_time, '%Y%m' ) ,  date_format( now( ) , '%Y%m' )) >1 or PERIOD_DIFF( date_format( bc.repayment_time, '%Y%m' ) ,  date_format( now( ) , '%Y%m' )) < 0)"
				+"and rb.status in(3,6,7) and bc.borrow_id=rb.id";
		q = em.createNativeQuery(jpqlOther).setParameter(1, userId);
		list = q.getResultList();
		double otherMonthAccount = 0;
		int otherMonthCount = 0;
		if (list != null && list.size() > 0) {
			Iterator iterator = list.iterator(); 
			while (iterator.hasNext()) { 
				Object[] row = (Object[]) iterator.next();	
				otherMonthAccount= Double.parseDouble(row[0].toString());
				otherMonthCount = Integer.parseInt(row[1].toString());
			}
		}
		data.put("otherMonthAccount", otherMonthAccount);
		data.put("otherMonthCount", otherMonthCount);
		double allMonthAccount=BigDecimalUtil.add(monthAccount,nextMonthAccount,otherMonthAccount);
		int allMonthCount=monthCount+nextMonthCount+otherMonthCount;
		data.put("allMonthAccount", allMonthAccount);
		data.put("allMonthCount", allMonthCount);
		bm.setCollectData(data);
		return bm;
	}
	@Override
	public double getCollectByMonth(int month, long userId) {
		String jpql = "select sum(repaymentAccount) from BorrowCollection bc where bc.user.userId= ?1 and bc.status=0 and month(bc.repaymentTime)=?2 AND year(bc.repaymentTime)=year(now())";
		Query q = em.createQuery(jpql).setParameter(1, userId).setParameter(2, month);
		Object ret = q.getSingleResult();
		if (ret == null)
			return 0;
		return Double.parseDouble(ret.toString());
	}
	
	@Override
	public BorrowCollectionModel getBCMByCollectTime(BorrowCollectionModel bm, Date nextCollectTime, long userId) {
		String jpql = "select ifnull(sum(repayment_account-bond_capital-bond_interest),0),count(id) from rd_borrow_collection bc where bc.status=0  and bc.user_id= ?1 "
				+ " and date_format( bc.repayment_time, '%Y%m%d' ) = date_format( ?2 , '%Y%m%d' )";
		Query q = em.createNativeQuery(jpql).setParameter(1, userId).setParameter(2, nextCollectTime);
		Object[] obj = (Object[]) q.getSingleResult();
		double account = Double.parseDouble(obj[0] + "");
		int count = Integer.parseInt(obj[1] + "");
		bm.setNextCollextAccount(account);
		bm.setNextCollextCount(count);
		return bm;
	}

	@Override
	public double getRemainderMoney(long borrowId) {
		String jpql = "select sum(capital) from BorrowCollection bc where bc.status=0  and bc.borrow.id= ?1 ";
		Query q = em.createQuery(jpql).setParameter(1, borrowId);
		Object ret = q.getSingleResult();
		if (ret == null)
			return 0;
		return Double.parseDouble(ret.toString());
	}

	@Override
	public void updatePriorRepayStatus(long borrowId) {
		StringBuffer buffer = new StringBuffer(
				"UPDATE rd_borrow_collection SET status = 1,repayment_yes_time = ?1");
		buffer.append(" WHERE status = 0 AND borrow_id = ?2");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter(1, new Date());
		query.setParameter(2, borrowId);
		query.executeUpdate();
	}
	
    @Override
    public double accumulatedNetIncome(User user) {
        String jpql = "select sum(interest - manageFee) from BorrowCollection bc where bc.status = 1  and bc.user.userId = ?1";
        Query q = em.createQuery(jpql).setParameter(1, user.getUserId());
        Object ret = q.getSingleResult();
        if(ret == null) return 0;
        return Double.parseDouble(ret.toString());
    }
	
	 @Override
	    public double netProfit(User user) {
		 double collection = borrowCollection(user);
		 double ppfund = ppfundEaring(user);
	        return (collection+ppfund);
	    }
	 
	 public double borrowCollection(User user){
		    String jpql = "select sum(interest - manageFee) from BorrowCollection bc where bc.status = 1  and bc.user.userId in(select userId from User where bindId= ?1)";
	        Query q = em.createQuery(jpql).setParameter(1, user.getBindId());
	        Object ret = q.getSingleResult();
	        if(ret == null) return 0;
	        return Double.parseDouble(ret.toString());
	 }
	 
	 public double ppfundEaring(User user){
		    String jpql = "select sum(money) from PpfundEarnings as pp where  pp.user.userId in(select userId from User where bindId= ?1)";
	        Query q = em.createQuery(jpql).setParameter(1, user.getBindId());
	        Object ret = q.getSingleResult();
	        if(ret == null) return 0;
	        return Double.parseDouble(ret.toString());
	 }

    @Override
    public double inInvestAmount(User user, int status) {
        StringBuffer jpql = new StringBuffer("SELECT SUM(p1.capital) FROM rd_borrow_collection p1,"); 
        jpql.append(" rd_borrow_repayment p2 WHERE p1.borrow_id = p2.borrow_id AND p1.period = p2.period");
        if(status == 0){
            jpql.append(" AND p1.status = 0 AND p2.status = 0 AND p1.user_id = :userId"); 
        }else if(status == 1){
            jpql.append(" AND ( (p1.status = 0 AND p2.status = 0) OR p1.status = 1) AND p1.user_id = :userId"); 
        }
        Query q = em.createNativeQuery(jpql.toString()).setParameter("userId", user.getUserId());
        Object ret = q.getSingleResult();
        if(ret == null) return 0;
        return Double.parseDouble(ret.toString());
    }

    /**
     * 今日收益：计算今日收益的时候，要把这个标已还和未还的都要算上，之后除以借款的时间
     * 在修改的次方法的时候一定要谨慎
     */
    @Override
    public double sumTodayInterest(User user) {
        StringBuffer jpql = new StringBuffer("SELECT SUM((p1.interest)/(TO_DAYS(p1.repayment_time)");
        jpql.append(" - IF(p3.borrow_time_type = 0, TO_DAYS(DATE_SUB(p1.repayment_time,INTERVAL p3.time_limit MONTH)),");
        jpql.append(" TO_DAYS(DATE_SUB(p1.repayment_time,INTERVAL p3.time_limit DAY))))) AS money");
        jpql.append(" FROM rd_borrow_collection p1 , rd_borrow_repayment p2 , rd_borrow p3 WHERE p1.borrow_id = p2.borrow_id");
        jpql.append(" AND p3.status != 8 AND p1.period = p2.period AND p3.id = p2.borrow_id AND p1.user_id = :userId");
        Query q = em.createNativeQuery(jpql.toString()).setParameter("userId", user.getUserId());
        Object ret = q.getSingleResult();
        if(ret == null) return 0;
        return Double.parseDouble(ret.toString());
    }

    @Override
    public double getInterestByUser(User user) {
    	StringBuffer jpql = new StringBuffer("SELECT SUM(p1.interest-p1.bond_interest+p1.interest_rate) FROM rd_borrow_collection p1,");
        jpql.append(" rd_borrow_repayment p2,rd_borrow p3 WHERE p1.borrow_id = p2.borrow_id AND p1.borrow_id = p3.id AND p3.status in(3,6,7,8)"
        		+ " AND p1.period = p2.period");
        jpql.append(" AND p1.status = 0 AND p1.user_id = :userId");
        Query q = em.createNativeQuery(jpql.toString()).setParameter("userId", user.getUserId());
        double interest = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            interest = Double.parseDouble(obj.toString());
        }
        return interest;
    }
    @Override
    public List<Object[]> getInterestByUserAndDate(User user, String date) {
    	StringBuffer sql = new StringBuffer("SELECT DATE_FORMAT(p1.repayment_time,'%m-%d'),SUM(p1.interest) FROM ");
        sql.append(" (SELECT c1.borrow_id, c1.period,c1.status,c1.user_id,(c1.interest-c1.bond_interest) as interest,c1.repayment_time FROM rd_borrow_collection c1 UNION ALL ");
        sql.append(" SELECT c2.borrow_id, c2.period,c2.status,c2.user_id,(c2.interest-c2.bond_interest) as interest,c2.collection_time AS repayment_time FROM rd_bond_collection c2) p1,");
        sql.append(" rd_borrow_repayment p2 WHERE p1.borrow_id = p2.borrow_id AND p1.period = p2.period");
        sql.append(" AND p1.status = 0 AND DATE_FORMAT(p1.repayment_time,'%Y-%m') = :timeDate AND p1.user_id = :userId");
        sql.append(" group by date_format(repayment_time,'%m-%d')");
        Query q = em.createNativeQuery(sql.toString()).setParameter("timeDate", date).setParameter("userId", user.getUserId());
        return q.getResultList();
    }
    @Override
    public double getCapitalByUser(User user) {
    	StringBuffer jpql = new StringBuffer("SELECT SUM(p1.capital-p1.bond_capital) FROM rd_borrow_collection p1,");
        jpql.append(" rd_borrow_repayment p2,rd_borrow p3 WHERE p1.borrow_id = p2.borrow_id AND p1.borrow_id = p3.id AND p3.status in (3,6,7,8)"
        		+ " AND p1.period = p2.period");
        jpql.append(" AND p1.status = 0 AND p1.user_id = :userId");
        Query q = em.createNativeQuery(jpql.toString()).setParameter("userId", user.getUserId());
        double capital = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            capital = Double.parseDouble(obj.toString());
        }
        return capital;
    }
    @Override
    public List<Object[]>  getCapitalByUserAndDate(User user, String date) {
    	StringBuffer sql = new StringBuffer("SELECT DATE_FORMAT(p1.repayment_time,'%m-%d'),SUM(p1.capital) FROM ");
        sql.append(" (SELECT c1.borrow_id, c1.period,c1.status,c1.user_id,(c1.capital-c1.bond_capital) as capital,c1.repayment_time FROM rd_borrow_collection c1 UNION ALL ");
        sql.append(" 	SELECT c2.borrow_id, c2.period,c2.status,c2.user_id,(c2.capital-c2.bond_capital) as capital,c2.collection_time AS repayment_time FROM rd_bond_collection c2) p1,");
        sql.append(" rd_borrow_repayment p2 WHERE p1.borrow_id = p2.borrow_id AND p1.period = p2.period");
        sql.append(" AND p1.status = 0 AND DATE_FORMAT(p1.repayment_time,'%Y-%m') = :timeDate AND p1.user_id = :userId");
        sql.append(" group by date_format(repayment_time,'%m-%d')");
        Query q = em.createNativeQuery(sql.toString()).setParameter("timeDate", date).setParameter("userId", user.getUserId());
        return q.getResultList();
    }

    @Override
    public List<String> getCollectionDate(User user) {
    	StringBuffer jpql = new StringBuffer("SELECT DISTINCT(p1.coll_time) from (");
    	jpql.append("SELECT DISTINCT(DATE_FORMAT(c2.collection_time,'%Y-%m')) as coll_time FROM rd_bond_collection c2 where c2.status = 0 and c2.user_id =:cuserId  union all ");
    	jpql.append("SELECT DISTINCT(DATE_FORMAT(bc2.repayment_time,'%Y-%m')) as coll_time FROM rd_borrow_collection bc2 join rd_borrow b on bc2.borrow_id = b.id where bc2.user_id = :bcUserId and bc2.status = 0 and b.status in (6,7)) p1");
    	jpql.append(" WHERE p1.coll_time IS NOT NULL ORDER BY p1.coll_time ASC");
    	Query q = em.createNativeQuery(jpql.toString());
        q.setParameter("cuserId", user.getUserId());
        q.setParameter("bcUserId", user.getUserId());
        return q.getResultList();
    }

    @Override
    public List<BorrowCollection> getMemberCollectionList(User user) {
        StringBuffer jpql = new StringBuffer("SELECT  p1.* FROM rd_borrow_collection p1,");
        jpql.append(" rd_borrow_repayment p2 WHERE p1.borrow_id = p2.borrow_id AND p1.period = p2.period");
        jpql.append(" AND p1.status = 0 AND p2.status = 0 AND p1.user_id = :userId ORDER BY p1.repayment_time ,p1.id ASC LIMIT 4");
        Query query = em.createNativeQuery(jpql.toString(), BorrowCollection.class);
        query.setParameter("userId", user.getUserId());
        List<BorrowCollection> list = (List<BorrowCollection>) query.getResultList();
        return list;
    }

    @Override
    public PageDataList<BorrowCollection> getList(BorrowCollectionModel model) {
    	StringBuffer jpql = new StringBuffer("SELECT  p1.* FROM rd_borrow_collection p1,");
        jpql.append(" rd_borrow_repayment p2, rd_borrow p3, rd_user p4 WHERE p1.borrow_id = p2.borrow_id");
        jpql.append(" AND p1.period = p2.period AND p1.borrow_id = p3.id AND p4.user_id = p1.user_id AND p3.status != 69 AND (p1.interest!=p1.bond_interest or p1.interest=0) ");
        long userId = 0;
        String investUserName = "";
        String investRealName = "";
        String borrowName = "";
        String bidNo = "";
        PageDataList<BorrowCollection> pageDataList = new PageDataList<BorrowCollection>();
        if (model != null) {
        	if(!StringUtil.isBlank(model.getBorrowName())){
        		borrowName = model.getBorrowName();
                jpql.append(" AND p3.name LIKE :borrowName");
            }
            if(StringUtil.isNotBlank(model.getBidNo())){
            	bidNo = model.getBidNo();
            	jpql.append(" AND p3.bid_no LIKE :bidNo");
            }
            if(model.getUser() != null && model.getUser().getUserId() > 0){
                jpql.append(" AND p1.user_id = :userId");
                userId = model.getUser().getUserId();
            }
            if(model.getInvestUserName() != null && model.getInvestUserName().length() > 0){
                investUserName = model.getInvestUserName();
                jpql.append(" AND p4.user_name = :investUserName");
            }
            if(model.getInvestRealName() != null && model.getInvestRealName().length() > 0){
            	investRealName = model.getInvestRealName();
            	jpql.append(" AND p4.real_name = :investRealName");
            }
            String searchName = null;
            if(!StringUtil.isBlank(model.getSearchName())){
            	searchName = model.getSearchName();
            	jpql.append(" AND ( p4.user_name LIKE :searchName or p4.real_name LIKE :searchName or p3.name LIKE :searchName )");
            }
            Date startTime1 = null;
            Date endTime1 = null;
            if (StringUtil.isNotBlank(model.getStartTime())) {
                startTime1 = DateUtil.valueOf(model.getStartTime());
                jpql.append(" AND p1.repayment_time >= :startTimeA");
            }
            if (StringUtil.isNotBlank(model.getEndTime())) {
                endTime1 = DateUtil.valueOf(model.getEndTime());
                jpql.append(" AND p1.repayment_time <= :endTimeA");
            }
            int status = -1;
            Date startTime2 = null;
            Date endTime2 = null;
            if(model.getTime() > 0){
                if(model.getStatus() == 0){// 如果状态为待收，则查询时间为当前时间加上model.getTime()
                    startTime1 = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                    jpql.append(" AND p1.status = 0 AND p1.repayment_time >= :startTimeA AND p1.repayment_time <= :endTimeA");
                    if (model.getTime() == 7) {
                        endTime1 = DateUtil.rollDay(startTime1, 7);
                        endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                    } else if (model.getTime() > 0 && model.getTime() < 4){
                        endTime1 = DateUtil.rollMon(startTime1, model.getTime());
                        endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                    }
                }else if(model.getStatus() == 1){// 如果状态为已收，则查询时间为当前时间减去model.getTime()
                    endTime1 = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                    jpql.append(" AND p1.status = 1 AND p1.repayment_time >= :startTimeA AND p1.repayment_time <= :endTimeA");
                    if (model.getTime() == 7) {
                        startTime1 = DateUtil.rollDay(endTime1, -7);
                        startTime1 = DateUtil.getDayEndTime(startTime1.getTime() / 1000);
                    } else if (model.getTime() > 0 && model.getTime() < 4){
                        startTime1 = DateUtil.rollMon(endTime1, -model.getTime());
                        startTime1 = DateUtil.getDayEndTime(startTime1.getTime() / 1000);
                    }
                }else if(model.getStatus() == -1){
                    jpql.append(" AND ( (p1.status = 0 AND p1.repayment_time >= :startTimeA AND p1.repayment_time <= :endTimeA)");
                    jpql.append(" OR ( p1.status = 1 AND p1.repayment_time >= :startTimeB AND p1.repayment_time <= :endTimeB ) )");
                    startTime1 = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                    endTime2 = startTime1;
                    if (model.getTime() == 7) {
                        // 待收信息时间处理
                        endTime1 = DateUtil.rollDay(startTime1, 7);
                        endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                        // 已收信息时间处理
                        startTime2 = DateUtil.rollDay(endTime2, -7);
                        startTime2 = DateUtil.getDayEndTime(startTime2.getTime() / 1000);
                        
                    } else if (model.getTime() > 0 && model.getTime() < 4){
                        // 待收信息时间处理
                        endTime1 = DateUtil.rollMon(startTime1, model.getTime());
                        endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                        // 已收信息时间处理
                        startTime2 = DateUtil.rollMon(endTime2, -model.getTime());
                        startTime2 = DateUtil.getDayEndTime(startTime2.getTime() / 1000);
                    } 
                }
            }else if(model.getTime() <= 0 && model.getStatus() > -1){
                status = model.getStatus();
                jpql.append(" AND p1.status = :status");
            }
            
            jpql.append(" ORDER BY p1.repayment_time ,p1.id ASC");
            Query query = em.createNativeQuery(jpql.toString(), BorrowCollection.class);
            if (userId > 0) {
                query.setParameter("userId", userId);
            }
            if (status > -1) {
                query.setParameter("status", status);
            }
            if (startTime1 != null) {
                query.setParameter("startTimeA", startTime1);
            }
            if (endTime1 != null) {
                query.setParameter("endTimeA", endTime1);
            }
            if (startTime2 != null) {
                query.setParameter("startTimeB", startTime2);
            }
            if (endTime2 != null) {
                query.setParameter("endTimeB", endTime2);
            }
            if(investUserName.length() > 0){
                query.setParameter("investUserName", investUserName);
            }
            if(investRealName.length() > 0){
            	query.setParameter("investRealName", investRealName);
            }
            if(borrowName.length() > 0){
            	query.setParameter("borrowName", "%"+borrowName+"%");
            }
            if(bidNo.length() > 0){
            	query.setParameter("bidNo", "%"+bidNo+"%");
            }
            if(searchName != null){
            	query.setParameter("searchName","%"+searchName+"%");
            }
            Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
            query.setFirstResult((model.getPage() - 1) * model.getSize());
            query.setMaxResults(model.getSize());
            List<BorrowCollection> list = query.getResultList();
            pageDataList.setList(list);
            pageDataList.setPage(page);
        }
        return pageDataList;
    }
    
    @Override
    public double sumInterest() {
        StringBuffer jpql = new StringBuffer("select p1.interest + p2.interest from " +
        			" (SELECT SUM(interest) interest FROM rd_borrow_collection WHERE status = 1) p1" +
        			" join (select sum(interest) interest from rd_ppfund_in where is_out = 1) p2");
        Query q = em.createNativeQuery(jpql.toString());
        double interest = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            interest = Double.parseDouble(obj.toString());
        }
        return interest;
    }

    @Override
    public double remainderCapital(long tenderId, long userId, int period) {
        StringBuffer jpql = new StringBuffer("SELECT SUM(p1.interest) FROM rd_borrow_collection p1 WHERE");
        jpql.append(" p1.status = 0 AND p1.tender_id = tenderId AND p1.user_id = userId AND p1.period >= :period");
        Query q = em.createNativeQuery(jpql.toString());
        q.setParameter("tenderId", tenderId);
        q.setParameter("userId", userId);
        q.setParameter("period", period);
        double capital = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            capital = Double.parseDouble(obj.toString());
        }
        return capital;
    }

    @Override
    public double remainderInterest(long tenderId, long userId, int period) {
        StringBuffer jpql = new StringBuffer("SELECT SUM(p1.capital) FROM rd_borrow_collection p1 WHERE");
        jpql.append(" p1.status = 0 AND p1.tender_id = tenderId AND p1.user_id = userId AND p1.period >= :period");
        Query q = em.createNativeQuery(jpql.toString());
        q.setParameter("tenderId", tenderId);
        q.setParameter("userId", userId);
        q.setParameter("period", period);
        double interest = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            interest = Double.parseDouble(obj.toString());
        }
        return interest;
    }

	@Override
	public Object[] getCapitalAndInterestByBorrowAndPeriod(long borrowId, int period) {
		String jpql = "select sum(capital), sum(interest) from BorrowCollection bc where bc.status=0  and bc.borrow.id= ?1 and bc.period= ?2 ";
		Query query = em.createQuery(jpql).setParameter(1, borrowId).setParameter(2, period);
        Object[] capitalAndInterest = null;
        Object obj = query.getSingleResult();
        if (obj != null) {
        	capitalAndInterest = (Object[]) obj;
        }
        return capitalAndInterest;
	}

	@Override
	public void updateInterest(long id, double difference) {
		String jpql = "select max(id) from rd_borrow_collection where borrow_id=?";
		int maxId = (Integer) em.createNativeQuery(jpql).setParameter(1, id).getSingleResult();
		BorrowCollection bc = find(new Long(maxId));
		bc.setRepaymentAccount(BigDecimalUtil.add(bc.getRepaymentAccount(), difference));
		bc.setInterest(BigDecimalUtil.add(bc.getInterest(), difference));
		merge(bc);
	}
	
	@Override
    public double sumInterestRate(long tenderId) {
        StringBuffer jpql = new StringBuffer("SELECT SUM(p1.interest_rate) FROM rd_borrow_collection p1 WHERE p1.tender_id = ?1");
        Query q = em.createNativeQuery(jpql.toString()).setParameter(1, tenderId);
        double interest = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            interest = Double.parseDouble(obj.toString());
        }
        return interest;
    }
	
	@Override
	public BorrowCollection getNextCollectionByBorrowId(long borrowId, long userId) {
		String jpql = "from BorrowCollection bc where bc.status=0  and repaymentTime >= now() and bc.borrow.id= ?1 and bc.user.userId= ?2 order by repaymentTime asc";
		Query query =em.createQuery(jpql);
		query.setParameter(1, borrowId);
		query.setParameter(2, userId);
		List<BorrowCollection> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Date getLastRepayDateByBorrowId(long borrowId) {
		String sql = "select max(bc.repaymentTime) from BorrowCollection bc where bc.borrow.id= :borrowId ";
        Query query = em.createQuery(sql);
        query.setParameter("borrowId", borrowId);
		Object ret = query.getSingleResult();
		if (ret != null){
			return (Date)ret;
		}
		return null;
	}
	
	@Override
	public boolean isLatedByBorrowId(long borrowId) {
		String sql = "select count(bc.id) from BorrowCollection bc where bc.borrow.id= :borrowId and ((bc.status=0 and bc.repaymentTime < now()) or (bc.status=1 and bc.repaymentTime < bc.repaymentYesTime)) ";
        Query query = em.createQuery(sql);
        query.setParameter("borrowId", borrowId);
		Object ret = query.getSingleResult();
		int count = 0;
		if (ret != null){
			count = Integer.parseInt(ret.toString());
		}
		return (count>0);
	}

	@Override
	public double sumInterestFeeByTender(BorrowTender t) {
		StringBuffer jpql = new StringBuffer("SELECT SUM(p1.manage_fee) FROM rd_borrow_collection p1 WHERE p1.tender_id= ?1");
		Query q = em.createNativeQuery(jpql.toString()).setParameter(1, t.getId());
		double interestFee = 0;
		Object obj = q.getSingleResult();
		if (obj != null) {
			interestFee = Double.parseDouble(obj.toString());
		}
		return interestFee;
	}
	
	@Override
	public Object[] getBorrowCollectionList(long tenderId) {
		StringBuffer buffer = new StringBuffer("select sum(capital),sum(interest + interest_rate) from BorrowCollection where tender.id = :tenderid and status = 0");
		 Query query = em.createQuery(buffer.toString());
	     query.setParameter("tenderid", tenderId);
	     Object[] obj = (Object[])query.getSingleResult();
	     return obj;
	}
	
	@Override
	public double getTotalCapitalByTenderId(long tenderId, long userId) {
		String jpql = "SELECT SUM(capital-bond_capital) FROM rd_borrow_collection WHERE tender_id = ? and user_id=?";
        Query q = em.createNativeQuery(jpql).setParameter(1, tenderId).setParameter(2, userId);
        double capital = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
        	capital = Double.parseDouble(obj.toString());
        }
        return capital;
	}
	
	@Override
	public List<BorrowCollection> getCurrentRepayPlanByModel(BorrowTenderModel model) {
		String jpql = "SELECT * FROM rd_borrow_collection WHERE borrow_id = ? and user_id=? and status=0 and repayment_time != \"\" and period=(select min(period) from rd_borrow_collection where borrow_id=? and user_id=? and status=0)";
        Query q = em.createNativeQuery(jpql, BorrowCollection.class)
        		.setParameter(1, model.getBorrowId())
        		.setParameter(2, model.getUser().getUserId())
        		.setParameter(3, model.getBorrowId())
        		.setParameter(4, model.getUser().getUserId())
        		.setFirstResult((model.getPage() - 1) * model.getSize())
        		.setMaxResults(model.getSize());
		return q.getResultList();
	}

	@Override
	public double getRepaymentYesInterest(String startTime, String endTime) {
		StringBuffer sql = new StringBuffer("SELECT SUM(interest) FROM rd_borrow_collection where status = 1 ");
		if(StringUtil.isNotBlank(startTime)){
			sql.append(" AND repayment_yes_time >= :startTime");
		}
		if(StringUtil.isNotBlank(endTime)){
			sql.append(" AND repayment_yes_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if(StringUtil.isNotBlank(startTime)){
			query.setParameter("startTime", startTime);
		}
		if(StringUtil.isNotBlank(endTime)){
			query.setParameter("endTime", endTime);
		}
		double interest = 0;
        Object obj = query.getSingleResult();
        if (obj != null) {
        	interest = Double.parseDouble(obj.toString());
        }
        return interest;
	}

	@Override
	public BorrowCollection getBorrowCollectionByTenderId(long id) {
		BorrowCollection borrowCollection = super.findObjByProperty("tender.id", id);
		if (borrowCollection != null) {
			return borrowCollection;
		}
		return null;
	}
    
}
