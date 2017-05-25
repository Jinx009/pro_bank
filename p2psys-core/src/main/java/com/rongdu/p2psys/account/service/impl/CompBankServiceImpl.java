package com.rongdu.p2psys.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.dao.CompBankDao;
import com.rongdu.p2psys.account.domain.CompBank;
import com.rongdu.p2psys.account.model.CompBankModel;
import com.rongdu.p2psys.account.service.CompBankService;

/**
 * 对公付款银行卡
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
@Service("compBankService")
public class CompBankServiceImpl implements CompBankService {
	@Resource
	private CompBankDao compBankDao;
	
	@Override
	public CompBank save(CompBank compBank) {
		
		return compBankDao.save(compBank);
	}

	@Override
	public PageDataList<CompBankModel> pageDataList(CompBankModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows());
		PageDataList<CompBank> dataList = compBankDao.findPageList(param);
		PageDataList<CompBankModel> dataList_ = new PageDataList<CompBankModel>();
		List<CompBankModel> list = new ArrayList<CompBankModel>();
		if(dataList.getList() != null && dataList.getList().size() > 0) {
			for (CompBank compBank : dataList.getList()) {
				CompBankModel bankModel = CompBankModel.instance(compBank);
				bankModel.setOpName(compBank.getOperator().getName());
				list.add(bankModel);
			}
		}
		dataList_.setPage(dataList.getPage());
		dataList_.setList(list);
		return dataList_;
	}

	@Override
	public CompBank update(CompBank compBank) {
		
		return compBankDao.update(compBank);
	}

	@Override
	public void delete(long id) {
		CompBank compBank = compBankDao.find(id);
		compBank.setIsDelete((byte) 1);
		compBankDao.update(compBank);
	}

	@Override
	public CompBank findById(long id) {
		
		return compBankDao.find(id);
	}

	@Override
	public List<CompBank> list() {
		
		return compBankDao.findByProperty("isDelete", 0);
	}

}
