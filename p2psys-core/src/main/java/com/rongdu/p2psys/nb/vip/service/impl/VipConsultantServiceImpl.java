package com.rongdu.p2psys.nb.vip.service.impl;

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
import com.rongdu.p2psys.nb.vip.dao.VipConsultantDao;
import com.rongdu.p2psys.nb.vip.domain.VipConsultant;
import com.rongdu.p2psys.nb.vip.model.VipConsultantModel;
import com.rongdu.p2psys.nb.vip.service.VipConsultantService;
@Service("vipConsultantService")
public class VipConsultantServiceImpl implements VipConsultantService
{

	@Resource
	private VipConsultantDao vipConsultantDao;

	@Override
	public VipConsultant saveObject(VipConsultant vipConsultant) {
		// TODO Auto-generated method stub
		return vipConsultantDao.save(vipConsultant);
	}

	@Override
	public void delVipConsultant(Integer id) {
		// TODO Auto-generated method stub
		vipConsultantDao.delete(id);
	}

	@Override
	public void update(VipConsultant vipConsultant) {
		// TODO Auto-generated method stub
		vipConsultantDao.update(vipConsultant);
	}

	@Override
	public PageDataList<VipConsultantModel> getPage(VipConsultant model,
			int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VipConsultant findById(Integer id) {
		// TODO Auto-generated method stub
		return vipConsultantDao.find(id);
	}
	

	@Override
	public PageDataList<VipConsultantModel> getAllList(int pageNum,
			int pageSize, VipConsultantModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNum, pageSize).addOrder(OrderType.DESC, "addTime");
		
		if (StringUtil.isNotBlank(model.getRealName())) {
			param.addParam("realName", model.getRealName());
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
		PageDataList<VipConsultant> dataList = vipConsultantDao.findAllPageList(param);
		PageDataList<VipConsultantModel> dataList_ = new PageDataList<VipConsultantModel>();
		List<VipConsultantModel> list = new ArrayList<VipConsultantModel>();
		dataList_.setPage(dataList.getPage());
		if (dataList.getList() != null && dataList.getList().size() > 0) {
			for (VipConsultant ac : dataList.getList()) {
				VipConsultantModel am = VipConsultantModel.instance(ac);
				if(ac.getConsultant()!=null)
				{
					am.setExpertName(ac.getConsultant().getName());
				}
				list.add(am);
			}
		}
		dataList_.setList(list);
		return dataList_;
	}

}
