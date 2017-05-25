package com.rongdu.p2psys.nb.protocol.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;
import com.rongdu.p2psys.nb.protocol.model.ProtocolConfigModel;

public interface ProtocolService {

	/**
	 * 所有协议配置列表(分页)
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList<ProtocolConfigModel>
	 */
	PageDataList<ProtocolConfigModel> getModelPageList(int pageNumber,
			int pageSize);

	/**
	 * 保存协议配置
	 * 
	 * @param typeModel
	 * @return ProtocolConfig
	 */
	ProtocolConfig saveProtocolConfig(ProtocolConfigModel typeModel);

	/**
	 * 保存协议内容
	 * 
	 * @param typeModel
	 * @return Protocol
	 */
	Protocol saveOrUpdateProtocolContent(ProtocolConfigModel typeModel);

	/**
	 * 更新协议配置
	 * 
	 * @param typeModel
	 * @return ProtocolConfig
	 */
	ProtocolConfig updateProtocolConfig(ProtocolConfigModel typeModel);

	/**
	 * 根据ID获取协议配置
	 * 
	 * @param id
	 * @return ProtocolConfig
	 */
	ProtocolConfig findById(Long id);

	/**
	 * 获取协议内容
	 * 
	 * @param id
	 * @return Protocol
	 */
	Protocol findContentByNid(String nid);

	/**
	 * 获取下一个产品协议号
	 * 
	 * @return Long
	 */
	Long getNextProtocolType();

	/**
	 * 根据产品类型获取对应协议配置
	 * 
	 * @param typeCode
	 * @return
	 */
	List<ProtocolConfig> findProtocolListByType(Integer typeCode);

}
