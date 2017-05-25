package com.rongdu.p2psys.tpp.ips.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.tpp.domain.TppIpsConfig;

/**
 * 用户环迅参数设置DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
public interface TppIpsConfigDao extends BaseDao<TppIpsConfig> {

   /**
    * 用户环迅参数设置:修改自动还款签约
    * @param tppIpsConfig 用户环迅参数设置实体
    */
   Boolean editAutoRepaymentSigning(TppIpsConfig tppIpsConfig);
}
