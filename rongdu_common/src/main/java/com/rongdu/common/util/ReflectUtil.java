package com.rongdu.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 反射帮助类
 * 
 * @author fxx
 * @version 2.0
 * @since 2014年1月28日
 */
public class ReflectUtil {
	private static Logger logger = Logger.getLogger(ReflectUtil.class);

	public static List<?> PRIMITIVE_TYPES = Arrays.asList(new Class[] { char.class, short.class, byte.class, int.class,
			long.class, float.class, double.class, boolean.class, Short.class, Byte.class, Integer.class, Long.class,
			Float.class, Double.class, Boolean.class, String.class, Date.class });

	public static boolean isPrimitive(Class<?> type) {
		return PRIMITIVE_TYPES.contains(type);
	}

	public static Object invokeGetMethod(Class<?> claszz, Object o, String name) {
		Object ret = null;
		try {
			Method method = claszz.getMethod("get" + StringUtil.firstCharUpperCase(name));
			ret = method.invoke(o);
		} catch (Exception e) {
			logger.error(e);
		}
		return ret;
	}

	public static Object invokeSetMethod(Class<?> claszz, Object o, String name, Class<?>[] argTypes, Object[] args) {
		Object ret = null;
		try {
			Method method = claszz.getMethod("set" + StringUtil.firstCharUpperCase(name), argTypes);
			ret = method.invoke(o, args);
		} catch (Exception e) {
			logger.error(e);
		}
		return ret;
	}

	public static Object invokeSetMethod(Class<?> claszz, Object o, String name, Class<?> argType, Object args) {
		Object ret = null;
		try {
			Method method = claszz.getMethod("set" + StringUtil.firstCharUpperCase(name), new Class[] { argType });
			ret = method.invoke(o, new Object[] { args });
		} catch (Exception e) {
			logger.error(e);
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static Class<?> getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if ((index >= params.length) || (index < 0)) {
			throw new RuntimeException("你输入的索引" + ((index < 0) ? "不能小于0" : "超出了参数的总数"));
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		Class<?> clazz = object.getClass();
		while (clazz != Object.class) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (Exception e) {

			}
			if (method != null)
				break;
			clazz = clazz.getSuperclass();
		}
		return method;
	}

	public static Map<String, Field> getClassField(Class<?> clazz) {
		Field[] declaredFields = clazz.getDeclaredFields();
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		Map<String, Field> superFieldMap = new HashMap<String, Field>();
		for (Field field : declaredFields) {
			fieldMap.put(field.getName(), field);
		}
		if (clazz.getSuperclass() != null) {
			superFieldMap = getClassField(clazz.getSuperclass());
		}
		fieldMap.putAll(superFieldMap);
		return fieldMap;
	}
}
