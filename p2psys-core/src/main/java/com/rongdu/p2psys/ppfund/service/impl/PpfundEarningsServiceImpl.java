package com.rongdu.p2psys.ppfund.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.ppfund.dao.PpfundEarningsDao;
import com.rongdu.p2psys.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundEarnings;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.service.PpfundEarningsService;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
@Service("ppfundEarningsService")
public class PpfundEarningsServiceImpl implements PpfundEarningsService {
	private Logger logger = Logger.getLogger(PpfundEarningsServiceImpl.class);
	@Resource
	private PpfundInDao ppfundInDao;
	@Resource
	private PpfundEarningsDao ppfundEarningsDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private ProductBasicService productBasicService;

	@Override
	public void doPpfundProfit() {
		// 获取到所有未转出的投资记录
		List<PpfundIn> list = ppfundInDao.getlist(0);
		// 处理每日收益
		if (list != null && list.size() > 0) {
			for (PpfundIn ppfundIn : list) {
				Ppfund ppfund = ppfundIn.getPpfund();
				User user = ppfundIn.getUser();
				Date startTime = ppfundIn.getAddTime();

				// 判断计息方式，T+?,?代表interestWay
				int interestWay = ppfund.getInterestWay();
				Date date = DateUtil.addDate(startTime, interestWay);
				logger.info("计息方式:T+" + ppfund.getInterestWay() + "时间:" + date);
				if (new Date().after(date)) {
					// 计算收益
					// 保存收益总额,由于小数位存在误差，具体计算还是以持有天数、年化率、金额为准
					// 计算持有天数
					int days = DateUtil.daysBetween(ppfundIn.getAddTime(),
							new Date());
					if (ppfund.getTimeLimit() > 0
							&& ppfund.getTimeLimit() < days) {
						days = ppfund.getTimeLimit();
					}
					double totalInterest = BigDecimalUtil.round(BigDecimalUtil
							.mul(ppfundIn.getInterestAmount(),
									BigDecimalUtil.div(ppfund.getApr(), 36500),
									days));
//					double sumInterest = ppfundEarningsDao
//							.getUserEarningsSum(ppfundIn);
					
					double sumInterest = BigDecimalUtil.round(BigDecimalUtil
							.mul(ppfundIn.getInterestAmount(),
									BigDecimalUtil.div(ppfund.getApr(), 36500),
									days-1));
					double interest = BigDecimalUtil.round(totalInterest
							- sumInterest);
					double dayInterest = BigDecimalUtil
							.round(ppfundIn.getInterestAmount()
									* BigDecimalUtil
											.div(ppfund.getApr(), 36500),
									4);
					PpfundEarnings earnings = new PpfundEarnings(user,
							ppfundIn, dayInterest);
					ppfundEarningsDao.save(earnings);

					ppfundIn.setInterest(totalInterest);
					// ppfundIn.setInterest(BigDecimalUtil.add(ppfundIn.getInterest(),
					// interest));
					ppfundInDao.update(ppfundIn);

					// 生成收益待收日志
					if (ppfundIn.getInterest() > 0) {
						accountDao.modify(interest, 0, 0, interest,
								user.getUserId());
						Global.setTransfer("user", user);
						Global.setTransfer("ppfund", ppfund);
						Global.setTransfer("money", interest);
						ProductBasic productBasic = productBasicService
								.getProductBasicInfo(ppfund.getProductType()
										.getId(), ppfund.getId());
						Global.setTransfer("flag",
								productBasic.getProductTypeFlag());
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("ppfundCollectionInterestExecuter");
						executer.execute(interest, user, new User(1));
					}
				}
			}
		}
	}
}
