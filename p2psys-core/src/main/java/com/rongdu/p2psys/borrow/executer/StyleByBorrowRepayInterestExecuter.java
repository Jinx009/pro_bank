package com.rongdu.p2psys.borrow.executer;

/**
 * 天标或还款方式=提前付息到期一次性还本
 * 
 * @author cx
 * @version 2.0
 * @since 2014-6-5
 */
public class StyleByBorrowRepayInterestExecuter extends BorrowRepayInterestExecuter {

	@Override
	public void handleAccount() {
		accountDao.modify(-super.money, -super.money, 0, 0, super.user.getUserId());
	}

}
