package com.rongdu.p2psys.ppfund.dao.jdbc;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.ppfund.dao.PpfundDao;
import com.rongdu.p2psys.ppfund.dao.PpfundUploadDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.model.PpfundModel;

/**
 * PPfund（资金管理产品）
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
@Repository("ppfundDao")
public class PpfundDaoImpl extends BaseDaoImpl<Ppfund> implements PpfundDao {
	@Resource
	private PpfundUploadDao ppfundUploadDao;

	@SuppressWarnings("unchecked")
	@Override
	public PpfundModel getLastPpfund() {
		String sql = "select * from rd_ppfund where status in(1,3) order by scales asc,add_time desc limit 1";
		Query query = em.createNativeQuery(sql,Ppfund.class);
		List<Ppfund> list = query.getResultList();
		if(list != null && list.size() > 0){
			Ppfund ppfund = list.get(0);
			PpfundModel model = PpfundModel.instance(ppfund);
			PpfundUpload ppfundUpload = ppfundUploadDao.getPpfundImg(ppfund.getId());
			if(ppfundUpload != null){
				model.setPpfundImg(ppfundUpload.getPicPath());
			}
			return model;
		}
		return null;
	}

	@Override
	public Object[] countByFinish() {
		String jpql = "select count(id),sum(accountYes),avg(apr) from Ppfund where status in (1,3)";
		Query query = em.createQuery(jpql);
		Object[] object = (Object[]) query.getSingleResult();
		return object;
	}
	
	@Override
	public int getUserPpfundInOrder(long ppfundInId,long ppfundId)
	{
		String sql = " select count(*) from rd_ppfund_in t where t.ppfund_id = " + ppfundId + " and t.id <= " + ppfundInId;
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Integer.parseInt(obj.toString());
		}
		return 1;
	}
	
	@Override
	public double getAllMoney()
	{
		String sql = " select sum(account) from rd_ppfund_in ";
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 1;
	}

}
