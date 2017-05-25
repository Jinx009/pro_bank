package com.rongdu.p2psys.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;

/**
 * 红包Dao
 */
public interface UserRedPacketDao extends BaseDao<UserRedPacket> {

	UserRedPacket findByNid(String nid);

	double getTotalPacketMoneyByIds(Long[] ids);

	List<Object[]> statisticsByModel(UserRedPacketModel model);

	int getTotalByModel(UserRedPacketModel model);

	/**
	 * 通过投资id得到当前投资使用的红包总额
	 * 
	 * @param tenderId
	 * @return
	 */
	public double getTotalPacketMoneyByTender(long tenderId);

	/**
	 * 通过债权投资id得到当前投资使用的红包总额
	 * 
	 * @param tenderId
	 * @return
	 */
	public double getTotalPacketMoneyByBondTender(long bondTenderId);

	/**
	 * 通过投标id得到当前投资使用的红包列表
	 * 
	 * @param tenderId
	 * @return
	 */
	List<UserRedPacket> getListByTenderId(long tenderId);
	
	/**
	 * 获取可以投资的多选红包
	 * @param tenderId
	 * @return
	 */
	List<UserRedPacket> getMultiAvalibleRedPacketList(User user);
	
	/**
	 * 获取可以投资的单选红包
	 * @param tenderId
	 * @return
	 */
	List<UserRedPacket> getSingleAvalibleRedPacketList(User user);
	
	/**
	 * 获取提醒的红包列表
	 * @return
	 */
	List<UserRedPacket> getRemindRedPacketList();
	
}
