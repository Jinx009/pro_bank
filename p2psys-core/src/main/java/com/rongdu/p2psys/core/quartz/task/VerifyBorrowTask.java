package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;

/**
 * 标相关审核处理
 * @author zhangyz
 */
public class VerifyBorrowTask extends AbstractLoanTask {
    private Logger logger = Logger.getLogger(VerifyBorrowTask.class);

    public VerifyBorrowTask() {
        super();
        task.setName("VerifyBorrow.Task");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doLoan() {

        JobQueue<ValueEvent> queue = JobQueue.getVerifyBorrowInstance();
        AutoBorrowService autoBorrowService = (AutoBorrowService) BeanUtil.getBean("autoBorrowService");
        ChinapnrService chinapnrService=(ChinapnrService)BeanUtil.getBean("chinapnrService");
        while (!queue.isEmpty()) {
            ValueEvent event = queue.peek();
            if (event != null) {
                String result = "success";
                try {
                    if ("autoVerifyFull".equals(event.getOperate())) {// 满标复审
                        autoBorrowService.autoVerifyFull(event.getBorrowModel());
                    } else if ("autoVerifyFullSuccess".equals(event.getOperate())) {// 复审通过
                    	   chinapnrService.fullSuccess(event.getBorrowModel());
                           //autoBorrowService.autoVerifyFullSuccess(event.getBorrowModel());
                    }else if ("autoVerifyFullFail".equals(event.getOperate())) {// 复审不通过
                           //autoBorrowService.autoVerifyFullFail(event.getBorrowModel());
                           chinapnrService.fullFail(event.getBorrowModel());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    if (e instanceof BussinessException) {// 业务异常，保存业务处理信息
                        result = e.getMessage();
                    } else {
                        result = "系统异常，业务处理失败";
                    }
                    if("autoVerifyFullSuccess".equals(event.getOperate())){ //复审处理失败状态改为1 重新复审
                        autoBorrowService.updateStatus(event.getBorrowModel().getId(), 1, 3);
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
        return VerifyBorrowTask.VERIFY_BORROW_STATUS;
    }

}
