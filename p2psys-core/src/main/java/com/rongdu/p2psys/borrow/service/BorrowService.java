package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAppointmentBid;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.model.ExchangeRatePacketCaptureModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 标Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月4日
 */
public interface BorrowService {

	/**
	 * 新增
	 * 
	 * @param model
	 * @param user
	 * @param list
	 * @return Borrow
	 */
	Borrow save(BorrowModel model, User user);

	/**
	 * 更新
	 * 
	 * @param model
	 * @param oldBorrow
	 * @param list
	 * @param ids
	 */
	public void updateLoan(BorrowModel model, Borrow oldBorrow,
			List<BorrowUpload> list, long[] ids);

	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<BorrowModel> list(BorrowModel model);

	/**
	 * 撤回标
	 * 
	 * @param id
	 * @return Borrow
	 */
	void cancel(Borrow borrow) throws Exception;

	/**
	 * 已登记未确认的借款标的取消
	 * 
	 * @param id
	 * @return Borrow
	 */
	void borrowCancel(Borrow borrow) throws Exception;

	/**
	 * 修改标
	 * 
	 * @param model
	 */
	void update(Borrow borrow);

	/**
	 * 获取所有借款标
	 * 
	 * @return
	 */
	List<BorrowModel> getBorrowList();

	/**
	 * 未完成的净值标
	 * 
	 * @param userId
	 *            userId
	 * @return List
	 */
	int unfinshJinBorrowCount(long userId);

	/**
	 * 净值标待还本息
	 * 
	 * @param userId
	 * @return
	 */
	double getRepayTotalWithJin(long userId);

	/**
	 * 通过主键获得borrow实体类
	 * 
	 * @param userId
	 * @return
	 */
	Borrow find(long id);

	/**
	 * 还款操作
	 * 
	 * @param repay
	 * @param act
	 */
	void doRepay(BorrowRepayment borrowRepayment);
	
	/**
	 * VIP还款操作
	 * 
	 * @param repay
	 * @param act
	 */
	void doVipRepay();


	/**
	 * 提前还款操作
	 * 
	 * @param borrowRepayment
	 */
	public void doPriorRepay(BorrowRepayment borrowRepayment);

	/**
	 * 更新标
	 * 
	 * @param b
	 */
	void addBorrow(Borrow b);

	/**
	 * 标初审
	 * 
	 * @param model
	 */
	void verify(BorrowModel model, Operator operator);

	/**
	 * 标初审-标准
	 * 
	 * @param model
	 */
	public void verifyBz(BorrowModel model, Operator operator);

	/**
	 * 标复审
	 * 
	 * @param model
	 * @throws Exception
	 */
	void verifyFull(BorrowModel model, Operator operator,
			String cashPurchasePrice) throws Exception;

	/**
	 * 更新标的状态
	 * 
	 * @param id
	 *            标ID
	 * @param status
	 *            目标状态
	 * @param preStatus
	 *            前一状态
	 * @return
	 */
	void updateStatus(long id, int status, int preStatus);

	/**
	 * 根据标Id获得标的详情
	 * 
	 * @param id
	 * @return
	 */
	Borrow getBorrowById(long id);

	/**
	 * 统计发标待审总数
	 * 
	 * @param status
	 * @return
	 */
	int trialCount(int status);

	/**
	 * 统计满标复审总数
	 * 
	 * @param status
	 * @return
	 */
	int fullCount(int status);

	/**
	 * 已成交的借款数 status=6,7,8
	 * 
	 * @return
	 */
	Object[] countByFinish();

	/**
	 * 截标
	 * 
	 * @param id
	 */
	void stopBorrow(Borrow borrow);

	/**
	 * 流标
	 * 
	 * @param model
	 * @return
	 */
	List<BorrowModel> spreadBorrowList(BorrowModel model);

	/**
	 * 校验用户是否借款人
	 * 
	 * @param borrow
	 * @param user
	 * @return
	 */
	boolean isBorrowUser(long borrowId, long userId);

	/**
	 * 校验用户是否投资人
	 * 
	 * @param borrow
	 * @param user
	 * @return
	 */
	boolean isTenderUser(long borrowId, long userId);

	/**
	 * 获得招标中的标
	 * 
	 * @param model
	 * @return PageDataList<BorrowModel>
	 */
	PageDataList<BorrowModel> getInviteList(BorrowModel model);

