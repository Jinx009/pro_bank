package com.rongdu.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {
	public static double format(double d,String format){
		DecimalFormat df = new DecimalFormat(format); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}
	//v1.6.7.2 RDPROJECT-725 wcw 2014-3-14 start
	public static double format2(double d){
		/*DecimalFormat df = new DecimalFormat("0.00"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);*/
		return BigDecimalUtil.decimal(d, 2);
	}
	
	public static String format2Str(double d){
		/*DecimalFormat df = new DecimalFormat("0.00"); 
		String ds=df.format(d);
		return ds;*/
		return BigDecimalUtil.decimal(d, 2)+"";
	}
	
	public static double format4(double d){
		/*DecimalFormat df = new DecimalFormat("0.0000"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);*/
		return BigDecimalUtil.decimal(d, 4);
	}
	
	public static double format6(double d){
		/*DecimalFormat df = new DecimalFormat("0.000000"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);*/
		return BigDecimalUtil.decimal(d, 6);
	}
	//v1.6.7.2 RDPROJECT-725 wcw 2014-3-14 end
	public static double getDouble(String str){
		if(str==null||str.equals(""))
			return 0.0;
		double ret=0.0;
		try {
			ret=Double.parseDouble(str);
		} catch (NumberFormatException e) {
			ret=0.0;
		}
		return format6(ret);
	}

	// v1.6.7.2 RDPROJECT-548 lx 2013-12-13 start
	public static double getDouble2(String str) {
		if (str == null || str.equals(""))
			return 0.0;
		double ret = 0.0;
		try {
			ret = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			ret = 0.0;
		}
		return format2(ret);
	}

	// v1.6.7.2 RDPROJECT-548 lx 2013-12-13 end
	public static long getLong(String str){
		if(str==null||str.equals(""))
			return 0L;
		long ret=0;
		try {
			ret=Long.parseLong(str);
		} catch (NumberFormatException e) {
			ret=0;
		}
		return ret;
	}
	
	public static Long[] getLongs(String[] str){
		
		if(str==null||str.length<1)
			return new Long[]{0L};
		Long[] ret=new Long[str.length];
		for(int i=0;i<str.length;i++){
			ret[i]=getLong(str[i]);
		}
		return ret;
	}
	
	public static int getInt(String str){
		if(str==null||str.equals(""))
			return 0;
		int ret=0;
		try {
			ret=Integer.parseInt(str);
		} catch (NumberFormatException e) {
			ret=0;
		}
		return ret;
	}
	
	public static int compare(double x,double y){
		BigDecimal val1=new BigDecimal(x);
		BigDecimal val2=new BigDecimal(y);
		return val1.compareTo(val2);
	}
	
	/**
	 * @param d
	 * @param len
	 * @return
	 */
	public static double ceil(double d,int len){
		String str=Double.toString(d);
		int a=str.indexOf(".");
		if(a+3>str.length()){
			a=str.length();
		}else{
			a=a+3;
		}
		str=str.substring(0, a);
		return Double.parseDouble(str);
	}
	
	public static double ceil(double d){
		return ceil(d,2);
	}	
	public static void main(String[] args) {
		String money="10000000000";
		Double a = NumberUtil.getDouble(money); // 使用分进行提交
		System.out.println("d:="+a); 
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		System.out.println("d:="+nf.format(a)); 
		String b = String.valueOf(a);
        System.out.println(b);
		a=NumberUtil.getDouble(nf.format(a))*0.0002;
		System.out.println("d:="+a); 
		//a = NumberUtils.getDouble(money2); // 使用分进行提交
		
	}
}
