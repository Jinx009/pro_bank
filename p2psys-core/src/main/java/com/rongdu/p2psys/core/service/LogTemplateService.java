package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.domain.LogTemplate;

/**
 * 日志模板处理Service
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2013-10-11
 */
public interface LogTemplateService {

	/**
	 * 获得日志模板列表数据
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param logTemplate
	 * @return
	 */
	PageDataList<LogTemplate> logTemplateList(int pageNumber, int pageSize, LogTemplate logTemplate);

	/**
	 * 添加日志模板
	 * 
	 * @param logTemplate
	 */
	void logTemplateAdd(LogTemplate logTemplate);

	/**
	 * 通过主键查询实体
	 * 
	 * @param id 主键
	 * @return 日志模板实体
	 */
	LogTemplate logTemplateEditPage(long id);

	/**
	 * 修改日志模板
	 * 
	 * @param logTemplate 实体
	 * @return 日志模板实体
	 */
	LogTemplate logTemplateEdit(LogTemplate logTemplate);
	
	/**
	 * 查询日志模板
	 * 
	 * @param param 条件参数
	 * @return 日志模板集合
	 */
    List logTemplateList(QueryParam param);

}
