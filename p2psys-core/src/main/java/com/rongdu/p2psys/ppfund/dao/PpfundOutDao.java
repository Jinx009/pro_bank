package com.rongdu.p2psys.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.model.PpfundOutModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund转出
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
public interface PpfundOutDao extends BaseDao<PpfundOut> {
	
	double dayOutMoney(User user);
	
	double ppfundOutMoney(PpfundIn in);
	
}
