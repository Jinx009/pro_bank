package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rongdu.p2psys.tpp.BaseTPPWay;

/**
 * 菜单表
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-03-17
 */
@Entity
@Table(name = "s_menu")
public class Menu implements Serializable {
	
	/** 菜单：是菜单 */
	public static final boolean IS_MENU = true;
	/** 菜单：全部 */
	public static final boolean MENU_ALL = false;
	
	/**菜单系统类型：1标准版*/
	public static final byte OPEN_TYPE_STANDARD  = 1;
	/** 菜单系统类型：2托管版 */
	public static final byte OPEN_TYPE_DEPOSIT = 2;
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键标示
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 父级ID
	 */
	private long parentId;
	/**
	 * 链接地址
	 */
	private String href;
	/**
	 * 图标
	 */
	private String iconCls;
	/**
	 * 排序
	 */
	private int sort;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加者
	 */
	private String addUser;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 修改者
	 */
	private String updateUser;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 是否删除：0不删除，1删除
	 */
	private boolean isDelete;
	
	/**
     * 是否是菜单：0不是菜单，1是菜单
     */
    private boolean isMenu;
    
    /**
     * 菜单系统类型：1标准版，2托管版
     */
    private byte openType;

	/**
	 * 构造方法
	 */
	public Menu() {
		super();
	}

	/**
	 * 构造方法
	 * 
	 * @param id 主键
	 */
	public Menu(long id) {
		super();
		this.id = id;
	}

	/**
	 * 获取主键标示
	 * 
	 * @return 主键标示
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键标示
	 * 
	 * @param id 要设置的主键标示
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取菜单名称
	 * 
	 * @return 菜单名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置菜单名称
	 * 
	 * @param name 要设置的菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取父级ID
	 * 
	 * @return 父级ID
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * 设置父级ID
	 * 
	 * @param parentId 要设置的父级ID
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return the iconCls
	 */
	public String getIconCls() {
		return iconCls;
	}

	/**
	 * @param iconCls the iconCls to set
	 */
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * 获取添加时间
	 * 
	 * @return 添加时间
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置添加时间
	 * 
	 * @param addTime 要设置的添加时间
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加者
	 * 
	 * @return 添加者
	 */
	public String getAddUser() {
		return addUser;
	}

	/**
	 * 设置添加者
	 * 
	 * @param addUser 要设置的添加者
	 */
	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置修改时间
	 * 
	 * @param updateTime 要设置的修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取修改者
	 * 
	 * @return 修改者
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 设置修改者
	 * 
	 * @param updateUser 要设置的修改者
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 要设置的备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    public byte getOpenType() {
        return openType;
    }

    public void setOpenType(byte openType) {
        this.openType = openType;
    }
    
    public static byte getCurrOpenType() {
        boolean isOpenApi = BaseTPPWay.isOpenApi();
        if(isOpenApi){
            return OPEN_TYPE_DEPOSIT;
        }
        return OPEN_TYPE_STANDARD;
    }
}
