package com.rongdu.p2psys.borrow.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowMortgage;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

public class BorrowModel extends Borrow {

	private static final long serialVersionUID = -6334914392240388373L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	/**
	 * 产品基本ID
	 **/
	private long productBasicId;

	/**
	 * 用户ID
	 **/
	private long userId;

	/**
	 * 担保公司ID
	 **/
	private long vouchFirmId;

	/**
	 * 标名
	 */
	private String borrowName;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 实名
	 */
	private String realName;

	/**
	 * 投标金额
	 **/
	private double money;

	/**
	 * 支付密码
	 **/
	private String payPwd;

	/**
	 * 最近还款时间
	 **/
	private String minRepaymentTime;

	/**
	 * 还款
	 **/
	private long repaymentId;

	/**
	 * 申请信用额度
	 **/
	private int amount;

	/**
	 * 详细说明
	 **/
	private String content;

	/**
	 * 备注
	 **/
	private String remark;

	/**
	 * 我要投资 - 利率排序
	 **/
	private int aprSearch;

	/**
	 * 我要投资 - 期限排序
	 **/
	private int timeSearch;

	/**
	 * 我要投资 - 借款金额排序
	 **/
	private int moneySearch;

	/**
	 * 我要投资 - 信用等级排序
	 **/
	private int creditSearch;

	/**
	 * 我要投资 - 升、降排序
	 **/
	private int order;

	/**
	 * 开始时间
	 **/
	private String startTime;

	/**
	 * 结束时间
	 **/
	private String endTime;
    private Date repayTime;
	

	public Date getRepayTime() {
//		if(getReviewTime()!=null){
//			long time = getReviewTime().getTime(); 
//			long day = getTimeLimit()*24*60*60*1000; 
//			time+=day; 
//			//System.out.println(time);
//		    repayTime=new Date(time);
//		}else{
//			repayTime=null;
//		}
		//System.out.println(this.repayTime);
		if(getReviewTime()!=null){
		Calendar ca = Calendar.getInstance(); 
		ca.setTime(getReviewTime());
		if(getBorrowTimeType() >0&&getBorrowTimeType()==1){
			ca.add(Calendar.DAY_OF_MONTH, getTimeLimit());// 30为增加的天数，可以改变的
		}else{
			ca.add(Calendar.MONTH, getTimeLimit());
		}
		  
		repayTime = ca.getTime(); 
		}else{
			repayTime=null;	
		}
		return repayTime;
	}

	public void setRepayTime(Date repayTime) {
		this.repayTime=repayTime;
	}

	/**
	 * 日期范围
	 * 
	 * <p>
	 * 0:全部
	 * </p>
	 * <p>
	 * 1:最近七天
	 * </p>
	 * <p>
	 * 2:最近一个月
	 * </p>
	 * <p>
	 * 3:最近两个月
	 * </p>
	 * <p>
	 * 4:最近三个月
	 * </p>
	 **/
	private int time;

	/**
	 * 还款传参使用借款ID
	 **/
	private long borrowId = 0;

	/**
	 * 是否为定向标
	 */
	private int isDXB;

	/**
	 * 是否为定时
	 */
	private int isDS;

	/**
	 * 借款标名隐藏后
	 **/
	@SuppressWarnings("unused")
	private String borrowNameHide;

	/**
	 * 是否流标
	 **/
	private boolean isFlow;

	/**
	 * 环讯处理结果
	 */
	private String bidStatus;

	/**
	 * 登记债券时候的订单号
	 */
	private String tenderBilNo;

	/**
	 * 登记债券时候的订单日期
	 */
	private String tenderBilDate;

	/**
	 * 环讯返回状态码
	 */
	private String errMsg;

	/**
	 * 车型抵押
	 **/
	private List<BorrowMortgage> borrowMortgageList;

	/**
	 * 正在借款项目信息
	 **/
	private int count;

	/**
	 * 总金额
	 */
	private double borrowTotal;

	/**
	 * 是否显示重新登记按钮
	 */
	private int isShowRegister;

	/**
	 * 当前需要还的期数
	 */
	private int currPeriod;

