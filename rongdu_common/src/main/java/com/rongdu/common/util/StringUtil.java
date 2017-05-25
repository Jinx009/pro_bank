package com.rongdu.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 工具类-字符串处理
 */
public class StringUtil extends StringUtils
{

	/**
	 * 字符串空处理，去除首尾空格
	 * 
	 * <p>
	 * 如果str为null，返回"",否则返回str
	 * </p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String isNull(String str)
	{
		if (str == null)
		{
			return "";
		}
		return str.trim();
	}

	/**
	 * 将对象转为字符串
	 * 
	 * @param o
	 * @return String
	 */
	public static String isNull(Object o)
	{
		if (o == null)
		{
			return "";
		}
		String str = "";
		if (o instanceof String)
		{
			str = (String) o;
		}
		else
		{
			str = o.toString();
		}
		return str.trim();
	}

	/**
	 * 检验是否为空或空字符串
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isBlank(String str)
	{
		return StringUtil.isNull(str).equals("");
	}

	public static boolean isBlank(Object o)
	{
		return StringUtil.isNull(o).equals("");
	}

	/**
	 * 检验是否非空字符串
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNotBlank(String str)
	{
		return !StringUtil.isNull(str).equals("");
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
	 * 校验是否全中文，返回true
	 * 
	 * <p>
	 * 表示是
	 * </p>
	 * <p>
	 * 反之为否
	 * </p>
	 * 
	 * @param realname
	 * @return boolean
	 */
	public static boolean isChinese(String realname)
	{
		realname = isNull(realname);
		Pattern regex = Pattern.compile("[\\u4e00-\\u9fa5]{2,25}");
		Matcher matcher = regex.matcher(realname);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 校验邮箱格式
	 * 
	 * @param email
	 * @return boolean
	 */
	public static boolean isEmail(String email)
	{
		email = isNull(email);
		Pattern regex = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 校验身份证号码
	 * 
	 * @param cardId
	 * @return boolean
	 */
	public static boolean isCard(String cardId)
	{
		cardId = isNull(cardId);
		// 身份证正则表达式(15位)
		Pattern isIDCard1 = Pattern
				.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
		// 身份证正则表达式(18位)
		Pattern isIDCard2 = Pattern
				.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
		Matcher matcher1 = isIDCard1.matcher(cardId);
		Matcher matcher2 = isIDCard2.matcher(cardId);
		boolean isMatched = matcher1.matches() || matcher2.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为整数
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isInteger(String str)
	{
		if (isBlank(str))
		{
			return false;
		}
		Pattern regex = Pattern.compile("\\d*");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNumber(String str)
	{
		if (isBlank(str))
		{
			return false;
		}
		Pattern regex = Pattern.compile("(-)?\\d*(.\\d*)?");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 首字母大写
	 * 
	 * @param s
	 * @return String
	 */
	public static String firstCharUpperCase(String s)
	{
		StringBuffer sb = new StringBuffer(s.substring(0, 1).toUpperCase());
		sb.append(s.substring(1, s.length()));
		return sb.toString();
	}

	/**
	 * 隐藏头几位
	 * 
	 * @param str
	 * @param len
	 * @return String
	 */
	public static String hideFirstChar(String str, int len)
	{
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		if (str.length() <= len)
		{
			for (int i = 0; i < chars.length; i++)
			{
				chars[i] = '*';
			}
		}
		else
		{
			for (int i = 0; i < 1; i++)
			{
				chars[i] = '*';
			}
		}
		str = new String(chars);
		return str;
	}

	/**
	 * 隐藏末几位
	 * 
	 * @param str
	 * @param len
	 * @return String
	 */
	public static String hideLastChar(String str, int len)
	{
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		if (str.length() <= len)
		{
			return hideLastChar(str, str.length() - 1);
		}
		else
		{
			for (int i = chars.length - 1; i > chars.length - len - 1; i--)
			{
				chars[i] = '*';
			}
		}
		str = new String(chars);
		return str;
	}

	/**
	 * 指定起始位置字符串隐藏
	 * 
	 * @param str
	 * @param index1
	 * @param index2
	 * @return String
	 */
	public static String hideStr(String str, int index1, int index2)
	{
		if (str == null)
			return null;
		String str1 = str.substring(index1, index2);
		String str2 = str.substring(index2);
		String str3 = "";
		if (index1 > 0)
		{
			str1 = str.substring(0, index1);
			str2 = str.substring(index1, index2);
			str3 = str.substring(index2);
		}
		char[] chars = str2.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			chars[i] = '*';
		}
		str2 = new String(chars);
		String str4 = str1 + str2 + str3;
		return str4;
	}

	public static String contact(Object[] args)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++)
		{
			sb.append(args[i]);
			if (i < args.length - 1)
			{
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 数字拼接为字符串
	 * 
	 * @param arr
	 * @return String
	 */
	public static String array2Str(int[] arr)
	{
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < arr.length; i++)
		{
			s.append(arr[i]);
			if (i < arr.length - 1)
			{
				s.append(",");
			}
		}
		return s.toString();
	}

	/**
	 * 字符串数组转换为数字数组
	 * 
	 * @param strarr
	 * @return int[]
	 */
	public static int[] strarr2intarr(String[] strarr)
	{
		int[] result = new int[strarr.length];
		for (int i = 0; i < strarr.length; i++)
		{
			result[i] = Integer.parseInt(strarr[i]);
		}
		return result;
	}

	public static String fillTemplet(String template,
			Map<String, Object> sendData)
	{
		// 模板中的'是非法字符，会导致无法提交，所以页面上用`代替
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

	/**
	 * 大写字母转成“_”+小写
	 * 
	 * <p>
	 * 驼峰命名转换为下划线命名
	 * </p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String toUnderline(String str)
	{
		char[] charArr = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		sb.append(charArr[0]);
		for (int i = 1; i < charArr.length; i++)
		{
			if (charArr[i] >= 'A' && charArr[i] <= 'Z')
			{
				sb.append('_').append(charArr[i]);
			}
			else
			{
				sb.append(charArr[i]);
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 下划线改成驼峰样子
	 * 
	 * @param str
	 * @return String
	 */
	public static String clearUnderline(String str)
	{
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (str == null || str.isEmpty())
		{
			// 没必要转换
			return "";
		}
		else if (!str.contains("_"))
		{
			// 不含下划线，仅将首字母小写
			return str.substring(0, 1).toLowerCase() + str.substring(1);
		}
		// 用下划线将原始字符串分割
		String camels[] = str.split("_");
		for (String camel : camels)
		{
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty())
			{
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0)
			{
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			}
			else
			{
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	/**
	 * String to int
	 * 
	 * @param str
	 * @return int
	 */
	public static int toInt(String str)
	{
		if (StringUtil.isBlank(str))
			return 0;
		int ret = 0;
		try
		{
			ret = Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			ret = 0;
		}
		return ret;
	}

	public static byte toByte(String str)
	{
		if (StringUtil.isBlank(str))
			return 0;
		byte ret = 0;
		try
		{
			ret = Byte.parseByte(str);
		}
		catch (NumberFormatException e)
		{
			ret = 0;
		}
		return ret;
	}

	/**
	 * String to Long
	 * 
	 * @param str
	 * @return long
	 */
	public static long toLong(String str)
	{
		if (StringUtil.isBlank(str))
			return 0L;
		long ret = 0;
		try
		{
			ret = Long.parseLong(str);
		}
		catch (NumberFormatException e)
		{
			ret = 0;
		}
		return ret;
	}

	/**
	 * String[] to Long[]
	 * 
	 * @param str
	 * @return Long[]
	 */
	public static Long[] toLongs(String[] str)
	{

		if (str == null || str.length < 1)
			return new Long[] { 0L };
		Long[] ret = new Long[str.length];
		for (int i = 0; i < str.length; i++)
		{
			ret[i] = toLong(str[i]);
		}
		return ret;
	}

	/**
	 * String to Double
	 * 
	 * @param str
	 * @return double
	 */
	public static double toDouble(String str)
	{
		if (StringUtil.isBlank(str))
			return 0;
		try
		{
			return BigDecimalUtil.round(str);
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	/**
	 * 生成指定长度的随机字符串，字母加数字组合
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomString(int length)
	{
		String val = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++)
		{
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum))
			{
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			}
			else if ("num".equalsIgnoreCase(charOrNum))
			{
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 根据身份证号码计算性别
	 * 
	 * @param cardId
	 * @return int
	 */
	public static int getSexByCardid(String cardId)
	{
		int sexNum = 0;
		if (cardId.length() == 15)
		{
			sexNum = cardId.charAt(13);
		}
		else
		{
			sexNum = cardId.charAt(16);
		}
		if (sexNum % 2 == 1)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 根据身份证计算生日
	 * 
	 * @param cardId
	 * @return Date
	 */
	public static Date getBirthdayByCardid(String cardId)
	{
		String birth = null;
		if (cardId.length() == 15)
		{
			birth = cardId.substring(6, 12);
		}
		else
		{
			birth = cardId.substring(6, 14);
		}
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = null;
		String str = null;
		try
		{
			str = sf2.format(sf1.parse(birth));
			birthday = sf2.parse(str);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return birthday;
	}

	public static String eraseHtmlTag(String str)
	{
		str = StringUtil.isNull(str);
		String tempStr = str;
		if (str.indexOf("<") > -1 && str.indexOf(">") > -1)
		{
			tempStr = str.substring(str.indexOf("<"), str.indexOf(">") + 1);
			tempStr = StringUtil.isNull(str.replace(tempStr, ""));
		}
		if (str.length() != tempStr.length())
		{
			return eraseHtmlTag(tempStr);
		}
		else
		{
			return tempStr;
		}
	}

}
