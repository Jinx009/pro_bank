package com.rongdu.p2psys.tpp.chinapnr.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.chinapnr.dao.TppPnrPayDao;
import com.rongdu.p2psys.tpp.chinapnr.service.TppPnrPayService;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;

@Service(value="tppPnrPayService")
@Transactional
public class TppPnrPayServiceImpl implements TppPnrPayService {
	
	private Logger logger=Logger.getLogger(TppPnrPayServiceImpl.class);
	
	@Autowired
	private TppPnrPayDao tppPnrPayDao;
	
	@Override
	public void dealChinapnrBack(String ordNo, String respcode, String respdesc) {
		TppPnrPay tppPnrPay  = tppPnrPayDao.findChinapnrModelByOrd(ordNo);
		if(tppPnrPay == null) return;
		if (respcode.equals("000")) {  //判断是否处理成功
			if (tppPnrPay.getStatus()!=null ) {
				if (tppPnrPay.getStatus().equals("2")) {
					tppPnrPay.setStatus("1");
					tppPnrPay.setErrorMsg("SUCCESS");
				}else{//状态不未2 的不做处理
					logger.info("此订单已经处理成功，状态已经修改！ordNo"+ordNo);
				}
			}
		}
	}

    @Override
    public PageDataList<TppPnrPay> getTppChinapnrPay(Map<String, Object> map) {
        //查询
        QueryParam sp = QueryParam.getInstance();
        Long borrowId = (Long) map.get("borrowId");
        if(borrowId != null && borrowId > 0) {
            sp.addParam("borrowId", borrowId);
        }
        String ordId = (String) map.get("ordId");
        if(!StringUtil.isBlank(ordId)) {
            sp.addParam("ordId", ordId);
        }
        String status = (String) map.get("status");
        if(!StringUtil.isBlank(status)) {
            sp.addParam("status", status);
        }
        String operatetype = (String) map.get("operateType");
        if(!StringUtil.isBlank(operatetype)) {
            sp.addParam("operateType", operatetype);  
        }
        String cmdid = (String) map.get("cmdid");
        if (!StringUtil.isBlank(cmdid)) {
            sp.addParam("cmdid", cmdid);
        }
        String startTime = (String) map.get("startTime");
        if(StringUtil.isNotBlank(startTime)){
        	Date start = DateUtil.valueOf(startTime + " 00:00:00");
			sp.addParam("addtime", Operators.GTE, start);
        }
        String endTime = (String) map.get("endTime");
        if(StringUtil.isNotBlank(endTime)){
        	Date end = DateUtil.valueOf(endTime + " 23:59:59");
			sp.addParam("addtime", Operators.LTE, end);
        }
        String payUserName = (String) map.get("payUserName");
        if(StringUtil.isNotBlank(payUserName)){
        	 sp.addParam("payUserName", payUserName);
        }
        String userName = (String) map.get("userName");
        if(StringUtil.isNotBlank(userName)){
        	 sp.addParam("userName", userName);
        }
        sp.addOrder(OrderType.DESC, "id");
        Integer rows = (Integer) map.get("rows");
        if(rows != null && rows == 0){
            sp.addPage(rows);
        }else {
            Integer page = (Integer) map.get("page");
            sp.addPage(page, rows);
        }
        PageDataList<TppPnrPay> itemPage = tppPnrPayDao.findPageList(sp);
        return itemPage;
    }

    @Override
    public TppPnrPay getTppPayById(int id) {
        return tppPnrPayDao.find(id);
    }
}
