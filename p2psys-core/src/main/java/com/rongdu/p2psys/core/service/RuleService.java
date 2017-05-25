package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.p2psys.core.domain.Rule;

/**
 * 规则Service
 */
public interface RuleService {

	/**
	 * 根据规则表主键ID查询数据
	 * 
	 * @param id 主键ID
	 * @return 规则实体
	 */
	Rule findById(Long id);

	/**
	 * 根据规则类型名查询数据
	 * 
	 * @param nid 规则类型名
	 * @return 规则实体
	 */
	Rule findByNid(String nid);

	/**
	 * 根据状态获取Rule规则
	 * 
	 * @param status 状态
	 * @return Rule规则
	 */
	List<Rule> list(int status);

	/**
	 * 查询所有的规则
	 * 
	 * @return 所有的规则
	 */
	List<Rule> findAll();

	/**
	 * 根据规则Id查询规则
	 * 
	 * @param id id
	 * @return rule
	 */
	Rule getRule(Long id);

}
