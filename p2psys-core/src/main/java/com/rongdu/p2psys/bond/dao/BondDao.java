package com.rongdu.p2psys.bond.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.model.BondModel;

/**
 * 债权DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public interface BondDao extends BaseDao<Bond> {

    /**
     * 查询债权
     * 
     * @param id 主键ID
     * @return 债权实体
     */
    Bond getBondById(long id); 
    
    /**
     * 债权一览
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondModel> getBondList(BondModel model);
    
    /**
     * 正在转让中的债权金额
     * @param tenderId
     * @param type
     * @return
     */
    double getSellingBondMoneyByTenderId(long tenderId, byte type);
    
    /**
     * 已经转让完成的债权金额
     * @param tenderId
     * @param type
     * @return
     */
    double getSoldBondMoneyByTenderId(long tenderId, byte type);
    
    /**
     * 可转让债权
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondModel> getBondModelPage(BondModel model);    
    
    /**
     * 转让中债权
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondModel> getSellingBondList(BondModel model);
    
    /**
     * 已转出债权
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondModel> getSoldBondList(BondModel model);

    /**
     * 取得最大的债权转让编号
     * @param date
     * @return
     */
    long getMaxDayId(String date);
    
    /**
     * 停止债权转让
     * @param borrowId
     */
	void stopBond(long borrowId, byte status);
	
	/**
	 * 获取已经售出的债权待收括本
	 * @param kfId
	 * @param type
	 * @return
	 */
	double getSoldCapitalByKfId(long kfId, byte type);
	
	/**
	 * 获取已经售出的债权待收利息
	 * @param kfId
	 * @return
	 */
	double getSoldInterestByKfId(long kfId, byte type);
    
}
