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
import com.rongdu.p2psys.nb.payment.model.ChannelUrlModel;
import com.rongdu.p2psys.nb.payment.service.IChannelUrlService;

public class ChannelUrlAction extends BaseAction<ChannelUrlModel> implements ModelDriven<ChannelUrlModel> {

	@Resource
	private IChannelUrlService channelUrlService;
	
	private Map<String, Object> data;
    
    private ChannelUrlModel model = new ChannelUrlModel();

	@Action(value="/modules/payment/channelUrl")
	public String channelUrl(){
		return "channelUrl";
	}
	
	@Action(value="/modules/payment/channelUrlAdd")
	public String channelUrlAdd(){
		List<Channel> clist = channelUrlService.loadChannelList();
		request.setAttribute("clist", clist);
		return "channelUrlAdd";
	}
	
	/**
	 * 分页展示
	 * @throws IOException
	 */
	@Action(value="/modules/payment/channelUrlList")
	public void getChannelUrlList() throws IOException{
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String searchName = paramString("searchName");

		PageDataList<ChannelUrlModel> recordList = channelUrlService.findChannelUrlByItem(pageNumber, pageSize, searchName);
		data.put("total",recordList.getPage().getTotal()); // 总行数
		data.put("rows", recordList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 编辑-页面
	 * @return
	 */
	@Action(value="/modules/payment/channelUrlEdit")
	public String editChannelUrl(){
		long id = paramLong("id");
		ChannelUrlModel model = channelUrlService.loadChannelUrlById(id);
		List<Channel> clist = channelUrlService.loadChannelList();
		request.setAttribute("channelUrlModel", model);
		request.setAttribute("clist", clist);
		return "channelUrlEdit";
	}
	
	/**
	 * 编辑-处理
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/channelUrlUpdate")
	public void updateChannelUrl() throws IOException{
		data = new HashMap<String, Object>();
		Channel channel = new Channel();
		channel.setId(model.getCid());
		model.setChanel(channel);
		channelUrlService.updateChannelUrl(model);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 通道URL添加
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/channelUrlSave")
	public void saveChannel() throws IOException{
		data = new HashMap<String, Object>();
		Channel channel = new Channel();
		channel.setId(model.getCid());
		model.setChanel(channel);
		channelUrlService.saveChannelUrl(model);
		data.put("result", true);
		data.put("msg", "添加成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/channelUrlDelete")
	public void deleteChannelUrl() throws IOException{
		data = new HashMap<String, Object>();
		long id = this.paramLong("id");
		channelUrlService.deleteChannelUrlById(id);
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	

	public ChannelUrlModel getModel() {
		return model;
	}

	public void setModel(ChannelUrlModel model) {
		this.model = model;
	}
}
