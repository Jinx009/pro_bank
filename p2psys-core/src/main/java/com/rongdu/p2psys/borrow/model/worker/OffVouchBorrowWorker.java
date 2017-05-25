package com.rongdu.p2psys.borrow.model.worker;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;

/**
 * 助学贷款
 * 
 * @author fuxingxing
 * @date 2012-11-6 下午3:17:07
 * @version <b>Copyright (c)</b> 2012-融都rongdu-版权所有<br/>
 */
public class OffVouchBorrowWorker extends BaseBorrowWorker {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -965497211520156565L;

	public OffVouchBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		super(data, config, flag);
	}

}
