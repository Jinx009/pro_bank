package com.rongdu.p2psys.bond.dao.impl;

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
import com.rongdu.p2psys.bond.dao.BondTenderDao;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.model.BondTenderModel;

/**
 * 债权投资DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Repository("bondTenderDao")
public class BondTenderDaoImpl extends BaseDaoImpl<BondTender> implements BondTenderDao {

    @SuppressWarnings("unchecked")
    @Override
    public BondTender getBondTenderById(long id) {
        String jpql = "from BondTender where id = ?1";
        Query query = em.createQuery(jpql);
        query.setParameter(1, id);
        List<BondTender> list = query.getResultList();
        if (list != null && list.size() >= 0) {
            return (BondTender) list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageDataList<BondTender> getTenderModelPage(BondTenderModel model) {

        StringBuffer jpql = new StringBuffer(" FROM BondTender p1 WHERE 1=1 ");
        
        if(model.getBondId() > 0){
            jpql.append(" AND p1.bond.id = :bondId");
        }
        if(model.getUserName() != null && model.getUserName().length() > 0){
            jpql.append(" AND p1.user.userName LIKE :userName");
        }
        if(model.getStatus()  > 0){
            jpql.append(" AND p1.status = :status");
        }
        Date startTime = null;
        if (StringUtil.isNotBlank(model.getStartTime())) {
            startTime = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
            jpql.append(" AND p1.addTime > :startTime");
        }
        Date endTime = null;
        if (StringUtil.isNotBlank(model.getEndTime())) {
            endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
            jpql.append(" AND p1.addTime < :endTime");
        }
        jpql.append(" ORDER BY p1.addTime DESC");
        QueryParam param = QueryParam.getInstance();
        param.addPage(model.getPage(), model.getRows());
        PageDataList<BondTender> pageList = new PageDataList<BondTender>();
        Query query = em.createQuery(jpql.toString(), BondTender.class);
        
        if(model.getBondId() > 0){
            query.setParameter("bondId", model.getBondId());
        }
        if (model.getUserName() != null && model.getUserName().length() > 0) {
            query.setParameter("userName", "%"+model.getUserName()+"%");
        }
        if (model.getStatus()  > 0) {
            query.setParameter("status", model.getStatus());
        }
        if (startTime != null) {
            query.setParameter("startTime", startTime);
        }
        if (endTime != null) {
            query.setParameter("endTime", endTime);
        }
        Page page = new Page(query.getResultList().size(), model.getPage(), model.getRows());
        query.setFirstResult((model.getPage() - 1) * model.getRows());
        query.setMaxResults(model.getRows());
        List<BondTender> list = query.getResultList();
        pageList.setList(list);
        pageList.setPage(page);
        return pageList;
    }
    
    @Override
    public PageDataList<BondTender> getBoughtBondList(BondModel model) {
    	
  		PageDataList<BondTender> pageDataList = new PageDataList<BondTender>();
		Query query = em.createQuery(" FROM BondTender WHERE user.userId = :userId ORDER BY  addTime DESC ");
		query.setParameter("userId", model.getUser().getUserId());
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getRows());
		query.setFirstResult((model.getPage() - 1) * model.getRows());
		query.setMaxResults(model.getRows());
		@SuppressWarnings("unchecked")
		List<BondTender> list = query.getResultList();
		pageDataList.setList(list);
		pageDataList.setPage(page);
        return pageDataList;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<BondTender> getLatestTenerList() {
		Query query = em.createNativeQuery("SELECT * FROM rd_bond_tender ORDER BY add_time DESC LIMIT 10",BondTender.class);
		List<BondTender> list =(List<BondTender>)query.getResultList();
		return list;
    }
    
    @Override
    public double getPayInterestByBondId(long bondId) {
        String sql = "SELECT SUM(bt.payInterest) FROM BondTender bt WHERE bt.bond.id = :bondId ";
        Query query = em.createQuery(sql);
        query.setParameter("bondId", bondId);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }
    
}
