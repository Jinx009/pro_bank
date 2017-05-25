package com.rongdu.p2psys.user.dao.jdbc;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;

@Repository("userRedPacketDao")
public class UserRedPacketDaoImpl extends BaseDaoImpl<UserRedPacket> implements
		UserRedPacketDao {

	@Override
	public UserRedPacket findByNid(String nid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTotalPacketMoneyByIds(Long[] ids) {
		if (ids == null) {
			return 0;
		}
		StringBuffer sb = new StringBuffer(
				"select sum(amount) from rd_user_red_packet where is_used = 0 and id in (");
		for (int i = 0; i < ids.length; i++) {
			sb.append("?,");
		}
		double totalPacketMoney = 0;
		Query query = em.createNativeQuery(sb.substring(0, sb.length() - 1)
				+ ")");
		for (int i = 0; i < ids.length; i++) {
			query.setParameter(i + 1, ids[i]);
		}
		Object obj = query.getSingleResult();
		if (obj != null) {
			totalPacketMoney = ((BigDecimal) obj).doubleValue();
		}
		return totalPacketMoney;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> statisticsByModel(UserRedPacketModel model) {
		StringBuffer sb = new StringBuffer(
				"select rd_user.user_id,user_name,real_name,sum(amount) from rd_user_red_packet,rd_user where rd_user.user_id = rd_user_red_packet.user_id");

		if (!StringUtil.isBlank(model.getSearchName())) {// 模糊条件
			sb.append(" and (user_name like ?3 or real_name like ?3 )");
		}
		if (!StringUtil.isBlank(model.getUserName())) {
			sb.append(" and user_name = ?1");
		}
		if (!StringUtil.isBlank(model.getRealName())) {
			sb.append(" and real_name = ?2");
		}
		Query query = em.createNativeQuery(sb.append(" group by user_id")
				.toString());
		if (!StringUtil.isBlank(model.getUserName())) {
			query.setParameter(1, model.getUserName());
		}
		if (!StringUtil.isBlank(model.getRealName())) {
			query.setParameter(2, model.getRealName());
		}
		if (!StringUtil.isBlank(model.getSearchName())) {
			query.setParameter(3, "%" + model.getSearchName() + "%");
		}
		query.setFirstResult((model.getPage() - 1) * model.getRows());
		query.setMaxResults(model.getRows());
		return query.getResultList();
	}

	@Override
	public int getTotalByModel(UserRedPacketModel model) {
		StringBuffer sb = new StringBuffer(
				"select rd_user.user_id,user_name,real_name,sum(amount) from rd_user_red_packet,rd_user where rd_user.user_id = rd_user_red_packet.user_id");
		if (!StringUtil.isBlank(model.getUserName())) {
			sb.append(" and user_name =?1");
		}
		if (!StringUtil.isBlank(model.getRealName())) {
			sb.append(" and real_name =?2");
		}
		Query query = em.createNativeQuery(sb.append(" group by user_id")
				.toString());
		if (!StringUtil.isBlank(model.getUserName())) {
			query.setParameter(1, model.getUserName());
		}
		if (!StringUtil.isBlank(model.getRealName())) {
			query.setParameter(2, model.getRealName());
		}
		return query.getResultList().size();
	}

	@Override
	public double getTotalPacketMoneyByTender(long tenderId) {
		StringBuffer buffer = new StringBuffer(
				"select sum(amount) from rd_user_red_packet where tender_id =? and is_used = 1");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter(1, tenderId);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getTotalPacketMoneyByBondTender(long bondTenderId) {
		StringBuffer buffer = new StringBuffer(
				"select sum(amount) from rd_user_red_packet where bond_tender_id =? and is_used = 1");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter(1, bondTenderId);
		Object obj = query.getSingleResult();
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public List<UserRedPacket> getListByTenderId(long tenderId) {
		QueryParam param = QueryParam.getInstance().addParam("tender.id",
				tenderId);
		return this.findByCriteria(param);
	}
	
	/**
	 * 获取可以投资的多选红包
	 * @param tenderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserRedPacket> getMultiAvalibleRedPacketList(User user)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select r.* from rd_user_red_packet r join s_red_packet p on r.type_id = p.id ");
		buffer.append(" where p.service_type != 'recommend' and r.is_used = 0 and r.expired_time >=NOW() and user_id = ");
		buffer.append(user.getUserId());
        Query query = em.createNativeQuery(buffer.toString(),UserRedPacket.class);
        return query.getResultList();
	}
	
	/**
	 * 获取可以投资的多选红包
	 * @param tenderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserRedPacket> getSingleAvalibleRedPacketList(User user)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select r.* from rd_user_red_packet r join s_red_packet p on r.type_id = p.id ");
		buffer.append(" where p.service_type = 'recommend' and r.is_used = 0 and r.expired_time >=NOW() and user_id = ");
		buffer.append(user.getUserId());
        Query query = em.createNativeQuery(buffer.toString(),UserRedPacket.class);
        return query.getResultList();
	}
	
	/**
	 * 获取提醒的红包列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserRedPacket> getRemindRedPacketList()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select r.* from rd_user_red_packet r  join s_red_packet p on r.type_id = p.id ");
		buffer.append(" where r.is_used = 0 and r.expired_time > NOW() and ");
		buffer.append(" r.expired_time <= date_add(NOW(), interval 2 day)  and r.is_remind = 0 ");
        Query query = em.createNativeQuery(buffer.toString(),UserRedPacket.class);
        return query.getResultList();
	}

}
