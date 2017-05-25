package com.rongdu.p2psys.nb.borrow.service;

import java.text.ParseException;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.InvestRecordModel;
import com.rongdu.p2psys.nb.borrow.model.InvestDetailModel;

public interface BorrowTenderService {

	/**
	 * 保存投资记录
	 * 
	 * @param borrowTender
	 * @return BorrowTender
	 */
	BorrowTender saveBorrowTender(BorrowTender borrowTender);

	/**
	 * 更新投资记录
	 * 
	 * @param borrowTender
	 */
	void updateBorrowTender(BorrowTender borrowTender);

	/**
	 * 当前多身份用户 — 投资列表信息查询
	 * 
	 * @param model
	 * @return PageDataList<InvestRecordModel>
	 */
	PageDataList<InvestRecordModel> multipleIdentitiesList(
			InvestRecordModel model) throws ParseException;

	/**
	 * 当前多身份用户 — 投资列表信息查询
	 * 
	 * @param model
	 * @return PageDataList<BorrowTenderModel>
	 */
	PageDataList<BorrowTenderModel> multiple(BorrowTenderModel model);
	
	/**
	 * 获取投资协议
	 * @param 投资协议ID
	 * @return 投资协议
	 */
	String  builderTenderProcol(String type,long id);

	/**
	 * 获取投资者列表信息（PC）
	 * 
	 * @param model
	 * @return PageDataList<InvestDetailModel>
	 */
	PageDataList<InvestDetailModel> getInvestRecordByItem(
			InvestDetailModel model);

	/**
	 * 撤销投资记录
	 * 
	 * @param borrowId
	 */
	void cancelBorrowTender(Long borrowId);
	
	/**
	 * 锁定金额明细
	 * 
	 * @param model
	 * @return PageDataList<InvestRecordModel>
	 */
	PageDataList<InvestRecordModel> lockCashRecord(
			InvestRecordModel model);
	

}
