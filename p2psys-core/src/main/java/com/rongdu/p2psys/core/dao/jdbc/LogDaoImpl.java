package com.rongdu.p2psys.core.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.LogDao;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.model.LogModel;

/**
 * 系统操作日志DAO接口
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-04-03
 */
@Repository("logDao")
public class LogDaoImpl extends BaseDaoImpl<Log> implements LogDao {

	@SuppressWarnings("unchecked")
	public Log getLogById(long id) {
		String jpql = "from Log where id = ?1";
		Query query = em.createQuery(jpql);
		query.setParameter(1, id);
		List<Log> list = query.getResultList();
		if (list != null && list.size() >= 0) {
			return (Log) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public PageDataList<LogModel> list(LogModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (StringUtil.isNotBlank(model.getType())) {
				param.addParam("type", model.getType());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GT, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<Log> pageDateList = super.findPageList(param);

		PageDataList<LogModel> pageDateList_ = new PageDataList<LogModel>();
		List<LogModel> list = new ArrayList<LogModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				Log log = (Log) pageDateList.getList().get(i);
				LogModel m = LogModel.instance(log);
				list.add(m);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

}
