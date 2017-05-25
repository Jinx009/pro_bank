package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.p2psys.core.dao.RedPacketDetailDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;

@Repository("redPacketDetailDao")
public class RedPacketDetailDaoImpl extends BaseDaoImpl<RedPacketDetail>
		implements RedPacketDetailDao {

	@Override
	public List<RedPacketDetail> getList(RedPacket redPacket) {
		QueryParam param = QueryParam.getInstance()
				.addParam("redPacket",  Operators.EQ, redPacket)
				.addParam("isDelete", Operators.EQ, 0);
		return this.findByCriteria(param);
	}

}
