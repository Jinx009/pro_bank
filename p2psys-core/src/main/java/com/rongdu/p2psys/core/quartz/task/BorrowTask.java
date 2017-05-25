package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.ips.service.IpsService;

/**
 * 标操作队列:还款，提前还款，撤回，截标等
 * @author zhangyz
 */
public class BorrowTask extends AbstractLoanTask {
    private Logger logger = Logger.getLogger(TenderTask.class);

    public BorrowTask() {
        super();
        task.setName("Borrow.Task");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doLoan() {

        JobQueue<ValueEvent> queue = JobQueue.getBorrowInstance();
        AutoBorrowService autoBorrowService = (AutoBorrowService) BeanUtil.getBean("autoBorrowService");
        ChinapnrService chinapnrService=(ChinapnrService)BeanUtil.getBean("chinapnrService");
        IpsService ipsService=(IpsService)BeanUtil.getBean("ipsService");
        while (!queue.isEmpty()) {
            ValueEvent event = queue.peek();
            if (event != null) {
                String result = "success";
                try {
                    if ("repay".equals(event.getOperate())) { // 还款
                        // 汇付
                        if(BaseTPPWay.isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_PNR){
                            chinapnrService.repay(event.getBorrowRepayment());
                        }else {
                            autoBorrowService.autoBorrowRepay(event.getBorrowRepayment());
                        }
                    }else if("vipRepay".equals(event.getOperate())){
                    	chinapnrService.vipRepay();
                    }else if ("autoCancel".equals(event.getOperate())) {//管理员撤标
                          autoBorrowService.autoCancel(event.getBorrow());
                    }else if ("doPriorRepay".equals(event.getOperate())) {
                        //提前还款
                        autoBorrowService.doPriorRepay(event.getBorrowRepayment());
                    }  else if ("overdue".equals(event.getOperate())) {
                        //后台逾期垫付
                        autoBorrowService.overdue(event.getBorrowRepayment());
                    }  else if ("overduePayment".equals(event.getOperate())) {
                        //前台逾期垫付
                        autoBorrowService.overduePayment(event.getBorrowRepayment());
                    }else if ("doAddBorrow".equals(event.getOperate())) {
                        //环讯发标回调处理
                        ipsService.doAddBorrow(event.getBorrowModel());
                    }else if ("doCompensateSuccess".equals(event.getOperate())) {
                        //环讯代偿回调处理
                        autoBorrowService.autoCompensateSuccess(event.getBorrowRepayment());
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
        return BorrowTask.BORROW_STATUS;
    }

}
