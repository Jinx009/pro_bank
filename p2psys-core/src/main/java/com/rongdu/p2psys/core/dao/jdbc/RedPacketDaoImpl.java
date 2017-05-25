package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.p2psys.core.dao.RedPacketDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.util.ConstantUtil;

@Repository("redPacketDao")
public class RedPacketDaoImpl extends BaseDaoImpl<RedPacket> implements
		RedPacketDao {

	@Override
	public List<RedPacket> findActivities() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", Operators.GT, 9);
		param.addParam("isDelete", ConstantUtil.FLAG_FALSE);
		return findByCriteria(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RedPacket> findFixedActiveRedPacket() {
		String sql = "from RedPacket where startTime<=now() and now()<=endTime and paymentType=1 and isDelete=0";
		Query query = em.createQuery(sql);
		List<RedPacket> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

}
