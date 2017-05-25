package com.rongdu.p2psys.nb.protocol.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.nb.product.dao.ProductTypeDao;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolConfigDao;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;
import com.rongdu.p2psys.nb.protocol.model.ProtocolConfigModel;
import com.rongdu.p2psys.nb.protocol.service.ProtocolService;

@Service("protocolService")
public class ProtocolServiceImpl implements ProtocolService {

	@Resource
	private ProtocolConfigDao protocolConfigDao;
	@Resource
	private ProductTypeDao productTypeDao;
	@Resource
	private ProtocolDao protocolDao;

	@Override
	public PageDataList<ProtocolConfigModel> getModelPageList(int pageNumber,
			int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		PageDataList<ProtocolConfig> productTypePageList = protocolConfigDao
				.findPageList(param);

		List<ProtocolConfigModel> list = new ArrayList<ProtocolConfigModel>();
		for (ProtocolConfig pConfig : productTypePageList.getList()) {
			ProtocolConfigModel model = ProtocolConfigModel.instance(pConfig);
			ProductType pType = productTypeDao.findObjByProperty("typeCode",
					pConfig.getTypeCode());
			if (pType != null) {
				model.setTypeName(pType.getTypeName());
			}
			list.add(model);
		}

		// 封装成带分页的集合
		PageDataList<ProtocolConfigModel> pageList = new PageDataList<ProtocolConfigModel>();
		pageList.setPage(productTypePageList.getPage());
		pageList.setList(list);

		return pageList;
	}

	@Override
	public ProtocolConfig saveProtocolConfig(ProtocolConfigModel protocolModel) {
		protocolModel.setAddTime(new Date());
		protocolModel.setAddIp(Global.getIP());
		ProtocolConfig pConfig = ProtocolConfigModel
				.transProtocolConfig(protocolModel);
		return protocolConfigDao.save(pConfig);
	}

	public Protocol saveOrUpdateProtocolContent(ProtocolConfigModel typeModel) {
		Protocol p = null;
		if (typeModel.getId() == 0) {
			p = new Protocol();
			p.setNid(typeModel.getNid());
			p.setContent(typeModel.getContent());
			protocolDao.save(p);
		} else {
			p = new Protocol();
			p.setId(typeModel.getId());
			p.setNid(typeModel.getNid());
			p.setContent(typeModel.getContent());
			protocolDao.update(p);
		}
		return p;
	}

	@Override
	public ProtocolConfig updateProtocolConfig(ProtocolConfigModel protocolModel) {
		ProtocolConfig pConfig = ProtocolConfigModel
				.transProtocolConfig(protocolModel);
		return protocolConfigDao.update(pConfig);
	}

	@Override
	public ProtocolConfig findById(Long id) {
		return protocolConfigDao.find(id);
	}

	@Override
	public Long getNextProtocolType() {
		return protocolConfigDao.getNextProtocolType();
	}

	@Override
	public List<ProtocolConfig> findProtocolListByType(Integer typeCode) {
		return protocolConfigDao.findByProperty("typeCode", typeCode);
	}

	@Override
	public Protocol findContentByNid(String nid) {
		return protocolDao.findObjByProperty("nid", nid);
	}

}
