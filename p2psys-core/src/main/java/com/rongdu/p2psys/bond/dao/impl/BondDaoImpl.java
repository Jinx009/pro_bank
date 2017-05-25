package com.rongdu.p2psys.bond.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.bond.dao.BondDao;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.BondConfigRuleCheck;



/**
 * 债权DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Repository("bondDao")
public class BondDaoImpl extends BaseDaoImpl<Bond> implements BondDao {

    @SuppressWarnings("unchecked")
    @Override
    public Bond getBondById(long id) {
        String jpql = "from Bond where id = ?1";
        Query query = em.createQuery(jpql);
        query.setParameter(1, id);
        List<Bond> list = query.getResultList();
        if (list != null && list.size() >= 0) {
            return (Bond) list.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public PageDataList<BondModel> getBondList(BondModel model) {
  		PageDataList<BondModel> pageDataList = new PageDataList<BondModel>();
		StringBuffer sb = new StringBuffer("");
		sb.append(" SELECT borrow.name,rb.day_id,rb.add_time as addTime,rb.status,rb.bond_money,rb.sold_capital,borrow.apr as apr,CONVERT(borrow.style, char), bc.next_repayment_time,bc.last_repayment_time, ");
		sb.append(" rb.bond_apr,rb.id,borrow.id as borrowId, borrow.lowest_account,borrow.most_account,borrow.type as borrowtype,");
		sb.append(" IF(rb.status = 1, datediff(bc.next_repayment_time, NOW()), datediff(bt.add_time, NOW())) as sortDays from rd_bond rb LEFT JOIN( ");
		sb.append(" SELECT bc.borrow_id,MIN(bc.repayment_time) AS next_repayment_time,MAX(bc.repayment_time) AS last_repayment_time from rd_borrow_collection bc GROUP BY bc.borrow_id) bc ON rb.borrow_id = bc.borrow_id LEFT JOIN ( ");
		sb.append(" SELECT bt.bond_id,MAX(bt.add_time) as add_time from rd_bond_tender bt GROUP BY bt.bond_id) bt ON rb.id = bt.bond_id, rd_borrow borrow where  rb.borrow_id = borrow.id AND (bc.last_repayment_time>now() or rb.sold_capital>=rb.bond_money ) ");
		if(model.getBorrowType() != 100 && model.getBorrowType() > 0){
			sb.append(" AND borrow.type = :type ");
		}
		sb.append(this.searchParam(model));
		if(model.getId() > 0){
			sb.append(" AND rb.id = :id ");
		}else{
			sb.append(" AND ((rb.status = 1) OR ");
			sb.append(" (rb.status in (3, 4, 5, 6) AND rb.sold_capital > 0)) ");
		}
		sb.append(" ORDER BY rb.status ASC, ");
		if(StringUtil.isBlank(model.getSort())){
			sb.append(" sortDays ASC");
		}else{
			sb.append(model.getSort()).append(" ").append(model.getOrder());
		}
		
		Query query = em.createNativeQuery(sb.toString());
		if(model.getBorrowType() != 100 && model.getBorrowType() > 0){
			query.setParameter("type", model.getBorrowType());
		}
		if(model.getId() > 0){
			query.setParameter("id", model.getId());
		}
		
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getRows());
		query.setFirstResult((model.getPage() - 1) * model.getRows());
		query.setMaxResults(model.getRows());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		
		List<BondModel> modelList = new ArrayList<BondModel>();
		for (Object[] o : list) {
			BondModel bondModel = new BondModel();
			bondModel.setBorrowName(o[0] + "");
			int maxDayId = Integer.parseInt(o[1] + "");
			Date addTime = (Date)o[2];
			bondModel.setAddTime(addTime);
			if(maxDayId < 10){
				bondModel.setName(DateUtil.dateStr7(addTime).substring(4) + "0" + maxDayId );
			}else{
				bondModel.setName(DateUtil.dateStr7(addTime).substring(4) + maxDayId );
			}
			byte status = Byte.parseByte(o[3] + "");
			bondModel.setStatus(status);
			double bondMoney = Double.parseDouble(o[4] + "");
			double soldCapital = Double.parseDouble(o[5] + "");
			if(status == 1){
				bondModel.setBondMoney(bondMoney);
			}else{
				bondModel.setBondMoney(soldCapital);
			}
			bondModel.setApr(Double.parseDouble(o[6] + ""));
			bondModel.setBorrowStyle(Integer.parseInt(o[7] + ""));
			//bondModel.setNextRepaymentTime((Date)o[8]);
			Date lastRepaymentTime = (Date)o[9];
			bondModel.setLastRepaymentTime(lastRepaymentTime);
			bondModel.setSoldCapital(soldCapital);
			bondModel.setRemainDays(DateUtil.daysBetween(new Date(), lastRepaymentTime)<0?0:DateUtil.daysBetween(new Date(), lastRepaymentTime));
			bondModel.setBondApr(Double.parseDouble(o[10] + ""));
			bondModel.setId(Long.parseLong(o[11] + ""));
			bondModel.setBorrowId(Long.parseLong(o[12] + ""));
			double lowestAccount = Double.parseDouble(o[13] + "");
			if(lowestAccount < 100){
				lowestAccount = 100;
			}
//			bondModel.setLowestAccount(lowestAccount);
			bondModel.setLowestAccount(100);
			bondModel.setMostAccount(Double.parseDouble(o[14] + ""));
			bondModel.setBorrowType(Integer.parseInt(o[15] + ""));
			modelList.add(bondModel);
		}
		pageDataList.setList(modelList);
		pageDataList.setPage(page);
		return pageDataList;
    }
    
    private String searchParam(BondModel model) {
        // 利率
        StringBuffer sql = new StringBuffer("");
        if (model.getAprSearch() != -1) {
            switch (model.getAprSearch()) {
                case 1:
                    sql.append(" AND borrow.apr <= 6");
                    break;
                case 2:
                    sql.append(" AND borrow.apr >= 6 AND borrow.apr <= 12");
                    break;
                case 3:
                    sql.append(" AND borrow.apr >= 12 AND borrow.apr <= 18");
                    break;
                case 4:
                    sql.append(" AND borrow.apr >= 18 AND borrow.apr <= 20");
                    break;
                case 5:
                    sql.append(" AND borrow.apr >= 20");
                    break;
                default:
                    break;
            }
        }
        // 金额
        if (model.getMoneySearch() != -1) {
            switch (model.getMoneySearch()) {
                case 2:
                    sql.append(" AND rb.bond_money <= 10000");
                    break;
                case 3:
                    sql.append(" AND rb.bond_money >= 10000 AND rb.bond_money <= 50000");
                    break;
                case 4:
                    sql.append(" AND rb.bond_money >= 50000 AND rb.bond_money <= 100000");
                    break;
                case 5:
                    sql.append(" AND rb.bond_money >= 100000 AND rb.bond_money <= 200000");
                    break;
                case 6:
                    sql.append(" AND rb.bond_money >= 200000 AND rb.bond_money <= 500000");
                    break;
                case 7:
                    sql.append(" AND rb.bond_money >= 500000");
                    break;
                default:
                    break;
            }
        }
        // 期限
        if (model.getTimeSearch() != -1) {
            switch (model.getTimeSearch()) {
                case 2:
                    sql.append(" AND datediff(bc.last_repayment_time,NOW()) < 30 ");
                    break;
                case 3:
                    sql.append(" AND datediff(bc.last_repayment_time,NOW()) >= 30 AND datediff(bc.last_repayment_time,NOW()) < 60  ");
                    break;
                case 4:
                    sql.append(" AND datediff(bc.last_repayment_time,NOW()) >= 60 AND datediff(bc.last_repayment_time,NOW()) < 90  ");
                    break;
                case 5:
                    sql.append(" AND datediff(bc.last_repayment_time,NOW()) >= 90 ");
                    break;
                default:
                    break;
            }
        }
        //状态
        if(model.getStatus() > 0){
        	switch (model.getStatus()) {
			case 1:
				sql.append(" AND rb.bond_money > rb.sold_capital ");
				break;
			case 6:
				sql.append(" AND rb.bond_money <= rb.sold_capital ");
				break;
			default:
				break;
			}
        }
      
        return sql.toString();
    }
    
    @Override
    public double getSellingBondMoneyByTenderId(long tenderId, byte type) {
        String sql = "SELECT SUM(b.bondMoney) FROM Bond b WHERE b.status in (0, 1) and b.kfId = :kfId AND b.type = :type";
        Query query = em.createQuery(sql);
        query.setParameter("kfId", tenderId);
        query.setParameter("type", type);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }
    
    @Override
    public double getSoldBondMoneyByTenderId(long tenderId, byte type) {
        String sql = "SELECT SUM(b.soldCapital) FROM Bond b WHERE b.status in (3, 4, 5, 6) and b.kfId = :kfId AND b.type = :type";
        Query query = em.createQuery(sql);
        query.setParameter("kfId", tenderId);
        query.setParameter("type", type);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }
    
    @Override
    public PageDataList<BondModel> getBondModelPage(BondModel model) {

  		PageDataList<BondModel> pageDataList = new PageDataList<BondModel>();
		StringBuffer sb = new StringBuffer("");
		sb.append(" SELECT bbt.borrow_id,bbt.borrow_tender_id,br.NAME,br.apr,(bbt.money - IFNULL(bond.soldmoney, 0) - IFNULL(bond1.soldmoney, 0)) AS money,br.review_time,bbt.last_repayment_time,bbt.next_repayment_time,");
		sb.append(" (bbt.remainRepayMoney - IFNULL(bond.sellingmoney, 0) - IFNULL(bond1.soldmoney, 0)) AS remainmoney, bbt.id as kf_id, bbt.type FROM");
		sb.append(" ( SELECT 0 as bondtype,bt1.id,bt1.borrow_id,bt1.id AS borrow_tender_id,bt1.money,bc.remainCapital AS remainRepayMoney,bt1.add_time,0 AS type,bc.next_repayment_time,bc.last_repayment_time");
		sb.append("	 FROM rd_borrow_tender bt1,");
		sb.append("			(SELECT SUM(bc.capital) AS remainCapital,MIN(bc.repayment_time) AS next_repayment_time,MAX(bc.repayment_time) AS last_repayment_time,bc.borrow_id,bc.tender_id");
		sb.append("			 FROM rd_borrow_collection bc");
		sb.append("			 WHERE bc. STATUS = 0 AND bc.repayment_time >= now() ");
		sb.append("			 GROUP BY bc.borrow_id,bc.tender_id) bc ");
		sb.append("	 WHERE bt1. STATUS = 1 AND bt1.user_id = :userId AND bt1.id = bc.tender_id AND bt1.borrow_id = bc.borrow_id AND datediff(now(), bt1.add_time) >= :holdDays AND datediff(bc.next_repayment_time,now()) >= :remainDays ");
		sb.append("               AND bt1.id not in (SELECT bc.tender_id FROM rd_borrow_collection bc WHERE ((bc.status=0 AND bc.repayment_time < now()) OR (bc.status=1 AND bc.repayment_time < bc.repayment_yes_time))) ");
//		sb.append("	 UNION ALL");
//		sb.append("	 SELECT 1 as bondtype,bt2.id,bt2.borrow_id,bt2.borrow_tender_id,bt2.tender_money AS money,(bt2.tender_money - bt2.received_account) AS remainRepayMoney,bt2.add_time,1 AS type,bc.next_repayment_time,bc.last_repayment_time");
//		sb.append("	 FROM rd_bond_tender bt2,");
//		sb.append("			(SELECT SUM(bc.capital) AS remainCapital,MIN(bc.collection_time) AS next_repayment_time,MAX(bc.collection_time) AS last_repayment_time,bc.bond_id,bc.bond_tender_id");
//		sb.append("			 FROM rd_bond_collection bc");
//		sb.append("			 WHERE bc. STATUS = 0 AND bc.collection_time >= now()");
//		//原先是根据borrow_id,borrow_tender_id进行分组，为了获取SUM(bc.capital)总共投资债券金额，由于多条债权投资记录的borrow_id和borrow_tender_id一样，导致页面只会出现一条投资债权记录
//		//目前分组导致SUM(bc.capital)无法一并统计总共投资金额，只能进行累加获取
//		sb.append("			 GROUP BY bc.bond_id, bc.bond_tender_id) bc");
//		sb.append("	 WHERE bt2. STATUS = 0 AND bt2.user_id = :userId AND bt2.id = bc.bond_tender_id AND bt2.bond_id = bc.bond_id AND datediff(now(), bt2.add_time) >= :holdDays AND datediff(bc.next_repayment_time,now()) >= :remainDays ");
//		sb.append("    AND bt2.id not in (SELECT bc.borrow_tender_id FROM rd_bond_collection bc WHERE ((bc.status=0 AND bc.collection_time < now()) OR (bc.status=1 AND bc.collection_time < bc.collection_yes_time)))) bbt");
		sb.append(") bbt");
		
		
		sb.append(" LEFT JOIN (SELECT SUM(b.bond_money) sellingmoney,SUM(b.sold_capital) soldmoney,b.kf_id,b.type FROM rd_bond b WHERE b. STATUS IN (0, 1) GROUP BY b.kf_id) bond ON bbt.id = bond.kf_id AND bbt.bondtype = bond.type ");
		sb.append(" LEFT JOIN (SELECT SUM(b.sold_capital) soldmoney,b.kf_id,b.type FROM rd_bond b WHERE b. STATUS IN (3, 4, 5, 6) GROUP BY b.kf_id) bond1 ON bbt.id = bond1.kf_id AND bbt.bondtype = bond1.type,rd_borrow br ");
		sb.append(" WHERE bbt.borrow_id = br.id AND (bbt.remainRepayMoney - IFNULL(bond.sellingmoney, 0) - IFNULL(bond1.soldmoney, 0)) >= 100");		

		Query query = em.createNativeQuery(sb.toString());
		query.setParameter("userId", model.getUser().getUserId());
		BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global.getRuleCheck("bondConfig");
		int holdDays = bondConfigRuleCheck.holdDays;
		int remainDays = bondConfigRuleCheck.remainDays;
		query.setParameter("holdDays", holdDays);
		query.setParameter("remainDays", remainDays);
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getRows());
		query.setFirstResult((model.getPage() - 1) * model.getRows());
		query.setMaxResults(model.getRows());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		
		List<BondModel> modelList = new ArrayList<BondModel>();
		for (Object[] o : list) {
			BondModel bondModel = new BondModel();
			bondModel.setBorrowId(Long.parseLong(o[0] + ""));
			bondModel.setBorrowTenderId(Long.parseLong(o[1] + ""));
			bondModel.setBorrowName(o[2] + "");
			bondModel.setApr(Double.parseDouble(o[3] + ""));
			bondModel.setTenderMoney(Double.parseDouble(o[4] + ""));
			Date reviewTime = (Date)o[5];
			bondModel.setHoldDays(DateUtil.daysBetween(reviewTime, new Date()));
			bondModel.setReviewTime(reviewTime);
			bondModel.setLastRepaymentTime((Date)o[6]);
			bondModel.setNextRepaymentTime((Date)o[7]);
			bondModel.setRemainMoney(Double.parseDouble(o[8] + ""));
			bondModel.setKfId(Long.parseLong(o[9] + ""));
			bondModel.setType(Byte.parseByte(o[10] + ""));
			modelList.add(bondModel);
		}
		pageDataList.setList(modelList);
		pageDataList.setPage(page);
		return pageDataList;
    }
    
    @Override
    public PageDataList<BondModel> getSellingBondList(BondModel model) {
  		PageDataList<BondModel> pageDataList = new PageDataList<BondModel>();
		StringBuffer sb = new StringBuffer("");
		sb.append(" SELECT rb.bond_money, rb.bond_apr, rb.add_time, rb.sold_capital, borrow.apr, bc.next_repayment_time,rb.id FROM rd_bond rb LEFT JOIN ");
		sb.append(" (SELECT bc.borrow_id, MIN(bc.repayment_time) AS next_repayment_time FROM rd_borrow_collection bc WHERE bc. STATUS = 0 AND bc.repayment_time >= NOW() ");
		sb.append(" GROUP BY bc.borrow_id ) bc ON rb.borrow_id = bc.borrow_id, rd_borrow borrow WHERE rb.status = 1 AND rb.user_id = :userId AND rb.borrow_id = borrow.id ");
		sb.append(" ORDER BY rb.add_time desc ");

		Query query = em.createNativeQuery(sb.toString());
		query.setParameter("userId", model.getUser().getUserId());
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getRows());
		query.setFirstResult((model.getPage() - 1) * model.getRows());
		query.setMaxResults(model.getRows());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		
		List<BondModel> modelList = new ArrayList<BondModel>();
		for (Object[] o : list) {
			BondModel bondModel = new BondModel();
			
			bondModel.setBondMoney(Double.parseDouble(o[0] + ""));
			bondModel.setBondApr(Double.parseDouble(o[1] + ""));
			bondModel.setAddTime((Date)o[2]);
			bondModel.setSoldCapital(Double.parseDouble(o[3] + ""));
			bondModel.setApr(Double.parseDouble(o[4] + ""));
			bondModel.setNextRepaymentTime((Date)o[5]);
			bondModel.setId(Long.parseLong(o[6] + ""));
			modelList.add(bondModel);
		}
		pageDataList.setList(modelList);
		pageDataList.setPage(page);
		return pageDataList;
    }
    
    @Override
    public PageDataList<BondModel> getSoldBondList(BondModel model) {
  		PageDataList<BondModel> pageDataList = new PageDataList<BondModel>();
		StringBuffer sb = new StringBuffer("");
		sb.append(" SELECT rb.sold_capital,rb.bond_apr,bt.pay_interest,bt.add_time,rb.id,rb.borrow_id FROM rd_bond rb LEFT JOIN ");
		sb.append(" (SELECT bt.bond_id,SUM(bt.pay_interest) pay_interest,MAX(bt.add_time) AS add_time FROM rd_bond_tender bt ");
		sb.append(" GROUP BY bt.bond_id) bt ON rb.id = bt.bond_id WHERE rb.sold_capital > 0 AND rb.status IN (3, 4, 5, 6) AND rb.user_id = :userId ");
		sb.append(" ORDER BY rb.add_time desc ");

		Query query = em.createNativeQuery(sb.toString());
		query.setParameter("userId", model.getUser().getUserId());
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getRows());
		query.setFirstResult((model.getPage() - 1) * model.getRows());
		query.setMaxResults(model.getRows());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		
		List<BondModel> modelList = new ArrayList<BondModel>();
		for (Object[] o : list) {
			BondModel bondModel = new BondModel();
			double bondMoney = Double.parseDouble(o[0] + "");
			bondModel.setBondMoney(bondMoney);
			bondModel.setBondApr(Double.parseDouble(o[1] + ""));
			if(o[2] != null){
				bondModel.setPayInterest(Double.parseDouble(o[2] + ""));
			}else{
				bondModel.setPayInterest(0);
			}
			// 手续费
			bondModel.setManageFee(getManageFee(bondMoney, 0));
			if(o[3] != null){
				bondModel.setAddTime((Date)o[3]);
			}
			bondModel.setId(Long.parseLong(o[4] + ""));
			bondModel.setBorrowId(Long.parseLong(o[5] + ""));
			modelList.add(bondModel);
		}
		pageDataList.setList(modelList);
		pageDataList.setPage(page);
		return pageDataList;
    }
    
    @Override
    public long getMaxDayId(String date) {
		String jpql = "SELECT MAX(dayId) FROM Bond WHERE date_format(addTime, '%Y%m%d') = ? ";
		Query query = em.createQuery(jpql).setParameter(1, date);
		Object obj = query.getSingleResult();
		if (obj == null) {
			obj = 0;
		}
		return Long.parseLong(obj.toString());
    }
    
    /**
     * 计算债权转让管理费(债权转让管理费 + 已经产生的利息的利息管理费)
     * @param money
     * @return
     */
    private double getManageFee(double capital, double interest){
    	
    	double manageCapital = 0;
    	double manageInterest = BigDecimalUtil.mul(interest, Global.getDouble("borrow_fee"));

    	BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global.getRuleCheck("bondConfig");
    	// 债权管理费未启用
    	if(bondConfigRuleCheck.status == 0){
    		manageCapital = 0;
    	}else{
    		// 按笔数收
    		if(bondConfigRuleCheck.feeType == 1){
    			manageCapital = bondConfigRuleCheck.sellFee;
    		// 按百分比收
    		}else if(bondConfigRuleCheck.feeType == 2){
    			manageCapital = BigDecimalUtil.mul(capital, bondConfigRuleCheck.sellFee);
    		}
    	}
    	return BigDecimalUtil.add(manageCapital, manageInterest);
    }    
    
    @Override
    public void stopBond(long borrowId, byte status) {
		String jpql = "UPDATE Bond SET status = :status WHERE borrow.id = :borrowId";
		Query query = em.createQuery(jpql);
		query.setParameter("status", status);
		query.setParameter("borrowId", borrowId);
		query.executeUpdate();
    }
    
    @Override
    public double getSoldCapitalByKfId(long kfId, byte type) {
        String sql = "SELECT SUM(b.soldCapital) FROM Bond b WHERE b.kfId = :kfId AND b.type = :type";
        Query query = em.createQuery(sql);
        query.setParameter("kfId", kfId);
        query.setParameter("type", type);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }
    
    @Override
    public double getSoldInterestByKfId(long kfId, byte type) {
        String sql = "SELECT SUM(b.soldInterest) FROM Bond b WHERE b.kfId = :kfId AND b.type = :type";
        Query query = em.createQuery(sql);
        query.setParameter("kfId", kfId);
        query.setParameter("type", type);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }    

}
