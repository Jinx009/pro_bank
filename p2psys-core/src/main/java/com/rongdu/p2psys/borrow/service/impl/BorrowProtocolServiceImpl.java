package com.rongdu.p2psys.borrow.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.service.BorrowProtocolService;
import com.rongdu.p2psys.core.constant.ProtocolConstant;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.protocol.ProtocolHelper;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolConfigDao;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;

@Service("borrowProtocolService")
public class BorrowProtocolServiceImpl implements BorrowProtocolService {

	@Resource
	private ProtocolDao protocolDao;
	@Resource
	private ProtocolConfigDao protocolConfigDao;
	@Resource
	private BorrowTenderDao borrowTenderDao;
	@Resource
	private BorrowDao borrowDao;

	/**
	 * 借款人协议下载
	 * 
	 * @param borrowId
	 * @param userId
	 * @return
	 */
	@Override
	public AbstractProtocolBean buildBorrowerProtocol(long borrowId, long userId) {
		Protocol protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_BORROWER).get(0);
		AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol(ProtocolConstant.BASE_FOR_BORROWER);
		protocolBean.executer(borrowId, protocol, userId);
		return protocolBean;
	}

	/**
	 * 投资人协议下载
	 * 
	 * @param borrowId
	 * @param userId
	 * @param tenderId
	 * @return
	 */
	@Override
	public AbstractProtocolBean buildTenderProtocol(long tenderId) {
		BorrowTender tender = borrowTenderDao.find(tenderId);
		ProtocolConfig pConfig = protocolConfigDao.findObjByProperty("protocolType",tender.getBorrow().getProtocolType());
		Protocol protocol = protocolDao.findByProperty("nid",pConfig.getNid()).get(0);
		AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol(ProtocolConstant.BASE_FOR_TENDER);
		protocolBean.executer(tender.getBorrow().getId(), tender.getId(), protocol, tender.getUser().getUserId());
		if(tender.getBorrow().getProtocolType() == 8)
		{
			byte[] buf = null;
			try {
				String downLoadName = protocolBean.getDownloadFileName().replaceAll("pdf", "zip");
	            String filePath = ServletActionContext.getServletContext().getRealPath("/data/") + File.separator;
	            String inPdfName = filePath + downLoadName;
	            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filePath + downLoadName));
				String[] filePaths = {filePath + "爱秀艺-公司章程.pdf", filePath + "爱秀艺-基础资料.pdf",protocolBean.getInPdfName()} ;
	            for(int i=0;i<filePaths.length;i++){
	            	FileInputStream in=new FileInputStream(filePaths[i]);
	            	String fileName = filePaths[i].substring(filePaths[i].lastIndexOf(File.separator)+1, filePaths[i].length());
	            	buf = new byte[in.available()];
	            	if(i<2)
	            	{
	            		out.putNextEntry(new ZipEntry(fileName));
	            	}else
	            	{
	            		out.putNextEntry(new ZipEntry("爱秀艺-投资协议.pdf"));
	            	}
		            int len;
		            while((len=in.read(buf))>0){
		                 out.write(buf,0,len);
		            }
		            out.setEncoding("gbk");
		            out.closeEntry();
		            in.close();
	            }
	            out.close();
	            protocolBean.setInPdfName(inPdfName);
	            protocolBean.setDownloadFileName(downLoadName);
			}catch(Exception e)
			{
				throw new BorrowException("生成协议包出错： "+ e.getMessage());
			}
		}
		
		return protocolBean;
	}

	/**
	 * 平台协议下载
	 * 
	 * @param borrowId
	 * @param userId
	 * @param operatorId
	 * @return
	 */
	@Override
	public AbstractProtocolBean buildWebProtocol(long borrowId, long userId, long operatorId) {
		Protocol protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_WEB).get(0);
		AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol(ProtocolConstant.BASE_FOR_WEB);
		protocolBean.executer(borrowId, protocol, userId);
		return protocolBean;
	}

}
