package com.rongdu.p2psys.ppfund.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.constant.ProtocolConstant;
import com.rongdu.p2psys.core.protocol.AbstractProtocolForPpfundBean;
import com.rongdu.p2psys.core.protocol.ProtocolHelper;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.service.PpfundProtocolService;

/**
 * PPfund下载协议
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月15日
 */
@Service("ppfundProtocolService")
public class PpfundProtocolServiceImpl implements PpfundProtocolService {
	@Resource
	private PpfundInDao ppfundInDao;
	@Resource
	private ProtocolDao protocolDao;
	
	@Override
	public AbstractProtocolForPpfundBean buildPpfundInProtocol(long inId) {
		PpfundIn ppfundIn = ppfundInDao.find(inId);
		Ppfund ppfund = ppfundIn.getPpfund();
		Protocol protocol = new Protocol();
		if(ppfund.getIsFixedTerm() == 1){
			protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_PPFUND).get(0);
		} else {
			protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_PPFUND_CURRENT).get(0);
		}
		AbstractProtocolForPpfundBean protocolBean = ProtocolHelper.doProtocolForPpfund(ProtocolConstant.BASE_FOR_PPFUND);
		protocolBean.executer(ppfund.getId(), inId, protocol, ppfundIn.getUser().getUserId());
		return protocolBean;
	}
	
	@Override
	public String buildPpfundProtocol(String type,long inId)
	{
		String content = "";
		if(type.equals("ppfund"))
		{
			PpfundIn ppfundIn = ppfundInDao.find(inId);
			Ppfund ppfund = ppfundIn.getPpfund();
			Protocol protocol = new Protocol();
			if(ppfund.getIsFixedTerm() == 1){
				protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_PPFUND).get(0);
			} else {
				protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_PPFUND_CURRENT).get(0);
			}
			AbstractProtocolForPpfundBean protocolBean = ProtocolHelper.doProtocolForPpfund(ProtocolConstant.BASE_FOR_PPFUND);
			content = protocolBean.generatePpfundProtocol(ppfund.getId(), inId, protocol, ppfundIn.getUser().getUserId());
		}else {
			Protocol protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_PPFUND_CURRENT).get(0);
			content = protocol.getContent();
			if(protocol.getContent().contains("percent!"))
			{
				content = content.replaceAll("percent!", "%");
			}
			if(protocol.getContent().contains("<p >"))
			{
				content = content.replaceAll("<p >", "<p>");
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
			content = content.replaceAll("(\\$)|(\\{.+?\\})", "   ");
		}
		return content;
	}

}
