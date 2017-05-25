package com.rongdu.p2psys.web.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.user.service.UserRedPacketService;

/**
 * 用户认证信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月20日
 */
public class UserRedPacketAction extends BaseAction<UserRedPacketModel> implements ModelDriven<UserRedPacketModel> {

	@Resource
	private UserRedPacketService userRedPacketService;

	private Map<String, Object> data;

	/**
	 * 我的红包页面
	 * @return
	 * @throws Exception
	 */
	@Action("/member/redpacket/availableRedPacket")
	public String availableRedPacket() {
		return "availableRedPacket";
	}
	
	/**
	 * 我的红包数据
	 * @throws IOException 
	 */
	@Action("/member/redpacket/availableRedPacketJSON")
	public void availableRedPacketJSON() throws IOException {
		model.setId(getSessionUserId());
		data = new HashMap<String, Object>();
		PageDataList<UserRedPacketModel> pagaDataList = userRedPacketService.findByModel(model);
		int total = pagaDataList.getPage().getTotal();// 总记录数
		data.put("total", total);
		data.put("data", pagaDataList);
		printWebJson(getStringOfJpaObj(data));
	}
	/**
	 * 现金红包兑换
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/exchangeRedPacket")
	public void exchangeRedPacket() throws Exception {
		User user = getSessionUser();
		String[] redPacketIds = request.getParameterValues("redPacket_id");
		if(redPacketIds == null || redPacketIds.length == 0){
			throw new BussinessException("请选择红包", 2);
		}
		if(userRedPacketService.checkIsCashRedPacket(redPacketIds)){
			throw new BussinessException("参数非法，请检查您选择的红包类型", 2);
		}
		if(redPacketIds!=null&&redPacketIds.length>0)
		{
			for(int i=0;i<redPacketIds.length;i++)
			{
				long redPacketId = NumberUtil.getLong(redPacketIds[i]);
				UserRedPacket uRedPacket = userRedPacketService.findUserRedPacketById(redPacketId);
				if(uRedPacket.isUsed())
				{
//					throw new BussinessException("红包已经使用，请重新选择！", 2);
					printWebResult("红包已经使用，请重新选择！", false);
					throw new BussinessException("红包已经使用，请重新选择！", 2);
				}
			}
		}
		userRedPacketService.doExchangeRedPacket(redPacketIds, user);
		printWebResult("兑换成功", true);
	}
}
