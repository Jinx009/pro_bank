package com.rongdu.p2psys.score.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.score.domain.GoodsCategory;

public class GoodsCategoryModel extends GoodsCategory {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * domain 转 model
     * @param item domain
     * @return model
     */
    public static GoodsCategoryModel instance(GoodsCategory item) {
        GoodsCategoryModel model = new GoodsCategoryModel();
        BeanUtils.copyProperties(item, model);
        return model;
    }

    public GoodsCategory prototype() {
        GoodsCategory item = new GoodsCategory();
        BeanUtils.copyProperties(this, item);
        return item;
    }
    
    /** 开始时间 **/
    private String startTime;
    
    /** 结束时间 **/
    private String endTime;
    
    /**条件查询 */
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

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
    
    
}
