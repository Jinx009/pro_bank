package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.core.domain.Log;

/**
 * 系统操作日志Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年5月7日
 */
public class LogModel extends Log {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;

	/** 每页数据条数 */
	private int size = Page.ROWS;

	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;

	public static LogModel instance(Log log) {
		LogModel model = new LogModel();
		BeanUtils.copyProperties(log, model);
		return model;
	}

	public Log prototype() {
		Log log = new Log();
		BeanUtils.copyProperties(this, log);
		return log;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
