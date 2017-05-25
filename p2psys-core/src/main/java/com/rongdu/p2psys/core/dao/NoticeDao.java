package com.rongdu.p2psys.core.dao;

import java.text.ParseException;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.Notice;

public interface NoticeDao extends BaseDao<Notice> {

	/**
	 * 根据用户ID获取发送校验码（重置密码用）时间
	 * 
	 * @param userId 用户ID
	 * @param email 电子邮件
	 * @return long
	 * @throws ParseException if has error
	 */
	long getAddTimeByUserId(long userId, String email) throws ParseException;
	/**
	 * 根据用户ID获取发送校验码（找回密码用）时间
	 * 
	 * @param userId 用户ID
	 * @param addr 接收地址
	 * @return long
	 * @throws ParseException if has error
	 */
	long getNoticeAddTimeByUserId(long userId, String addr) throws ParseException;
}
