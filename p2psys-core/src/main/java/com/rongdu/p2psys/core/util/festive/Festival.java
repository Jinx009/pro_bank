package com.rongdu.p2psys.core.util.festive;

/*******************************************************************************
 * Festival.java,此类为一个节日类,包括公历和农历的节日.
 ******************************************************************************/

public class Festival {
	
	//公历节日
	private String sFtv[] = {
			
			"1", //1 1月1日 元旦

			"2",//2 5月1日 劳动节
			
			"3"//3 10月1日 国庆节
			
	};

	//农历节日
	private String lFtv[] = {
			
			"4", //4 	正月初一 春节
			
			"5", //5 五月初五 端午节
			
			"6"//6 八月十五 中秋节
			
	};

	// 返回公历节日
	public String showSFtv(int month, int day) {
		if (month == 1 && day == 1) {
			return sFtv[0];
		} else if (month == 5 && day == 1) {
			return sFtv[1];
		} else if (month == 10 && day == 1) {
			return sFtv[2];
		}
		return "";

	}

	// 返回农历节日
	public String showLFtv (int month, int day, int dayOfmonth) {
		if (month == 1 && day == 1) {
			return lFtv[0];//春节
		} else if (month == 5 && day == 5) {
			return lFtv[1];//五月初五
		} else if (month == 8 && day == 15) {//八月十五
			return lFtv[2];
		}
		return "";
	}
	

}