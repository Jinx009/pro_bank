package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScorePhoneLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScoreRealnameLog;
import com.rongdu.p2psys.tpp.domain.YjfPay;
import com.rongdu.p2psys.tpp.yjf.dao.YjfDao;
import com.rongdu.p2psys.tpp.yjf.model.ForwardConIdentify;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.dao.UserVipApplyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserVipApply;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserVipApplyModel;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;

/**
 * 认证信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月17日17:27:02
 */
@Service("userIdentifyService")
public class UserIdentifyServiceImpl implements UserIdentifyService {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(UserIdentifyServiceImpl.class);

	@Resource
	private UserIdentifyDao userIdentifyDao;
	@Autowired
	private UserVipApplyDao userVipApplyDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private UserCacheDao userCacheDao;
	@Autowired
	private YjfDao yjfDao;
	@Autowired
	private UserDao userDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserRedPacketService userRedPacketService;
	
	@Override
	public UserIdentify findByUserId(long userId) {
		return userIdentifyDao.findObjByProperty("user.userId", userId);
	}

	@Override
	public void modifyRealnameStatus(long userId, int status, int preStatus) {
		userIdentifyDao.modifyRealnameStatus(userId, status, preStatus);
	}

	@Override
	public void modifyMobilePhoneStatus(long userId, int status, int preStatus) {
		UserIdentify identify = findByUserId(userId);
		if (identify.getMobilePhoneStatus() != status) {
			userIdentifyDao.modifyMobilePhoneStatus(userId, status, preStatus);
			if(status == 1){ // 手机认证通过
				User user = userDao.find(userId);
				//若实名认证也通过，则赠送红包
				if (identify.getRealNameStatus() == 1) {
					userRedPacketService.doRedPacket(RedPacket.REALNAME, user, null, null);
				}
				BaseScoreLog bLog = new TenderScorePhoneLog(userId);
				bLog.doEvent();
			}
		}
	}

	@Override
	public UserIdentify getAttestationStatus(long userId) {
		return userIdentifyDao.findObjByProperty("user.userId", userId);
	}

	public UserIdentifyModel getUserIdentifyByUserId(long userId) {
		return userIdentifyDao.getUserIdentifyByUserId(userId);
	}

	@Override
	public void modifyEmailStatus(long userId, int status, int preStatus) {
		UserIdentify userAttestation = findByUserId(userId);
		if (userAttestation.getEmailStatus() != status) {
			userIdentifyDao.modifyEmailStatus(userId, status, preStatus);
		}

	}

	@Override
	public void userAttestationEdit(long id, String realNameVerifyRemark, int realNameStatus, Operator operator) {
		// 添加审核日志
		VerifyLog verifyLog = new VerifyLog(operator, "realName", id, 2, realNameStatus, realNameVerifyRemark);
		verifyLogDao.save(verifyLog);
		UserIdentify identify = userIdentifyDao.find(id);
		User user = identify.getUser();
		if (realNameStatus == 1) {
			//若手机认证也通过，则赠送红包
			if (identify.getMobilePhoneStatus() == 1) {
				userRedPacketService.doRedPacket(RedPacket.PHONE, user, null, null);
			}
			BaseScoreLog bLog = new TenderScoreRealnameLog(identify.getUser().getUserId());
			bLog.doEvent();
		} else {
			user.setCardId("");
		}
		userIdentifyDao.userAttestationEdit(id, realNameVerifyRemark, realNameStatus, operator);
	}

	@Override
	public int countByRealName(int status) {
		return userIdentifyDao.countByRealName(status);
	}
	
	@Override
	public int countByRealName(int status, String startTime, String endTime) {
		return userIdentifyDao.countByRealName(status, startTime, endTime);
	}

	/**
	 * 通过ID查询记录
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public UserIdentify findById(long id) {
		return userIdentifyDao.find(id);
	}

	/*
	 * @Override public PageDataList<UserIdentifyModel> list(int pageNumber, int pageSize,UserIdentifyModel model) {
	 * QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
	 * if(!StringUtil.isBlank(model.getUserName())){ param.addParam("user.userName", model.getUserName()); } if
	 * (StringUtil.isNotBlank(model.getStartTime())) { Date start = DateUtil.valueOf(model.getStartTime() +
	 * " 00:00:00"); param.addParam("addTime", Operators.GT, start); } if (StringUtil.isNotBlank(model.getEndTime())) {
	 * Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59"); param.addParam("addTime", Operators.LTE, end); }
	 * if(model.getStatus() <3){ //代表全部 param.addParam("status", Operators.EQ, model.getStatus()); }
	 * PageDataList<UserIdentify> pageList = userIdentifyDao.findPageList(param); PageDataList<UserIdentifyModel>
	 * pageDataList_ = new PageDataList<UserIdentifyModel>(); List<UserIdentifyModel> list_ = new
	 * ArrayList<UserIdentifyModel>(); pageDataList_.setPage(pageList.getPage()); List<UserIdentify> list =
	 * pageList.getList(); for (UserIdentify userIdentify : list) { UserIdentifyModel userIdentifyModel =
	 * UserIdentifyModel.instance(userIdentify); list_.add(userIdentifyModel); } pageDataList_.setList(list_); return
	 * pageDataList_; }
	 */
	public PageDataList<UserIdentify> list(int pageNumber, int pageSize, UserIdentify model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		param.addParam("vipStatus", model.getVipStatus());
		return userIdentifyDao.findPageList(param);

	}

