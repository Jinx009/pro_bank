package com.rongdu.p2psys.core.concurrent;

import org.apache.log4j.Logger;

import com.lmax.disruptor.EventHandler;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.user.service.UserCacheService;

public class CEventHandler implements EventHandler<ValueEvent> {
	Logger logger = Logger.getLogger(CEventHandler.class);

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) throws Exception {
	    String result = "success";
        boolean isSystemMessage = false;
        AutoBorrowService autoBorrowService = (AutoBorrowService) BeanUtil.getBean("autoBorrowService");
	    try {
			AccountCashService accountCashService = (AccountCashService) BeanUtil.getBean("accountCashService");
			AccountService accountService = (AccountService) BeanUtil.getBean("accountService");
			BorrowTenderService borrowTenderService = (BorrowTenderService)BeanUtil.getBean("borrowTenderService");
			ChinapnrService chinapnrService=(ChinapnrService)BeanUtil.getBean("chinapnrService");
			
			// 
			if("apiUserRegister".equals(event.getOperate())){
			    isSystemMessage = true;
				//汇付回调的处理
			    chinapnrService.apiUserRegister(event.getUser());
			//投标
			}else if("tender".equals(event.getOperate())){
				 isSystemMessage = true;
				 borrowTenderService.addTender(event.getBorrow(),event.getBorrowModel());
				// 还款
			}else if ("repay".equals(event.getOperate())) {
			    isSystemMessage = true;
			    // 汇付
			    if(BaseTPPWay.isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_PNR){
			    	chinapnrService.repay(event.getBorrowRepayment());
			    }else {
			    	autoBorrowService.autoBorrowRepay(event.getBorrowRepayment());
			    }
				// 自动投标
			} else if ("autoTender".equals(event.getOperate())) {
			    isSystemMessage = true;
				autoBorrowService.autoDealTender(event.getBorrowModel());
				// 满标复审
			} else if ("autoVerifyFull".equals(event.getOperate())) {
                isSystemMessage = true;
                autoBorrowService.autoVerifyFull(event.getBorrowModel());
                // 复审通过
            } else if ("autoVerifyFullSuccess".equals(event.getOperate())) {
			    isSystemMessage = true;
		    	//autoBorrowService.autoVerifyFullSuccess(event.getBorrowModel());
			    chinapnrService.fullSuccess(event.getBorrowModel());
				// 复审不通过
			} else if ("autoVerifyFullFail".equals(event.getOperate())) {
			    isSystemMessage = true;
		    	autoBorrowService.autoVerifyFullFail(event.getBorrowModel());
			    chinapnrService.fullFail(event.getBorrowModel());
				//管理员撤标
			} else if ("autoCancel".equals(event.getOperate())) {
			    isSystemMessage = true;
			    // 汇付
			    if(BaseTPPWay.isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_PNR){
			    	chinapnrService.cancelBorrow(event.getBorrow());
			    }else {
			    	autoBorrowService.autoCancel(event.getBorrow());
			    }
			} else if ("verifyCashBack".equals(event.getOperate())) {
			    isSystemMessage = true;
				//提现回调全部处理
				accountCashService.cashCallBack(event.getCashModel());
				
			} else if ("verifyRecharge".equals(event.getOperate())) {
			    isSystemMessage = true;
				//充值回调全部处理
				accountService.doRechargeTask(event.getRechargeModel());
			} else if ("doPriorRepay".equals(event.getOperate())) {
			    isSystemMessage = true;
				//提前还款
				autoBorrowService.doPriorRepay(event.getBorrowRepayment());
			}  else if ("overdue".equals(event.getOperate())) {
			    isSystemMessage = true;
				//后台逾期垫付
				autoBorrowService.overdue(event.getBorrowRepayment());
			}  else if ("overduePayment".equals(event.getOperate())) {
			    isSystemMessage = true;
				//前台逾期垫付
				autoBorrowService.overduePayment(event.getBorrowRepayment());
			} else if ("ipsRegister".equals(event.getOperate())) {
			    isSystemMessage = true;
				//环讯回调的处理
				UserCacheService userCacheService = (UserCacheService) BeanUtil.getBean("userCacheService");
				userCacheService.ipsRegisterCall(event.getUser(), event.getIpsRegister());
			} else if ("doAddBorrow".equals(event.getOperate())) {
			    isSystemMessage = true;
				//环讯发标回调处理
				IpsService ipsService=(IpsService)BeanUtil.getBean("ipsService");
				ipsService.doAddBorrow(event.getBorrowModel());
			}else if ("doIpsRegisterGuarantor".equals(event.getOperate())) {
                isSystemMessage = true;
                //环讯发标回调处理
                IpsService ipsService=(IpsService)BeanUtil.getBean("ipsService");
                ipsService.doIpsRegisterGuarantor(event.getBorrowModel());
            }else if ("doAddTender".equals(event.getOperate())) {
			    isSystemMessage = true;
				//投标回调处理
			    // 易极付
				if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				// 环讯
				}else if(TPPWay.API_CODE == TPPWay.API_CODE_IPS){
					IpsService ipsService=(IpsService)BeanUtil.getBean("ipsService");
					ipsService.doAddTender(event.getBorrowModel());
				// 汇付
				}else if(TPPWay.API_CODE == TPPWay.API_CODE_PNR){
					chinapnrService.addTender(event.getBorrowModel());
				}
			}else if ("doCompensateSuccess".equals(event.getOperate())) {
                isSystemMessage = true;
                //环讯代偿回调处理
                autoBorrowService.autoCompensateSuccess(event.getBorrowRepayment());
            }
		} catch (Exception e) {
			logger.info("exception="+e.getMessage());
			if(e instanceof com.rongdu.common.exception.BussinessException ){
                result = e.getMessage();
            }else{
                result = "系统异常，操作失败！！！";
            }	
			if("autoVerifyFullSuccess".equals(event.getOperate())){ //复审处理失败状态改为1 重新复审
				autoBorrowService.updateStatus(event.getBorrowModel().getId(), 1, 3);
			}
		}
	    if(isSystemMessage && event.getResultFlag()!=null){//在需要保存系统处理信息的地方直接保存进来
            Global.RESULT_MAP.put(event.getResultFlag(), result);
        }
	}
}
