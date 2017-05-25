package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowBespeak;
import com.rongdu.p2psys.borrow.domain.BorrowBespeakPic;
import com.rongdu.p2psys.borrow.model.BorrowBespeakModel;

/**
 * 预约借款
 * @author sj
 * @version 2.0
 * @since 2014-08-20
 */
public interface BorrowBespeakService {

	/**
	 * 保存预约借款记录
	 * @return
	 */
	public BorrowBespeak doBespeak(BorrowBespeak borrowBespeak);

	/**
	 * 
	 * @param borrowBespeak
	 * @return
	 */
	public PageDataList<BorrowBespeakModel> borrowBespeakList(
			BorrowBespeakModel borrowBespeak, int page, int size, String searchName);

	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public BorrowBespeak find(long id);

	/**
	 * 更新
	 * @param borrowBespeak
	 */
	public void borrowBespeakEdit(BorrowBespeak borrowBespeak);

	public void addBorrowBespeakPic(List<BorrowBespeakPic> list);

	public List<BorrowBespeakPic> findBorrowBespeakPicById(long id);

	/**
	 * 根据借款标id获取资料图片
	 * @param borrowId
	 * @return
	 */
	public List<BorrowBespeakPic> findBorrowBespeakPicByBorrowBespeak(BorrowBespeak borrowBespeak);
	
}
