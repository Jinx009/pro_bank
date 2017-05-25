package com.rongdu.p2psys.score.tool;

import com.rongdu.common.util.BigDecimalUtil;

/**
 * TODO 积分计算
 * @author <a href="mailto:Administrator@abc.com">Administrator</a>
 * @version 1.0
 * @since 2013-7-26
 */
public class ScoreCountTool {

	private static int DEF_DIV_SCALE = 0; //默认精确的小数位   
	
	/**
	 * 投资积分计算规则1(除type外，所有参数，必传)
	 * @param type 1天标/0月标
	 * @param time 1天标：天数。0月标：月数。
	 * @param tenderMoney 投资总额
	 * @param checkMoney 限制金额
	 * @return
	 * 注：投资额*月份/1000（月标）；投资额*天数/30/1000（天标）。小数取整。
	 */
	public static int getTenderValueMethodOne(byte type , int time , double tenderMoney, double checkMoney){
		
		//判断参数是否为空，如果任何一个为空，则return
		if(tenderMoney <=  0 || checkMoney <= 0 || time <= 0) return 0;
		
		//天标算法
		if(type == 1){
			double t = BigDecimalUtil.mul(tenderMoney, time);//投资额*天数
			double f = BigDecimalUtil.div(t, checkMoney, DEF_DIV_SCALE);//投资额*天数/限制金额
			double y = BigDecimalUtil.div(f, 30, DEF_DIV_SCALE);//投资额*天数/限制金额/30(天标除以30天)
			return (int) y;
		}else if(type == 0){//月标算法
			double t = BigDecimalUtil.mul(tenderMoney, time);//投资额*月数
			double f = BigDecimalUtil.div(t, checkMoney, DEF_DIV_SCALE);//投资额*月数/限制金额
			return (int) f;
		}
		return 0;
	}
	/**
	 * 投资积分计算规则1(除type外，所有参数，必传)
	 * @param type 1天标/0月标
	 * @param time 1天标：天数。0月标：月数。
	 * @param tenderMoney 投资总额
	 * @param checkMoney 限制金额、
	 * @param decimal_manage 小数处理限制
	 * @return
	 * 注：投资额*月份/月标限制金额；投资额*天数/天标限制金额。小数根据小数规则，进行处理。
	 */
	public static int getTenderValue(int time , double tenderMoney, double checkMoney , double decimal_manage){
		
		//判断参数是否为空，如果任何一个为空，则return
		if(tenderMoney <=  0 || checkMoney <= 0 || time <= 0) return 0;
		
		double t = BigDecimalUtil.mul(tenderMoney, time);//投资额*天数
		double f = BigDecimalUtil.div(t, checkMoney);//投资额*天数/限制金额
		int value = (int) f;
		double decimal =  f-value;
		if(decimal > decimal_manage){
			return value + 1;
		}
		return (int) value;
	}
}
