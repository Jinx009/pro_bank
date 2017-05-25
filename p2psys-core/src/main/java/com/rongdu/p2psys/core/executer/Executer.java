package com.rongdu.p2psys.core.executer;

import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * 业务处理基类
 *  
 * @author：fxx 
 * @version 2.0
 * @since 2014年6月11日
 */
interface Executer {

	/**
	 * 业务核心处理方法
	 * @param money 资金
	 * @param user User实体类
	 */
	void execute(double money, User user);
	
	/**
	 * 满标复审 account标中更新repay字段（本金+利息）
	 * @param money
	 * @param interest
	 * @param user
	 */
	void execute(double money,double interest,User user);

	/**
	 * 业务核心处理方法
	 * @param money 资金
	 * @param user 用户
	 * @param operator 操作员实体类
	 * @param toUser 交易对方
	 */
	void execute(double money, User user, Operator operator, User toUser);

	/**
	 * 业务核心处理方法
	 * @param money 资金
	 * @param user 用户
	 * @param toUser 交易对方
	 */
	void execute(double money, User user, User toUser);

	/**
	 * 业务核心处理方法
	 * @param money 资金
	 * @param userId 用户ID
	 * @param operator 操作员实体类
	 */
	void execute(double money, long userId, Operator operator);
	
	/**
	 * 业务核心处理方法 -增加红包金额
	 * @param money
	 * @param interest
	 * @param user
	 * @param redMoney
	 */
	public void execute(double money,User user, User toUser,double redMoney);
	
	public void execute(double money,User user, User toUser,double redMoney, double borrowVoucherMoney);

	/**
	 * 逻辑执行之前的准备工作
	 */
	void prepare();

	/**
	 * 新增资金记录
	 */
	void addAccountLog();

	/**
	 * 处理用户资金
	 */
	void handleAccount();

	/**
	 * 处理资金合计
	 */
	void handleAccountSum();

	/**
	 * 处理积分
	 */
	void handlePoints();

	/**
	 * 处理通知
	 */
	void handleNotice();

	/**
	 * 新增操作记录
	 */
	void addOperateLog();

	/**
	 * 处理第三方托管接口
	 */
	void handleInterface();

	/**
	 * 逻辑执行之后的处理工作
	 */
	void extend();

}
