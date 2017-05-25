package com.rongdu.p2psys.nb.user.service;

import java.text.ParseException;
import java.util.List;

import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;

public interface UserRedPacketService
{
	public void updateUserRedPacket(UserRedPacket userRedPacket);
	
	public UserRedPacket saveUserRedPacket(UserRedPacket userRedPacket);
	
	public List<UserRedPacketModel> getRecommendByUserId(long userId) throws ParseException;
	
	public List<UserRedPacketModel> getNotRecommednByUserId(long userId) throws ParseException;

	public List<UserRedPacketModel> findNotUsed(long userId) throws ParseException;
	
	public List<UserRedPacketModel> findUsed(long userId) throws ParseException;
	
	public List<UserRedPacketModel> findOverdue(long userId) throws ParseException;
	
	public void doExchangeRedPacket(String[] id, User user);
	
	public UserRedPacket getById(long id);

	/**
	 * 执行发放红包过程
	 * 
	 * @param type
	 *            发放类型
	 * @param user
	 *            用户
	 * @param tender
	 *            投标记录（如果与投标相关则传值，否则传null）
	 * @param recharge
	 *            充值（如果与充值相关则传值，否则传null）
	 */
	public void doRedPacket(String type, User user, BorrowTender tender,
			AccountRecharge recharge);
}
