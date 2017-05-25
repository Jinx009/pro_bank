package com.rongdu.common.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * 工具类-随机数
 * 
 * @author ZhuJunjie
 * @version 2.0
 * @since 2015-06-01
 */
public class RandomUtil {

	/**
	 * 获取范围内int值
	 * 
	 * @param max
	 * @param min
	 * @return int
	 */
	public static int getRandomRange(int max, int min) {
		return (int) (Math.random() * (max - min) + min);
	}

	/**
	 * 返回指定范围的随机数验证码，无重复
	 * 
	 * @param start
	 * @param end
	 * @return int[]
	 */
	public static int[] getRangRandom(int start, int end) {
		return getRangRandom(start, end, end - start + 1);
	}

	/**
	 * 返回指定范围指定个数的随机数验证码，无重复
	 * 
	 * @param start
	 * @param end
	 * @param num
	 * @return int[]
	 */
	public static int[] getRangRandom(int start, int end, int num) {
		int length = end - start + 1;
		// 参数不合法
		if (length < 1 || num > length) {
			return null;
		} else {
			int[] numbers = new int[length];
			int[] result = new int[num];
			// 循环赋初始值
			for (int i = 0; i < length; i++) {
				numbers[i] = i + start;
			}
			Random random = new Random();
			// 取randomMax次数
			for (int i = 0; i < num; i++) {
				// 随机获取取数的位置
				int m = random.nextInt(length - i) + i;
				result[i] = numbers[m];
				// 交换位置
				int temp = numbers[m];
				numbers[m] = numbers[i];
				numbers[i] = temp;
			}
			return result;
		}
	}

	/**
	 * 生成6位随机数验证码，纯数字，无重复
	 * 
	 * @return String
	 */
	public static String code() {
//		Set<Integer> set = GetRandomNumber(6);
//		Iterator<Integer> iterator = set.iterator();
		String temp = "";
//		while (iterator.hasNext()) {
//			temp += iterator.next();
//		}
		int code = getRandomRange(1000000,1000);
		temp = String.format("%06d",code);
		return temp;
	}

	/**
	 * 生成指定长度随机数验证码，纯数字，无重复
	 * 
	 * @return Set
	 */
	public static Set<Integer> GetRandomNumber(int length) {
		Set<Integer> set = new HashSet<Integer>();
		Random random = new Random();
		while (set.size() < length) {
			set.add(random.nextInt(10));
		}
		return set;
	}

	/**
	 * 从指定的字符串范围内，获取指定长度的随机字符，允许重复
	 * 
	 * @param length
	 * @param base
	 * @return String
	 */
	private static String getRandomString(int length, String base) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获取随机字符，只包含小写字母，允许重复
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomLowercaseString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz";
		return getRandomString(length, base);
	}

	/**
	 * 获取随机字符，只包含数字和小写字母，允许重复
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomNumbersAndLowercaseString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		return getRandomString(length, base);
	}

	/**
	 * 获取随机字符，只包含大写字母，允许重复
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomUppercaseString(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return getRandomString(length, base);
	}

	/**
	 * 获取随机字符，只包含大写字母和数字，允许重复
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomNumbersAndUppercaseString(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return getRandomString(length, base);
	}

	/**
	 * 获取随机字符，只包含数字，允许重复
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomNumString(int length) {
		String base = "0123456789";
		return getRandomString(length, base);
	}

	/**
	 * 获取随机字符，包含大小写字母和数字，允许重复
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomNumbersAndString(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		return getRandomString(length, base);
	}

	/**
	 * 从指定的字符串范围内，获取指定长度的随机字符，不允许重复
	 * 
	 * @param length
	 * @param base
	 * @return String
	 */
	private static String getUniqueRandomString(int length, String base) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < length) {
			int number = random.nextInt(base.length());
			char tempChar = base.charAt(number);
			if (sb.indexOf(String.valueOf(tempChar)) < 0) {
				sb.append(tempChar);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取随机字符，包含小写字母和数字，不允许重复
	 * 
	 * <p>
	 * (a) 没有1和l，以免混淆
	 * <p>
	 * (b) 没有0和O，以免混淆
	 * <p>
	 * (c) 没有4和b，避嫌
	 * 
	 * 
	 * @param length
	 * @return
	 */
	public static String getSpecialRandomCode(int length) {
		String base = "2356789acdefghijkmnpqrstuvwxyz";
		return getUniqueRandomString(length, base);
	}

}