	/**
	 * 查询条件（借款人，邮箱地址，公司名称）
	 */
	private String searchName;

	/**
	 * 更新前标的状态
	 */
	private int preStatus;

	/**
	 * 红包id
	 */
	private Long[] ids;

	/**
	 * 单选红包id
	 */
	private long redPacketId;
	
	/**
	 * 加息券id
	 */
	private Long[] cids;

	/**
	 * 投标使用加息券
	 */
	private double borrowInterestRateValue;

	/**
	 * 投标使用VIP加息券
	 */
	private double vipInterestRateValue;

	/**
	 * 投资笔数
	 */
	private int tenderCount;

	/**
	 * 环讯提交标号
	 */
	private String submitBidNo;

	/**
	 * 还款时间（海外投资产品专用）
	 */
	private Date repaymentTime;

	/**
	 * 环讯LOGO
	 */
	private String bidLogo;

	/**
	 * 借款标头像
	 */
	private String borrowImg;

	/**
	 * 红包名称
	 */
	private String redPacketName;

	/**
	 * 类型描述
	 */
	private String typeStr;

	/**
	 * 借款期限描述
	 */
	private String timeLimitStr;

	/**
	 * 借款标状态描述
	 */
	private String statusStr;

	/**
	 * 还款方式描述
	 */
	private String styleStr;

	/**
	 * 投标类型
	 * 
	 * <p>
	 * 0:网站投标
	 * </p>
	 * <p>
	 * 1:自动投标
	 * </p>
	 * <p>
	 * 2:手机投标
	 * </p>
	 * <p>
	 * 3:预约投标
	 * </p>
	 */
	private byte tenderType;

	/**
	 * 对应产品ID
	 */
	private long productId;

	/**
	 * 对应产品类型的文字描述
	 */
	private String typeDesc;

	/**
	 * 微信素材 - 头图
	 */
	private String wechatHeadPic;

	/**
	 * 微信素材 - 产品亮点
	 */
	private String wechatLightspot;

	/**
	 * 微信素材 - 小贴士
	 */
	private String wechatTips;

	/**
	 * 微信素材 - 产品摘要
	 */
	private String wechatProductDigest;

	/**
	 * 微信素材 - 产品详情
	 */
	private String wechatProductDetail;

	/**
	 * 微信素材 - 收益时间概要
	 */
	private String wechatRefundDigest;

	/**
	 * 微信素材 - 收益时间详情
	 */
	private String wechatRefundDetail;

	/**
	 * 微信素材 - 安全保障概要
	 */
	private String wechatSafeguardDigest;

	/**
	 * 微信素材 - 安全保障详情
	 */
	private String wechatSafeguardDetail;

	/**
	 * 产品标签
	 */
	private Long flagId;

	/**
	 * 加息百分比
	 */
	private Double interestRateValue;

	private String url = Global.getValue("adminurl");

	public String getBorrowNameHide() {
		if (StringUtil.isNotBlank(getName()) && getName().length() > 7) {
			return getName().substring(0, 6) + "***";
		}
		return "";
	}

	public void setBorrowNameHide(String borrowNameHide) {
		this.borrowNameHide = borrowNameHide;
	}

	public static BorrowModel instance(Borrow borrow) {
		BorrowModel borrowModel = new BorrowModel();
		BeanUtils.copyProperties(borrow, borrowModel);
		return borrowModel;
	}

	public Borrow prototype() {
		Borrow borrow = new Borrow();
		BeanUtils.copyProperties(this, borrow);
		return borrow;
	}

	public static BorrowModel instanceCurr(Borrow borrow,
			BorrowModel borrowModel) {
		BeanUtils.copyProperties(borrow, borrowModel);
		return borrowModel;
	}

