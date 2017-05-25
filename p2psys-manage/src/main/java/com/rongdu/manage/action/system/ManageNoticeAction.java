package com.rongdu.manage.action.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.service.NoticeTypeService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 通知配置
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-24
 */
public class ManageNoticeAction extends BaseAction implements ModelDriven<NoticeType> {

	@Resource
	private NoticeTypeService noticeTypeService;
	@Resource
	private DictService dictService;

	private NoticeType noticeType = new NoticeType();
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 通知列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeManager")
	public String noticeManager() throws Exception {
		List<Dict> dictList = dictService.list("email_address");
		request.setAttribute("dictList", dictList);
		return "noticeManager";
	}

	/**
	 * 通知配置列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeList")
	public void noticeList() throws Exception {
		int page = paramInt("page");
		int rows = paramInt("rows");
		PageDataList<NoticeType> pageList = noticeTypeService.list(page, rows, noticeType);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 修改配置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeEditPage")
	public String noticeEditPage() throws Exception {
		NoticeType noticeType = noticeTypeService.findById(paramLong("id"));
		request.setAttribute("notice", noticeType);
		return "noticeEditPage";
	}

	/**
	 * 修改配置
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeEdit")
	public void noticeEdit() throws Exception {
		request.getAttribute("templet");
		noticeType.setUpdateTime(new Date());
		noticeType.setUpdateIp(Global.getIP());
		noticeTypeService.update(noticeType);
		printResult("修改成功！", true);
	}

	/**
	 * 设置该通知为启用，不启用
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeDelete")
	public void noticeDelete() throws Exception {
		NoticeType notice = noticeTypeService.findById(noticeType.getId());
		notice.setSend(noticeType.getSend());
		notice.setUpdateTime(new Date());
		notice.setUpdateIp(Global.getIP());
		noticeTypeService.update(notice);
		printResult("设置成功！", true);
	}

	/**
	 * 新增配置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeAddPage")
	public String noticeAddPage() throws Exception {
		return "noticeAddPage";
	}

	/**
	 * 新增配置
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/notice/noticeAdd")
	public void noticeAdd() throws Exception {
		noticeType.setAddTime(new Date());
		noticeType.setAddIp(Global.getIP());
		noticeTypeService.add(noticeType);
		printResult("保存成功！", true);
	}

	@Override
	public NoticeType getModel() {
		return noticeType;
	}
}
