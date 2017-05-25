package com.rongdu.manage.action.payment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.model.UnllBankRet;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.model.ChannelBankModel;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelUrlService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

public class ChannelBankAction extends BaseAction<ChannelBankModel> implements ModelDriven<ChannelBankModel> {

	@Resource
	private IChannelBankService channelBankService;
	
	@Resource
	private IChannelUrlService channelUrlService;
	
	private Map<String, Object> data;
    
    private ChannelBankModel model = new ChannelBankModel();

	@Action(value="/modules/payment/channelBank")
	public String channelBank(){
		return "channelBank";
	}
	
	/**
	 * 分页展示
	 * @throws IOException
	 */
	@Action(value="/modules/payment/channelBankList")
	public void getChannelBankList() throws IOException{
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String searchName = paramString("searchName");

		PageDataList<ChannelBankModel> recordList = channelBankService.findChannelBankByItem(pageNumber, pageSize, searchName);
		data.put("total",recordList.getPage().getTotal()); // 总行数
		data.put("rows", recordList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 解绑银行卡
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/unbindUrl")
	public void unbindUrl() throws IOException{
		data = new HashMap<String, Object>();
		long id = this.paramLong("id");
		ChannelBankModel model = channelBankService.loadChannelBankById(id);
		String bindId = model.getBindId();
		String channelKey = model.getChannelKey();
		if(channelKey.equals(ConstantUtil.channelKey.unionpay.getValue())){//银联解绑
			UnionPayRet ret = SignHelper.unbind(bindId);
			if(ret.getRetCode().equals("0000")){
				//更新通道管理表，更新银行卡信息表
				model.setStatus(0);
				channelBankService.updateChannelBank(model);
				data.put("result", true);
				data.put("msg", "解绑成功！");
			}else{
				data.put("result", false);
				data.put("msg", "解绑失败！");
			}
		}else if(channelKey.equals(ConstantUtil.channelKey.llpay.getValue())){//连连解绑
			String userId = String.valueOf(model.getUser().getUserId());
			UnllBankRet ret = LianlPayHelper.unbind(bindId, userId);
			if(ret.getRet_code().equals("0000")){
				//更新通道管理表，更新银行卡信息表
				model.setStatus(0);
				channelBankService.updateChannelBank(model);
				data.put("result", true);
				data.put("msg", "解绑成功！");
			}else{
				data.put("result", false);
				data.put("msg", "解绑失败！");
			}
		}
		printJson(getStringOfJpaObj(data));
	}

	public ChannelBankModel getModel() {
		return model;
	}

	public void setModel(ChannelBankModel model) {
		this.model = model;
	}
}
