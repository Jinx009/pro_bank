package com.rongdu.p2psys.core.freemarker.method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.rongdu.common.util.DateUtil;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * @author fuxingxing
 * @date 2012-7-13-上午11:09:22
 * @version <b>Copyright (c)</b> 2012-51融都-版权所有<br/>
 *          Example: dateroll(date,year,month,day,format) date代表需要滚动的时间 year表示滚动年份，month表示滚动月份，day表示滚动天数，指DAY_OF_MONTH,
 *          format代表格式，如不填写，格式为"yyyy-MM-dd HH:mm:ss"
 */
public class DateRollMethodModel implements TemplateMethodModel {

	String dateStr = "", yStr = "", mStr = "", dStr = "", format = "";

	public Object exec(List args) throws TemplateModelException {
		long times = 0;
		int year = 0, mon = 0, day = 0;
		if (!parseArgs(args).equals("")) {
			return "Arguments error!";
		}
		times = Long.parseLong(dateStr);
		year = Integer.parseInt(yStr);
		mon = Integer.parseInt(mStr);
		day = Integer.parseInt(dStr);
		Date d = new Date(times * 1000);
		d = DateUtil.rollDate(d, year, mon, day);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String f = sdf.format(d);
		return f;
	}

	private String parseArgs(List args) {
		if (args.size() == 4) {
			dateStr = (String) args.get(0);
			yStr = (String) args.get(1);
			mStr = (String) args.get(2);
			dStr = (String) args.get(3);
			format = "yyyy-MM-dd HH:mm:ss";
		} else if (args.size() == 5) {
			dateStr = (String) args.get(0);
			yStr = (String) args.get(1);
			mStr = (String) args.get(2);
			dStr = (String) args.get(3);
			format = (String) args.get(4);
		} else {
			return "Arguments error!";
		}
		return "";
	}

}
