package com.rongdu.p2psys.nb.borrow.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.model.BorrowProfitModel;
import com.rongdu.p2psys.nb.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.nb.borrow.util.Calculate;
import com.rongdu.p2psys.user.domain.User;

@Repository("theBorrowCollectionDao")
public class BorrowCollectionDaoImpl extends BaseDaoImpl<BorrowCollection> implements BorrowCollectionDao
{

	public double netProfit(User user)
	{
		double collection = borrowCollection(user);
		double ppfund = ppfundEaring(user);
		
		return (collection+ppfund);
	}

	public double borrowCollection(User user)
	{
		StringBuffer buffer = new StringBuffer();
		
//		buffer.append(" select (sum(interest - manageFee) + interestRate)");
//		buffer.append(" from BorrowCollection bc ");
//		buffer.append("  where bc.status = 1  and bc.user.userId in ");
//		buffer.append(" (select userId from User where bindId= ?1)  ");
//		
//		Query q = em.createQuery(buffer.toString()).setParameter(1, user.getBindId());
//		Object ret = q.getSingleResult();
//		
//		if (ret == null)
//		{
//			return 0;
//		}
//		return Double.valueOf(ret.toString());
			double disp = disposable(user);
			double corpus = equalCorpusAndInterest(user);
			double interest = equalInterest(user);
			double mid= midTerm(user);
		return (disp+corpus+interest+mid);
	}
	
	public double set(User user){
		return 0;
	}
	
	//一次性付款
	public double disposable(User user){
		List<BorrowProfitModel> list = doSql(user, 0, 2,0);//  未还
		List<BorrowProfitModel> doneList = doSql(user,1,2,0);//已还
		double dayMark = Calculate.dayMark(list);
		double mothMark = Calculate.mothMark(list);
		double repay = Calculate.Repayment(doneList);
		return (dayMark+mothMark+repay);
	}
	
	//等额本息
	public double equalCorpusAndInterest(User user){
		List<BorrowProfitModel> list = doSql(user, 0, 1,1);//  未还
		List<BorrowProfitModel> doneList = doSql(user,1,1,0);//已还
		double earning = Calculate.equalInterest(list);
		double repay = Calculate.Repayment(doneList);
		return (repay+ earning);
	}
	
	//等息
	public double equalInterest(User user){
		List<BorrowProfitModel> list = doSql(user,0,3,1);//未还
		List<BorrowProfitModel> doneList = doSql(user,1,3,0);//已还
		double earning = Calculate.equalInterest(list);
		double repay = Calculate.Repayment(doneList);
		return (repay+ earning);
	}
	
	//中期
	public double midTerm(User user){
		List<BorrowProfitModel> list = doSql(user,0,4,0);//未还
		List<BorrowProfitModel> doneList = doSql(user,1,4,0);//已还
		double earning = Calculate.midInterest(list);
		double repay = Calculate.Repayment(doneList);
		return (repay+ earning);
	}
	
	/***
	 * 
	 * @param user 用户信息
	 * @param status 0:未还 1:已还
	 * @param style 1:等额本息 2:一次性还款 3:等息 4:中期
	 * @param type 1:加分组条件
	 * @return
	 */
	public List<BorrowProfitModel> doSql(User user,int status,int style,int type){
		StringBuffer sb = new StringBuffer();
		sb.append(" select bc.repayment_time as repaymentTime,bc.interest as interest,CONVERT(b.borrow_time_type,SIGNED) as borrowTimeType,case when b.review_time is null then bt.add_time else b.review_time end as reviewTime,b.time_limit as timeLimit,b.middle_day as middleDay,bc.interest_rate from rd_borrow_collection as bc ");
		sb.append(" left join rd_borrow as b on b.id = bc.borrow_id ");
		sb.append(" left join rd_borrow_tender as bt on bt.id = bc.tender_id ");
		sb.append(" where b.`status`in (6,7,8) and bc.user_id = ?1 and bc.status=?2 and b.style =?3");
		if(type!=0){
			sb.append(" group by bc.tender_id");
		}
		Query query = em.createNativeQuery(sb.toString()).setParameter(1, user.getBindId())
				.setParameter(2,status).setParameter(3, style);
        List<Object[]> list = query.getResultList();
        List<BorrowProfitModel> modelList = new ArrayList<BorrowProfitModel>();
        for (Object[] o : list) {
        		BorrowProfitModel modle = new BorrowProfitModel();
        		modle.setRepaymentTime(o[0]==null?null:(Date)o[0]);
        		modle.setInterest(o[1]==null?null:Double.valueOf(o[1].toString()));
        		modle.setBorrowTimeType(o[2]==null?null:Integer.valueOf(o[2].toString()));
        		modle.setReviewTime(o[3]==null?null:(Date)o[3]);
        		modle.setTimeLimit(o[4]==null?null:Integer.valueOf(o[4].toString()));
        		modle.setMiddleDay(o[5]==null?null:Integer.valueOf(o[5].toString()));
        		modle.setInterestRate(o[6]==null?null:Double.valueOf(o[6].toString()));
        		modelList.add(modle);
        }
		return modelList;
	}

	public double ppfundEaring(User user)
	{
		StringBuffer buffer = new StringBuffer();
//		buffer.append("select sum(p.interest) from rd_ppfund_in p  "
//				+ "where p.is_out = 1 and  p.user_id in  (select user_id from rd_user where bind_id=:bindId) ");
		
		buffer.append(" select sum(e.money)  ");
		buffer.append(" from rd_ppfund_earnings e ");
		buffer.append(" where   e.user_id in ");
		buffer.append(" (select user_id from rd_user where bind_id=:bindId) ");//只有已转出才算入累计收益
		Query q = em.createNativeQuery(buffer.toString()).setParameter("bindId", user.getBindId());
		Object ret = q.getSingleResult();
		
		if (ret == null)
		{
			return 0;
		}
			
		return Double.parseDouble(ret.toString());
	}

	@Override
	public double getInterestByUser(User user) {
		StringBuffer jpql = new StringBuffer("SELECT SUM(p1.interest-p1.bond_interest+p1.interest_rate) FROM rd_borrow_collection p1,");
        jpql.append(" rd_borrow_repayment p2,rd_borrow p3 WHERE p1.borrow_id = p2.borrow_id AND p1.borrow_id = p3.id AND p3.status in(3,6,7,8)"
        		+ " AND p1.period = p2.period");
        jpql.append(" AND p1.status = 0 AND p1.user_id in(select u.user_id from rd_user as u where u.bind_id=:bindId )");
        Query q = em.createNativeQuery(jpql.toString()).setParameter("bindId", user.getBindId());
        double interest = 0;
        Object obj = q.getSingleResult();
        if (obj != null) {
            interest = Double.parseDouble(obj.toString());
        }
        return interest;
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

	@Override
	public double getFloatRate(double tenderId, double userId) {
		StringBuffer jpql = new StringBuffer("SELECT SUM(interest+float_income) FROM rd_borrow_collection p1 where p1.user_id =:userId and p1.tender_id =:tenderId");
		Query q = em.createNativeQuery(jpql.toString()).setParameter("userId", userId).setParameter("tenderId", tenderId);
        Object ret = q.getSingleResult();
        if(ret == null) return 0;
        return Double.parseDouble(ret.toString());
	}

}
