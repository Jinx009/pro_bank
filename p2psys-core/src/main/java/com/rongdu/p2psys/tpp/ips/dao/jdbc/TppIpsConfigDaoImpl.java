package com.rongdu.p2psys.tpp.ips.dao.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.tpp.domain.TppIpsConfig;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsConfigDao;

/**
 * 用户环迅参数设置DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
@Repository
public class TppIpsConfigDaoImpl extends BaseDaoImpl<TppIpsConfig> implements TppIpsConfigDao {

    private static Logger logger = Logger.getLogger(TppIpsConfigDaoImpl.class);
    
    @Override
    public Boolean editAutoRepaymentSigning(TppIpsConfig item) {
        StringBuffer sql = new StringBuffer("UPDATE TppIpsConfig SET auto_repay_status = :autoRepayStatus,");
        sql.append(" auto_repay_no = :autoRepayNo,");
        sql.append(" auto_repay_end_time = :autoRepayEndTime");
        sql.append(" where auto_repay_status in (1,-2) and auto_repay_num = :autoRepayNum");
        int result = this.updateByJpql(sql.toString(), 
                new String[]{"autoRepayStatus", "autoRepayNo", "autoRepayEndTime", "autoRepayNum"}, 
                new Object[] {item.getAutoRepayStatus(), item.getAutoRepayNo(),item.getAutoRepayEndTime(),item.getAutoRepayNum()});
        if (result < 1) {
            return false;
        }else{
            // 更新缓存
            em.refresh(getTppIpsConfigByNum(item.getAutoRepayNum()));
            logger.info("用户自动还款签约处理成功！流水号："+item.getAutoRepayNum());
        }
        return true;
    }

    public TppIpsConfig getTppIpsConfigByNum(String merBillNo) {
        return this.findObjByProperty("autoRepayNum", merBillNo);
    }
}
