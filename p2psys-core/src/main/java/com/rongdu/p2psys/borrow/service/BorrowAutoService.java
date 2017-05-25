package com.rongdu.p2psys.borrow.service;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.user.domain.User;

/**
 * 自动投标Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月21日
 */
public interface BorrowAutoService {

	/**
	 * 初始化，若无记录新增
	 * 
	 * @param userId
	 */
	BorrowAuto init(User user);

	/**
	 * 更新
	 * 
	 * @param auto
	 * @throws Exception
	 */
	void update(BorrowAuto auto) throws Exception;

	/**
	 * 计数
	 * 
	 * @return
	 */
	int count(Borrow borrow);

	/**
	 * 更新自动投标update_time
	 * 
	 * @param auto
	 */
	void modifyTime(BorrowAuto auto);

	/**
	 * @param auto
	 */
	void modifyStatus(BorrowAuto auto);

	/**
	 * 添加自动投标
	 * @param auto
	 */
	void add(BorrowAuto auto);

	/**
	 * 根据用户查找
	 * @param sessionUser
	 */
	BorrowAuto findByUser(User user);

	public BorrowAuto getBorrowAutoById(int autoTenderId);

	public void updateBorrowAuto(BorrowAuto ba);

}
