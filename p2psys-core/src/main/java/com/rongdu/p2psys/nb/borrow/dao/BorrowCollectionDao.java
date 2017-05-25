package com.rongdu.p2psys.nb.borrow.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.user.domain.User;

public interface BorrowCollectionDao extends BaseDao<BorrowCollection>
{
	/**
     * 累计净收益（多身份）
     * 
     * @param user
     * @return
     */
    public double netProfit(User user);
    
    /**
     * 根据用户获取待收利息总额(多身份)
     * @param user
     * @return 待收利息总额（待收利息-转出利息+加息券待收利息）
     */
    double getInterestByUser(User user);
    
    /**
     * 在投金额
     * @param user
     * @return
     */
    public double inInvestAmount(User user, int status);
    
    /**
     * 计算浮动收益
     * @param tenderId
     * @param userId
     * @return
     */
    public double getFloatRate(double tenderId,double userId);

}
