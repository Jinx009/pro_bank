package com.rongdu.p2psys.nb.payment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.dao.IChannelConfigDao;
import com.rongdu.p2psys.nb.payment.dao.ISupportBankDao;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.user.dao.UserDao;

/***
 * 支付通道配置业务处理
 * @author ChenGangwei
 * 2015-06-23
 */
@Service("channelConfigService")
public class ChannelConfigServiceImpl implements IChannelConfigService {

	@Resource
	private UserDao userDao;
	
	@Resource                        
	private IChannelConfigDao channelConfigDao;
	@Resource
	private ISupportBankDao theSupportBankDao;
	
	@Override
	public PageDataList<ChannelConfigModel> findChannelConfigByItem(int pageNumber,
			int pageSize, String searchName) {
		return channelConfigDao.findChannelConfigByItem(pageNumber,pageSize, searchName);
	}

	@Override
	public ChannelConfigModel loadChannelConfigById(long id) {
		ChannelConfig channelConfig = channelConfigDao.loadChannelConfigById(id);
		ChannelConfigModel model = ChannelConfigModel.instance(channelConfig);
		model.setBankLogo(channelConfig.getSb().getBankLogo());
		model.setBankName(channelConfig.getSb().getBankName());
		model.setBid(channelConfig.getSb().getId());
		return model;
	}

	@Override
	public void deleteChannelConfigById(long id) {
		channelConfigDao.deleteChannelConfigById(id);
	}

	@Override
	public void updateChannelConfig(ChannelConfigModel model) {
		ChannelConfig channelConfig = ChannelConfigModel.instance(model);
		String webRechargeKey = channelConfig.getWebRechargeKey();
		String webCashKey = channelConfig.getWebCashKey();
		String wapRechargeKey = channelConfig.getWapRechargeKey();
		String wapCashKey = channelConfig.getWapCashKey();
		if(webRechargeKey.equals("银联支付")){
			webRechargeKey = channelKey.unionpay.getValue();
		}else{
			webRechargeKey = channelKey.llpay.getValue();
		}
		if(webCashKey.equals("银联支付")){
			webCashKey = channelKey.unionpay.getValue();
		}else{
			webCashKey = channelKey.llpay.getValue();
		}
		if(wapRechargeKey.equals("银联支付")){
			wapRechargeKey = channelKey.unionpay.getValue();
		}else{
			wapRechargeKey = channelKey.llpay.getValue();
		}
		if(wapCashKey.equals("银联支付")){
			wapCashKey = channelKey.unionpay.getValue();
		}else{
			wapCashKey = channelKey.llpay.getValue();
		}
		NbSupportBank sb = new NbSupportBank();
		sb.setId(model.getBid());
		channelConfig.setSb(sb);
		channelConfig.setWebRechargeKey(webRechargeKey);
		channelConfig.setWebCashKey(webCashKey);
		channelConfig.setWapRechargeKey(wapRechargeKey);
		channelConfig.setWapCashKey(wapCashKey);
		channelConfigDao.updateChannelConfig(channelConfig);		
	}

	@Override
	public void saveChannelConfig(ChannelConfigModel model) {
		ChannelConfig channelConfig = ChannelConfigModel.instance(model);
		channelConfigDao.save(channelConfig);
	}

	@Override
	public List<ChannelConfigModel> loadChannelInfo() {
		return channelConfigDao.loadChannelInfo();
	}

	/*@Override
	public String loadGatewayUrlByKey(String gatewayKey) {
		return channelConfigDao.loadGatewayUrlByKey(gatewayKey);
	}*/
	
	@Override
	public ChannelConfig getChannelConfigByBid(long bId){
		return channelConfigDao.getChannelConfigByBid(bId);
	}

	@Override
	public ChannelConfig getChannelConfigByCode(String bankCode) {
		NbSupportBank sb = theSupportBankDao.loadSupportBankByCode(bankCode);
		if(sb!=null){			
			return this.getChannelConfigByBid(sb.getId());
		}
		return null;
	}

}
