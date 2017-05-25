package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.dao.RuleDao;
import com.rongdu.p2psys.core.domain.Rule;
import com.rongdu.p2psys.core.service.RuleService;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {

	@Resource
	private RuleDao ruleDao;

	@Override
	public Rule findById(Long id) {
		return ruleDao.find(id);
	}

	@Override
	public Rule findByNid(String nid) {
		return ruleDao.findObjByProperty("nid", nid);
	}

	/**
	 * 根据状态获取列表
	 * 
	 * @param status status
	 * @return List
	 */
	public List<Rule> list(int status) {
		return ruleDao.findByProperty("status", status);
	}

	/**
	 * 查询所有的规则
	 * 
	 * @param status
	 * @return
	 */
	public List<Rule> findAll() {
		return ruleDao.findAll();
	}

	@Override
	public Rule getRule(Long id) {
		return ruleDao.find(id);
	}

}
