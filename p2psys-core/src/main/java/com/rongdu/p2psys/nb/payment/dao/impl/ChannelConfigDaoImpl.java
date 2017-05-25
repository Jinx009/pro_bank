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
import com.rongdu.p2psys.nb.payment.dao.IChannelConfigDao;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;

@Repository("channelConfigDao")
public class ChannelConfigDaoImpl extends BaseDaoImpl<ChannelConfig>  implements IChannelConfigDao {

	@Override
	public PageDataList<ChannelConfigModel> findChannelConfigByItem(int pageNumber,
			int pageSize, String searchName) {
		PageDataList<ChannelConfigModel> pageDataList = new PageDataList<ChannelConfigModel>();
		StringBuffer sql =new StringBuffer("select cl.*,sb.bank_name from nb_channel_config as cl left join nb_support_bank sb on cl.bid=sb.id where 1=1 ");
		if(!StringUtil.isBlank(searchName)){
			sql.append("AND cl.channel_key like '%"+searchName+"%'");
		} 
		sql.append(" order by cl.id desc");
		Query query = em.createNativeQuery(sql.toString(),ChannelConfig.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<ChannelConfig> list = query.getResultList();
        List<ChannelConfigModel> list_ =new ArrayList<ChannelConfigModel>();
        for (ChannelConfig channelConfig : list) {
			ChannelConfigModel model = ChannelConfigModel.instance(channelConfig);
			model.setBankName(channelConfig.getSb().getBankName());
			model.setBankLogo(channelConfig.getSb().getBankLogo());
			list_.add(model);
		}
        pageDataList.setList(list_);
        pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public ChannelConfig loadChannelConfigById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<ChannelConfig> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (ChannelConfig) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteChannelConfigById(long id) {
		ChannelConfig channelConfig = super.find(id);
		if(null != channelConfig){
			super.delete(id);
		}
	}

	@Override
	public void updateChannelConfig(ChannelConfig channelConfig) {
		em.merge(channelConfig);
	}

	@Override
	public ChannelConfig getChannelConfigByBid(long bId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("sb.id", bId);
		ChannelConfig cc = super.findByCriteriaForUnique(param);
		return cc;
	}

	@Override
	public List<ChannelConfigModel> loadChannelInfo() {
		StringBuffer sql =new StringBuffer("select cl.*,sb.bank_name from nb_channel_config as cl left join nb_support_bank sb on cl.bid=sb.id where 1=1 ");
		Query query = em.createNativeQuery(sql.toString(),ChannelConfig.class);
        List<ChannelConfig> list = query.getResultList();
        List<ChannelConfigModel> list_ =new ArrayList<ChannelConfigModel>();
        for (ChannelConfig channelConfig : list) {
			ChannelConfigModel model = ChannelConfigModel.instance(channelConfig);
			model.setBankName(channelConfig.getSb().getBankName());
			model.setBankLogo(channelConfig.getSb().getBankLogo());
			String webChannelKey = channelConfig.getWebRechargeKey();
			String webRechargeUrl = ConstantUtil.WEB_LLPAY_RECHARGE_URL;
		    if(webChannelKey.equals(channelKey.unionpay.getValue())){
		    	webRechargeUrl = ConstantUtil.WEB_UNIONPAY_RECHARGE_URL;
		    }
		    model.setWebRechargeKey(webChannelKey);
		    model.setWebRechargeURL(webRechargeUrl);
		    String wapChannelKey = channelConfig.getWapRechargeKey();
			String wapRechargeUrl = ConstantUtil.WAP_LLPAY_RECHARGE_URL;
		    if(wapChannelKey.equals(channelKey.unionpay.getValue())){
		    	wapRechargeUrl = ConstantUtil.WAP_UNIONPAY_RECHARGE_URL;
		    }
		    model.setWapRechargeKey(wapChannelKey);
		    model.setWapRechargeURL(wapRechargeUrl);
			list_.add(model);
		}
		return list_;
	}

	/*@Override
	public String loadGatewayUrlByKey(String gatewayKey) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("gatewayKey", gatewayKey);
		ChannelConfig cc = super.findByCriteriaForUnique(param);
		if (cc != null) {
			return cc.getGatewayUrl();
		} else {
			return null;
		}
	}*/
	
	/*@Override
	public ChannelConfig getChannelConfigByKey(String gatewayKey) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("gatewayKey", gatewayKey);
		ChannelConfig cc = super.findByCriteriaForUnique(param);
		return cc;
	}*/

}
