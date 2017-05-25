package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.NoticeTypeDao;
import com.rongdu.p2psys.core.domain.NoticeType;

@Repository("noticeTypeDao")
public class NoticeTypeDaoImpl extends BaseDaoImpl<NoticeType> implements NoticeTypeDao {

	@Override
	public PageDataList<NoticeType> noticeTypeList(int page) {
		QueryParam param = QueryParam.getInstance();
		param.addOrder(OrderType.ASC, "nid");
		param.addOrder(OrderType.ASC, "noticeType");
		return this.findPageList(param);
	}

	@Override
	public int modify(NoticeType noticeType) {
		String jpql = "UPDATE NoticeType SET name=:name,type=:type,send=:send,sendRoute=:sendRoute,"
				+ "titleTemplet=:titleTemplet, templet=:templet,remark=:remark,"
				+ "updateTime=:updateTime,updateIp=:updateIp WHERE nid=:nid AND noticeType=:noticeType";
		Query query = em.createQuery(jpql);
		query.setParameter("name", noticeType.getName());
		query.setParameter("type", noticeType.getType());
		query.setParameter("send", noticeType.getSend());
		query.setParameter("sendRoute", noticeType.getSendRoute());
		query.setParameter("titleTemplet", noticeType.getTitleTemplet());
		query.setParameter("templet", noticeType.getTemplet());
		query.setParameter("remark", noticeType.getRemark());
		query.setParameter("updateTime", noticeType.getUpdateTime());
		query.setParameter("updateIp", noticeType.getUpdateIp());
		query.setParameter("nid", noticeType.getNid());
		query.setParameter("noticeType", noticeType.getNoticeType());
		return query.executeUpdate();
	}

	@Override
	public List<NoticeType> findAllSend() {
		// 系统通知短信用户也可以配置为接收或者不接收，所以传回前台的配置类型不能只是用户类型短信了，要全部发送短信类型，但验证类的短信比较特殊，不能配置，所以只有可canswitch为1才能配置
		QueryParam param = QueryParam.getInstance();
		param.addParam("send", 1).addParam("canSwitch", 1);
		return findByCriteria(param);
	}

	@Override
	public NoticeType findByNid(String nid, int noticeType) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("nid", nid).addParam("noticeType", noticeType);
		return super.findByCriteriaForUnique(param);
	}

	@Override
	public List<NoticeType> list() {
		QueryParam param = QueryParam.getInstance();
		param.addOrder(OrderType.ASC, "nid").addOrder(OrderType.ASC, "noticeType");
		return findByCriteria(param);
	}

	@Override
	public PageDataList<NoticeType> list(int page, int rows, NoticeType noticeType) {
		QueryParam param = QueryParam.getInstance();
		if (StringUtil.isNotBlank(noticeType.getNid()) && !("all").equals(noticeType.getNid())) {
			param.addParam("nid", noticeType.getNid());
		}
		param.addParam("send", noticeType.getSend());
		param.addOrder(OrderType.DESC, "id");
		param.addPage(page, rows);
		return findPageList(param);
	}
}
