package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeModel;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 短信邮件管理Action
 * 
 * @author zf
 * @version 2.0
 * @since 2014-05-20
 */
public class SystemNoticeAction extends BaseAction implements ModelDriven<NoticeModel> {

	private NoticeModel model = new NoticeModel();

	@Override
	public NoticeModel getModel() {
		return model;
	}

	/**
	 * 转换JSON字符串用map
	 */
	private Map<String, Object> data = new HashMap<String, Object>();

	@Resource
	private NoticeService noticeService;

	/**
	 * 信息管理
	 * 
	 * @return 返回页面
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/message/noticeManager")
	public String noticeManager() throws Exception {
		return "noticeManager";
	}

	/**
	 * 信息列表
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/message/noticeList")
	public void noticeList() throws Exception {
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		PageDataList<NoticeModel> pagaDataList = noticeService.noticeList(model, pageNumber, pageSize);

		data.put("total", pagaDataList.getPage().getTotal()); // 总行数
		data.put("rows", pagaDataList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 查看信息内容
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/message/contentPage")
	public String contentPage() throws Exception {
		request.setAttribute("content", noticeService.findById(paramLong("id")).getContent());
		return "contentPage";
	}

}
