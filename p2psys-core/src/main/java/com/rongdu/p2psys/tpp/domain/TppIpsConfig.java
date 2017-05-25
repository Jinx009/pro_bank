package com.rongdu.p2psys.tpp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户环迅参数设置
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
@Entity
@Table(name = "tpp_ips_config")
public class TppIpsConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//自动签约状态：0未申请
	public static final byte AUTO_REPAY_NOT = 0;
	//自动签约状态：1待审核
	public static final byte AUTO_REPAY_WAIT = 1;
	//自动签约状态：2审核通过
	public static final byte AUTO_REPAY_YES = 2;
	//自动签约状态：-2审核失败
	public static final byte AUTO_REPAY_NO = -2;
	
    /**
     * 
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /**
     * 用户id
     */
    private long userId;
    /**
     * 自动签约状态：0未申请，1待审核，2审核通过，-2审核失败
     */
    private byte autoRepayStatus;
    /**
     * 用户签约流水号
     */
    private String autoRepayNum;
    /**
     * 自动投标签约授权号
     */
    private String autoRepayNo;
    /**
     * 自动投标签约结束时间
     */
    private Date autoRepayEndTime;
    /**
     * 自动投标签约添加时间
     */
    private Date autoRepayTime;
    /**
     * 添加时间
     */
    private Date addTime;
    
    /**
     * 获取
     * 
     * @return 
     */
    public long getId(){
        return id;
    }

    /**
     * 设置
     * 
     * @param id 要设置的
     */
    public void setId(long id){
        this.id = id;
    }
    /**
     * 获取用户id
     * 
     * @return 用户id
     */
    public long getUserId(){
        return userId;
    }

    /**
     * 设置用户id
     * 
     * @param userId 要设置的用户id
     */
    public void setUserId(long userId){
        this.userId = userId;
    }
    /**
     * 获取自动签约状态：0待审核，1审核通过，-1审核失败
     * 
     * @return 自动签约状态：0待审核，1审核通过，-1审核失败
     */
    public byte getAutoRepayStatus(){
        return autoRepayStatus;
    }

    /**
     * 设置自动签约状态：0待审核，1审核通过，-1审核失败
     * 
     * @param autoRepayStatus 要设置的自动签约状态：0待审核，1审核通过，-1审核失败
     */
    public void setAutoRepayStatus(byte autoRepayStatus){
        this.autoRepayStatus = autoRepayStatus;
    }
    /**
     * 获取用户签约流水号
     * 
     * @return 用户签约流水号
     */
    public String getAutoRepayNum(){
        return autoRepayNum;
    }

    /**
     * 设置用户签约流水号
     * 
     * @param autoRepayNum 要设置的用户签约流水号
     */
    public void setAutoRepayNum(String autoRepayNum){
        this.autoRepayNum = autoRepayNum;
    }
    /**
     * 获取自动投标签约授权号
     * 
     * @return 自动投标签约授权号
     */
    public String getAutoRepayNo(){
        return autoRepayNo;
    }

    /**
     * 设置自动投标签约授权号
     * 
     * @param autoRepayNo 要设置的自动投标签约授权号
     */
    public void setAutoRepayNo(String autoRepayNo){
        this.autoRepayNo = autoRepayNo;
    }
    /**
     * 获取自动投标签约结束时间
     * 
     * @return 自动投标签约结束时间
     */
    public Date getAutoRepayEndTime(){
        return autoRepayEndTime;
    }

    /**
     * 设置自动投标签约结束时间
     * 
     * @param autoRepayEndTime 要设置的自动投标签约结束时间
     */
    public void setAutoRepayEndTime(Date autoRepayEndTime){
        this.autoRepayEndTime = autoRepayEndTime;
    }
    /**
     * 获取自动投标签约添加时间
     * 
     * @return 自动投标签约添加时间
     */
    public Date getAutoRepayTime(){
        return autoRepayTime;
    }

    /**
     * 设置自动投标签约添加时间
     * 
     * @param autoRepayTime 要设置的自动投标签约添加时间
     */
    public void setAutoRepayTime(Date autoRepayTime){
        this.autoRepayTime = autoRepayTime;
    }
    /**
     * 获取添加时间
     * 
     * @return 添加时间
     */
    public Date getAddTime(){
        return addTime;
    }

    /**
     * 设置添加时间
     * 
     * @param addTime 要设置的添加时间
     */
    public void setAddTime(Date addTime){
        this.addTime = addTime;
    }
}


