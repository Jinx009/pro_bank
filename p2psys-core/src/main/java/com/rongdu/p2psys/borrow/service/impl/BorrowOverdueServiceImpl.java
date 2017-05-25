package com.rongdu.p2psys.borrow.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowOverdueDao;
import com.rongdu.p2psys.borrow.domain.BorrowOverdue;
import com.rongdu.p2psys.borrow.service.BorrowOverdueService;

@Service("borrowOverdueService")
public class BorrowOverdueServiceImpl implements BorrowOverdueService {

	/**
	 * 注入BorrowOverdueDao
	 */
	@Resource
	private BorrowOverdueDao borrowOverdueDao;

	@Override
	public PageDataList<BorrowOverdue> list(BorrowOverdue model, int currentPage, Date startTime, Date endTime) {
		QueryParam param = QueryParam.getInstance();
		param.addPage(currentPage);
		if (!StringUtil.isBlank(model.getUsername())) {
			param.addParam("username", Operators.EQ, model.getUsername());
		}
		if (startTime != null) {
			param.addParam("overdueTime", Operators.GTE, startTime);
		}
		if (endTime != null) {
			param.addParam("overdueTime", Operators.LTE, DateUtil.rollDay(endTime, 1));
		}
		PageDataList<BorrowOverdue> plist = borrowOverdueDao.findPageList(param);
		return plist;
	}

	@Override
	public void save(BorrowOverdue model) {
		borrowOverdueDao.save(model);
	}

}
