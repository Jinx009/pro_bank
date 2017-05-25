package com.rongdu.p2psys.ppfund.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.ppfund.model.PpfundOutModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品转出
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
public interface PpfundOutService {
	/**
	 * 转出记录
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<PpfundOutModel> pageDataList(PpfundOutModel model);

	/**
	 * 转出记录
	 * 
	 * @param ppfundId
	 * @param page
	 * @param size
	 * @return
	 */
	PageDataList<PpfundOutModel> pageDataList(long ppfundId, int page, int size);
	
	/**
	 * 查询当天已赎回总金额
	 * @param model
	 * @return
	 */
	double getPresentRedeem(User user);
	
	
}
