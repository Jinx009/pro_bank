package com.rongdu.p2psys.account.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.user.model.UserModel;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDaoImpl<Account> implements AccountDao {

	@Override
	public PageDataList<AccountModel> list(AccountModel model) {
		QueryParam param = QueryParam.getInstance();
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{
			if (StringUtil.isNotBlank(model.getUsername())) {
				param.addParam("user.userName", Operators.EQ, model.getUsername());
			}
			if (StringUtil.isNotBlank(model.getRealname())) {
				param.addParam("user.realName", Operators.EQ, model.getRealname());
			}
			if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		param.addPage(model.getPage(), model.getRows());
		if (model.getOrder().equals("desc")) {
			param.addOrder(OrderType.DESC, model.getSort());
		} else {
			param.addOrder(OrderType.ASC, model.getSort());
		}
		PageDataList<Account> pageDateList = findPageList(param);
		PageDataList<AccountModel> pageDateList_ = new PageDataList<AccountModel>();
		List<AccountModel> list = new ArrayList<AccountModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				Account account = (Account) pageDateList.getList().get(i);
				AccountModel am = AccountModel.instance(account);
				try{
    				am.setUsername(account.getUser().getUserName());
    				am.setRealname(account.getUser().getRealName());
    				list.add(am);
				}catch(Exception e){
				    e.printStackTrace();
				}
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}
	
	@Override
	public PageDataList<AccountModel> userList(AccountModel model) {
		String strSql = " select l.id,u.user_id userId,u.user_name,u.real_name ,"
						+"l.total,l.use_money,l.no_use_money,"
						+"l.collection,l.add_time"
						+" from rd_account_log l,"
						+" (select user_id,max(add_time) max_time from rd_account_log l GROUP BY l.user_id ) b,"
						+" rd_user u "
						+" where l.user_id=b.user_id"
						+" and u.user_id=b.user_id"
						+" and l.add_time=b.max_time";
		String strCountSql=" select count(*) "
				+"from rd_account_log l,"
				+"(select user_id,max(add_time) max_time from rd_account_log l GROUP BY l.user_id ) b,"
				+"rd_user u "
				+"where l.user_id=b.user_id"
				+" and u.user_id=b.user_id"
				+" and l.add_time=b.max_time";
		if(StringUtils.isNotEmpty(model.getSearchName())){
			strCountSql=strCountSql+" and date_format(l.add_time,'%Y-%m')='"+model.getSearchName()+"'";
			strSql=strSql+" and date_format(l.add_time,'%Y-%m')='"+model.getSearchName()+"'";
		}
            strSql=strSql+" GROUP BY u.user_id";
            strSql=strSql+" ORDER BY b.user_id";
            strSql=strSql+" limit "+model.getPage()+" , "+ model.getRows();
            Query query = em.createNativeQuery(strSql.toString());
            Query queryCount=em.createNativeQuery(strCountSql.toString());
            int total=Integer.parseInt(queryCount.getSingleResult().toString());
    		List<AccountModel> list = new ArrayList<AccountModel>();
    		List rows = query.getResultList();
    		for (Object row : rows) {  
    	        Object[] cells = (Object[]) row; 
    	        AccountModel accountModel=new AccountModel();
    	        accountModel.setId(Long.parseLong(cells[0].toString()));
    	        accountModel.setUsername(cells[2]==null?"":cells[2].toString());
    	        accountModel.setRealname(cells[3]==null?"":cells[3].toString());
    	        accountModel.setTotal(Double.parseDouble(cells[4]==null?"0":cells[4].toString()));
    	        accountModel.setUseMoney(Double.parseDouble(cells[5]==null?"0":cells[5].toString()));
    	        accountModel.setNoUseMoney(Double.parseDouble(cells[6]==null?"0":cells[6].toString()));
    	        accountModel.setCollection(Double.parseDouble(cells[7]==null?"0":cells[7].toString()));
    	        list.add(accountModel);
    	    } 
    		
    		PageDataList<AccountModel> pageDateList_ = new PageDataList<AccountModel>();
    		Page page=new Page(total,model.getPage(),model.getRows());
    		pageDateList_.setPage(page);
    		pageDateList_.setList(list);
    		return pageDateList_;
	}
	
	@Override
	public PageDataList<AccountModel> exportUserList(AccountModel model) {
		String strSql = " select l.id,u.user_id userId,u.user_name,u.real_name ,"
						+"l.total,l.use_money,l.no_use_money,"
						+"l.collection,l.add_time"
						+" from rd_account_log l,"
						+" (select user_id,max(add_time) max_time from rd_account_log l GROUP BY l.user_id ) b,"
						+" rd_user u "
						+" where l.user_id=b.user_id"
						+" and u.user_id=b.user_id"
						+" and l.add_time=b.max_time";
		
		if(StringUtils.isNotEmpty(model.getSearchName())){
			strSql=strSql+" and date_format(l.add_time,'%Y-%m')='"+model.getSearchName()+"'";
		}
            strSql=strSql+" GROUP BY u.user_id";
            strSql=strSql+" ORDER BY b.user_id";
            strSql=strSql+" limit "+model.getPage()+" , "+ model.getRows();
            Query query = em.createNativeQuery(strSql.toString());
    		List<AccountModel> list = new ArrayList<AccountModel>();
    		List rows = query.getResultList();
    		for (Object row : rows) {  
    	        Object[] cells = (Object[]) row; 
    	        AccountModel accountModel=new AccountModel();
    	        accountModel.setId(Long.parseLong(cells[0].toString()));
    	        accountModel.setUsername(cells[2]==null?"":cells[2].toString());
    	        accountModel.setRealname(cells[3]==null?"":cells[3].toString());
    	        accountModel.setTotal(Double.parseDouble(cells[4]==null?"0":cells[4].toString()));
    	        accountModel.setUseMoney(Double.parseDouble(cells[5]==null?"0":cells[5].toString()));
    	        accountModel.setNoUseMoney(Double.parseDouble(cells[6]==null?"0":cells[6].toString()));
    	        accountModel.setCollection(Double.parseDouble(cells[7]==null?"0":cells[7].toString()));
    	        list.add(accountModel);
    	    } 
    		
    		PageDataList<AccountModel> pageDateList_ = new PageDataList<AccountModel>();
    		pageDateList_.setPage(null);
    		pageDateList_.setList(list);
    		return pageDateList_;
	}

	@Override
	public Account getAccountByUserId(long userId) {
		return this.findObjByProperty("user.userId", userId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Account> getGroupAccountListByUserId(long userId) {
		String strSql = " select * from rd_account where user_id in (select u1.user_id from rd_user u1 join rd_user u2 " +
						" on u1.bind_id = u2.bind_id where u2.user_id = " + userId + ") order by use_money desc ";
		Query query =  em.createNativeQuery(strSql,Account.class);
		return query.getResultList();
	}


	@Override
	public void modify(Account account) {
		
	       // 账户资金CHECK
	       checkAccount(account);
		
	       // 更新
	       update(account);
	}

	@Override
	public void modify(double totalVar, double useVar, double nouseVar, long userId) {
	   Account account = getAccountByUserId(userId);
	   if(account.getUseMoney() >= nouseVar)//主账户余额充足
	   {
		   modifyAccount(totalVar, useVar, nouseVar, account);	   
	   }
	   else 
	   {		
		   //先扣主账户
		   modifyAccount(totalVar, -account.getUseMoney(), account.getUseMoney(), account);
		   modifyAccountList(totalVar, account.getUseMoney()+useVar, nouseVar-account.getUseMoney(), userId);
	   }
	}
	//多账户扣款
	public void modifyAccountList(double totalVar, double useVar, double nouseVar, long userId)
	{
		List<Account> list = findListAccount(userId); 
		double balance  = nouseVar;
		for(Account account : list)
		{			
			if(balance - account.getUseMoney() > 0)
			{
				modifyAccount(totalVar, -account.getUseMoney(), account.getUseMoney(), account);
				balance = balance - account.getUseMoney();
			}
			else
			{
				modifyAccount(totalVar, -balance, balance, account);
				return;
			}
		}
		
	}
	//主账户扣款
	public void modifyAccount(double totalVar, double useVar, double nouseVar, Account account) {
		   // 总金额
		   account.setTotal(BigDecimalUtil.add(account.getTotal(), totalVar));
		   // 可用金额
		   account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(), useVar));
		   // 冻结金额
		   account.setNoUseMoney(BigDecimalUtil.add(account.getNoUseMoney(), nouseVar));
		   
	       // 账户资金CHECK
	       checkAccount(account);
			
		   // 更新
		   update(account);
		}

	@Override
	public void modify(double totalVar, double useVar, double nouseVar, double collectVar, long userId) {
	   
       Account account = getAccountByUserId(userId);
       // 总金额
       account.setTotal(BigDecimalUtil.add(account.getTotal(), totalVar));
       // 可用金额
       account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(), useVar));
       // 冻结金额
       account.setNoUseMoney(BigDecimalUtil.add(account.getNoUseMoney(), nouseVar));
       // 待收金额
       account.setCollection(BigDecimalUtil.add(account.getCollection(), collectVar));
       
       // 账户资金CHECK
       checkAccount(account);
		
       // 更新
       update(account);
	}
	
	
	@Override
	public void modify(double totalVar, double useVar, double nouseVar, double collectVar,double repay,long userId) {
		
       Account account = getAccountByUserId(userId);
       // 总金额
       account.setTotal(BigDecimalUtil.add(account.getTotal(), totalVar));
       // 可用金额
       account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(), useVar));
       // 冻结金额
       account.setNoUseMoney(BigDecimalUtil.add(account.getNoUseMoney(), nouseVar));
       // 待收金额
       account.setCollection(BigDecimalUtil.add(account.getCollection(), collectVar));
       
       // 账户资金CHECK
       checkAccount(account);
		
       // 更新
       update(account);
	}

	@Override
	public double getAccountUseMoney(long userId) {
		
		String strSql = " select SUM(use_money) from rd_account where user_id in (select u1.user_id from rd_user u1 join rd_user u2 " +
						" on u1.bind_id = u2.bind_id where u2.user_id = " + userId + ") ";
	    Query  query = em.createNativeQuery(strSql);
	    Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}
	
	@Override
	public double getAllUseMoney() {
        String sql = "SELECT SUM(t1.use_money) FROM rd_account t1, rd_user_cache t2 WHERE t1.user_id = t2.user_id AND t2.user_type = 1";
        Query query = em.createNativeQuery(sql);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public void update(double totalVar, double useVar, double nouseVar,
			double collectVar, long userId) {
		
		int count = updateByJpql(
				"UPDATE Account SET total=total+:total,useMoney=useMoney+:useMoney,noUseMoney=noUseMoney+:noUseMoney,collection=collection+:collect WHERE user.userId=:userId AND round(useMoney+:money)>=0",
				new String[] { "total", "useMoney", "noUseMoney","collect", "userId", "money" }, new Object[] { totalVar, useVar,
						nouseVar,collectVar,userId, useVar });
		if (count != 1) {
			throw new AccountException("投资人资金有误！", 1);
		}
		
		Account account = getAccountByUserId(userId);
		
		// 账户资金CHECK
		checkAccount(account);
		
		//更新缓存
		em.refresh(account);
	}	
	
	/*
	 * 账户资金不能为负数
	 */
	public void checkAccount(Account account) {
		if(account != null){
			if(account.getTotal() < 0 ){
				throw new AccountException("账户总额有误！", 1);
			}
			if(account.getUseMoney() < 0 ){
				throw new AccountException("账户可用余额有误！", 1);
			}
			if(account.getNoUseMoney() < 0 ){
				throw new AccountException("账户冻结金额有误！", 1);
			}
			if(account.getCollection() < 0 ){
				throw new AccountException("账户待收总额有误！", 1);
			}
		}
	}

	@Override
	public double getAllTotal() {
		String sql = "SELECT SUM(t1.total) FROM rd_account t1, rd_user_cache t2 WHERE t1.user_id = t2.user_id AND t2.user_type = 1";
        Query query = em.createNativeQuery(sql);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public Object[] financialStatistics() {
		String jpql = "select sum(total),sum(useMoney),sum(noUseMoney),sum(collection) from Account";
		Query query = em.createQuery(jpql);
		Object[] object = (Object[]) query.getSingleResult();
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> findListAccount(double bind_id){
		//String jpql = "select * from (select a.*,u.bind_id from rd_account a LEFT JOIN rd_user u on (a.user_id = u.user_id)) s where s.bind_id = "+bind_id+" ORDER BY s.use_money DESC";
		String jpql = "select * from rd_account where user_id in (select u1.user_id from rd_user u1 join rd_user u2 on u1.bind_id = u2.bind_id where u2.user_id = "+bind_id+")  order by use_money DESC";
		Query query = em.createNativeQuery(jpql,Account.class);		
		return query.getResultList();
	}
	public double getSumAccount(long bind_id) {
		String sql = "select sum(use_money) from rd_account where user_id in (select u1.user_id from rd_user u1 join rd_user u2 on u1.bind_id = u2.bind_id where u2.user_id = "+bind_id+")  order by use_money DESC";
        Query query = em.createNativeQuery(sql);
        Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}

	@Override
	public PageDataList<AccountModel> exportList(AccountModel model) {
		
		String sql="select a.id,u.user_name,u.real_name,a.total,a.use_money,a.no_use_money,a.collection from rd_user u,rd_user_cache uc,rd_account a"+ 
                    " where 1=1 ";
		
//		QueryParam param = QueryParam.getInstance();
		if(!StringUtil.isBlank(model.getSearchName())){
			sql=sql+" and (u.user_name like '%"+model.getSearchName()+"%' or u.real_name like '%"+model.getSearchName()+"%')";
//			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
//    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
//    		param.addOrFilter(orFilter1,orFilter2);
		}else{
			if (StringUtil.isNotBlank(model.getUsername())) {
				sql=sql+" and u.user_name ='"+model.getUsername()+"'";
//				param.addParam("user.userName", Operators.EQ, model.getUsername());
			}
			if (StringUtil.isNotBlank(model.getRealname())) {
				sql=sql+" and u.real_name ='"+model.getRealname()+"'";
//				param.addParam("user.realName", Operators.EQ, model.getRealname());
			}
			if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				sql=sql+" and uc.user_type='"+model.getUser().getUserCache().getUserType()+"'";
//				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		sql=sql+" and u.user_id=uc.user_id and u.user_id=a.user_id";
//		param.addPage(model.getPage(), model.getRows());
		if (model.getOrder().equals("desc")) { 
			sql=sql+" order by "+model.getSort()+" desc";
//			param.addOrder(OrderType.DESC, model.getSort());
		} else {
			sql=sql+" order by "+model.getSort()+" asc";
//			param.addOrder(OrderType.ASC, model.getSort());
		}
		
		Query query = em.createNativeQuery(sql.toString());
		List<AccountModel> list = new ArrayList<AccountModel>();
		List rows = query.getResultList();
		for (Object row : rows) {  
	        Object[] cells = (Object[]) row; 
	        AccountModel accountModel=new AccountModel();
	        accountModel.setId(Long.parseLong(cells[0].toString()));
	        accountModel.setUsername(cells[1]==null?"":cells[1].toString());
	        accountModel.setRealname(cells[2]==null?"":cells[2].toString());
	        accountModel.setTotal(Double.parseDouble(cells[3]==null?"0":cells[3].toString()));
	        accountModel.setUseMoney(Double.parseDouble(cells[4]==null?"0":cells[4].toString()));
	        accountModel.setNoUseMoney(Double.parseDouble(cells[5]==null?"0":cells[5].toString()));
	        accountModel.setCollection(Double.parseDouble(cells[6]==null?"0":cells[6].toString()));
	        list.add(accountModel);
	    } 
		
		PageDataList<AccountModel> pageDateList_ = new PageDataList<AccountModel>();
		pageDateList_.setPage(null);
		
//		PageDataList<Account> pageDateList = findPageList(param);
//		PageDataList<AccountModel> pageDateList_ = new PageDataList<AccountModel>();
//		List<AccountModel> list = new ArrayList<AccountModel>();
//		pageDateList_.setPage(pageDateList.getPage());
//		if (pageDateList.getList().size() > 0) {
//			for (int i = 0; i < pageDateList.getList().size(); i++) {
//				Account account = (Account) pageDateList.getList().get(i);
//				AccountModel am = AccountModel.instance(account);
//				try{
//    				am.setUsername(account.getUser().getUserName());
//    				am.setRealname(account.getUser().getRealName());
//    				list.add(am);
//				}catch(Exception e){
//				    e.printStackTrace();
//				}
//			}
//		}
		pageDateList_.setList(list);
		return pageDateList_;
	}
}
