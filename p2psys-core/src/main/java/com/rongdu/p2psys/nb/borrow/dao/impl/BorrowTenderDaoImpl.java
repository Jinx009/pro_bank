package com.rongdu.p2psys.nb.borrow.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.InvestRecordModel;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.nb.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.nb.borrow.model.InvestDetailModel;

@Repository("theBorrowTenderDao")
public class BorrowTenderDaoImpl extends BaseDaoImpl<BorrowTender> implements BorrowTenderDao
{
	public void update(double account, double scales, int status, long id) 
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" update borrow  ");
		buffer.append(" set tenderTimes = tenderTimes + 1 , ");
		buffer.append(" accountYes = round(accountYes + :account, 2), ");
		buffer.append(" scales = :scales ");
		buffer.append("  where id = :id and status = :status and round(accountYes + :account) <= account ");
		
		Query query = em.createQuery(buffer.toString());
		query.setParameter("account", account);
		query.setParameter("scales", scales);
		query.setParameter("status", status);
		query.setParameter("id", id);
		
		int count = query.executeUpdate();
		if (count < 1) 
		{
			throw new BorrowException("投标失败！此标可能已满！", 1);
		}
		super.refresh(find(id));
	}

	public void saveList(List<BorrowTender> list)
	{
		saveList(list);
	}
	

	@SuppressWarnings("unused")
	@Override
	public PageDataList<InvestRecordModel> multipleIdentitiesList(InvestRecordModel model) throws ParseException {
		PageDataList<InvestRecordModel> pageDataList = new PageDataList<InvestRecordModel>();	
		StringBuffer sql = new StringBuffer();
		if(model.getFlag() == 0){
			sql.append("select id,addTime,productName,money,type,reviewTime,timeLimit,apr,CONVERT(borrowTimeType,SIGNED),isOut,isFixedTerm,expireTime,ppfundInType,interestAmount,expectProfit,account,CONVERT(addRate,DECIMAL),CONVERT(typeId,SIGNED) from noexpires where userId="+model.getUser().getBindId()+" order by expireTime asc");
		}else{
			sql.append("select id,addTime,productName,money,type,reviewTime,timeLimit,apr,CONVERT(borrowTimeType,SIGNED),isOut,isFixedTerm,expireTime,ppfundInType,interestAmount,expectProfit,account,CONVERT(addRate,DECIMAL),CONVERT(typeId,SIGNED) from "+
							  " ( "+
							  " select * from (select * from noexpires order by expireTime asc) "+
							  " as noe where noe.userId="+model.getUser().getBindId()+ ""+
							  " union ALL "+
							  " select * from ( "+
							  " select * from expires order by expireTime asc) as ex where ex.userId="+model.getUser().getBindId()+ ""+
							  " ) as ye ");
		}
		
		Query query = em.createNativeQuery(sql.toString());
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
		query.setFirstResult((model.getPage() - 1) *  model.getSize());
        query.setMaxResults(model.getSize());
        List<Object[]> list = query.getResultList();
        PageDataList<InvestRecordModel> pageDataList_ = new PageDataList<InvestRecordModel>();
        List<InvestRecordModel> modelList = new ArrayList<InvestRecordModel>();
        pageDataList_.setPage(page);
        DateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		for (Object[] o : list) {
	    		InvestRecordModel invest = new InvestRecordModel();
	    		Integer typeId = (o[17]== null?-1:Integer.valueOf(o[17].toString()));
	    		if(null != typeId && Borrow.TYPE_ENTRUST != typeId){//固定、海外、vip
	    			invest.setId(Long.parseLong(o[0].toString()));
	        		invest.setAddTime(df.parse(o[1].toString()));
	        		invest.setProductName(o[2]==null?null:o[2].toString());
	        		invest.setMoney(o[3]==null?null:Double.valueOf(o[3].toString()));
	        		invest.setType(o[4]==null?null:Integer.valueOf(o[4].toString()));
	        		invest.setReviewTime(df.parse(o[5]==null?"0000-00-00 00:00:00":o[5].toString()));
	        		invest.setTimeLimit(o[6]==null?null:Integer.valueOf(o[6].toString()));
	        		invest.setApr(o[7]==null?null:Double.valueOf(o[7].toString()));
	        		int borrowTimeType=(o[8]==null?-1:Integer.valueOf(o[8].toString()));
	        		invest.setTypeId(typeId);
	        		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
	        		if(invest.getType() > 1){
	        			if(Borrow.TYPE_VIP == invest.getTypeId()){//vip标
	        				if(borrowTimeType >0 && borrowTimeType ==1){
	                			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollDay(invest.getAddTime(), invest.getTimeLimit())));
	                		}else{
	                			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollMon(invest.getAddTime(), invest.getTimeLimit())));
	                		}
	        			}else{
	        				if(borrowTimeType >0 && borrowTimeType ==1){
	                			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollDay(invest.getReviewTime(), invest.getTimeLimit())));
	                		}else{
	                			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollMon(invest.getReviewTime(), invest.getTimeLimit())));
	                		}
	        			}
	        			
	        		}else{
	        			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollDay(invest.getReviewTime(), invest.getTimeLimit())));
	        		}
	        		invest.setFlag(DateUtil.compareDate(sim.format(new Date()), invest.getExpirationDate()));
	        		invest.setIsOut(o[9]==null?null:Integer.valueOf(o[9].toString()));
	        		invest.setIsFixedTerm(o[10]==null?null:Integer.valueOf(o[10].toString()));
	        		
	        		//加息
	        		if(invest.getType() ==2){//非现金
	        			int rateId = (o[16]==null?0:Integer.valueOf(o[16].toString()));
	            		if(rateId>=0){
	            			Double personalRate = getPersonalInterest(invest.getId(), 2);
	            			invest.setAddRate((getInterestRate(Integer.valueOf(rateId))+personalRate));
	            			invest.setRateStatus(1);
	            		}
	            		
	        		}
	        		
	        		int type = (o[12]==null?-1:Integer.valueOf(o[12].toString()));
	        		if(invest.getType()==1){//现金类(固定期限)
	        			String codeType = getType(type);
	            		invest.setPpfundInType(codeType);
	            		if(null != codeType && codeType.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)){
	            			invest.setInterestAmount(o[13]==null?null:Double.valueOf(o[13].toString()));
	            			double profit =  (((invest.getApr() /100)* invest.getTimeLimit() * invest.getInterestAmount())/Constant.YEAR_DAY);
	            			invest.setExpectProfit(profit);
	            		}else{
	            			invest.setInterestAmount(0);
	            		}
	            		
	            		if(invest.getType()==1 && invest.getIsFixedTerm() ==1&& null !=codeType && !codeType.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)){
	            			double account = (o[15]==null?null:Double.valueOf(o[15].toString()));
	            			double profit =  (((invest.getApr()/100)* invest.getTimeLimit() * account)/Constant.YEAR_DAY);
	            			invest.setExpectProfit(profit);
	            		}
	        		}
	        	
	        		if(invest.getType() ==2 ){//非现金类(固定、海外)
	        			invest.setExpectProfit(getExpectProfit(invest.getId()));
	        		}
	        		modelList.add(invest);
	    		}
    		}
		pageDataList_.setList(modelList);
		return pageDataList_;
	}
	
	public String getType(int type){
		StringBuffer sql =new StringBuffer("select * from  nb_product_type where id =:type");
		Query query = em.createNativeQuery(sql.toString()).setParameter("type", type);
		List list = query.getResultList();
		String codeType =null;
		if(list !=null && list.size()>0){
			Iterator iterator = list.iterator(); 
			while (iterator.hasNext()) { 
				Object[] row = (Object[]) iterator.next();	
				codeType= row[5].toString();
			}
		}
		return codeType;
	}

	@Override
	public PageDataList<InvestDetailModel> getInvestRecordByItem(InvestDetailModel model) {
		PageDataList<InvestDetailModel> pageDataList = new PageDataList<InvestDetailModel>();	
		StringBuffer sql = new StringBuffer();
		if(null != model){
				sql.append("select ");
				sql.append("id,addTime,productName,money,type,reviewTime,timeLimit,apr,"
						         + "CONVERT(borrowTimeType,SIGNED),isOut,isFixedTerm,expireTime,"
						         + "ppfundInType,interestAmount,expectProfit,account,"
						         + "CONVERT(addRate,DECIMAL),interestRate,CONVERT(typeId,SIGNED),"
						         + "CONVERT(status,SIGNED),scales,expectedLow,expectedUp,interest_m,floatIncome "
						         + "  from ( ");
			String noexpiresSql=noexpires(model);
			String expiresSql =expires(model);
			sql.append(noexpiresSql);
			sql.append(expiresSql);
		}
		Query query = em.createNativeQuery(sql.toString());
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
		query.setFirstResult((model.getPage() - 1) *  model.getSize());
        query.setMaxResults(model.getSize());
        List<Object[]> list = query.getResultList();
        PageDataList<InvestDetailModel> pageDataList_ = new PageDataList<InvestDetailModel>();
        List<InvestDetailModel> modelList = new ArrayList<InvestDetailModel>();
        pageDataList_.setPage(page);
    	for (Object[] o : list) {
    		InvestDetailModel invest = new InvestDetailModel();
    		invest.setId(Long.parseLong(o[0].toString()));
    		invest.setAddTime(o[1]==null?null:(Date)o[1]);
    		invest.setProductName(o[2]==null?null:o[2].toString());
    		invest.setMoney(o[3]==null?null:Double.valueOf(o[3].toString()));
    		invest.setType(o[4]==null?null:Integer.valueOf(o[4].toString()));
    		invest.setReviewTime(o[5]==null?null:(Date) o[5]);
    		invest.setTimeLimit(o[6]==null?null:Integer.valueOf(o[6].toString()));
    		invest.setApr(o[7]==null?null:Double.valueOf(o[7].toString()));
    		//标类型
    		invest.setTypeId(o[18]==null?null:Integer.valueOf(o[18].toString()));
    		int borrowTimeType=(o[8]==null?-1:Integer.valueOf(o[8].toString()));
    		if(invest.getType() > 1){
    			if(Borrow.TYPE_VIP == invest.getTypeId()){//vip标
    				if(borrowTimeType >0 && borrowTimeType ==1){
            			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollDay(invest.getAddTime(), invest.getTimeLimit())));
            		}else{
            			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollMon(invest.getAddTime(), invest.getTimeLimit())));
            		}
    			}else{
    				if(borrowTimeType >0 && borrowTimeType ==1){
            			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollDay(invest.getReviewTime(), invest.getTimeLimit())));
            		}else{
            			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollMon(invest.getReviewTime(), invest.getTimeLimit())));
            		}
    			}
    			
    		}else{
    			invest.setExpirationDate(DateUtil.dateStr2(DateUtil.rollDay(invest.getReviewTime(), invest.getTimeLimit())));
    		}
    		invest.setIsOut(o[9]==null?null:Integer.valueOf(o[9].toString()));
    		invest.setIsFixedTerm(o[10]==null?null:Integer.valueOf(o[10].toString()));
    		int type = (o[12]==null?-1:Integer.valueOf(o[12].toString()));
    		if(invest.getType()==1){//现金类(固定期限)
    			String codeType = getType(type);
        		invest.setPpfundInType(codeType);
        		if(null != codeType && codeType.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)){
        			invest.setInterestAmount(o[13]==null?null:Double.valueOf(o[13].toString()));
        			double profit =  (((invest.getApr() /100)* invest.getTimeLimit() * invest.getInterestAmount())/Constant.YEAR_DAY);
        			invest.setExpectProfit(profit);
        		}else{
        			invest.setInterestAmount(0);
        		}
        		
        		if(invest.getType()==1 && invest.getIsFixedTerm() ==1&& null !=codeType && !codeType.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)){
        			double account = (o[15]==null?null:Double.valueOf(o[15].toString()));
        			double profit =  (((invest.getApr() /100)* invest.getTimeLimit() * account)/Constant.YEAR_DAY);
        			invest.setExpectProfit(profit);
        		}
    		}
    		
    		//状态
    		invest.setStatus(o[19]==null?null:Integer.valueOf(o[19].toString()));
    		//进度
    		invest.setScales(o[20]==null?null:Double.valueOf(o[20].toString()));
    		
    		
    		//浮动标收益率
    		invest.setExpectedLow(o[21]==null?null:Double.valueOf(o[21].toString()));
    		//浮动标收益率
    		invest.setExpectedUp(o[22]==null?null:Double.valueOf(o[22].toString()));
    		
    		if(invest.getType() ==2 && invest.getTypeId() !=Borrow.TYPE_ENTRUST ){//非现金类(固定、海外)
    			invest.setExpectProfit(getExpectProfit(invest.getId()));
    		}
    		
    		if(invest.getType() ==2 && invest.getTypeId() == Borrow.TYPE_ENTRUST){//非现金(浮动)
    			Double interest =(o[23]==null?null:Double.valueOf(o[23].toString()));
    			Double floatIncome =(o[24]==null?null:Double.valueOf(o[24].toString()));
    			if(DateUtil.compareDateS(invest.getExpirationDate())){
    				invest.setExpectProfit((interest+floatIncome));
    			}else{
    				invest.setExpectProfit(-1);
    			}
    			
    		}
    		
    		//红包
    		double money =0;
    		if(invest.getType() == 1){//现金
    			money = countRedMoney(model.getUser().getUserId(), 0, invest.getId());
    		}
    		if(invest.getType() == 2){//非现金
    			money = countRedMoney(model.getUser().getUserId(), invest.getId(), 0);
    		}
    		invest.setRemark(redPacketRemark(money));
    		
    		//加息
    		if(invest.getType() ==2){//非现金
    			int rateId =(o[16]==null?0:Integer.valueOf(o[16].toString()));
        		if(rateId >= 0){
        			Double personalRate = getPersonalInterest(invest.getId(), 2);
        			invest.setAddRate((getInterestRate(rateId)+personalRate));
        			invest.setRateStatus(1);
        		}
    		}
    		modelList.add(invest);
    		}
		pageDataList_.setList(modelList);
		return pageDataList_;
	}
	
	/**
	 * 现金类
	 * @param model
	 * @return
	 */
	public String noexpires(InvestDetailModel model){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (select * from noexpires  order by expireTime asc) "+
				          " as noe where noe.userId="+model.getUser().getBindId()+ "");
			if( !"".equals(model.getStartTime()) && null != model.getStartTime()){
				sql.append(" and addTime >= '"+DateUtil.dateStr4(DateUtil.valueOf(model.getStartTime() + " 00:00:00"))+"'");
			}
			if(!"".equals(model.getEndTime()) && null != model.getEndTime()){
				sql.append(" and addTime <= '"+DateUtil.dateStr4(DateUtil.valueOf(model.getEndTime() + " 23:59:59"))+"'");
			}
			return sql.toString();
	}
	
	public String expires(InvestDetailModel model){
		StringBuffer sql = new StringBuffer();
		sql.append(
				  " union ALL "+
				  " select * from ( "+
				  " select * from expires order by expireTime asc) as ex where ex.userId="+model.getUser().getBindId()+ ""
				  );

			if( !"".equals(model.getStartTime()) && null != model.getStartTime()){
				sql.append(" and addTime >= '"+DateUtil.dateStr4(DateUtil.valueOf(model.getStartTime() + " 00:00:00"))+"'");
			}
			if(!"".equals(model.getEndTime()) && null != model.getEndTime()){
				sql.append(" and addTime <= '"+DateUtil.dateStr4(DateUtil.valueOf(model.getEndTime() + " 23:59:59"))+"'");
			}
			sql.append(" ) as ye ");
			return sql.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public double  countRedMoney(long userId,long tenderId,long ppfundId){
		StringBuffer sql =new StringBuffer("select * from rd_user_red_packet where user_id =:userId and is_used=1 ");
		Query query = null;
		double codeType =0;
		if(tenderId >0 || ppfundId > 0){
			if(tenderId >0){
				sql.append(" and tender_id =:tenderId");
				query = em.createNativeQuery(sql.toString()).setParameter("userId", userId).setParameter("tenderId", tenderId);
			}
			if(ppfundId > 0){
				sql.append(" and ppfund_in_id =:ppfundId");
				query = em.createNativeQuery(sql.toString()).setParameter("userId", userId).setParameter("ppfundId", ppfundId);
			}
			List list = query.getResultList();
			
			if(list !=null && list.size()>0){
				Iterator iterator = list.iterator(); 
				while (iterator.hasNext()) { 
					Object[] row = (Object[]) iterator.next();	
					codeType += Double.valueOf(row[3].toString());
				}
			}
		}
		return codeType;
	}
	
	
	public String redPacketRemark(double money){
		if(money >0){
			return "使用红包"+money+"元";
		}else{
			return "--";
		}
		
	}

	@Override
	public PageDataList<InvestRecordModel> lockCashRecord(
			InvestRecordModel model) {
		PageDataList<InvestRecordModel> pageDataList = new PageDataList<InvestRecordModel>();	
		StringBuffer sql = new StringBuffer();
		if(null != model){
			sql.append("select * from (select '1' as type, c.id as id,'' as productName,c.money as money,c.add_time as addTime,'0' as typeId from rd_account_cash as c "+ 
					" where c.user_id="+model.getUser().getBindId()+" and c.status in(0,5,6,9) "+
					" UNION all "+
					" select '2' as type, b.id as id,b.`name` as productName,t.account as money,t.add_time as addTime,b.type as typeId from rd_borrow_tender as t "+
					" left join rd_borrow as b on b.id = t.borrow_id "+
					" where t.user_id="+model.getUser().getBindId()+" and b.`status` =1) as td order by td.addTime desc");
		}
		Query query = em.createNativeQuery(sql.toString());
		Page page = new Page(query.getResultList().size(), model.getPage(), model.getSize());
		query.setFirstResult((model.getPage() - 1) *  model.getSize());
        query.setMaxResults(model.getSize());
        List<Object[]> list = query.getResultList();
        List<InvestRecordModel> modelList = new ArrayList<InvestRecordModel>();
        pageDataList.setPage(page);
    	for (Object[] o : list) {
    		InvestRecordModel invest = new InvestRecordModel();
    		invest.setType(o[0]==null?null:Integer.valueOf(o[0].toString()));
    		invest.setId(Long.parseLong(o[1].toString()));
    		invest.setProductName(o[2]==null?null:o[2].toString());
    		invest.setMoney(o[3]==null?null:Double.valueOf(o[3].toString()));
    		invest.setAddTime(o[4]==null?null:(Date)o[4]);
    		if(2==invest.getType()){
    			Long productId =getProductBasicId(Long.parseLong(o[5].toString()), invest.getId());
    			invest.setId(productId);
    		}
    		modelList.add(invest);
    	}
    	pageDataList.setList(modelList);
    	return pageDataList;
  }
	
	public Long getProductBasicId(Long typeId, Long relatedId){
		StringBuffer sql = new StringBuffer();
		sql.append("select pb.id from nb_product_basic as pb left join nb_product_type as pt on pt.id = pb.type_id where pb.type_id=:typeId and pb.related_id=:relatedId");
		Query query = em.createNativeQuery(sql.toString()).setParameter("typeId", typeId).setParameter("relatedId", relatedId);
		Object list = query.getSingleResult();
		Long baseId =Long.parseLong(list.toString());
		return baseId;
	}
	
	public double getInterestRate(int id){
		StringBuffer sb = new StringBuffer();
		sb.append(" select sum(rate) from nb_interest_rate where id= ?1 ");
		Query q = em.createNativeQuery(sb.toString()).setParameter("1", id);
		Object ret = q.getSingleResult();
		
		if (ret == null)
		{
			return 0;
		}
			
		return Double.parseDouble(ret.toString());
	}
	
	//获取到期收益
	public double getExpectProfit(long tenderId){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(interest+interest_rate+float_income) from rd_borrow_collection where tender_id=?1");
		Query q = em.createNativeQuery(sb.toString()).setParameter("1", tenderId);
		Object ret = q.getSingleResult();
		
		if (ret == null)
		{
			return 0;
		}
			
		return Double.parseDouble(ret.toString());
	}
	
	
	/**
	 * 个人加息券
	 * @param id 购买产品记录Id
	 * @param type 哪种产品（1:现金 2:非现金）
	 * @return
	 */
	public double getPersonalInterest(long id,int type){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(to_rate_adjust) from nb_coupon ");
		if(1==type){
			sb.append(" where ppfund_in_id =?1");
		}else if(2 == type){
			sb.append(" where borrow_tender_id =?1");
		}
		Query q = em.createNativeQuery(sb.toString()).setParameter("1", id);
		Object ret = q.getSingleResult();
		
		if (ret == null)
		{
			return 0;
		}
			
		return Double.parseDouble(ret.toString());
		
	}
	
}
