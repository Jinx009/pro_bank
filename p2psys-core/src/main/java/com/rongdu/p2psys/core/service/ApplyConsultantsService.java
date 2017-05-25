package com.rongdu.p2psys.core.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.ApplyConsultants;
import com.rongdu.p2psys.core.model.ApplyConsultantsModel;

/**
 * 私人顾问
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
public interface ApplyConsultantsService {
	/**
	 * 添加
	 * 
	 * @param consultants
	 * @return
	 */
	public ApplyConsultants save(ApplyConsultants consultants);
	
	/**
	 * 查看所有申请记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param model
	 * @return
	 */
	PageDataList<ApplyConsultantsModel> getAllList(int pageNum,int pageSize,ApplyConsultantsModel model);

	/**
	 * 获取
	 * @param id
	 * @return
	 */
	ApplyConsultants findById(long id);
	
	/**
	 * 修改
	 * @param consultants
	 * @return
	 */
	ApplyConsultants update(ApplyConsultants consultants);
}
