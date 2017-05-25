package com.rongdu.p2psys.borrow.model.worker;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;

/**
 * 固定收益类产品或者给力标
 * 
 * @author fuxingxing
 * @date 2012-9-5 下午5:18:32
 * @version <b>Copyright (c)</b> 2012-融都rongdu-版权所有<br/>
 */
public class MortgageBorrowWorker extends BaseBorrowWorker {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -965497211520156565L;

	public MortgageBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		super(data, config, flag);
	}

}