	/**
	 * 获得非流转的标
	 * 
	 * @param borrowId
	 *            标ID
	 * @return Borrow
	 */
	Borrow findNotFlow(long borrowId);

	public boolean allowPublish(User user, BorrowModel model);

	/**
	 * 逾期垫付
	 * 
	 * @param borrowRepayment
	 */
	public void overduePayment(BorrowRepayment borrowRepayment);

	/**
	 * 查到当前ID所有BorrowUpload对象
	 * 
	 * @param id
	 *            标ID
	 * @return List<BorrowUpload>
	 */
	List<BorrowUpload> findPicByBorrowId(long id);

	/**
	 * 商家账户查看招标中的项目
	 * 
	 * @param user
	 * @return
	 */
	public List<Borrow> businessBid(User user);

	/**
	 * 商家账户查看还款中的项目
	 * 
	 * @param user
	 * @return
	 */
	public List<BorrowModel> businessRepayment(User user);

	/**
	 * 正在借款项目个数
	 * 
	 * @param userId
	 * @return
	 */
	public int findByStatusAndUserId(long userId, int status1, int status2);

	/**
	 * 正在借款项目金额
	 * 
	 * @param userId
	 * @return
	 */
	public double findAccountTotalByStatus(long userId, int status1, int status2);

	/**
	 * 可借款标
	 * 
	 * @return
	 */
	public List<BorrowModel> investList(User user);

	/**
	 * 前台分页
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<BorrowModel> getList(BorrowModel model);

	/**
	 * 是否已存在合同号
	 * 
	 * @param dealNo
	 * @return
	 */
	boolean isExistDealNo(String dealNo);

	/**
	 * 更新抵押物图片
	 * 
	 * @param list
	 *            新上传的图片集合
	 * @param delIds
	 *            删除的图片ID
	 */
	void updatePic(List<BorrowUpload> list, long[] delIds);

	/**
	 * 根据时间获取借款金额
	 * 
	 * @param date
	 * @return 借款金额
	 */
	Double getBorrowAccountByDate(String date);

	/**
	 * 成功发标个数
	 */
	int count();

	/**
	 * 获取当前担保公司正在担保的标的总个数
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	int getGuaranteeingCount(long userId);

	/**
	 * 获取当前担保公司正在担保的标的总金额信息
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	double getGuaranteeingAccount(long userId);

	/**
	 * 获取当前担保公司待登记的标的总个数
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	int getNeedGuaranteeRegisteCount(long userId);

	/**
	 * 获取当前担保公司待登记的标的总金额信息
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	double getNeedGuaranteeRegisteAccount(long userId);

	/**
	 * 获取当前担保公司逾期项目个数
	 */
	int getOverdueCount(long userId);

	/**
	 * 获取当前担保公司待登记的标的信息列表
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	List<BorrowModel> getNeedGuaranteeRegisteList(long userId);

	/**
	 * 获取当前担保公司正在担保的标的信息列表
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	PageDataList<BorrowModel> getGuaranteeingList(BorrowModel model);

	/**
	 * 获取当前担保公司逾期的标的信息列表
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	PageDataList<BorrowRepaymentModel> getOverdueGuaranteeList(BorrowModel model);

	/**
	 * 获取当前担保公司已经代偿的标的信息列表
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	PageDataList<BorrowRepaymentModel> getCompensatedList(BorrowModel model);

	/**
	 * 根据发标时间段及项目状态统计项目总数
	 * 
	 * @param status
	 *            当status为99时查询全部
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getAllCount(int status, String startTime, String endTime);

	/**
	 * 项目总金额
	 */
	double getAllMomeny();

	/**
	 * 根据还款时间统计已还款逾期项目总数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getAllOverduedCount(String startTime, String endTime);

	/**
	 * 根据还款时间统计逾期已还款项目总数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	double getAllOverduedMomeny(String startTime, String endTime);

	/**
	 * 根据标状态获取个数
	 * 
	 * @param status
	 * @return
	 */
	int getCountByStatus(int status);

	/**
	 * 根据标状态获取个数
	 * 
	 * @param status
	 * @return
	 */
	double getMomenyByStatus(int status);

	/**
	 * 已满标未复审的项目数量
	 * 
	 * @return
	 */
	int getVerifyFullCount();