	@Override
	public PageDataList<UserIdentifyModel> findUserIdentifylist(int pageNumber, int pageSize, UserIdentify model, String searchName) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (!StringUtil.isBlank(searchName)){//模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, searchName);
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, searchName);
    		param.addOrFilter(orFilter1,orFilter2);
		}else{//精确查询条件
			if (model != null) {
				if (model.getVipStatus() != -2) {
					param.addParam("vipStatus", model.getVipStatus());
				}
				if (model.getEmailStatus() != -2) {
					param.addParam("emailStatus", model.getEmailStatus());
				}
				if (model.getRealNameStatus() != -2) {
					param.addParam("realNameStatus", model.getRealNameStatus());
				}
				if (model.getMobilePhoneStatus() != -2) {
					param.addParam("mobilePhoneStatus", model.getMobilePhoneStatus());
				}
				if (model.getVideoStatus() != -2) {
					param.addParam("videoStatus", model.getVideoStatus());
				}
				if (model.getUser() != null && !StringUtil.isBlank(model.getUser().getUserName())) {
					param.addParam("user.userName", Operators.EQ, model.getUser().getUserName());
				}
				if (model.getUser() != null && model.getUser().getUserCache() != null 
						&& model.getUser().getUserCache().getUserType() != 0){
					param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
				}
			}
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<UserIdentify> pageDataList = userIdentifyDao.findPageList(param);
		PageDataList<UserIdentifyModel> pageDateList_ = new PageDataList<UserIdentifyModel>();
		List<UserIdentifyModel> list = new ArrayList<UserIdentifyModel>();
		pageDateList_.setPage(pageDataList.getPage());
		for (int i = 0; i < pageDataList.getList().size(); i++) {
			UserIdentify userIdentify = (UserIdentify) pageDataList.getList().get(i);
			UserIdentifyModel um = UserIdentifyModel.instance(userIdentify);
			try {
				String userName = userIdentify.getUser().getUserName();
				um.setUserName(userName);
				um.setRealName(userIdentify.getUser().getRealName());
				list.add(um);
			} catch (Exception e) {
			    um.setUser(null);
				um.setUserName("<font color='red'>该用户已被删除</font>");
				list.add(um);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	/**
	 * 查询用户申请vip
	 * 
	 * @param applyId
	 * @return
	 */
	@Override
	public UserVipApply getUserVipApplyById(long applyId) {
		return userVipApplyDao.find(applyId);
	}

	/**
	 * vip审核
	 * 
	 * @param user
	 * @param userVipApply
	 */
	@Override
	public void verifyVip(Operator operator, UserVipApply userVipApply) {
		User user = userVipApply.getUser(); // user 申请vip的用户

		Account account = accountDao.getAccountByUserId(user.getUserId());
		double vipMoney = userVipApply.getMoney();
		if (vipMoney > account.getUseMoney()) {
			throw new BussinessException("余额不足，请您充值", 1);
		}
		// 处理业务
		AbstractExecuter executer = ExecuterHelper.doExecuter("verifyVipExecuter");
		Global.setTransfer("userVipApply", userVipApply);
		executer.execute(vipMoney, user);

		// 修改本地状态
		userVipApplyDao.update(userVipApply);

		UserIdentify userIdentify = userIdentifyDao.findByUserId(user.getUserId());
		if (userVipApply.getStatus() == 1) {
			userIdentify.setVipStatus(1);
		} else {
			userIdentify.setVipStatus(-1);
		}
		Date verifyTime = new Date();
		Date endTime = DateUtil.rollYear(verifyTime, 1);

		userIdentify.setVipVerifyTime(verifyTime);
		userIdentify.setVipEndTime(endTime);
		userIdentifyDao.update(userIdentify);

	}

	@Override
	public PageDataList<UserModel> userModelList(int pageNumber, int pageSize, UserIdentify model,String searchName) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (!StringUtil.isBlank(searchName)){//模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, searchName);
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, searchName);
    		SearchFilter orFilter3 = new SearchFilter("user.cardId", Operators.LIKE, searchName);
    		param.addOrFilter(orFilter1,orFilter2, orFilter3);
		}else{//精确查询条件
			if (model != null) {
				if (model.getRealNameStatus() != -2) {
					param.addParam("realNameStatus", model.getRealNameStatus());
				}
			} else {
				param.addParam("realNameStatus", 2);
			}
			if (model != null && model.getUser() != null && !StringUtil.isBlank(model.getUser().getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUser().getUserName());
			}
			if (model != null && model.getUser() != null && !StringUtil.isBlank(model.getUser().getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getUser().getRealName());
			}
			if (model != null && model.getUser() != null && !StringUtil.isBlank(model.getUser().getCardId())) {
				param.addParam("user.cardId", Operators.EQ, model.getUser().getCardId());
			}
		}
		param.addOrder(OrderType.DESC, "realNameVerifyTime");
		PageDataList<UserIdentify> pageDataList = userIdentifyDao.findPageList(param);
		PageDataList<UserModel> pageDataList_ = new PageDataList<UserModel>();
		List<UserModel> list = new ArrayList<UserModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0;i < pageDataList.getList().size();i++) {
				UserIdentify userIdentify = (UserIdentify) pageDataList.getList().get(i);
				UserModel userModel = null;
				try {
					User user = userIdentify.getUser();
					if (user == null) {
						userModel = new UserModel();
						userModel.setUserCache(null);
						userModel.setUserIdentify(userIdentify);
						userModel.setUserName("<font color='red'>该用户已被删除</font>");
					} else {
						userModel = UserModel.instance(user);
						userModel.setUserCache(userCacheDao.findByUserId(user.getUserId()));
						userModel.setUserIdentify(userIdentify);
					}
					list.add(userModel);
				} catch ( Exception e ) {
					userModel = new UserModel();
					userIdentify.setUser(null);
					userModel.setUserName("<font color='red'>该用户已被删除</font>");
					list.add(userModel);
				}
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
		
	}

	/**
	 * vip申请列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param userAttestationModel
	 * @return
	 */
	@Override
	public PageDataList<UserVipApplyModel> vipApplyList(int pageNumber, int pageSize, UserVipApplyModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}
		if (!StringUtil.isBlank(model.getUserName())) {
			param.addParam("user.userName", Operators.EQ, model.getUserName());
		}
		if (StringUtil.isNotBlank(model.getStartTime())) {
			Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
			param.addParam("addTime", Operators.GTE, start);
		}
		if (StringUtil.isNotBlank(model.getEndTime())) {
			Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
			param.addParam("addTime", Operators.LTE, end);
		}
		if (model.getStatus() < 3) {
			param.addParam("status", model.getStatus());
		}
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<UserVipApply> pageDataList = userVipApplyDao.findPageList(param);
		PageDataList<UserVipApplyModel> pageDataList_ = new PageDataList<UserVipApplyModel>();
		List<UserVipApplyModel> list = new ArrayList<UserVipApplyModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				UserVipApply user = (UserVipApply) pageDataList.getList().get(i);
				user.getUser().getUserId();
				UserVipApplyModel userModel = UserVipApplyModel.instance(user);
				list.add(userModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}
	
	@Override
	public boolean updateRealNameStatusByCallBack(ForwardConIdentify fci) {
		YjfPay yp = yjfDao.findByOrderNo(fci.getOrderNo());
		if (yp != null) {
			long userId = StringUtil.toLong(yp.getUserid());
			User user=userDao.find(userId);
			UserIdentify userIdentify = userIdentifyDao.findByUserId(userId);
			if ("EXECUTE_SUCCESS".equals(fci.getResultCode())) {
				if (userIdentify != null && userIdentify.getRealNameStatus() == 2) {
					userIdentifyDao.modifyRealnameStatus(userId, 1, 2);
				} else if (userIdentify != null && userIdentify.getRealNameStatus() == 0) {
					userIdentifyDao.modifyRealnameStatus(userId, 1, 0);
				} else {
					userIdentifyDao.modifyRealnameStatus(userId, 1, 2);
				}
				String apiUserCustId=Global.getValue("webid") + user.getUserName();
				userCacheDao.modify(userId, fci.getUserId(), "1", apiUserCustId);
				userCacheDao.modify(userId, fci.getCertNo());
				userDao.modifyRealname(userId, fci.getRealName());
				
			} else {
				if (userIdentify != null && userIdentify.getRealNameStatus() == 2) {
					userIdentifyDao.modifyRealnameStatus(userId, -1, 2);
				} else if (userIdentify != null && userIdentify.getRealNameStatus() == 0) {
					userIdentifyDao.modifyRealnameStatus(userId, -1, 0);
				} 
			}
		}
		return true;
	}

	@Override
	public void update(UserIdentify identify) {
		userIdentifyDao.update(identify);
	}
}
