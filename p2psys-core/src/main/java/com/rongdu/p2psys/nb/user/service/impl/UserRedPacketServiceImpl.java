package com.rongdu.p2psys.nb.user.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.RedPacketDetailDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.nb.account.dao.AccountDao;
import com.rongdu.p2psys.nb.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.nb.recommend.dao.RedPacketDao;
import com.rongdu.p2psys.nb.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.nb.user.service.UserRedPacketService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;

@Service("theUserRedPacketService")
public class UserRedPacketServiceImpl implements UserRedPacketService 
{

	@Resource
	private UserRedPacketDao theUserRedPacketDao;
	@Resource
	private AccountDao theAccountDao;
	@Resource
	private UserService theUserService;
	@Resource
	private RedPacketDao theRedPacketDao;
	@Resource
	private RedPacketDetailDao redPacketDetailDao;
	@Resource
	private BorrowTenderDao theBorrowTenderDao;
	
	public void updateUserRedPacket(UserRedPacket userRedPacket)
	{
		theUserRedPacketDao.update(userRedPacket);
	}

	public UserRedPacket saveUserRedPacket(UserRedPacket userRedPacket)
	{
		return theUserRedPacketDao.save(userRedPacket);
	}

	public List<UserRedPacketModel> getRecommendByUserId(long userId) throws ParseException
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" SELECT ");
		buffer.append(" 	u.*, s.service_type, ");
		buffer.append("		s.name ");
		buffer.append(" FROM ");
		buffer.append(" 	rd_user_red_packet u, ");
		buffer.append(" 	s_red_packet s ");
		buffer.append(" WHERE ");
		buffer.append(" 	u.is_used = 0 ");
		buffer.append("    AND u.user_id =  ");
		buffer.append(userId);
		buffer.append("	   AND ( ");
		buffer.append(" 		TO_DAYS(NOW()) < TO_DAYS(u.expired_time) ");
		buffer.append("		   ) ");
		buffer.append("    AND u.red_packet_type = 2 ");
		buffer.append("    AND u.type_id = s.id ");
		buffer.append("    AND s.service_type = 'recommend'  "); 
		buffer.append("    AND s.status = 1 ");
		buffer.append("	   ORDER BY u.expired_time");
		
		List<Object[]> list =  theUserRedPacketDao.findBySql(buffer.toString());
		List<UserRedPacketModel> data = new ArrayList<UserRedPacketModel>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		
		for(int i = 0 ;i<list.size();i++)
		{
			UserRedPacketModel userRedPacketModel = new UserRedPacketModel();
			
			Object[] object = list.get(i);
			
			Date d = format.parse(object[8].toString());
			
			userRedPacketModel.setId(Long.valueOf(object[0].toString()));
			userRedPacketModel.setAmount(Double.valueOf(object[3].toString()));
			userRedPacketModel.setExpiredTime(d);
			userRedPacketModel.setServiceName(object[14].toString());
			userRedPacketModel.setServiceType(object[13].toString());
			
			data.add(userRedPacketModel);
		}
		
		return data;
	}

	public List<UserRedPacketModel> getNotRecommednByUserId(long userId) throws ParseException
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" SELECT ");
		buffer.append(" 	u.*, s.service_type, ");
		buffer.append("		s.name ");
		buffer.append(" FROM ");
		buffer.append(" 	rd_user_red_packet u, ");
		buffer.append(" 	s_red_packet s ");
		buffer.append(" WHERE ");
		buffer.append(" 	u.is_used = 0 ");
		buffer.append("    AND u.user_id =  ");
		buffer.append(userId);
		buffer.append("	   AND ( ");
		buffer.append(" 		TO_DAYS(NOW()) < TO_DAYS(u.expired_time) ");
		buffer.append("		   ) ");
		buffer.append("    AND u.red_packet_type = 2 ");
		buffer.append("    AND u.type_id = s.id ");
		buffer.append("    AND s.service_type != 'recommend'  ");
		buffer.append("    AND s.status = 1  ");
		buffer.append("	   ORDER BY u.expired_time");
		
		List<Object[]> list =  theUserRedPacketDao.findBySql(buffer.toString());
		List<UserRedPacketModel> data = new ArrayList<UserRedPacketModel>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		
		for(int i = 0 ;i<list.size();i++)
		{
			UserRedPacketModel userRedPacketModel = new UserRedPacketModel();
			
			Object[] object = list.get(i);
			
			Date d=format.parse(object[8].toString());
			
			userRedPacketModel.setId(Long.valueOf(object[0].toString()));
			userRedPacketModel.setAmount(Double.valueOf(object[3].toString()));
			userRedPacketModel.setExpiredTime(d);
			userRedPacketModel.setServiceName(object[14].toString());
			userRedPacketModel.setServiceType(object[13].toString());
			
			data.add(userRedPacketModel);
		}
		
		return data;
	}

	public List<UserRedPacketModel> findNotUsed(long userId) throws ParseException
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" SELECT ");
		buffer.append(" 	u.*, s.service_type, ");
		buffer.append("		s.name ");
		buffer.append(" FROM ");
		buffer.append(" 	rd_user_red_packet u, ");
		buffer.append(" 	s_red_packet s ");
		buffer.append(" WHERE ");
		buffer.append(" 	u.is_used = 0 ");
		buffer.append("    AND u.user_id =  ");
		buffer.append(userId);
		buffer.append("	   AND ( ");
		buffer.append(" 		TO_DAYS(NOW()) < TO_DAYS(u.expired_time) ");
		buffer.append("		   ) ");
		buffer.append("    AND u.type_id = s.id ");
		buffer.append("    AND s.status = 1  ");
		buffer.append("	   ORDER BY u.expired_time");
		
		List<Object[]> list =  theUserRedPacketDao.findBySql(buffer.toString());
		List<UserRedPacketModel> data = new ArrayList<UserRedPacketModel>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		
		for(int i = 0 ;i<list.size();i++)
		{
			UserRedPacketModel userRedPacketModel = new UserRedPacketModel();
			
			Object[] object = list.get(i);
			
			Date d=format.parse(object[8].toString());
			
			userRedPacketModel.setId(Long.valueOf(object[0].toString()));
			userRedPacketModel.setAmount(Double.valueOf(object[3].toString()));
			userRedPacketModel.setExpiredTime(d);
			userRedPacketModel.setServiceName(object[14].toString());
			userRedPacketModel.setServiceType(object[13].toString());
			userRedPacketModel.setRedMyPacketType(object[1].toString());
			
			data.add(userRedPacketModel);
		}
		
		return data;
	}

	public List<UserRedPacketModel> findUsed(long userId) throws ParseException
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" SELECT ");
		buffer.append(" 	u.*, s.service_type, ");
		buffer.append("		s.name ");
		buffer.append(" FROM ");
		buffer.append(" 	rd_user_red_packet u, ");
		buffer.append(" 	s_red_packet s ");
		buffer.append(" WHERE ");
		buffer.append(" 	u.is_used = 1 ");
		buffer.append("    AND u.user_id =  ");
		buffer.append(userId);
		buffer.append("    AND u.type_id = s.id ");
		buffer.append("    AND s.status = 1  ");
		buffer.append("	   ORDER BY u.expired_time");
		
		List<Object[]> list =  theUserRedPacketDao.findBySql(buffer.toString());
		List<UserRedPacketModel> data = new ArrayList<UserRedPacketModel>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		
		for(int i = 0 ;i<list.size();i++)
		{
			UserRedPacketModel userRedPacketModel = new UserRedPacketModel();
			
			Object[] object = list.get(i);
			
			Date d=format.parse(object[8].toString());
			Date d2 = null;
			
			if(null!=object[7])
			{
				d2 = format.parse(object[7].toString());
			}
			
			if(null!=object[9])
			{
				Long tenderId = Long.valueOf(object[9].toString());
				BorrowTender borrowTender = theBorrowTenderDao.find(tenderId);
				userRedPacketModel.setTenderName(borrowTender.getBorrow().getName());
			}
			else
			{
				userRedPacketModel.setTenderName("");
			}
			
			userRedPacketModel.setUsedTime(d2);
			userRedPacketModel.setId(Long.valueOf(object[0].toString()));
			userRedPacketModel.setAmount(Double.valueOf(object[3].toString()));
			userRedPacketModel.setExpiredTime(d);
			userRedPacketModel.setServiceName(object[14].toString());
			userRedPacketModel.setServiceType(object[13].toString());
			userRedPacketModel.setRedMyPacketType(object[1].toString());
			
			
			data.add(userRedPacketModel);
		}
		
		return data;
	}

	public List<UserRedPacketModel> findOverdue(long userId) throws ParseException
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" SELECT ");
		buffer.append(" 	u.*, s.service_type, ");
		buffer.append("		s.name ");
		buffer.append(" FROM ");
		buffer.append(" 	rd_user_red_packet u, ");
		buffer.append(" 	s_red_packet s ");
		buffer.append(" WHERE ");
		buffer.append(" 	u.is_used = 0 ");
		buffer.append("    AND u.user_id =  ");
		buffer.append(userId);
		buffer.append("	   AND ( ");
		buffer.append(" 		TO_DAYS(NOW()) >= TO_DAYS(u.expired_time) ");
		buffer.append("		   ) ");
		buffer.append("    AND u.type_id = s.id ");
		buffer.append("    AND s.status = 1  ");
		buffer.append("	   ORDER BY u.expired_time");
		
		List<Object[]> list =  theUserRedPacketDao.findBySql(buffer.toString());
		List<UserRedPacketModel> data = new ArrayList<UserRedPacketModel>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		
		for(int i = 0 ;i<list.size();i++)
		{
			UserRedPacketModel userRedPacketModel = new UserRedPacketModel();
			
			Object[] object = list.get(i);
			
			Date d=format.parse(object[8].toString());
			
			userRedPacketModel.setId(Long.valueOf(object[0].toString()));
			userRedPacketModel.setAmount(Double.valueOf(object[3].toString()));
			userRedPacketModel.setExpiredTime(d);
			userRedPacketModel.setServiceName(object[14].toString());
			userRedPacketModel.setServiceType(object[13].toString());
			userRedPacketModel.setRedMyPacketType(object[1].toString());
			
			data.add(userRedPacketModel);
		}
		
		return data;
	}

	public void doExchangeRedPacket(String[] id, User user)
	{
		double money = 0;
		
		for (String redId : id)
		{
			long redPacketId = NumberUtil.getLong(redId);
			
			UserRedPacket redPacket = theUserRedPacketDao.find(redPacketId);
			
			if (redPacket.getRedPacketType() == 1)
			{
				money += redPacket.getAmount();
				redPacket.setUsed(true);
				
				theUserRedPacketDao.update(redPacket);
			}
		}
		if (money > 0)
		{
			theAccountDao.update(money, money, 0, 0, user.getUserId());
			Global.setTransfer("money", money);
			AbstractExecuter executer = ExecuterHelper.doExecuter("userRedPacketExchangeExecuter");
			User toUser = theUserService.getByUserId(1);
			
			executer.execute(money, user, toUser);
		}
	}

	public UserRedPacket getById(long id)
	{
		return theUserRedPacketDao.find(id);
	}

	@Override
	public void doRedPacket(String type, User user, BorrowTender tender,
			AccountRecharge recharge) {
		// 检测规则是否存在，检测该规则是否配有红包
		RedPacket redPacket = theRedPacketDao.findObjByProperty("serviceType",type);
		Date date = new Date();
		if (redPacket == null // 规则是否存在
				|| redPacket.getStatus() != 1 // 红包是否开启
				|| (redPacket.getStartTime() != null && date.before(redPacket
						.getStartTime())) // 是否还未到活动开始时间
				|| (redPacket.getEndTime() != null && date.after(redPacket
						.getEndTime())) // 是否已经超过活动结束时间
				|| (redPacket.getTotalNum() != 0 && redPacket.getUseNum() >= redPacket
						.getTotalNum())// 红包是否已经发放完毕
				|| (redPacket.getIsDelete() == 1) // 红包是否已被删除
		) {// 判断用书是否为借款人，只有投资人才会有红包
			return;
		}

		// 操作金额
		double money = 0;
		if (tender != null) {
			money = tender.getAccount();
		}
		if (recharge != null) {
			money = recharge.getMoney();
		}

		// 红包金额
		double redPacketMoney = getRedPacketMoney(money, redPacket);
		// 红包到期时间
		Date expiredTime = null;
		// 如果红包有效天数大于0则设置到期时间，否则则代表长久有效
		if (redPacket.getDay() > 0) {
			expiredTime = DateUtil.rollDay(new Date(), redPacket.getDay());
		}
		UserRedPacket userRedPacket = new UserRedPacket(user, tender,
				redPacket, redPacketMoney, expiredTime);

		// 如果红包金额大于0，则保存红包
		if (userRedPacket.getAmount() > 0) {
			theUserRedPacketDao.save(userRedPacket);

			// 更新红包发放个数
			redPacket.setUseNum(redPacket.getUseNum() + 1);
			theRedPacketDao.update(redPacket);
		}
	}

	/**
	 * 计算红包金额
	 * 
	 * @param money
	 * @param redPacket
	 * @param rule
	 * @return
	 */
	public double getRedPacketMoney(double money, RedPacket redPacket) {
		double redPacketMoney = 0;
		// 红包最小金额
		double minMoney = redPacket.getMinMoney();
		// 红包最大金额
		double maxMoney = redPacket.getMaxMoney();

		// 发放方式
		switch (redPacket.getPaymentType()) {
		case 1:// 固定金额
			redPacketMoney = redPacket.getMoney();
			break;
		case 2:// 固定兑换比率
			redPacketMoney = BigDecimalUtil.round(money * redPacket.getRate()
					/ 100, 0);
			break;
		case 3:// 浮动方式
			List<RedPacketDetail> details = redPacketDetailDao
					.getList(redPacket);
			switch (redPacket.getFloatType()) {
			case 1:// 区间固定金额
				for (int i = 0; i < details.size(); i++) {
					RedPacketDetail detail = details.get(i);
					if (i == details.size() - 1) {// 最后一档不限
						if (detail.getMin() <= money) {
							redPacketMoney = detail.getMoney();
							break;
						}
					} else {
						if (detail.getMin() <= money && money < detail.getMax()) {
							redPacketMoney = detail.getMoney();
							break;
						}
					}
				}
				break;
			case 2:// 区间固定兑换比率
				for (int i = 0; i < details.size(); i++) {
					RedPacketDetail detail = details.get(i);
					if (i == details.size() - 1) {// 最后一档不限
						if (detail.getMin() <= money) {
							redPacketMoney = BigDecimalUtil.round(money
									* detail.getRate() / 100, 0);
							break;
						}
					} else {
						if (detail.getMin() <= money && money < detail.getMax()) {
							redPacketMoney = BigDecimalUtil.round(money
									* detail.getRate() / 100, 0);
							break;
						}
					}
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		if (minMoney > 0 && redPacketMoney < minMoney) {
			redPacketMoney = minMoney;
		}
		if (maxMoney > 0 && redPacketMoney > maxMoney) {
			redPacketMoney = maxMoney;
		}
		return redPacketMoney;
	}

	
}
