package com.rongdu.p2psys.nb.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.rongdu.common.util.FreemarkerUtil;

/**
 * 字符串辅助处理类
 * 
 * @author Jinx
 */
public class StringUtil extends StringUtils
{

	/**
	 * 空字符串转换，非空字符串去除空格
	 * 
	 * @param str
	 * @return 转换后字符串
	 */
	public static String nullToBlank(String str)
	{
		if (null == str)
		{
			return "";
		}
		return str.trim();
	}

	/**
	 * 是否为非空字符串
	 * 
	 * @param str
	 * @return 判断结果
	 */
	public static boolean isNotBlank(String str)
	{
		return !"".equals(nullToBlank(str));
	}

	/**
	 * 检验手机号
	 * 
	 * @param phone
	 * @return boolean
	 */
	public static boolean isPhone(String phone)
	{
		phone = isNull(phone);
		Pattern regex = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(16[0-9])|(14[0-9])|(18[0-9]))\\d{8}$");
		Matcher matcher = regex.matcher(phone);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 是否为空字符串
	 * 
	 * @param str
	 * @return 判断结果
	 */
	public static boolean isNullOrBlank(String str)
	{
		return "".equals(nullToBlank(str));
	}

	/**
	 * 返回字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String isNull(String str)
	{
		if (str == null)
		{
			return "";
		}
		return str.trim();
	}
	public static boolean nullOrNull(Object object)
	{
		if(null!=object)
		{
			String objStr = object.toString();
			
			if(!"".equals(objStr))
			{
				return true;
			}
		}
		
		return false;
	}

	public static String fillTemplet(String template,Map<String, Object> sendData)
	{
		template = template.replace('`', '\'');
		try
		{
			return FreemarkerUtil.renderTemplate(template, sendData);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public static String replaceBlank(String str) 
	{
        String dest = "";
        if (str!=null) 
        {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return  dest.replaceAll(" ","");
    }
	
	public static void main(String[] args)
	{
		String str = "e y   x 4";
		System.out.println("结果为："+replaceBlank(str));
	}
	
}
