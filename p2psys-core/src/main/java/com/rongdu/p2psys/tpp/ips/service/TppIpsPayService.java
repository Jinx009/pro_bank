package com.rongdu.p2psys.tpp.ips.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.model.TppIpsPayModel;

/**
 * 环迅资金操作日志Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
public interface TppIpsPayService {

    /**
     * 添加环迅资金操作日志
     * 
     * @param pIpsPay 环迅资金操作日志实体
     */
    TppIpsPay addTppIpsPay(TppIpsPay pIpsPay);

    /**
     * 查询环迅资金操作日志
     * 
     * @param id 主键ID
     * @return 环迅资金操作日志
     */
    TppIpsPay getTppIpsPayById(long id);

    /**
     * 环迅资金操作日志修改
     * @param pIpsPay 环迅资金操作日志实体
     */
    TppIpsPay tppIpsPayUpdate(TppIpsPay pIpsPay);
    
    /**
     * 分页
     * 
     * @param model
     * @return
     */
    PageDataList<TppIpsPayModel> list(TppIpsPayModel model);
    
    /**
     * 更新环迅资金操作日志
     * 
     * @param merBillNo 订单号
     * @return 环迅资金操作日志
     */
    void updateTppIpsPayByMerBillNo(TppIpsPay pay);    
}
