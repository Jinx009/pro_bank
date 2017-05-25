package com.rongdu.p2psys.nb.borrow.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.domain.Operator;

public interface BorrowService {

	/**
	 * 根据ID获取实体类
	 * 
	 * @param id
	 * @return Borrow
	 */
	Borrow getById(Long id);

	/**
	 * 判断是否流标
	 * 
	 * @param borrowId
	 * @return 是否流标
	 */
	Boolean isSpreadBorrow(Long id);

	/**
	 * 后台分页查询
	 * 
	 * @param model
	 * @return PageDataList<BorrowModel>
	 */
	PageDataList<BorrowModel> manageList(BorrowModel model);
	
	/**
	 * 招标管理页面
	 * 
	 * @param model
	 * @return PageDataList<BorrowModel>
	 */
	PageDataList<BorrowModel> biddingManageList(BorrowModel model);

	/**
	 * 后台添加非现金产品
	 * 
	 * @param borrowModel
	 * @return Borrow
	 */
	Borrow addBorrow(BorrowModel borrowModel);

	/**
	 * 后台修改非现金产品
	 * 
	 * @param borrow
	 */
	void updateBorrow(BorrowModel borrowModel);

	/**
	 * 后台初审非现金类产品
	 * 
	 * @param model
	 * @param operator
	 */
	void confirmBorrow(BorrowModel model, Operator operator);

	/**
	 * 后台满标复审非现金类产品
	 * 
	 * @param model
	 * @param operator
	 */
	void verifyFullBorrow(BorrowModel model, Operator operator);

	/**
	 * 后台管理员撤标
	 * 
	 * @param borrow
	 */
	void cancelBorrow(Borrow borrow);

	/**
	 * 后台管理员截标
	 * 
	 * @param borrow
	 */
	void stopBorrow(Borrow borrow, Operator operator);
	/**
	 * 是否省略复审
	 */
	boolean isSkipReview(Borrow borrow);
}
