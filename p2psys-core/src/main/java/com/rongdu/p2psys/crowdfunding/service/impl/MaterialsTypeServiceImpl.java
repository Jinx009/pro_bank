package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.MaterialsTypeDao;
import com.rongdu.p2psys.crowdfunding.domain.MaterialsType;
import com.rongdu.p2psys.crowdfunding.service.MaterialsTypeService;

@Service("materialsTypeService")
public class MaterialsTypeServiceImpl implements MaterialsTypeService {

	@Resource
	private MaterialsTypeDao materialsTypeDao;
	
	public List<MaterialsType> getList() {
		return materialsTypeDao.findAll();
	}

}
