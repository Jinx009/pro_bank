package com.rongdu.p2psys.customer.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.customer.model.AccountLogModel;
import com.rongdu.p2psys.customer.model.CustomerProductModel;
import com.rongdu.p2psys.customer.model.CustomerBaseinfoModel;
import com.rongdu.p2psys.customer.model.CustomerRedPacketModel;
import com.rongdu.p2psys.customer.model.ReferrerModel;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * PPfund（资金管理产品）
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
public interface CustomerService {
	/**
	 * 分页查询
	 * 
	 * @param model AccountLogModel
	 * @return
	 */
	PageDataList<AccountLogModel> accountLogList(int pageNumber,
			int pageSize, AccountLogModel model);
	
	/**
	 * 后台分页查询
	 * 
	 * @param model CustomerBaseinfoModel
	 * @return
	 */
	PageDataList<CustomerProductModel> customerProductList(int pageNumber,
			int pageSize, CustomerProductModel model);

	/**
	 * 后台分页查询
	 * 
	 * @param model CustomerProductModel
	 * @return
	 */
	PageDataList<CustomerBaseinfoModel> customerBaseinfoList(int pageNumber,
			int pageSize, CustomerBaseinfoModel model);
	
	/**
	 * 后台分页查询
	 * 
	 * @param model CustomerRedPacketModel
	 * @return
	 */
	PageDataList<CustomerRedPacketModel> customerRedPacketList(int pageNumber,
			int pageSize, CustomerRedPacketModel model);
	
	/**
	 * 后台分页查询
	 * 
	 * @param model ReferrerModel
	 * @return
	 */
	PageDataList<ReferrerModel> referrerList(int pageNumber,
			int pageSize,ReferrerModel model);
}
