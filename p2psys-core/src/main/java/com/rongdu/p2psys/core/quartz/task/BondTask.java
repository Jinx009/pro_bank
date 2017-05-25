package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.bond.service.BondTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;

/**
 * 债权队列
 * @author zhangyz
 */
public class BondTask extends AbstractLoanTask {
    private Logger logger = Logger.getLogger(BondTask.class);

    public BondTask() {
        super();
        task.setName("Bond.Task");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doLoan() {

        JobQueue<ValueEvent> queue = JobQueue.getBondInstance();
        BondTenderService bondTenderService = (BondTenderService) BeanUtil.getBean("bondTenderService");
        ChinapnrService chinapnrService = (ChinapnrService)BeanUtil.getBean("chinapnrService");
        
        while (!queue.isEmpty()) {
            ValueEvent event = queue.peek();
            if (event != null) {
                String result = "success";
                try {
                	// 汇付
                    if(BaseTPPWay.isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_PNR){
                        chinapnrService.addBondTender(event.getBondModel());
                    }else {
                    	bondTenderService.addBondTender(event.getBondModel());
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
        return BondTask.BOND_STATUS;
    }

}
