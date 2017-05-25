package com.rongdu.p2psys.core.util.itext;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper {
	/*
	 * 基础字体对象
	 **/
	private BaseFont bfChinese;
	/*
	 * 页眉table
	 **/
	private PdfPTable table;
	/**
	 * 文档字体大小，页脚页眉最好和文本大小一致
	 */
	private int presentFontSize = 8;

	private Logger logger = Logger.getLogger(HeaderFooter.class);
	// 模板
	private PdfTemplate total;

	// 利用基础字体生成的字体对象，一般用于生成中文文字
	private Font font = null;
	
	private Image bkImage = null;

	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(50, 50);// 共 页 的矩形的长宽高
		Image image = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);// 设置中文字体
			font = new Font(bfChinese, presentFontSize, Font.NORMAL);// 设置字体大小
			String picPath = ServletActionContext.getServletContext().getRealPath("/data/pdfpic/");
			image = Image.getInstance(picPath + "/pageHeader.png");
			image.setBorder(Rectangle.NO_BORDER);
			
			bkImage = Image.getInstance(picPath + "/pageBk.jpg");
			bkImage.setAbsolutePosition(20, 150);

			table = new PdfPTable(4);

			table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			int hws1[] = {1,1,1,1};
			table.setWidths(hws1);
			table.setTotalWidth(document.right() - document.left());
			table.setLockedWidth(true);
		    table.getDefaultCell().setBorder(Rectangle.BOTTOM);
			table.addCell(image);
			
			Phrase content = new Phrase("上海众朴信息科技有限公司", font);
			PdfPCell cellright1 = new PdfPCell(content);
			cellright1.setColspan(3);
			cellright1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cellright1.setVerticalAlignment(Element.ALIGN_BOTTOM);
			cellright1.setBorder(Rectangle.NO_BORDER);
			cellright1.setBorder(Rectangle.BOTTOM);
			table.addCell(cellright1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

	}
	
	public void onEndPage(PdfWriter writer, Document document) {

		table.writeSelectedRows(0, 1, 50, 800, writer.getDirectContent());
		int pageS = writer.getPageNumber();
		String foot1 = "地址：上海市静安区成都北路333号招商局广场东楼23层          邮编: 200000";
		Phrase footer1 = new Phrase(foot1, font);
		String foot2 = "客服热线：400-6366-800  ";
		Phrase footer2 = new Phrase(foot2, font);
		String foot3 = "第 " + pageS + " 页 /共";
		Phrase footer3 = new Phrase(foot3, font);

		// 3.计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
		float len = bfChinese.getWidthPoint(foot3, presentFontSize);

		// 4.拿到当前的PdfContentByte
		PdfContentByte cb = writer.getDirectContentUnder();
		
		
        try {
			cb .addImage(bkImage);
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		}
		// 5.写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F
		// 再给偏移20F适合人类视觉感受，否则肉眼看上去就太偏左了
		// ,y轴就是底边界-20,否则就贴边重叠到数据体里了就不是页脚了；注意Y轴是从下往上累加的，最上方的Top值是大于Bottom好几百开外的。
		ColumnText.showTextAligned(cb,Element.ALIGN_CENTER,footer1,
						(document.rightMargin() + document.right() + 
						document.leftMargin() - document.left() - len) / 2.0F + 20F,
						document.bottom() - 10, 0);
		ColumnText.showTextAligned(cb,Element.ALIGN_CENTER,footer2,
						(document.rightMargin() + document.right() + 
						document.leftMargin() - document.left() - len) / 2.0F + 20F,
						document.bottom(), 0);
		ColumnText.showTextAligned(cb,Element.ALIGN_CENTER,footer3,
						(document.rightMargin() + document.right() + 
						document.leftMargin() - document.left() - len) / 2.0F + 20F,
						document.bottom() + 10, 0);
		// 6.写入页脚2的模板（就是页脚的Y页这俩字）添加到文档中，计算模板的和Y轴,X=(右边界-左边界 - 前半部分的len值)/2.0F +
		// len ， y 轴和之前的保持一致，底边界-20
		cb.addTemplate(total, (document.rightMargin() + document.right()
				+ document.leftMargin() - document.left()) / 2.0F + 20F,
				document.bottom() + 10); // 调节模版显示的位置

	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		total.beginText();
		total.setFontAndSize(bfChinese, presentFontSize);// 生成的模版的字体、颜色
		String foot2 = " " + (writer.getPageNumber() - 1) + " 页";
		total.showText(foot2);// 模版显示的内容
		total.endText();
		total.closePath();

	}
}