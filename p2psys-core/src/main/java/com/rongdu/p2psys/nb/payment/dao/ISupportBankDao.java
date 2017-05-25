package com.rongdu.p2psys.nb.payment.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.SupportBankModel;

public interface ISupportBankDao extends BaseDao<NbSupportBank> {
	PageDataList<SupportBankModel> findSupportBankByItem(int pageNumber,
			int pageSize, String searchName);
	
	NbSupportBank loadSupportBankById(long id);
	
	void deleteSupportBankById(long id);
	
	void updateSupportBank(NbSupportBank sb);
	
	NbSupportBank loadSupportBankByCode(String bankCode);
}
