package com.rongdu.p2psys.core.web.interceptor;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.blogspot.radialmind.html.HTMLParser;
import com.blogspot.radialmind.html.HandlingException;
import com.blogspot.radialmind.xss.XSSFilter;
import com.opensymphony.xwork2.ActionInvocation;
import com.rongdu.common.util.StringUtil;

public class ParamFilterInterceptor extends BaseInterceptor {

	/** 序列号 */
	private static final long serialVersionUID = -6325242223825713099L;
	/** 日志 */
	private static final Logger logger = Logger.getLogger(ParamFilterInterceptor.class);
	/** XSSFilter */
	private static final XSSFilter xssFilter = new XSSFilter();

	/**
	 * 初始化
	 */
	public void init() {
		super.init();
	}

	/**
	 * 任何请求的参数 都要经过安全级别的过滤
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		Enumeration names = request.getParameterNames();
		// 默认不包含有
		boolean hasDeniedText = false;
		// 默认不包含有
		boolean hasDeniedXss = false;
		String illegalParam = "";
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				illegalParam = values[i];
				// 先对请求进行转码，然后转成大写
				String needCheckParams = UrlDecoder(values[i]).toUpperCase();
				// 富文本特殊处理
				if ("content".equals(StringUtil.isNull(name))) {
					hasDeniedText = isAttack(needCheckParams);
					fliterXSSForContent(StringUtil.isNull(values[i]));
				} else {
					hasDeniedText = isAttack(needCheckParams);
					hasDeniedXss = fliterXSS(StringUtil.isNull(values[i]));
				}
				// 文本校验
				if (hasDeniedText) {
					break;
				}
				// XSS校验
				if (hasDeniedXss) {
					break;
				}
			}
			// 文本校验
			if (hasDeniedText) {
				break;
			}
			// XSS校验
			if (hasDeniedXss) {
				break;
			}
		}
		// 文本过滤
		if (hasDeniedText) {
			logger.info("存在非法字符:" + illegalParam);
			return "error";
		}
		// xss过滤
		if (hasDeniedXss) {
			logger.info("存在非法字符:" + illegalParam);
			return "error";
		}
		String result = ai.invoke();
		return result;
	}

	/**
	 * 检查是否已经存在xss的字符
	 * 
	 * @param text 字符串
	 * @return 存在返回true
	 */
	public boolean fliterXSS(String text) {
		StringBuffer sb = new StringBuffer().append("<html>").append(text).append("</html>");;
		StringReader reader = new StringReader(sb.toString());
		StringWriter writer = new StringWriter();

		StringReader noReader = new StringReader(sb.toString());
		StringWriter noWriter = new StringWriter();
		try {
			HTMLParser.process(reader, writer, xssFilter, true);
			HTMLParser.process(noReader, noWriter, null, true);
		} catch (HandlingException e) {
			logger.error(e);
			return false;
		}

		String filterStr = writer.toString();
		String noFilterStr = noWriter.toString();

		return !filterStr.equals(noFilterStr);
	}

	/**
	 * 检查是否已经存在xss的字符
	 * 
	 * @param text
	 * @return 不存在返回true
	 */
	public String fliterXSSForContent(String text) {
		StringBuffer sb = new StringBuffer("<html>").append(text).append("</html>");
		StringReader reader = new StringReader(sb.toString());
		StringWriter writer = new StringWriter();
		try {
			HTMLParser.process(reader, writer, xssFilter, true);
		} catch (HandlingException e) {
		}
		return writer.toString();
	}
	
	/**
	 * 返回true就是包含非法字符，返回false就是不包含非法字符 
	 * 系统内容过滤规则
	 * 1、包含  『 and 1 特殊字符 』， 特殊字符指>,<,=, in , like 字符 
	 * 2、『 /特殊字符/ 』，特殊字符指 *字符
	 * 3、『<特殊字符 script 』特殊字符指空字符
	 * 4、『 EXEC 』
	 * 5、『 UNION SELECT』
	 * 5、『 UPDATE SET』
	 * 5、『 INSERT INTO VALUES』
	 * 5、『 SELECT或DELETE FROM』
	 * 5、『CREATE或ALTER或DROP或TRUNCATE TABLE或DATABASE』
	 */
	public static boolean isAttack(String input) {
		String getfilter = "\\b(AND|OR)\\b.+?(>|<|=|\\bIN\\b|\\bLIKE\\b)|\\/\\*.+?\\*\\/|<\\s*SCRIPT\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)|\\bON.+\\b\\s*=|\\bJAVASCRIPT\\s*:\\b";
		Pattern pat = Pattern.compile(getfilter);
		Matcher mat = pat.matcher(input);
		boolean rs = mat.find();
		return rs;
	}
	
	public static void main(String[] args) {
	    String str="O75eJ5spQphliWkDyBTXItr9RH/lz3O0Qj08cPVAFYjmboeyvbx3itLYU80BmCwtvw469bjl4h6SlAbLzl0b3K2vcA540FBgeXiLjVtUJKnKUKITQb1tVRT8SW65Ii+8T/FDVEWPDp+hX7f7QJ35utddfquKjjz+iKc6bxUDjG5IHBdeATVjVfDdO9BpL01/3OwcwqS1veFTAeZqlJdrdYEDJEYebrQSSy9oYeTbqGx/xhU63OjzAZnCtk+tukIxpqn3MSAJSNV7E7MhKQHV2NW/H7jPhKdiRqIhlkEiN/geUvKl+wjOcSQFyITYL1UInYbh+Mvsb+B/OnSGnW/CTU/iQ84RA2ol+GXuh1JECsTTbX9PYuAnX3h8dLuPd1eugYK5LvJtU4KXy0aTrm6/gZbTSKtpsUU4ElnezZz8ZUspXHWl/X+QAK/etWtsI0QeACbDeC80dUcHnifEURi03iIJKGr7K5EGEEmCcBk0o2BYjR9Wug4uORDP8zOs9m4hlZac6d/7TX+PGebK+Jr2mi++VDmDvQtVhXE17fhTjE7z5hTf9EWaW4EPHIlpPDSbYAUINFRGtKAz04t+2Cw60Fa0jxxJBDv7lzgPvVLbT5pdHBbEvCyNeEOsV1twHVTMjGGWU4BZ7dzHh2qeSmdYp5jgfU8TI8ymlRTgGoGnq41cVSp9LvViTkUGuTkk4mUZGQlpgIsiDOHN2vaSkMXjiJmS0r/aifu9jF0ldimLuVN6GSw3JxewpRKgaP4u/qWCieycTSl7kycBTIbFMRzk4gZ7ogr8udxOJ3JaiOhK90Scomm3xviZC5e1yf6ywm/AqVddr1dt7sjovyHlsgCpjXuuChgSWLR+LKKMp52bp3FSuDOwO+xaSw==";
	    System.out.println(isAttack(str));
    }
	
	/**
	 * 字符串解码
	 * 
	 * @param sStr
	 * @return
	 */
	public static String UrlDecoder(String sStr) {
		String sReturnCode = sStr;
		try {
			sReturnCode = URLDecoder.decode(sStr, "utf-8");
		} catch (Exception e) {
		}
		return sReturnCode;
	}
}
