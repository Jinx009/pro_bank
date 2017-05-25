package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.LogTemplateDao;
import com.rongdu.p2psys.core.domain.LogTemplate;

@Repository("logTemplateDao")
public class LogTemplateDaoImpl extends BaseDaoImpl<LogTemplate> implements LogTemplateDao {

	@Override
	public PageDataList<LogTemplate> logTemplateList(int pageNumber, int pageSize, LogTemplate logTemplate) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (!StringUtil.isBlank(logTemplate.getLogType())) {
			param.addParam("logType", Operators.EQ, logTemplate.getLogType());
		}
		if (logTemplate.getType() != 0) {
			param.addParam("type", logTemplate.getType());
		}
		param.addOrder(OrderType.DESC, "id");
		return super.findPageList(param);
	}

}
