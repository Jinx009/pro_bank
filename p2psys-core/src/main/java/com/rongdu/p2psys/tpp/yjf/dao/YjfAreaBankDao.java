package com.rongdu.p2psys.tpp.yjf.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.tpp.domain.YjfAreaBank;

/**
 * 地区
 * 
 * @author lx
 * @version 2.0
 * @since 2014-7-31
 */
public interface YjfAreaBankDao extends BaseDao<YjfAreaBank> {

	/**
	 * 根据pid取得地区列表
	 * 
	 * @param pid 上一级id
	 * @return 地区列表
	 */
	List<YjfAreaBank> getYjfAreaBankListByPid(String pid);

}
