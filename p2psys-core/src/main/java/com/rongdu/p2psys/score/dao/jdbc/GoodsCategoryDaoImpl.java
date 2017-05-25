package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.score.dao.GoodsCategoryDao;
import com.rongdu.p2psys.score.domain.GoodsCategory;

@Repository("goodsCategoryDao")
public class GoodsCategoryDaoImpl extends BaseDaoImpl<GoodsCategory> implements GoodsCategoryDao {
    
}
