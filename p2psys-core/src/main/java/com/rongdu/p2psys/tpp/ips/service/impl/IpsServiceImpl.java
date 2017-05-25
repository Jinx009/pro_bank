package com.rongdu.p2psys.tpp.ips.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.rule.BorrowAprLimitRuleCheck;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.tpp.IpsTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.IpsType;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsPayDao;
import com.rongdu.p2psys.tpp.ips.model.IpsAutoRecharge;
import com.rongdu.p2psys.tpp.ips.model.IpsRegisterGuarantor;
import com.rongdu.p2psys.tpp.ips.model.IpsRepayment;
import com.rongdu.p2psys.tpp.ips.model.IpsTransfer;
import com.rongdu.p2psys.tpp.ips.model.IpsTransferDetail;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

@Service(value = "ipsService")
@Transactional
public class IpsServiceImpl implements IpsService
{
	@Autowired
	private BorrowDao borrowDao;
	@Autowired
	private BorrowTenderDao borrowTenderDao;
	@Autowired
	private BorrowRepaymentDao borrowRepaymentDao;
	@Autowired
	private UserService userService;
	@Autowired
	private TppIpsPayDao tppIpsPayDao;
	@Autowired
	private BorrowTenderService borrowTenderService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private BorrowCollectionDao borrowCollectionDao;

	private Borrow data;

	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(IpsServiceImpl.class);

	/**
	 * 参数
	 */
	private Map<String, Object> map;
	/**
	 * 接口名称
	 */
	private final String tips = Global.getValue("api_name") + "提醒：";

