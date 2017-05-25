package com.rongdu.p2psys.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.CompCashDao;
import com.rongdu.p2psys.account.domain.CompCash;
import com.rongdu.p2psys.account.model.CompCashModel;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.CompCashService;

@Service("compCashService")
public class CompCashServiceImpl implements CompCashService {
	@Resource
	private CompCashDao compCashDao;
	
	@Override
	public CompCash save(CompCash compCash) {
		
		return compCashDao.save(compCash);
	}

	@Override
	public PageDataList<CompCashModel> pageDataList(CompCashModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows());
		if (model.getTppStatus() != 99) {
			param.addParam("tppStatus", model.getTppStatus());
		}
		if (model.getWebStatus() != 99) {
			param.addParam("webStatus", model.getWebStatus());
		}
		if (StringUtil.isNotBlank(model.getStartTime())) {
            Date start = DateUtil.valueOf(model.getStartTime());
            param.addParam("addTime", Operators.GTE, start);
        }
        if (StringUtil.isNotBlank(model.getEndTime())) {
            Date end = DateUtil.valueOf(model.getEndTime());
            param.addParam("addTime", Operators.LTE, end);
        }
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<CompCash> dataList = compCashDao.findPageList(param);
		PageDataList<CompCashModel> dataList_ = new PageDataList<CompCashModel>();
		List<CompCashModel> list = new ArrayList<CompCashModel>();
		if(dataList.getList() != null && dataList.getList().size() > 0) {
			for (CompCash compCash : dataList.getList()) {
				CompCashModel cashModel = CompCashModel.instance(compCash);
				cashModel.setAddOpName(compCash.getAddOperator().getName());
				if(compCash.getVerifyOperator() != null) {
					cashModel.setVerifyOpName(compCash.getVerifyOperator().getName());
				}
				list.add(cashModel);
			}
		}
		dataList_.setPage(dataList.getPage());
		dataList_.setList(list);
		return dataList_;
	}

	@Override
	public void doCompCashWait() {
		QueryParam param = QueryParam.getInstance()
				.addParam("tppStatus", CompCashModel.TPP_PROCESSING)
				.addParam("webStatus", CompCashModel.WEB_SUCCESS);
		List<CompCash> list = compCashDao.findByCriteria(param);
		if (list != null && list.size() > 0) {
			for (CompCash compCash : list) {
				UnionPayRet ret = SignHelper.queryOrder(compCash.getOrderNo());
				if (ret.getOrderStatus() == 2) {
					compCash.setTppStatus(CompCashModel.TPP_SUCCESS);
					compCashDao.update(compCash);
				} else if (ret.getOrderStatus() == 3) {
					compCash.setTppStatus(CompCashModel.TPP_FAIL);
					compCashDao.update(compCash);
				}
			}
		}
	}

	@Override
	public CompCash findById(long id) {
		
		return compCashDao.find(id);
	}

	@Override
	public CompCash update(CompCash compCash) {
		
		return compCashDao.update(compCash);
	}

}
