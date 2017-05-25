package com.rongdu.p2psys.tpp.ips.service;

import java.util.List;

import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsRegisterGuarantor;
import com.rongdu.p2psys.tpp.ips.model.IpsRepayment;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * TODO 调用托管接口的业务处理方法
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月22日
 */
public interface IpsService {
	/**
	 * 处理环讯发标的回调的处理逻辑
	 * @param bm 标的信息
	 * @return  处理结果
	 */
	boolean doAddBorrow(BorrowModel bm);
	/**
	 * 处理环讯投标的回调的处理逻辑
	 * @param bm 投标的信息
	 * @return  处理结果
	 */
	boolean doAddTender(BorrowModel bm);
	
	
	/**
	 * 统一处理所有接口任务
	 * @param taskList 
	 * @return 处理结果
	 */
	boolean doIpsTask(List<Object> taskList);
	
	
	/**
	 * 转账
	 * @param borrow 标
	 * @param tender 投标记录
	 * @param u  转出人
	 * @param toU 转入人
	 * @param transferType 转账类型
	 * @param transferMode 转账方式 1：逐笔入账；2：批量入账
	 * @param taskList 任务集合
	 */
	void transfer(Borrow borrow, BorrowTender tender, User u, User toU,
			String transferType, String transferMode, List<Object> taskList);
	/**
     * 转账
     * @param borrow 标
     * @param tender 投标记录
     * @param transferType 转账类型
     * @param transferMode 转账方式 1：逐笔入账；2：批量入账
     * @param taskList 任务集合
     */
    void transfer(Borrow borrow, List<BorrowTender> tender,
            String transferType, String transferMode);
    
	/**
	 * 环迅用户还款信息组装
	 * @param repayment 还款信息
	 * @param repayType 还款类型
	 * @return
	 */
    IpsRepayment repaySkip(BorrowRepayment repayment, byte repayType);
    
    /**
     * 环迅还款处理
     * @param pay
     * @return
     */
    Boolean ipsRayManage(TppIpsPay pay);
    
    /**
     * 自动代扣充值
     * @param recharge 充值信息
     * @return 环迅参数
     */
    IpsAutoRecharge doAutoRecharge(AccountRecharge recharge);
    
    /**
     * 更新repayment信息
     * @param pay
     */
    BorrowRepayment updateRepay(TppIpsPay pay);
    /**
     * 登记担保方
     * @param borrow
     * @return
     */
    IpsRegisterGuarantor registerGuarantor(Borrow borrow);
    /**
     * 登记担保方回调处理
     * @param bm
     * @return
     */
    boolean doIpsRegisterGuarantor(BorrowModel bm);

    /**
     * 担保方代偿
     * @param repayment
     * @return
     */
    void doCompensate(BorrowRepayment repayment);
    
    /**
     * 担保方代偿成功后处理
     * @param borrow
     * @return
     */
    void doCompensateSuccess(TppIpsPay pay);
    
}
