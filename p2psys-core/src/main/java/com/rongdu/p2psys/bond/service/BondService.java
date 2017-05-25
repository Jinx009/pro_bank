package com.rongdu.p2psys.bond.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.model.BondModel;

/**
 * 债权Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public interface BondService {

    /**
     * 添加债权
     * 
     * @param bond 债权实体
     */
    Bond addBond(Bond bond);

    /**
     * 查询债权
     * 
     * @param id 主键ID
     * @return 债权
     */
    Bond getBondById(long id);
    
    /**
     * 查询债权详情
     * @param model
     * @return
     */
    BondModel getBondDetail(BondModel model);

    /**
     * 债权修改
     * @param bond 债权实体
     */
    Bond bondUpdate(Bond bond);

    /**
     * 债权删除
     * 
     * @param id 主键ID
     */
    void deleteBond(long id);
    
    /**
     * 债权分页
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondModel> getBondList(BondModel model);
    
    /**
     * 所以债权转让信息
     * @param model 查询参数
     * @return 
     */
    PageDataList<BondModel> getAllBondList(BondModel model);    
    
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
     * 停止债权转让
     * @param bondId
     */
    void stopBond(long bondId);
    
    /**
     * 还款前一天停止债权转让(自动停止)
     */
    void autoStopBond();
    
    /**
     * 查询债权详情
     * @param model
     * @return
     */
    BondModel getBondModelByBondId(long bondId);
    
    /**
     * 查询债权详情
     * @param model
     * @return
     */
    BondModel getBondModelByBondTenderId(long tenderId);

}
