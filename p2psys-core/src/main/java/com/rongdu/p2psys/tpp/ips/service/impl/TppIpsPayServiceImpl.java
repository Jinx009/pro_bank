package com.rongdu.p2psys.tpp.ips.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsPayDao;
import com.rongdu.p2psys.tpp.ips.model.TppIpsPayModel;
import com.rongdu.p2psys.tpp.ips.service.TppIpsPayService;

/**
 * 环迅资金操作日志Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
@Service(value = "tppIpsPayService")
@Transactional
public class TppIpsPayServiceImpl implements TppIpsPayService {
    
    @Autowired
    private TppIpsPayDao tppIpsPayDao;

    @Override
    public TppIpsPay addTppIpsPay(TppIpsPay pIpsPay) {
       return tppIpsPayDao.save(pIpsPay);

    }

    @Override
    public TppIpsPay getTppIpsPayById(long id) {
        return tppIpsPayDao.find(id);
    }

    @Override
    public TppIpsPay tppIpsPayUpdate(TppIpsPay pIpsPay) {
        return  tppIpsPayDao.update(pIpsPay);

    }
    @Override
    public PageDataList<TppIpsPayModel> list(TppIpsPayModel model) {
        QueryParam param = QueryParam.getInstance();
        if (model != null) {
            if (StringUtil.isNotBlank(model.getSearchStartTime())) {
                Date start = DateUtil.valueOf(model.getSearchStartTime() + " 00:00:00");
                param.addParam("addTime", Operators.GT, start);
            }
            if (StringUtil.isNotBlank(model.getSearchEndTime())) {
                Date end = DateUtil.valueOf(model.getSearchEndTime() + " 23:59:59");
                param.addParam("addTime", Operators.LTE, end);
            }
            param.addPage(model.getPage(), model.getSize());
        }
        PageDataList<TppIpsPay> pageDataList = tppIpsPayDao.findPageList(param);
        PageDataList<TppIpsPayModel> pageDataList_ = new PageDataList<TppIpsPayModel>();
        List<TppIpsPayModel> list = new ArrayList<TppIpsPayModel>();
        pageDataList_.setPage(pageDataList.getPage());
        if (pageDataList.getList().size() > 0) {
            for (int i = 0; i < pageDataList.getList().size(); i++) {
                TppIpsPay tppIpsPay = (TppIpsPay) pageDataList.getList().get(i);
                TppIpsPayModel tipm = TppIpsPayModel.instance(tppIpsPay);
                list.add(tipm);
            }
        }
        pageDataList_.setList(list);
        return pageDataList_;
    }
    
    @Override
    public void updateTppIpsPayByMerBillNo(TppIpsPay pay) {
    	TppIpsPay tppIpsPay =  tppIpsPayDao.getTppIpsPayByMerBillNo(pay.getMerBillNo());
    	if(tppIpsPay != null && tppIpsPay.getStatus() != TppIpsPay.STATUS_SUCCESS){
    		tppIpsPay.setIpsBillNo(pay.getIpsBillNo());
    		tppIpsPay.setIpsTime(pay.getIpsTime());
    		tppIpsPay.setIpsFee(pay.getIpsFee());
    		tppIpsPay.setStatus(pay.getStatus());
    		tppIpsPay.setEndTime(new Date());
    		tppIpsPayDao.save(tppIpsPay);
    	}
    }
}
