package com.rongdu.p2psys.nb.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.user.dao.CouponCategoryDao;
import com.rongdu.p2psys.user.domain.CouponCategory;

@Repository("couponCategoryDao")
public class CouponCategoryDaoImpl extends BaseDaoImpl<CouponCategory>
		implements CouponCategoryDao {

}