	/**
	 * 投标前校验
	 */
	public Borrow checkTenderModel(Borrow borrow, User user) {
		if (borrow.getStatus() != 1) {
			throw new BorrowException("不能进行投标!", 2);
		}
		if (borrow.getType() != Borrow.TYPE_ENTRUST) {
			if (borrow.getUser().getUserId() == user.getUserId()) {
				throw new BorrowException("自己不能投自己发布的标！", 2);
			}
		}
		if (StringUtil.isBlank(payPwd)) {
			throw new BorrowException("支付密码不能为空!", 2);
		}
		if (!MD5.encode(payPwd).equals(user.getPayPwd())) {
			throw new BorrowException("支付密码不正确!", 2);
		}
		if (StringUtil.isNotBlank(borrow.getPwd())
				&& (!getPwd().equals(borrow.getPwd()))) {
			throw new BorrowException("定向标密码不正确!", 2);
		}
		if (borrow.getAccountYes() >= borrow.getAccount()) {
			throw new BorrowException("此标已满!", 2);
		}
		return borrow;
	}

	/**
	 * 验证定向标密码不能为空
	 */
	public void validDXB() {
		if (this.isDXB == 1 && StringUtil.isBlank(getPwd())) {
			throw new BorrowException("定向标密码不能为空！", BorrowException.TYPE_JSON);
		}
	}

	/**
	 * 校验支付密码
	 * 
	 * @param user
	 */
	public void checkPayPwd(User user) {
		if (StringUtil.isBlank(this.payPwd)) {
			throw new BorrowException("请输入支付密码！", BorrowException.TYPE_JSON);
		}
		if (StringUtil.isBlank(user.getPayPwd())) {
			throw new BorrowException("请先设置支付密码！", BorrowException.TYPE_JSON);
		}
		if (!user.getPayPwd().equals(MD5.encode(this.payPwd))) {
			throw new BorrowException("请输入正确的支付密码！", BorrowException.TYPE_JSON);
		}
	}

	public List<BorrowUpload> picList(String fileValueList) {
		List<BorrowUpload> list = new ArrayList<BorrowUpload>();
		BorrowUpload borrowUpload = null;
		String[] str = fileValueList.split(",");
		for (int i = 0; i < str.length; i++) {
			if (StringUtil.isBlank(str[i])) {
				continue;
			}
			borrowUpload = new BorrowUpload();
			borrowUpload.setPicPath("/data/upfiles/images/borrow/" + str[i]);
			borrowUpload.setType(4);
			list.add(borrowUpload);
		}
		return list;
	}

