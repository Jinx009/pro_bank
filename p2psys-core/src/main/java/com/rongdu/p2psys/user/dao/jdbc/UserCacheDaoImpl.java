package com.rongdu.p2psys.user.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserCacheModel;

@Repository("userCacheDao")
public class UserCacheDaoImpl extends BaseDaoImpl<UserCache> implements UserCacheDao {

	@Override
	public UserCacheModel getUserCache(long userId) {
		String sql = "SELECT u.real_name,u.mobile_phone,c.* " + "FROM rd_user u,rd_user_cache c "
				+ "WHERE u.user_id=c.user_id AND c.user_id = :userId";
		String[] names = new String[] { "userId" };
		Object[] values = new Object[] { userId };
		return findForUniqueBySql(sql, names, values, UserCacheModel.class);
	}

	@Override
	public UserCache getUserCacheByUserId(long userId) {
		String sql = " FROM UserCache where user = ?1 ";
		Query query = em.createQuery(sql).setParameter(1, new User(userId));
		@SuppressWarnings("unchecked")
		List<UserCache> list = query.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void modify(long userId, String card_id) {
		String nativeSql = "UPDATE rd_user_cache SET card_id = :card_id WHERE user_id = :userId";
		String[] names = new String[] { "card_id", "userId" };
		Object[] values = new Object[] { card_id, userId };
		int result = updateBySql(nativeSql, names, values);
		if (result != 1) {
			throw new UserException("更新用户实名认证失败！", 1);
		}
	}

	@Override
	public void modify(long userId, String cardId, String cardPositive, String cardOpposite) {
		
		if (!StringUtil.isCard(cardId)) {
			throw new UserException("身份证格式不对，请检查！", 1);
		}
		String sexNum;
		int length = cardId.length();
		if (length == 15) {
			sexNum = cardId.substring(length - 1);
		} else {
			sexNum = cardId.substring(length - 2, length - 1);
		}
		// 获取性别 1:男,0:女
		int sex = Integer.parseInt(sexNum) % 2;
		String nativeSql = "UPDATE rd_user_cache SET card_id = :card_id,sex = :sex,card_positive = :card_positive,"
				+ "card_opposite = :card_opposite WHERE user_id = :userId";
		
		String[] names = new String[] { "card_id", "sex", "card_positive", "card_opposite", "userId" };
		Object[] values = new Object[] { cardId, sex, cardPositive, cardOpposite, userId };
		int result = updateBySql(nativeSql, names, values);
		if (result != 1) {
			throw new UserException("更新用户实名认证失败！", 1);
		}
	}

	@Override
	public void modifyPwdTime(long userId) {
		String nativeSql = "UPDATE rd_user_cache SET pwd_modify_time = LOCALTIME() WHERE user_id = :userId";
		String[] names = new String[] { "userId" };
		Object[] values = new Object[] { userId };
		int result = updateBySql(nativeSql, names, values);
		if (result != 1) {
			throw new UserException("更新用户实名认证失败！");
		}
	}
	
	@Override
	public void modifyPayPwdTime(long userId) {
		String nativeSql = "UPDATE rd_user_cache SET pay_pwd_modify_time = LOCALTIME() WHERE user_id = :userId";
		String[] names = new String[] { "userId" };
		Object[] values = new Object[] { userId };
		int result = updateBySql(nativeSql, names, values);
		if (result != 1) {
			throw new UserException("更新用户实名认证失败！");
		}
	}

	@Override
	public void userLockEdit(long userId, int status) {
		String nativeSql = "UPDATE rd_user_cache SET status = :status ,login_fail_times =0  WHERE user_id = :userId";
		String[] names = new String[] { "status", "userId" };
		Object[] values = new Object[] { status, userId };
		int result = updateBySql(nativeSql, names, values);
		if (result != 1) {
			throw new UserException("锁定用户或解锁用户失败！", 1);
		}
	}

	@Override
	public UserCache findByUserId(long userId) {
		return this.findObjByProperty("user.userId", userId);
	}

	@Override
	public void updateStatus() {
		String sql = "UPDATE UserCache SET status = 0 WHERE status = 1";
		em.createQuery(sql).executeUpdate();
	}
	@Override
	public void modifyApi(long userId, String api_id, String api_status,String apiUserCustId) {
		String nativeSql = "UPDATE rd_user_cache SET api_id = :api_id,api_status = :api_status,api_usercust_id=:apiUserCustId WHERE user_id = :userId";
		String[] names = new String[] { "api_id", "api_status",  "userId","apiUserCustId" };
		Object[] values = new Object[] { api_id, api_status,  userId,apiUserCustId };
		int result = updateBySql(nativeSql, names, values);
		if (result != 1) {
			throw new UserException("更新用户第三方账号失败！", 1);
		}
	}

	@Override
	public int countByBusinessRegistrationNumber(String businessRegistrationNumber,long userId) {
		return countByCriteria(QueryParam.getInstance().addParam("businessRegistrationNumber", businessRegistrationNumber).addParam("user.userId", Operators.NOTEQ, userId));
	}
}
