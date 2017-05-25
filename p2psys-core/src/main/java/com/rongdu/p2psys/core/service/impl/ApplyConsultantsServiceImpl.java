package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.ApplyConsultantsDao;
import com.rongdu.p2psys.core.domain.ApplyConsultants;
import com.rongdu.p2psys.core.model.ApplyConsultantsModel;
import com.rongdu.p2psys.core.service.ApplyConsultantsService;

/**
 * 私人顾问
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
@Service("applyConsultantsService")
public class ApplyConsultantsServiceImpl implements
		ApplyConsultantsService {
	@Resource
	private ApplyConsultantsDao applyConsultantsDao;
	
	@Override
	public ApplyConsultants save(ApplyConsultants consultants) {
		
		return applyConsultantsDao.save(consultants);
	}

	@Override
	public PageDataList<ApplyConsultantsModel> getAllList(int pageNum,
			int pageSize, ApplyConsultantsModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNum, pageSize).addOrder(OrderType.DESC, "addTime");
		if (StringUtil.isNotBlank(model.getSearchName())) {
			param.addParam("consultant.name", Operators.LIKE, model.getSearchName());
		} else {
			if (StringUtil.isNotBlank(model.getExpertName())) {
				param.addParam("consultant.name",model.getExpertName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", model.getRealName());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}
			if (model.getStatus() != 99) {
				param.addParam("status", model.getStatus());
			}
		}
		PageDataList<ApplyConsultants> dataList = applyConsultantsDao.findAllPageList(param);
		PageDataList<ApplyConsultantsModel> dataList_ = new PageDataList<ApplyConsultantsModel>();
		List<ApplyConsultantsModel> list = new ArrayList<ApplyConsultantsModel>();
		dataList_.setPage(dataList.getPage());
		if (dataList.getList() != null && dataList.getList().size() > 0) {
			for (ApplyConsultants ac : dataList.getList()) {
				ApplyConsultantsModel am = ApplyConsultantsModel.instance(ac);
				if(ac.getUser().getRealName() != null) {
					am.setRealName(ac.getUser().getRealName());
				}
				am.setMobilePhone(ac.getUser().getMobilePhone());
				am.setExpertName(ac.getConsultant().getName());
				list.add(am);
			}
		}
		dataList_.setList(list);
		return dataList_;
	}

	@Override
	public ApplyConsultants findById(long id) {
		
		return applyConsultantsDao.find(id);
	}

	@Override
	public ApplyConsultants update(ApplyConsultants consultants) {
		
		return applyConsultantsDao.update(consultants);
	}

}
