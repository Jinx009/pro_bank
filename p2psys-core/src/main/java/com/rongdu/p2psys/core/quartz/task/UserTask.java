package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.user.service.UserCacheService;

public class UserTask extends AbstractLoanTask {
    private Logger logger = Logger.getLogger(TenderTask.class);

    public UserTask() {
        super();
        task.setName("User.Task");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doLoan() {

        JobQueue<ValueEvent> queue = JobQueue.getUserInstance();
        UserCacheService userCacheService = (UserCacheService) BeanUtil.getBean("userCacheService");
        ChinapnrService chinapnrService=(ChinapnrService)BeanUtil.getBean("chinapnrService");
        IpsService ipsService=(IpsService)BeanUtil.getBean("ipsService");
        while (!queue.isEmpty()) {
            ValueEvent event = queue.peek();
            if (event != null) {
                String result = "success";
                try {
                    if ("ipsRegister".equals(event.getOperate())) {
                        //环讯开户
                        userCacheService.ipsRegisterCall(event.getUser(), event.getIpsRegister());
                    }else if("apiUserRegister".equals(event.getOperate())){
                        //汇付开户回调的处理
                        chinapnrService.apiUserRegister(event.getUser());
                    }else if ("doIpsRegisterGuarantor".equals(event.getOperate())) {
                        //环讯担保开户回调处理
                        ipsService.doIpsRegisterGuarantor(event.getBorrowModel());
                    }else if("apiCorpRegister".equals(event.getOperate())){
                    	//汇付企业开户回调处理
                    	chinapnrService.apiCorpRegister(event.getUser(), event.getCorpRegister());
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
        return UserTask.USER_STATUS;
    }

}