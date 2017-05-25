package com.rongdu.p2psys.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountBankModel;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.nb.payment.dao.IChannelBankDao;
import com.rongdu.p2psys.nb.payment.dao.IChannelDao;
import com.rongdu.p2psys.tpp.domain.YjfDrawBank;
import com.rongdu.p2psys.tpp.yjf.dao.YjfDrawBankDao;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.domain.User;

@Service("accountBankService")
public class AccountBankServiceImpl implements AccountBankService {

	@Resource
	private AccountBankDao accountBankDao;
	
	@Resource
	private IChannelDao channelDao;
	@Resource
	private IChannelBankDao channelBankDao;
	
	/**
	 * 用户详细信息dao
	 */
	@Resource
	private UserCacheDao userCacheDao;
	/**
	 * 数据字典dao
	 */
	@Resource
	private DictDao dictDao;
	@Resource
	private YjfDrawBankDao yjfDrawBankDao;
	@Override
	public List<AccountBank> list(long userId) {
		List<AccountBank> ablist = accountBankDao.list(userId);
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
	public List<AccountBank> list(long userId,String channelKey) {
		List<AccountBank> ablist = accountBankDao.list(userId,channelKey);
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
	public List<AccountBank> listAll(long userId) {
		return accountBankDao.listAll(userId);
	}

	@Override
	public AccountBank save(AccountBank bank) {
		/*if (BaseTPPWay.isOpenApi()) {
			YjfDrawBank yb = yjfDrawBankDao.getYjfDrawBankByBankName(bank.getBank());
			if (yb != null) {
				TPPWay dw = TPPFactory.getTPPWay();
				BankNoQuery bnq = PayModelHelper.bankNoQuery(bank.getCity(), yb.getBankCode());
				if (StringUtil.isBlank(bnq.getBankLasalle())) {
					throw new AccountException(" 根据您提供的银行卡所在市区，我们查询不到任何交易银行，" 
							+ " 请您核对绑定银行卡时选择的省市区 ", 1);
				}
				VerifyFacade vf = null;
			    vf = PayModelHelper.verifyFacade(bank.getBankNo(), yb, bank.getUser(), bank.getUser());
			    //校验银行卡的有效性失败
			    if (!"VS".equals(vf.getVerifyStatus())) {
			    	throw new AccountException("绑卡校验信息失败：" + vf.getResultMessage(), 1);
			    }  
			    
				dw.bindBank(bank, bank.getUser(), yb.getBankCode());

				bank.setBank(yb.getBankName());
				bank.setYjfDrawBank(yb);
			} else {
				throw new AccountException("绑卡出现异常，请联系客服处理！", 1);
			}
		}
		
		bank.setAddTime(new Date());
		bank.setAddIp(Global.getIP());
		bank.setStatus(1);*/
		
		return accountBankDao.save(bank);
	}
	@Override
    public AccountBank update(AccountBank bank) {
	    return accountBankDao.update(bank);
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
		AccountBank ab = accountBankDao.find(id);
		UnionPayRet ret = SignHelper.unbind(ab.getBindId());
		if(!"0000".equals(ret.getRetCode())){
			throw new AccountException("解绑银行卡失败，失败原因:"+ret.getRetDesc(), 1);
		}
		accountBankDao.disable(userId, id);
		return "success";
	}

	@Override
	public int count(long userId) {
//		accountBankDao.count(userId);
		return this.list(userId).size();
	}

	@Override
	public AccountBank find(long userId, String bankNo,String channlKey) {
		return accountBankDao.find(userId, bankNo,channlKey);
	}

	@Override
	public PageDataList<AccountBankModel> accountBankList(AccountBankModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("bankNo", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter3 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1, orFilter2, orFilter3);
		}else{
			if (model != null && !StringUtil.isBlank(model.getBankNo())) {
				param.addParam("bankNo", Operators.EQ, model.getBankNo());
			}
			if (model != null && !StringUtil.isBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (model != null && !StringUtil.isBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			if (model != null && model.getStatus() != -1) {
				param.addParam("status", model.getStatus());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<AccountBank> pList = accountBankDao.findPageList(param);
		PageDataList<AccountBankModel> modelList = new PageDataList<AccountBankModel>();
		List<AccountBankModel> list = new ArrayList<AccountBankModel>();
		modelList.setPage(pList.getPage());
		if (pList.getList().size() > 0) {
			for (int i = 0; i < pList.getList().size(); i++) {
				AccountBank accountBank = (AccountBank) pList.getList().get(i);
				AccountBankModel abm = AccountBankModel.instance(accountBank);
				try {
					abm.setUserName(accountBank.getUser().getUserName());
					abm.setRealName(accountBank.getUser().getRealName());
					abm.setProvinceStr(accountBank.getProvince());
					abm.setCityStr(accountBank.getCity());
					abm.setAreaStr(accountBank.getArea());
					list.add(abm);
				} catch (Exception e) {}
			}
		}
		modelList.setList(list);
		return modelList;
	}
	
	@Override
	public List<YjfDrawBank> getYjfDrawBankBySearchParam(QueryParam param) {
		return yjfDrawBankDao.findByCriteria(param);
	}

	@Override
	public AccountBank findByBankNo(String bankNo) {
		QueryParam param = QueryParam.getInstance().addParam("bankNo", bankNo).addParam("status", 1);
		return accountBankDao.findByCriteriaForUnique(param);
	}
	
	@Override
	public AccountBank findByBankNoAndUserid(String bankNo, User user) {
		QueryParam param = QueryParam.getInstance().addParam("bankNo", bankNo).addParam("status", 1).addParam("user", user);
		return accountBankDao.findByCriteriaForUnique(param);
	}

	@Override
	public String disable(long id) {
		AccountBank ab = accountBankDao.find(id);
		UnionPayRet ret = SignHelper.unbind(ab.getBindId());
		if(!"0000".equals(ret.getRetCode())){
			throw new AccountException("解绑银行卡失败，失败原因:"+ret.getRetDesc(), 1);
		}
		accountBankDao.disable(ab.getUser().getUserId(), id);
		return "success";
	}

	@Override
	public AccountBank findById(long id) {
		return accountBankDao.findById(id);
	}
}
