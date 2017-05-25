package com.rongdu.p2psys.borrow.dao.jdbc;

import java.math.BigDecimal;
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
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;


/**
 * 还款
 * @author cx
 * @version 2.0
 * 2014-11-13
 */
@Repository("borrowRepaymentDao")
public class BorrowRepaymentDaoImpl extends BaseDaoImpl<BorrowRepayment> implements BorrowRepaymentDao {

	@Override
	public BorrowRepayment find(long id) {
		BorrowRepayment borrowRepayment = super.find(id);
		if (borrowRepayment.getStatus() == 1) {
			throw new BorrowException("该还款计划已经还款", 1);
		}
		return borrowRepayment;
	}

	@Override
	public void updateBorrowRepaymentByStatus(BorrowRepayment repay) {
		StringBuffer buffer = new StringBuffer(
				"UPDATE rd_borrow_repayment SET status = :status,repayment_yes_time = :repaymentYesTime,");
		buffer.append("type = :type, repayment_user_id = :repaymentUserId,");
		buffer.append("repayment_yes_account = :repaymentYesAccount WHERE ( status = 0 or status = 3 or status = 4 or status = 5) AND id = :id");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter("status", repay.getStatus());
		query.setParameter("repaymentYesTime", repay.getRepaymentYesTime());
		query.setParameter("repaymentYesAccount", repay.getRepaymentYesAccount());
		query.setParameter("type", repay.getType());
		query.setParameter("repaymentUserId", repay.getRealRepayer().getUserId());
		query.setParameter("id", repay.getId());
		query.executeUpdate();
	}

	@Override
	public boolean hasRepaymentAhead(int period, long borrowId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("period", Operators.LT, period);
		param.addParam("borrow.id", borrowId);
		param.addParam("status", 0);
		int count = super.countByCriteria(param);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void updateStatus(int status, int status_, long repayId) {
		String jpql = "UPDATE rd_borrow_repayment SET status = :status WHERE status = :alreadyStatus  AND (web_status = 3 OR web_status = 1) AND id = :id";
		Query query = em.createNativeQuery(jpql);
		query.setParameter("id", repayId);
		query.setParameter("status", status);
		query.setParameter("alreadyStatus", status_);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new BorrowException("该期借款已经还款，请勿重复操作！", 1);
		}else{
		    // 更新缓存
	        em.refresh(this.find(repayId));
		}
	}

	@Override
	public double getUserBorrowTotal(long userId) {
		StringBuffer buffer = new StringBuffer(
				"SELECT SUM(b.capital) FROM BorrowRepayment b WHERE b.user.userId = :userId group by b.user.userId");
		Query query = em.createQuery(buffer.toString());
		query.setParameter("userId", userId);
		return Double.parseDouble(query.getSingleResult().toString());
	}
	@Override
	public double getUserBorrowRepayTotal(long userId) {
		StringBuffer buffer = new StringBuffer(
				"SELECT SUM(b.repaymentAccount+b.lateInterest)  FROM BorrowRepayment b WHERE b.user.userId = :userId and status in (:status1,:status2)");
		Query query = em.createQuery(buffer.toString());
		query.setParameter("userId", userId);
		query.setParameter("status1", 0);
		query.setParameter("status2", 2);
		Object ret =query.getSingleResult();
		if(ret == null) return 0;
		return Double.parseDouble(ret.toString());
	}

