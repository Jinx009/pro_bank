package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.WechatUserDao;
import com.rongdu.p2psys.crowdfunding.domain.WechatUser;

@Repository("wechatUserDao")
public class WechatUserDaoImpl extends BaseDaoImpl<WechatUser> implements WechatUserDao{

	@SuppressWarnings("unchecked")
	public List<WechatUser> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<WechatUser> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
