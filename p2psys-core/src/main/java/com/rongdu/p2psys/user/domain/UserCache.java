package com.rongdu.p2psys.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用户附属信息表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_user_cache")
public class UserCache implements Serializable {
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
	 * 用户ID
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 性别 1:男,0:女
	 */
	private int sex;
	/**
	 * 状态 1:锁定，0:解锁
	 */
	private int status;
	/**
	 * 用户类型(参考s_dict 表，只填写值1，2...) ，参考数据字典表
	 * 1:投资人/2:借款人/3:投资借款人
	 */
	private int userType;
	/**
	 *  用户性质(参考s_dict 表，只填写值1，2...) ，参考数据字典表
	 *  1:自然人,2:企业用户(公司注册),3:担保公司
	 */
	private int userNature;
	/**
	 * 证件类型，参考数据字典表
	 */
	private int cardType;
	
	/**
	 * 证件图片正面
	 */
	private String cardPositive;
	/**
	 * 证件图片反面
	 */
	private String cardOpposite;
	/**
	 * 头像 0:默认,1:自定义
	 */
	private int headStatus;
	/**
	 * 登录密码是否锁定
	 */
	private int loginPwdStatus;
	/**
	 * 登录失败次数
	 */
	private int loginFailTimes;
	/**
	 * 锁定备注
	 */
	private String remark;
	/**
	 * 锁定时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lockTime;
	/**
	 * 支付密码是否锁定
	 */
	private int payPwdStatus;
	/**
	 * 支付密码输错次数
	 */
	private int payFailTimes;
	
	/**
	 * 锁定交易密码时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lockPayTime;
	
	/**
	 * 最后登录时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date loginTime;
	/**
	 * 最后登录IP private String lastIp;
	 */
	/**
	 * 总登录次数 private int loginTimes;
	 */
	/**
	 * 密码修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date pwdModifyTime;
	
	/**
	 * 交易密码修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date payPwdModifyTime;
	
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 客服ID,不做任何关联
	 */
	private int customerUserId;
	/**
	 * 客服姓名
	 */
	private String customerUserName;
	/**
	 * 客服添加时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date customerAddTime;

	/**
	 * 新增IP
	 */
	private String addIp;
//-------------纵通项目字段--------------------------
	
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 公司类型，参考数据字典表
	 */
	private int companyType;
	/**
	 * 公司注册号
	 */
	private String companyRegNo;
	/**
	 * 税务登记证号
	 */
	private String taxRegNo;
	/**
	 * 公司描述
	 */
	private String description;
	/**
	 * 法人代表姓名
	 */
	private String frdbName;
	/**
	 * 法人代表证件号
	 */
	private String frdbNo;
	/**
	 * 注册资本
	 */
	private double regCapital;
	/**
	 * 组织机构代码
	 */
	private String zzjgCode;
	/**
	 * 营业执照编号
	 */
	private String businessRegistrationNumber;
	/**
	 * 公司电话
	 */
	private String companyPhone;
	/**
	 * 公司传真
	 */
	private String companyFax;
	/**
	 * 公司邮箱
	 */
	private String companyEmail;
	/**
	 * 经营执照图片路径
	 */
	private String jyzzPicPath;
	/**
	 * 税务登记证图片路径
	 */
	private String swdjPicPath;
	/**
	 * 组织机构证图片路径
	 */
	private String zzjgPicPath;
	/**
	 * 开户许可证图片路径
	 */
	private String khxkPicPath;
	/**
	 * 贷款卡记录（企业）图片路径
	 */
	private String dkkPicPath;
	/**
	 * 个人征信报告图片路径
	 */
	private String grzxPicPath;
	
	private Integer investStatus;

	//身份证正面 2016-4-6
	private String idCardPicPositive; 
	//身份证反面 2016-4-6
	private String idCardPicOther;
	//营业执照 2016-4-6
	private String businessLicense;
	//项目方身份 2016-4-6
	private String userIdentity;
	//项目方身份审核状态 2016-4-6 -1 是审核中 0是修改申请1是审核通过
	private Integer approvalStatus;
	

	
	/**
	 * 生日（by Chris）
	 */
	private String birthday;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Integer getInvestStatus() {
		return investStatus;
	}

