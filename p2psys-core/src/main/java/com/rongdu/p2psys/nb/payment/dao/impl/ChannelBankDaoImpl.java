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
import com.rongdu.p2psys.nb.payment.dao.IChannelBankDao;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.model.ChannelBankModel;

@Repository("channelBankDao")
public class ChannelBankDaoImpl extends BaseDaoImpl<ChannelBank>  implements IChannelBankDao {

	@Override
	public PageDataList<ChannelBankModel> findChannelBankByItem(int pageNumber,
			int pageSize, String searchName) {
		PageDataList<ChannelBankModel> pageDataList = new PageDataList<ChannelBankModel>();
		StringBuffer sql =new StringBuffer("select cl.* from nb_channel_Bank as cl left join nb_channel nc on cl.cid=nc.id left join rd_user ru on cl.user_id=ru.user_id where 1=1 ");
		if(!StringUtil.isBlank(searchName)){
			sql.append("AND (nc.channel_name like '%"+searchName+"%' OR nc.channel_key like '%"+searchName+"%' OR ru.real_name like '%"+searchName+"%')");
		} 
		sql.append(" order by cl.id desc");
		Query query = em.createNativeQuery(sql.toString(),ChannelBank.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<ChannelBank> list = query.getResultList();
        List<ChannelBankModel> list_ =new ArrayList<ChannelBankModel>();
        for (ChannelBank channelBank : list) {
			ChannelBankModel model = ChannelBankModel.instance(channelBank);
			model.setChannelKey(channelBank.getChanel().getChannelKey());
			model.setChannelName(channelBank.getChanel().getChannelName());
			model.setBank(channelBank.getAb().getBank());
			model.setBankNo(channelBank.getAb().getBankNo());
			model.setBid(channelBank.getAb().getId());
			model.setBindId(channelBank.getAb().getBindId());
			model.setRealName(channelBank.getUser().getRealName());
			model.setAddTime(String.valueOf(channelBank.getAb().getAddTime()));
			list_.add(model);
		}
        pageDataList.setList(list_);
        pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public ChannelBank loadChannelBankById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<ChannelBank> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (ChannelBank) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteChannelBankById(long id) {
		ChannelBank channelBank = super.find(id);
		if(null != channelBank){
			super.delete(id);
		}
	}

	@Override
	public void updateChannelBank(ChannelBank channelBank) {
		em.merge(channelBank);
	}
	
	@Override
	public List<ChannelBank> list(long userId,long channelId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addParam("chanel.id", channelId);
		param.addParam("status", 1);
		return findByCriteria(param);
	}

}
