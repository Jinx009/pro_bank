package com.rongdu.manage.action.crowdfunding;

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
 * 获取产品标签结构
 * @author Jinx
 *
 */
public class FlagAction extends BaseAction<Flag> implements ModelDriven<Flag> {

	private Map<String,Object> data;
	
	@Resource
	private FlagService flagService;

	/**
	 * 标签数据
	 * @throws IOException
	 */
	@Action(value = "/cf/flag/data")
	public void flagList() throws IOException{
		List<Flag> list = flagService.findAll();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
