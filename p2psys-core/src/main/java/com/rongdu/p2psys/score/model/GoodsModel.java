package com.rongdu.p2psys.score.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.domain.GoodsPic;

public class GoodsModel extends Goods {

    /**
     * 
     */
    private static final long serialVersionUID = -7181613891214500083L;

    /**
     * domain 转 model
     * @param item domain
     * @return model
     */
    public static GoodsModel instance(Goods item) {
        GoodsModel model = new GoodsModel();
        BeanUtils.copyProperties(item, model);
        return model;
    }

    public Goods prototype() {
        Goods item = new Goods();
        BeanUtils.copyProperties(this, item);
        return item;
    }
    
    /**
     * 商品图片
     */
    private List<GoodsPic> picList;
    
    /**
     * 后台图片上传
     */
    private List<String> files;
    
    /**
     * 第一张图片
     */
    private String imgSrc;
    
    /** 开始时间 **/
    private String startTime;
    
    /** 结束时间 **/
    private String endTime;
    
    /** 积分范围：开始积分 **/
    private int startScore;
    
    /** 积分范围：结束积分 **/
    private int endScore;
    
    /** 操作者ID */
    private long operatorId;
    
    /** 商品分类 */
    private long categoryId;
    
    /** 操作备注 */
    private String operatorRemark;
    
    /** 当前页数 **/
    private int page;
    
    /** 分页数 **/
    private int rows;
    
    /** 条件查询**/
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

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public List<GoodsPic> getPicList() {
        return picList;
    }

    public void setPicList(List<GoodsPic> picList) {
        this.picList = picList;
    }

    public int getStartScore() {
        return startScore;
    }

    public void setStartScore(int startScore) {
        this.startScore = startScore;
    }

    public int getEndScore() {
        return endScore;
    }

    public void setEndScore(int endScore) {
        this.endScore = endScore;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorRemark() {
        return operatorRemark;
    }

    public void setOperatorRemark(String operatorRemark) {
        this.operatorRemark = operatorRemark;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
    
    
}
