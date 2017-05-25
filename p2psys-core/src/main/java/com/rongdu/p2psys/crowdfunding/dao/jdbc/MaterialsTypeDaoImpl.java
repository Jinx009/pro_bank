package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.MaterialsTypeDao;
import com.rongdu.p2psys.crowdfunding.domain.MaterialsType;

@Repository("materialsTypeDao")
public class MaterialsTypeDaoImpl extends BaseDaoImpl<MaterialsType> implements MaterialsTypeDao {

}
