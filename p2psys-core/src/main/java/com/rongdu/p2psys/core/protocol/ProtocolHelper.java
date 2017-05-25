package com.rongdu.p2psys.core.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.itext.PdfHelper;

/**
 * 
 *  
 * 协议基类
 *  
 * @author：qj 
 * @version 1.0
 * @since 2014年6月13日
 */
public class ProtocolHelper {
	private static Logger logger = Logger.getLogger(ProtocolHelper.class);

	public static void init() {
		Properties prop = new Properties();
		InputStream in = ProtocolHelper.class.getResourceAsStream("protocol.properties");
		try {
			prop.load(in);
			Set<Object> keySet = prop.keySet();
			logger.info("系统指定初始化protocolBean:.......");
			for (Object object : keySet) {
				String key = object.toString();
				String value = prop.getProperty(key).trim();
				logger.info("bean:" + key + "------->" + value);
				AbstractProtocolBean bean = (AbstractProtocolBean) Class.forName(value).newInstance();
				Global.PROTOCOL_MAP.put(key, bean);
			}
		} catch (Exception e) {
			logger.error("初始化系统指定ProtocolBean报错..", e);
			throw new RuntimeException("初始化系统指定ProtocolBean报错..");
		}
	}

	/**
	 * 根据key获取，逻辑bean
	 * 
	 * @param key
	 * @return
	 */
	public static AbstractProtocolBean doProtocol(String key) {
		AbstractProtocolBean bean = Global.PROTOCOL_MAP.get(key);
		return bean;
	}
	
	public static AbstractProtocolForPpfundBean doProtocolForPpfund(String key) {
		AbstractProtocolForPpfundBean bean = (AbstractProtocolForPpfundBean) Global.PROTOCOL_MAP.get(key);
		return bean;
	}
	/**
	 * 
	 * @param str
	 * @param pdf
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected static String templateHtml(String str, PdfHelper pdf) throws IOException, DocumentException {
		final List<Element> pdfeleList = new ArrayList<Element>();
		ElementHandler elemH = new ElementHandler() {
			public void add(final Writable w) {
				if (w instanceof WritableElement) {
					pdfeleList.addAll(((WritableElement) w).elements());
				}
			}
		};
		InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(str.getBytes("UTF-8")), "UTF-8");
		XMLWorkerHelper.getInstance().parseXHtml(elemH, isr);
		List<Element> list = new ArrayList<Element>();
		for (Element ele : pdfeleList) {
			if (ele instanceof LineSeparator || ele instanceof WritableDirectElement) {
				continue;
			}
			list.add(ele);
		}
		pdf.addHtmlList(list);
		return "";
	}

}
