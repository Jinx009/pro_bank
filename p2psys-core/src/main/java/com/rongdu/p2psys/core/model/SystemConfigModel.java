package com.rongdu.p2psys.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.core.domain.SystemConfig;

/**
 * 系统参数Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月21日
 */
public class SystemConfigModel extends SystemConfig {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;
	/** 每页总数 **/
	private int rows;

	/** 每页数据条数 */
	private int size = Page.ROWS;

	private Map<String, SystemConfig> map;
	
	/** 条件查询 */
	private String searchName;

	public SystemConfigModel() {
		map = Collections.synchronizedMap(new HashMap<String, SystemConfig>());
	}

	public static SystemConfigModel instance(SystemConfig sconfig) {
		SystemConfigModel systemConfigModel = new SystemConfigModel();
		BeanUtils.copyProperties(sconfig, systemConfigModel);
		return systemConfigModel;
	}

	public SystemConfig prototype() {
		SystemConfig sconfig = new SystemConfig();
		BeanUtils.copyProperties(this, sconfig);
		return sconfig;
	}

	public void addConfig(SystemConfig sys) {
		map.put(sys.getNid().replace("con_", ""), sys);
	}

	private SystemConfig getConfig(String key) {
		SystemConfig sys = (SystemConfig) map.get(key);
		return sys;
	}

	public String getValue(String key) {
		SystemConfig c = getConfig(key);
		if (c == null)
			return null;
		return c.getValue();
	}

	public String getStatus(String key) {
		SystemConfig c = getConfig(key);
		if (c == null)
			return null;
		return getConfig(key).getStatus() + "";
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size > 0 ? size : Page.ROWS;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<String, SystemConfig> getMap() {
		return map;
	}

	public void setMap(Map<String, SystemConfig> map) {
		this.map = map;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

}