	public String findReturnUrl(int type) {
		String url = "/invest/detail.html";
		switch (type) {
		case 119: // 海外投资
			url = "/invest/entrustDetail.html";
			break;
		case 122:// 浮动收益
			url = "/invest/entrustDetail.html";
			break;
		default:
			break;
		}
		return url;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size > 0 ? size : Page.ROWS;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getMinRepaymentTime() {
		return minRepaymentTime;
	}

	public void setMinRepaymentTime(String minRepaymentTime) {
		this.minRepaymentTime = minRepaymentTime;
	}

	public long getRepaymentId() {
		return repaymentId;
	}

	public void setRepaymentId(long repaymentId) {
		this.repaymentId = repaymentId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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

	public int getAprSearch() {
		return aprSearch;
	}

	public void setAprSearch(int aprSearch) {
		this.aprSearch = aprSearch;
	}

	public int getTimeSearch() {
		return timeSearch;
	}

	public void setTimeSearch(int timeSearch) {
		this.timeSearch = timeSearch;
	}

	public int getMoneySearch() {
		return moneySearch;
	}

	public void setMoneySearch(int moneySearch) {
		this.moneySearch = moneySearch;
	}

	public int getCreditSearch() {
		return creditSearch;
	}

	public void setCreditSearch(int creditSearch) {
		this.creditSearch = creditSearch;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}

	public int getIsDXB() {
		return isDXB;
	}

	public void setIsDXB(int isDXB) {
		this.isDXB = isDXB;
	}

	public int getIsDS() {
		return isDS;
	}

	public void setIsDS(int isDS) {
		this.isDS = isDS;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	public String getTenderBilNo() {
		return tenderBilNo;
	}

	public void setTenderBilNo(String tenderBilNo) {
		this.tenderBilNo = tenderBilNo;
	}

	public String getTenderBilDate() {
		return tenderBilDate;
	}

	public void setTenderBilDate(String tenderBilDate) {
		this.tenderBilDate = tenderBilDate;
	}

	public List<BorrowMortgage> getBorrowMortgageList() {
		return borrowMortgageList;
	}

	public void setBorrowMortgageList(List<BorrowMortgage> borrowMortgageList) {
		this.borrowMortgageList = borrowMortgageList;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getBorrowTotal() {
		return borrowTotal;
	}

	public void setBorrowTotal(double borrowTotal) {
		this.borrowTotal = borrowTotal;
	}

	public int getIsShowRegister() {
		return isShowRegister;
	}

	public void setIsShowRegister(int isShowRegister) {
		this.isShowRegister = isShowRegister;
	}

	public int getCurrPeriod() {
		return currPeriod;
	}

	public void setCurrPeriod(int currPeriod) {
		this.currPeriod = currPeriod;
	}

	public boolean isFlow() {
		return isFlow;
	}

	public void setFlow(boolean isFlow) {
		this.isFlow = isFlow;
	}

	public long getVouchFirmId() {
		return vouchFirmId;
	}

	public void setVouchFirmId(long vouchFirmId) {
		this.vouchFirmId = vouchFirmId;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public Long[] getCids() {
		return cids;
	}

	public void setCids(Long[] cids) {
		this.cids = cids;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public int getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(int preStatus) {
		this.preStatus = preStatus;
	}

	public double getBorrowInterestRateValue() {
		return borrowInterestRateValue;
	}

	public void setBorrowInterestRateValue(double borrowInterestRateValue) {
		this.borrowInterestRateValue = borrowInterestRateValue;
	}

	public double getVipInterestRateValue() {
		return vipInterestRateValue;
	}

	public void setVipInterestRateValue(double vipInterestRateValue) {
		this.vipInterestRateValue = vipInterestRateValue;
	}

	public int getTenderCount() {
		return tenderCount;
	}

	public void setTenderCount(int tenderCount) {
		this.tenderCount = tenderCount;
	}

	public String getSubmitBidNo() {
		return submitBidNo;
	}

	public void setSubmitBidNo(String submitBidNo) {
		this.submitBidNo = submitBidNo;
	}

	public Date getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public String getBidLogo() {
		return bidLogo;
	}

	public void setBidLogo(String bidLogo) {
		this.bidLogo = bidLogo;
	}

	public String getBorrowImg() {
		return borrowImg;
	}

	public void setBorrowImg(String borrowImg) {
		this.borrowImg = borrowImg;
	}

	public String getRedPacketName() {
		return redPacketName;
	}

	public void setRedPacketName(String redPacketName) {
		this.redPacketName = redPacketName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTypeStr() {
		switch (getType()) {
		case 103:
			typeStr = "固定收益类产品";
			break;
		case 119:
			typeStr = "海外投资产品";
			break;
		case 122:
			typeStr = "浮动收益类产品";
			break;
		case 199:
			typeStr = "VIP特享产品";
			break;
		default:
			typeStr = "标种异常";
			break;
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getTimeLimitStr() {
		switch (getBorrowTimeType()) {
		case 0:
			timeLimitStr = getTimeLimit() + "个月";
			break;
		case 1:
			timeLimitStr = getTimeLimit() + "天";
			break;
		default:
			timeLimitStr = "异常";
			break;
		}
		return timeLimitStr;
	}

	public void setTimeLimitStr(String timeLimitStr) {
		this.timeLimitStr = timeLimitStr;
	}

	public String getStatusStr() {
		switch (getStatus()) {
		case Borrow.STATUS_MANAGER_CANCEL_DOING:
			statusStr = "管理员撤回";
			break;
		case Borrow.STATUS_USER_CANCEL:
			statusStr = "用户撤回";
			break;
		case Borrow.STATUS_PUBLISHING:
			statusStr = "等待初审";
			break;
		case Borrow.STATUS_TRIAL_PASSED:
			statusStr = "初审通过";
			break;
		case Borrow.STATUS_AUTO_TENDER_DONE:
			statusStr = "自动投标中";
			break;
		case Borrow.STATUS_TRIAL_PASSLESS:
			statusStr = "初审不通过";
			break;
		case Borrow.STATUS_RECHECK_PASS:
			statusStr = "复审通过";
			break;
		case Borrow.STATUS_RECHECK_PASSLESS:
			statusStr = "复审不通过";
			break;
		case Borrow.STATUS_RECHECK_PASSLESS2:
			statusStr = "复审不通过";
			break;
		case Borrow.STATUS_MANAGER_CANCEL:
			statusStr = "管理员撤回";
			break;
		case Borrow.STATUS_MANAGER_CANCEL2:
			statusStr = "管理员撤回";
			break;
		case Borrow.STATUS_REPAYMENT_START:
			statusStr = "还款中";
			break;
		case Borrow.STATUS_REPAYMENT_DOING:
			statusStr = "部分还款";
			break;
		case Borrow.STATUS_REPAYMENT_DONE:
			statusStr = "已还款";
			break;
		case Borrow.STATUS_REGISTRATE_DOING:
			statusStr = "已登记待确认";
			break;
		case Borrow.STATUS_TRIAL_WAITING:
			statusStr = "已确认待初审";
			break;
		default:
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getStyleStr() {
		switch (getStyle()) {
		case 1:
			styleStr = "按月分期还款";
			break;
		case 2:
			styleStr = "一次性还款";
			break;
		case 3:
			styleStr = "每月还息到期还本";
			break;
		case 4:
			styleStr = "按中期天数还息到期还本";
			break;
		case 5:
			styleStr = "按中期天数分期还款";
			break;
		default:
			break;
		}
		return styleStr;
	}

	public void setStyleStr(String styleStr) {
		this.styleStr = styleStr;
	}

	public byte getTenderType() {
		return tenderType;
	}

	public void setTenderType(byte tenderType) {
		this.tenderType = tenderType;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getWechatHeadPic() {
		return wechatHeadPic;
	}

	public void setWechatHeadPic(String wechatHeadPic) {
		this.wechatHeadPic = wechatHeadPic;
	}

	public String getWechatLightspot() {
		return wechatLightspot;
	}

	public void setWechatLightspot(String wechatLightspot) {
		this.wechatLightspot = wechatLightspot;
	}

	public String getWechatTips() {
		return wechatTips;
	}

	public void setWechatTips(String wechatTips) {
		this.wechatTips = wechatTips;
	}

	public String getWechatProductDigest() {
		return wechatProductDigest;
	}

	public void setWechatProductDigest(String wechatProductDigest) {
		this.wechatProductDigest = wechatProductDigest;
	}

	public String getWechatProductDetail() {
		return wechatProductDetail;
	}

	public void setWechatProductDetail(String wechatProductDetail) {
		this.wechatProductDetail = wechatProductDetail;
	}

	public String getWechatRefundDigest() {
		return wechatRefundDigest;
	}

	public void setWechatRefundDigest(String wechatRefundDigest) {
		this.wechatRefundDigest = wechatRefundDigest;
	}

	public String getWechatRefundDetail() {
		return wechatRefundDetail;
	}

	public void setWechatRefundDetail(String wechatRefundDetail) {
		this.wechatRefundDetail = wechatRefundDetail;
	}

	public String getWechatSafeguardDigest() {
		return wechatSafeguardDigest;
	}

	public void setWechatSafeguardDigest(String wechatSafeguardDigest) {
		this.wechatSafeguardDigest = wechatSafeguardDigest;
	}

	public String getWechatSafeguardDetail() {
		return wechatSafeguardDetail;
	}

	public void setWechatSafeguardDetail(String wechatSafeguardDetail) {
		this.wechatSafeguardDetail = wechatSafeguardDetail;
	}

	public long getProductBasicId() {
		return productBasicId;
	}

	public void setProductBasicId(long productBasicId) {
		this.productBasicId = productBasicId;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public long getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(long redPacketId) {
		this.redPacketId = redPacketId;
	}

	public Double getInterestRateValue() {
		return interestRateValue;
	}

	public void setInterestRateValue(Double interestRateValue) {
		this.interestRateValue = interestRateValue;
	}

}
