package com.rongdu.p2psys.borrow.service;

import java.util.Date;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowOverdue;

public interface BorrowOverdueService {

	/**
	 * 逾期垫付列表
	 * @param model BorrowOverdue实体类
	 * @param currentPage 当前页
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return PageDataList<BorrowOverdue>
	 */
	PageDataList<BorrowOverdue> list(BorrowOverdue model, int currentPage, Date startTime, Date endTime);

	/**
	 * 保存实体
	 * @param model BorrowOverdue实体类
	 */
	void save(BorrowOverdue model);

}
