package com.rongdu.p2psys.user.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.UserNoticeConfigDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;

@Repository("userNoticeConfigDao")
public class UserNoticeConfigDaoImpl extends BaseDaoImpl<UserNoticeConfig> implements UserNoticeConfigDao {

	@Override
	public UserNoticeConfig getUNConfig(long userId, String noticeTypeNid) {
		return findByCriteriaForUnique(QueryParam.getInstance().addParam("user", new User(userId))
				.addParam("nid", noticeTypeNid));
	}

	@Override
	public List<UserNoticeConfig> getAllUNConfigs(long userId) {
		return this.findByProperty("user.userId", userId);
	}

	@Override
	public void updateUNConfigs(ArrayList<UserNoticeConfig> uncList, long userId) {
		String jpql = " DELETE FROM UserNoticeConfig where userId = :userId ";
		Query query = em.createQuery(jpql);
		query.setParameter("Id", new Integer(1));
		query.executeUpdate();
		this.save(uncList);
	}

}
