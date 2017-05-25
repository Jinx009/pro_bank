package com.rongdu.p2psys.nb.protocol.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.model.ProductTypeModel;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;

public class ProtocolConfigModel extends ProtocolConfig {

	private static final long serialVersionUID = -9126745910386693699L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;
	
	/*类型名称*/
	private String typeName;
	/*协议内容*/
	private String content;
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static ProtocolConfigModel instance(ProtocolConfig protocol) {
		ProtocolConfigModel model = new ProtocolConfigModel();
		BeanUtils.copyProperties(protocol, model);
		return model;
    }

	public static ProtocolConfig transProtocolConfig(ProtocolConfigModel model) {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		BeanUtils.copyProperties(model, protocolConfig);
		return protocolConfig;
	}


	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
