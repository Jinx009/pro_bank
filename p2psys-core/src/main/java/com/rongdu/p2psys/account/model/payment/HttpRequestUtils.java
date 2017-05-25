package com.rongdu.p2psys.account.model.payment;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.ReflectUtil;
import com.rongdu.common.util.StringUtil;

public class HttpRequestUtils {

	public static int paramInt(String str, HttpServletRequest request) {
		return StringUtil.toInt(request.getParameter(str));
	}

	public static long paramLong(String str, HttpServletRequest request) {
		return StringUtil.toLong(request.getParameter(str));
	}

	public static double paramDouble(String str, HttpServletRequest request) {
		return BigDecimalUtil.round(request.getParameter(str));
	}

	public static String paramString(String str, HttpServletRequest request) {
		return StringUtil.isNull(request.getParameter(str));
	}

	public static Object paramValue(Class<?> type, String name, HttpServletRequest request) {
		Object v = null;
		if (type.equals(Long.class) || type.equals(long.class)) {
			v = HttpRequestUtils.paramLong(name, request);
		} else if (type == Integer.class || type == int.class || type == Byte.class || type == Short.class
				|| type == short.class) {
			v = HttpRequestUtils.paramInt(name, request);
		} else if (type == Boolean.class || type == boolean.class) {
			int i = HttpRequestUtils.paramInt(name, request);
			if (i == 1)
				v = true;
			v = false;
		} else if (type.equals(Double.class) || type.equals(double.class) || type.equals(Float.class)
				|| type.equals(float.class)) {
			v = HttpRequestUtils.paramDouble(name, request);
		} else {
			v = HttpRequestUtils.paramString(name, request);
		}
		return v;
	}

	public static Object paramModel(Class<?> clazz, HttpServletRequest request) {
		Object model;
		try {
			model = clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		Field[] fs = clazz.getDeclaredFields();
		Object v = null;

		for (Field f : fs) {
			v = paramValue(f.getType(), f.getName(), request);
			ReflectUtil.invokeSetMethod(clazz, model, f.getName(), f.getType(), v);
		}
		return model;
	}
}
