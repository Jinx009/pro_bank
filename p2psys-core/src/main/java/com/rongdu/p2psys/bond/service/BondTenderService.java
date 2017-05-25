package com.rongdu.p2psys.bond.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.model.BondTenderModel;

/**
 * 债权投资Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public interface BondTenderService {

    /**
     * 添加债权投资
     * 
     * @param bondModel 债权实体
     */
    BondTender addBondTender(BondModel bondModel);

    /**
     * 查询债权投资
     * 
     * @param id 主键ID
     * @return 债权投资
     */
    BondTender getBondTenderById(long id);

    /**
     * 债权投资修改
     * @param bondTender 债权投资实体
     */
    BondTender bondTenderUpdate(BondTender bondTender);

    /**
     * 债权投资删除
     * 
     * @param id 主键ID
     */
    void deleteBondTender(long id);
    
    /**
     * 债权投资分页
     * @param param 查询参数
     * @return 分页
     */
    PageDataList<BondTender> getTenderPage(QueryParam param);
    
    /**
     * 债权投资分页
     * @param param 查询参数
     * @return 分页
     */
    PageDataList<BondTenderModel> getTenderModelPage(BondTenderModel model);
    
    /**
     * 已转入债权
     * @param param 查询参数
     * @return 
     */
    PageDataList<BondTenderModel> getBoughtBondList(BondModel model);
    
    /**
     * 最新债权转让成交记录
     * @return
     */
    List<BondTenderModel> getLatestTenerList();
	
}
