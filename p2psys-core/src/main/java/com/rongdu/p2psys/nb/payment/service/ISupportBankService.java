package com.rongdu.p2psys.nb.payment.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.SupportBankModel;

public interface ISupportBankService {
	PageDataList<SupportBankModel> findSupportBankByItem(int pageNumber,
			int pageSize, String searchName);
	
	SupportBankModel loadSupportBankById(long id);
	
	void deleteSupportBankById(long id);
	
	void updateSupportBank(SupportBankModel model);
	
	void saveSupportBank(SupportBankModel model);
	
	NbSupportBank loadSupportBankByCode(String bankCode);
	
	List<NbSupportBank> loadSupportBankList();
	
	/**
	 * 查询银行卡
	 * 
	 * @param name
	 * @return
	 */
	NbSupportBank findByName(String name);
}
