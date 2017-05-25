package com.rongdu.p2psys.borrow.model.interest;

import java.util.Date;
import java.util.List;

/**
 * 利息计算接口
 * 
 * @author：xx
 * @version 1.0
 * @since 2014年7月15日
 */
public interface InterestCalculator {

	/**
	 * 计算每期还款信息
	 * 
	 * @return 还款计划
	 */
	List<EachPlan> calculator();

	/**
	 * 计算按天计算还款信息
	 * 
	 * @param days 天数
	 * @return 还款计划
	 */
	List<EachPlan> calculator(int days);

	/**
	 * 取得每期还款计划
	 * 
	 * @return 每期还款计划
	 */
	List<EachPlan> getEachPlan();

	/**
	 * 还款总额
	 * 
	 * @return 还款总额
	 */
	double repayTotal();

	/**
	 * 最后一期还款时间
	 * 
	 * @return 最后一期还款时间
	 */
	Date repayTime();

	/**
	 * 还款总期数
	 * 
	 * @return 还款总期数
	 */
	int repayPeriods();

	/**
	 * 还款时间计算公式
	 * 
	 * @param date 投资时间
	 * @return 还款时间
	 */
	Date calculatorRepaytime(Date date);

}
