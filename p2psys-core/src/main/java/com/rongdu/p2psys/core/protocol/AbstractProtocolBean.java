package com.rongdu.p2psys.core.protocol;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.common.util.MoneyUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.borrow.service.BorrowProtocolService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.itext.PdfHelper;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

import freemarker.template.TemplateException;

/**
 * 创建协议抽象类
 * 
 * @author qj
 * @version 2.0
 * @since 2014-05-22
 */
public abstract class AbstractProtocolBean implements ProtocolBean {
	private static Logger logger = Logger.getLogger(AbstractProtocolBean.class);

	protected BorrowService borrowService;
	protected BorrowTenderService borrowTenderService;
	protected BorrowCollectionService borrowCollectionService;
	protected VerifyLogService verifyLogService;
	protected UserService userService;
	protected BorrowProtocolService borrowProtocolService;
	protected BorrowRepaymentService borrowRepaymentService;
	// 生成pdf路径名
	protected String inPdfName;
	// 下载pdf名
	protected String downloadFileName;
	// 标id
	protected long borrowId;
	// 标model
	protected BorrowModel borrow;
	// 投标记录
	protected List<BorrowTenderModel> tenderList;
	// 标id
	protected long tenderId;
	// 投标记录
	protected BorrowTenderModel tender;
	// 投标用户
	protected User tenderUser;
	// 发标用户
	protected User borrowUser;
	// 还款计划
	private List<BorrowRepayment> repaymentList;
	// 协议模板
	protected Protocol protocol;
	// 操纵用户Id
	protected long userId;
	
    /**
     * 审核通过时间 
     */
	protected String vetifyTime;
	
	/**
	 * 借款到期日
	 */
	protected String expirationDate;
	
	/**
	 * 月截止还款日
	 */
	protected int monthlyRepayDate;
	
	/**
	 * 投资人利息管理费
	 */
	protected double interestFee;
	
	/*
	 * 
	 * */
	protected String protocolNo;
	
	protected PdfHelper pdf;
	// 引入数据map
	protected Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 业务核心处理方法
	 */
	@Override
	public void executer(long borrowId, Protocol protocol, long userId) {
		this.executer(borrowId, 0, protocol, userId);
	}

	/**
	 * 业务核心处理方法
	 */
	@SuppressWarnings("static-access")
	public void executer(long borrowId, long tenderId, Protocol protocol, long userId) {
		// 初始化基础参数
		logger.info("初始化ProtocolBean");
		this.borrowService = (BorrowService) BeanUtil.getBean("borrowService");
		this.borrowTenderService = (BorrowTenderService) BeanUtil.getBean("borrowTenderService");
		this.borrowCollectionService = (BorrowCollectionService) BeanUtil.getBean("borrowCollectionService");
		this.verifyLogService = (VerifyLogService) BeanUtil.getBean("verifyLogService");
		this.userService = (UserService) BeanUtil.getBean("userService");
		this.borrowProtocolService = (BorrowProtocolService) BeanUtil.getBean("borrowProtocolService");
		this.borrowRepaymentService = (BorrowRepaymentService) BeanUtil.getBean("borrowRepaymentService");
		String contextPath = ServletActionContext.getServletContext().getRealPath("/data/protocol/");
		this.inPdfName = contextPath + borrowId + "_" + System.currentTimeMillis() + ".pdf";
		this.pdf = PdfHelper.instance(inPdfName);
		this.borrowId = borrowId;
		this.borrow = BorrowModel.instance(borrowService.getBorrowById(borrowId));
		this.downloadFileName = borrow.getName() + ".pdf";
		this.borrowUser = borrow.getUser();
		this.protocol = protocol;
		this.tenderList = borrowTenderService.getTenderList(borrowId);
		this.repaymentList = borrowRepaymentService.getRepaymentByBorrowId(borrowId);
		this.userId = userId;
		if (tenderId != 0) {
			this.tenderId = tenderId;
			this.tender = BorrowTenderModel.instance(borrowTenderService.getTenderById(tenderId));
			this.tenderUser = this.tender.getUser();
			this.interestFee = borrowCollectionService.sumInterestFeeByTender(tender);
		}
		VerifyLog log = verifyLogService.findByType(borrowId, "borrow", 2);
		Date fullTime = new Date();
		if(log!=null)
		{
			fullTime = verifyLogService.findByType(borrowId, "borrow", 2).getTime();
		}
		this.vetifyTime = DateUtil.dateStr6(fullTime);
		if(borrow.getType() == borrow.TYPE_SECOND){
			this.expirationDate = vetifyTime;
		}else{
			if(borrow.getBorrowTimeType() == 1){//天标
				this.expirationDate = DateUtil.dateStr6(DateUtil.rollDay(fullTime, borrow.getTimeLimit()));
			}else{
				this.expirationDate = DateUtil.dateStr6(DateUtil.rollMon(fullTime, borrow.getTimeLimit()));
			}
		}
		this.monthlyRepayDate = DateUtil.getDay(fullTime);
		int tenderOrder = borrowTenderService.getUserBorrowTenderOrder(tenderId,borrowId);
		this.protocolNo = this.borrow.getBidNo() + "-" +  String.format("%03d",tenderOrder);
		// 业务预处理
		prepare();
		// 下载协议校验
		validDownload();
		// 初始化参数
		initData();
		// 创建pdf逻辑
		createPdf();
	}
	
