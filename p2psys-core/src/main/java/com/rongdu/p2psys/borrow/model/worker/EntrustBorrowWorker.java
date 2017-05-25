package com.rongdu.p2psys.borrow.model.worker;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;

/**
 * 浮动收益类产品
 * @author sj
 * @since 2015年3月18日17:04:37
 *
 */
public class EntrustBorrowWorker extends BaseBorrowWorker {

	public EntrustBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		super(data, config, flag);
	}
	
}
