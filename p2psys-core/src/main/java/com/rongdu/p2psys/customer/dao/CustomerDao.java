package com.rongdu.p2psys.customer.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.customer.model.AccountLogModel;
import com.rongdu.p2psys.customer.model.CustomerBaseinfoModel;
import com.rongdu.p2psys.customer.model.CustomerProductModel;
import com.rongdu.p2psys.customer.model.CustomerRedPacketModel;
import com.rongdu.p2psys.customer.model.ReferrerModel;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * PPfund（资金管理产品)
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
public interface CustomerDao extends BaseDao<User> {
	/**
	 * 首页PPfund数据
	 * 
	 * @return
	 */
	PageDataList<CustomerProductModel> customerProductList(int pageNumber,
			int pageSize,CustomerProductModel model); 
	
	public PageDataList<CustomerBaseinfoModel> customerBaseinfoList(int pageNumber,
			int pageSize,CustomerBaseinfoModel model) ;
	
	public PageDataList<AccountLogModel> accountLogList(int pageNumber,
			int pageSize,AccountLogModel model);
	
	public PageDataList<ReferrerModel> referrerList(int pageNumber,
			int pageSize,ReferrerModel model);
	
	public PageDataList<CustomerRedPacketModel> customerRedPacketList(int pageNumber,
			int pageSize,CustomerRedPacketModel model);

}
