package com.rongdu.p2psys.core.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.p2psys.core.dao.MessageDao;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.model.MessageModel;
import com.rongdu.p2psys.user.exception.UserException;

@Repository
public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao {

	@Override
	public int unreadCount(long receiveUser) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 0);
		param.addParam("delType", 0);
		param.addParam("receiveUser.userId", receiveUser);
		return super.countByCriteria(param);
	}

	@Override
	public PageDataList<MessageModel> receiveList(long receiveUser, int startPage) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("delType", 0);
		param.addParam("receiveUser.userId", receiveUser);
		param.addOrder(OrderType.DESC, "addTime");
		param.addPage(startPage);
		PageDataList<Message> pageDateList = super.findPageList(param);
		PageDataList<MessageModel> pageDateList_ = new PageDataList<MessageModel>();
		List<MessageModel> list = new ArrayList<MessageModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				Message m = (Message) pageDateList.getList().get(i);
				MessageModel mm = MessageModel.instance(m);
				if(m.getSentUser() != null){
					mm.setSentUserName(m.getSentUser().getUserName());
				}else{
					mm.setSentUserName("");
				}
				
				mm.setReceiveUserName(m.getReceiveUser().getUserName());
				list.add(mm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public PageDataList<MessageModel> sentList(long sentUser, int startPage) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("delType", 0);
		param.addParam("sented", 1);
		param.addParam("sentUser.userId", sentUser);
		param.addOrder(OrderType.DESC, "addTime");
		param.addPage(startPage);
		PageDataList<Message> pageDateList = super.findPageList(param);
		PageDataList<MessageModel> pageDateList_ = new PageDataList<MessageModel>();
		List<MessageModel> list = new ArrayList<MessageModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				Message m = (Message) pageDateList.getList().get(i);
				MessageModel mm = MessageModel.instance(m);
				mm.setSentUserName(m.getSentUser().getUserName());
				mm.setReceiveUserName(m.getReceiveUser().getUserName());
				list.add(mm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public MessageModel find(long id, long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		SearchFilter filter1 = new SearchFilter("sentUser.userId", userId);
		SearchFilter filter2 = new SearchFilter("receiveUser.userId", userId);
		param.addOrFilter(filter1, filter2);
		Message m = super.findByCriteriaForUnique(param);
		MessageModel model = MessageModel.instance(m);
		model.setSentUserName(m.getSentUser().getUserName());
		model.setReceiveUserName(m.getReceiveUser().getUserName());
		return model;
	}

	@Override
	public void setRead(long id, long receiveUser) {
		String jpql = " UPDATE Message SET status = 1 WHERE id = :id AND receiveUser.userId = :receiveUser ";
		Query query = em.createQuery(jpql);
		query.setParameter("id", id);
		query.setParameter("receiveUser", receiveUser);
		query.executeUpdate();
	}

	@Override
	public void delReceiveList(Object[] ids, long receiveUser) {
		QueryParam param = QueryParam.getInstance();
		if (ids.length > 1) {
			SearchFilter filters[] = new SearchFilter[ids.length];
			for (int i = 0; i < ids.length; i++) {
				SearchFilter searchFilter = new SearchFilter("id", Operators.EQ, ids[i]);
				filters[i] = searchFilter;
			}
			param.addOrFilter(filters);
		} else if (ids.length == 1) {
			param.addParam("id", ids[0]);
		} else {
			throw new UserException("请选择删除的接收信息！", 1);
		}
		param.addParam("receiveUser.userId", receiveUser);
		List<Message> list = super.findByCriteria(param);
		for (Message message : list) {
			message.setDelType(1);
			super.update(message);
		}
	}

	@Override
	public void delSentList(Object[] ids, long sentUser) {
		QueryParam param = QueryParam.getInstance();
		if (ids.length > 1) {
			SearchFilter filters[] = new SearchFilter[ids.length];
			for (int i = 0; i < ids.length; i++) {
				SearchFilter searchFilter = new SearchFilter("id", Operators.EQ, ids[i]);
				filters[i] = searchFilter;
			}
			param.addOrFilter(filters);
		} else if (ids.length == 1) {
			param.addParam("id", ids[0]);
		} else {
			throw new UserException("请选择删除的发送信息！", 1);
		}
		param.addParam("sentUser.userId", sentUser);
		List<Message> list = super.findByCriteria(param);
		for (Message message : list) {
			message.setSented(0);
			super.update(message);
		}
	}

	@Override
	public void setReadList(Object[] ids, long receiveUser) {
		QueryParam param = QueryParam.getInstance();
		if (ids.length > 1) {
			SearchFilter filters[] = new SearchFilter[ids.length];
			for (int i = 0; i < ids.length; i++) {
				SearchFilter searchFilter = new SearchFilter("id", Operators.EQ, ids[i]);
				filters[i] = searchFilter;
			}
			param.addOrFilter(filters);
		} else if (ids.length == 1) {
			param.addParam("id", ids[0]);
		} else {
			throw new UserException("请选择要设置为已读信息的信息！", 1);
		}
		param.addParam("receiveUser.userId", receiveUser);
		List<Message> list = super.findByCriteria(param);
		for (Message message : list) {
			message.setStatus(1);
			super.update(message);
		}
	}

	@Override
	public void setUnreadList(Object[] ids, long receiveUser) {
		QueryParam param = QueryParam.getInstance();
		if (ids.length > 1) {
			SearchFilter filters[] = new SearchFilter[ids.length];
			for (int i = 0; i < ids.length; i++) {
				SearchFilter searchFilter = new SearchFilter("id", Operators.EQ, ids[i]);
				filters[i] = searchFilter;
			}
			param.addOrFilter(filters);
		} else if (ids.length == 1) {
			param.addParam("id", ids[0]);
		} else {
			throw new UserException("请选择要设置为未读信息的信息！", 1);
		}
		param.addParam("receiveUser.userId", receiveUser);
		List<Message> list = super.findByCriteria(param);
		for (Message message : list) {
			message.setStatus(0);
			super.update(message);
		}
	}

}
