package com.rongdu.p2psys.bond.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.bond.domain.BondCollection;

/**
 * 债权待收Model
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public class BondCollectionModel extends  BondCollection{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static BondCollectionModel instance(BondCollection item) {
        BondCollectionModel model = new BondCollectionModel();
        BeanUtils.copyProperties(item, model);
        return model;
    }

    public BondCollection prototype() {
        BondCollection item = new BondCollection();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}


