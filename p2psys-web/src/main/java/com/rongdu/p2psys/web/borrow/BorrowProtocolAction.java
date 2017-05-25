package com.rongdu.p2psys.web.borrow;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.borrow.service.BorrowProtocolService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;

/**
 * 协议下载
 * 
 * @author qj
 * @version 2.0
 * @since 2014年3月17日
 */
@SuppressWarnings("rawtypes")
public class BorrowProtocolAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(BorrowProtocolAction.class);
	@Resource
	private BorrowProtocolService borrowProtocolService;
	@Resource
	private BorrowService borrowService;

	/**
	 * 下载协议
	 * 
	 * @return
	 */
	@Action("/member/borrow/borrowerProtocol")
	public String borrowerProtocol() throws Exception {
		User user = getSessionUser();
		long borrowId = paramLong("borrowId");
		AbstractProtocolBean protocolBean = borrowProtocolService.buildBorrowerProtocol(borrowId, user.getUserId());
		try {
			generateDownloadFile(protocolBean.getInPdfName(), protocolBean.getDownloadFileName());
		} catch (FileNotFoundException e) {
			logger.error("协议pdf文件" + protocolBean.getDownloadFileName() + "未找到！");
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 下载协议
	 * 
	 * @return
	 */
	@Action("/invest/tenderProtocol")
	public String tenderProtocol() throws Exception {
		long tenderId = paramLong("tenderId");
		AbstractProtocolBean protocolBean = borrowProtocolService.buildTenderProtocol(tenderId);
		try {
			generateDownloadFile(protocolBean.getInPdfName(), protocolBean.getDownloadFileName());
		} catch (FileNotFoundException e) {
			logger.error("协议pdf文件" + protocolBean.getDownloadFileName() + "未找到！");
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
