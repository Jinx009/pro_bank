package com.rongdu.p2psys.borrow.dao;

import java.util.Date;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 标Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月4日
 */
/**
 * @author Administrator
 *
 */
public interface BorrowDao extends BaseDao<Borrow>
{

	/**
	 * @param borrow
	 */
	void modifyBorrowAndRepay(Borrow borrow);

	/**
	 * 首页借款标列表查询
	 * 
	 * @param model
	 * @return
	 */
	List<BorrowModel> getIndexList(BorrowModel model);

	/**
	 * 未完成的净值标
	 * 
	 * @param userId
	 * @return
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
	 * 投标
	 * 
	 * @param account
	 * @param status
	 * @param id
	 */
	void update(double account, double scales, int status, long id);

	/**
	 * 更新流转标次数
	 * 
	 * @param borrow
	 * @return
	 */
	void updateFlowTotalYesCount(Borrow borrow);

	/**
	 * 审核
	 * 
	 * @param id
	 * @param status
	 * @param preStatus
	 */
	void updateStatus(long id, int status, int preStatus);

	/**
	 * 修改状态
	 * 
	 * @param id
	 *            标ID
	 * @param status
	 *            状态
	 */
	void updateStatus(long id, int status);

	/**
	 * 修改定时时间
	 * 
	 * @param id
	 *            标ID
	 * @param time
	 *            定时时间
	 */
	void updatefixedTime(long id, BorrowModel model);

	/**
	 * 修改登记时间
	 * 
	 * @param id
	 *            标ID
	 * @param time
	 *            登记时间
	 */
	void updateRegisterTime(long id, Date time);

	/**
	 * 根据标名称获得标的详情
	 * 
	 * @param name
	 * @return
	 */
	Borrow getBorrowByName(String name);

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
	 * 已成交的借款数
	 * 
	 * @return
	 */
	Object[] countByFinish();

	/**
	 * 流标
	 * 
	 * @param model
	 * @return
	 */
	List<BorrowModel> spreadBorrowList(BorrowModel model);

	/**
	 * 获得招标中的标
	 * 
	 * @param model
	 * @param params
	 * @return PageDataList<Borrow>
	 */
	@SuppressWarnings("rawtypes")
	PageDataList<Borrow> getInviteList(BorrowModel model, List params);

	/**
	 * 获得非流转的标
	 * 
	 * @param borrowId
	 *            标ID
	 * @return Borrow
	 */
	Borrow findNotFlow(long borrowId);

	/**
	 * 修改标的标号
	 * 
	 * @param borrowId
	 *            标ID
	 * @param bidNo
	 *            标号
	 */
	void modifyBidNo(long borrowId, String bidNo);

	/**
	 * 修改标的担保方登记号
	 * 
	 * @param borrowId
	 *            标ID
	 * @param bidNo
	 *            标号
	 */
	void modifyGuaranteeNo(long borrowId, String guaranteeNo);

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
	 * 用户中心可投资的标
	 * 
	 * @param user
	 * @return
	 */
	public List<Borrow> getMemberInvestList(User user);

	/**
	 * 投资列表及后台标分页方法
	 * 
	 * @param model
	 *            标model
	 * @return borrow信息
	 */
	public PageDataList<Borrow> getList(BorrowModel model);

	/**
	 * 根据时间获取借款金额
	 * 
	 * @param date
	 * @return 借款金额
	 */
	double getBorrowAccountByDate(String date);

	/**
	 * 通过标号查询
	 * 
	 * @param name
	 * @return
	 */
	Borrow getBorrowByBidNo(String bidNo);

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
	 * 获取当前担保公司待登记的标的信息列表
	 * 
	 * @param userId
	 * @return 标的总信息
	 */
	List<Borrow> getNeedGuaranteeRegisteList(long userId);

	/**
	 * 根据还款时间统计已还款逾期项目总数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getAllOverduedCount(String startTime, String endTime);

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
	 * 根据发标时间段统计项目个数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param status
	 *            当status为99时查询全部
	 * @return
	 */
	int getAllCount(int status, String startTime, String endTime);

	/**
	 * 项目总金额
	 */
	double getAllMomeny();

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
	 */
	double getMomenyByStatus(int status);

	/**
	 * 已满标未复审的项目金额
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
	 * 获取已确认待初审的借款列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<Borrow> getConfirmedBorrowList(BorrowModel model);

	/**
	 * 更新borrow表中的待还与repayment保持一致
	 * 
	 * @param borrowId
	 * @param repayMoney
	 */
	public void updateRepaymentAccount(long borrowId, double repayMoney);

	/**
	 * 获取环迅待审个数
	 * 
	 * @return
	 */
	long ipsTrialCount();

	Borrow getLastBorrow();

	/**
	 * 招标中项目个数
	 * 
	 * @return
	 */
	int getInviteCount();

	/**
	 * 根据发标时间段搜索正在招标中项目个数
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
	 * 根据发标时间及借款标状态统计借款标个数
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
	 * 根据借款标发布时间统计已满标为复审项目个数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getVerifyFullCount(String startTime, String endTime);

	/**
	 * 统计借款人数
	 * 
	 * @return
	 */
	int getBorrowUserCount();

	/**
	 * 根据状态和类型查询标列表
	 * 
	 * @return
	 */
	public List<Borrow> getBorrowListByStatusAndType();

	public List<Borrow> getAppointmentBorrow();
	
	/*获取满标和流标列表*/
	public List<Borrow> getFullAndFailBorrow();

	/**
	 * 获取首页借款标推荐数据
	 * 
	 * @param borrowType
	 * @return
	 */
	public BorrowModel getLastBorrow(int borrowType);

	public BorrowModel getLastUnRecommendBorrow(int borrowType);

	public List<Borrow> getListByIsRecommend();
}