	/**
	 * 根据借款标发布时间统计已满标未复审项目数量
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getVerifyFullCount(String startTime, String endTime);

	/**
	 * 已满标未复审的项目金额
	 * 
	 * @return
	 */
	double getVerifyFullMomeny();

	/**
	 * 根据借款标发布时间统计已满标未复审项目金额
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	double getVerifyFullMoney(String startTime, String endTime);

	/**
	 * 根据应还款时间统计逾期未还款项目个数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getAllOverdueingCount(String startTime, String endTime);

	/**
	 * 逾期中项目金额
	 * 
	 * @return
	 */
	double getAllOverdueingMomeny();

	/**
	 * 根据应还款时间统计逾期未还款项目金额
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	double getAllOverdueingMoney(String startTime, String endTime);

	/**
	 * 获取已确认待初审的借款列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<BorrowModel> getConfirmedBorrowList(BorrowModel model);

	/**
	 * 获取环迅下待初审个数
	 * 
	 * @return
	 */
	long ipsTrialCount();

	/**
	 * 获取尚未满标的最后一个借款标
	 * 
	 * @return
	 */
	Borrow getLastBorrow();

	/**
	 * 获取招标中项目个数
	 * 
	 * @return
	 */
	int getInviteCount();

	/**
	 * 根据发标时间统计正在招标中项目个数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getInviteCount(String startTime, String endTime);

	/**
	 * 获取招标中项目金额
	 * 
	 * @return
	 */
	double getInviteMoney();

	/**
	 * 根据发标时间统计正在招标中项目金额
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	double getInviteMoney(String startTime, String endTime);

	/**
	 * 根据借款标发标时间及状态统计借款标个数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @return
	 */
	int getBorrowCount(String startTime, String endTime, int... status);

	/**
	 * 根据发标时间段搜索借款标发布金额
	 * 
	 * @param startTime
	 * @param endTime
	 * @param status
	 *            当status为99时则代表查询全部
	 * @return
	 */
	double getBorrowAccount(String startTime, String endTime, int... status);

	/**
	 * 统计借款人数
	 * 
	 * @return
	 */
	int getBorrowUserCount();

	/**
	 * 汇率抓包
	 */
	public void doExchangeRatePacketCapture();

	public List<ExchangeRatePacketCapture> getCaptureRate(long borrowId);

	/**
	 * 海外投资产品（预约投标）
	 * 
	 * @param user
	 * @param borrow
	 */
	public void appointmentBid(User user, Borrow borrow, double money);

	/**
	 * 根据borrowId获得预约投标的列表
	 * 
	 * @param borrowId
	 * @return
	 */
	public List<BorrowAppointmentBid> getBorrowAppointmentBidByBorrowId(
			long borrowId);

	/**
	 * 借款预约总额
	 * 
	 * @param borrow
	 * @return
	 */
	public double sumBidMoney(BorrowModel borrow);

	/**
	 * 海外投资产品（预约标进行投标）
	 * 
	 * @throws Exception
	 */
	public void doAppointmentBid() throws Exception;

	/**
	 * 设置预期收益率
	 * 
	 * @param borrowId
	 */
	public void repaymentEntrustEdit(long repaymentId, double expectedRate);

	/**
	 * 预约产品上线发送短信
	 */
	public void doAppointmentSendSms();
	
	/**
	 * 提醒流标和满标
	 */
	public void doRemindFailAndFullBids();

	/**
	 * 获取首页借款标推荐数据
	 * 
	 * @param borrowType
	 * @return
	 */
	public BorrowModel getLastBorrow(int borrowType);
	
	/**
	 * vip自动还款
	 **/

	/**
	 * 汇率管理
	 * 
	 * @param model
	 * @return
	 */
	public PageDataList<ExchangeRatePacketCaptureModel> exchangeRatePacketCaptureList(
			BorrowModel model);

	public ExchangeRatePacketCapture getExchangeRatePacketCapture(long id);

	public void exchangeRatePacketCaptureEdit(long id, double cashPurchasePrice);

	/**
	 * 中期还款
	 */
	public void middleReapy();

	public Borrow getBorrowByBorrowName(String borrowName);

	public void exchangeRatePacketCaptureAdd(ExchangeRatePacketCapture erpc);
}
