package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 领投人产品标签
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_leader_flag"))
public class LeaderFlag implements  Serializable{

	private static final long serialVersionUID = 5852873424059468446L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//对应仓库领投人
	@OneToOne
	@JoinColumn(name = "leader_factory_id")
	private LeaderFactory leaderFactory;
	//对应标签库
	@OneToOne
	@JoinColumn(name = "flag_id")
	private Flag flag;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LeaderFactory getLeaderFactory() {
		return leaderFactory;
	}
	public void setLeaderFactory(LeaderFactory leaderFactory) {
		this.leaderFactory = leaderFactory;
	}
	public Flag getFlag() {
		return flag;
	}
	public void setFlag(Flag flag) {
		this.flag = flag;
	}
}
