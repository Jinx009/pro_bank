package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.model.DictModel;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 数据字典管理
 * 
 * @author cx
 * @version 2.0
 * @since 2014年3月21日
 */
public class ManageDictAction extends BaseAction implements ModelDriven<DictModel> {

	private DictModel model = new DictModel();

	public DictModel getModel() {
		return model;
	}

	@Resource
	private DictService dictService;

	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 数据字典页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/dict/dictManager")
	public String dictManager() throws Exception {
		return "dictManager";
	}

	/**
	 * 数据字典列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/dict/dictList")
	public void dictList() throws Exception {
		PageDataList<Dict> pageList = dictService.list(model);
		int totalPage = pageList.getPage().getTotal(); // 总页数
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 数据字典新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/dict/dictAddPage")
	public String dictAddPage() throws Exception {
		model.setSort(model.getSort() + 1);
		request.setAttribute("dict", model);
		return "dictAddPage";
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/dict/dictAdd")
	public void dictAdd() throws Exception {
		dictService.add(model.prototype());
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	/**
	 * 数据字典修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/dict/dictEditPage")
	public String dictEditPage() throws Exception {
		Dict dict = dictService.find(this.model.getId());
		request.setAttribute("dict", dict);
		return "dictEditPage";
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/dict/dictEdit")
	public void dictEdit() throws Exception {
		dictService.update(model.prototype());
		printResult(MessageUtil.getMessage("I10002"), true);
	}

}
