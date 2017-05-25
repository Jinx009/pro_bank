package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.p2psys.borrow.dao.BorrowAutoDao;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.model.BorrowModel;

@Repository("borrowAutoDao")
public class BorrowAutoDaoImpl extends BaseDaoImpl<BorrowAuto> implements BorrowAutoDao {

	@Override
	public int rank(long userId) {
		BorrowAuto auto = super.findObjByProperty("user.userId", userId);
		QueryParam param = QueryParam.getInstance();
		param.addParam("enable", 1);
		param.addParam("updateTime", Operators.GTE, auto.getUpdateTime());
		return super.countByCriteria(param) + 1;
	}

	@Override
	public int count() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("enable", 1);
		return super.countByCriteria(param);
	}

	@Override
	public List<BorrowAuto> list(BorrowModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("enable", true);
		param.addParam("user.userId", Operators.NOTEQ, model.getUserId());
		/*param.addParam("money", Operators.GTE, borrow.getLowestAccount());*/
		// 根据更新时间正序排序，投标成功后则更新为当前时间
		param.addOrder("updateTime"); 
		List<BorrowAuto> list = findByCriteria(param);
		return list;
	}
}
