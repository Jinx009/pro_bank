package com.rongdu.p2psys.nb.borrow.dao;


import java.text.ParseException;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.InvestRecordModel;
import com.rongdu.p2psys.nb.borrow.model.InvestDetailModel;

public interface BorrowTenderDao extends BaseDao<BorrowTender>
{
	public void update(double account, double scales, int status, long id);
	
	public void saveList(List<BorrowTender> list);
	
	PageDataList<InvestRecordModel> multipleIdentitiesList(InvestRecordModel model) throws ParseException ;
	
	PageDataList<InvestDetailModel> getInvestRecordByItem(InvestDetailModel model);

	PageDataList<InvestRecordModel> lockCashRecord(InvestRecordModel model);
	

}
