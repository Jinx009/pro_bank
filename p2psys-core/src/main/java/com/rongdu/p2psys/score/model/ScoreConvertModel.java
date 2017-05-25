package com.rongdu.p2psys.score.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.score.domain.ScoreConvert;
import com.rongdu.p2psys.user.exception.UserException;


public class ScoreConvertModel extends ScoreConvert {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ScoreConvertModel instance(ScoreConvert item) {
		ScoreConvertModel model = new ScoreConvertModel();
		BeanUtils.copyProperties(item, model);
		return model;
	}

	public ScoreConvert prototype() {
		ScoreConvert item = new ScoreConvert();
		BeanUtils.copyProperties(this, item);
		return item;
	}
	
	/** 用户名 **/
	private String userName;
	
	/** 开始时间 **/
	private String startTime;
	
	/** 结束时间 **/
	private String endTime;
	
	/** 当前页数 **/
	private int page;
	
	/** 分页数 **/
	private int rows;
	
	
	/** 条件查询 **/
	private String searchName;
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
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	/**
	 * 积分兑换
	 * @param userId
	 * @return
	 */
	public boolean checkUser(long userId){
		if(userId != this.getUser().getUserId() || this.getUser().getUserId() <= 0){
			throw new UserException("用户信息不正确！", 1);
		}
		return true;
	}
}
