package com.rongdu.p2psys.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * TODO 类说明</p>
 *  
 * @author：xx 
 * @version 1.0
 * @since 2014年6月13日
 */
public class DisruptorUtil {

	private static DisruptorUtil dis = null;
	private static final int BUFFER_SIZE = Integer.parseInt(Global.getValue("disruptor_ringbuffer_size"));

	private final RingBuffer<ValueEvent> ringBuffer = new RingBuffer<ValueEvent>(ValueEvent.EVENT_FACTORY,
			new SingleThreadedClaimStrategy(BUFFER_SIZE), new BlockingWaitStrategy());

	private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
	private final CEventHandler handler = new CEventHandler();

	private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

	private final BatchEventProcessor<ValueEvent> batchEventProcessor = new BatchEventProcessor<ValueEvent>(ringBuffer,
			sequenceBarrier, handler);

	public DisruptorUtil() {
		ringBuffer.setGatingSequences(batchEventProcessor.getSequence());
	}

	public void consume() {
		EXECUTOR.submit(batchEventProcessor);
	}

	public void produce(ValueEvent event) {
		new Thread(new Producer(event, ringBuffer)).start();
	}

	static {
		dis = new DisruptorUtil();
	}

	
	/**
	 * 投标
	 * @param model
	 * @param borrow
	 * @throws Exception
	 */
	public static void tender(BorrowModel model,Borrow borrow)throws Exception{
		ValueEvent event = new ValueEvent();
		event.setOperate("tender");
		event.setBorrowModel(model);
		event.setBorrow(borrow);
		dis.produce(event);
		dis.consume();
	}
	
	
	/**
	 * 自动投标
	 * @param model
	 * @throws Exception
	 */
	public static void autoTender(BorrowModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoTender");
		event.setBorrowModel(model);
		dis.produce(event);
		dis.consume();
	}
	
	/**
	 * 满标复审
	 * @param model
	 * @throws Exception
	 */
	public static void autoVerifyFull(BorrowModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoVerifyFull");
		event.setBorrowModel(model);
		dis.produce(event);
		dis.consume();
	}	

	/**
	 * 复审通过
	 * @param model
	 * @throws Exception
	 */
	public static void autoVerifyFullSuccess(BorrowModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoVerifyFullSuccess");
		event.setBorrowModel(model);
		dis.produce(event);
		dis.consume();
	}

	/**
	 * 复审不通过
	 * @param model
	 * @throws Exception
	 */
	public static void autoVerifyFullFail(BorrowModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoVerifyFullFail");
		event.setBorrowModel(model);
		dis.produce(event);
		dis.consume();
	}

	/**
	 * 还款
	 * @param borrowRepayment
	 */
	public static void repay(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("repay");
		event.setBorrowRepayment(borrowRepayment);
		dis.produce(event);
		dis.consume();
	}
	/**
	 * 撤标
	 * @param model
	 */
	public static void autoCancel(Borrow borrow) {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoCancel");
		event.setBorrow(borrow);
		dis.produce(event);
		dis.consume();
	}
	
	/**
	 * 提现
	 * @param cashModel 提现的参数封装
	 * @throws Exception 异常
	 */
	public static void doVerifyCashBackTask(CashModel cashModel,String resultFlag) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("verifyCashBack");
		event.setCashModel(cashModel);
		event.setResultFlag(resultFlag);
		dis.produce(event);
		dis.consume();
	}
	/**
	 * 充值
	 * @param reModel 充值的参数封装
	 * @param log 充值日志的参数封装
	 */
	public static void doRechargeBackTask(RechargeModel reModel,String resultFlag) {
		ValueEvent event = new ValueEvent();
		event.setOperate("verifyRecharge");
		event.setRechargeModel(reModel);
		event.setResultFlag(resultFlag);
		dis.produce(event);
		dis.consume();
	}
	
	
	
	/**
	 * 提前还款
	 * @param borrowRepayment
	 */
	public static void doPriorRepay(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("doPriorRepay");
		event.setBorrowRepayment(borrowRepayment);
		dis.produce(event);
		dis.consume();
	}
	/**
	 * 后台逾期垫付
	 * @param borrowRepayment
	 */
	public static void overdue(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("overdue");
		event.setBorrowRepayment(borrowRepayment);
		dis.produce(event);
		dis.consume();
	}
	/**
	 * 前台逾期垫付
	 * @param borrowRepayment
	 */
	public static void overduePayment(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("overduePayment");
		event.setBorrowRepayment(borrowRepayment);
		dis.produce(event);
		dis.consume();
	}
	
    /**
     * 环迅开户
     * @param user
     * @param ips
     */
    public static void ipsRegister(User user , IpsRegister ips, String resultFlag){
    	ValueEvent event = new ValueEvent();
    	event.setOperate("ipsRegister");
    	event.setUser(user);
    	event.setIpsRegister(ips);
    	event.setResultFlag(resultFlag);
    	dis.produce(event);
    	dis.consume();
    	
    }
    
	/**
	 * 第三方开户接口
	 */
	public static void apiUserRegister(User user, String resultFlag){
    	ValueEvent event = new ValueEvent();
    	event.setOperate("apiUserRegister");
    	event.setUser(user);
//    	event.setApiRegisteInfo(rgsInfo);
    	event.setResultFlag(resultFlag);
    	dis.produce(event);
    	dis.consume();
	}
    
    /**
     * 发标回调
     * @param user
     * @param ips
     */
    public static void doAddBorrow(BorrowModel bm){
    	ValueEvent event = new ValueEvent();
    	event.setOperate("doAddBorrow");
    	event.setBorrowModel(bm);
    	dis.produce(event);
    	dis.consume();
    	
    }
    /**
     * 登记担保方
     * @param bm
     */
    public static void doIpsRegisterGuarantor(BorrowModel bm, String resultFlag){
        ValueEvent event = new ValueEvent();
        event.setOperate("doIpsRegisterGuarantor");
        event.setBorrowModel(bm);
        event.setResultFlag(resultFlag);
        dis.produce(event);
        dis.consume();
        
    }
    
    /**
     * 投标回调
     * @param bm
     */
    public static void doAddTender(BorrowModel bm,String resultFlag){
    	ValueEvent event = new ValueEvent();
    	event.setOperate("doAddTender");
    	event.setBorrowModel(bm);
    	event.setResultFlag(resultFlag);
    	dis.produce(event);
    	dis.consume();
    	
    }
    
    /**
     * 代偿回调
     * @param borrowRepayment
     */
    public static void doCompensateSuccess(BorrowRepayment borrowRepayment) {
        ValueEvent event = new ValueEvent();
        event.setOperate("doCompensateSuccess");
        event.setBorrowRepayment(borrowRepayment);
        dis.produce(event);
        dis.consume();
    }
}
