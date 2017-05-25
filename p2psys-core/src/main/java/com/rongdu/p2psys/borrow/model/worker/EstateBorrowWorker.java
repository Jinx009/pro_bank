package com.rongdu.p2psys.borrow.model.worker;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;

/**
 * 海外投资产品
 * @author sj
 * @since 2015年3月13日17:04:37
 *
 */
public class EstateBorrowWorker extends BaseBorrowWorker {

	public EstateBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		super(data, config, flag);
	}
	
}
