package com.rongdu.p2psys.borrow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.user.domain.User;

@Service("borrowCollectionService")
public class BorrowCollectionServiceImpl implements BorrowCollectionService {

	@Resource
	private BorrowCollectionDao borrowCollectionDao;
	@Resource
	private ProductBasicService productBasicService;

	@Override
	public void save(List<BorrowCollection> list) {
		borrowCollectionDao.save(list);
	}

	@Override
	public double getReceivedInterestSum(long userId) {
		return borrowCollectionDao.getReceivedInterestSum(userId);
	}

	/**
	 * 根据标获取用户的待收
	 * 
	 * @param tender
	 *            投标类
	 * @return List<BorrowCollection>
	 */
	@Override
	public List<BorrowCollection> getListByUserAndBorrow(BorrowTender tender) {
		QueryParam param = QueryParam.getInstance().addParam("tender.id",
				tender.getId());
		return borrowCollectionDao.findPageList(param).getList();
	}

	@Override
	public PageDataList<BorrowCollectionModel> list(BorrowCollectionModel model) {
		PageDataList<BorrowCollection> pList = borrowCollectionDao
				.getList(model);
		PageDataList<BorrowCollectionModel> modelList = new PageDataList<BorrowCollectionModel>();
		List<BorrowCollectionModel> list = new ArrayList<BorrowCollectionModel>();
		modelList.setPage(pList.getPage());
		if (pList.getList().size() > 0) {
			for (int i = 0; i < pList.getList().size(); i++) {
				BorrowCollection c = (BorrowCollection) pList.getList().get(i);
				BorrowCollectionModel cm = BorrowCollectionModel.instance(c);
				cm.setBorrowId(c.getBorrow().getId());
				cm.setBorrowName(c.getBorrow().getName());
				cm.setTimeLimit(c.getBorrow().getTimeLimit());
				cm.setBorrowStyle(c.getBorrow().getStyle());
				cm.setUserName(c.getBorrow().getUser().getUserName());
				cm.setInvestUserName(c.getUser().getUserName());
				cm.setInvestRealName(c.getUser().getRealName());
				cm.setPeriod(c.getPeriod() + 1);
				// cm.setActualInterest(BigDecimalUtil.sub(c.getInterest(),
				// c.getManageFee()));
				cm.setInterest(BigDecimalUtil.sub(c.getInterest(),
						c.getBondInterest()));
				cm.setActualInterest(BigDecimalUtil.sub(BigDecimalUtil.sub(
						c.getInterest(), c.getBondInterest()), c.getManageFee()));
				// cm.setActualInterest(BigDecimalUtil.add(BigDecimalUtil.sub(c.getInterest(),
				// c.getBondInterest()),c.getInterestRate()));
				cm.setCapital(BigDecimalUtil.sub(c.getCapital(),
						c.getBondCapital()));
				cm.setRepaymentAccount(BigDecimalUtil.sub(
						BigDecimalUtil.add(c.getCapital(), c.getInterest()),
						BigDecimalUtil.add(c.getBondCapital(),
								c.getBondInterest())));
				cm.setBidNo(c.getBorrow().getBidNo());
				cm.setUser(null);
				// 获取分类ID（flag_id）
				cm.setFlagId(productBasicService
						.getProductBasicInfo(new Long(c.getBorrow().getType()),
								c.getBorrow().getId()).getProductTypeFlag()
						.getId());
				list.add(cm);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public BorrowCollection getNextCollectionByUserId(long userId) {
		return borrowCollectionDao.getNextCollectionByUserId(userId);
	}

	@Override
	public int countCollect(long userId, int status) {
		return borrowCollectionDao.countCollect(userId, status);
	}

	@Override
	public BorrowCollectionModel getCollectStatistics(long userId) {
		BorrowCollectionModel bm = borrowCollectionDao
				.getCollectStatistics(userId);
		BorrowCollection bc = borrowCollectionDao
				.getNextCollectionByUserId(userId);
		if (bc != null) {
			bm.setNextCollectTime(bc.getRepaymentTime());
			bm = borrowCollectionDao.getBCMByCollectTime(bm,
					bc.getRepaymentTime(), userId);
		}
		double jan = borrowCollectionDao.getCollectByMonth(1, userId);
		double feb = borrowCollectionDao.getCollectByMonth(2, userId);
		double mar = borrowCollectionDao.getCollectByMonth(3, userId);
		double apr = borrowCollectionDao.getCollectByMonth(4, userId);
		double may = borrowCollectionDao.getCollectByMonth(5, userId);
		double jun = borrowCollectionDao.getCollectByMonth(6, userId);
		double jul = borrowCollectionDao.getCollectByMonth(7, userId);
		double aug = borrowCollectionDao.getCollectByMonth(8, userId);
		double sep = borrowCollectionDao.getCollectByMonth(9, userId);
		double oct = borrowCollectionDao.getCollectByMonth(10, userId);
		double nov = borrowCollectionDao.getCollectByMonth(11, userId);
		double dec = borrowCollectionDao.getCollectByMonth(12, userId);
		bm.getCollectData().put("jan", jan);
		bm.getCollectData().put("feb", feb);
		bm.getCollectData().put("mar", mar);
		bm.getCollectData().put("apr", apr);
		bm.getCollectData().put("may", may);
		bm.getCollectData().put("jun", jun);
		bm.getCollectData().put("jul", jul);
		bm.getCollectData().put("aug", aug);
		bm.getCollectData().put("sep", sep);
		bm.getCollectData().put("oct", oct);
		bm.getCollectData().put("nov", nov);
		bm.getCollectData().put("dec", dec);
		return bm;
	}

	@Override
	public List<BorrowCollectionModel> investCollectionList(User user) {
		List<BorrowCollection> pList = borrowCollectionDao
				.getMemberCollectionList(user);
		List<BorrowCollectionModel> modelList = new ArrayList<BorrowCollectionModel>();
		if (pList.size() > 0) {
			for (int i = 0; i < pList.size(); i++) {
				BorrowCollection c = pList.get(i);
				BorrowCollectionModel cm = BorrowCollectionModel.instance(c);
				cm.setBorrowName(c.getBorrow().getName());
				cm.setTimeLimit(c.getBorrow().getTimeLimit());
				cm.setBorrowStyle(c.getBorrow().getStyle());
				if (c.getBorrow().getUser() != null) {
					cm.setUserName(c.getBorrow().getUser().getUserName());
				}
				cm.setActualInterest(BigDecimalUtil.sub(c.getInterest(),
						c.getManageFee()));
				cm.setUser(null);
				modelList.add(cm);
			}
		}
		return modelList;
	}

	@Override
	public double accumulatedNetIncome(User user) {
		return borrowCollectionDao.accumulatedNetIncome(user);
	}

	public double netProfit(User user) {
		return borrowCollectionDao.netProfit(user);
	}

	@Override
	public double inInvestAmount(User user, int status) {
		return borrowCollectionDao.inInvestAmount(user, status);
	}

	@Override
	public double sumTodayInterest(User user) {
		return borrowCollectionDao.sumTodayInterest(user);
	}

	@Override
	public double getInterestByUser(User user) {
		return borrowCollectionDao.getInterestByUser(user);
	}

	@Override
	public List<Object[]> getInterestByUserAndDate(User user, String date) {
		return borrowCollectionDao.getInterestByUserAndDate(user, date);
	}

	@Override
	public double getCapitalByUser(User user) {
		return borrowCollectionDao.getCapitalByUser(user);
	}

	@Override
	public List<Object[]> getCapitalByUserAndDate(User user, String date) {
		return borrowCollectionDao.getCapitalByUserAndDate(user, date);
	}

	@Override
	public List<String> getCollectionDate(User user) {
		return borrowCollectionDao.getCollectionDate(user);
	}

	@Override
	public double sumInterest() {
		return borrowCollectionDao.sumInterest();
	}

	@Override
	public double sumInterestFeeByTender(BorrowTender t) {
		return borrowCollectionDao.sumInterestFeeByTender(t);
	}

	@Override
	public PageDataList<BorrowCollectionModel> getRepayPlanByModel(
			BorrowTenderModel model) {
		Date nowdate = new Date();
		QueryParam param = QueryParam.getInstance();
		param.addParam("user", model.getUser());
		param.addParam("status", model.getStatus());
		param.addParam("repaymentTime", Operators.NOTEQ, SearchFilter.NULL);

		// 如果此条记录为债权全部转出则不显示记录
		// 只判断利息是否全部转出
		param.addParam("interest", Operators.PROPERTY_NOTEQ, "bondInterest");

		if (StringUtil.isNotBlank(model.getStartTime())) {
			param.addParam("repaymentTime", Operators.GT,
					DateUtil.valueOf(model.getStartTime()));
		}
		if (StringUtil.isNotBlank(model.getEndTime())) {
			Date date = DateUtil.valueOf(model.getEndTime());
			param.addParam("repaymentTime", Operators.LT,
					DateUtil.rollDay(date, 1));
		}
		if (model.getTime() == 7) {
			param.addParam("repaymentTime", Operators.GTE,
					DateUtil.rollDay(nowdate, -7));
			param.addParam("repaymentTime", Operators.LTE, nowdate);
		} else if (model.getTime() > 0 && model.getTime() < 4) {
			param.addParam("repaymentTime", Operators.GTE,
					DateUtil.rollMon(nowdate, -model.getTime()));
			param.addParam("repaymentTime", Operators.LTE, nowdate);
		}
		param.addPage(model.getPage(), model.getSize());
		PageDataList<BorrowCollection> pageList = borrowCollectionDao
				.findPageList(param);
		List<BorrowCollectionModel> modelList = new ArrayList<BorrowCollectionModel>();
		PageDataList<BorrowCollectionModel> pageDataList = new PageDataList<BorrowCollectionModel>();
		for (BorrowCollection bc : pageList.getList()) {
			BorrowCollectionModel bcm = BorrowCollectionModel.instance(bc);
			Borrow borrow = bc.getBorrow();
			bcm.setStartDate(borrow.getReviewTime());
			bcm.setBorrowStyle(borrow.getBorrowTimeType());
			bcm.setApr(borrow.getApr());
			bcm.setBorrowName(borrow.getName());
			bcm.setBorrowId(borrow.getId());
			bcm.setBorrowStyle(borrow.getStyle());
			int timeLimit = 1;
			if (borrow.getStyle() != Borrow.STYLE_ONETIME_REPAYMENT) {
				timeLimit = borrow.getTimeLimit();
			}
			bcm.setTimeLimit(timeLimit);
			bcm.setTotalCapital(borrowCollectionDao.getTotalCapitalByTenderId(
					bc.getTender().getId(), model.getUser().getUserId()));
			bcm.setUser(null);
			bcm.setBorrow(null);
			modelList.add(bcm);
		}
		pageDataList.setList(modelList);
		pageDataList.setPage(pageList.getPage());
		return pageDataList;
	}

	@Override
	public PageDataList<BorrowCollectionModel> getCurrentRepayPlanByModel(
			BorrowTenderModel model) {
		List<BorrowCollection> list = borrowCollectionDao
				.getCurrentRepayPlanByModel(model);
		List<BorrowCollectionModel> modelList = new ArrayList<BorrowCollectionModel>();
		PageDataList<BorrowCollectionModel> pageDataList = new PageDataList<BorrowCollectionModel>();
		for (BorrowCollection bc : list) {
			BorrowCollectionModel bcm = BorrowCollectionModel.instance(bc);
			Borrow borrow = bc.getBorrow();
			bcm.setStartDate(borrow.getReviewTime());
			bcm.setBorrowStyle(borrow.getBorrowTimeType());
			bcm.setApr(borrow.getApr());
			bcm.setBorrowName(borrow.getName());
			bcm.setBorrowId(borrow.getId());
			bcm.setBorrowStyle(borrow.getStyle());
			int timeLimit = 1;
			if (borrow.getStyle() != Borrow.STYLE_ONETIME_REPAYMENT) {
				timeLimit = borrow.getTimeLimit();
			}
			bcm.setTimeLimit(timeLimit);
			bcm.setPeriod(bcm.getPeriod() + 1);
			bcm.setTotalCapital(borrowCollectionDao.getTotalCapitalByTenderId(
					bc.getTender().getId(), model.getUser().getUserId()));
			bcm.setRepaymentAccount(BigDecimalUtil.add(
					bcm.getRepaymentAccount(), -bcm.getBondCapital(),
					-bcm.getBondInterest()));
			bcm.setUser(null);
			bcm.setBorrow(null);
			modelList.add(bcm);
		}
		pageDataList.setPage(new Page(model.getPage(), model.getSize()));
		pageDataList.setList(modelList);
		return pageDataList;
	}

	@Override
	public Object[] getBorrowCollectionList(long tenderId) {
		return borrowCollectionDao.getBorrowCollectionList(tenderId);
	}

	@Override
	public double getRepaymentYesInterest(String startTime, String endTime) {
		return borrowCollectionDao.getRepaymentYesInterest(startTime, endTime);
	}
}
