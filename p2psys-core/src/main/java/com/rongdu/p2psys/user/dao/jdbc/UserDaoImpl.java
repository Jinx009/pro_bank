package com.rongdu.p2psys.user.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.customer.model.AccountLogModel;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserPromotModel;

/**
 * 用户DAO实现类
 * 
 * @author ZhuJunjie
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao
{

	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Override
	public int count(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT COUNT(DISTINCT t.user_id) FROM rd_user t WHERE 1 = 1 ");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public int countByEmail(String email, long userId)
	{
		return countByCriteria(QueryParam.getInstance()
				.addParam("email", email)
				.addParam("userId", Operators.NOTEQ, userId));
	}

	@Override
	public User modifyPwd(User user)
	{
		String sql = "UPDATE User SET pwd = :pwd WHERE userId = :userId";
		user.setPwd(MD5.encode(user.getPwd()));
		Query query = em.createQuery(sql);
		query.setParameter("pwd", user.getPwd());
		query.setParameter("userId", user.getUserId());
		int result = query.executeUpdate();
		logger.debug("用户修改密码, userId:" + user.getUserId() + " ,修改结果：" + result);
		if (result != 1)
		{
			throw new UserException("密码修改失败！");
		}
		return user;
	}

	@Override
	public User modifyPaypwd(User user)
	{
		String sql = "UPDATE User SET payPwd = :payPwd WHERE userId = :userId";
		user.setPayPwd(MD5.encode(user.getPayPwd()));
		Query query = em.createQuery(sql);
		query.setParameter("payPwd", user.getPayPwd());
		query.setParameter("userId", user.getUserId());
		int result = query.executeUpdate();
		logger.debug("用户修改交易密码, userId:" + user.getUserId() + " ,修改结果："
				+ result);
		if (result != 1)
		{
			throw new UserException("支付密码修改失败！");
		}
		return user;
	}

	public void modifyRealname(long userId, String realName)
	{
		String sql = "UPDATE User SET realName = :realName WHERE userId = :userId";
		Query query = em.createQuery(sql);
		query.setParameter("realName", realName);
		query.setParameter("userId", userId);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new UserException("更新用户实名认证失败！", 1);
		}
	}

	@Override
	public void modifyEmail(long userId, String email)
	{
		String sql = "UPDATE User SET email = :email WHERE userId = :userId";
		Query query = em.createQuery(sql);
		query.setParameter("email", email);
		query.setParameter("userId", userId);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new UserException("更新用户电子邮箱失败！", 1);
		}
	}

	@Override
	public void modifyPhone(long userId, String mobilePhone)
	{
		String sql = "UPDATE User SET mobilePhone = :mobilePhone,userName = :mobilePhone WHERE userId = :userId";
		Query query = em.createQuery(sql);
		query.setParameter("mobilePhone", mobilePhone);
		query.setParameter("userId", userId);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new UserException("更新用户电话号码失败！", 1);
		}
	}
	
	@Override
	public PageDataList<UserModel> userList(int pageNumber, int pageSize,
			UserModel model) {

		StringBuffer strSql = new StringBuffer();
		strSql.append(" select u.user_id,u.user_name,u.real_name,u.email,u.mobile_phone,u.add_time, ")
			  .append(" u.card_id,c.status,c.sex,c.user_nature,i.real_name_status,c.user_type from ")
			  .append(" rd_user u join rd_user_cache c on u.user_id = c.user_id ")
			  .append(" join rd_user_identify i on u.user_id = i.user_id where u.user_name is not null ");

		if (!StringUtil.isBlank(model.getSearchName())) {
			strSql.append(" and (u.user_name like '%").append(model.getSearchName()).append("%' ")
				  .append(" or u.real_name like '%").append(model.getSearchName()).append("%') ");
		} else {
			if (model.getSign() == 1) {
				if (model.getStatus() != 99) {
					strSql.append(" and c.status = ").append(model.getStatus());
				}
				if (model.getUserIdentify() != null
						&& model.getUserIdentify().getRealNameStatus() != -2) {
					strSql.append(" and i.real_name_status = ").append(model.getRealNameStatus());
				}else if (model.getRealNameStatus() != -2) {
					strSql.append(" and i.real_name_status = ").append(model.getRealNameStatus());
				}
				
			} else {
				if (model.getStatus() != 99) {
					strSql.append(" and c.status = ").append(model.getStatus());
				}
				if (model.getUserIdentify() != null
						&& model.getUserIdentify().getRealNameStatus() != -2) {
					strSql.append(" and i.real_name_status = ").append(model.getRealNameStatus());
				}else if (model.getRealNameStatus() != -2) {
					strSql.append(" and i.real_name_status = ").append(model.getRealNameStatus());
				}
			}
			
			if (!StringUtil.isBlank(model.getUserName())) {
				strSql.append(" and u.user_name like '%").append(model.getUserName()).append("%' ");
			}
			if (!StringUtil.isBlank(model.getRealName())) {
				strSql.append(" and u.real_name like '%").append(model.getRealName()).append("%' ");
			}
		}
		strSql.append(" order by u.user_id desc ");
		Query query = em.createNativeQuery(strSql.toString());
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		List<UserModel> userList = new ArrayList<UserModel>();
		PageDataList<UserModel> uList = new PageDataList<UserModel>();
		uList.setPage(page);
		if (list.size() > 0) {
			for (Object[] o : list) {
				{
					UserModel userModel = new UserModel();
					userModel.setUserId(o[0] == null ? 0 : Long.parseLong(o[0]
							.toString()));
					userModel.setUserName(o[1] == null ? "" : o[1].toString());
					userModel.setRealName(o[2] == null ? "" : o[2].toString());
					userModel.setEmail(o[3] == null ? "" : o[3].toString());
					userModel.setMobilePhone(o[4] == null ? "" : o[4]
							.toString());
					userModel.setAddTime(o[5] == null ? new Date()
							: (Date) o[5]);
					userModel.setCardId(o[6] == null ? "" : o[6].toString());
					userModel.setStatus(o[7] == null ? 0 : (Boolean)o[7]?1:0 );
					userModel.setSex(o[8] == null ? 0 : (Boolean)o[8]?1:0 );
					userModel.setUserNature(o[9] == null ?0 : (Boolean)o[9]?1:0 );
					userModel.setRealNameStatus(o[10] == null ? 0 : Integer
							.parseInt(o[10].toString()));
					userModel.setUserType(o[11] == null ? 0 : (Boolean)o[11]?1:0 );
					userList.add(userModel);
				}
			}
		}
		uList.setList(userList);
		return uList;
	}

	@Override
	public List<UserModel> getUserModels(Map<String, Object> param) {
		String sql="select a.user_id,a.user_name,a.real_name,u1.user_name as invite_user_name,u1.real_name as invite_real_name,a.email,a.mobile_phone,a.invite_time from (";
		
		sql = sql+"select u.user_id,u.user_name,u.real_name,u.email,u.mobile_phone,ui.invite_user,ui.invite_time from rd_user u,rd_user_invite ui"
				+ " where 1=1";
		if (param.containsKey("inviteUser.userName")) {
			sql = sql + " and u.user_name =" + param.get("inviteUser.userName");
		}
		if (param.containsKey("inviteUser.realName")) {
			sql = sql + " and u.real_name =" + param.get("inviteUser.realName");
		}
		sql = sql + " and u.user_id=ui.user_id ) a,rd_user u1";
		sql=sql+" where u1.user_id=a.invite_user ";
		sql = sql + " order by u1.user_id desc";
		
		
//		"id", "userName", "realName",
//		"inviteUserName", "inviteRealName", "email", "mobilePhone",
//		"inviteTime" 
		
		
		Query query = em.createNativeQuery(sql.toString());
		List<UserModel> list = new ArrayList<UserModel>();
		List rows = query.getResultList();
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        UserModel userModel=new UserModel();
	        userModel.setUserName(cells[1]==null?"":cells[1].toString());
	        userModel.setRealName(cells[2]==null?"":cells[2].toString());
	        userModel.setInviteUserName(cells[3]==null?"":cells[3].toString());
	        userModel.setInviteRealName(cells[4]==null?"":cells[4].toString());
	        userModel.setEmail(cells[5]==null?"":cells[5].toString());
	        userModel.setMobilePhone(cells[6]==null?"":cells[6].toString());
	        userModel.setInviteTime((java.util.Date)cells[7]);
	        list.add(userModel);
	    } 
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<User> getByGroupId(Long bindId)
	{
		String hql = " from User where  bindId = "+bindId+" order by bindId ";
		
		Query query = em.createQuery(hql);
		
		List<User> list = query.getResultList();
		
		return list;
	}
}
