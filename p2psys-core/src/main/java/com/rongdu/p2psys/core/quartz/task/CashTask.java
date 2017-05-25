package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 用户充值提现队列
 * @author zhangyz
 *
 */
public class CashTask extends AbstractLoanTask {
    private Logger logger = Logger.getLogger(TenderTask.class);

    public CashTask() {
        super();
        task.setName("Cash.Task");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doLoan() {

        JobQueue<ValueEvent> queue = JobQueue.getCashInstance();
        AccountCashService accountCashService = (AccountCashService) BeanUtil.getBean("accountCashService");
        AccountService accountService = (AccountService) BeanUtil.getBean("accountService");
        while (!queue.isEmpty()) {
            ValueEvent event = queue.peek();
            if (event != null) {
                String result = "success";
                try {
                    if ("verifyCashBack".equals(event.getOperate())) {
                        //提现回调全部处理
                        accountCashService.cashCallBack(event.getCashModel());
                        
                    } else if ("verifyRecharge".equals(event.getOperate())) {
                        //充值回调全部处理
                        accountService.doRechargeTask(event.getRechargeModel());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    if (e instanceof BussinessException) {// 业务异常，保存业务处理信息
                        result = e.getMessage();
                    } else {
                        result = "系统异常，业务处理失败";
                    }
                } finally {
                    queue.remove(event);
                }
                if (event.getResultFlag() != null) {// 在需要保存系统处理信息的地方直接保存进来
                    Global.RESULT_MAP.put(event.getResultFlag(), result);
                }
            }
        }

    }

    @Override
    public Object getLock() {
        return CashTask.CASH_STATUS;
    }

}