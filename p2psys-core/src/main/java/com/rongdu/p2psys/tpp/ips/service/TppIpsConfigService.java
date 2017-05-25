package com.rongdu.p2psys.tpp.ips.service;

import com.rongdu.p2psys.tpp.domain.TppIpsConfig;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRepaymentSigning;

/**
 * 用户环迅参数设置Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
public interface TppIpsConfigService {

    /**
     * 添加用户环迅参数设置
     * 
     * @param tppIpsConfig 用户环迅参数设置实体
     */
    TppIpsConfig addTppIpsConfig(TppIpsConfig tppIpsConfig);

    /**
     * 查询用户环迅参数设置
     * 
     * @param id 主键ID
     * @return 用户环迅参数设置
     */
    TppIpsConfig getTppIpsConfigById(long userId);

    /**
     * 用户环迅参数设置修改
     * @param tppIpsConfig 用户环迅参数设置实体
     */
    TppIpsConfig tppIpsConfigUpdate(TppIpsConfig tppIpsConfig);
    
    /**
     * 用户环迅参数设置:自动还款签约
     * @param tppIpsConfig 用户环迅参数设置实体
     */
    IpsAutoRepaymentSigning doAutoRepaymentSigning(TppIpsConfig tppIpsConfig);
    
    /**
     * 用户环迅参数设置:修改自动还款签约
     * @param tppIpsConfig 用户环迅参数设置实体
     */
    Boolean editAutoRepaymentSigning(TppIpsConfig tppIpsConfig);
    
    /**
     * 查询用户环迅参数设置
     * 
     * @param merBillNo 商户签约订单号
     * @return 用户环迅参数设置
     */
    TppIpsConfig getTppIpsConfigByNum(String merBillNo);
    
}
