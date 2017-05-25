package com.rongdu.p2psys.core.freemarker.method;

import java.util.List;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 有四种方式: 　　(1).在模板文件中注册,在模板中使用 　　<#assign interest= "com.rd.freemarker.InterestMethodModel"?new()> 　　<#assign lilv=
 * interest(500,0.02,3)/> 　 (2).处理模板文件时注册 Map<String,Object> root=new HashMap<String, Object>(); root.put("interest",
 * new InterestMethodModel()); Configuration config=new Configuration(); TemplateModel model = createModel(wrapper,
 * servletContext, request, response); // 处理模版 template.process(model, root); (3).Struts2集成处理,自定义FreemarkerManager 1、
 * 继承FreemarkerManager Configuration configuration = super.createConfiguration(servletContext); //计算利息的自定义方法
 * configuration.setSharedVariable("interest", new InterestMethodModel() ); 2、struts2.xml配置文件新增 <constant
 * name="struts.freemarker.manager.classname" value="com.rd.freemarker.CustomFreemarkerManager" />
 * (4)，通过struts2的interceptor的后方法实现函数注册自定义方法
 * 
 * @author fuxingxing
 * @date 2012-7-13-上午11:09:22
 * @version <b>Copyright (c)</b> 2012-51融都-版权所有<br/>
 */
public class InterestMethodModel implements TemplateMethodModel {

	public Object exec(List args) throws TemplateModelException {
		if (null != args && 4 == args.size()) {
			double account = 0.0, apr = 0.0;
			int period = 0;
			String type = "";
			try {
				account = Double.parseDouble((String) args.get(0));
				apr = Double.parseDouble((String) args.get(1));
				period = Integer.parseInt((String) args.get(2));
				type = StringUtil.isNull(args.get(3));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			InterestCalculator ic = null;
			double result = 0.0;
			/*
			 * if (type.equals("end")) { ic = new EndInterestCalculator(account, apr, period); } else if
			 * (type.equals("month")) { ic = new MonthEqualCalculator(account, apr, period); } else if
			 * (type.equals("monthEnd")) { ic = new EndInterestCalculator(account, apr, period,
			 * InterestCalculator.TYPE_MONTH_END); } else if (type.equals("monthInterest")) { ic = new
			 * MonthInterestCalculator(account, apr, period); } else if (type.equals("monthEqual")) { ic = new
			 * MonthEqualCalculator(account, apr, period); } ic.each(); result = ic.getMoneyPerMonth() * ic.getPeriod();
			 */
			return result;
		} else {
			return "Argument is null,or argument's length illegal.";
		}
	}
}