	public void setInvestStatus(Integer investStatus) {
		this.investStatus = investStatus;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getCompanyType() {
		return companyType;
	}

	public void setCompanyType(int companyType) {
		this.companyType = companyType;
	}

	public String getCompanyRegNo() {
		return companyRegNo;
	}

	public void setCompanyRegNo(String companyRegNo) {
		this.companyRegNo = companyRegNo;
	}

	public String getTaxRegNo() {
		return taxRegNo;
	}

	public void setTaxRegNo(String taxRegNo) {
		this.taxRegNo = taxRegNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFrdbName() {
		return frdbName;
	}

	public void setFrdbName(String frdbName) {
		this.frdbName = frdbName;
	}

	public String getFrdbNo() {
		return frdbNo;
	}

	public void setFrdbNo(String frdbNo) {
		this.frdbNo = frdbNo;
	}

	public double getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(double regCapital) {
		this.regCapital = regCapital;
	}

	public String getZzjgCode() {
		return zzjgCode;
	}

	public void setZzjgCode(String zzjgCode) {
		this.zzjgCode = zzjgCode;
	}
	
	public String getBusinessRegistrationNumber() {
		return businessRegistrationNumber;
	}

	public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
		this.businessRegistrationNumber = businessRegistrationNumber;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}

	public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getJyzzPicPath() {
		return jyzzPicPath;
	}

	public void setJyzzPicPath(String jyzzPicPath) {
		this.jyzzPicPath = jyzzPicPath;
	}

	public String getSwdjPicPath() {
		return swdjPicPath;
	}

	public void setSwdjPicPath(String swdjPicPath) {
		this.swdjPicPath = swdjPicPath;
	}

	public String getZzjgPicPath() {
		return zzjgPicPath;
	}

	public void setZzjgPicPath(String zzjgPicPath) {
		this.zzjgPicPath = zzjgPicPath;
	}

	public String getKhxkPicPath() {
        return khxkPicPath;
    }

    public void setKhxkPicPath(String khxkPicPath) {
        this.khxkPicPath = khxkPicPath;
    }

    public String getDkkPicPath() {
        return dkkPicPath;
    }

    public void setDkkPicPath(String dkkPicPath) {
        this.dkkPicPath = dkkPicPath;
    }

    public String getGrzxPicPath() {
        return grzxPicPath;
    }

    public void setGrzxPicPath(String grzxPicPath) {
        this.grzxPicPath = grzxPicPath;
    }

    //-----------------------纵通项目字段结束------------------------------------
	public UserCache() {
		super();
	}

	public UserCache(User user, String addIp) {
		this.addIp = addIp;
		this.user = user;
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
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param user 要设置的用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取性别 1:男,0:女
	 * 
	 * @return 性别 1:男,0:女
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * 设置性别 1:男,0:女
	 * 
	 * @param sex 要设置的性别 1:男,0:女
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * 获取状态 1:锁定，0:解锁
	 * 
	 * @return 状态 1:锁定，0:解锁
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态 1:锁定，0:解锁
	 * 
	 * @param status 要设置的状态 1:锁定，0:解锁
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取用户类型(参考rd_user_type)
	 * 
	 * @return 用户类型(参考rd_user_type)
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * 设置用户类型(参考rd_user_type)
	 * 
	 * @param userType 要设置的用户类型(参考rd_user_type)
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	/**
	 * 获取用户性质 1:自然人,2:公司法人(公司注册),3:担保公司
	 * 
	 * @return 用户性质 1:自然人,2:公司法人(公司注册),3:担保公司
	 */
	public int getUserNature() {
		return userNature;
	}

	/**
	 * 设置用户性质 1:自然人,2:公司法人(公司注册),3:担保公司
	 * 
	 * @param userNature 要设置的用户性质 1:自然人,2:公司法人(公司注册),3:担保公司
	 */
	public void setUserNature(int userNature) {
		this.userNature = userNature;
	}


	/**
	 * 获取证件类型
	 * 
	 * @return 证件类型
	 */
	public int getCardType() {
		return cardType;
	}

	/**
	 * 设置证件类型
	 * 
	 * @param cardType 要设置的证件类型
	 */
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	/**
	 * 获取证件图片正面
	 * 
	 * @return 证件图片正面
	 */
	public String getCardPositive() {
		return cardPositive;
	}

	/**
	 * 设置证件图片正面
	 * 
	 * @param cardPositive 要设置的证件图片正面
	 */
	public void setCardPositive(String cardPositive) {
		this.cardPositive = cardPositive;
	}

	/**
	 * 获取证件图片反面
	 * 
	 * @return 证件图片反面
	 */
	public String getCardOpposite() {
		return cardOpposite;
	}

	/**
	 * 设置证件图片反面
	 * 
	 * @param cardOpposite 要设置的证件图片反面
	 */
	public void setCardOpposite(String cardOpposite) {
		this.cardOpposite = cardOpposite;
	}

	/**
	 * 获取头像 0:默认,1:自定义
	 * 
	 * @return 头像 0:默认,1:自定义
	 */
	public int getHeadStatus() {
		return headStatus;
	}

	/**
	 * 设置头像 0:默认,1:自定义
	 * 
	 * @param headStatus 要设置的头像 0:默认,1:自定义
	 */
	public void setHeadStatus(int headStatus) {
		this.headStatus = headStatus;
	}

	/**
	 * 获取登录失败次数
	 * 
	 * @return 登录失败次数
	 */
	public int getLoginFailTimes() {
		return loginFailTimes;
	}

	/**
	 * 设置登录失败次数
	 * 
	 * @param loginFailTimes 要设置的登录失败次数
	 */
	public void setLoginFailTimes(int loginFailTimes) {
		this.loginFailTimes = loginFailTimes;
	}

	/**
	 * 获取锁定时间
	 * 
	 * @return 锁定时间
	 */
	public Date getLockTime() {
		return lockTime;
	}

	/**
	 * 设置锁定时间
	 * 
	 * @param lockTime 要设置的锁定时间
	 */
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	/**
	 * 获取最后登录时间
	 * 
	 * @return 最后登录时间
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * 设置最后登录时间
	 * 
	 * @param loginTime 要设置的最后登录时间
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * 获取交易密码错误次数
	 * 
	 * @return
	 */
	public int getPayFailTimes() {
		return payFailTimes;
	}

	/**
	 * 设置交易密码错误次数
	 * 
	 * @param payFailTimes
	 */
	public void setPayFailTimes(int payFailTimes) {
		this.payFailTimes = payFailTimes;
	}

	/**
	 * 获取交易密码锁定时间
	 * 
	 * @return
	 */
	public Date getLockPayTime() {
		return lockPayTime;
	}

	/**
	 * 获取交易密码锁定时间
	 * 
	 * @param lockPayTime
	 */
	public void setLockPayTime(Date lockPayTime) {
		this.lockPayTime = lockPayTime;
	}

	/**
	 * 获取密码修改时间
	 * 
	 * @return 密码修改时间
	 */
	public Date getPwdModifyTime() {
		return pwdModifyTime;
	}

	/**
	 * 设置密码修改时间
	 * 
	 * @param pwdModifyTime 要设置的密码修改时间
	 */
	public void setPwdModifyTime(Date pwdModifyTime) {
		this.pwdModifyTime = pwdModifyTime;
	}

	/**
	 * 获取交易密码修改时间
	 * 
	 * @return
	 */
	public Date getPayPwdModifyTime() {
		return payPwdModifyTime;
	}

	/**
	 * 设置交易密码修改时间
	 * 
	 * @param payPwdModifyTime
	 */
	public void setPayPwdModifyTime(Date payPwdModifyTime) {
		this.payPwdModifyTime = payPwdModifyTime;
	}

	/**
	 * 获取省
	 * 
	 * @return 省
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 设置省
	 * 
	 * @param province 要设置的省
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 获取市
	 * 
	 * @return 市
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置市
	 * 
	 * @param city 要设置的市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取区
	 * 
	 * @return 区
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置区
	 * 
	 * @param area 要设置的区
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取地址
	 * 
	 * @return 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * 
	 * @param address 要设置的地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取客服ID
	 * 
	 * @return 客服ID
	 */
	public int getCustomerUserId() {
		return customerUserId;
	}

	/**
	 * 设置客服ID
	 * 
	 * @param customerUserId 要设置的客服ID
	 */
	public void setCustomerUserId(int customerUserId) {
		this.customerUserId = customerUserId;
	}

	/**
	 * 获取客服姓名
	 * 
	 * @return 客服姓名
	 */
	public String getCustomerUserName() {
		return customerUserName;
	}

	/**
	 * 设置客服姓名
	 * 
	 * @param customerUserName 要设置的客服姓名
	 */
	public void setCustomerUserName(String customerUserName) {
		this.customerUserName = customerUserName;
	}

	/**
	 * 获取客服添加时间
	 * 
	 * @return 客服添加时间
	 */
	public Date getCustomerAddTime() {
		return customerAddTime;
	}

	/**
	 * 设置客服添加时间
	 * 
	 * @param customerAddTime 要设置的客服添加时间
	 */
	public void setCustomerAddTime(Date customerAddTime) {
		this.customerAddTime = customerAddTime;
	}


	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public int getLoginPwdStatus() {
		return loginPwdStatus;
	}

	public void setLoginPwdStatus(int loginPwdStatus) {
		this.loginPwdStatus = loginPwdStatus;
	}

	public int getPayPwdStatus() {
		return payPwdStatus;
	}

	public void setPayPwdStatus(int payPwdStatus) {
		this.payPwdStatus = payPwdStatus;
	}

	public String getIdCardPicPositive() {
		return idCardPicPositive;
	}

	public void setIdCardPicPositive(String idCardPicPositive) {
		this.idCardPicPositive = idCardPicPositive;
	}

	public String getIdCardPicOther() {
		return idCardPicOther;
	}

	public void setIdCardPicOther(String idCardPicOther) {
		this.idCardPicOther = idCardPicOther;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
}
