package com.rongdu.p2psys.web.ppfund;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.core.protocol.AbstractProtocolForPpfundBean;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.service.PpfundProtocolService;
import com.rongdu.p2psys.ppfund.service.PpfundService;

@SuppressWarnings("rawtypes")
public class PpfundProtocolAction extends BaseAction {
	private static Logger logger = Logger.getLogger(PpfundProtocolAction.class);
	@Resource
	private PpfundProtocolService ppfundProtocolService;
	@Resource
	private PpfundService ppfundService;

	/**
	 * 下载协议
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/invest/inProtocol")
	public String inProtocol() throws Exception {
		long inId = paramLong("inId");
		AbstractProtocolForPpfundBean protocolBean = ppfundProtocolService.buildPpfundInProtocol(inId);
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
