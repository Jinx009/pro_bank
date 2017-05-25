package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;

public interface UserRedPacketService {
	/**
	 * 获取红包列表
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	PageDataList<UserRedPacketModel> findByUser(User user, int page);

	/**
	 * 获取红包列表
	 * 
	 * @param user
	 * @return
	 */
	List<UserRedPacketModel> findByUser(User user);
	
	/**
	 * 获取红包列表
	 * 
	 * @param user
	 * @return
	 */
	List<UserRedPacketModel> findByUserAndSelType(User user,int selType);
	
	
	/**
	 * 获取红包列表
	 * 
	 * @param user
	 * @return
	 */
	UserRedPacket findUserRedPacketById(long redPacketId);

	/**
	 * 根据红包ID获取红包总价值
	 * 
	 * @param ids
	 * @return
	 */
	double getTotalPacketMoneyByIds(Long[] ids);

	/**
	 * 根据model对象查找红包列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<UserRedPacketModel> findByModel(UserRedPacketModel model);

	/**
	 * 根据model对象统计红包
	 * 
	 * @param model
	 * @return
	 */
	List<UserRedPacketModel> statisticsByModel(UserRedPacketModel model);

	/**
	 * 根据model对象统计红包页面
	 * 
	 * @param model
	 * @return
	 */
	int getTotalByModel(UserRedPacketModel model);

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

	/**
	 * 执行投标相关红包
	 * 
	 * @param user
	 * @param tender
	 */
	public void doTenderRedPacket(User user, BorrowTender tender);
	
	/**
	 * 投标生成加息券
	 * 
	 * @param user
	 * @param tender
	 */
	public void doTenderCoupon(User user, BorrowTender tender);

	/**
	 * 执行充值相关红包
	 * 
	 * @param user
	 * @param recharge
	 */
	public void doRechargeRedPacket(User user, AccountRecharge recharge);

	/**
	 * 校验红包类型是否为现金红包
	 * 
	 * @param id
	 * @return
	 */
	boolean checkIsCashRedPacket(String[] id);

	/*
	 * 给单个用户发放红包
	 * */
	void giveUserRedPacket(RedPacket redPacket,User user);
	
	/*
	 * 提醒红包到期提醒  
	 * */
	void remindUserDueRedPacket();
	
	/**
	 * 发放红包
	 * 
	 * @param id
	 */
	void doExchangeRedPacket(String[] id, User user);

	/**
	 * 执行借款标红包
	 * 
	 * @param user
	 * @param tender
	 */
	void doBorrowRedPacket(User user, BorrowTender tender);

	void doPpfundRedPacket(User user, PpfundIn ppfundIn);
}
