package com.rongdu.p2psys.bond.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.model.BondTenderModel;

/**
 * 债权投资DAO接口
 * @since 2014-12-11
 */
public interface BondTenderDao extends BaseDao<BondTender> {

    /**
     * 查询债权投资
     * 
     * @param id 主键ID
     * @return 债权投资实体
     */
    BondTender getBondTenderById(long id);
    
    /**
     * 债权投资分页
     * @param param 查询参数
     * @return 分页
     */
    PageDataList<BondTender> getTenderModelPage(BondTenderModel model);
    
    /**
     * 已转入债权
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondTender> getBoughtBondList(BondModel model);
    
    /**
     * 最新债权转让成交记录
     * @return
     */
    List<BondTender> getLatestTenerList();
    
    /**
     * 债权应收收益
     * @param bondId
     * @return
     */
    double getPayInterestByBondId(long bondId);
}
