package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.WechatActiveFactoryDao;
import com.rongdu.p2psys.crowdfunding.domain.WechatActiveFactory;

@Repository("wechatActiveFactoryDao")
public class WechatActiveFactoryDaoImpl extends BaseDaoImpl<WechatActiveFactory> implements WechatActiveFactoryDao{

	@SuppressWarnings("unchecked")
	public List<WechatActiveFactory> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<WechatActiveFactory> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
