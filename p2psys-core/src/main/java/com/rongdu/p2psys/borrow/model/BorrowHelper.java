package com.rongdu.p2psys.borrow.model;

import com.rongdu.p2psys.borrow.dao.BorrowConfigDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.model.worker.BaseBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.CreditBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.EntrustBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.EstateBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.FlowBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.MortgageBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.OffVouchBorrowWorker;
import com.rongdu.p2psys.borrow.model.worker.SecondBorrowWorker;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 生成标工厂类
 */
public final class BorrowHelper {
	/**
	 * 构造函数私有化
	 */
	private BorrowHelper() {

	}

	/**
	 * 根据标属性，生成对应的标Worker对象
	 * 
	 * @param borrow
	 * @return BorrowWorker
	 */
	public static BorrowWorker getWorker(Borrow borrow) {
		BorrowConfigDao borrowConfigDao = (BorrowConfigDao) BeanUtil
				.getBean("borrowConfigDao");
		BorrowConfig cfg = borrowConfigDao.find(new Long(borrow.getType()));

		// 判断是否是发标或复审或投标
		boolean flag = false;
		switch (borrow.getType()) {
		case Borrow.TYPE_SECOND: // 101:秒标
			borrow.setTimeLimit(1);
			return new SecondBorrowWorker(borrow, cfg);
		case Borrow.TYPE_CREDIT:// 102:信用标
			if (borrow.getStatus() == 0 || borrow.getStatus() == 3
					|| borrow.getScales() == -1) {
				flag = true;
			}
			return new CreditBorrowWorker(borrow, cfg, flag);
		case Borrow.TYPE_MORTGAGE:// 103:固定收益
			if (borrow.getStatus() == 0 || borrow.getStatus() == 3
					|| borrow.getScales() == -1) {
				flag = true;
			}
			return new MortgageBorrowWorker(borrow, cfg, flag);
		case Borrow.TYPE_FLOW:// 110:流转标
			if (borrow.getStatus() == 0 || borrow.getStatus() == 3
					|| borrow.getScales() == -1) {
				flag = true;
			}
			return new FlowBorrowWorker(borrow, cfg, flag);
		case Borrow.TYPE_OFFVOUCH:// 112:担保标
			if (borrow.getStatus() == 0 || borrow.getStatus() == 3
					|| borrow.getScales() == -1) {
				flag = true;
			}
			return new OffVouchBorrowWorker(borrow, cfg, flag);
		case Borrow.TYPE_ESTATE:// 119:海外投资
			if (borrow.getStatus() == 0 || borrow.getStatus() == 3
					|| borrow.getScales() == -1) {
				flag = true;
			}
			return new EstateBorrowWorker(borrow, cfg, flag);
		case Borrow.TYPE_ENTRUST:// 122:浮动收益类
			if (borrow.getStatus() == 0 || borrow.getStatus() == 3
					|| borrow.getScales() == -1) {
				flag = true;
			}
			return new EntrustBorrowWorker(borrow, cfg, flag);
		default:
			return new BaseBorrowWorker(borrow, cfg);
		}
	}

}
