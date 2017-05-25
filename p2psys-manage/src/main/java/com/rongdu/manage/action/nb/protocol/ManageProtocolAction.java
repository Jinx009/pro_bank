package com.rongdu.manage.action.nb.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;
import com.rongdu.p2psys.nb.protocol.model.ProtocolConfigModel;
import com.rongdu.p2psys.nb.protocol.service.ProtocolService;

public class ManageProtocolAction extends BaseAction<ProtocolConfigModel>
		implements ModelDriven<ProtocolConfigModel> {

	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private ProtocolService protocolService;

	private Map<String, Object> data;

	Logger logger = Logger.getLogger(ManageProtocolAction.class);

	@Action("/modules/nb/protocol/protocolManager")
	public String protocolManager() throws Exception {
		return "protocolManager";
	}

	@Action("/modules/nb/protocol/protocolList")
	public void protocolList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProtocolConfigModel> list = protocolService
				.getModelPageList(pageNumber, pageSize);
		model.setSize(pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	@Action("/modules/nb/protocol/addProtocol")
	public void addProtocol() throws Exception {
		// 保存产品信息
		model.setStatus(0);
		protocolService.saveProtocolConfig(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	@Action("/modules/nb/protocol/editProtocol")
	public void editProtocol() throws Exception {
		// 保存产品信息
		protocolService.updateProtocolConfig(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	@Action("/modules/nb/protocol/protocolAddPage")
	public String protocolAddPage() throws Exception {

		Long protocolType = protocolService.getNextProtocolType();

		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setProtocolType(protocolType);

		request.setAttribute("protocol", protocol);
		// 可选产品列表
		request.setAttribute("productTypes",
				productTypeService.getModelPageList(1, 2000).getList());

		return "protocolAddPage";
	}

	@Action("/modules/nb/protocol/protocolEditPage")
	public String protocolEditPage() throws Exception {

		Long id = paramLong("id");

		ProtocolConfig protocol = protocolService.findById(id);

		request.setAttribute("protocol", protocol);
		// 可选产品列表
		request.setAttribute("productTypes",
				productTypeService.getModelPageList(1, 2000).getList());

		return "protocolEditPage";
	}

	@Action("/modules/nb/protocol/protocolContentEditPage")
	public String protocolContentEditPage() throws Exception {

		Long id = paramLong("id");
		ProtocolConfig config = protocolService.findById(id);
		Protocol protocol = null;
		if (config == null || config.getNid() == null) {
			protocol = new Protocol();
			protocol.setId(0);
			protocol.setNid(" ");
			protocol.setContent(" ");
		} else {
			protocol = protocolService.findContentByNid(config.getNid());
		}
		request.setAttribute("protocol", protocol);
		// 可选产品列表
		request.setAttribute("productTypes",
				productTypeService.getModelPageList(1, 2000));

		return "protocolContentEditPage";
	}

	@Action("/modules/nb/protocol/editProtocolContent")
	public void editProtocolContent() throws Exception {
		// 保存产品信息
		protocolService.saveOrUpdateProtocolContent(model);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "操作成功！");
		printJson(getStringOfJpaObj(data));
	}

	@Action("/modules/nb/protocol/findProtocolListByType")
	public void findProtocolListByType() throws Exception {
		List<ProtocolConfig> list = protocolService
				.findProtocolListByType(paramInt("typeCode"));

		data = new HashMap<String, Object>();
		data.put("protocolList", list);
		printJson(getStringOfJpaObj(data));
	}

}
