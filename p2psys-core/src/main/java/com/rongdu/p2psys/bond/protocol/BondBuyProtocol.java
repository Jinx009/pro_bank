package com.rongdu.p2psys.bond.protocol;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.util.itext.PdfHelper;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;

/**
 * 债权受让人下载协议实现类
 * 
 * @author wzh
 * @version 2.0
 * @since 2014-12-31
 */
public class BondBuyProtocol extends AbstractProtocolBean {
	
	private static Logger logger = Logger.getLogger(BondBuyProtocol.class);

	@Override
	public void validDownload() {
		
		// 判断是否有模板
		if (this.protocol == null || StringUtil.isBlank(this.protocol.getContent())) {
			throw new BorrowException("读取协议模板出错！");
		}
		
//		// 先调用父类基础校验
//		super.validDownload();
//		if (tender == null || tenderId == 0) {
//			throw new BorrowException("你不是投资人不能下载该协议！");
//		}
	}
	
	
	/**
	 * 业务核心处理方法
	 */
	@Override
	public void executer(long bondId, Protocol protocol, long userId) {
		// 初始化基础参数
		logger.info("初始化ProtocolBean");
//		this.borrowService = (BorrowService) BeanUtil.getBean("borrowService");
//		this.borrowTenderService = (BorrowTenderService) BeanUtil.getBean("borrowTenderService");
//		this.borrowCollectionService = (BorrowCollectionService) BeanUtil.getBean("borrowCollectionService");
//		this.verifyLogService = (VerifyLogService) BeanUtil.getBean("verifyLogService");
//		this.userService = (UserService) BeanUtil.getBean("userService");
//		this.borrowProtocolService = (BorrowProtocolService) BeanUtil.getBean("borrowProtocolService");
//		this.borrowRepaymentService = (BorrowRepaymentService) BeanUtil.getBean("borrowRepaymentService");
		String contextPath = ServletActionContext.getServletContext().getRealPath("/data/protocol/");
		contextPath += File.separator;
		this.downloadFileName = bondId + ".pdf";
		this.inPdfName = contextPath + bondId + "_" + System.currentTimeMillis() + ".pdf";
		this.pdf = PdfHelper.instance(inPdfName);
//		this.borrowId = borrowId;
//		this.borrow = BorrowModel.instance(borrowService.getBorrowById(borrowId));
//		this.borrowUser = borrow.getUser();
		this.protocol = protocol;
//		this.tenderList = borrowTenderService.getTenderList(borrowId);
//		this.repaymentList = borrowRepaymentService.getRepaymentByBorrowId(borrowId);
		this.userId = userId;
//		if (tenderId != 0) {
//			this.tenderId = tenderId;
//			this.tender = BorrowTenderModel.instance(borrowTenderService.getTenderById(tenderId));
//			this.tenderUser = this.tender.getUser();
//		}
		//this.vetifyTime = DateUtil.dateStr2(verifyLogService.findByType(borrowId, "borrow", 2).getTime());
		// 业务预处理
		prepare();
		// 下载协议校验
		validDownload();
		// 初始化参数
		initData();
		// 创建pdf逻辑
		createPdf();
	}	
	

	@Override
	public void prepare() {
	}
	
	@Override
	public void initData() {
	}

}
