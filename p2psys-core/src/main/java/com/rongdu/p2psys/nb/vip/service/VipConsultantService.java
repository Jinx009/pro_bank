package com.rongdu.p2psys.nb.vip.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.model.ApplyConsultantsModel;
import com.rongdu.p2psys.nb.vip.domain.VipConsultant;
import com.rongdu.p2psys.nb.vip.model.VipConsultantModel;

public interface VipConsultantService
{
	public PageDataList<VipConsultantModel> getPage(VipConsultant model,int pageNumber, int pageSize);
	/**
	 * 添加
	 * @param VipConsultant
	 */
	public VipConsultant saveObject(VipConsultant vipConsultant);
	/**
	 * 根据财富id删除财富管家信息
	 * @param id
	 */
	public void delVipConsultant(Integer id);
	/**
	 * 修改财富管家信息
	 */
	public void update(VipConsultant vipConsultant);
	/**
	 * 通过id查找财富管家对应关系
	 * @param id
	 * @return
	 */
	public VipConsultant findById(Integer id);
	/**
	 * 查看所有申请记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param model
	 * @return
	 */
	PageDataList<VipConsultantModel> getAllList(int pageNum,int pageSize,VipConsultantModel model);
	
}
