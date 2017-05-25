package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Pay;

/**
 * 充值方式Service
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日
 */
public interface PayService {

	/**
	 * 列表(开启的)
	 * 
	 * @param enable
	 * @return
	 */
	List<Pay> list(int enable);

	/**
	 * 列表(开启网络直联的)
	 * 
	 * @param enable_direct
	 * @return
	 */
	List<Pay> directList(int enable_direct);

	/**
	 * 根据标识nid获取
	 * 
	 * @param nid
	 * @return
	 */
	Pay findByNid(String nid);

	/**
	 * 根据id索引
	 * 
	 * @param id
	 * @return
	 */
	Pay findById(long id);

	/**
	 * 新增pay
	 * 
	 * @param pay
	 * @return
	 */
	Pay save(Pay pay);

	/**
	 * 更新pay
	 * 
	 * @param pay
	 * @return
	 */
	Pay update(Pay pay);

	/**
	 * 删除pay
	 * 
	 * @param pay
	 * @return
	 */
	void delete(Pay pay);

	/**
	 * 查询支付接口列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	PageDataList<Pay> list(int pageNumber, int pageSize, Pay model);

}
