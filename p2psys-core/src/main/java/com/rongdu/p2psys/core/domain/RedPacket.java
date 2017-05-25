package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 红包
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
@Entity
@Table(name = ("s_red_packet"))
public class RedPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 注册
	 */
	public static final String REGISTER = "register";

	/**
	 * 实名认证
	 */
	public static final String REALNAME = "realname";
	
	/**
	 * 手机认证
	 */
	public static final String PHONE = "phone";
	
	/**
	 * 邮箱认证
	 */
	public static final String EMAIL = "email";

	/**
	 * 首次充值
	 */
	public static final String FIRST_RECHARGE = "firstRecharge";

	/**
	 * 首次投资
	 */
	public static final String FIRST_TENDER = "firstTender";

	/**
	 * 邀请好友
	 */
	public static final String INVENT_FRIEND = "inventFriend";

	/**
	 * 投资成功赠送红包
	 */
	public static final String TENDER = "tender";

	/**
	 * 充值赠送红包
	 */
	public static final String RECHARGE = "recharge";
	
	/**
	 * 活动赠送红包
	 */
	public static final String ACTIVITE = "activitie";

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 红包名称
	 */
	private String name;

	/**
	 * 红包描述
	 */
	private String description;

	/**
	 * 红包是否启用，1为启用，0为不启用
	 */
	private int status;

	/**
	 * 业务类型（register/tender/realname/recharge/invite...）
	 */
	private String serviceType;

	/**
	 * 业务类型名称（注册红包/投资红包/认证红包/充值红包/推荐红包...）
	 */
	private String serviceName;

	/**
	 * 红包类型， 1为现金红包（直接发放金额至用户可用余额，可直接提现），2为虚拟红包（只允许投资时使用）
	 */
	private int type;

	/**
	 * 发放方式，1为固定金额发放，2为固定比率（遇小数四舍五入取整），3为浮动方式
	 */
	private int paymentType;

	/**
	 * 红包浮动方式， 1：根据金额区间发放固定金额， 2：根据金额区间按照金额比例生成红包金额（遇小数四舍五入取整）
	 */
	private int floatType;

	/**
	 * 红包最大金额，0为不限
	 */
	private double maxMoney;

	/**
	 * 红包最小金额，0为不限
	 */
	private double minMoney;

	/**
	 * 固定金额，发放方式为1时必填
	 */
	private double money;

	/**
	 * 金额比例，发放方式为2（固定比率）时必填
	 */
	private double rate;

	/**
	 * 红包有效期限，单位：天，0代表无限期，长久有效
	 */
	private int day;
	
	/**
	 *红包到期提醒提前天数
	 */
	private int dueTimeLimit;


	/**
	 * 红包数量，0为不限量
	 */
	private long totalNum;

	/**
	 * 红包发放数量
	 */
	private long useNum;

	/**
	 * 活动开始时间
	 */
	private Date startTime;

	/**
	 * 活动结束时间
	 */
	private Date endTime;

	/**
	 * 是否删除 1删除，0不删除
	 */
	private int isDelete;
	
	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 添加IP
	 */
	private String addIp;

	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 修改者
	 */
	private String updateUser;

	/**
	 * 规则，限制各项红包允许的发放方式及浮动方式（JSON字符串保存）
	 */
	private String rule;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取红包名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置红包名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取红包描述
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置红包描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取红包是否开启状态
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置红包是否开启状态
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取业务类型
	 * 
	 * @return
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * 设置业务类型
	 * 
	 * @param redPacketType
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * 获取业务类型名称
	 * 
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 设置业务类型名称
	 * 
	 * @param redPacketTypeName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 获取红包类型
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置红包类型
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取红包发放方式
	 * 
	 * @return
	 */
	public int getPaymentType() {
		return paymentType;
	}

	/**
	 * 设置红包发放类型
	 * 
	 * @param paymentType
	 */
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * 获取浮动方式
	 * 
	 * @return
	 */
	public int getFloatType() {
		return floatType;
	}

	/**
	 * 设置浮动方式
	 * 
	 * @param floatType
	 */
	public void setFloatType(int floatType) {
		this.floatType = floatType;
	}

	/**
	 * 获取红包最大值
	 * 
	 * @return
	 */
	public double getMaxMoney() {
		return maxMoney;
	}

	/**
	 * 设置红包最大值
	 * 
	 * @param maxMoney
	 */
	public void setMaxMoney(double maxMoney) {
		this.maxMoney = maxMoney;
	}

	/**
	 * 获取红包最小值
	 * 
	 * @return
	 */
	public double getMinMoney() {
		return minMoney;
	}

	/**
	 * 设置红包最小值
	 * 
	 * @param minMoney
	 */
	public void setMinMoney(double minMoney) {
		this.minMoney = minMoney;
	}

	/**
	 * 获取红包固定金额
	 * 
	 * @return
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * 设置红包固定金额
	 * 
	 * @param money
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 获取红包固定兑换比率
	 * 
	 * @return
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * 设置红包固定兑换比率
	 * 
	 * @param rate
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * 获取红包有效天数
	 * 
	 * @return
	 */
	public int getDay() {
		return day;
	}

	/**
	 * 设置红包有效天数
	 * 
	 * @param day
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getDueTimeLimit() {
		return dueTimeLimit;
	}

	public void setDueTimeLimit(int dueTimeLimit) {
		this.dueTimeLimit = dueTimeLimit;
	}
	
	/**
	 * 获取红包总数量
	 * 
	 * @return
	 */
	public long getTotalNum() {
		return totalNum;
	}

	/**
	 * 设置红包总数量
	 * 
	 * @param totalNum
	 */
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * 获取红包发放个数
	 * 
	 * @return
	 */
	public long getUseNum() {
		return useNum;
	}

	/**
	 * 设置红包发放个数
	 * 
	 * @param useNum
	 */
	public void setUseNum(long useNum) {
		this.useNum = useNum;
	}

	/**
	 * 获取活动开始时间
	 * 
	 * @return
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 设置活动开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 设置活动结束时间
	 * 
	 * @return
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 设置活动结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取红包是否删除
	 * 
	 * @return
	 */
	public int getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置红包是否删除
	 * 
	 * @param isDelete
	 */
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取红包添加时间
	 * 
	 * @return
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置红包添加时间
	 * 
	 * @param addTime
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取红包添加IP
	 * 
	 * @return
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置红包添加IP
	 * 
	 * @param addIp
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取红包修改时间
	 * 
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置红包修改时间
	 * 
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取红包修改人员
	 * 
	 * @return
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 设置红包修改人员
	 * 
	 * @param updateUser
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

}
