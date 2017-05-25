package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.CompBank;
import com.rongdu.p2psys.account.model.CompBankModel;

/**
 * 对公付款银行卡
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
public interface CompBankService {
	/**
	 * 保存
	 * 
	 * @param compBank
	 * @return CompBank
	 */
	CompBank save(CompBank compBank);

	/**
	 * 列表
	 * 
	 * @param model
	 * @return CompBankModel
	 */
	PageDataList<CompBankModel> pageDataList(CompBankModel model);

	/**
	 * 编辑
	 * 
	 * @param compBank
	 * @return
	 */
	CompBank update(CompBank compBank);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	void delete(long id);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	CompBank findById(long id);
	
	/**
	 * 列表
	 * @return
	 */
	List<CompBank> list();
}
