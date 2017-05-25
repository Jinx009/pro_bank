package com.rongdu.p2psys.tpp.chinapnr.tool;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCashReconciliation;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrLoans;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrQueryAccts;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrQueryBalance;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrQueryTransStat;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrReconciliation;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrRepayment;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrSaveReconciliation;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrTransfer;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrTrfReconciliation;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrUsrFreezeBg;

public class ChinapnrHelper {
	private static final Logger logger = Logger.getLogger(ChinapnrHelper.class);

	private static ChinapnrModel doSubmit(ChinapnrModel mod) {
		try {
			mod.submit();
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return mod;
	}
	/**
	 * 汇付2.0中冻结接口
	 * */ 
	public static ChinapnrModel usrFreezeBg(String usrid, String tranAmt,
			String ordId, String date) {
		ChinapnrUsrFreezeBg mod = new ChinapnrUsrFreezeBg(usrid, tranAmt, ordId, date);
		return doSubmit(mod);
	}

	/**
	 * 用户满标放款接口 outCustId:出账账户,inUustId:入账账户,transAmt:交易金额,fee:费率,userid:用户id
	 * 是否解冻 isUnFreeze, 解冻订单号 unFreezeOrdId, 冻结订单号 freezeTrxId
	 * */
	public static ChinapnrModel loans(String outCustId, 
			String inCnstId,
			String transAmt, 
			String fee,
			String riskReserveFee,
			String ordId,
			String ordDate, 
			String subOrdId, 
			String subOrdDate, 
			String isdefault,
			String args, 
			String isUnFreeze, 
			String unFreezeOrdId, 
			String freezeTrxId) {

		ChinapnrLoans loan = new ChinapnrLoans(outCustId, inCnstId, transAmt, ordId, ordDate,
				subOrdId, subOrdDate, fee ,riskReserveFee);
		loan.setIsUnFreeze(isUnFreeze);
		loan.setUnFreezeOrdId(unFreezeOrdId);
		loan.setFreezeTrxId(freezeTrxId);
		loan.setIsDefault(isdefault);
		return doSubmit(loan);
	}
	
	/**
	 * 自动扣款(还款接口)
	 * outCustId:出账账户,inCnstId:进账账户,transAmt:交易金额,
	 * chinapnr_manageid:金账户在汇付平台上的一个账号，在1.0版本中无此参数
	 * MDT000023类似这中格式
	 * */
	public static ChinapnrModel repayment(String outCustId, String inCustId,
			String transAmt, String fee,String ordId,
			String ordDate, String subOrdId, String subOrdDate,String args){

		ChinapnrRepayment repay=new ChinapnrRepayment(outCustId,inCustId,transAmt,ordId,ordDate, subOrdId, subOrdDate,fee);
		return doSubmit(repay);
	}	
	
	/**
	 * 汇付2.0中后台划账接口，仅限平台划账给用户
	 * tranAmt:划账金额，inCustId:划账给用户接收方的用户好
	 * ordid：订单号
	 * */
	public static ChinapnrModel transfer(String transAmt,String inCustId,String ordId,String inAcctId,String outAcctId){
		ChinapnrTransfer tran=new ChinapnrTransfer(transAmt, inCustId, ordId,outAcctId,inAcctId);
		return doSubmit(tran);
	}	

	/**
	 * 汇付2.0用户查询用户资金
	 * @param usrCustId
	 * @return
	 */
	public static ChinapnrModel queryBalanceBg(String usrCustId){
		ChinapnrQueryBalance querybalance=new ChinapnrQueryBalance(usrCustId);
		return doSubmit(querybalance);
	}
	
	/**
	 * 商户子账户信息查询
	 * @return
	 */
	public static ChinapnrModel queryAcctschin(){
		ChinapnrQueryAccts qu=new ChinapnrQueryAccts();
		return doSubmit(qu);
	}
	
	/**
	 * 交易状态查询
	 * @param ordId
	 * @param ordDate
	 * @param querytransType
	 * @return
	 */
	public static ChinapnrModel queryTransStat(String ordId,String ordDate,String querytransType){
		ChinapnrQueryTransStat query=new ChinapnrQueryTransStat(ordId, ordDate, querytransType);
		return doSubmit(query);
	}
	
	/**
	 * 商户扣款对账
	 * @param beginDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public static ChinapnrModel trfReconciliation(String beginDate,String endDate,String pageNum,String pageSize){
		ChinapnrTrfReconciliation trf=new ChinapnrTrfReconciliation(beginDate, endDate, pageNum, pageSize);
		return doSubmit(trf);
	}
	
	/**
	 * 放还款对账（投标对账）
	 * @param beginDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @param queryTransType
	 * @return
	 */
	public static ChinapnrModel reconciliationchin(String beginDate,String endDate,String pageNum,String pageSize,String queryTransType){
		ChinapnrReconciliation re=new ChinapnrReconciliation(beginDate, endDate, pageNum, pageSize, queryTransType);
		return doSubmit(re);
	}
	
	/**
	 * 取现对账
	 * @param beginDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public static ChinapnrModel cashReconciliationchin(String beginDate,String endDate,String pageNum,String pageSize){
		 ChinapnrCashReconciliation ca=new ChinapnrCashReconciliation(beginDate, endDate, pageNum, pageSize);
		 return doSubmit(ca);
	}
	
	/**
	 * 充值对账
	 * @param beginDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public static ChinapnrModel saveReconchin(String beginDate,String endDate,String pageNum,String pageSize){
		ChinapnrSaveReconciliation save=new ChinapnrSaveReconciliation(beginDate, endDate, pageNum, pageSize);
		return doSubmit(save);
	}
}
