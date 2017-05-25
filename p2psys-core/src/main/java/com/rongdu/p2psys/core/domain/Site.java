package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 栏目表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_site")
public class Site implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 标识
	 */
	private String nid;
	/**
	 * 等级
	 */
	private int level;
	/**
	 * 父ID
	 */
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "pid")
	// @NotFound(action=NotFoundAction.IGNORE)
	// private SiteNew parent;

	private long pid;
	/**
	 * 叶子，0：否（有子节点），1：是（没有子节点）
	 */
	private int leaf;
	/**
	 * 状态，0：隐藏，1：显示
	 */
	private int status;
	/**
	 * 是否删除，0：未删除，1：删除
	 */
	private int isDelete;
	/**
	 * 类型，1：列表，2：单页，3：站内链接，4：站外链接
	 */
	private int type;
	/**
	 * 跳转链接
	 */
	private String url;
	/**
	 * 排序
	 */
	private int sort;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	

	/**
	 * 显示个数
	 */
	private int size;

	public Site() {
	}

	public Site(long id) {
		this.id = id;
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name 要设置的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取标识
	 * 
	 * @return 标识
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置标识
	 * 
	 * @param nid 要设置的标识
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * 获取等级
	 * 
	 * @return 等级
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 设置等级
	 * 
	 * @param level 要设置的等级
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	/**
	 * 获取叶子，0：否（有子节点），1：是（没有子节点）
	 * 
	 * @return 叶子，0：否（有子节点），1：是（没有子节点）
	 */
	public int getLeaf() {
		return leaf;
	}

	/**
	 * 设置叶子，0：否（有子节点），1：是（没有子节点）
	 * 
	 * @param leaf 要设置的叶子，0：否（有子节点），1：是（没有子节点）
	 */
	public void setLeaf(int leaf) {
		this.leaf = leaf;
	}

	/**
	 * 获取状态，0：隐藏，1：显示
	 * 
	 * @return 状态，0：隐藏，1：显示
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态，0：隐藏，1：显示
	 * 
	 * @param status 要设置的状态，0：隐藏，1：显示
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取该栏目是否已被删除，1：已删除，0，未删除
	 * @return
	 */
	public int getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置该栏目是否删除，1：删除，0：不删除
	 * @param isDelete
	 */
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取类型，1：列表，2：单页，3：站内链接，4：站外链接
	 * 
	 * @return 类型，1：列表，2：单页，3：站内链接，4：站外链接
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置类型，1：列表，2：单页，3：站内链接，4：站外链接
	 * 
	 * @param type 要设置的类型，1：列表，2：单页，3：站内链接，4：站外链接
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取跳转链接
	 * 
	 * @return 跳转链接
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置跳转链接
	 * 
	 * @param url 要设置的跳转链接
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * 
	 * @param sort 要设置的排序
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description 要设置的描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加IP
	 * 
	 * @return 添加IP
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * 
	 * @param addIp 要设置的添加IP
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	/**
	 * 设置显示个数
	 * @return int 显示个数
	 */
	public int getSize() {
		return size;
	}
	/**
	 * 设置显示个数
	 * 
	 * @param size 要设置的显示个数
	 */
	public void setSize(int size) {
		this.size = size;
	}
}
