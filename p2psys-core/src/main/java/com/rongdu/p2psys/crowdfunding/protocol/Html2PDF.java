package com.rongdu.p2psys.crowdfunding.protocol;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;





import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.core.util.itext.HeaderFooter;




/**
 * 生成PDF通用方法
 * @author Jinx
 *
 */
public class Html2PDF {

	
	public static void createPdf(String fileName,String path,String html,Map<String,Object> data) throws Exception {
		File pdfFile = new File(path);
		if (!pdfFile.exists()) {
			pdfFile.mkdir();
		}
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path+fileName));// 建立一个PdfWriter对象
		String out = FreemarkerUtil.renderTemplate(html,data);
		List<Element> list = templateHtml(out);
		Rectangle rect = new Rectangle(50, 50, 545, 792);
        rect.setBorderColor(BaseColor.BLACK);
        writer.setBoxSize("art", rect);
        writer.setFullCompression();
        HeaderFooter header=new HeaderFooter();
        writer.setPageEvent(header);
		document.open();
		for (Element e : list) {
			document.add(e);
		}
		document.close();
	}

	/**
	 * 测试用例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String htmlStr = "<a>hhehe</a><p>哈哈哈哈</p>";
		createPdf("222.pdf","D:/pdftest/44433/",htmlStr,null);
	}
	
	/**
	 * html数据整合
	 * @param str
	 * @param pdf
	 * @throws IOException
	 * @throws DocumentException
	 */
	public  static List<Element> templateHtml(String str) throws IOException, DocumentException{
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
		return list;
	}
}

