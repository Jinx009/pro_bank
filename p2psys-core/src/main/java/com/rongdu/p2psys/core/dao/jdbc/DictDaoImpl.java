package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.domain.Dict;

@Repository("dictDao")
public class DictDaoImpl extends BaseDaoImpl<Dict> implements DictDao {

	@Override
	public PageDataList<Dict> list(QueryParam param) {
		return findPageList(param);
	}

	@Override
	public Dict find(String nid, String value) {
		QueryParam param = QueryParam.getInstance();
		if (StringUtil.isNumber(value)) {
			param.addParam("nid", nid);
		}
		param.addParam("status", 1);
		param.addParam("value", value);
		return super.findByCriteriaForUnique(param);
	}
	
	@Override
	public Dict findByName(String name) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("name", name);
		return super.findByCriteriaForUnique(param);
	}
	
	@Override
	public List<Dict> list(String nid) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("nid", nid);
		param.addParam("status", 1);
		param.addOrder(OrderType.ASC, "sort");
		return super.findByCriteria(param);
	}

	@Override
	public void delete(long id) {
		String sql = "UPDATE Dict SET status = 0 WHERE id = :id";
		Query query = em.createQuery(sql);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new BussinessException("删除/禁用数据字典信息失败！", 1);
		}
	}

}
