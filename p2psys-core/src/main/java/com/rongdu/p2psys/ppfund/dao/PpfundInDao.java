package com.rongdu.p2psys.ppfund.dao;

import java.util.Date;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品转入
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月20日
 */
public interface PpfundInDao extends BaseDao<PpfundIn> {
	/**
	 * 根据状态查询用户转入记录
	 * 
	 * @param status
	 * @return
	 */
	List<PpfundIn> getlist(int isOut);

	/**
	 * 查询到期应转出记录
	 * 
	 * @return
	 */
	List<PpfundIn> getExpireList();
	
	/**
	 * 获取PPfund最近一年购买金额
	 * @return
	 */
	double getLastYearInMoney(Date startTime, Date endTime, User user);
	
	/**
	 * 统计待收本金
	 * @param user
	 * @return
	 */
	double getCollectionCapitalByUser(User user);
	
	/**
	 * 统计待收利息
	 * @param user
	 * @return
	 */
	double getCollectionInterestByUser(User user);
	
	/**
	 * 获取最后一笔投资
	 * 
	 * @param id
	 * @return
	 */
	PpfundIn getLastInByPpfundId(long id);
	
	/**
	 * 统计用户投资总额
	 * @param user
	 * @param ppfund
	 * @return
	 */
	double getMostAccountTotalByUserAndPpfund(User user, Ppfund ppfund);
	
	/**
	 * 统计未设置转出时间的借款
	 * @param ppfund
	 * @return
	 */
	List<PpfundIn> getNoOutTimePpfundIn(Ppfund ppfund);
	
	/**
	 * 转出利息
	 * 
	 * @return
	 */
	double outInterest();
	
	/**
	 * 根据时间段获取转出金额
	 * 
	 * @param date
	 * @return
	 */
	double getOutAmountByDate(String date);
	
	/**
	 * 根据时间段获取转入金额
	 * @param date
	 * @return
	 */
	double getInAmountByDate(String date);
}
