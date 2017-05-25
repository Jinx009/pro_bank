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
import com.rongdu.p2psys.nb.payment.dao.IChannelUrlDao;
import com.rongdu.p2psys.nb.payment.domain.ChannelUrl;
import com.rongdu.p2psys.nb.payment.model.ChannelUrlModel;

@Repository("channelUrlDao")
public class ChannelUrlDaoImpl extends BaseDaoImpl<ChannelUrl>  implements IChannelUrlDao {

	@Override
	public PageDataList<ChannelUrlModel> findChannelUrlByItem(int pageNumber,
			int pageSize, String searchName) {
		PageDataList<ChannelUrlModel> pageDataList = new PageDataList<ChannelUrlModel>();
		StringBuffer sql =new StringBuffer("select cl.* from nb_channel_url as cl left join nb_channel nc on cl.cid=nc.id where 1=1 ");
		if(!StringUtil.isBlank(searchName)){
			sql.append("AND (nc.channel_name like '%"+searchName+"%' OR nc.channel_key like '%"+searchName+"%')");
		} 
		sql.append(" order by cl.id desc");
		Query query = em.createNativeQuery(sql.toString(),ChannelUrl.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<ChannelUrl> list = query.getResultList();
        List<ChannelUrlModel> list_ =new ArrayList<ChannelUrlModel>();
        for (ChannelUrl channelUrl : list) {
			ChannelUrlModel model = ChannelUrlModel.instance(channelUrl);
			model.setChannelKey(channelUrl.getChanel().getChannelKey());
			model.setChannelName(channelUrl.getChanel().getChannelName());
			list_.add(model);
		}
        pageDataList.setList(list_);
        pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public ChannelUrl loadChannelUrlById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<ChannelUrl> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (ChannelUrl) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteChannelUrlById(long id) {
		ChannelUrl channelUrl = super.find(id);
		if(null != channelUrl){
			super.delete(id);
		}
	}

	@Override
	public void updateChannelUrl(ChannelUrl channelUrl) {
		em.merge(channelUrl);
	}

}
