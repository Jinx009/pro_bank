package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowConfigDao;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.model.BorrowConfigModel;

@Repository("borrowConfigDao")
public class BorrowConfigDaoImpl extends BaseDaoImpl<BorrowConfig> implements BorrowConfigDao {

	@Override
	public PageDataList<BorrowConfig> list(BorrowConfigModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
				SearchFilter orFilter1 = new SearchFilter("name", Operators.LIKE, model.getSearchName());
				SearchFilter orFilter2 = new SearchFilter("cname", Operators.LIKE, model.getSearchName());
				param.addOrFilter(orFilter1, orFilter2);
			}
			param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.DESC, "id");

		return super.findPageList(param);
	}

	@Override
	public List<BorrowConfig> findAllOutFlow() {
		QueryParam param = QueryParam.getInstance().addParam("cname", Operators.NOTEQ, "flow");
		return findByCriteria(param);
	}
	@Override
	public List<BorrowConfig> findAll() {
		QueryParam param = QueryParam.getInstance().addParam("enable", true).addOrder(OrderType.DESC, "id");
		return findByCriteria(param);
	}

	@Override
	public List<BorrowConfig> findAllNotFlowAndSecond() {
		QueryParam param = QueryParam.getInstance().addParam("enable", true)
				.addParam("cname", Operators.NOTEQ, "miaobiao")
				.addParam("cname", Operators.NOTEQ, "flow");
		return findByCriteria(param);
	}

}
