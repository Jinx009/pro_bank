package com.rongdu.p2psys.account.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.CompCash;
import com.rongdu.p2psys.account.model.CompCashModel;

/**
 * 对公付款提现
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
public interface CompCashService {
	/**
	 * 保存
	 * 
	 * @param compCash
	 * @return
	 */
	CompCash save(CompCash compCash);
	
	/**
	 * 分页查询
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<CompCashModel> pageDataList(CompCashModel model);
	
	/**
	 * 处理处理中订单
	 */
	void doCompCashWait();
	
	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	CompCash findById(long id);
	
	/**
	 * 修改
	 * @param compCash
	 * @return
	 */
	CompCash update(CompCash compCash);
}
