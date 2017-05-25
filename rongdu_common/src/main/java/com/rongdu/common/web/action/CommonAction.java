package com.rongdu.common.web.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.apache.struts2.util.TokenHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.octo.captcha.service.CaptchaServiceException;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.ReflectUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.ip.IPSeeker;
import com.rongdu.common.util.ip.IPUtil;
import com.rongdu.common.util.jcaptcha.CaptchaServiceSingleton;
import com.rongdu.common.web.filter.SimplePropertyFilter;

@Controller
@Scope("prototype")
public class CommonAction implements ServletRequestAware, ServletResponseAware, SessionAware, ServletContextAware {
	private final static Logger logger = Logger.getLogger(CommonAction.class);
	public final static String SUCCESS = "success";
	public final static String ERROR = "error";
	public final static String FAIL = "fail";
	public final static String OK = "ok";
	public final static String MSG = "msg";
	public final static String ADMINMSG = "adminmsg";
	public final static String NOTFOUND = "notfound";

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, Object> session;
	protected ServletContext context;
	protected List<String> names = null;

	protected int paramInt(String str) {
		String obj = request.getParameter(str);
		if (StringUtil.isBlank(obj))
			return 0;
		return StringUtil.toInt(obj);
	}

	protected long paramLong(String str) {
		String obj = request.getParameter(str);
		if (StringUtil.isBlank(obj))
			return 0l;
		return StringUtil.toLong(obj);
	}

	protected double paramDouble(String str) {
		String obj = request.getParameter(str);
		if (StringUtil.isBlank(obj))
			return 0;
		return BigDecimalUtil.round(obj);
	}

	protected String paramString(String str) {
		return StringUtil.isNull(request.getParameter(str));
	}

	private Object paramValue(Class<?> type, String name) {
		Object v = null;
		if (type.equals(Long.class) || type.equals(long.class)) {
			v = this.paramLong(name);
		} else if (type == Integer.class || type == int.class || type == Byte.class || type == Short.class
				|| type == short.class) {
			v = this.paramInt(name);
		} else if (type == Boolean.class || type == boolean.class) {
			int i = this.paramInt(name);
			if (i == 1)
				v = true;
			v = false;
		} else if (type.equals(Double.class) || type.equals(double.class) || type.equals(Float.class)
				|| type.equals(float.class)) {
			v = this.paramDouble(name);
		} else {
			v = this.paramString(name);
		}
		return v;
	}

	protected Object paramModel(Class<?> clazz) {
		Object model;
		try {
			model = clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		Field[] fs = clazz.getDeclaredFields();
		Object v = null;

		for (Field f : fs) {
			v = paramValue(f.getType(), f.getName());
			ReflectUtil.invokeSetMethod(clazz, model, f.getName(), f.getType(), v);
		}
		return model;
	}

	/**
	 * 获取http请求的实际IP
	 * 
	 * @return
	 */
	protected String getRequestIp() {
		String realip = IPUtil.getRemortIP(request);
		return realip;
	}

	/**
	 * 获取IP所在地
	 * 
	 * @return
	 */
	protected String getAreaByIp() {
		String realip = getRequestIp();
		return getAreaByIp(realip);
	}

	protected String getAreaByIp(String ip) {
		IPSeeker ipSeeker = IPSeeker.getInstance();
		String nowarea = ipSeeker.getArea(ip);
		return nowarea;
	}

	/**
	 * 生产校验码
	 * 
	 * @throws IOException
	 */
	protected void genernateCaptchaImage() throws IOException {
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream out = response.getOutputStream();
		try {
			String captchaId = request.getSession(true).getId();
			BufferedImage challenge = (BufferedImage) CaptchaServiceSingleton.getInstance().getChallengeForID(
					captchaId, request.getLocale());
			ImageIO.write(challenge, "jpg", out);
			out.flush();
		} catch (CaptchaServiceException e) {
		} finally {
			out.close();
		}
	}

	/**
	 * 校验校验码是否正确
	 * 
	 * @param valid
	 * @return
	 */
	protected boolean checkValidImg(String valid) {
		boolean b = false;
		try {
			b = CaptchaServiceSingleton.getInstance().validateResponseForID(request.getSession().getId(),
					valid.toLowerCase());
		} catch (CaptchaServiceException e) {
			b = false;
		}
		return b;
	}

	/**
	 * 校验校验码是否正确
	 * 
	 * @param valid
	 * @return
	 */
	protected void checkValidImgJson(String valid) {
		try {
			CaptchaServiceSingleton.getInstance().validateResponseForID(request.getSession().getId(),
					valid.toLowerCase());
		} catch (CaptchaServiceException e) {
			throw new BussinessException("验证码错误", 1);
		}
	}

	/**
	 * 提示消息
	 * 
	 * @param msg
	 * @param url
	 */
	protected void message(String msg, String url) {
		String urltext = "";
		if (StringUtil.isNotBlank(url)) {
			urltext = "<a href=" + request.getContextPath() + url + " >返回上一页</a>";
			request.setAttribute("backurl", urltext);
		} else {
			urltext = "<a href='javascript:history.go(-1)'>返回上一页</a>";
		}
		message(msg, url, urltext);
	}

	protected void message(String msg) {
		this.message(msg, getMsgUrl());
	}

	/**
	 * 提示消息
	 * 
	 * @param msg
	 * @param url
	 * @param text
	 */
	protected void message(String msg, String url, String text) {
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("rsmsg", msg);
		String urltext = "<a href=" + request.getContextPath() + url + " >" + text + "></a>";
		request.setAttribute("backurl", urltext);
	}

	protected void setMsgUrl(String url) {
		request.setAttribute("currentUrl", url);
		String msgurl = (String) session.get("msgurl");
		String query = request.getQueryString();
		if (StringUtil.isNotBlank(query)) {
			url = url + "?" + query;
		}
		msgurl = url;
		session.put("msgurl", msgurl);
	}

	protected String getMsgUrl() {
		String msgurl = "";
		Object o = null;
		if ((o = session.get("msgurl")) != null) {
			msgurl = (String) o;
		}
		return msgurl;
	}

	protected String upload(File upload, String fileName, String destDir, String destFileName) throws Exception {
		if (upload == null)
			return "";
		logger.info("文件：" + upload);
		logger.info("文件名：" + fileName);
		String destFileUrl = destDir + "/" + destFileName;
		String destfilename = ServletActionContext.getServletContext().getRealPath(destDir) + "/" + destFileName;
		logger.info(destfilename);
		File imageFile = null;
		imageFile = new File(destfilename);
		FileUtils.copyFile(upload, imageFile);
		return destFileUrl;
	}

	protected void printJson(Object json) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}

