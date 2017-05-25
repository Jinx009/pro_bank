package com.rongdu.p2psys.core.executer;

import org.apache.log4j.Logger;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

public abstract class AbstractExecuter implements Executer
{

	Logger logger = Logger.getLogger(AbstractExecuter.class);
	protected double money;
	protected User user;
	protected Operator operator;
	protected User toUser;
	protected double repayMoney;
	/**
	 * 红包金额
	 */
	protected double redMoney;

	/**
	 * 抵用券金额
	 */
	protected double borrowVoucherMoney;

	@Override
	public void execute(double money, User user, Operator operator, User toUser)
	{
		this.money = BigDecimalUtil.round(money, 2);
		this.user = user;
		this.operator = operator;
		this.toUser = toUser;
		// 业务预处理
		prepare();
		// 更新账户
		handleAccount();
		if(money>0){			
			// 添加资金记录
			addAccountLog();
		}
		// 资金统计
		handleAccountSum();
		// 积分处理
		handlePoints();
		// 通知处理
		handleNotice();
		// 第三方接口
		handleInterface();
		// 操作日志记录记录
		addOperateLog();
		// 扩展处理
		extend();
	}

	@Override
	public void execute(double money, User user)
	{
		execute(money, user, null, null);
	}

	@Override
	public void execute(double money, double repayMoney, User user)
	{
		this.repayMoney = BigDecimalUtil.round(repayMoney, 2);
		execute(money, user, null, null);
	}

	@Override
	public void execute(double money, User user, User toUser, double redMoney)
	{
		this.redMoney = redMoney;
		if (toUser == null)
		{
			execute(money, user, null, null);
		} else
		{
			execute(money, user, null, toUser);
		}
	}

	@Override
	public void execute(double money, User user, User toUser, double redMoney,
			double borrowVoucherMoney)
	{
		this.redMoney = redMoney;
		this.borrowVoucherMoney = borrowVoucherMoney;
		if (toUser == null)
		{
			execute(money, user, null, null);
		} else
		{
			execute(money, user, null, toUser);
		}
	}

	@Override
	public void execute(double money, User user, User toUser)
	{
		execute(money, user, null, toUser);
	}

	@Override
	public void execute(double money, long userId, Operator operator)
	{
		UserService userService = (UserService) BeanUtil.getBean("userService");
		execute(money, userService.getUserById(userId), operator, null);
	}

	@Override
	public abstract void prepare();

	@Override
	public abstract void addAccountLog();

	@Override
	public abstract void handleAccount();

	@Override
	public abstract void handleAccountSum();

	@Override
	public abstract void handlePoints();

	@Override
	public abstract void handleNotice();

	@Override
	public abstract void addOperateLog();

	@Override
	public abstract void handleInterface();

	@Override
	public abstract void extend();

}
