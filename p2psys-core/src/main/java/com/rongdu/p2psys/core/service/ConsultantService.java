package com.rongdu.p2psys.core.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Consultant;

/**
 * 顾问专家
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
public interface ConsultantService {
	/**
	 * 保存
	 * 
	 * @param consultant
	 * @return
	 */
	Consultant save(Consultant consultant);

	/**
	 * 获取所有专家顾问
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	PageDataList<Consultant> getAllList(int pageNumber, int pageSize,
			Consultant consultant);

	/**
	 * 修改
	 * 
	 * @param consultant
	 * @return
	 */
	Consultant update(Consultant consultant);
	
	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	Consultant getByid(long id);
}
