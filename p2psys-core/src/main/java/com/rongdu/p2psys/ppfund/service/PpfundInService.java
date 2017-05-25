package com.rongdu.p2psys.ppfund.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.user.domain.User;

public interface PpfundInService {
	/**
	 * 转入
	 * 
	 * @param model
	 * @return PpfundIn
	 */
	PpfundIn ppfundIn(PpfundInModel model);

	/**
	 * 获取该PPfund转入记录
	 * 
	 * @param ppfundId
	 * @param page
	 * @param size
	 * @return
	 */
	PageDataList<PpfundInModel> list(long ppfundId, int page, int size);

	/**
	 * 处理用户转入记录是否转出或复利
	 */
	void doPpfundIn();

	/**
	 * 根据用户获取购买记录
	 * 
	 * @param user
	 * @return
	 */
	PageDataList<PpfundInModel> pageList(PpfundInModel model);

	/**
	 * 获取用户购买记录
	 * 
	 * @param ppfundId
	 * @param page
	 * @param size
	 * @return
	 */
	PageDataList<PpfundInModel> pageList(long ppfundId, int page, int size);

	/**
	 * 根据ID获取记录
	 * 
	 * @param id
	 * @return
	 */
	PpfundIn getById(long id);

	/**
	 * 修改
	 * 
	 * @param in
	 */
	void ppfundIdEdit(PpfundIn in);

	/**
	 * 统计待收本金
	 * 
	 * @param user
	 * @return
	 */
	double getCollectionCapitalByUser(User user);

	/**
	 * 统计待收利息
	 * 
	 * @param user
	 * @return
	 */
	double getCollectionInterestByUser(User user);

	/**
	 * 统计用户投资总额
	 * 
	 * @param user
	 * @param ppfund
	 * @return
	 */
	double getMostAccountTotalByUserAndPpfund(User user, Ppfund ppfund);

	/**
	 * ppfund关闭以后处理未设置转出时间的记录
	 * 
	 * @param ppfund
	 */
	void doPpfundInOutTime(Ppfund ppfund);

	/**
	 * 转出利息
	 * 
	 * @return
	 */
	double outInterest();

	/**
	 * 获取最后一笔投资
	 * 
	 * @param id
	 * @return
	 */
	PpfundIn getLastInByPpfundId(long id);

	/**
	 * 转出
	 * 
	 * @param in
	 * @return
	 */
	PpfundOut doOut(PpfundOut out);

	/**
	 * 根据时间段获取转出金额
	 * 
	 * @param date
	 * @return
	 */
	double getOutAmountByDate(String date);

	/**
	 * 根据时间段获取转入金额
	 * 
	 * @param date
	 * @return
	 */
	double getInAmountByDate(String date);

	/**
	 * 获取PPFUNDIN投资顺序
	 */
	int getUserPpfundInOrder(long ppfundInId, long ppfundId);

	/**
	 * 获取所有的金额
	 */
	double getAllMoney();

	/**
	 * 根据ppfundId查询ppfundInList
	 * 
	 * @param ppfundId
	 * @return List<PpfundIn>
	 */
	List<PpfundIn> getPpfundInListByPpfundId(Long ppfundId);
}
