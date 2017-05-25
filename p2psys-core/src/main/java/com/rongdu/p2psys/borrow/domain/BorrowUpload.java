package com.rongdu.p2psys.borrow.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 上传图片表
 * 
 */
@Entity
@Table(name = "rd_borrow_upload")
public class BorrowUpload implements Serializable
{

	private static final long serialVersionUID = 3964310188624760049L;

	/**
	 * 资料详情图
	 */
	public static final int BORROW_DETAIL = 0;

	/**
	 * 抵押实物照
	 */
	public static final int MORTGAGE_PHYSICAL = 1;

	/**
	 * 抵押档案照
	 */
	public static final int MORTGAGE_ARCHIVES = 2;

	/**
	 * 担保函
	 */
	public static final int GUARANTEE = 3;

	/**
	 * 借款标头像
	 */
	public static final int BORROW_ICON = 5;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 借款标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_id")
	private Borrow borrow;

	/**
	 * 抵押物
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mortgage_id")
	private BorrowMortgage borrowMortgage;

	/**
	 * 图片类型
	 * 
	 * <p>
	 * 1:抵押实物照
	 * </p>
	 * <p>
	 * 2:抵押档案照
	 * </p>
	 * <p>
	 * 3:担保函
	 * </p>
	 * <p>
	 * 4:前台发标图片
	 * </p>
	 * <p>
	 * 5:借款标头像
	 * </p>
	 */
	private int type;

	/**
	 * 图片路径
	 */
	private String picPath;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Borrow getBorrow()
	{
		return borrow;
	}

	public void setBorrow(Borrow borrow)
	{
		this.borrow = borrow;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public BorrowMortgage getBorrowMortgage()
	{
		return borrowMortgage;
	}

	public void setBorrowMortgage(BorrowMortgage borrowMortgage)
	{
		this.borrowMortgage = borrowMortgage;
	}

	public String getPicPath()
	{
		return picPath;
	}

	public void setPicPath(String picPath)
	{
		this.picPath = picPath;
	}
}
