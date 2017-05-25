package com.rongdu.p2psys.nb.account.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.nb.account.dao.AccountBankDao;
import com.rongdu.p2psys.nb.account.service.AccountBankService;

@Service("theAccountBankService")
public class AccountBankServiceImpl implements AccountBankService
{

	@Resource
	private AccountBankDao theAccountBankDao;
	
	public AccountBank saveAccountBank(AccountBank accountBank)
	{
		return theAccountBankDao.save(accountBank);
	}

	public List<AccountBank> getByUserId(long user_id)
	{
		return theAccountBankDao.getByUserId(user_id);
	}
	
	@Override
	public List<AccountBank> list(long userId) {
		return this.list_(userId);
	}
	
	@Override
	public AccountBank findById(long id) {
		return theAccountBankDao.findById(id);
	}
	@Override
	public int count(long userId) {
		List<AccountBank> list = this.list(userId);
		if(null != list)
			return list.size();
		else
			return 0;
	}

	
	public List<AccountBank> list_(long userId) {
		List<AccountBank> ablist = theAccountBankDao.list(userId);
		if(null != ablist){
			for (int i = 0; i < ablist.size(); i++){  //外循环是循环的次数
	            for (int j = ablist.size() - 1 ; j > i; j--){  //内循环是 外循环一次比较的次数
	                if ((ablist.get(i).getBankNo()).equals(ablist.get(j).getBankNo())){
	                	ablist.remove(j);
	                }
	            }
	        }
		}
		return ablist;
	}
	

	@Override
	public List<AccountBank> list(long userId,String channelKey) {
		List<AccountBank> ablist = theAccountBankDao.list(userId,channelKey);
		for (int i = 0; i < ablist.size(); i++){  //外循环是循环的次数
            for (int j = ablist.size() - 1 ; j > i; j--){  //内循环是 外循环一次比较的次数
                if ((ablist.get(i).getBankNo()).equals(ablist.get(j).getBankNo())){
                	ablist.remove(j);
                }
            }
        }
		return ablist;
	}
	
	@Override
    public AccountBank update(AccountBank bank) {
	    return theAccountBankDao.update(bank);
	}

	@Override
	public String disable(long userId, long id) {
		/*if (BaseTPPWay.isOpenApi()) {
			UserCache uc = userCacheDao.findByUserId(userId);
			AccountBank ab = accountBankDao.find(id);
			TPPWay dw = TPPFactory.getTPPWay();
			Map<String, Object> map = dw.bankBindRemove(ab, uc.getUser());
			Boolean success = (Boolean) map.get("result");
			String message = map.get("message").toString();
			if (!success) {
				//throw new AccountException("解绑银行卡失败，失败原因:"+message, 1);
				return message;
			}else{
				accountBankDao.disable(userId, id);
				return "success";
			}
		}*/
		AccountBank ab = theAccountBankDao.find(id);
		UnionPayRet ret = SignHelper.unbind(ab.getBindId());
		if(!"0000".equals(ret.getRetCode())){
			throw new AccountException("解绑银行卡失败，失败原因:"+ret.getRetDesc(), 1);
		}
		theAccountBankDao.disable(userId, id);
		return "success";
	}
	
	@Override
	public AccountBank find(long userId, String bankNo,String channlKey) {
		return theAccountBankDao.find(userId, bankNo,channlKey);
	}

	@Override
	public AccountBank findByBankNo(String bankNo) {
		QueryParam param = QueryParam.getInstance().addParam("bankNo", bankNo).addParam("status", 1);
		return theAccountBankDao.findByCriteriaForUnique(param);
	}
}
