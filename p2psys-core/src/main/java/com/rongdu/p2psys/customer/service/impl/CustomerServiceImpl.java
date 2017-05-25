package com.rongdu.p2psys.customer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.dao.RedPacketDao;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.customer.dao.CustomerDao;
import com.rongdu.p2psys.customer.model.AccountLogModel;
import com.rongdu.p2psys.customer.model.CustomerBaseinfoModel;
import com.rongdu.p2psys.customer.model.CustomerProductModel;
import com.rongdu.p2psys.customer.model.CustomerRedPacketModel;
import com.rongdu.p2psys.customer.model.ReferrerModel;
import com.rongdu.p2psys.customer.service.CustomerService;
import com.rongdu.p2psys.ppfund.dao.PpfundDao;
import com.rongdu.p2psys.ppfund.dao.PpfundUploadDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * PPfund（资金管理产品）
 * 
 * @author yeshenghong
 * @version 2.0
 * @Date 2015年3月16日
 */
@Service("cstomerService")
public class CustomerServiceImpl implements CustomerService {
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private AccountDao accountDao;
	@Resource
	private BorrowTenderDao tenderDao;
	@Resource
	private AccountBankDao aBankDao;
	@Resource
	private UserIdentifyDao identifyDao;
	@Resource
	private UserPromotDao promotDao;
	@Resource
	private UserInviteDao  userInviteDao;
	@Resource
	private AccountLogDao accountLogDao;
	@Resource
	private UserRedPacketDao  userRedPacketDao;
	@Resource
	private RedPacketDao  redPacketDao;
	
	@Resource
	private CustomerDao customerDao;
	
	@Override
	public PageDataList<CustomerProductModel> customerProductList(int pageNumber,
			int pageSize,CustomerProductModel model) {
		// TODO Auto-generated method stub
		return customerDao.customerProductList(pageNumber, pageSize, model);
	}
	
	@Override
	public PageDataList<AccountLogModel> accountLogList(int pageNumber,
			int pageSize,AccountLogModel model) {
		// TODO Auto-generated method stub
		return customerDao.accountLogList(pageNumber, pageSize, model);
	}

	@Override
	public PageDataList<CustomerBaseinfoModel> customerBaseinfoList(int pageNumber,
			int pageSize,CustomerBaseinfoModel model) {
		// TODO Auto-generated method stub
		return customerDao.customerBaseinfoList(pageNumber, pageSize, model);
	}

	



	@Override
	public PageDataList<ReferrerModel> referrerList(int pageNumber,
			int pageSize,ReferrerModel model) {
		// TODO Auto-generated method stub
		return customerDao.referrerList(pageNumber, pageSize, model);
	}

	@Override
	public PageDataList<CustomerRedPacketModel> customerRedPacketList(int pageNumber,
			int pageSize,CustomerRedPacketModel model) {
		// TODO Auto-generated method stub
		return customerDao.customerRedPacketList(pageNumber, pageSize, model);
	}
	

}
