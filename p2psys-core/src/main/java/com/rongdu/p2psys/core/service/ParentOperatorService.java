package com.rongdu.p2psys.core.service;

import com.rongdu.p2psys.core.domain.ParentOperator;

public interface ParentOperatorService {
	/**
	 * 根据条件获取对象
	 * @param param
	 * @return
	 */
	public ParentOperator getParentOperator(long c_operator_id);
	
	/**
	 * 修改
	 * @param c_operator_id
	 * @param p_operator_id
	 */
	public void modify(ParentOperator operator);
	
	/**
	 * 增加
	 * @param operator
	 */
	public void add(ParentOperator operator);
}
