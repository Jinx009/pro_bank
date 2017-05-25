package com.rongdu.p2psys.borrow.dao.jdbc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.NewTenderModel;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.model.RankModel;
import com.rongdu.p2psys.core.rule.IndexRuleCheck;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.exception.UserException;

@Repository("borrowTenderDao")
public class BorrowTenderDaoImpl extends BaseDaoImpl<BorrowTender> implements BorrowTenderDao {

	@Override
	public PageDataList<BorrowTenderModel> list(BorrowTenderModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (model.getUser() != null && model.getUser().getUserId() > 0) {
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (model.getStatus() != 99) {
				if (model.getStatus() == 98) {// 前台查询投资人的所有投资
					param.addParam("status", Operators.NOTEQ, -1);
					param.addParam("status", Operators.NOTEQ, 0);
					param.addParam("status", Operators.NOTEQ, 2);
				} else if (model.getStatus() == 49) { // 4/49复审未通过;
					SearchFilter orFilter1 = new SearchFilter("status", Operators.EQ, 4);
					SearchFilter orFilter2 = new SearchFilter("status", Operators.EQ, 49);
					param.addOrFilter(orFilter1, orFilter2);
				} else if (model.getStatus() == 59) { // 5/59用户取消;
					SearchFilter orFilter1 = new SearchFilter("status", Operators.EQ, 5);
					SearchFilter orFilter2 = new SearchFilter("status", Operators.EQ, 59);
					param.addOrFilter(orFilter1, orFilter2);
				} else {
					param.addParam("status", model.getStatus());
				}
			}
//			if (StringUtil.isNotBlank(model.getBorrowName())) {
//				param.addParam("borrow.name", Operators.EQ, model.getBorrowName());
//			}HCB
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.DESC, "id");

		PageDataList<BorrowTender> pageDataList = findPageList(param);
		PageDataList<BorrowTenderModel> pageDateList_ = new PageDataList<BorrowTenderModel>();
		List<BorrowTenderModel> list = new ArrayList<BorrowTenderModel>();
		pageDateList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowTender t = (BorrowTender) pageDataList.getList().get(i);
				BorrowTenderModel tm = BorrowTenderModel.instance(t);
				tm.setBorrowName(t.getBorrow().getName());
				tm.setAccountYes(t.getBorrow().getAccountYes());
				tm.setUserName(t.getUser().getUserName());
				tm.setBorrowUserName(t.getBorrow().getUser().getUserName());
				tm.setScales(t.getBorrow().getScales());
				list.add(tm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	/** 修改tender的待收本金和待收利息 **/
	@Override
	public void updateRepayTender(double capital, double interest, long id) {
		String jpql = "UPDATE BorrowTender SET repaymentYesAccount = repaymentYesAccount + :repaymentYesAccount,"
				+ "repaymentYesInterest = repaymentYesInterest + :repaymentYesInterest,waitAccount = waitAccount - :waitAccount,"
				+ "waitInterest = waitInterest - :waitInterest WHERE id=:id";
		Query query = em.createQuery(jpql);
		query.setParameter("repaymentYesAccount", capital);
		query.setParameter("repaymentYesInterest", interest);
		query.setParameter("waitAccount", capital);
		query.setParameter("waitInterest", interest);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public double hasTenderTotalPerBorrowByUserid(long borrowId, long userId) {
		String jpql = "SELECT SUM(account) FROM BorrowTender WHERE borrow.id = :borrowId AND user.userId = :userId";
		Query query = em.createQuery(jpql).setParameter("borrowId", borrowId).setParameter("userId", userId);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Double.parseDouble(query.getSingleResult().toString());
		}
		return 0;
	}

	@Override
	public PageDataList<BorrowTenderModel> list(long borrowId, int page, int size) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.id", borrowId);
		param.addOrder(OrderType.DESC, "addTime");
		if (size != 0) {
	        param.addPage(page, size);
		} else {
		    param.addPage(page);
		}
		PageDataList<BorrowTender> pageDataList = findPageList(param);
		PageDataList<BorrowTenderModel> pageDataList_ = new PageDataList<BorrowTenderModel>();
		List<BorrowTenderModel> list = new ArrayList<BorrowTenderModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowTender tender = (BorrowTender) pageDataList.getList().get(i);
				BorrowTenderModel model = BorrowTenderModel.instance(tender);
				String username = tender.getUser().getUserName();
				String realName=tender.getUser().getRealName();
				model.setUser(null);
				model.setBorrow(null);
				if (size == 0) {
					model.setRealName(realName);
	                model.setUserName(username.charAt(0)+"******"+username.charAt(username.length()-1));
				} else {
					model.setRealName(realName);
	                model.setUserName(username);
				}
				list.add(model);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	/** 首页排行榜 **/
	@SuppressWarnings({ "unchecked" })
	public List<RankModel> getRankList() {
		String selSql = "select p3.user_name as username,sum(p1.account) as tenderMoney from rd_borrow_tender p1"
				+ " left join rd_borrow p2 on p2.id=p1.borrow_id" + " left join rd_user p3 on p3.user_id=p1.user_id"
				+ " where ";
		IndexRuleCheck rankCheck = (IndexRuleCheck) Global.getRuleCheck("index");
		int day = rankCheck.rank.day;
		String daySql = null;
		if (rankCheck.rank.status == 1) {
			daySql = "(p2.borrow_time_type <>1 or p2.time_limit >= " + day + ")";
		} else {
			daySql = "p2.borrow_time_type <> 1";
		}
		String conditSql = "and ((p2.type !=" + Borrow.TYPE_SECOND + " and p2.status in (3,6,7,8)) or (p2.type="
				+ Borrow.TYPE_FLOW + " and p2.status in (1,3,6,7,8)))";
		String groupSql = " group by p3.user_name order by tenderMoney desc";
		StringBuffer sb = new StringBuffer();
		sb.append(selSql).append(daySql).append(conditSql).append(groupSql).append(" limit 0,10");
		Query query = em.createNativeQuery(sb.toString());
		List<Object[]> list = query.getResultList();
		List<RankModel> list_ = new ArrayList<RankModel>();
		for(Object[] obj : list){
			RankModel model = new RankModel();
			model.setUsername(obj[0]+"");
			model.setTenderMoney(NumberUtil.getDouble2(obj[1]+""));
			list_.add(model);
		}
		return list_;
	}

	public int getTenderCountByBorrowid(long id) {
		String sql = "SELECT COUNT(1) FROM rd_borrow_tender AS tender,rd_user AS user WHERE user.userId=tender.userId and tender.borrow_id=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id", id);
		return (Integer) query.getSingleResult();
	}

	public PageDataList<BorrowTenderModel> getTenderListByBorrowid(long id, int page, int pernum) {
		QueryParam param = QueryParam.getInstance();
		String sql = "select t.*,collection.repay_time as repay_time,collection.repay_yes_time as repay_yes_time,collection.repay_account as repay_account,u.userName from rd_borrow_tender t "
				+ "left join rd_borrow_collection collection on t.id=collection.tender_id "
				+ "left join rd_user u on u.userId=t.userId "
				+ "left join rd_borrow borrow on t.borrow_id = borrow.id where t.borrow_id=:borrowId group by t.id";
		param.addParam("borrowId", id);
		param.addPage(page, pernum);
		return this.findPageListBySql(sql, param, BorrowTenderModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NewTenderModel> getNewTenderList() {
		String sql = "SELECT t.* FROM rd_borrow_tender t, rd_user c, rd_borrow b WHERE t.user_id=c.user_id AND "
				+ "t.borrow_id=b.id AND b.status in(1,3,6,7,8) order by t.add_time desc limit 0,5";
		Query query = em.createNativeQuery(sql, BorrowTender.class);
		List<BorrowTender> list = query.getResultList();
		List<NewTenderModel> list_ = new ArrayList<NewTenderModel>();
		for (int i = 0; i < list.size(); i++) {
			BorrowTender tender = list.get(i);
			NewTenderModel model = new NewTenderModel();
			model.setUserName(tender.getUser().getUserName());
			model.setTenderTime(DateUtil.getTimeStr(tender.getAddTime()));
			model.setBorrowId(tender.getBorrow().getId());
			model.setBorrowName(tender.getBorrow().getName());
			model.setTenderMoney(tender.getAccount());
			list_.add(model);
		}
		return list_;
	}

	@Override
	public BorrowTender addBorrowTender(BorrowTender tender) {
		return save(tender);
	}

	public double getUserTenderNum(long userId, Date beginDate, Date endDate) {
		String sql = "select sum(tender.account) from rd_borrow_tender tender, rd_borrow borrow "
				+ "where tender.user_id = :userId and tender.borrow_id = borrow.id and borrow.type="
				+ Borrow.TYPE_SECOND
				+ " and(borrow.status in (1, 3, 6, 8)) and tender.add_time between :startTime and :endTime ";
		long startTime = DateUtil.getTime(DateUtil.getFirstSecIntegralTime(beginDate));
		long endTime = DateUtil.getTime(DateUtil.getLastSecIntegralTime(endDate));
		Query query = em.createNativeQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return (Double) obj;
		}
		return 0;
	}
	
	public int getUserBorrowTenderOrder(long tenderId,long borrowId)
	{
		String sql = " select count(*) from rd_borrow_tender t where t.borrow_id = " +  borrowId + " and t.id <= " + tenderId;
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Integer.parseInt(obj.toString());
		}
		return 1;
	}

	public int getBorrowTenderTimes(long borrowId, long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.borrowId", borrowId);
		param.addParam("user.userId", userId);
		return this.countByCriteria(param);
	}

	public BorrowTender modifyBorrowTender(BorrowTender tender) {
		String sql = "update rd_borrow_tender set repayment_account = :repayment_account,wait_account = :wait_account,interest = :interest,"
				+ "wait_interest = :wait_interest, status= :status,repayment_yes_interest = :repayment_yes_interest where id = :id";
		String[] names = new String[] { "repayment_account", "wait_account", "interest", "wait_interest", "status",
				"repayment_yes_interest", "id" };
		Object[] values = new Object[] { tender.getRepaymentAccount(), tender.getWaitAccount(), tender.getInterest(),
				tender.getWaitInterest(), tender.getStatus(), tender.getRepaymentYesAccount(), tender.getId() };
		int result = this.updateBySql(sql, names, values);
		if (result < 1) {
			return null;
		}
		super.refresh(find(tender.getId()));
		return tender;
	}

	@Override
	public double sumTenderAccount(long userId) {
		String selSql = "select sum(p1.account) as num from rd_borrow_tender p1 left join rd_borrow b on b.id=p1.borrow_id"
				+ "  where ((b.status in(3,6,7,8) and b.type<>"
				+ Borrow.TYPE_FLOW
				+ ") or (b.status in(1,8) and b.type=" + Borrow.TYPE_FLOW + ")) and p1.userId=:userId";
		Query query = em.createNativeQuery(selSql);
		query.setParameter("userId", userId);
		return (Double) query.getSingleResult();
	}

	@Override
	public void updateStatus(long borrowId, int status, int preStatus) {
		String sql = "UPDATE BorrowTender SET status = :status WHERE borrow.id = :borrowId AND status = :preStatus ";
		Query query = em.createQuery(sql);
		query.setParameter("borrowId", borrowId);
		query.setParameter("status", status);
		query.setParameter("preStatus", preStatus);
		int result = query.executeUpdate();
		if (result <= 0) {
			throw new UserException("更新tender记录状态失败", 1);
		}
	}
	
	@Override
    public void updateStatus(long id, int status){
	    String sql = "UPDATE BorrowTender SET status = :status WHERE id = :id  ";
        Query query = em.createQuery(sql);
        query.setParameter("id", id);
        query.setParameter("status", status);
        int result = query.executeUpdate();
        if (result <= 0) {
            throw new UserException("更新tender记录状态失败", 1);
        } 
	}
	@Override
	public int getAutoTenderByUserId(long userId, byte tenderType, long borrowId, int status) {
		String sql = "select count(*) from rd_borrow_tender tender, rd_borrow borrow where borrow.id = tender.borrow_id "
				+ "and borrow.status = :status and tender.user_id = :user_id and tender.borrow_id = :borrow_id and tender.tender_type = :tenderType";
		Query query = em.createNativeQuery(sql);
		query.setParameter("status", status);
		query.setParameter("user_id", userId);
		query.setParameter("borrow_id", borrowId);
		query.setParameter("tenderType", tenderType);
		BigInteger times = (BigInteger) query.getSingleResult();
		return times.intValue();
	}

	@Override
	public void modifyTenderBilNo(long id, String tenderBilNo) {
		String sql = "UPDATE BorrowTender SET tender_bil_no = :tenderBilNo WHERE id = :id ";
		Query query = em.createQuery(sql);
		query.setParameter("id", id);
		query.setParameter("tenderBilNo", tenderBilNo);
		int result = query.executeUpdate();
		if (result <= 0) {
			throw new UserException("更新tender失败", 1);
		}
		
	}
	
	@Override
	public BorrowTender getTenderByBillNo(String tenderBillNo){
		BorrowTender borrowTender = super.findObjByProperty("tenderBilNo", tenderBillNo);
		if (borrowTender != null) {
			return borrowTender;
		}
		return null;
	}

    @Override
    public List<BorrowTender> getTenderByBorrowId(long id) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("borrow.id", id);
        return findByCriteria(param);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageDataList<Borrow> getBorrowlist(BorrowTenderModel model) {
        StringBuffer jpql = new StringBuffer("SELECT p1.* FROM rd_borrow p1 , rd_borrow_tender p2 WHERE p1.id = p2.borrow_id");
        long userId =0;
        Date startTime = null;
        Date endTime = null;
        String borrowName = "";
        if (model != null) {
            if (model.getUser() != null && model.getUser().getUserId() > 0) {
                jpql.append(" AND p2.user_id = :userId");
                userId = model.getUser().getUserId();
            }
            if (StringUtil.isNotBlank(model.getBorrowName()) && model.getBorrowName().length() > 0) {
                jpql.append(" AND p1.name like :borrowName");
                borrowName = model.getBorrowName();
            }
            if (StringUtil.isNotBlank(model.getStartTime())) {
                startTime = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
                jpql.append(" AND p1.add_time >= :startTime");
            }
            if (StringUtil.isNotBlank(model.getEndTime())) {
                endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
                jpql.append(" AND p1.add_time <= :endTime");
            }
            if(model.getTime() > 0){
            	endTime = DateUtil.getDayStartTime(System.currentTimeMillis()/1000);
                jpql.append(" AND p1.add_time >= :endTime");
                if (model.getTime() == 7) {
                    endTime = DateUtil.rollDay(endTime, -7);
                    endTime = DateUtil.getDayEndTime(endTime.getTime() / 1000);
                } else if (model.getTime() > 0 && model.getTime() < 4){
                    endTime = DateUtil.rollMon(endTime, -model.getTime());
                    endTime = DateUtil.getDayEndTime(endTime.getTime() / 1000);
                }
            }
        }
        jpql.append(" GROUP BY p1.id ORDER BY p2.add_time DESC");
        PageDataList<Borrow> pageDataList = new PageDataList<Borrow>();
        Query query = em.createNativeQuery(jpql.toString(), Borrow.class);
        if(userId > 0){
            query.setParameter("userId", userId);
        }
        if(borrowName.length() > 0){
            query.setParameter("borrowName", "%"+borrowName+"%");
        }
        if (startTime != null) {
            query.setParameter("startTime", startTime);
        }
        if (endTime != null) {
            query.setParameter("endTime", endTime);
        }
        Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
        query.setFirstResult((model.getPage() - 1) * model.getSize());
        query.setMaxResults(model.getSize());
        List<Borrow> list = query.getResultList();
        pageDataList.setList(list);
        pageDataList.setPage(page);
        return pageDataList;        
    }

    @Override
    public int getInvestCountByDate(int i) {
        String sql = "select count(distinct user_id) from rd_borrow_tender where date_format(add_time,'%Y-%m-%d') = DATE_SUB(CURDATE(),INTERVAL ? DAY)";
        Query query = em.createNativeQuery(sql).setParameter(1, i);
        Object obj = query.getSingleResult();
        int count = 0;
        if (obj != null) {
            count = ((BigInteger) query.getSingleResult()).intValue();
        }
        return count;
    }

    @Override
    public int getInvestCountByDate(String date) {
        String dateFormat = "%Y-%m-%d";
        if (date.length() == 7) {
            dateFormat = "%Y-%m";
        }
        String sql = "select count(distinct user_id) from rd_borrow_tender where date_format(add_time,'"+dateFormat+"') = ?";
        Query query = em.createNativeQuery(sql).setParameter(1, date);
        Object obj = query.getSingleResult();
        int count = 0;
        if (obj != null) {
            count = ((BigInteger) obj).intValue();
        }
        return count;
    }

    @Override
    public double tenderAccount(String startTime, String endTime) {
    	StringBuffer sql = new StringBuffer("select sum(p1.account) as num from rd_borrow_tender p1 left join rd_borrow b on b.id=p1.borrow_id"
                + "  where ((b.status in(3,6,7,8) and b.type<>" + Borrow.TYPE_FLOW + ") or (b.status in(1,8) and b.type=" + Borrow.TYPE_FLOW + "))");
    	if (StringUtil.isNotBlank(startTime)) {
			sql.append(" AND p1.add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime)) {
			sql.append(" AND p1.add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime)) {
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime)) {
			query.setParameter("endTime", endTime);
		}
        Object obj = query.getSingleResult();
        if(obj != null){
            return Double.parseDouble(obj.toString());
        }
        return 0; 
    }
    
    @Override
    public int getInvestUserCount() {
        String sql = "SELECT COUNT(DISTINCT t.user_id) FROM rd_borrow_tender t";
        Query query = em.createNativeQuery(sql);
        Object count = query.getSingleResult();
        if(count != null){
            return Integer.parseInt(count.toString());
        }
        return 0;
    }
    
    @Override
	public int getInvestUserCount(String startTime, String endTime) {
		StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT t.user_id) FROM rd_borrow_tender t WHERE 1=1 ");
		if(StringUtil.isNotBlank(startTime)){
			sql.append(" AND t.add_time >= :startTime");
		}
		if(StringUtil.isNotBlank(endTime)){
			sql.append(" AND t.add_time >= :endTime");
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
    public double tenderAllInterest() {
        String sql = "SELECT SUM(t1.repayment_yes_interest) FROM rd_borrow_tender t1 WHERE t1.status = 1";
        Query query = em.createNativeQuery(sql);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }

	@Override
	public double getInterestByBorrowId(long id) {
		String sql = "SELECT SUM(interest) FROM rd_borrow_tender WHERE borrow_id = ?";
        Query query = em.createNativeQuery(sql).setParameter(1, id);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public int getTenderByUserAndStatus(long userId, int status) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.status", status);
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}

	@Override
	public int getTenderCountByBorrow(Borrow borrow) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.id", borrow.getId());
		return countByCriteria(param);
	}

	@Override
	public int getUserTenderNum(long userId) {
		QueryParam param = QueryParam.getInstance();
		SearchFilter orFilter1 = new SearchFilter("borrow.status", Operators.EQ, 1);
		SearchFilter orFilter2 = new SearchFilter("borrow.status", Operators.EQ, 3);
		SearchFilter orFilter3 = new SearchFilter("borrow.status", Operators.EQ, 6);
		SearchFilter orFilter4 = new SearchFilter("borrow.status", Operators.EQ, 7);
		SearchFilter orFilter5 = new SearchFilter("borrow.status", Operators.EQ, 8);
		param.addOrFilter(orFilter1, orFilter2, orFilter3, orFilter4,orFilter5);
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}
	
	@Override
	public int getUserTenderNum(long userId, int borrowType) {
		QueryParam param = QueryParam.getInstance().addParam("borrow.type", Operators.NOTEQ, borrowType);
		SearchFilter orFilter1 = new SearchFilter("borrow.status", Operators.EQ, 1);
		SearchFilter orFilter2 = new SearchFilter("borrow.status", Operators.EQ, 3);
		SearchFilter orFilter3 = new SearchFilter("borrow.status", Operators.EQ, 6);
		SearchFilter orFilter4 = new SearchFilter("borrow.status", Operators.EQ, 7);
		SearchFilter orFilter5 = new SearchFilter("borrow.status", Operators.EQ, 8);
		param.addOrFilter(orFilter1, orFilter2, orFilter3, orFilter4,orFilter5);
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageDataList<BorrowTenderModel> getTenderRecordlist(BorrowTenderModel model) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" SELECT * FROM (SELECT bt.borrow_id,rb.name,rb.apr,SUM((IFNULL(bc1.capital1,0)+IFNULL(bc2.capital2,0))) AS capital,rb.review_time,rb.borrow_time_type,rb.time_limit,IFNULL(SUM(bc1.yesInterest),0) AS yesInterest, IFNULL(SUM(bc2.waitInterest),0) AS waitInterest from rd_borrow_tender bt LEFT JOIN ");
		sb.append(" (SELECT bc.borrow_id,bc.tender_id,SUM(bc.capital - bc.bond_capital) AS capital1, SUM(bc.interest + bc.repay_award - bc.bond_interest - bc.bond_award) AS yesInterest FROM rd_borrow_collection bc where bc.status = 1 AND bc.user_id = :userId GROUP BY bc.borrow_id,bc.tender_id ) bc1 ");
		sb.append(" ON bt.borrow_id = bc1.borrow_id AND bt.id= bc1.tender_id LEFT JOIN ");
		sb.append(" (SELECT bc.borrow_id,bc.tender_id,SUM(bc.capital - bc.bond_capital) AS capital2,SUM(bc.interest + bc.repay_award - bc.bond_interest - bc.bond_award) AS waitInterest FROM rd_borrow_collection bc where bc.status = 0 AND bc.user_id = :userId GROUP BY bc.borrow_id,bc.tender_id) bc2 ");
		sb.append(" ON bt.borrow_id = bc2.borrow_id AND bt.id= bc2.tender_id, rd_borrow rb ");
		sb.append(" WHERE rb.id = bt.borrow_id AND bt.user_id=:userId ");

		if (model.getStatus() == Borrow.STATUS_REPAYMENT_DONE) {
			sb.append(" AND rb.status = 8 ");
		} else {
			sb.append(" AND (rb.status IN(3, 6, 7 ,69) OR (rb.status=1 AND rb.expiration_time > now())) ");
		}
		
        Date d = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
        Date startTime = null;
        Date endTime = null;
        if (model.getTime() == 7) {
        	sb.append(" AND bt.add_time >= :startTime");
        	sb.append(" AND bt.add_time <= :endTime");
            startTime = DateUtil.rollDay(d, -7);
            endTime = d;
        } else if (model.getTime()>0 && model.getTime()<4){
        	sb.append(" AND bt.add_time >= :startTime");
        	sb.append(" AND bt.add_time <= :endTime");
            startTime = DateUtil.rollMon(d, -model.getTime());
            endTime = d;
        }
        if (StringUtil.isNotBlank(model.getStartTime())) {
            startTime = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
            sb.append(" AND bt.add_time > :startTime");
        }
        if (StringUtil.isNotBlank(model.getEndTime())) {
            endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
            sb.append(" AND bt.add_time < :endTime");
        }
        sb.append(" GROUP BY bt.borrow_id,bt.id ORDER BY bt.add_time DESC ) tb WHERE capital > 0");
		
		Query query = em.createNativeQuery(sb.toString());
		query.setParameter("userId", model.getUser().getUserId());
        if (startTime != null) {
            query.setParameter("startTime", startTime);
        }
        if (endTime != null) {
            query.setParameter("endTime", endTime);
        }
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
        query.setFirstResult((model.getPage() - 1) * model.getSize());
        query.setMaxResults(model.getSize());
		List<Object[]> list = query.getResultList();
        List<BorrowTenderModel> modelList = new ArrayList<BorrowTenderModel>();
        PageDataList<BorrowTenderModel> pageDataList = new PageDataList<BorrowTenderModel>();
		for (Object[] o : list) {
			BorrowTenderModel bt = new BorrowTenderModel();
			bt.setBorrowId((Integer)o[0]);
			bt.setBorrowName(o[1] + "");
			bt.setApr(Double.parseDouble((o[2]+"")));
			bt.setMoney(Double.parseDouble((o[3]+"")));
			bt.setStartDate((Date)o[4]);
			if (bt.getStartDate() != null) {
				if ((Boolean)o[5] == false) {
					bt.setEndDate(DateUtil.rollMon(bt.getStartDate(), (Integer)o[6]));
				} else {
					bt.setEndDate(DateUtil.rollDay(bt.getStartDate(), (Integer)o[6]));
				}
			}
			bt.setRepaymentYesInterest(Double.parseDouble((o[7]+"")));
			bt.setWaitInterest(Double.parseDouble((o[8]+"")));
			modelList.add(bt);
		}

        pageDataList.setList(modelList);
        pageDataList.setPage(page);
		return pageDataList;
	}
	
	@Override
	public BorrowTender getTenderBySubmitBidNo(String submitBidNo){
		BorrowTender borrowTender = super.findObjByProperty("submitBidNo", submitBidNo);
		if (borrowTender != null) {
			return borrowTender;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowTender> getBorrowTenderByBorrowId(Long borrowId) {
		String sql = "select * from rd_borrow_tender where borrow_id = ?1";
		Query query = em.createNativeQuery(sql, BorrowTender.class);
		query.setParameter(1, borrowId);
		return query.getResultList();
	}

	@Override
	public double getLastYearInvest(Date starTime, Date endTime, User user) {
		String sql = "select sum(account) from BorrowTender where addTime >= ?1 and addTime <= ?2 and user = ?3 and borrow.status in (3,6,7,8)";
		Query query = em.createQuery(sql).setParameter(1, starTime).setParameter(2, endTime).setParameter(3, user);
		Object obj = query.getSingleResult();
		if(obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}
}
