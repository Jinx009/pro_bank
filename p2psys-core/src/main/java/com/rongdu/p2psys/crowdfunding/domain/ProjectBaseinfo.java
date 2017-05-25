package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 众筹产品
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_project_baseinfo"))
public class ProjectBaseinfo implements Serializable
{
	private static final long serialVersionUID = 3223807638574510528L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	//项目类型 1 实物众筹 2 股权众筹 3 影视众筹 4 公益众筹
	private Integer type;
	//项目名称
	private String projectName;
	//众筹状态 -1 新标 0 提交审核中 2 审核通过 3 众筹完成 4 撤回 5 失败1代表驳回
	private Integer status;
	//起投金额 单笔最小金额
	private Double minMoney;
	//单笔最大金额
	private Double maxMoney;
	//超募是否接受
	private Integer isExceedAccept;
	//最小投资人数
	private Integer minInvestor;
	//最大投资人数
	private Integer maxInvestor;
	//超过人数是否接受
	private Integer isExceedAcceptInvestor;
	//推荐级别
	private Integer isRecommend;
	//开始跟投时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	private Date startTime;
	//跟投结束时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;
	//预计权益到达日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "right_due_time")
	private Date rightDueTime;
	//平台管理费
	private Double manageFee;
	//定向码
	private Long acceptanceCode;
	//发起人公司地址
	private String address;
	//发起人公司名称
	private String company;
	//发起人名称
	private String creater;
	//违约金比例
	private Double breach;
	//添加时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "add_time")
	private Date addTime;
	//投资金额增加规则
	private Double addAmount;
	//当前募集金额
	private Double account;
	//募集金额
	private Double wannaAccount;
	//领投人
	@OneToOne
	@JoinColumn(name = "leader")
	private Leader leader;
	//一句话简介
	private String info;
	//是否OC显示
	private Integer isShowPc;
	//是否微信显示
	private Integer isShowWechat;
	//墓基前公司估值
	private Double companyMoney;
	//添加ip
	private String addIp;
	//热销PC 1代表热销推荐
	private Integer  pcIndexStatus;
	//热销微信 1代表热销推荐
	private Integer wechatIndexStatus;
	//点赞人数
	private Integer likeNum;
	//关注人数
	private Integer attentionNum;
	//融资进度       种子轮，天使轮，Pre-A轮，A轮，Pre-B轮，B轮，C轮，D轮
	private Integer financing;
	//填写者身份
	private Long userId;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getMinMoney() {
		return minMoney;
	}
	public void setMinMoney(Double minMoney) {
		this.minMoney = minMoney;
	}
	public Double getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
	}
	public Integer getIsExceedAccept() {
		return isExceedAccept;
	}
	public void setIsExceedAccept(Integer isExceedAccept) {
		this.isExceedAccept = isExceedAccept;
	}
	public Integer getMinInvestor() {
		return minInvestor;
	}
	public void setMinInvestor(Integer minInvestor) {
		this.minInvestor = minInvestor;
	}
	public Integer getMaxInvestor() {
		return maxInvestor;
	}
	public void setMaxInvestor(Integer maxInvestor) {
		this.maxInvestor = maxInvestor;
	}
	public Integer getIsExceedAcceptInvestor() {
		return isExceedAcceptInvestor;
	}
	public void setIsExceedAcceptInvestor(Integer isExceedAcceptInvestor) {
		this.isExceedAcceptInvestor = isExceedAcceptInvestor;
	}
	public Integer getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getRightDueTime() {
		return rightDueTime;
	}
	public void setRightDueTime(Date rightDueTime) {
		this.rightDueTime = rightDueTime;
	}
	public Double getManageFee() {
		return manageFee;
	}
	public void setManageFee(Double manageFee) {
		this.manageFee = manageFee;
	}
	public Long getAcceptanceCode() {
		return acceptanceCode;
	}
	public void setAcceptanceCode(Long acceptanceCode) {
		this.acceptanceCode = acceptanceCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Double getBreach() {
		return breach;
	}
	public void setBreach(Double breach) {
		this.breach = breach;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Double getAddAmount() {
		return addAmount;
	}
	public void setAddAmount(Double addAmount) {
		this.addAmount = addAmount;
	}
	public Double getAccount() {
		return account;
	}
	public void setAccount(Double account) {
		this.account = account;
	}
	public Double getWannaAccount() {
		return wannaAccount;
	}
	public void setWannaAccount(Double wannaAccount) {
		this.wannaAccount = wannaAccount;
	}
	public Leader getLeader() {
		return leader;
	}
	public void setLeader(Leader leader) {
		this.leader = leader;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getIsShowPc() {
		return isShowPc;
	}
	public void setIsShowPc(Integer isShowPc) {
		this.isShowPc = isShowPc;
	}
	public Integer getIsShowWechat() {
		return isShowWechat;
	}
	public void setIsShowWechat(Integer isShowWechat) {
		this.isShowWechat = isShowWechat;
	}
	public Double getCompanyMoney() {
		return companyMoney;
	}
	public void setCompanyMoney(Double companyMoney) {
		this.companyMoney = companyMoney;
	}
	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	public Integer getPcIndexStatus() {
		return pcIndexStatus;
	}
	public void setPcIndexStatus(Integer pcIndexStatus) {
		this.pcIndexStatus = pcIndexStatus;
	}
	public Integer getWechatIndexStatus() {
		return wechatIndexStatus;
	}
	public void setWechatIndexStatus(Integer wechatIndexStatus) {
		this.wechatIndexStatus = wechatIndexStatus;
	}
	public Integer getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}
	public Integer getAttentionNum() {
		return attentionNum;
	}
	public void setAttentionNum(Integer attentionNum) {
		this.attentionNum = attentionNum;
	}
	public Integer getFinancing() {
		return financing;
	}
	public void setFinancing(Integer financing) {
		this.financing = financing;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