	protected void printSuccess() throws IOException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("result", true);
		printJson(getStringOfJpaObj(data));
	}

	protected void printResult(String msg, boolean result) throws IOException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("msg", msg);
		data.put("result", result);
		printJson(getStringOfJpaObj(data));
	}
	
	protected void printWebSuccess() throws IOException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("result", true);
		printWebJson(getStringOfJpaObj(data));
	}

	protected void printWebResult(String msg, boolean result) throws IOException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("msg", msg);
		data.put("result", result);
		printWebJson(getStringOfJpaObj(data));
	}

	protected void printWebJson(Object json) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
	 * 检查表单提交的
	 * 
	 * @param name
	 * @return
	 */
	protected String checkToken(String name) {
		String paramValue = paramString(name);
		String tokenValue = StringUtil.isNull((String) session.get(name));
		// 参数、session中都没用token值提示错误
		if (paramValue.isEmpty() && tokenValue.isEmpty()) {
			return "会话Token未设定！";
		} else if (paramValue.isEmpty() && !tokenValue.isEmpty()) {
			return "表单Token未设定！";
		} else if (paramValue.equals(tokenValue) && !tokenValue.isEmpty()) { // session中有token,防止重复提交检查
			session.remove(name);
			return "";
		} else {
			return "请勿重复提交！";
		}
	}
	
	/**
	 * 检查WAP表单提交的
	 * 
	 * @param name
	 * @return
	 */
	protected String checkWAPToken(String name) {
		String paramValue = paramString(name);
		String tokenValue = StringUtil.isNull((String) session.get(name));
		// 参数、session中都没用token值提示错误
		if (paramValue.isEmpty() && tokenValue.isEmpty()) {
			return "0";
		} else if (paramValue.isEmpty() && !tokenValue.isEmpty()) {
			return "0";
		} else if (paramValue.equals(tokenValue) && !tokenValue.isEmpty()) { // session中有token,防止重复提交检查
			session.remove(name);
			return "-1";
		} else {
			return "请勿重复提交！";
		}
	}

	/**
	 * 防重复提交 生成Token
	 * 
	 * @param name
	 */
	protected void saveToken(String name) {
		if(StringUtil.isBlank((session.get(name)))){			
			String token = TokenHelper.generateGUID();
			session.put(name, token);
		}
	}

	/**
	 * obj里边包含jpa对象会报错，要过滤一次。
	 * 
	 * @return
	 */
	protected String getStringOfJpaObj(Object obj) {
		SimplePropertyFilter spf = new SimplePropertyFilter();
		SerializeWriter sw = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(sw);
		serializer.getPropertyFilters().add(spf);
		serializer.write(obj);
		return sw.toString();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	protected boolean hasNameInParam(String name) {
		if (names == null) {
			names = new ArrayList<String>();
			Enumeration<String> e = request.getParameterNames();
			while (e.hasMoreElements()) {
				names.add(e.nextElement());
			}
		}
		for (String s : names) {
			if (s.indexOf(name) > -1)
				return true;
		}
		return false;
	}

	protected Object paramModel(Class<?> clazz, String name) {
		if (!hasNameInParam(name))
			return null;
		Object model;
		try {
			model = clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		Field[] fs = clazz.getDeclaredFields();
		Object v = null;
		StringBuffer nameBuffer = new StringBuffer();
		for (Field f : fs) {
			nameBuffer.setLength(0);
			if (!StringUtil.isBlank(name)) {
				nameBuffer.setLength(0);
				nameBuffer.append(name).append(".");
			}
			nameBuffer.append(f.getName());
			if (ReflectUtil.isPrimitive(f.getType())) {
				v = paramValue(f.getType(), nameBuffer.toString());
			} else {
				v = paramModel(f.getType(), nameBuffer.toString());
			}
			ReflectUtil.invokeSetMethod(clazz, model, f.getName(), f.getType(), v);
		}
		return model;
	}

}
