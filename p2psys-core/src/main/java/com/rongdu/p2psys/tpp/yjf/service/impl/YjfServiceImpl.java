package com.rongdu.p2psys.tpp.yjf.service.impl;

/**
 * 此service   没有事物 只是api处理工具类
 * @author zxc
 *
 */
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.domain.YjfPay;
import com.rongdu.p2psys.tpp.yjf.PayModelHelper;
import com.rongdu.p2psys.tpp.yjf.YjfType;
import com.rongdu.p2psys.tpp.yjf.dao.YjfDao;
import com.rongdu.p2psys.tpp.yjf.model.TradeCreatePoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePayPoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePayerApplyPoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePayerQuitPoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePoolReceiveBorrow;
import com.rongdu.p2psys.tpp.yjf.model.TradeTransfer;
import com.rongdu.p2psys.tpp.yjf.service.YjfService;
import com.rongdu.p2psys.user.domain.User;

public class YjfServiceImpl implements YjfService {
	private Map<String, Object> map;
	private List<Object> taskList;
	
	public YjfServiceImpl() {
		
	}
	public YjfServiceImpl(Map<String, Object> map, List<Object> taskList) {
		super();
		this.map = map;
		this.taskList = taskList;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<Object> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Object> taskList) {
		this.taskList = taskList;
	}
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(YjfServiceImpl.class);
	/**
	 * 接口名称
	 */
	private final String tips = Global.getValue("api_name") + "提醒：";
	@Autowired
	private YjfDao yjfDao;

	// 获取第三方接口的类型
	private int getApiType() {
		return Global.getInt("api_code");// 接口类型 1：易极付，...
	}

