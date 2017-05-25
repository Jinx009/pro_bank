package com.rongdu.p2psys.core.util.itext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfHelper {
	private Logger logger = Logger.getLogger(PdfHelper.class);

	private Document document;
//	private BaseFont bfChinese;
//	private Font font;

	public PdfHelper(String path) {
		document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));// 建立一个PdfWriter对象
			Rectangle rect = new Rectangle(50, 50, 545, 792);
            rect.setBorderColor(BaseColor.BLACK);
            writer.setBoxSize("art", rect);
//            writer.setBoxSize("art", PageSize.A4);
            writer.setFullCompression();
//    		writer.setPdfVersion(PdfWriter.VERSION_1_4);
            HeaderFooter header=new HeaderFooter();
            logger.info("添加生成事件。。。");
            writer.setPageEvent(header);
            logger.info("开始生成PDF。。。");
			document.open();
//			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
//			font = new Font(bfChinese, 10, Font.NORMAL);// 设置字体大小
		} catch (DocumentException de) {

		} catch (IOException ioe) {

		}
	}

	public static PdfHelper instance(String path) {
		return new PdfHelper(path);
	}

	public void exportPdf() {
		document.close();
	}

	public void addHtmlList(List<Element> list) throws DocumentException {
		for (Element e : list) {
			document.add(e);
		}
	}
	
}
