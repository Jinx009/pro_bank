package com.rongdu.p2psys.core.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.SystemConfigDao;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.model.SystemConfigModel;

@Repository("systemConfigDao")
public class SystemConfigDaoImpl extends BaseDaoImpl<SystemConfig> implements SystemConfigDao {

	@Override
	public List<SystemConfig> findSystemListByStatus(int status) {
		QueryParam param = QueryParam.getInstance().addParam("status", status);
		return this.findByCriteria(param);
	}

	@Override
	public PageDataList<SystemConfigModel> list(SystemConfigModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
				SearchFilter orFilter1 = new SearchFilter("name", Operators.LIKE, model.getSearchName());
				SearchFilter orFilter2 = new SearchFilter("nid", Operators.LIKE, model.getSearchName());
				param.addOrFilter(orFilter1, orFilter2);
			}else{ //精确查询条件
				if (model.getStatus() != 99) {
					param.addParam("status", model.getStatus());
				}
				if (model.getType() != 99) {
					param.addParam("type", model.getType());
				}
				if (StringUtil.isNotBlank(model.getName())) {
					param.addParam("name", Operators.EQ, model.getName());
				}
				if (StringUtil.isNotBlank(model.getNid())) {
					param.addParam("nid", Operators.EQ, model.getNid());
				}
			}
			param.addPage(model.getPage(), model.getRows());
		}
		param.addOrder(OrderType.DESC, "id");

		PageDataList<SystemConfig> pageDateList = super.findPageList(param);
		PageDataList<SystemConfigModel> pageDateList_ = new PageDataList<SystemConfigModel>();
		List<SystemConfigModel> list = new ArrayList<SystemConfigModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				SystemConfig sc = (SystemConfig) pageDateList.getList().get(i);
				SystemConfigModel scm = SystemConfigModel.instance(sc);
				list.add(scm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SystemConfig findByHql(String hql)
	{
		Query query = (Query) em.createQuery(hql);
		
		SystemConfig systemConfig = null;
		
		List<SystemConfig> list = query.getResultList();
		
		if(list!=null&&!list.isEmpty())
		{
			systemConfig = list.get(0);
		}
		
		return systemConfig;
	}

}