	@Override
	public BorrowRepayment find(long borrowId, int period) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.id", borrowId);
		param.addParam("period", period);
		return super.findByCriteriaForUnique(param);
	}

	@Override
	public double getRemainderCapital(long borrowId) {
		String jpql = "select sum(capital) from BorrowRepayment br where br.status=0  and br.borrow.id= ?1 ";
		Query q = em.createQuery(jpql).setParameter(1, borrowId);
		Object ret = q.getSingleResult();
		if(ret == null) return 0;
		return Double.parseDouble(ret.toString());
	}

	@Override
	public double getwaitRpayInterest(long borrowId, int period) {
		String jpql = "select sum(interest) from BorrowRepayment br where br.status=0  and br.borrow.id= ?1  and (br.period >=?2 and br.period <=?3)";
		Query q = em.createQuery(jpql).setParameter(1, borrowId);
		q.setParameter(2, period);
		q.setParameter(3, period + 1);
		Object ret = q.getSingleResult();
		if(ret == null) return 0;
		return Double.parseDouble(ret.toString());
	}

	@Override
	public double getRemainderInterest(long borrowId) {
		String jpql = "select sum(interest) from BorrowRepayment br where br.status=0  and br.borrow.id= ?1 ";
		Query q = em.createQuery(jpql).setParameter(1, borrowId);
		Object ret = q.getSingleResult();
		if(ret == null) return 0;
		return Double.parseDouble(ret.toString());
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BorrowRepaymentModel getRepayStatistics(long userId){
		BorrowRepaymentModel bm = new BorrowRepaymentModel();
		HashMap data = new HashMap();
		List list = new ArrayList();
		//获取本月待还sql
		String jpqlMonth = "select ifnull(sum(repayment_account),0),count(id) from rd_borrow_repayment br where br.status=0  and br.user_id= ?1 "
				+ "and month(br.repayment_time)=month(now()) AND year(br.repayment_time)=year(now())";
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
		//获取下月待还sql
		String jpqlNextMonth = "select ifnull(sum(repayment_account),0),count(id) from rd_borrow_repayment br where br.status=0  and br.user_id= ?1 "
				+ " and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( br.repayment_time, '%Y%m' ) ) =-1";
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
		String jpqlOther = "select ifnull(sum(repayment_account),0),count(id) from rd_borrow_repayment br where br.status=0  and br.user_id= ?1 "
				+ " and (PERIOD_DIFF( date_format( br.repayment_time, '%Y%m' ) ,  date_format( now( ) , '%Y%m' )) >1 "
				+ "or PERIOD_DIFF( date_format( br.repayment_time, '%Y%m' ) ,  date_format( now( ) , '%Y%m' )) < 0)";
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
		bm.setRepayData(data);
		return bm;
	}
	@Override
	public double getRepayByMonth(int month, long userId) {
		String jpql = "select sum(repaymentAccount) from BorrowRepayment br where br.status=0 and br.user.userId= ?1 and month(br.repaymentTime)=?2 AND year(br.repaymentTime)=year(now())";
		Query q = em.createQuery(jpql).setParameter(1, userId).setParameter(2, month);
		Object ret = q.getSingleResult();
		if (ret == null) {
			return 0;
		}
		return (Double) ret;
	}
	@SuppressWarnings("unchecked")
	@Override
	public BorrowRepayment getNextRepayByUserId(long userId) {
		String jpql = "from BorrowRepayment br where br.status=0 and br.user.userId= ?1 and repaymentTime >= now() order by repaymentTime asc";
		Query query = em.createQuery(jpql);
		query.setParameter(1, userId);
		List<BorrowRepayment> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public  BorrowRepaymentModel getBRMByCollectTime(BorrowRepaymentModel bm, Date nextRepayTime, long userId){
		String jpql = "select ifnull(sum(repayment_account),0),count(id) from rd_borrow_repayment br where br.status=0  and br.user_id= ?1 "
				+ " and (PERIOD_DIFF( date_format( br.repayment_time, '%Y%m%d' ) ,  date_format( ?2 , '%Y%m%d' )) =0)";
		Query q = em.createNativeQuery(jpql).setParameter(1, userId).setParameter(2, nextRepayTime);
		List list = new ArrayList();
		list = q.getResultList();
		double account = 0;
		int count = 0;
		if (list != null && list.size() > 0) {
			Iterator iterator = list.iterator(); 
			while (iterator.hasNext()) { 
				Object[] row = (Object[]) iterator.next();	
				account= Double.parseDouble(row[0].toString());
				count = Integer.parseInt(row[1].toString());
			}
		}
		bm.setNextRepayAccount(account);
		bm.setNextRepayCount(count);
		return bm;
	}
	@Override
	public void updateBorrowRepaymentStatusAndWebStatus(
			BorrowRepayment borrowRepayment) {
		StringBuffer buffer = new StringBuffer(
				"UPDATE rd_borrow_repayment SET status = ?1,repayment_yes_time = ?2,");
		buffer.append("web_status = ?3 WHERE status = 0 AND borrow_id = ?4");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter(1, 1);
		query.setParameter(2, new Date());
		query.setParameter(3, 1);
		query.setParameter(4, borrowRepayment.getBorrow().getId());
		query.executeUpdate();
	}
	
	@Override
	public void updateBorrowRepaymentStatus(BorrowRepayment repay) {
		StringBuffer buffer = new StringBuffer(
				"UPDATE rd_borrow_repayment SET status = :status,repayment_yes_time = :repaymentYesTime,");
		buffer.append("repayment_yes_account = :repaymentYesAccount WHERE status = 2 AND id = :id");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter("status", repay.getStatus());
		query.setParameter("repaymentYesTime", repay.getRepaymentYesTime());
		query.setParameter("repaymentYesAccount", repay.getRepaymentYesAccount());
		query.setParameter("id", repay.getId());
		query.executeUpdate();
	}

	@Override
	public double getWaitInterest(long borrowId, int period) {
		String jpql = "select sum(interest) from BorrowRepayment br where br.status = 0  and br.borrow.id = ?1  and br.period >= ?2";
		Query q = em.createQuery(jpql).setParameter(1, borrowId);
		q.setParameter(2, period);
		Object ret = q.getSingleResult();
		if(ret == null) return 0;
		return (Double)ret;
	}

    @SuppressWarnings("unchecked")
    @Override
    public PageDataList<BorrowRepayment> getList(BorrowRepaymentModel model) {
        StringBuffer jpql = new StringBuffer("SELECT p1.* FROM rd_borrow_repayment p1, rd_borrow p2, rd_user p3");
        jpql.append(" WHERE p1.borrow_id = p2.id AND p2.user_id = p3.user_id");
        long vouchId = 0;
        long borrowId = 0;
        long userId = 0;
        String borrowName = "";
        String userName = "";
        int status = 99;
        Date startTime1 = null;
        Date endTime1 = null;
        Date startTime2 = null;
        Date endTime2 = null;
        Date repaymentTime = null;
        int type = -1;
        if (model != null) {
            if (model.getVouchFirm() != null) {
                jpql.append(" AND p2.vouch_id = :vouchId");
                vouchId = model.getVouchFirm().getUserId();
            } else {
                if (model.getBorrowId() > 0) {
                    jpql.append(" AND p1.borrow_id = :borrowId");
                    borrowId = model.getBorrowId();
                }
                if (model.getUser() != null && model.getUser().getUserId() > 0) {
                    jpql.append(" AND p1.user_id = :userId");
                    userId = model.getUser().getUserId();
                }
                if(model.getUserName() != null && model.getUserName().length() > 0){
                    userName = model.getUserName();
                    jpql.append(" AND p3.user_name LIKE :userName");
                }
                if(model.getBorrowName() != null && model.getBorrowName().length() > 0 ){
                    borrowName = model.getBorrowName();
                    jpql.append(" AND p2.name LIKE :borrowName");
                }
                if (StringUtil.isNotBlank(model.getStartTime())) {
                    startTime1 = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
                    jpql.append(" AND p1.repayment_time >= :startTimeA");
                }
                if (StringUtil.isNotBlank(model.getEndTime())) {
                    endTime1 = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
                    jpql.append(" AND p1.repayment_time <= :endTimeA");
                }
                if(model.getTime() > 0){
                    if(model.getStatus() == 0){// 如果状态为待还，则查询时间为当前时间加上model.getTime()，且平台垫付也需统计(status=2)
                        startTime1 = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                        jpql.append(" AND p1.status in(0,2) AND p1.repayment_time >= :startTimeA AND p1.repayment_time <= :endTimeA");
                        if (model.getTime() == 7) {
                            endTime1 = DateUtil.rollDay(startTime1, 7);
                            endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                        } else if (model.getTime() > 0 && model.getTime() < 4){
                            endTime1 = DateUtil.rollMon(startTime1, model.getTime());
                            endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                        }
                    }else if(model.getStatus() == 99){
                        jpql.append(" AND ( (p1.status = 0 AND p1.repayment_time >= :startTimeA AND p1.repayment_time <= :endTimeA)");
                        jpql.append(" OR ( p1.status != 0 AND p1.repayment_time >= :startTimeB AND p1.repayment_time <= :endTimeB ) )");
                        startTime1 = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                        endTime2 = startTime1;
                        if (model.getTime() == 7) {
                            // 待还信息时间处理
                            endTime1 = DateUtil.rollDay(startTime1, 7);
                            endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                            // 已还信息时间处理
                            startTime2 = DateUtil.rollDay(endTime2, -7);
                            startTime2 = DateUtil.getDayEndTime(startTime2.getTime() / 1000);
                            
                        } else if (model.getTime() > 0 && model.getTime() < 4){
                            // 待还信息时间处理
                            endTime1 = DateUtil.rollMon(startTime1, model.getTime());
                            endTime1 = DateUtil.getDayEndTime(endTime1.getTime() / 1000);
                            // 已还信息时间处理
                            startTime2 = DateUtil.rollMon(endTime2, -model.getTime());
                            startTime2 = DateUtil.getDayEndTime(startTime2.getTime() / 1000);
                        } 
                    }else if(model.getStatus() != 99){// 如果状态为已还，则查询时间为当前时间减去model.getTime()
                        status = model.getStatus();
                        endTime1 = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                        jpql.append(" AND p1.status = :status AND p1.repayment_time >= :startTimeA AND p1.repayment_time <= :endTimeA");
                        if (model.getTime() == 7) {
                            startTime1 = DateUtil.rollDay(endTime1, -7);
                            startTime1 = DateUtil.getDayEndTime(startTime1.getTime() / 1000);
                        } else if (model.getTime() > 0 && model.getTime() < 4){
                            startTime1 = DateUtil.rollMon(endTime1, -model.getTime());
                            startTime1 = DateUtil.getDayEndTime(startTime1.getTime() / 1000);
                        }
                    }
                }else if(model.getTime() <= 0 && model.getStatus() != 99){
                    status = model.getStatus();
                    if(status == 0){//如果status=0，则同样需要将网站垫付，逾期未还的查询出来
                    	jpql.append(" AND p1.status in (0,2)");
                    }else{
                    	jpql.append(" AND p1.status = :status");
                    }
                }
            }
            if (model.isLate()) {
                if (model.getType() != 0) {
                    jpql.append(" AND p2.type = :type");
                    type = model.getType();
                }
                repaymentTime = new Date();
                jpql.append(" AND p1.repayment_time <= :repaymentTime");
                jpql.append(" AND p2.type <> 110 AND p2.type <> 101");
                jpql.append(" AND (p1.repayment_yes_time > p1.repayment_time OR p1.repayment_yes_account = 0 )");
            }
        }
        if (model.getStatus() == 0) {
            jpql.append(" ORDER BY p1.repayment_time ASC");
        } else if (model.getStatus() == 1) {
        	jpql.append(" ORDER BY p1.repayment_yes_time DESC");
        } else {
        	jpql.append(" ORDER BY FIELD(p1.status,5,0,1,2,3,4), p1.repayment_yes_time DESC, repayment_time ASC");
        }
        Query query = em.createNativeQuery(jpql.toString(), BorrowRepayment.class);
        if (userId > 0) {
            query.setParameter("userId", userId);
        }
        if (status != 99 && status != 0) {
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
        if(userName.length() > 0){
            query.setParameter("investUserName", "%"+userName+"%");
        }
        if(borrowName.length() > 0){
            query.setParameter("borrowName", "%"+borrowName+"%");
        }
        if (repaymentTime != null) {
            query.setParameter("repaymentTime", repaymentTime);
        }
        if (type > -1) {
            query.setParameter("type", type);
        }
        if (vouchId > 0) {
            query.setParameter("vouchId", vouchId);
        }
        if (borrowId > 0) {
            query.setParameter("borrowId", borrowId);
        }
        PageDataList<BorrowRepayment> pageDataList = new PageDataList<BorrowRepayment>();
        Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
        if(model.getPage() == 0){
            model.setPage(1);
        }
        query.setFirstResult((model.getPage() - 1) * model.getSize());
        query.setMaxResults(model.getSize());
        List<BorrowRepayment> list = query.getResultList();
        pageDataList.setList(list);
        pageDataList.setPage(page);
        return pageDataList;
    }

    @Override
    public int getCurrPeriod(long borrowId) {
        StringBuffer jpql =new StringBuffer("SELECT MIN(p1.period) FROM rd_borrow_repayment p1 WHERE p1.borrow_id = :borrowId AND (p1.status = 0 OR p1.status = 5)");
        Query q = em.createNativeQuery(jpql.toString()).setParameter("borrowId", borrowId);
        int currPeriod = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            currPeriod = Integer.parseInt(obj.toString());
        }
        return currPeriod;
    }
    
    @Override
    public int getOverdueCount(long userId) {
        String sql = "SELECT COUNT(t2.id) FROM rd_borrow t1, rd_borrow_repayment t2 WHERE t1.vouch_id=:vouch_id AND t1.status IN ('6','7') AND t1.id = t2.borrow_id AND t2.status = '0' AND t2.repayment_time < NOW()";
        Query query = em.createNativeQuery(sql);
        query.setParameter("vouch_id", userId);
        Object count = query.getSingleResult();
        if(count != null){
            return Integer.parseInt(count.toString());
        }
        return 0;
    }
    
    @Override
    public List<BorrowRepayment> getGuaranteeingList(String userId) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<BorrowRepayment> getUrgeGuaranteeList(String userId) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public PageDataList<BorrowRepayment> getOverdueGuaranteeList(BorrowModel model) {
        StringBuffer sql = new StringBuffer("SELECT t2.* FROM rd_borrow t1, rd_borrow_repayment t2, rd_user t3 WHERE t1.vouch_id=:vouch_id AND t1.status IN ('6','7') AND t1.user_id = t3.user_id AND t1.id = t2.borrow_id AND t2.status = '0' AND t2.repayment_time < NOW()");
        
        String userName = "";
        if (StringUtil.isNotBlank(model.getUserName())) {
            userName = model.getUserName();
            sql.append(" AND t3.user_name LIKE :userName");
        }
        Date d = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
        Date startTime = null;
        Date endTime = null;
        if (model.getTime() == 7) {
            sql.append(" AND t2.add_time >= :startTime");
            sql.append(" AND t2.add_time <= :endTime");
            startTime = DateUtil.rollDay(d, -7);
            endTime = d;
        } else if (model.getTime()>0 && model.getTime()<4){
            sql.append(" AND t2.add_time >= :startTime");
            sql.append(" AND t2.add_time <= :endTime");
            startTime = DateUtil.rollMon(d, -model.getTime());
            endTime = d;
        }
        if (StringUtil.isNotBlank(model.getStartTime())) {
            startTime = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
            sql.append(" AND t2.add_time > :startTime");
        }
        if (StringUtil.isNotBlank(model.getEndTime())) {
            endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
            sql.append(" AND t2.add_time < :endTime");
        }

        Query query = em.createNativeQuery(sql.toString(), BorrowRepayment.class);
        query.setParameter("vouch_id", model.getVouchFirmId());
        
        if (startTime != null) {
            query.setParameter("startTime", startTime);
        }
        if (endTime != null) {
            query.setParameter("endTime", endTime);
        }
        
        if(userName.length() > 0){
            query.setParameter("userName", "%"+userName+"%");
        }

        PageDataList<BorrowRepayment> pageDataList = new PageDataList<BorrowRepayment>();
        Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
        if(model.getPage() == 0){
            model.setPage(1);
        }
        
        query.setFirstResult((model.getPage() - 1) * model.getSize());
        query.setMaxResults(model.getSize());
        List<BorrowRepayment> list = query.getResultList();
        pageDataList.setList(list);
        pageDataList.setPage(page);
        return pageDataList; 
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public PageDataList<BorrowRepayment> getCompensatedList(BorrowModel model) {
        
        StringBuffer sql = new StringBuffer("SELECT t1.* FROM rd_borrow_repayment t1, rd_user t2 WHERE t1.user_id = t2.user_id AND t1.status = '1' AND t1.type = '2' AND t1.repayment_user_id = :vouch_id ");
        
        String userName = "";
        if (StringUtil.isNotBlank(model.getUserName())) {
            userName = model.getUserName();
            sql.append(" AND t2.user_name LIKE :userName");
        }
        Date d = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
        Date startTime = null;
        Date endTime = null;
        if (model.getTime() == 7) {
            sql.append(" AND t1.add_time >= :startTime");
            sql.append(" AND t1.add_time <= :endTime");
            startTime = DateUtil.rollDay(d, -7);
            endTime = d;
        } else if (model.getTime()>0 && model.getTime()<4){
            sql.append(" AND t1.add_time >= :startTime");
            sql.append(" AND t1.add_time <= :endTime");
            startTime = DateUtil.rollMon(d, -model.getTime());
            endTime = d;
        }
        if (StringUtil.isNotBlank(model.getStartTime())) {
            startTime = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
            sql.append(" AND t1.add_time > :startTime");
        }
        if (StringUtil.isNotBlank(model.getEndTime())) {
            endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
            sql.append(" AND t1.add_time < :endTime");
        }

        Query query = em.createNativeQuery(sql.toString(), BorrowRepayment.class);
        query.setParameter("vouch_id", model.getVouchFirmId());
        
        if (startTime != null) {
            query.setParameter("startTime", startTime);
        }
        if (endTime != null) {
            query.setParameter("endTime", endTime);
        }
        
        if(userName.length() > 0){
            query.setParameter("userName", "%"+userName+"%");
        }
        
        PageDataList<BorrowRepayment> pageDataList = new PageDataList<BorrowRepayment>();
        Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
        if(model.getPage() == 0){
            model.setPage(1);
        }
        
        query.setFirstResult((model.getPage() - 1) * model.getSize());
        query.setMaxResults(model.getSize());
        List<BorrowRepayment> list = query.getResultList();
        pageDataList.setList(list);
        pageDataList.setPage(page);
        return pageDataList; 
    }

	@Override
	public double getSumRepayAccount(long borrowId, int status) {
		String sql = "select sum(repayment_account) from rd_borrow_repayment  WHERE borrow_id = ? and status = ? and web_status =1";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, borrowId);
        query.setParameter(2, status);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public List<BorrowRepayment> doLate() {
		QueryParam param = QueryParam.getInstance();
		SearchFilter orFilter1 = new SearchFilter("status", Operators.EQ, 0);
        SearchFilter orFilter2 = new SearchFilter("status", Operators.EQ, 2);//网站垫付
        param.addOrFilter(orFilter1, orFilter2);
		return findByCriteria(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowRepayment> middleReapy() {
		String sql = "select * from rd_borrow_repayment br, rd_borrow b where b.id=br.borrow_id and (b.type=119 or b.type=122) and br.status=0 and b.middle_day > 0 and TO_DAYS(b.review_time) = TO_DAYS(date_sub(now(), interval  b.middle_day day))"
				+ " and b.borrow_time_type=1";
		Query query = em.createNativeQuery(sql, BorrowRepayment.class);
		return query.getResultList();
	}

	@Override
	public double getRepaymentNoByDate(String date) {
		String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select sum(repayment_account) from rd_borrow_repayment where date_format(repayment_time,'"+dateFormat+"') = ? and status = 0";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        double count = 0;
        if (obj != null) {
            count = ((BigDecimal) obj).doubleValue();
        }
        return count;
	}

	@Override
	public double getRepaymentYesByDate(String date) {
		String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select sum(repayment_yes_account) from rd_borrow_repayment where date_format(repayment_yes_time,'"+dateFormat+"') = ? and status = 1";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        double count = 0;
        if (obj != null) {
            count = ((BigDecimal) obj).doubleValue();
        }
        return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowRepayment> autoRepayList() {
		String date = DateUtil.dateStr2(new Date()) + " 23:59:59";
		String sql = "select * from rd_borrow_repayment where status = 0 and repayment_time < '" + date +"'" ;
		Query query = em.createNativeQuery(sql, BorrowRepayment.class);
		return query.getResultList();
	}
    

}
