package com.rongdu.p2psys.tpp.ips.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.domain.TppIpsConfig;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsConfigDao;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRepaymentSigning;
import com.rongdu.p2psys.tpp.ips.service.TppIpsConfigService;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

/**
 * 用户环迅参数设置Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
@Service(value = "tppIpsConfigService")
@Transactional
public class TppIpsConfigServiceImpl implements TppIpsConfigService {
    
    @Autowired
    private TppIpsConfigDao tppIpsConfigDao;
    @Autowired
    private UserDao userDao;

    @Override
    public TppIpsConfig addTppIpsConfig(TppIpsConfig tppIpsConfig) {
       return tppIpsConfigDao.save(tppIpsConfig);

    }

    @Override
    public TppIpsConfig getTppIpsConfigById(long userId) {
        return tppIpsConfigDao.findObjByProperty("userId", userId);
    }

    @Override
    public TppIpsConfig tppIpsConfigUpdate(TppIpsConfig tppIpsConfig) {
        return  tppIpsConfigDao.update(tppIpsConfig);

    }

    @Override
    public IpsAutoRepaymentSigning doAutoRepaymentSigning(TppIpsConfig item) {
        TppIpsConfig config = this.getTppIpsConfigById(item.getUserId());
        
        TPPWay way = TPPFactory.getTPPWay(new User(item.getUserId()));
        IpsAutoRepaymentSigning repayment = new IpsAutoRepaymentSigning();
        try {
            repayment = (IpsAutoRepaymentSigning) way.doAutoRepaymentSigning();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(repayment.getSigningDate());
            config.setAutoRepayTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setAutoRepayNum(repayment.getMerBillNo());
        config.setAutoRepayStatus(TppIpsConfig.AUTO_REPAY_WAIT);
        this.tppIpsConfigUpdate(config);
        return repayment;
    }

    @Override
    public TppIpsConfig getTppIpsConfigByNum(String merBillNo) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("autoRepayNum", merBillNo);
        param.addParam("autoRepayStatus", TppIpsConfig.AUTO_REPAY_WAIT);
        return tppIpsConfigDao.findByCriteriaForUnique(param);
    }

    @Override
    public Boolean editAutoRepaymentSigning(TppIpsConfig tppIpsConfig) {
        // TODO Auto-generated method stub
        return tppIpsConfigDao.editAutoRepaymentSigning(tppIpsConfig);
    }
}
