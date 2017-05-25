package com.rongdu.p2psys.bond.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.bond.dao.BondDao;
import com.rongdu.p2psys.bond.dao.BondTenderDao;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.exception.BondException;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.service.BondService;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.BondConfigRuleCheck;

/**
 * 债权Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Service(value = "bondService")
@Transactional
public class BondServiceImpl implements BondService {
    
	@Resource
    private BondDao bondDao;
	@Resource
    private BondTenderDao bondTenderDao;
	@Resource
    private BorrowTenderDao borrowTenderDao;
	@Resource
    private BorrowCollectionDao borrowCollectionDao;

    @Override
    public Bond addBond(Bond bond) {
    	
    	Date nowDate = new Date();
    	
    	bond.setDayId(bondDao.getMaxDayId(DateUtil.dateStr7(new Date())) + 1);
    	// 跳过初审
    	BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global.getRuleCheck("bondConfig");
    	if(bondConfigRuleCheck.bondNeedVerify == 0){
    		bond.setStatus(Bond.STATUS_VERIFY_SUCC);
    	}else{
    		bond.setStatus(Bond.STATUS_RELEASE);
    	}
    	// 下一期还款
    	BorrowTender borrowTender =borrowTenderDao.find(bond.getBorrowTenderId()); //获得最初发布人的userId
    	BorrowCollection nextBorrowCollection = borrowCollectionDao.getNextCollectionByBorrowId(bond.getBorrow().getId(), borrowTender.getUser().getUserId());
    	bond.setStartPeriod(nextBorrowCollection.getPeriod());
    	bond.setVerifyUserId(1);
    	bond.setVerifyTime(nowDate);
    	bond.setFullVerifyUserId(1);
    	bond.setFullVerifyTime(nowDate);
    	bond.setAddTime(nowDate);
    	bondDao.save(bond);
    	
    	// 给债权出让人发送发布债权成功通知
    	Global.setTransfer("bond", bond);
    	Global.setTransfer("user", bond.getUser());
		AbstractExecuter bondAddNoticeExecuter = ExecuterHelper.doExecuter("bondAddNoticeExecuter");
		bondAddNoticeExecuter.execute(0, bond.getUser());

       return bond;

    }

    @Override
    public Bond getBondById(long id) {
        return bondDao.getBondById(id);
    }
    
    @Override
    public BondModel getBondDetail(BondModel model) {
    	
    	PageDataList<BondModel> pageList = bondDao.getBondList(model);
    	
    	List<BondModel> list = pageList.getList();
    	
    	if(list != null && list.size() > 0){
    		return list.get(0);
    	}else{
    		throw new BondException("债权不存在！");
    	}
    }

    @Override
    public Bond bondUpdate(Bond bond) {
        return  bondDao.update(bond);

    }

    @Override
    public void deleteBond(long id) {
        bondDao.delete(id);
    }

    @Override
    public PageDataList<BondModel> getBondList(BondModel model) {
        return bondDao.getBondList(model);
    }
    
    @Override
    public PageDataList<BondModel> getAllBondList(BondModel model) {

    	QueryParam param = QueryParam.getInstance();
    	param.addOrder(OrderType.DESC, "addTime");
    	param.addPage(model.getPage(), model.getRows());
    	PageDataList<Bond> pageDataList = bondDao.findPageList(param);
    	
        PageDataList<BondModel> pageDataList_ = new PageDataList<BondModel>();
        List<BondModel> list = new ArrayList<BondModel>();
        pageDataList_.setPage(pageDataList.getPage());
        
        List<Bond> bondList = pageDataList.getList();
        if (bondList != null && bondList.size() > 0) {
            for (Bond bond : bondList) {
				Borrow borrow = bond.getBorrow();
            	BondModel bm = BondModel.instance(bond);
            	bm.setUserName(bond.getUser().getUserName());
            	bm.setUser(null);
            	bm.setBorrowName(borrow.getName());
            	if(borrow.getStatus() == Borrow.STATUS_REPAYMENT_DONE){
            		bm.setStatus((byte)Borrow.STATUS_REPAYMENT_DONE);
            	}
            	bm.setApr(borrow.getApr());
            	bm.setPeriodStartAll((bm.getStartPeriod() + 1) + "/" + (borrow.getPeriod() + 1));
            	bm.setPayInterest(bondTenderDao.getPayInterestByBondId(bond.getId()));
            	list.add(bm);
			}
        }
        pageDataList_.setList(list);
        return pageDataList_;
    }

    @Override
    public PageDataList<BondModel> getBondModelPage(BondModel model) {
    	return bondDao.getBondModelPage(model);
    }
    
    @Override
    public PageDataList<BondModel> getSellingBondList(BondModel model) {
    	return bondDao.getSellingBondList(model);
    }
    
    @Override
    public PageDataList<BondModel> getSoldBondList(BondModel model) {
    	return bondDao.getSoldBondList(model);
    }
    
    @Override
    public void stopBond(long bondId) {
    	Bond bond = bondDao.getBondById(bondId);
    	if(bond.getStatus() != Bond.STATUS_VERIFY_SUCC){
    		throw new BondException("债权撤回失败！");
    	}
    	bond.setStatus(Bond.STATUS_STOP_USER);
    	bondDao.update(bond);
    	
    	// 债权撤回时给债权出让人发送通知
    	Global.setTransfer("user", bond.getUser());
		AbstractExecuter repayExecuter = ExecuterHelper.doExecuter("bondSellStopNoticeExecuter");
		repayExecuter.execute(0, bond.getUser());
    	
    }
    
    @Override
    public void autoStopBond() {
    	
    	QueryParam param = QueryParam.getInstance();
		param.addParam("status", Bond.STATUS_VERIFY_SUCC);
		List<Bond> list = bondDao.findByCriteria(param);
		
		if(list ==null || list.size() ==0){
			return;
		}
		Date nowTime = new Date();
		for (Bond bond : list) {
			BorrowCollection borrowCollection = borrowCollectionDao.getNextCollectionByBorrowId(bond.getBorrow().getId(), bond.getUser().getUserId());
			String repayStartTime = DateUtil.dateStr2(borrowCollection.getRepaymentTime()) + " 00:00:00";
			if(getDatePoor(DateUtil.getDateByFullDateStr(repayStartTime), nowTime) <=24){
				bond.setStatus(Bond.STATUS_STOP_AUTO);
		    	// 债权撤回时给债权出让人发送通知
		    	Global.setTransfer("user", bond.getUser());
				AbstractExecuter repayExecuter = ExecuterHelper.doExecuter("bondSellStopNoticeExecuter");
				repayExecuter.execute(0, bond.getUser());
			}
		}
		// 更新债权
		bondDao.update(list);
    }
    
    /** 获取两个时间的时间查 如1天2小时30分钟 */
    public long getDatePoor(Date endDate, Date nowDate) {
     
        long nh = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少小时
        long hour = diff / nh;
        return hour;
    }
    
    @Override
    public BondModel getBondModelByBondId(long bondId) {
    	Bond bond = bondDao.getBondById(bondId);
    	BondModel bondModel = new BondModel();
    	Borrow borrow = bond.getBorrow();
    	bondModel.setBorrowName(borrow.getName());
    	bondModel.setBorrowId(borrow.getId());
    	bondModel.setApr(borrow.getApr());
    	bondModel.setTenderMoney(borrowTenderDao.find(bond.getBorrowTenderId()).getMoney());
    	bondModel.setHoldDays(DateUtil.daysBetween(borrow.getReviewTime(), new Date()));
    	bondModel.setReviewTime(borrow.getReviewTime());
    	bondModel.setLastRepaymentTime(borrowCollectionDao.getLastRepayDateByBorrowId(borrow.getId()));
    	bondModel.setBorrowStyle(borrow.getStyle());
    	return bondModel;
    }
    
    @Override
    public BondModel getBondModelByBondTenderId(long tenderId) {
    	
    	BondTender bondTender = bondTenderDao.getBondTenderById(tenderId);
    	Bond bond = bondTender.getBond();
    	
    	BondModel bondModel = new BondModel();
    	Borrow borrow = bond.getBorrow();
    	bondModel.setBorrowName(borrow.getName());
    	bondModel.setBorrowId(borrow.getId());
    	bondModel.setApr(borrow.getApr());
    	bondModel.setTenderMoney(bondTender.getTenderMoney());
    	Date lastDay = borrowCollectionDao.getLastRepayDateByBorrowId(borrow.getId());
    	bondModel.setReviewTime(borrow.getReviewTime());
    	bondModel.setRemainDays(DateUtil.daysBetween(new Date(), lastDay));
    	bondModel.setLastRepaymentTime(lastDay);
    	bondModel.setBorrowStyle(borrow.getStyle());
    	return bondModel;
    }
}
