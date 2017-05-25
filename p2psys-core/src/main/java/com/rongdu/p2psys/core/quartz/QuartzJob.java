package com.rongdu.p2psys.core.quartz;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountRecordeService;
import com.rongdu.p2psys.account.service.CompCashService;
import com.rongdu.p2psys.account.service.TppGlodLogService;
import com.rongdu.p2psys.bond.service.BondService;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.ppfund.service.PpfundEarningsService;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserVipService;
@Component
@Lazy(value=false)
public class QuartzJob {
	private Logger logger = Logger.getLogger(QuartzJob.class);
	@Resource
    private AccountCashService accountCashService;
	@Resource
    private AccountRechargeService accountRechargeService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private BondService bondService;
	@Resource
	private TppGlodLogService tppGlodLogService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private PpfundEarningsService ppfundEarningsService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private UserService userService;
	@Resource
	private CompCashService compCashService;
	@Resource
	private AccountRecordeService accountRecordeService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private OrderService cfOrderService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	 
	
    /**
     * 众筹预约过期
     * @throws Exception 
     */
    @Scheduled(cron = "*/5 * * * * ?")//每隔5秒执行一次
    public void doCfOrder() throws Exception {
    	cfOrderService.changeType();
    }
	
    /**
     * 众筹产品过期
     * @throws Exception 
     */
    @Scheduled(cron = "*/5 * * * * ?")//每隔5秒执行一次
    public void doProject() throws Exception {
    	projectBaseinfoService.changeType();
    }
    
    @Scheduled(cron = "0 59 23 * * ?")//每天凌晨0点0分
    public void doAccountRecorde() {
    	logger.info("开始统计平台资金汇总");
    	accountRecordeService.doStatistics();
    }
    
    

	/**
	 * 修改充值状态
	 */
	@Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点整
    public void setAccountRecharge() {
	    accountRechargeService.setAccountRecharge();
    }
	
	/**
	 * 修改平台资金操作状态
	 */
	@Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点整
    public void setTppGlodLog() {
	    tppGlodLogService.setTppGlodLogStatus();
    }
	
   
}
