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
import com.rongdu.p2psys.nb.payment.dao.IChannelDao;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.model.ChannelModel;

@Repository("channelDao")
public class ChannelDaoImpl extends BaseDaoImpl<Channel>  implements IChannelDao {

	@Override
	public PageDataList<ChannelModel> findChannelByItem(int pageNumber,
			int pageSize, String searchName) {
		PageDataList<ChannelModel> pageDataList = new PageDataList<ChannelModel>();
		StringBuffer sql =new StringBuffer("select cl.* from nb_channel as cl where 1=1 ");
		if(!StringUtil.isBlank(searchName)){
			sql.append("AND (cl.channel_name like '%"+searchName+"%' OR cl.channel_key like '%"+searchName+"%')");
		} 
		sql.append(" order by cl.id desc");
		Query query = em.createNativeQuery(sql.toString(),Channel.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Channel> list = query.getResultList();
        List<ChannelModel> list_ =new ArrayList<ChannelModel>();
        for (Channel channel : list) {
			ChannelModel model = ChannelModel.instance(channel);
			list_.add(model);
		}
        pageDataList.setList(list_);
        pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public Channel loadChannelById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<Channel> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (Channel) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteChannelById(long id) {
		Channel channel = super.find(id);
		if(null != channel){
			super.delete(id);
		}
	}

	@Override
	public void updateChannel(Channel channel) {
		em.merge(channel);
	}

}
