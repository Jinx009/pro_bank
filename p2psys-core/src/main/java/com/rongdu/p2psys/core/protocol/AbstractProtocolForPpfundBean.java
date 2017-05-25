package com.rongdu.p2psys.core.protocol;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.itext.PdfHelper;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.ppfund.exception.PpfundException;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

import freemarker.template.TemplateException;

public abstract class AbstractProtocolForPpfundBean extends AbstractProtocolBean {
	private static Logger logger = Logger.getLogger(AbstractProtocolForPpfundBean.class);

	protected UserService userService;
	protected PpfundService ppfundService;
	protected PpfundInService ppfundInService;

	// 生成pdf路径名
	protected String inPdfName;
	// 下载pdf名
	protected String downloadFileName;
	
	// 协议模板
	protected Protocol protocol;

	protected PdfHelper pdf;
	
	/**
	 * PPfund Model
	 */
	protected PpfundModel ppfund;
	
	/**
	 * PpfundIn Model
	 */
	protected PpfundInModel ppfundIn;
	
	protected User inUser;
	// 操纵用户Id
	protected long userId;
	
	/**
	 * 合同签订日期
	 */
	protected String signDate;

	// 引入数据map
	protected Map<String, Object> data = new HashMap<String, Object>();

	@Override
	public void executer(long ppfunId, Protocol protocol, long userId) {
		this.executer(ppfunId, 0, protocol, userId);
	}

	@Override
	public void executer(long ppfunId, long inId, Protocol protocol, long userId) {
		// 初始化基础参数
		logger.info("初始化ProtocolBean");
		this.userService = (UserService) BeanUtil.getBean("userService");
		this.ppfundService = (PpfundService) BeanUtil.getBean("ppfundService");
		this.ppfundInService = (PpfundInService) BeanUtil.getBean("ppfundInService");
		
		String contextPath = ServletActionContext.getServletContext().getRealPath("/data/protocol/");
		this.downloadFileName = "投资服务协议.pdf";
		this.inPdfName = contextPath + ppfunId + "_" + System.currentTimeMillis() + ".pdf";
		this.pdf = PdfHelper.instance(inPdfName);
		
		this.ppfund = PpfundModel.instance(ppfundService.getPpfundById(ppfunId));
		this.ppfundIn = PpfundInModel.instance(ppfundInService.getById(inId));
		if(this.ppfund.getIsFixedTerm() == 0) {
			this.signDate = DateUtil.dateStr6(this.ppfundIn.getAddTime());
		} else {
			this.signDate = DateUtil.dateStr6(ppfundInService.getLastInByPpfundId(ppfund.getId()).getAddTime());
		}
		this.inUser = this.ppfundIn.getUser();
		this.userId = userId;
		this.protocol = protocol;
		int ppfundInId = ppfundInService.getUserPpfundInOrder(inId, ppfunId);
		this.protocolNo = this.ppfund.getPidNo() + "-" +  String.format("%03d",ppfundInId);
		
		// 业务预处理
		prepare();
		// 下载协议校验
		validDownload();
		// 初始化参数
		initData();
		// 创建pdf逻辑
		createPdf();
	}
	

	public String generatePpfundProtocol(long ppfunId, long inId, Protocol protocol, long userId) {
		// 初始化基础参数
		logger.info("初始化ProtocolBean");
		this.userService = (UserService) BeanUtil.getBean("userService");
		this.ppfundService = (PpfundService) BeanUtil.getBean("ppfundService");
		this.ppfundInService = (PpfundInService) BeanUtil.getBean("ppfundInService");
		
		String contextPath = ServletActionContext.getServletContext().getRealPath("/data/protocol/");
		this.downloadFileName = "投资服务协议.pdf";
		this.inPdfName = contextPath + ppfunId + "_" + System.currentTimeMillis() + ".pdf";
		this.pdf = PdfHelper.instance(inPdfName);
		
		this.ppfund = PpfundModel.instance(ppfundService.getPpfundById(ppfunId));
		this.ppfundIn = PpfundInModel.instance(ppfundInService.getById(inId));
		if(this.ppfund.getIsFixedTerm() == 0) {
			this.signDate = DateUtil.dateStr6(this.ppfundIn.getAddTime());
		} else {
			this.signDate = DateUtil.dateStr6(ppfundInService.getLastInByPpfundId(ppfund.getId()).getAddTime());
		}
		this.inUser = this.ppfundIn.getUser();
		this.userId = userId;
		this.protocol = protocol;
		int ppfundInId = ppfundInService.getUserPpfundInOrder(inId, ppfunId);
		this.protocolNo = this.ppfund.getPidNo() + "-" +  String.format("%03d",ppfundInId);
		
		// 业务预处理
		prepare();
		// 下载协议校验
		validDownload();
		// 初始化参数
		initData();
		String out = "";
		try {
			String content = protocol.getContent();
			content = content.replaceAll("<p >", "<p>");
			content = content.replaceAll("<p>&nbsp;</p>", "");
			if(content.contains("line-height:"))
			{
				String fontSize = content.substring(content.indexOf("line-height:")+12, content.indexOf("px;"));
				content = content.replace(fontSize, "30");
			}
			out = FreemarkerUtil.renderTemplate(content,this.data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("生成ppfund协议内容出错：" +  e.getMessage());
		}
		return out;
	}

	@Override
	public void validDownload() {
		// 判断是否有模板
		if (this.protocol == null || StringUtil.isBlank(this.protocol.getContent())) {
			throw new PpfundException("读取协议模板出错！");
		}
		// 判断标是否存在
		if (this.ppfund != null) {
			int status = this.ppfund.getStatus();
			boolean isDown = status == 0 || status == 1 || status == 3;
			// 判断标是否状态异常
			if (!isDown) {
				throw new PpfundException("资金管理产品状态异常！");
			}
		} else {
			throw new PpfundException("资金管理产品不存在！");
		}
		// 判断用户是否存在
		if (userId == 0) {
			throw new BorrowException("用户信息异常！");
		}
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		data.put("ppfund", ppfund);
		data.put("ppfundIn", ppfundIn);
		data.put("inUser", inUser);
		data.put("signDate", signDate);
		data.put("protocolNo", protocolNo);
		data.put("downTime", DateUtil.dateStr6(new Date()));
		data.put("weburl", Global.getValue("weburl"));
	}

	@Override
	public synchronized void createPdf() {
		boolean checkFile = false;
		File pdfFile = new File(inPdfName);
		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdir();
			}
			String out = FreemarkerUtil.renderTemplate(protocol.getContent(),this.data);
			ProtocolHelper.templateHtml(out, pdf);
			checkFile = true;
		} catch (Exception e) {
			logger.info("生成pdf出错");
			e.printStackTrace();
		}
		if (!checkFile) {
			throw new BorrowException("pdf生成的路径不存在...");
		}
		pdf.exportPdf();
	}

	public String getInPdfName() {
		return inPdfName;
	}

	public void setInPdfName(String inPdfName) {
		this.inPdfName = inPdfName;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
}
