package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;

/**
 * 还款提醒
 * @author zhangyz
 */
public class BorrowerRepayNoticeExecuter extends BaseExecuter {

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    public void addAccountLog() {
    }

    @Override
    public void handleAccount() {
    }

    @Override
    public void handleAccountSum() {

    }

    @Override
    public void handlePoints() {

    }

    @Override
    public void handleNotice() {
        BaseMsg msg = new BaseMsg(NoticeConstant.BORROWER_REPAY_NOTICE);
        msg.doEvent();
    }

    @Override
    public void addOperateLog() {

    }

    @Override
    public void handleInterface() {

    }

    @Override
    public void extend() {

    }
}