	@Override
	public boolean doAddBorrow(BorrowModel bm)
	{
		/**
		 * 标状态 -1用户撤回;0等待初审;1初审通过;2初审未通过;3复审通过;4/49复审未通过;5/59管理员撤回;6还款中;7部分还款;8
		 * 还款成功； 环讯新增状态 9：登记成功（对应环讯接口文档中MG02500F） 11 募集中（只有为这个状态才能初审
		 * （对应环讯接口文档中MG02501F））， -2：撤回处理中（（对应环讯接口文档中MG02503F）） 5:
		 * 管理员撤回（对应环讯接口文档中MG02505F）
		 */
		Borrow b = borrowDao.getBorrowByBidNo(bm.getBidNo());
		if (b == null)
		{
			return false;
		}
		if (bm.getErrMsg().equals("MG02500F")
				&& (b.getStatus() == 0 || b.getStatus() == 9))
		{
			borrowDao.updateRegisterTime(b.getId(), new Date());
			borrowDao.updateStatus(b.getId(), 9);
		} else if (bm.getErrMsg().equals("MG02501F") && b.getStatus() == 9)
		{
			borrowDao.updateStatus(b.getId(), 11);
		} else if (bm.getErrMsg().equals("MG02503F") && b.getStatus() != 5)
		{
			borrowDao.updateStatus(b.getId(), -2);
		} else if (bm.getErrMsg().equals("MG02505F") && b.getStatus() != 5)
		{
			// 需要解冻投资人的 钱
			try
			{
				borrowService.cancel(b);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public boolean doAddTender(BorrowModel bm)
	{
		if (bm.getBidStatus().equals("0"))
		{
			BorrowTender borrowTender = borrowTenderDao.getTenderByBillNo(bm
					.getTenderBilNo());
			if (borrowTender == null)
			{
				Borrow b = borrowDao.find(bm.getId());
				User user = userService.getUserById(bm.getUserId());
				bm.setUser(user);
				BorrowTender tender = borrowTenderService.addTender(b, bm);
				borrowTenderDao.modifyTenderBilNo(tender.getId(),
						bm.getTenderBilNo());
			}
		}
		return true;
	}

	@Override
	public boolean doIpsTask(List<Object> taskList)
	{
		boolean isSuccess = true;
		for (Object tppIpsPay : taskList)
		{
			TppIpsPay ip = (TppIpsPay) tppIpsPay;
			if (isSuccess)
			{
				try
				{
					ip = autoIpsPay(ip); // 获取最新的yp
					ip.setStatus((byte) 1);
					ip.setMessage("success"); // 处理成功
				} catch (Exception e)
				{
					ip.setMessage(e.getMessage());
					ip.setStatus((byte) 2);
					// 一旦处理失败，环讯就不在触发;但是记录还要插入记录表tpp_ips_pay
					isSuccess = false;
				}
			}
			try
			{

				tppIpsPayDao.save(ip);
			} catch (Exception e)
			{
				logger.error(e + "保存交易信息出错！！！");
			}
		}
		return isSuccess;
	}

	@Override
	public void transfer(Borrow borrow, List<BorrowTender> tender,
			String transferType, String transferMode)
	{
		User inUser = null;
		User outUser = null;
		double payAccount = 0;
		double payManageFee = 0;
		IpsTransfer ipsTransfer = new IpsTransfer();
		ipsTransfer.setMerBillNo(OrderNoUtils.getSerialNumber());
		ipsTransfer.setBidNo(borrow.getBidNo());
		ipsTransfer.setDate(DateUtil.dateStr7(new Date()));
		ipsTransfer.setTransferType(transferType);
		ipsTransfer.setTransferMode(transferMode);
		ipsTransfer.setS2SUrl(Global.getValue("admins2surl")
				+ "/public/ips/ipsTransferNotify.html");
		StringBuffer details = new StringBuffer();
		double manageFee = borrow.getBorrowManageFee();
		// 平台收取的总手续费
		double manageFeeTotal = BigDecimalUtil.mul(borrow.getAccount(),
				borrow.getBorrowManageRate() / 100);
		if (borrow.getGuaranteeFee() > 0
				&& !StringUtil.isBlank(borrow.getGuaranteeNo()))
		{
			manageFeeTotal = BigDecimalUtil.sub(manageFeeTotal,
					borrow.getGuaranteeFee());
		}
		// 每一笔投资的手续费
		double feeTender = 0;
		// 每一笔投资的手续费累计值
		double feeTenderTotal = 0;
		// 满标复审通过，投资人转账给借款人
		if (transferType.equals("1"))
		{
			inUser = borrow.getUser();
			outUser = null;
			payAccount = borrow.getAccount();
			payManageFee = manageFeeTotal;
			// 投资转账
			for (int i = 0; i < tender.size(); i++)
			{
				BorrowTender bt = tender.get(i);
				IpsTransferDetail row = new IpsTransferDetail();
				// 最后一笔手续费做减法
				if (i == (tender.size() - 1))
				{
					feeTender = BigDecimalUtil.sub(manageFeeTotal,
							feeTenderTotal);
				} else
				{
					feeTender = BigDecimalUtil.mul(bt.getAccount(), manageFee);
				}
				if (feeTender < 0)
				{
					feeTender = 0;
				}
				feeTenderTotal = BigDecimalUtil.add(feeTenderTotal, feeTender);
				row.setfAcctType("1");
				// TODO zjj
				// row.setfIpsAcctNo(bt.getUser().getApiId());
				// row.settIpsAcctNo(borrow.getUser().getApiId());
				row.setfTrdFee("0.00");
				row.settTrdFee(XmlTool.format2Str(feeTender));
				row.setOriMerBillNo(bt.getTenderBilNo());
				row.settAcctType("1");
				row.setTrdAmt(XmlTool.format2Str(bt.getAccount()));
				details.append(row.createSign(row.getParamNames()));
			}
		} else if (transferType.equals("5"))
		{
			inUser = borrow.getVouchFirm();
			outUser = borrow.getUser();
			payAccount = borrow.getGuaranteeFee();
			payManageFee = 0;
			// 担保收益转账
			IpsTransferDetail row = new IpsTransferDetail();
			row.setfAcctType("1");
			// TODO zjj
			// row.setfIpsAcctNo(borrow.getUser().getApiId());
			// row.settIpsAcctNo(borrow.getVouchFirm().getApiId());
			row.setfTrdFee("0.00");
			row.settTrdFee("0.00");
			row.setOriMerBillNo(borrow.getGuaranteeNo());
			row.settAcctType("0");
			row.setTrdAmt(XmlTool.format2Str(borrow.getGuaranteeFee()));
			details.append(row.createSign(row.getParamNames()));
		}
		ipsTransfer.setDetails(details.toString());
		ipsTransfer.setMemo1(String.valueOf(borrow.getId()));
		ipsTransfer.createSign();
		try
		{
			String res = ipsTransfer.doNotifySubmit(ipsTransfer.WS_URL,
					"Transfer");
			String pErrCode = XmlTool.getXmlNodeValue(res, "pErrCode");
			if (!"MG00008F".equals(pErrCode) && !"MG00000F".equals(pErrCode))
			{
				logger.error(XmlTool.getXmlNodeValue(res, "pErrMsg"));
				throw new BussinessException("转账失败", 1);
			}
			ipsTransfer.setErrCode(pErrCode);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new BussinessException("转账失败", 1);
		}
		TppIpsPay pay = new TppIpsPay();
		if (inUser != null)
		{
			pay.setInUserId(inUser.getUserId());
			// TODO zjj
			// pay.setInAcctNo(inUser.getApiId());
		}
		if (outUser != null)
		{
			pay.setOutUserId(outUser.getUserId());
			// TODO zjj
			// pay.setOutAcctNo(outUser.getApiId());
		}
		pay.setType(TppIpsPay.TYPE_DO_TRANSFER);
		pay.setTransferType(transferType);
		pay.setMerBillNo(ipsTransfer.getMerBillNo());
		pay.setAddTime(new Date());
		pay.setBorrowId(borrow.getId());
		pay.setStatus(TppIpsPay.STATUS_WAIT);
		pay.setAccount(payAccount);
		pay.setManageFee(payManageFee);
		tppIpsPayDao.save(pay);
	}

	@Override
	public void transfer(Borrow borrow, BorrowTender tender, User u, User toU,
			String transferType, String transferMode, List<Object> taskList)
	{
		double fee = 0.0;
		double account = tender.getAccount();
		fee = BigDecimalUtil.round(BigDecimalUtil.mul(account,
				borrow.getBorrowManageFee()));
		TppIpsPay tppIpsPay = new TppIpsPay();
		tppIpsPay.setAddTime(new Date());
		tppIpsPay.setBorrowId((int) borrow.getId());
		tppIpsPay.setTenderId((int) tender.getId());
		tppIpsPay.setOutUserId(u.getUserId());
		// TODO zjj
		// tppIpsPay.setOutAcctNo(u.getApiId());
		// tppIpsPay.setInAcctNo(toU.getApiId());
		tppIpsPay.setInUserId(toU.getUserId());
		tppIpsPay.setTransferType(transferType);
		tppIpsPay.setAccount(tender.getAccount());
		tppIpsPay.setIpsBillNo(borrow.getBidNo());
		tppIpsPay.setOriMerBillNo(tender.getTenderBilNo());
		tppIpsPay.setManageFee(fee);
		tppIpsPay.setType(IpsType.TRANFER);
		taskList.add(tppIpsPay);
	}

	public TppIpsPay autoIpsPay(TppIpsPay tppIpsPay)
	{
		if ("1".equals(tppIpsPay.getStatus()))
		{
			// 过滤 已经完成的
			throw new BussinessException("已经处理完成，请不要重复处理。");
		}
		// 转账
		if (IpsType.TRANFER.equals(tppIpsPay.getType()))
		{
			IpsTransfer ipsTransfer = IpsTPPWay.transfer(
					tppIpsPay.getIpsBillNo(), tppIpsPay.getTransferType(),
					tppIpsPay.getOriMerBillNo(),
					XmlTool.format2Str(tppIpsPay.getAccount()),
					tppIpsPay.getOutAcctNo(), tppIpsPay.getInAcctNo(),
					XmlTool.format2Str(tppIpsPay.getManageFee()));
			if (ipsTransfer != null)
			{
				if (!"MG00008F".equals(ipsTransfer.getErrCode())
						&& !"MG00000F".equals(ipsTransfer.getErrCode()))
				{
					throw new BussinessException(tips + ipsTransfer.getErrMsg());
				}
			}
		}
		return tppIpsPay;
	}

	/**
	 * 环迅还款校验
	 * 
	 * @param repayment
	 * @return
	 */
	public boolean ipsRepayCheck(BorrowRepayment repay)
	{
		// 环迅还款300限制，在还款时，去掉还款成功的信息
		List<TppIpsPay> ipsPayList = tppIpsPayDao.getTppIpsPayList(repay
				.getId());
		double repayMoney = 0;// 已还或正在还款的总金额
		if (ipsPayList != null && ipsPayList.size() > 0)
		{
			for (TppIpsPay pay : ipsPayList)
			{
				repayMoney = repayMoney + pay.getAccount();
			}
		}
		double account = repay.getCapital() + repay.getInterest()
				+ repay.getLateInterest() + repay.getExtensionInterest();
		if (account > repayMoney)
		{
			return true;
		}
		return false;
	}

	@Override
	public IpsRepayment repaySkip(BorrowRepayment repayment, byte repayType)
	{
		BorrowRepayment repay = borrowRepaymentDao.find(repayment.getId());
		// 如果还款信息为空，或者还款状态不是还款处理中OR未还款OR部分还款中的任何一个，则return
		if (repay == null
				|| !(repay.getStatus() == BorrowRepayment.STATUS_WAIT_REPAY || repay
						.getStatus() == BorrowRepayment.STATUS_PART_REPAY))
		{
			return null;
		}
		String merBillNo = "";
		TppIpsPay tppPay = new TppIpsPay();
		if (!this.ipsRepayCheck(repay))
		{// 如果还款约束条件不成立，则查询是否有待处理的还款，如果有，则查询出来，补单
			// 查询
			tppPay = tppIpsPayDao.getTppIpsPayByRepayId(repayment.getId(),
					TppIpsPay.STATUS_WAIT);
			if (tppPay != null)
			{
				merBillNo = tppPay.getMerBillNo();
			} else
			{
				merBillNo = "R" + OrderNoUtils.getSerialNumber();
			}
		} else
		{
			merBillNo = "R" + OrderNoUtils.getSerialNumber();
		}

		map = new HashMap<String, Object>();
		List<BorrowCollection> collList = this.getCollList(repayment,
				repayType, merBillNo);
		if (collList == null)
		{
			return null;
		}

		this.data = borrowDao.find(repayment.getBorrow().getId());
		User user = data.getUser();
		Account account = accountDao.findObjByProperty("user.userId",
				user.getUserId());
		BorrowWorker worker = BorrowHelper.getWorker(data);
		worker.validBeforeRepayment(repayment, account);
		// 计算逾期利息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date nowTime = null;
		Date repaymentTime = null;
		try
		{
			nowTime = format.parse(DateUtil.dateStr2(new Date()));
			repaymentTime = format.parse(DateUtil.dateStr2(repayment
					.getRepaymentTime()));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		long day = nowTime.getTime() - repaymentTime.getTime();
		int days = (int) (day / (24 * 60 * 60 * 1000));
		if (days > 0)
		{
			repayment.setLateDays(days);
		}
		// 因为环迅分批次还款，还款金额校验时，需要计算本次的逾期利息，校验通过，修改还款信息的时候，恢复以前的逾期利息
		BorrowRepayment rep = borrowRepaymentDao.find(repayment.getId());
		repayment.setLateInterest(rep.getLateInterest());
		borrowRepaymentDao.update(repayment);
		try
		{
			IpsRepayment ips = new IpsRepayment(repayment, collList, repayType);
			ips.setMerBillNo(merBillNo);
			ips.createSign();
			if (tppPay == null || tppPay.getId() <= 0)
			{
				TppIpsPay pay = new TppIpsPay();
				pay.setType(TppIpsPay.TYPE_DO_RAPAY);
				pay.setOutUserId(user.getUserId());
				// TODO zjj
				// pay.setOutAcctNo(user.getApiId());
				pay.setTransferType(repayType + "");
				pay.setMerBillNo(ips.getMerBillNo());
				pay.setAddTime(new Date());
				pay.setRepaymentId(repayment.getId());
				pay.setStatus(TppIpsPay.STATUS_WAIT);
				pay.setAccount(Double.parseDouble(ips.getOutAmt()));
				pay.setManageFee(Double.parseDouble(ips.getOutFee()));
				tppIpsPayDao.save(pay);
			} else
			{
				tppPay.setAccount(Double.parseDouble(ips.getOutAmt()));
				tppPay.setManageFee(Double.parseDouble(ips.getOutFee()));
				tppIpsPayDao.update(tppPay);
			}
			return ips;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean ipsRayManage(TppIpsPay pay)
	{
		Boolean result = tppIpsPayDao.editTppIpsPay(pay, TppIpsPay.STATUS_WAIT);
		if (result && pay.getStatus() == TppIpsPay.STATUS_SUCCESS)
		{
			// 根据流水号查询还款历史信息，如果出现多条或者为空，则说明本次还款有误，数据需要核对，则return
			List<TppIpsPay> payList = tppIpsPayDao.getTppIpsPay(
					pay.getMerBillNo(), TppIpsPay.TYPE_DO_RAPAY);
			if (payList == null || payList.size() <= 0)
			{//
				return false;
			}
			BorrowRepayment borrowRepayment = null;
			try
			{
				borrowRepayment = borrowRepaymentDao.find(payList.get(0)
						.getRepaymentId());
			} catch (BorrowException bx)
			{
				return false;
			}
			borrowRepayment.setMerBillNo(pay.getMerBillNo());
			Boolean check = this.checkRepay(borrowRepayment);
			if (!check)
			{
				return false;
			}
			// 还款交给Disruptor处理
			Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());// 防止懒加载失效
			borrowRepayment.setBorrow(borrow);
			BorrowRepayment repay = new BorrowRepayment();
			repay = borrowRepayment;
			ConcurrentUtil.repay(repay);
		}
		return true;
	}

	@Override
	public IpsAutoRecharge doAutoRecharge(AccountRecharge recharge)
	{
		TPPWay way = TPPFactory.getTPPWay(recharge.getUser());
		// 是否平台支付手续费（1：平台支付2：用户支付）
		int rechargeWeb = Global.getInt("recharge_web"); // 获取平台是否自己垫付充值手续费
		String ipsFeeType = "";
		if (rechargeWeb == 1)
		{
			// 平台垫付充值手续费,不收取任何手续费
			ipsFeeType = "1";
		} else
		{
			// 从用户自己账户扣款
			ipsFeeType = "2";
		}
		IpsAutoRecharge ips = (IpsAutoRecharge) way.doAutoRecharge(
				recharge.getUser(), ipsFeeType, "1", recharge.getMoney(),
				Double.parseDouble("0.00"));
		return ips;
	}

	/**
	 * 环迅还款后回调：如果此次还款的代收里面，有任何一个待收不是待还款状态，则返回false 如果成功，则计算本次应还的金额
	 * 
	 * @param repay
	 *            还款
	 * @return 成功/失败
	 */
	private Boolean checkRepay(BorrowRepayment repay)
	{
		List<BorrowCollection> list = tppIpsPayDao.getCollByIpsNo(
				repay.getMerBillNo(), TppIpsPay.STATUS_SUCCESS);
		if (list != null && list.size() > 0)
		{
			for (BorrowCollection coll : list)
			{
				if (coll == null || coll.getStatus() != 0)
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 环迅还款待收信息处理
	 * 
	 * @param repay
	 * @param repayType
	 * @param merBillNo
	 * @return
	 */
	private List<BorrowCollection> getCollList(BorrowRepayment repay,
			byte repayType, String merBillNo)
	{
		List<BorrowCollection> list = tppIpsPayDao.getWaitColl(repay
				.getBorrow().getId(), repay.getPeriod());
		if (list == null || list.size() <= 0)
		{
			list = tppIpsPayDao
					.getCollByIpsNo(merBillNo, TppIpsPay.STATUS_WAIT);
		}
		List<BorrowCollection> itemList = new ArrayList<BorrowCollection>();
		double lateInter = 0; // 逾期利息
		if (list != null && list.size() > 0)
		{
			int forSize = list.size() >= 2 ? 2 : list.size();
			for (int i = 0; i < forSize; i++)
			{
				BorrowCollection coll = list.get(i);
				// 取到待收后，查看环迅资金处理日志，如果待收资金处理日志为处理成功，或者待处理状态，则说明本次不在需要处理的范围之内
				TppIpsPay itemPay = tppIpsPayDao.findObjByProperty(
						"collectionId", coll.getId());
				if (itemPay != null
						&& itemPay.getStatus() == TppIpsPay.STATUS_SUCCESS)
				{
					continue;
				}
				User inUser = coll.getUser();
				double account = 0;
				double capital = coll.getCapital();
				// 归还投资人利息
				double manageFee = getInvestRepayInterest(coll);
				coll.setManageFee(manageFee);
				// 计算逾期利息
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date nowTime = null;
				Date repaymentTime = null;
				try
				{
					nowTime = format.parse(DateUtil.dateStr2(new Date()));
					repaymentTime = format.parse(DateUtil.dateStr2(coll
							.getRepaymentTime()));
				} catch (ParseException e)
				{
					e.printStackTrace();
				}
				long day = nowTime.getTime() - repaymentTime.getTime();
				int days = (int) (day / (24 * 60 * 60 * 1000));
				String overdueFee = Global.getValue("overdue_fee");
				double overdue = Double.parseDouble(overdueFee);
				int FreeOverdueDay = Integer.parseInt(Global
						.getValue("free_overdue_day"));// ？天内免逾期罚息

				double capitalLate = 0; // 本金逾期金额
				double interestLate = 0; // 利息逾期金额
				double lateInterest = 0; // 逾期利息
				if (days <= FreeOverdueDay && days > 0)
				{
					capitalLate = coll.getCapital();
					interestLate = coll.getInterest();
					lateInterest = BigDecimalUtil.mul(BigDecimalUtil.mul(
							capitalLate + interestLate, days), overdue); // 计算出逾期利息
				} else if (days > FreeOverdueDay)
				{
					capitalLate = borrowCollectionDao.remainderCapital(coll
							.getTender().getId(), inUser.getUserId(), coll
							.getPeriod());
					interestLate = borrowCollectionDao.remainderInterest(coll
							.getTender().getId(), inUser.getUserId(), coll
							.getPeriod());
					lateInterest = BigDecimalUtil.mul(BigDecimalUtil.mul(
							capitalLate + interestLate, days), overdue); // 计算出逾期利息
				}
				// 根据投资比例归还逾期利息
				if (lateInterest > 0)
				{
					coll.setLateDays(days);
					lateInterest = BigDecimalUtil.round(lateInterest);
					coll.setLateInterest(lateInterest);
					account = account + lateInterest;
					lateInter = lateInter + lateInterest;
				}

				// 展期利息
				if (repay.getExtensionInterest() > 0)
				{
					double userTenderExtensionInterest = 0;
					double tenderExtensionInterest = BigDecimalUtil.mul(
							repay.getExtensionInterest(),
							(BigDecimalUtil.div(capital, repay.getCapital())));
					BorrowAprLimitRuleCheck aprLimitRuleCheck = (BorrowAprLimitRuleCheck) Global
							.getRuleCheck(EnumRuleNid.BORROW_APR_LIMIT
									.getValue());
					if (aprLimitRuleCheck != null
							&& aprLimitRuleCheck.extension.extension_enable)
					{
						userTenderExtensionInterest = BigDecimalUtil.mul(
								tenderExtensionInterest,
								aprLimitRuleCheck.extension.tender_apr);
						coll.setExtensionInterest(userTenderExtensionInterest);
						account = account + userTenderExtensionInterest;
					}
				}

				// 更新collection
				borrowCollectionDao.update(coll);
				itemList.add(coll);

				account = account + coll.getCapital() + coll.getInterest();
				if (coll.getRepayAwardStatus() == 0)
				{
					account = account + coll.getRepayAward();
				}
				if (itemPay != null
						&& itemPay.getStatus() == TppIpsPay.STATUS_WAIT)
				{
					itemPay.setAddTime(new Date());
					itemPay.setAccount(account);
					itemPay.setManageFee(coll.getManageFee());
					tppIpsPayDao.update(itemPay);
				} else
				{
					TppIpsPay pay = new TppIpsPay();
					pay.setType(TppIpsPay.TYPE_DO_COLLECTION);
					pay.setInUserId(inUser.getUserId());
					// TODO zjj
					// pay.setInAcctNo(inUser.getApiId());
					pay.setTransferType(repayType + "");
					pay.setAddTime(new Date());
					pay.setRepaymentId(repay.getId());
					pay.setCollectionId(coll.getId());
					pay.setStatus(TppIpsPay.STATUS_WAIT);
					pay.setAccount(account);
					pay.setManageFee(coll.getManageFee());
					pay.setMerBillNo(merBillNo);
					tppIpsPayDao.save(pay);
				}
			}
			repay.setLateInterest(lateInter);
			return itemList;
		}
		return null;
	}

	public double getInvestRepayInterest(BorrowCollection borrowCollection)
	{
		double cInterest = borrowCollection.getInterest();
		double borrowFee = 0;
		// 归还投资人利息
		if (cInterest > 0)
		{
			double borrow_fee = Global.getDouble("borrow_fee");
			borrowFee = BigDecimalUtil.mul(cInterest, borrow_fee);
			// 扣除投资人利息管理费
			if (borrowFee > 0)
			{
				borrowCollection.setManageFee(borrowFee);
			}
			// 利息管理费优惠返还 VIP （此处代码去掉了，更具业务需求，再加上)
			// 利息管理费优惠返还 会员积分等级 (此处代码去掉了，更具业务需求，再加上)
		}
		return borrowFee;
	}

	@Override
	public BorrowRepayment updateRepay(TppIpsPay pay)
	{
		List<TppIpsPay> payList = tppIpsPayDao.getTppIpsPay(pay.getMerBillNo(),
				TppIpsPay.TYPE_DO_RAPAY);
		if (payList == null || payList.size() <= 0)
		{
			return null;
		}
		BorrowRepayment repay = borrowRepaymentDao.find(payList.get(0)
				.getRepaymentId());
		if (repay.getStatus() == BorrowRepayment.STATUS_YES_REPAY)
		{// 如果待还信息状态为已还，则返回false
			return null;
		}
		// 将还款的状态改为处理中
		borrowRepaymentDao.updateStatus(BorrowRepayment.STATUS_MANAGE_REPAY,
				BorrowRepayment.STATUS_WAIT_REPAY, repay.getId());
		return repay;
	}

	@Override
	public IpsRegisterGuarantor registerGuarantor(Borrow borrow)
	{
		IpsRegisterGuarantor irg = new IpsRegisterGuarantor();
		TPPWay way = TPPFactory.getTPPWay();
		irg = (IpsRegisterGuarantor) way.registerGuarantor(borrow);
		// 环讯资金日志做成
		TppIpsPay pay = new TppIpsPay();
		pay.setInUserId(borrow.getVouchFirm().getUserId());
		// TODO zjj
		// pay.setInAcctNo(borrow.getVouchFirm().getApiId());
		pay.setOutUserId(borrow.getUser().getUserId());
		// pay.setOutAcctNo(borrow.getUser().getApiId());
		pay.setType(TppIpsPay.TYPE_REGISTER_GUARANTOR);
		pay.setMerBillNo(irg.getMerBillNo());
		pay.setAddTime(new Date());
		pay.setBorrowId(borrow.getId());
		pay.setStatus(TppIpsPay.STATUS_WAIT);
		pay.setAccount(StringUtil.toDouble(irg.getProFitAmt()));
		tppIpsPayDao.save(pay);

		return irg;
	}

	@Override
	public boolean doIpsRegisterGuarantor(BorrowModel bm)
	{
		Borrow b = borrowDao.getBorrowByBidNo(bm.getBidNo());
		borrowDao.modifyGuaranteeNo(b.getId(), bm.getGuaranteeNo());
		TppIpsPay pay = tppIpsPayDao.getTppIpsPayByMerBillNo(bm
				.getGuaranteeNo());
		if (pay != null)
		{
			pay.setStatus(TppIpsPay.STATUS_SUCCESS);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			try
			{
				pay.setIpsTime(format.parse(bm.getEndTime()));
			} catch (ParseException e)
			{
				pay.setIpsTime(new Date());
			}
			tppIpsPayDao.update(pay);
		}
		return true;
	}

	@Override
	public void doCompensate(BorrowRepayment repayment)
	{

		BorrowRepayment repay = borrowRepaymentDao.find(repayment.getId());
		if (repay == null
				|| !(repay.getStatus() == BorrowRepayment.STATUS_MANAGE_REPAY || repay
						.getStatus() == BorrowRepayment.STATUS_WAIT_REPAY))
		{
			return;
		}
		TppIpsPay ipsPay = tppIpsPayDao.getTppIpsPayByRepayId(
				repayment.getId(), TppIpsPay.STATUS_SUCCESS);
		if (ipsPay != null)
		{
			return;
		}

		// 当前处理的标的信息
		Borrow borrow = repayment.getBorrow();

		long borrowId = borrow.getId();
		this.data = borrowDao.find(borrowId);
		BorrowWorker worker = BorrowHelper.getWorker(data);
		// 代偿前校验
		worker.validBeforeCompensate(repayment);
		// 计算逾期利息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date nowTime = null;
		Date repaymentTime = null;
		try
		{
			nowTime = format.parse(DateUtil.dateStr2(new Date()));
			repaymentTime = format.parse(DateUtil.dateStr2(repayment
					.getRepaymentTime()));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		long day = nowTime.getTime() - repaymentTime.getTime();
		int days = (int) (day / (24 * 60 * 60 * 1000));
		double lateInterest = 0;
		if (days > 0)
		{
			String overdueFee = Global.getValue("overdue_fee");
			double overdue = Double.parseDouble(overdueFee);
			int FreeOverdueDay = Integer.parseInt(Global
					.getValue("free_overdue_day"));// ？天内免逾期罚息
			if (days <= FreeOverdueDay)
			{
				double captial = repayment.getCapital();
				lateInterest = BigDecimalUtil.mul(BigDecimalUtil.mul(captial
						+ repayment.getInterest(), days), overdue); // 计算出逾期利息
				repayment.setLateDays(days);
				repayment.setLateInterest(lateInterest);
			} else if (days > FreeOverdueDay)
			{
				double capital = borrowRepaymentDao
						.getRemainderCapital(borrowId);
				double interest = borrowRepaymentDao
						.getRemainderInterest(borrowId);
				lateInterest = BigDecimalUtil.mul(
						BigDecimalUtil.mul(capital + interest, days), overdue); // 计算出逾期利息
				repayment.setLateInterest(lateInterest);
			}
			repayment.setLateDays(days);
		}
		repayment.setStatus(BorrowRepayment.STATUS_MANAGE_COMPENSATE);
		// 还款信息更新
		borrowRepaymentDao.update(repayment);
		// 担保公司
		User outUser = borrow.getVouchFirm();

		TppIpsPay tppPay = tppIpsPayDao.getTppIpsPayByRepayId(
				repayment.getId(), TppIpsPay.STATUS_WAIT);
		String merBillNo = "";
		if (tppPay != null)
		{
			merBillNo = tppPay.getMerBillNo();
		} else
		{
			merBillNo = "C" + OrderNoUtils.getSerialNumber();
		}

		if (tppPay == null)
		{
			TppIpsPay pay = new TppIpsPay();
			pay.setType(TppIpsPay.TYPE_DO_COMPENSATE);
			pay.setOutUserId(outUser.getUserId());
			// TODO zjj
			// pay.setOutAcctNo(outUser.getApiId());
			pay.setTransferType(IpsType.TRANSFERTYPE_COMPENSATE);
			pay.setMerBillNo(merBillNo);
			pay.setAddTime(new Date());
			pay.setRepaymentId(repayment.getId());
			pay.setStatus(TppIpsPay.STATUS_WAIT);
			pay.setAccount(BigDecimalUtil.add(repayment.getRepaymentAccount(),
					repayment.getInterest(), repayment.getLateInterest()));
			pay.setManageFee(0);
			tppIpsPayDao.save(pay);
		} else
		{
			tppPay.setAccount(BigDecimalUtil.add(
					repayment.getRepaymentAccount(), repayment.getInterest(),
					repayment.getLateInterest()));
			tppPay.setManageFee(0);
			tppIpsPayDao.update(tppPay);
		}

		IpsTransfer ipsTransfer = new IpsTransfer();

		// 商户订单号
		ipsTransfer.setMerBillNo(merBillNo);
		// 标的号
		ipsTransfer.setBidNo(borrow.getBidNo());
		// 日期
		ipsTransfer.setDate(DateUtil.dateStr7(new Date()));
		// 转账类型（2：代偿）
		ipsTransfer.setTransferType("2");
		// 转账方式（2：批量入账）
		ipsTransfer.setTransferMode("1");
		// 异步返回地址
		ipsTransfer.setS2SUrl(Global.getValue("weburl")
				+ "/public/ips/ipsCompensateNotify.html");

		StringBuffer details = new StringBuffer();
		// 还款待收信息
		List<BorrowCollection> collectionList = this.getCollList(repayment,
				(byte) Integer.parseInt(IpsType.TRANSFERTYPE_COMPENSATE),
				merBillNo);
		// 待收明细处理
		for (int i = 0; i < collectionList.size(); i++)
		{
			BorrowCollection collection = collectionList.get(i);
			IpsTransferDetail row = new IpsTransferDetail();
			// 原商户订单号
			row.setOriMerBillNo(collection.getTender().getTenderBilNo());
			// 转账金额（还款本金+还款利息+逾期利息）
			row.setTrdAmt(XmlTool.format2Str(BigDecimalUtil.add(
					collection.getCapital(), collection.getInterest(),
					collection.getLateInterest())));
			// 转出方账户类型（0# 机构）
			row.setfAcctType("0");
			// 转出方 IPS 托管账户号
			// TODO zjj
			// row.setfIpsAcctNo(outUser.getApiId());
			// 转出方明细手续费
			row.setfTrdFee("0.00");
			// 转入方账户类型（1# 个人）
			row.settAcctType("1");
			// 转入方 IPS 托管 账
			// TODO zjj
			// row.settIpsAcctNo(collection.getUser().getApiId());
			// 转入方明细手续费
			row.settTrdFee("0.00");
			// 追加一条待收明细信息
			details.append(row.createSign(row.getParamNames()));
		}
		// 设置明细信息
		ipsTransfer.setDetails(details.toString());
		// 参数加密
		ipsTransfer.createSign();
		try
		{
			String res = ipsTransfer.doNotifySubmit(ipsTransfer.WS_URL,
					"Transfer");
			String pErrCode = XmlTool.getXmlNodeValue(res, "pErrCode");
			if (!"MG00008F".equals(pErrCode))
			{
				logger.error(XmlTool.getXmlNodeValue(res, "pErrMsg"));
				throw new BussinessException("代偿转账失败", 1);
			}
			ipsTransfer.setErrCode(pErrCode);
		} catch (Exception e)
		{
			throw new BussinessException("代偿转账失败", 1);
		}
	}

	@Override
	public void doCompensateSuccess(TppIpsPay pay)
	{
		Boolean result = tppIpsPayDao.editTppIpsPay(pay, TppIpsPay.STATUS_WAIT);
		if (result && pay.getStatus() == TppIpsPay.STATUS_SUCCESS)
		{
			List<TppIpsPay> payList = tppIpsPayDao.getTppIpsPay(
					pay.getMerBillNo(), TppIpsPay.TYPE_DO_COMPENSATE);
			if (payList == null || payList.size() <= 0)
			{
				return;
			}
			BorrowRepayment borrowRepayment = null;
			try
			{
				borrowRepayment = borrowRepaymentDao.find(payList.get(0)
						.getRepaymentId());
			} catch (BorrowException bx)
			{
				// 该还款计划已经还款
				return;
			}
			borrowRepayment.setMerBillNo(pay.getMerBillNo());
			Boolean check = this.checkRepay(borrowRepayment);
			if (!check)
			{
				return;
			}
			// 代偿交给Disruptor处理
			ConcurrentUtil.doCompensateSuccess(borrowRepayment);
		}
	}
}
