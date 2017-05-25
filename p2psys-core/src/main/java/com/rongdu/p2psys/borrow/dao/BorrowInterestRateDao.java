package com.rongdu.p2psys.borrow.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.user.domain.User;

/**
 * @author sj
 *
 */
public interface BorrowInterestRateDao extends BaseDao<BorrowInterestRate> {

	public List<BorrowInterestRate> findByStatus(int status);

	public List<BorrowInterestRate> findByStatusAndAddTime(int status);

	public List<BorrowInterestRate> findByStatusAndUser(int status, User user);

	public BorrowInterestRate findByStatusAndInterestRateValue(int status,
			double interestRateValue, User user);

	public BorrowInterestRate findByUserId(long userId);

	/**
	 * 根据投标记录和状态获取加息券
	 * @param status
	 * @param tender
	 * @return
	 */
	public BorrowInterestRate findByStatusAndTender(int status,BorrowTender tender);
	
}
