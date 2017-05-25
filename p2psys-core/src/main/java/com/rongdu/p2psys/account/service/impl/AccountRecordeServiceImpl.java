package com.rongdu.p2psys.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountRecordeDao;
import com.rongdu.p2psys.account.domain.AccountRecorde;
import com.rongdu.p2psys.account.model.AccountRecordeModel;
import com.rongdu.p2psys.account.service.AccountRecordeService;

/**
 * 资金汇总记录
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月4日
 */
@Service("accountRecordeService")
public class AccountRecordeServiceImpl implements AccountRecordeService {
	@Resource
	private AccountDao accountDao;
	@Resource
	private AccountRecordeDao accountRecordeDao;
	
	@Override
	public void doStatistics() {
		Object[] object = accountDao.financialStatistics();
		double total = NumberUtil.getDouble(((object[0]==null?0:object[0]).toString()));
		double useMoney = NumberUtil.getDouble(((object[1]==null?0:object[1]).toString()));
		double noUseMoney = NumberUtil.getDouble(((object[2]==null?0:object[2]).toString()));
		double collection = NumberUtil.getDouble(((object[3]==null?0:object[3]).toString()));
		AccountRecorde recorde = new AccountRecorde(total, useMoney, noUseMoney, collection);
		accountRecordeDao.save(recorde);
	}

	@Override
	public PageDataList<AccountRecorde> pageDataList(AccountRecordeModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows());
		if (StringUtil.isNotBlank(model.getStartTime())) {
			param.addParam("addTime", Operators.GTE, model.getStartTime());
		}
		if (StringUtil.isNotBlank(model.getEndTime())) {
			param.addParam("addTime", Operators.LTE, model.getEndTime());
		}
		param.addOrder(OrderType.DESC, "addTime");
		return accountRecordeDao.findPageList(param);
	}

}
