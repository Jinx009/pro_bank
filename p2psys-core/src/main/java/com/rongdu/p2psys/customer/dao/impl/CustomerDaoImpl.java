package com.rongdu.p2psys.customer.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.Page;
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
@Service("customerDao")
public class CustomerDaoImpl  extends BaseDaoImpl<User> implements CustomerDao {
	
	@Override
	public PageDataList<CustomerProductModel> customerProductList(int pageNumber,
			int pageSize,CustomerProductModel model) {
		// TODO Auto-generated method stub
		
		StringBuffer borrowSql =  new StringBuffer();
					 borrowSql.append(" select w.id,w.name,u.real_name,h.sex,u.mobile_phone,b.name as product_name, " )
					 		  .append(" b.id as product_id,b.review_time,date(r.repayment_time) repayment_time,c.interest " )
					          .append(" from rd_borrow_collection c left join rd_borrow b on c.borrow_id = b.id " )
					          .append(" LEFT JOIN ehb_wealth_user wu ON c.user_id = wu.user_id " )
					          .append(" LEFT JOIN ehb_zc_wealth_manager_user m ON wu.id = m.wealth_user_id left join " )
					          .append(" ehb_zc_wealth_manager w on m.wealth_manager_id = w.id join rd_user u on c.user_id = u.user_id left join " )
					          .append(" rd_user_cache h on c.user_id = h.user_id left join rd_borrow_repayment r on c.borrow_id = r.borrow_id where 1=1 ");
		if (!StringUtil.isBlank(model.getSearchName())) {
			borrowSql.append(" and (u.mobile_phone like '%").append(model.getSearchName()).append("%' ")
		             .append(" or u.real_name like '%").append(model.getSearchName()).append("%') ");
		} 
		StringBuffer ppfundSql =  new StringBuffer();
					 ppfundSql.append(" select w.id,w.name,u.real_name,h.sex,u.mobile_phone,b.name as product_name, ")
							  .append(" b.id as product_id,c.add_time,date(date_add(c.add_time, interval b.time_limit day)) end_time,c.interest ")
					          .append(" from rd_ppfund_in c left join rd_ppfund b on c.ppfund_id = b.id ")
				              .append(" LEFT JOIN ehb_wealth_user wu ON c.user_id = wu.user_id ")
							  .append(" LEFT JOIN ehb_zc_wealth_manager_user m ON wu.id = m.wealth_user_id left join ")   
							  .append(" ehb_zc_wealth_manager w on m.wealth_manager_id = w.id join rd_user u on c.user_id = u.user_id left join ")
							  .append(" rd_user_cache h on c.user_id = h.user_id where 1=1 ");
		if (!StringUtil.isBlank(model.getSearchName())) {
			ppfundSql.append(" and (u.mobile_phone like '%").append(model.getSearchName()).append("%' ")
	                 .append(" or u.real_name like '%").append(model.getSearchName()).append("%') ");
		} 
		
		String strSql = " select * from ( " + borrowSql + " union all " + ppfundSql + " ) a ORDER BY a.id,a.product_id ";

		Query query = em.createNativeQuery(strSql);
		Page page = new Page(query.getResultList().size(), pageNumber,pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		PageDataList<CustomerProductModel> pageDataList_ = new PageDataList<CustomerProductModel>();
        List<CustomerProductModel> cpmList = new ArrayList<CustomerProductModel>();
        pageDataList_.setPage(page);
        int i = 1;
    	for (Object[] o : list) {
    		CustomerProductModel customer = new CustomerProductModel();
			customer.setId(i);
			customer.setSaleCode(o[0]==null?"":o[0].toString());
			customer.setSaleName(o[1]==null?"":o[1].toString());
			customer.setRealName(o[2]==null?"":o[2].toString());
			customer.setSex(o[3]==null?0:Integer.parseInt(o[3].toString()));
			customer.setPhone(o[4]==null?"":o[4].toString());
			customer.setProductName(o[5]==null?"":o[5].toString());
			customer.setProductCode(o[6]==null?"":o[6].toString());
			customer.setStartDate(o[7]==null?new Date():(Date)o[7]);
			customer.setEndDate(o[8]==null?null:(Date)o[8]);
			customer.setProfit(o[9]==null?0:((BigDecimal)o[9]).doubleValue());
			cpmList.add(customer);
			i++;
    	}
		pageDataList_.setList(cpmList);
		return pageDataList_;
	}
	
	@Override
	public PageDataList<CustomerBaseinfoModel> customerBaseinfoList(int pageNumber,int pageSize,CustomerBaseinfoModel model) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct w.id,w.name,u.real_name,c.sex,u.mobile_phone, ")
		  .append(" (DATE_FORMAT(CURDATE(),'%Y') - SUBSTR(u.card_id,7,4)) age,  ")
		  .append(" u.card_id, ab.bank_no,ab.bank,u.add_time,i.real_name_verify_time as real_name_time,i.real_name_verify_time as bind_card_time, ")
		  .append(" c.status,p.coupon_code,a.use_money,u.user_id from  rd_user u  ")
		  .append(" LEFT JOIN ehb_wealth_user wu ON u.user_id = wu.user_id  ")
		  .append(" LEFT JOIN ehb_zc_wealth_manager_user m ON wu.id = m.wealth_user_id ")		
		  .append(" left join ehb_zc_wealth_manager w on m.wealth_manager_id = w.id  " )
		  .append(" left join rd_user_identify i on u.user_id = i.user_id ")
		  .append(" left join rd_user_base_info b on u.user_id = b.user_id " )
		  .append(" left join rd_user_cache c on u.user_id = c.user_id " )
		  .append(" left join rd_account_bank  ab on u.user_id = ab.user_id  ")
		  .append(" left join rd_user_invite ui on u.user_id = ui.user_id ")
		  .append(" left join rd_user_promot p on ui.invite_user = p.user_id ")
		  .append(" left join rd_account a on u.user_id = a.user_id where ab.status = 1 ");
		String strSql =  sb.toString();
	    if (!StringUtil.isBlank(model.getSearchName())) {
	    	strSql +=  " and (u.mobile_phone like '%" + model.getSearchName() + "%' " + 
             	   " or u.real_name like '%" + model.getSearchName() + "%') ";
		} 
		Query query = em.createNativeQuery(strSql);
		Page page = new Page(query.getResultList().size(), pageNumber,pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		PageDataList<CustomerBaseinfoModel> pageDataList_ = new PageDataList<CustomerBaseinfoModel>();
        List<CustomerBaseinfoModel> cpmList = new ArrayList<CustomerBaseinfoModel>();
        pageDataList_.setPage(page);
        int i = 1;
    	for (Object[] o : list) {
    		CustomerBaseinfoModel customer = new CustomerBaseinfoModel();
			customer.setId(i);
			customer.setSaleCode(o[0]==null?"":o[0].toString());
			customer.setSaleName(o[1]==null?"":o[1].toString());
			customer.setRealName(o[2]==null?"":o[2].toString());
			customer.setSex(o[3]==null?0:((Boolean)o[3]?1:0));
			customer.setPhone(o[4]==null?"":o[4].toString());
			customer.setAge(o[5]==null?0:((Double)o[5]).intValue());
			customer.setCertificate(o[6]==null?"":o[6].toString());
			customer.setBankNo(o[7]==null?"":o[7].toString());
			customer.setBank(o[8]==null?"":o[8].toString());
			customer.setRegisterTime(o[9]==null?new Date():(Date)o[9]);
			customer.setRealNameTime(o[10]==null?new Date():(Date)o[10]);
			customer.setBindCardTime(o[11]==null?new Date():(Date)o[11]);
			customer.setAccountState(o[12]==null?0:((Boolean)o[12]?1:0));
			customer.setRecommendCode(o[13]==null?"":o[13].toString());
			customer.setUseMoney(o[14]==null?0:Double.parseDouble(o[14].toString()));
			strSql = " select sum(account) from rd_borrow_tender where user_id = " + (o[15]==null?0:Integer.parseInt(o[15].toString()));
			query = em.createNativeQuery(strSql);
			if(query.getResultList()!=null&&query.getResultList().size()>0&&query.getResultList().get(0)!=null)
			{
				String money = query.getResultList().get(0).toString();
				customer.setInvestMoney(Double.parseDouble(money));
			}else
			{
				customer.setInvestMoney(0.0);
			}
			
			cpmList.add(customer);
			i++;
    	}
		pageDataList_.setList(cpmList);
		return pageDataList_;
	}

	
	@Override
	public PageDataList<AccountLogModel> accountLogList(int pageNumber,
			int pageSize,AccountLogModel model) {
		// TODO Auto-generated method stub
		String  strSql = " select u.real_name,b.sex,r.total,r.use_money,r.no_use_money,r.collection,r.type,r.money, " +
				" r.add_time,r.remark from rd_account_log r join rd_user u on u.user_id = r.user_id " +
				" left join rd_user_cache b on u.user_id = b.user_id where 1=1";
		if (!StringUtil.isBlank(model.getSearchName())) {
		    	strSql +=  " and (u.mobile_phone like '%" + model.getSearchName() + "%' " + 
	             	   " or u.real_name like '%" + model.getSearchName() + "%') ";
		} 
		Query query = em.createNativeQuery(strSql);
		Page page = new Page(query.getResultList().size(), pageNumber,pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		PageDataList<AccountLogModel> pageDataList_ = new PageDataList<AccountLogModel>();
        List<AccountLogModel> cpmList = new ArrayList<AccountLogModel>();
        pageDataList_.setPage(page);
        int i = 1;
    	for (Object[] o : list) {
    		AccountLogModel log = new AccountLogModel();
			log.setId(i);
			log.setCustomerName(o[0]==null?"":o[0].toString());
			log.setSex(o[1]==null?0:o[1]==null?0:((Boolean)o[1]?1:0));
			log.setAccount(o[2]==null?0:Double.parseDouble(o[2].toString()));
			log.setUseMoney(o[3]==null?0:Double.parseDouble(o[3].toString()));
			log.setFreezeMoney(o[4]==null?0:Double.parseDouble(o[4].toString()));
			log.setCollection(o[5]==null?0:Double.parseDouble(o[5].toString()));
			log.setCashUseType(o[6]==null?"":o[6].toString());
			log.setOperMoney(o[7]==null?0:Double.parseDouble(o[7].toString()));
			log.setOperTime(o[8]==null?new Date():(Date)o[8]);
			log.setRemark(o[9]==null?"":o[9].toString());
			cpmList.add(log);
			i++;
    	}
		pageDataList_.setList(cpmList);
		return pageDataList_;
	}
	
	
	@Override
	public PageDataList<ReferrerModel> referrerList(int pageNumber,int pageSize,ReferrerModel model) {
		// TODO Auto-generated method stub
		String  strSql = " select w.id,w.name,u.real_name,u.mobile_phone,p.coupon_code, " +
				" i.real_name as invite_user,i.mobile_phone invite_mobile_phone,p.used_times from rd_user_invite ui  " +
				" join rd_user u on ui.user_id = u.user_id " +
				" join rd_user i on ui.invite_user = i.user_id " +
				" LEFT JOIN ehb_wealth_user wu ON ui.user_id = wu.user_id " + 
				" LEFT JOIN ehb_zc_wealth_manager_user m ON wu.id = m.wealth_user_id " +
				" left join ehb_zc_wealth_manager w on m.wealth_manager_id = w.id  " + 
				" left join rd_user_promot p on ui.invite_user = p.user_id  where 1=1 ";
		if (!StringUtil.isBlank(model.getSearchName())) {
	    	strSql +=  " and (u.mobile_phone like '%" + model.getSearchName() + "%' " + 
             	   " or u.real_name like '%" + model.getSearchName() + "%') ";
	    } 
		Query query = em.createNativeQuery(strSql);
		Page page = new Page(query.getResultList().size(), pageNumber,pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		PageDataList<ReferrerModel> pageDataList_ = new PageDataList<ReferrerModel>();
        List<ReferrerModel> cpmList = new ArrayList<ReferrerModel>();
        pageDataList_.setPage(page);
        int i = 1;
    	for (Object[] o : list) {
    		ReferrerModel referrer = new ReferrerModel();
    		referrer.setId(i);
    		referrer.setSaleCode(o[0]==null?"":o[0].toString());
    		referrer.setSaleName(o[1]==null?"":o[1].toString());
    		referrer.setRealName(o[2]==null?"":o[2].toString());
    		referrer.setPhone(o[3]==null?"":o[3].toString());
    		referrer.setRecommendCode(o[4]==null?"":o[4].toString());
    		referrer.setReferrer(o[5]==null?"":o[5].toString());
    		referrer.setReferrerPhone(o[6]==null?"":o[6].toString());
    		referrer.setCodeUsedTimes(o[7]==null?0:Integer.parseInt(o[7].toString()));
    		cpmList.add(referrer);
			i++;
    	}
		pageDataList_.setList(cpmList);
		return pageDataList_;
	}
	
	
	@Override
	public PageDataList<CustomerRedPacketModel> customerRedPacketList(int pageNumber,int pageSize,CustomerRedPacketModel model) {
		// TODO Auto-generated method stub
		String  strSql = " select u.real_name,u.mobile_phone, s.name,r.amount,r.add_time, " +
				" r.expired_time,r.is_used from rd_user_red_packet r " +
				" join s_red_packet s on r.red_packet_type = s.id " +
				" join rd_user u on r.user_id = u.user_id ";
		if (!StringUtil.isBlank(model.getSearchName())) {
	    	strSql +=  " and (u.mobile_phone like '%" + model.getSearchName() + "%' " + 
             	   " or u.real_name like '%" + model.getSearchName() + "%') ";
	    } 
		Query query = em.createNativeQuery(strSql);
		Page page = new Page(query.getResultList().size(), pageNumber,pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		PageDataList<CustomerRedPacketModel> pageDataList_ = new PageDataList<CustomerRedPacketModel>();
        List<CustomerRedPacketModel> cpmList = new ArrayList<CustomerRedPacketModel>();
        pageDataList_.setPage(page);
        int i = 1;
    	for (Object[] o : list) {
    		CustomerRedPacketModel customer = new CustomerRedPacketModel();
			customer.setId(i);
			customer.setCustomerName(o[0]==null?"":o[0].toString());
			customer.setCustomerPhone(o[1]==null?"":o[1].toString());
			customer.setName(o[2]==null?"":o[2].toString());
			customer.setMoney(o[3]==null?0:Double.parseDouble(o[3].toString()));
			customer.setStartTime(o[4]==null?new Date():(Date)o[4]);
			customer.setDueTime(o[5]==null?new Date():(Date)o[5]);
			customer.setIsExchanged(o[6]==null?0:((Boolean)o[6]?1:0));
    		cpmList.add(customer);
			i++;
    	}
		pageDataList_.setList(cpmList);
		return pageDataList_;
	}
	
	

}
