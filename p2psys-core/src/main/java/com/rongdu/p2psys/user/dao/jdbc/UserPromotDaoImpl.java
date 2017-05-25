package com.rongdu.p2psys.user.dao.jdbc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserPromotModel;

@Repository("userPromotDao")
public class UserPromotDaoImpl extends BaseDaoImpl<UserPromot> implements UserPromotDao {

	@Override
	public PageDataList<UserPromot> userPromotList(QueryParam param) {
		return super.findPageList(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserPromot getUserPromotByUserId(User user) {
		String sql = " FROM UserPromot where user = ?1 ";
		Query query = em.createQuery(sql).setParameter(1, user);
		List<UserPromot> list = query.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasPromotByCouponCode(String code) {
		String sql = " FROM UserPromot where couponCode = ?1 ";
		Query query = em.createQuery(sql).setParameter(1, code);
		List<UserPromot> list = query.getResultList();
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public UserPromot getUserPromotByCode(String code) {
		return this.findObjByProperty("couponCode", code);
	}

	@Override
	public List<UserPromotModel> exportUserPromotList(int status) {
		String sql="";
		if (status == 0)
		{
			sql ="select a.id,a.status,a.user_id,a.coupon_code,a.can_use_times,a.used_times,a.rate,a.add_time,b.user_name,b.real_name from rd_user_promot a,rd_user b where  a.user_id=b.user_id";
		}else{
			sql ="select a.id,a.status,a.user_id,a.coupon_code,a.can_use_times,a.used_times,a.rate,a.add_time,b.user_name,b.real_name from rd_user_promot a,rd_user b where a.status="+status+" and a.user_id=b.user_id";
		}
		
//		 "id", "userName", "realName",
//			"status", "couponCode", "canUseTimes", "usedTimes", "rate",
//			"addTime" 
//		
		
		Query query = em.createNativeQuery(sql.toString());
		List<UserPromotModel> list = new ArrayList<UserPromotModel>();
		List rows = query.getResultList();
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        UserPromotModel userPromotModel=new UserPromotModel();
	        userPromotModel.setId(Long.parseLong(cells[0].toString()));
	        userPromotModel.setStatus((boolean)cells[1]==true?1:2);
	        //userPromotModel.set((long)cells[2]);
	        userPromotModel.setCouponCode(cells[3]==null?"":cells[3].toString());
	        userPromotModel.setCanUseTimes((int)cells[4]);
	        userPromotModel.setUsedTimes((int)cells[5]);
	        userPromotModel.setRate(Double.parseDouble(cells[6]==null?"0":cells[6].toString()));
	        userPromotModel.setAddTime((java.util.Date)cells[7]);
	        userPromotModel.setUserName(cells[8]==null?"":cells[8].toString());
	        userPromotModel.setRealName(cells[9]==null?"":cells[9].toString());
	        list.add(userPromotModel);
	    }  

//		List<UserPromotModel> list = query.getResultList();
		
		return list;
	}

	@Override
	public PageDataList<UserPromot> userPromotList(String sql, QueryParam param)
	{
		return super.findPageListBySql(sql, param);
	}

}
