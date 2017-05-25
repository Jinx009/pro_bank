package com.rongdu.p2psys.borrow.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowOverdueDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowOverdue;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.user.domain.User;

@Service("borrowRepaymentService")
public class BorrowRepaymentServiceImpl implements BorrowRepaymentService {

	Logger logger = Logger.getLogger(BorrowRepaymentServiceImpl.class);
	
	@Resource
	private BorrowRepaymentDao borrowRepaymentDao;
	@Resource
	private BorrowOverdueDao borrowOverdueDao;
	@Resource
	private AutoBorrowService autoBorrowService;
	@Resource
	private BorrowCollectionDao collectionDao;
	@Resource
	private ChinapnrService chinapnrService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private BorrowService borrowService;

	@Override
	public PageDataList<BorrowRepaymentModel> list(BorrowRepaymentModel model, String searchName) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
		    if (model.getVouchFirm() != null) {
		        param.addParam("borrow.vouchFirm", model.getVouchFirm());
		    } 
		    if(StringUtil.isNotBlank(model.getTimeVal())){
		    	Calendar c = Calendar.getInstance();
		    	if ("all".equals(model.getTimeVal())) {
		    		
		    	}
				if ("day".equals(model.getTimeVal())) {
					String startTime = DateUtil.dateStr2(c.getTime()) + " 00:00:00";
					String endTime = DateUtil.dateStr2(c.getTime()) + " 23:59:59";
					param.addParam("repaymentTime", Operators.GTE, DateUtil.valueOf(startTime));
					param.addParam("repaymentTime", Operators.LTE, DateUtil.valueOf(endTime));
				}
				if ("month".equals(model.getTimeVal())) {
					String startTime = DateUtil.dateStr2(c.getTime()) + " 00:00:00";
					String endTime = DateUtil.dateStr2(DateUtil.rollDay(c.getTime(), 30)) + " 23:59:59";
					param.addParam("repaymentTime", Operators.GTE, DateUtil.valueOf(startTime));
					param.addParam("repaymentTime", Operators.LTE, DateUtil.valueOf(endTime));
				}
				if ("nowMonth".equals(model.getTimeVal())) {
					c.add(Calendar.MONTH, 1);
					c.set(Calendar.DAY_OF_MONTH, 0);
					int days = c.getActualMaximum(Calendar.DATE);
					String startTime = DateUtil.dateStr2(DateUtil.rollDay(c.getTime(), -days+1)) + " 00:00:00";
					String endTime = DateUtil.dateStr2(c.getTime()) + " 23:59:59";
					param.addParam("repaymentTime", Operators.GTE, DateUtil.valueOf(startTime));
					param.addParam("repaymentTime", Operators.LTE, DateUtil.valueOf(endTime));
				}
		    } //else {
		    	  if (StringUtil.isNotBlank(model.getSearchName())){
		    		  SearchFilter orFilter1 = new SearchFilter("borrow.name", Operators.LIKE, model.getSearchName());
			      	  SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
			      	  SearchFilter orFilter3 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
			      	  param.addOrFilter(orFilter1, orFilter2, orFilter3);
		    	  }else{
			        if (model.getBorrowId() > 0) {
		                param.addParam("borrow.id", model.getBorrowId());
		            }
		            if (model.getUser() != null && model.getUser().getUserId() > 0) {
		                param.addParam("user.userId", model.getUser().getUserId());
		            }
		            if (model.getStatus() != 99) {
		                param.addParam("status", model.getStatus());
		            }
		            if (StringUtil.isNotBlank(model.getUserName())) {
		                param.addParam("user.userName", Operators.EQ, model.getUserName());
		            }
		            if (StringUtil.isNotBlank(model.getRealName())) {
		                param.addParam("user.realName", Operators.EQ, model.getRealName());
		            }
		            Date d = DateUtil.getDate(System.currentTimeMillis() / 1000 + "");
		            if (model.getTime() == 7) {
		                param.addParam("repaymentTime", Operators.GTE, d);
		                param.addParam("repaymentTime", Operators.LTE, DateUtil.rollDay(d, 7));
		            } else if (model.getTime() > 0 && model.getTime() < 4) {
		                param.addParam("repaymentTime", Operators.GTE, d);
		                param.addParam("repaymentTime", Operators.LTE, DateUtil.rollMon(d, model.getTime()));
		            }
		            if (StringUtil.isNotBlank(model.getStartTime())) {
		                Date start = DateUtil.valueOf(model.getStartTime());
		                param.addParam("repaymentTime", Operators.GTE, start);
		            }
		            if (StringUtil.isNotBlank(model.getEndTime())) {
		                Date end = DateUtil.valueOf(model.getEndTime());
		                param.addParam("repaymentTime", Operators.LTE, end);
		            }
		            if (StringUtil.isNotBlank(model.getBidNo())) {
		            	param.addParam("borrow.bidNo", model.getBidNo());
		            }
		    	}
		   // }
            if (model.isLate()) {
                if (model.getType() != 0) {
                    param.addParam("borrow.type", model.getType());
                }
                param.addParam("repaymentTime", Operators.LTE, new Date());
                param.addParam("borrow.type", Operators.NOTEQ, Borrow.TYPE_FLOW);
                param.addParam("borrow.type", Operators.NOTEQ, Borrow.TYPE_SECOND);
                SearchFilter orFilter1 = new SearchFilter("repaymentYesTime", Operators.PROPERTY_GT, "repaymentTime");
                SearchFilter orFilter2 = new SearchFilter("repaymentYesAccount", Operators.EQ, 0);
                param.addOrFilter(orFilter1, orFilter2);
            }
            param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.ASC, "repaymentTime");

		PageDataList<BorrowRepayment> pageDataList = borrowRepaymentDao.findPageList(param);
		PageDataList<BorrowRepaymentModel> pageDateList = new PageDataList<BorrowRepaymentModel>();
		List<BorrowRepaymentModel> list = new ArrayList<BorrowRepaymentModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowRepayment r = (BorrowRepayment) pageDataList.getList().get(i);
				BorrowRepaymentModel rm = BorrowRepaymentModel.instance(r);
				rm.setBorrowId(r.getBorrow().getId());
				rm.setBorrowName(r.getBorrow().getName());
				rm.setUserName(r.getUser().getUserName());
				rm.setRealName(r.getUser().getRealName());
				rm.setMobilePhone(r.getUser().getMobilePhone());
				rm.setTimeLimit(r.getBorrow().getTimeLimit());
				rm.setType(r.getBorrow().getType());
				rm.setBorrowStyle(r.getBorrow().getStyle()); // 设置还款方式
				rm.setBidNo(r.getBorrow().getBidNo());
				rm.setBorrow(null);
				rm.setUser(null);
				rm.setPeriod(r.getPeriod()+1);
                rm.setLateDays(r.getLateDays());
                rm.setLateInterest(r.getLateInterest());
				list.add(rm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}
	
	@Override
    public PageDataList<BorrowRepaymentModel> getList(BorrowRepaymentModel model) {
        PageDataList<BorrowRepayment> pageDataList = borrowRepaymentDao.getList(model);
        PageDataList<BorrowRepaymentModel> pageDateList = new PageDataList<BorrowRepaymentModel>();
        List<BorrowRepaymentModel> list = new ArrayList<BorrowRepaymentModel>();
        pageDateList.setPage(pageDataList.getPage());
        if (pageDataList.getList().size() > 0) {
            for (int i = 0; i < pageDataList.getList().size(); i++) {
                BorrowRepayment r = (BorrowRepayment) pageDataList.getList().get(i);
                BorrowRepaymentModel rm = BorrowRepaymentModel.instance(r);
                rm.setBorrowId(r.getBorrow().getId());
                rm.setBorrowName(r.getBorrow().getName());
                rm.setUserName(r.getUser().getUserName());
                rm.setRealName(r.getUser().getRealName());
                rm.setMobilePhone(r.getUser().getMobilePhone());
                rm.setTimeLimit(r.getBorrow().getTimeLimit());
                rm.setType(r.getBorrow().getType());
                rm.setBorrowStyle(r.getBorrow().getStyle()); // 设置还款方式
                rm.setBorrow(null);
                rm.setUser(null);
                rm.setLateInterest(r.getLateInterest());
                rm.setLateDays(r.getLateDays());
                list.add(rm);
            }
        }
        pageDateList.setList(list);
        return pageDateList;
    }

	@Override
	public BorrowRepayment findById(long repayId) {
		return borrowRepaymentDao.find(repayId);
	}

	@Override
	public double getUserBorrowRepayTotal(long userId) {
		return borrowRepaymentDao.getUserBorrowRepayTotal(userId);
	}

	@Override
	public BorrowRepaymentModel getReapyStatistics(long userId) {
		BorrowRepaymentModel bm = borrowRepaymentDao.getRepayStatistics(userId);
		BorrowRepayment br = borrowRepaymentDao.getNextRepayByUserId(userId);
		if (br != null) {
	        bm.setBorrowId(br.getBorrow().getId());
			bm.setNextRepayTime(br.getRepaymentTime());
			bm = borrowRepaymentDao.getBRMByCollectTime(bm, br.getRepaymentTime(), userId);
		}
		double jan = borrowRepaymentDao.getRepayByMonth(1, userId);
		double feb = borrowRepaymentDao.getRepayByMonth(2, userId);
		double mar = borrowRepaymentDao.getRepayByMonth(3, userId);
		double apr = borrowRepaymentDao.getRepayByMonth(4, userId);
		double may = borrowRepaymentDao.getRepayByMonth(5, userId);
		double jun = borrowRepaymentDao.getRepayByMonth(6, userId);
		double jul = borrowRepaymentDao.getRepayByMonth(7, userId);
		double aug = borrowRepaymentDao.getRepayByMonth(8, userId);
		double sep = borrowRepaymentDao.getRepayByMonth(9, userId);
		double oct = borrowRepaymentDao.getRepayByMonth(10, userId);
		double nov = borrowRepaymentDao.getRepayByMonth(11, userId);
		double dec = borrowRepaymentDao.getRepayByMonth(12, userId);
		bm.getRepayData().put("jan", jan);
		bm.getRepayData().put("feb", feb);
		bm.getRepayData().put("mar", mar);
		bm.getRepayData().put("apr", apr);
		bm.getRepayData().put("may", may);
		bm.getRepayData().put("jun", jun);
		bm.getRepayData().put("jul", jul);
		bm.getRepayData().put("aug", aug);
		bm.getRepayData().put("sep", sep);
		bm.getRepayData().put("oct", oct);
		bm.getRepayData().put("nov", nov);
		bm.getRepayData().put("dec", dec);
		return bm;
	}

	@Override
	public void overdue(BorrowRepayment borrowRepayment,  Operator operator) {
		BorrowOverdue borrowOverdue = new BorrowOverdue();
		borrowOverdue.setOperator(operator);
		borrowOverdue.setOverdueAccount(borrowRepayment.getRepaymentAccount());
		borrowOverdue.setOverdueTime(new Date());
		borrowOverdue.setRepaymentAccount(borrowRepayment.getRepaymentAccount());
		borrowOverdue.setRepaymentTime(borrowRepayment.getRepaymentTime());
		borrowOverdue.setUsername(borrowRepayment.getUser().getUserName());
		borrowOverdueDao.save(borrowOverdue);
		// 任务列表
		chinapnrService.overdue(borrowRepayment);
		// 设置webstatus=3
		borrowRepayment.setWebStatus(BorrowRepayment.WEB_STATUS_INSTEAD);
		borrowRepayment = borrowRepaymentDao.update(borrowRepayment);
		Borrow borrow = borrowRepayment.getBorrow();
		Global.setTransfer("borrow", borrow);
		autoBorrowService.overdue(borrowRepayment);
		//ConcurrentUtil.overdue(borrowRepayment);
	}
	
    @Override
    public double getRemainderCapital(long borrowId) {
        return borrowRepaymentDao.getRemainderCapital(borrowId);
    }

    @Override
    public double getRemainderInterest(long borrowId) {
        return borrowRepaymentDao.getRemainderInterest(borrowId);
    }

    @Override
    public List<BorrowRepayment> fingOverDueBorrowRepayment(User user) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("borrow.vouchFirm", user);
        param.addParam("repaymentTime", Operators.LT, new Date());
        param.addParam("repaymentYesAccount", Operators.NOTEQ, 0);
        param.addPage(1, 4);
        List<BorrowRepayment> list = borrowRepaymentDao.findByCriteria(param);
        for (BorrowRepayment br : list) {
            br.getBorrow().getId();
        }
        return list;
    }

    @Override
    public int count() {
        
        String date = DateUtil.dateStr2(new Date());
        Date start = DateUtil.valueOf(date + " 00:00:00");
        Date end = DateUtil.valueOf(date + " 23:59:59");
        QueryParam param = QueryParam.getInstance()
                .addParam("status", 0)
                .addParam("repaymentTime", Operators.GT, start)
                .addParam("repaymentTime", Operators.LT, end);
        return borrowRepaymentDao.countByCriteria(param);
    }

    @Override
    public List<BorrowRepayment> getRepaymentByBorrowId(long borrowId) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("borrow.id", borrowId);
        return borrowRepaymentDao.findByCriteria(param);
    }

    /**
     * 获得当前标需要还的还款期数
     * @param period
     * @return
     */
    public int getCurrPeriod(long borrowId){
        return borrowRepaymentDao.getCurrPeriod(borrowId);
    }
    
    @Override
    public int getUrgeCount(long userId) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("borrow.vouchFirm.userId", userId);
        param.addParam("status", 0);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        param.addParam("repaymentTime", Operators.LT, new Date());
        param.addParam("repaymentTime", Operators.GT, c.getTime());
        return borrowRepaymentDao.countByCriteria(param);
    }

    @Override
    public PageDataList<BorrowRepaymentModel> getCollectionList(BorrowModel model) {
        QueryParam param = QueryParam.getInstance();
//        if (StringUtil.isNotBlank(model.getBorrowName())) {
//            param.addParam("borrow.name", Operators.LIKE, model.getBorrowName());
//        }
        if (StringUtil.isNotBlank(model.getUserName())) {
            param.addParam("borrow.user.userName", Operators.EQ, model.getUserName());
        }
        if (model.getTime() == 7) {
            param.addParam("borrow.addTime", Operators.GT, DateUtil.rollDay(new Date(), -model.getTime()));
        } else if (model.getTime() != 0) {
            param.addParam("borrow.addTime", Operators.GT, DateUtil.rollMon(new Date(), -model.getTime()));
        }
        if (StringUtil.isNotBlank(model.getStartTime())) {
            Date startTime = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
            param.addParam("borrow.addTime", Operators.GT, startTime);
        }
        if (StringUtil.isNotBlank(model.getEndTime())) {
            Date endTime = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
            param.addParam("addTime", Operators.LT, endTime);
        }
        param.addParam("borrow.vouchFirm.userId", model.getVouchFirmId());
        param.addParam("status", 0);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        param.addParam("repaymentTime", Operators.LT, new Date());
        param.addParam("repaymentTime", Operators.GT, c.getTime());
        param.addPage(model.getPage(),model.getSize());
        PageDataList<BorrowRepayment> pageDataList = borrowRepaymentDao.findPageList(param);
        PageDataList<BorrowRepaymentModel> pageDateList = new PageDataList<BorrowRepaymentModel>();
        List<BorrowRepaymentModel> list = new ArrayList<BorrowRepaymentModel>();
        pageDateList.setPage(pageDataList.getPage());
        if (pageDataList.getList().size() > 0) {
            for (int i = 0; i < pageDataList.getList().size(); i++) {
                BorrowRepayment r = (BorrowRepayment) pageDataList.getList().get(i);
                BorrowRepaymentModel rm = BorrowRepaymentModel.instance(r);
                BorrowModel b = BorrowModel.instance(rm.getBorrow());
                rm.setUserName(b.getUser().getUserName());
                rm.setMobilePhone(b.getUser().getMobilePhone());
                b.setUser(null);
                rm.setBorrow(b.prototype());
                rm.setUser(null);
                rm.setRealRepayer(null);
                Calendar cld = Calendar.getInstance();
                int lastDays = (int) ((cld.getTime().getTime() - rm.getRepaymentTime().getTime()) / (1000 * 3600 * 24));
                rm.setLastDays(lastDays);
                list.add(rm);
            }
        }
        pageDateList.setList(list);
        return pageDateList;
    }

    @Override
    public void doRepaymentNotice() {
        String noticeTimeStr = DateUtil.dateStr2(DateUtil.rollDay(new Date(), 3));
        Date noticeTimeStart = DateUtil.valueOf(noticeTimeStr+" 00:00:00");
        Date noticeTimeEnd = DateUtil.valueOf(noticeTimeStr+" 23:59:59");
        QueryParam param = QueryParam.getInstance();
        param.addParam("repaymentTime", Operators.GTE, noticeTimeStart);
        param.addParam("repaymentTime", Operators.LTE, noticeTimeEnd);
        param.addParam("status", BorrowRepayment.STATUS_WAIT_REPAY);
        param.addParam("webStatus", BorrowRepayment.WEB_STATUS_NORMAL);
        List<BorrowRepayment> repayList = borrowRepaymentDao.findByCriteria(param);
        if(repayList != null && repayList.size() > 0){
            for(BorrowRepayment repay : repayList){
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("name", repay.getBorrow().getName());
//                map.put("period", repay.getPeriod());
//                map.put("repaymentTime", repay.getRepaymentTime());
//                map.put("repaymentAccount", repay.getRepaymentAccount());
//                Global.setTransfer("user", repay.getUser());
//                Global.setTransfer("repay", map);
//                AbstractExecuter repaySuccessExecuter = ExecuterHelper.doExecuter("borrowerRepayNoticeExecuter");
//                repaySuccessExecuter.execute(0, repay.getUser(), new User(Constant.ADMIN_ID));
                
                NoticeType smsType = Global.getNoticeType(NoticeConstant.REPAYMENT_SUPERVISION_NOTICE, NoticeConstant.NOTICE_SMS);
                String receiveAddr = Global.getValue("admin_receive_phone");
    			if(smsType.getSend() == 1 && StringUtil.isNotBlank(receiveAddr)) {
    				Map<String, Object> sendData = new HashMap<String, Object>();
    				sendData.put("repaymentTime", repay.getRepaymentTime());
    				sendData.put("repaymentAccount", repay.getRepaymentAccount());
    				sendData.put("name", repay.getBorrow().getName());
    				sendData.put("bidNo", repay.getBorrow().getBidNo());
    				sendData.put("period", repay.getPeriod());
    				Notice sms = new Notice();
    				sms.setType(NoticeConstant.NOTICE_SMS + "");
    				sms.setReceiveAddr(receiveAddr);
    				sms.setNid(NoticeConstant.REPAYMENT_SUPERVISION_NOTICE);
    				sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
    				noticeService.sendNotice(sms);
    			}
            } 
        }
    }

	@Override
	public void doLate() {
		List<BorrowRepayment> list = borrowRepaymentDao.doLate();
		if(list!=null && list.size()>0){
			for (BorrowRepayment borrowRepayment : list) {
				  // 计算逾期利息
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Date nowTime = null;
		        Date repaymentTime = null;
		        try {
		            nowTime = format.parse(DateUtil.dateStr2(new Date()));
		            repaymentTime = format.parse(DateUtil.dateStr2(borrowRepayment.getRepaymentTime()));
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }
		        long day = nowTime.getTime() - repaymentTime.getTime();
		        int days = (int) (day / (24 * 60 * 60 * 1000));
		        double lateInterest = 0;
		        logger.info("计算逾期利息repayment="+borrowRepayment.getId()+",逾期天数:"+days);
		        if (days > 0) {
		            String overdueFee = Global.getValue("overdue_fee");
		            double overdue = Double.parseDouble(overdueFee);
		            int FreeOverdueDay = Integer.parseInt(Global.getValue("free_overdue_day"));//？天内免逾期罚息
		            if (days <= FreeOverdueDay) {
		                double captial = borrowRepayment.getCapital();
		                lateInterest = BigDecimalUtil.mul(BigDecimalUtil.mul(captial + borrowRepayment.getInterest(), days),
		                        overdue); 
		            } else if (days > FreeOverdueDay) {
		                double capital = borrowRepaymentDao.getRemainderCapital(borrowRepayment.getBorrow().getId());
		                double interest = borrowRepaymentDao.getRemainderInterest(borrowRepayment.getBorrow().getId());
		                lateInterest = BigDecimalUtil.mul(BigDecimalUtil.mul(capital + interest, days), overdue); 
		            }
		            borrowRepayment.setLateDays(days);
		            borrowRepayment.setLateInterest(lateInterest);
		            borrowRepaymentDao.update(borrowRepayment);
		        }
			}
		}
	}

	@Override
	public PageDataList<BorrowRepaymentModel> repaymentEntrustList(
			BorrowRepaymentModel model, String searchName) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
    		if(StringUtil.isNotBlank(searchName)){
    			param.addParam("borrow.name", Operators.LIKE, searchName);
    		}
            if (model.getStatus() != 99) {
                param.addParam("status", model.getStatus());
            }
            if (StringUtil.isNotBlank(model.getStartTime())) {
                Date start = DateUtil.valueOf(model.getStartTime());
                param.addParam("repaymentTime", Operators.GTE, start);
            }
            if (StringUtil.isNotBlank(model.getEndTime())) {
                Date end = DateUtil.valueOf(model.getEndTime());
                param.addParam("repaymentTime", Operators.LTE, end);
            }
            if (StringUtil.isNotBlank(model.getBidNo())) {
            	param.addParam("borrow.bidNo", model.getBidNo());
            }
            param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.ASC, "repaymentTime");

		PageDataList<BorrowRepayment> pageDataList = borrowRepaymentDao.findPageList(param);
		PageDataList<BorrowRepaymentModel> pageDateList = new PageDataList<BorrowRepaymentModel>();
		List<BorrowRepaymentModel> list = new ArrayList<BorrowRepaymentModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowRepayment r = (BorrowRepayment) pageDataList.getList().get(i);
				BorrowRepaymentModel rm = BorrowRepaymentModel.instance(r);
				rm.setBorrowId(r.getBorrow().getId());
				rm.setBorrowName(r.getBorrow().getName());
				rm.setCompanyName(r.getBorrow().getCompanyName());
				rm.setUserName("admin");
				rm.setRealName("admin");
				rm.setMobilePhone("");
				rm.setTimeLimit(r.getBorrow().getTimeLimit());
				rm.setType(r.getBorrow().getType());
				rm.setBorrowStyle(r.getBorrow().getStyle()); // 设置还款方式
				rm.setBorrow(null);
				rm.setUser(null);
				rm.setPeriod(r.getPeriod()+1);
                rm.setLateDays(r.getLateDays());
                rm.setLateInterest(r.getLateInterest());
                rm.setBorrowType(r.getBorrow().getType());
                rm.setBidNo(r.getBorrow().getBidNo());
				list.add(rm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}

	@Override
	public double getRepaymentNoByDate(String date) {
		
		return borrowRepaymentDao.getRepaymentNoByDate(date);
	}

	@Override
	public double getRepaymentYesByDate(String date) {
		
		return borrowRepaymentDao.getRepaymentYesByDate(date);
	}

	@Override
	public void doAutoRepay() {
		List<BorrowRepayment> list = borrowRepaymentDao.autoRepayList();
		
		for (BorrowRepayment borrowRepayment : list) {
			borrowService.doRepay(borrowRepayment);
		}
	}
}

