package com.rongdu.p2psys.nb.recommend.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.domain.Operator;
/**
 * 推荐收益规则
 * 
 * @author Jinx
 *
 */
@Entity
@Table(name = ("nb_recommend_profit"))
public class RecommendProfit  implements Serializable
{
	private static final long serialVersionUID = 6482609764959991031L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	/**
	 * 收益
	 */
	private Double rate;
	/**
	 * 区间最小值
	 */
	private Double startMoney;
	/**
	 * 区间较大值
	 */
	private Double endMoney;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 后台操作用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id")
	private Operator operator;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Double getRate()
	{
		return rate;
	}

	public void setRate(Double rate)
	{
		this.rate = rate;
	}

	public Double getStartMoney()
	{
		return startMoney;
	}

	public void setStartMoney(Double startMoney)
	{
		this.startMoney = startMoney;
	}

	public Double getEndMoney()
	{
		return endMoney;
	}

	public void setEndMoney(Double endMoney)
	{
		this.endMoney = endMoney;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public Operator getOperator()
	{
		return operator;
	}

	public void setOperator(Operator operator)
	{
		this.operator = operator;
	}

}
