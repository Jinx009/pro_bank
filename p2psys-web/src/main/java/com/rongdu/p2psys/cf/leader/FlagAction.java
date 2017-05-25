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

public class FlagAction extends BaseAction<Flag> implements ModelDriven<Flag>{

	@Resource
	private FlagService flagService;
	
	private Map<String,Object> data;

	/**
	 * 获取行业标签
	 * @throws IOException
	 */
	@Action(value="/cf/flagData")
	public void flagData() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		
		List<Flag> list = flagService.findAll();
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
