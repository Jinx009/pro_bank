package com.rongdu.p2psys.user.dao.jdbc;

import java.util.Date;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserIdentifyModel;

/**
 * 认证信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月17日17:27:02
 */
@Repository("userIdentifyDao")
public class UserIdentifyDaoImpl extends BaseDaoImpl<UserIdentify> implements UserIdentifyDao {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(UserCreditDaoImpl.class);

	@Override
	public UserIdentify findByUserId(long userId) {
		return this.findObjByProperty("user.userId", userId);
	}

	/**
	 * 更具主键查询
	 * 
	 * @param userIdentifyId
	 * @return
	 */
	@Override
	public UserIdentify findById(long userIdentifyId) {
		return find(userIdentifyId);
	}

	@Override
	public void modifyRealnameStatus(long userId, int status, int preStatus) {
		String nativeSql = "UPDATE rd_user_identify " + "SET real_name_status = :status " + "WHERE user_id = :userId "
				+ "AND real_name_status = :preStatus";
		String[] names = new String[] { "status", "userId", "preStatus" };
		Object[] values = new Object[] { status, userId, preStatus };
		int count = updateBySql(nativeSql, names, values);
		if (count != 1) {
			throw new UserException("更新用户实名认证状态失败！", 1);
		}
		this.refresh(this.findByUserId(userId));
	}
	@Override
    public void modifyRealnameStatus(long userId, int status) {
        String nativeSql = "UPDATE rd_user_identify " + "SET real_name_status = :status , real_name_verify_time = now() " + "WHERE user_id = :userId ";
        String[] names = new String[] { "status", "userId"};
        Object[] values = new Object[] { status, userId};
        int count = updateBySql(nativeSql, names, values);
        if (count != 1) {
            throw new UserException("更新用户实名认证状态失败！", 1);
        }
        this.refresh(this.findByUserId(userId));
    }
	@Override
	public void modifyEmailStatus(long userId, int status, int preStatus) {
		String nativeSql = "UPDATE rd_user_identify " + "SET email_status = :status " + "WHERE user_id = :userId "
				+ "AND email_status = :preStatus";
		String[] names = new String[] { "status", "userId", "preStatus" };
		Object[] values = new Object[] { status, userId, preStatus };
		int count = updateBySql(nativeSql, names, values);
		if (count != 1) {
			throw new UserException("更新用户邮箱激活状态失败！", 1);
		}
		this.refresh(this.findByUserId(userId));
	}

	@Override
	public void modifyMobilePhoneStatus(long userId, int status, int preStatus) {
		String nativeSql = "UPDATE rd_user_identify " + "SET mobile_phone_status = :status "
				+ "WHERE user_id = :userId " + "AND mobile_phone_status = :preStatus";
		String[] names = new String[] { "status", "userId", "preStatus" };
		Object[] values = new Object[] { status, userId, preStatus };
		int count = updateBySql(nativeSql, names, values);
		if (count != 1) {
			throw new UserException("更新用户手机绑定状态失败！", 1);
		}
		this.refresh(this.findByUserId(userId));
	}
	@Override
    public void modifyMobilePhoneStatus(long userId, int status) {
        String nativeSql = "UPDATE rd_user_identify " + "SET mobile_phone_status = :status , mobile_phone_verify_time =now() "
                + "WHERE user_id = :userId " ;
        String[] names = new String[] { "status", "userId" };
        Object[] values = new Object[] { status, userId};
        int count = updateBySql(nativeSql, names, values);
        if (count != 1) {
            throw new UserException("更新用户手机绑定状态失败！", 1);
        }
        this.refresh(this.findByUserId(userId));
    }
	@Override
	public UserIdentifyModel getUserIdentifyByUserId(long userId) {
		String sql = "SELECT user.user_name,attestation.* FROM rd_user AS  user," + "rd_user_identify AS attestation "
				+ "WHERE user.user_id=attestation.user_id AND attestation.user_id =:userId";
		String[] names = new String[] { "userId" };
		Object[] values = new Object[] { userId };
		return findForUniqueBySql(sql, names, values, UserIdentifyModel.class);
	}

	@Override
	public void userAttestationEdit(long id, String realNameVerifyRemark, int realNameStatus, Operator operator) {
		String jpql = "UPDATE UserIdentify SET realNameStatus = :realNameStatus, "
				+ "realNameVerifyTime= :realNameVerifyTime WHERE id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("realNameStatus", realNameStatus);
		query.setParameter("realNameVerifyTime", new Date());
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new UserException("实名认证审核失败！");
		}
	}

	@Override
	public int countByRealName(int status) {
		QueryParam param = QueryParam.getInstance().addParam("realNameStatus", status);
		return countByCriteria(param);
	}

	@Override
	public int countByRealName(int status, String startTime, String endTime){
		StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT t.user_id) FROM rd_user_identify t WHERE 1=1 ");
		if (StringUtil.isNotBlank(startTime)) {
			sql.append(" AND real_name_verify_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime)) {
			sql.append(" AND real_name_verify_time <= :endTime");
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
	public int countByMobilePhone(String mobilePhone) {
		QueryParam param = QueryParam.getInstance().addParam("user.mobilePhone", mobilePhone)
				.addParam("mobilePhoneStatus", 1);
		return countByCriteria(param);
	}

	@Override
	public int countByEmail(String email) {
		QueryParam param = QueryParam.getInstance().addParam("user.email", email).addParam("emailStatus", 1);
		return countByCriteria(param);
	}

}
