package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.model.BorrowConfigModel;
import com.rongdu.p2psys.borrow.service.BorrowConfigService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 标种配置管理
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月22日
 */
public class BorrowConfigAction extends BaseAction implements ModelDriven<BorrowConfigModel> {

	private BorrowConfigModel model = new BorrowConfigModel();

	public BorrowConfigModel getModel() {
		return model;
	}

	@Resource
	private BorrowConfigService borrowConfigService;

	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/borrowConfig/borrowConfigManager")
	public String borrowConfigManager() throws Exception {
		return "borrowConfigManager";
	}

	/**
	 * 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/borrowConfig/borrowConfigList")
	public void borrowConfigList() throws Exception {
		PageDataList<BorrowConfigModel> pageList = borrowConfigService.list(model);
		int totalPage = pageList.getPage().getTotal(); // 总页数
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/borrowConfig/borrowConfigAddPage")
	public String borrowConfigAddPage() throws Exception {
		saveToken("borrowConfigAddToken");
		return "borrowConfigAddPage";
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/borrowConfig/borrowConfigAdd")
	public void borrowConfigAdd() throws Exception {
		checkToken("borrowConfigAddToken");
		borrowConfigService.add(model.prototype());
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/borrowConfig/borrowConfigEditPage")
	public String borrowConfigEditPage() throws Exception {
		BorrowConfig borrowConfig = borrowConfigService.find(model.getId());
		if(borrowConfig != null){
			if (borrowConfig.isTrail()) {
				request.setAttribute("isTrail", 1);
			} else {
				request.setAttribute("isTrail", 0);
			}
			if (borrowConfig.isReview()) {
				request.setAttribute("isReview", 1);
			} else {
				request.setAttribute("isReview", 0);
			}
			if (borrowConfig.isEnable()) {
				request.setAttribute("isEnable", 1);
			} else {
				request.setAttribute("isEnable", 0);
			}
		}
		request.setAttribute("borrowConfig", borrowConfig);
		return "borrowConfigEditPage";
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/borrowConfig/borrowConfigEdit")
	public void borrowConfigEdit() throws Exception {
		int isTrail = paramInt("isTrail");
		if(isTrail == 1){
			model.setTrail(true);
		}else{
			model.setTrail(false);
		}
		int isReview = paramInt("isReview");
		if(isReview == 1){
			model.setReview(true);
		}else{
			model.setReview(false);
		}
		int enable = paramInt("enable");
		if(enable == 1){
			model.setEnable(true);
		}else{
			model.setEnable(false);
		}
		borrowConfigService.update(model.prototype());
		printResult(MessageUtil.getMessage("I10002"), true);
	}

}
