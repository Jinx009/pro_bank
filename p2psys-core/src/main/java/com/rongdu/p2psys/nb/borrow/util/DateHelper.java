package com.rongdu.p2psys.nb.borrow.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	/**
	 * 计算两个日期相差的自然天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int dayInterval(Date smdate, Date bdate) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        try {
			smdate=sdf.parse(sdf.format(smdate));
			bdate=sdf.parse(sdf.format(bdate));  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days)); 
	}
	
	/**
	 * 计算两个日期相差的自然月
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int mothInterval(String date1, String date2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
		} catch (ParseException e) {
			System.out.println("两个日期相差月份计算："+e.toString());
		}
		return ( c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH) );
	}
	
	//当前是这个月第几天
	public static int days(){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		return c1.get(Calendar.DATE);
	}
	
	public static void main(String[] args) throws ParseException {
		DateFormat sim =new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(dayInterval(sim.parse("2015-11-23"),new Date()));
//		System.out.println(days());
		System.out.println(16.00/30);
	}
}
