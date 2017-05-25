package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.p2psys.borrow.domain.BorrowUpload;

/**
 * 上传图片
 * @author sj
 * @since 2014年8月25日
 */
public interface BorrowUploadService {

	/**
	 * 获得图片list
	 * @param id
	 * @return
	 */
	public List<BorrowUpload> findByBorrowId(long id);
	/**
    * 根据标ID获取担保函图片
    * @param id
    * @return 担保函图片路径集合
    */
    public List<BorrowUpload> findByBorrowIdAndType(long id);
    /**
     * 根据抵押物ID获取图片
     * @param id
     * @return 图片路径集合
     */
    public List<BorrowUpload> findByMortgageId(long id);
    /**
     * 根据抵押物ID和类型获取图片
     * @param id 抵押物ID 
     * @param type 图片类型 
     * @return 图片路径集合
     */
    public List<BorrowUpload> findByMortgageIdAndType(long id, long type);
    /**
     * 根据图片ID删除图片
     * @param id 图片ID 
     */
    public void delete(long id);

    /**
     * 根据标id和图片类型获取图片
     * @param id 借款标id
     * @param type 图片类型
     * @return
     */
    public List<BorrowUpload> findByBorrowIdAndType(long id, long type);
}