	@Override
	public void verifyVipSuccess(User cache, double vipFee, List<Object> taskList) {
		switch (getApiType()) {
			case 1:// 易极付接口
				YjfPay yjfPayVip = new YjfPay(null, Global.getValue("trade_name_vip"), vipFee + "",
						YjfType.VERIFYVIPSUCCESS, null, YjfType.TRADE_TRANSFER, null, Global.getValue("yjf_partnerId"),
						null, /* cache.getApiId() */ "");
				taskList.add(yjfPayVip);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean doApiTask(List<Object> taskList) {
		boolean isSuccess = true;
		for (Object objYjfPay : taskList) {
			YjfPay yp = (YjfPay) objYjfPay;
			if (isSuccess) {
				try {
					yp = autoYjfPay(yp); // 获取最新的yp
					yp.setStatus(1);
					yp.setErrormsg("success"); // 处理成功
				} catch (Exception e) {
					yp.setErrormsg(e.getMessage());
					yp.setStatus(2);
					// 一旦处理失败，易极付就不在触发;但是记录还要插入记录表 yjf_pay
					isSuccess = false;
				}
			}
			try {
				yjfDao.save(yp);
			} catch (Exception e) {
				logger.error(e + "保存交易信息出错！！！");
			}
		}
		return isSuccess;
	}

	
	/**
	 * 自动处理
	 * 
	 * @param yjfPay
	 * @return
	 */
	public YjfPay autoYjfPay(YjfPay yjfPay) {
		// status=1 处理完成； status=2 处理失败；status = 0 未处理。

		if ("1".equals(yjfPay.getStatus())) {
			// 过滤 已经完成的
			throw new BussinessException("已经处理完成，请不要重复处理。");
		}
		// 转账
		if (YjfType.TRADE_TRANSFER.equals(yjfPay.getService())) {
			TradeTransfer tt = tradeTransfer(yjfPay.getTouserid(), yjfPay.getUserid(), yjfPay.getMoney(),
					yjfPay.getErrormsg());
			if (tt != null) {
				yjfPay.setOrderno(tt.getOrderNo());
				yjfPay.setTradeno(yjfPay.getTradeno());
				if (!"EXECUTE_SUCCESS".equals(tt.getResultCode())) {
					throw new BussinessException(tips + tt.getResultMessage());
				}
			}
		}
		// 借款产生交易号 并且保存。
		if (YjfType.TRADE_CREATE_POOL_TOGETHER.equals(yjfPay.getService())) {

			TradeCreatePoolTogether td = tradeCreatePoolTogether(yjfPay.getMoney(), yjfPay.getUserid());
			if (td != null) {
				yjfPay.setOrderno(td.getOrderNo()); // 封装交易号、单据号
				yjfPay.setTradeno(td.getTradeNo());
				if (!"EXECUTE_SUCCESS".equals(td.getResultCode())) {
					throw new BussinessException(tips + td.getResultMessage());
				}
			}
		}
		if(YjfType.TRADE_CREATE_POOL.equals(yjfPay.getService())){ // 流转标 创建交易号
        	TradeCreatePoolTogether tcp = tradeCreatePool(yjfPay.getUserid(),yjfPay.getMoney());
            if(tcp!=null){
            	yjfPay.setTradeno(tcp.getTradeNo());
            	yjfPay.setOrderno(tcp.getOrderNo());
            	if(!"EXECUTE_SUCCESS".equals( tcp.getResultCode() )){
            		throw new BussinessException(tips + tcp.getResultMessage());
            	}
			}
        }
        
		
		// 流转标直接划款
		if (YjfType.TRADE_POOL_RECEIVE_BORROW.equals(yjfPay.getService())) {
			// 开始划款
			TradePoolReceiveBorrow tt = tradePoolReceiveBorrow(yjfPay);
			if (tt != null) {
				yjfPay.setOrderno(tt.getOrderNo());
				if (!"EXECUTE_SUCCESS".equals(tt.getResultCode())) {
					throw new BussinessException(tips + tt.getResultMessage());
				}
			}
		}

		// 投标
		if (YjfType.TRADE_PAYER_APPLY_POOL_TOGETHER.equals(yjfPay.getService())) {
			TradePayerApplyPoolTogether tt = tradePayerApplyPoolTogether(yjfPay.getUserid(), yjfPay.getMoney(),
					yjfPay.getTradeno());
			if (tt != null) {
				yjfPay.setSubtradeno(tt.getSubTradeNo());
				yjfPay.setOrderno(tt.getOrderNo());
				if (!"EXECUTE_SUCCESS".equals(tt.getResultCode())) {
					throw new BussinessException(tips + tt.getResultMessage());
				}
			}
		}

		// 退出投标
		if (YjfType.TRADE_PAYER_QUIT_POOL_TOGETHER.equals(yjfPay.getService())) {
			TradePayerQuitPoolTogether tqt = tradePayerQuitPoolTogether(yjfPay.getTradeno(),
					yjfPay.getSubtradeno(), yjfPay.getUserid());
			yjfPay.setOrderno(tqt.getOrderNo());
			yjfPay.setTradeno(tqt.getTradeNo());
			yjfPay.setSubtradeno(tqt.getSubTradeNo());
			if (!"EXECUTE_SUCCESS".equals(tqt.getResultCode())) {
				throw new BussinessException(tips + tqt.getResultMessage());
			}
		}
		// 放款
		if (YjfType.TRADE_PAY_POOL_TOGETHER.equals(yjfPay.getService())) {
			TradePayPoolTogether tpp = tradePayPoolTogether(yjfPay.getTradeno());
			if (tpp != null) {
				yjfPay.setOrderno(tpp.getOrderNo());
				if (!"EXECUTE_SUCCESS".equals(tpp.getResultCode())) {
					throw new BussinessException(tips + tpp.getResultMessage());
				}
			}
		}
		return yjfPay;
	}

	

	

	/**
	 * 转账功能
	 * 
	 * @param sellerUserId 收款人
	 * @param payerUserId 付款人
	 * @param money 金额
	 * @param tradeName 交易名称
	 * @return 交易对象
	 */
	public TradeTransfer tradeTransfer(String sellerUserId, String payerUserId, String money, String tradeName) {
		if (BaseTPPWay.isOpenApi()) {
			TradeTransfer tt = PayModelHelper.tradeTransfer(sellerUserId, payerUserId, money, tradeName);
			return tt;
		} else {
			return null;
		}
	}

	/**
	 * 创建交易号
	 * 
	 * @param borrow
	 * @param user
	 */
	public TradeCreatePoolTogether tradeCreatePoolTogether(String money, String apiId) {
		if (BaseTPPWay.isOpenApi()) {
			TradeCreatePoolTogether ttp = PayModelHelper.tradeCreatePoolTogether(money, apiId);
			return ttp;
		} else {
			return null;
		}
	}

	@Override
	public void addBorrowCreateTradeNo(Borrow model, User uc, List<Object> taskList) {
					// 发标 易极付创建交易号
					// 流标特殊处理
		if (model.getType() == Borrow.TYPE_FLOW) {
			YjfPay yjfPay = new YjfPay(model.getId() + "", null, model.getAccount() + "", YjfType.ADDBORROW, null,
					YjfType.TRADE_CREATE_POOL, null, null, null, /* uc.getApiId() */"");
			taskList.add(yjfPay);
		} else {
			YjfPay yjfPay = new YjfPay(model.getId() + "", null, model.getAccount() + "", YjfType.ADDBORROW, null,
					YjfType.TRADE_CREATE_POOL_TOGETHER, null, null, null, /* uc.getApiId() */"");
			taskList.add(yjfPay);
		}
	}

	@Override
	public void flowBorrowLoan(double money, User tenderUserCache, Borrow borrow, User borrowUser,
			List<Object> taskList) {
			// 查询交易号
			// YjfPay yp = yjfDao.getBorrowTradeNo(borrowUser.getApiId() + "", borrow.getId() + "", 1);
			// String tenderUserStr = tenderUserCache.getApiId() + "=" + money; // 所有投资人列表
			// YjfPay yjfPayFlow = new YjfPay(borrow.getId() + "", null, money + "", YjfType.ADDTENDER, null,
			// 		YjfType.TRADE_POOL_RECEIVE_BORROW, null, borrowUser.getApiId(), yp.getTradeno(), tenderUserStr);
			// taskList.add(yjfPayFlow);
	}

	@Override
	public void addTenderFreezeMoney(double money, User uc, Borrow borrow, User borrowUserCahche,
			List<Object> taskList) {
		switch (getApiType()) {
			case 1:
				String tradeNo = "";
				// 获取易极付交易号 --》每个标都有一个交易号
				/*
				YjfPay yp = yjfDao.getBorrowTradeNo(borrowUserCahche.getApiId() + "", borrow.getId() + "", 1);
				if (yp == null) {
					throw new BorrowException("标： " + borrow.getId() + "  查询交易号失败,请联系客户", 1);
				}
				tradeNo = yp.getTradeno();
				YjfPay yjfPay = new YjfPay(borrow.getId() + "", null, money + "", YjfType.ADDTENDER, null,
						YjfType.TRADE_PAYER_APPLY_POOL_TOGETHER, null, null, tradeNo, uc.getApiId());
				taskList.add(yjfPay);
				 */
				break;
			default:
				break;
		}

	}

	/**
	 * 流标款付款接口 和 给力标一样 只是 传值 不一样 这里直接用 给力标 付款
	 */
	public TradePoolReceiveBorrow tradePoolReceiveBorrow(YjfPay yjfPay) {
		if (BaseTPPWay.isOpenApi()) {
			TradePoolReceiveBorrow tt = PayModelHelper.tradePoolReceiveBorrow(yjfPay);
			return tt;
		} else {
			return null;
		}
	}

	/**
	 * 投标冻结资金
	 * 
	 * @param apiId
	 * @param money
	 * @param tradeNo
	 * @return
	 */
	public TradePayerApplyPoolTogether tradePayerApplyPoolTogether(String apiId, String money, String tradeNo) {
		if (BaseTPPWay.isOpenApi()) {
			TradePayerApplyPoolTogether tpa = PayModelHelper.tradePayerApplyPoolTogether(apiId, money, tradeNo);
			return tpa;
		} else {
			return null;
		}
	}

	@Override
	public void failBorrow(List<Object> taskList, Borrow model) {
		switch (getApiType()) {
			case 1:// 易极付接口
				List<YjfPay> tenders = yjfDao.getTendersPayed(model.getId() + "");
				for (YjfPay yp : tenders) {
					YjfPay yjfPayDeal = new YjfPay(model.getId() + "", null, BigDecimalUtil.round(
							Double.parseDouble(yp.getMoney()), 2)
							+ "", YjfType.FAIL, null, YjfType.TRADE_PAYER_QUIT_POOL_TOGETHER, yp.getSubtradeno(), null,
							yp.getTradeno(), yp.getUserid());
					taskList.add(yjfPayDeal);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 取消投标
	 * 
	 * @param tradeNo
	 * @param subTradeNo
	 * @param payApiId
	 * @return
	 */
	public TradePayerQuitPoolTogether tradePayerQuitPoolTogether(String tradeNo, String subTradeNo, String payApiId) {
		if (BaseTPPWay.isOpenApi()) {
			TradePayerQuitPoolTogether tqt = PayModelHelper.tradePayerQuitPoolTogether(tradeNo, subTradeNo, payApiId);
			return tqt;
		} else {
			return null;
		}
	}

	@Override
	public void fullSuccessLoanMoney(String apiNo, Borrow model, BorrowTender tender, User borrowUser,
			User tenderUser, List<Object> taskList) {
		switch (getApiType()) {
			case 1:
				// 添加易极付放款任务
				// 查询交易号
				/*
				YjfPay yp = yjfDao.getBorrowTradeNo(borrowUser.getApiId() + "", model.getId() + "", 1);
				YjfPay yjPay = new YjfPay(model.getId() + "", null, BigDecimalUtil.round(model.getAccount(), 2) + "",
						YjfType.AUTOVERIFYFULLSUCCESS, null, YjfType.TRADE_PAY_POOL_TOGETHER, null, null,
						yp.getTradeno(), null);
				taskList.add(yjPay);
				logger.info("添加易极付任务成功： borrowId:" + model.getId());
				*/
				break;

			default:
				break;
		}
	}

	/**
	 * 放款操作
	 * 
	 * @param tradeNo
	 * @param borrowTenderList
	 * @param to_apiId
	 * @return
	 */
	public TradePayPoolTogether tradePayPoolTogether(String tradeNo) { // 放款
		if (BaseTPPWay.isOpenApi()) {
			TradePayPoolTogether tpt = PayModelHelper.tradePayPoolTogether(tradeNo);
			return tpt;
		} else {
			return null;
		}
	}

	@Override
	public void fullSuccessAward(Borrow model, BorrowTender tender, User borrowUser, User tenderUser,
			List<Object> taskList, double awardValue) {
		switch (getApiType()) {
			case 1:// 易极付
				/*
				YjfPay yjfPayawArdValue = new YjfPay(model.getId() + "", Global.getValue("trade_name_award"),
						BigDecimalUtil.round(awardValue, 2) + "", YjfType.AUTOVERIFYFULLSUCCESS, null,
						YjfType.TRADE_TRANSFER, null, tenderUser.getApiId(), null, borrowUser.getApiId());
				taskList.add(yjfPayawArdValue);
				*/
				break;
			default:
				break;
		}

	}

	@Override
	public void fullSuccessDeductFee(List<Object> taskList, double borrowfee, Borrow borrow, User uc) {
		switch (getApiType()) {
			case 1:// 易极付
					// 转账给 参与账户 当做风险基金池
				/*
				YjfPay feepayChargeFee = new YjfPay(borrow.getId() + "", Global.getValue("trade_name_manage_fee"),
						BigDecimalUtil.round(borrowfee, 2) + "", YjfType.AUTOVERIFYFULLSUCCESS, null,
						YjfType.TRADE_TRANSFER, null, Global.getValue("yjf_partnerId"), null, uc.getApiId());
				taskList.add(feepayChargeFee);
				*/
				break;
			default:
				break;
		}
	}
	/**
	 * 还款，还<本金等>接口
	 */
	@Override
	public void repayLoanMoney(String apiId,double money,List<Object> taskList,Borrow borrow,BorrowCollection c,String apiMethodType){
		if(BaseTPPWay.isOpenApi()){
			switch (getApiType()) {
				case 1://易极付接口
					//平台获得一般逾期的利息
					YjfPay  yjfPay = new YjfPay(borrow.getId()+"", null, BigDecimalUtil.round(money, 2)+"", 
							YjfType.AUTOREPAY, null, null, YjfType.TRADE_CLOSE_POOL_REVERSE, 
							null, apiId, null);
					taskList.add(yjfPay);
					break;
			}
		}
	}
	
	/**
	 * 还款，逾期罚息接口
	 */
	@Override
	public void payManageFee(String apiId, double money,List<Object> taskList,Borrow borrow){
		switch (getApiType()) {
			case 1://易极付接口
				//平台获得一般逾期的利息
				YjfPay  yjfPay = new YjfPay(borrow.getId()+"", null, BigDecimalUtil.round(money, 2)+"", 
			    		YjfType.AUTOREPAY, null, null, YjfType.TRADE_CLOSE_POOL_REVERSE, 
			    		null, apiId, null);
			    taskList.add(yjfPay);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 还款，易极付利息管理费，汇付，还利息与扣除利息管理费。
	 */
	@Override
	public void repayBorrowFee(Borrow model,BorrowRepayment repay ,User tenderUser,double borrowFee,double interest
			,List<Object> taskList, BorrowCollection c, String apiMethodType){
		boolean isDo = apiMethodType.contains(getApiType()+"");
		User borrowUser = model.getUser();
		switch (getApiType()) {
			case 1://易极付接口
				if(!isDo)return;
				//利息管理费 易极付转账给 平台账户。
				/*
				if(borrowFee != 0){
					YjfPay  yjfManage = new YjfPay(model.getId()+"", Global.getValue("trade_name_interest_fee"), BigDecimalUtil.round(borrowFee, 2) +"", YjfType.DOREPAY, 
							null, repay.getPeriod()+"", YjfType.TRADE_TRANSFER, null, 
							Global.getValue("yjf_partnerId"), tenderUser.getApiId());
					taskList.add(yjfManage); // 扣除管理费，要放到还款的后面
				}
				*/
				break;
			default:
				break;
		}
	}
	/**
	 * 流转标
	 * 集资创建交易接口和普通标创建一样只是参数有变化  那就用同一个吧   大家写的时候要注意
	 * 
	 */
	public  TradeCreatePoolTogether tradeCreatePool(String apiId,String money){
		if(BaseTPPWay.isOpenApi()){
			TradeCreatePoolTogether  tpt = PayModelHelper.tradeCreatePool(apiId, money);
			return tpt;
		}else{
			return null;
		}
	}

	@Override
	public boolean doTender(double money, User uc, Borrow borrow, User toUc) {
		if(borrow.getType() == Borrow.TYPE_FLOW){
			//apiService.flowBorrowLoan(money, uc, borrow, toUc, taskList);
		} else {
			//apiService.addTenderFreezeMoney(money, uc, borrow, toUc, taskList);	
		}
		return false;
	}
	
}
