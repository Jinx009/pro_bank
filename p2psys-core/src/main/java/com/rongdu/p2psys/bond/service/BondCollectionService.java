package com.rongdu.p2psys.bond.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.bond.domain.BondCollection;
import com.rongdu.p2psys.bond.model.BondCollectionModel;

/**
 * 债权待收Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public interface BondCollectionService {

    /**
     * 添加债权待收
     * 
     * @param bondCollection 债权待收实体
     */
    BondCollection addBondCollection(BondCollection bondCollection);

    /**
     * 查询债权待收
     * 
     * @param id 主键ID
     * @return 债权待收
     */
    BondCollection getBondCollectionById(long id);

    /**
     * 债权待收修改
     * @param bondCollection 债权待收实体
     */
    BondCollection bondCollectionUpdate(BondCollection bondCollection);

    /**
     * 债权待收删除
     * 
     * @param id 主键ID
     */
    void deleteBondCollection(long id);
    
    /**
     * 债权待收分页
     * @param param 查询参数
     * @return 分页
     */
    PageDataList<BondCollection> getCollectionPage(QueryParam param);
    
    /**
     * 债权待收分页
     * @param model 查询参数
     * @return 分页
     */
    PageDataList<BondCollectionModel> getCollectionModelPage(BondCollectionModel model);
    
    /**
     * 查询当前用户债权待收本金和利息
     * @param userId
     * @return
     */
    public Object[] getSumBondCollection(long userId);
	
}
