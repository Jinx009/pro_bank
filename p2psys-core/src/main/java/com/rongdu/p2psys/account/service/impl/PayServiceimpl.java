package com.rongdu.p2psys.account.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.PayDao;
import com.rongdu.p2psys.account.domain.Pay;
import com.rongdu.p2psys.account.service.PayService;

@Service("payService")
public class PayServiceimpl implements PayService {

	@Resource
	private PayDao payDao;

	@Override
	public List<Pay> list(int enable) {
		return payDao.findByProperty("enable", enable);
	}

	@Override
	public List<Pay> directList(int enableDirect) {
		return payDao.findByProperty("enableDirect", enableDirect);
	}

	@Override
	public Pay findByNid(String nid) {
		return payDao.findObjByProperty("nid", nid);
	}

	@Override
	public Pay findById(long id) {
		return payDao.find(id);
	}

	@Override
	public Pay save(Pay pay) {
		return payDao.save(pay);
	}

	@Override
	public Pay update(Pay pay) {
		return payDao.update(pay);
	}

	@Override
	public void delete(Pay pay) {
		payDao.delete(pay.getId());
	}

	/**
	 * 查询支付接口列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@Override
	public PageDataList<Pay> list(int pageNumber, int pageSize, Pay model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (model != null) {
			if (!StringUtil.isBlank(model.getEnable()) && model.getEnable() != 0) {
				param.addParam("enable", Operators.EQ, model.getEnable());
			}
		}
		return payDao.findAllPageList(param);
	}

}
