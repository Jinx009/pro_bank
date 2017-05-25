package com.rongdu.p2psys.account.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.PayOfflinebankDao;
import com.rongdu.p2psys.account.domain.PayOfflinebank;
import com.rongdu.p2psys.account.service.PayOfflinebankService;
import com.rongdu.p2psys.core.Global;

@Service
public class PayOfflinebankServiceImpl implements PayOfflinebankService {

	@Resource
	private PayOfflinebankDao payOfflinebankDao;

	@Override
	public List<PayOfflinebank> list() {
		return payOfflinebankDao.list(2);
	}

	@Override
	public PageDataList<PayOfflinebank> payOfflinebankList(PayOfflinebank payOfflinebank, int pageNumber, int pageSize, String searchName) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (payOfflinebank != null && !StringUtil.isBlank(searchName)) {
			SearchFilter orFilter1 = new SearchFilter("bankNo", Operators.LIKE, searchName);
    		SearchFilter orFilter2 = new SearchFilter("owner", Operators.LIKE, searchName);
    		param.addOrFilter(orFilter1,orFilter2);
	    }
		param.addOrder(OrderType.DESC, "id");
		PageDataList<PayOfflinebank> pList = payOfflinebankDao.findPageList(param);
		return pList;
	}

	@Override
	public void offlinebankAdd(PayOfflinebank payOfflinebank) {
		payOfflinebank.setAddTime(new Date());
		payOfflinebank.setAddIp(Global.getIP());
		payOfflinebankDao.save(payOfflinebank);
	}

	@Override
	public PayOfflinebank find(long id) {
		return payOfflinebankDao.find(id);
	}

	@Override
	public void offlinebankEdit(PayOfflinebank payOfflinebank) {
		payOfflinebank.setAddTime(new Date());
		payOfflinebank.setAddIp(Global.getIP());
		payOfflinebankDao.update(payOfflinebank);
	}

}
