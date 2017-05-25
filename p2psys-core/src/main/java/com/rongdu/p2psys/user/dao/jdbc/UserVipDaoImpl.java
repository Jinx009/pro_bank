package com.rongdu.p2psys.user.dao.jdbc;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.domain.UserVipRule;
import com.rongdu.p2psys.user.dao.UserVipDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserVip;

@Repository("userVipDao")
public class UserVipDaoImpl extends BaseDaoImpl<UserVip> implements UserVipDao {

	@SuppressWarnings("unchecked")
	@Override
	public UserVip getLateYearInvestByUser(User user) {
		String sql = " FROM UserVip where user = ?1 ";
		Query query = em.createQuery(sql).setParameter(1, user);
		List<UserVip> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateUserVip(double money1, double money2, UserVipRule rule) {
		String sql = "update UserVip set name = ?1,level = ?2,update_time = ?3 where last_year_invest > ?4 and last_year_invest <= ?5";
		Query query = em.createQuery(sql)
				.setParameter(1, rule.getName())
				.setParameter(2, rule.getLevel())
				.setParameter(3, new Date())
				.setParameter(4, money1)
				.setParameter(5, money2);
		query.executeUpdate();
	}

	@Override
	public void updateUserVip(double money, UserVipRule rule) {
		String sql = "update UserVip set name = ?1,level = ?2,update_time = ?3 where last_year_invest > ?4";
		Query query = em.createQuery(sql)
				.setParameter(1, rule.getName())
				.setParameter(2, rule.getLevel())
				.setParameter(3, new Date())
				.setParameter(4, money);
		query.executeUpdate();
	}

}
