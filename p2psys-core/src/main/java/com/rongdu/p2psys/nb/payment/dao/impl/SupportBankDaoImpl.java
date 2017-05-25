package com.rongdu.p2psys.nb.payment.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.payment.dao.ISupportBankDao;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.SupportBankModel;

@Repository("theSupportBankDao")
public class SupportBankDaoImpl extends BaseDaoImpl<NbSupportBank>  implements ISupportBankDao {

	@Override
	public PageDataList<SupportBankModel> findSupportBankByItem(int pageNumber,
			int pageSize, String searchName) {
		PageDataList<SupportBankModel> pageDataList = new PageDataList<SupportBankModel>();
		StringBuffer sql =new StringBuffer("select sb.* from nb_support_bank as sb where 1=1 ");
		if(!StringUtil.isBlank(searchName)){
			sql.append("AND sb.bank_name like '%"+searchName+"%' ");
		} 
		sql.append(" order by sb.id desc");
		Query query = em.createNativeQuery(sql.toString(),NbSupportBank.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<NbSupportBank> list = query.getResultList();
        List<SupportBankModel> list_ =new ArrayList<SupportBankModel>();
        for (NbSupportBank supportBank : list) {
			SupportBankModel model = SupportBankModel.instance(supportBank);
			list_.add(model);
		}
        pageDataList.setList(list_);
        pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public NbSupportBank loadSupportBankById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<NbSupportBank> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (NbSupportBank) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteSupportBankById(long id) {
		NbSupportBank supportBank = super.find(id);
		if(null != supportBank){
			super.delete(id);
		}
	}

	@Override
	public void updateSupportBank(NbSupportBank supportBank) {
		em.merge(supportBank);
	}

	@Override
	public NbSupportBank loadSupportBankByCode(String bankCode) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("bankCode", bankCode);
		NbSupportBank supportBank = super.findByCriteriaForUnique(param);
		return supportBank;
	}

}
