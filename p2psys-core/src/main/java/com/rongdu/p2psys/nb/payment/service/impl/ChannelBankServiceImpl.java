package com.rongdu.p2psys.nb.payment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.nb.payment.dao.IChannelBankDao;
import com.rongdu.p2psys.nb.payment.dao.IChannelDao;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.model.ChannelBankModel;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

/***
 * 支付通道银行卡业务处理
 * @author ChenGangwei
 * 2015-06-26
 */
@Service("channelBankService")
public class ChannelBankServiceImpl implements IChannelBankService {

	@Resource
	private UserDao userDao;
	
	@Resource                        
	private IChannelBankDao channelBankDao;
	
	@Resource                        
	private IChannelDao channelDao;
	
	@Resource                        
	private AccountBankDao accountBankDao;
	
	@Override
	public PageDataList<ChannelBankModel> findChannelBankByItem(int pageNumber,
			int pageSize, String searchName) {
		return channelBankDao.findChannelBankByItem(pageNumber,pageSize, searchName);
	}

	@Override
	public void updateChannelBank(ChannelBankModel model) {
		ChannelBank channelBank = ChannelBankModel.instance(model);
		channelBankDao.updateChannelBank(channelBank);
		AccountBank ab = model.getAb();
		ab.setStatus(model.getStatus());
		accountBankDao.update(ab);
	}

	@Override
	public List<Channel> loadChannelList() {
		return channelDao.findAll();
	}

	@Override
	public List<ChannelBank> list(long userId, String channelKey) {
		List<Channel> clList = channelDao.findByProperty("channelKey", channelKey);
		if(clList!=null && clList.size()>0){
			Channel cl = clList.get(0);
			return channelBankDao.list(userId, cl.getId());
		}
		return null;
	}
	
	@Override
	public void saveChannelBank(User user, AccountBank ab,String channelKey) {
		List<Channel> clList = channelDao.findByProperty("channelKey", channelKey);
		if(clList!=null && clList.size()>0){
			Channel cl = clList.get(0);
			ChannelBank cb = new ChannelBank();
			cb.setChanel(cl);
			cb.setAb(ab);
			cb.setStatus(1);
			cb.setUser(user);
			channelBankDao.save(cb);//保存新帮卡关联
		}
	}

	@Override
	public ChannelBankModel loadChannelBankById(long id) {
		ChannelBank channelBank = channelBankDao.loadChannelBankById(id);
		ChannelBankModel model = ChannelBankModel.instance(channelBank);
		model.setCid(channelBank.getChanel().getId());
		model.setChannelKey(channelBank.getChanel().getChannelKey());
		model.setBankNo(channelBank.getAb().getBankNo());
		model.setBid(channelBank.getAb().getId());
		model.setBindId(channelBank.getAb().getBindId());
		model.setUser(channelBank.getUser());
		return model;
	}

}
