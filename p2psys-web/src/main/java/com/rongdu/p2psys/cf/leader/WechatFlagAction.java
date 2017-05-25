package com.rongdu.p2psys.cf.leader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Flag;
import com.rongdu.p2psys.crowdfunding.service.FlagService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

/**
 * 微信端产品标签
 * @author Jinx
 *
 */
public class WechatFlagAction extends BaseAction<Flag> implements ModelDriven<Flag>{

	private Map<String,Object> data;
	
	@Resource
	private FlagService flagService;

	/**
	 * 微信端产品标签 -- 数据
	 * @throws IOException
	 */
	@Action(value="/cf/wechat/flagData")
	public void flagData() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"微信端产品标签相关");
		
		List<Flag> list = flagService.findAll();
		data.put(ConstantUtil.DATA,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
