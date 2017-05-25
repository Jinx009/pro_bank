package com.rongdu.p2psys.nb.borrow.service;

import com.rongdu.p2psys.user.domain.User;


public interface BorrowCollectionService
{
	/**
	 * 	累计净收益(多身份)
	 * @param user
	 * @return
	 */
	public double netProfit(User user);
	
	 /**
     * 根据用户获取待收利息总额(多身份)
     * @param user
     * @return 待收利息总额
     */
    double getInterestByUser(User user);
    
    /**
     * 在投金额
     * @param user
     * @return
     */
    public double inInvestAmount(User user, int status);
}
