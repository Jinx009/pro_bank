package com.rongdu.p2psys.nb.payment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.dao.ISupportBankDao;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.SupportBankModel;
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;
import com.rongdu.p2psys.user.dao.UserDao;

/***
 * 支付通道业务处理
 * @author ChenGangwei
 * 2015-06-23
 */
@Service("theSupportBankService")
public class SupportBankServiceImpl implements ISupportBankService {

	@Resource
	private UserDao userDao;
	
	@Resource                        
	private ISupportBankDao theSupportBankDao;
	
	@Override
	public PageDataList<SupportBankModel> findSupportBankByItem(int pageNumber,
			int pageSize, String searchName) {
		return theSupportBankDao.findSupportBankByItem(pageNumber,pageSize, searchName);
	}

	@Override
	public SupportBankModel loadSupportBankById(long id) {
		NbSupportBank supportBank = theSupportBankDao.loadSupportBankById(id);
		SupportBankModel model = SupportBankModel.instance(supportBank);
		return model;
	}

	@Override
	public void deleteSupportBankById(long id) {
		theSupportBankDao.deleteSupportBankById(id);
	}

	@Override
	public void updateSupportBank(SupportBankModel model) {
		NbSupportBank supportBank = SupportBankModel.instance(model);
		theSupportBankDao.updateSupportBank(supportBank);		
	}

	@Override
	public void saveSupportBank(SupportBankModel model) {
		NbSupportBank supportBank = SupportBankModel.instance(model);
		theSupportBankDao.save(supportBank);
	}

	@Override
	public NbSupportBank loadSupportBankByCode(String bankCode) {
		return theSupportBankDao.loadSupportBankByCode(bankCode);
	}

	@Override
	public List<NbSupportBank> loadSupportBankList() {
		return theSupportBankDao.findAll();
	}

	@Override
	public NbSupportBank findByName(String name) {
		return theSupportBankDao.findObjByProperty("bankName", name);
	}

}