	/**
	 * 业务核心处理方法
	 */
	@SuppressWarnings("static-access")
	public String generateProtocol(long borrowId, long tenderId, Protocol protocol, long userId) {
		// 初始化基础参数
		logger.info("初始化ProtocolBean");
		this.borrowService = (BorrowService) BeanUtil.getBean("borrowService");
		this.borrowTenderService = (BorrowTenderService) BeanUtil.getBean("borrowTenderService");
		this.borrowCollectionService = (BorrowCollectionService) BeanUtil.getBean("borrowCollectionService");
		this.verifyLogService = (VerifyLogService) BeanUtil.getBean("verifyLogService");
		this.userService = (UserService) BeanUtil.getBean("userService");
		this.borrowProtocolService = (BorrowProtocolService) BeanUtil.getBean("borrowProtocolService");
		this.borrowRepaymentService = (BorrowRepaymentService) BeanUtil.getBean("borrowRepaymentService");
		String contextPath = ServletActionContext.getServletContext().getRealPath("/data/protocol/");
		this.inPdfName = contextPath + borrowId + "_" + System.currentTimeMillis() + ".pdf";
		this.pdf = PdfHelper.instance(inPdfName);
		this.borrowId = borrowId;
		this.borrow = BorrowModel.instance(borrowService.getBorrowById(borrowId));
		this.downloadFileName = borrow.getName() + ".pdf";
		this.borrowUser = borrow.getUser();
		this.protocol = protocol;
		this.tenderList = borrowTenderService.getTenderList(borrowId);
		this.repaymentList = borrowRepaymentService.getRepaymentByBorrowId(borrowId);
		this.userId = userId;
		if (tenderId != 0) {
			this.tenderId = tenderId;
			this.tender = BorrowTenderModel.instance(borrowTenderService.getTenderById(tenderId));
			this.tenderUser = this.tender.getUser();
			this.interestFee = borrowCollectionService.sumInterestFeeByTender(tender);
		}
		VerifyLog log = verifyLogService.findByType(borrowId, "borrow", 2);
		Date fullTime = new Date();
		if(log!=null)
		{
			fullTime = verifyLogService.findByType(borrowId, "borrow", 2).getTime();
		}
		this.vetifyTime = DateUtil.dateStr6(fullTime);
		if(borrow.getType() == borrow.TYPE_SECOND){
			this.expirationDate = vetifyTime;
		}else{
			if(borrow.getBorrowTimeType() == 1){//天标
				this.expirationDate = DateUtil.dateStr6(DateUtil.rollDay(fullTime, borrow.getTimeLimit()));
			}else{
				this.expirationDate = DateUtil.dateStr6(DateUtil.rollMon(fullTime, borrow.getTimeLimit()));
			}
		}
		this.monthlyRepayDate = DateUtil.getDay(fullTime);
		int tenderOrder = borrowTenderService.getUserBorrowTenderOrder(tenderId,borrowId);
		this.protocolNo = this.borrow.getBidNo() + "-" +  String.format("%03d",tenderOrder);
		// 业务预处理
		prepare();
		// 下载协议校验
		validDownload();
		// 初始化参数
		initData();
		String content = protocol.getContent();
		//备注：因为%存到数据库中会产生乱码，故存储为percent!这样不会产生乱码
		if(protocol.getContent().contains("percent!"))
		{
			content = content.replaceAll("percent!", "%");
		}
		if(protocol.getContent().contains("space!"))
		{
			content = content.replaceAll("space!", "&nbsp;");
		}
		content = content.replaceAll("<p>&nbsp;</p>", "");
		if(content.contains("line-height:"))
		{
			String fontSize = content.substring(content.indexOf("line-height:")+12, content.indexOf("px;"));
			content = content.replace(fontSize, "30");
		}
		String out = "";
		try {
			out = FreemarkerUtil.renderTemplate(content, this.data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("生成协议内容出错：" + e.getMessage());
		} 
		return out;
	}


	@Override
	public abstract void prepare();

	@Override
	public void validDownload() {
		// 判断是否有模板
		if (this.protocol == null || StringUtil.isBlank(this.protocol.getContent())) {
			throw new BorrowException("读取协议模板出错！");
		}
		// 判断标是否存在
		if (this.borrow != null) {
			int status = this.borrow.getStatus();
			boolean isDown = status == 0 || status == 1 || status == 3 || status == 6 || status == 7 || status == 8;
			// 判断标是否状态异常
			if (!isDown) {
				throw new BorrowException("借款标状态异常！");
			}
		} else {
			throw new BorrowException("借款标不存在！");
		}
		// 判断用户是否存在
		if (userId == 0) {
			throw new BorrowException("用户信息异常！");
		}
	}

	@Override
	public void initData() {
		data.put("protocolNo", protocolNo);
		data.put("vetifyTime", vetifyTime); //添加审核时间
		data.put("borrowId", borrowId);
		data.put("bidNo", borrow.getBidNo());
		data.put("borrowAccount", MoneyUtil.convert(this.borrow.getAccount()+""));
		if (this.borrow.getVouchFirm() != null) {
			data.put("vouchFirmCache", this.borrow.getVouchFirm().getUserCache());
		}
		data.put("borrowUser", this.borrowUser);
		data.put("borrowUserCache", this.borrowUser.getUserCache());
		data.put("tenderList", tenderList);
		data.put("userId", userId);
		data.put("repaymentList", repaymentList);
		data.put("expirationDate", expirationDate);
		data.put("monthlyRepayDate", monthlyRepayDate);
		data.put("interestFee", interestFee);
		data.put("interestRate", Global.getValue("borrow_fee"));
		data.put("overdue_fee", Global.getValue("overdue_fee"));
		data.put("manageFee", BaseTPPWay.formatMoney(borrow.getBorrowManageRate()/100*borrow.getAccountYes()));
		data.put("nowDate", DateUtil.dateStr6(new Date()));
		if (this.tenderId != 0) {
			data.put("tenderId", this.tenderId);
			data.put("tender", this.tender);
			data.put("tenderUser", this.tenderUser);
			if(this.tender.getInterestRateValue()>0)
			{
				this.borrow.setApr(borrow.getApr() + tender.getInterestRateValue());
			}
		}
		data.put("borrow", this.borrow);
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
			String content = protocol.getContent();
			//备注：因为%存到数据库中会产生乱码，故存储为percent!这样不会产生乱码
			if(protocol.getContent().contains("percent!"))
			{
				content = content.replaceAll("percent!", "%");
			}
			if(protocol.getContent().contains("space!"))
			{
				content = content.replaceAll("space!", "&nbsp;");
			}
			String out = FreemarkerUtil.renderTemplate(content, this.data);
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
