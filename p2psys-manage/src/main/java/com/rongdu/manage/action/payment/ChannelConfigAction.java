package com.rongdu.manage.action.payment;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.domain.ChannelUrl;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IChannelUrlService;
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;

public class ChannelConfigAction extends BaseAction<ChannelConfigModel> implements ModelDriven<ChannelConfigModel> {

	@Resource
	private IChannelConfigService channelConfigService;
	
	@Resource
	private IChannelUrlService channelUrlService;
	@Resource
	private ISupportBankService theSupportBankService;
	
	private Map<String, Object> data;
    
    private ChannelConfigModel model = new ChannelConfigModel();

	@Action(value="/modules/payment/channelConfig")
	public String channelConfig(){
		return "channelConfig";
	}
	
	@Action(value="/modules/payment/channelConfigAdd")
	public String channelConfigAdd(){
		List<ChannelUrl> webList = channelUrlService.loadChannelUrlByType(0);//WEB端
		List<ChannelUrl> wapList = channelUrlService.loadChannelUrlByType(1);//微信端
		List<Channel> clist = channelUrlService.loadChannelList();//通道信息
		request.setAttribute("clist", clist);
		request.setAttribute("webList", webList);
		request.setAttribute("wapList", wapList);
		return "channelConfigAdd";
	}
	
	/**
	 * 分页展示
	 * @throws IOException
	 */
	@Action(value="/modules/payment/channelConfigList")
	public void getChannelConfigList() throws IOException{
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String searchName = paramString("searchName");

		PageDataList<ChannelConfigModel> recordList = channelConfigService.findChannelConfigByItem(pageNumber, pageSize, searchName);
		data.put("total",recordList.getPage().getTotal()); // 总行数
		data.put("rows", recordList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 编辑-页面
	 * @return
	 */
	@Action(value="/modules/payment/channelConfigEdit")
	public String editChannelConfig(){
		long id = paramLong("id");
		ChannelConfigModel model = channelConfigService.loadChannelConfigById(id);
		/*if(model.getGatewayKey().equals("web_recharge_key")){//WEB端充值
			List<ChannelUrl> webList = channelUrlService.loadChannelUrlByType(0);//WEB端
			request.setAttribute("urlList", webList);
			request.setAttribute("operType", 0);
		}else if(model.getGatewayKey().equals("web_cash_key")){//WEB端提现
			List<ChannelUrl> webList = channelUrlService.loadChannelUrlByType(0);//WEB端
			request.setAttribute("urlList", webList);
			request.setAttribute("operType", 1);
		}else if(model.getGatewayKey().equals("wap_recharge_key")){//微信端充值
			List<ChannelUrl> wapList = channelUrlService.loadChannelUrlByType(1);//微信端
			request.setAttribute("urlList", wapList);
			request.setAttribute("operType", 0);
		}else if(model.getGatewayKey().equals("wap_cash_key")){//微信端提现
			List<ChannelUrl> wapList = channelUrlService.loadChannelUrlByType(1);//微信端
			request.setAttribute("urlList", wapList);
			request.setAttribute("operType", 1);
		}*/
		List<Channel> clist = channelUrlService.loadChannelList();//通道信息
		request.setAttribute("clist", clist);
		List<NbSupportBank> sblist = theSupportBankService.loadSupportBankList();//银行信息
		request.setAttribute("sblist", sblist);
		
		request.setAttribute("channelConfigModel", model);
		return "channelConfigEdit";
	}
	
	/**
	 * 编辑-处理
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/channelConfigUpdate")
	public void updateChannelConfig() throws IOException{
		data = new HashMap<String, Object>();
		channelConfigService.updateChannelConfig(model);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 通道添加
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/channelConfigSave")
	public void saveChannel() throws IOException{
		data = new HashMap<String, Object>();
		channelConfigService.saveChannelConfig(model);
		data.put("result", true);
		data.put("msg", "添加成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/channelConfigDelete")
	public void deleteChannelConfig() throws IOException{
		data = new HashMap<String, Object>();
		long id = this.paramLong("id");
		channelConfigService.deleteChannelConfigById(id);
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	

	public ChannelConfigModel getModel() {
		return model;
	}

	public void setModel(ChannelConfigModel model) {
		this.model = model;
	}
}
