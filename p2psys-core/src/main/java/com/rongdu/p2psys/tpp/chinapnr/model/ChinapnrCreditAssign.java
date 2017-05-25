package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

import chinapnr.SecureLink;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;

/**
 * 债权转让实体类
 * @author sj
 * @since 2014年12月31日09:19:58
 *
 */
public class ChinapnrCreditAssign  extends ChinapnrModel {
	
	private static final Logger logger = Logger.getLogger(ChinapnrCreditAssign.class);
	
	public ChinapnrCreditAssign() {
		super();
		this.setVersion(super.getVersion());
		this.setCmdId("CreditAssign");
		this.setMerCustId(super.getMerCustId());
		this.setRetUrl(Global.getValue("weburl") + "/public/chinapnr/creditAssignReturn.html");
		this.setBgRetUrl(Global.getValue("weburl") + "/public/chinapnr/creditAssignNotify.html");
	}
	
	private String[] paramNames = new String[]{
			"version","cmdId","merCustId","sellCustId","creditAmt",
			"creditDealAmt","bidDetails","fee","divDetails","buyCustId",
			"ordId","ordDate","retUrl","bgRetUrl","merPriv","reqExt"
	};
	
	
	public void fieldBidDetails(String[][] args){
		StringBuffer sb = new StringBuffer();
	    sb.append("{\"BidDetails\":" + "[{");
        sb.append("\"BidOrdId\":\"" + args[0][0] + "\",");
        sb.append("\"BidOrdDate\":\"" + args[0][1] + "\",");
        sb.append("\"BidCreditAmt\":\"" + args[0][3] + "\",");
        sb.append("\"BorrowerDetails\":" + "[{" + "\"BorrowerCustId\":\"" + args[0][2] + "\","
        		+ "\"BorrowerCreditAmt\":\"" + args[0][3] + "\","
        		+ "\"PrinAmt\":\"" + new DecimalFormat("0.00").format(0) + "\"}]");
	    sb.append("}]}");
	    this.setBidDetails(sb.toString());
	 }
	
	public void fieldDivDetails(String[][] args){
		StringBuffer sb = new StringBuffer();
        boolean first = true;
        sb.append("[");
        for (int i = 0; i < args.length; i++) {
           String[] blogItem = args[i];
           if (!first) {
               sb.append(",");
           }
           sb.append("{");
           sb.append("\"DivAcctId\":\"" + blogItem[0] + "\",");
           sb.append("\"DivAmt\":\"" + blogItem[1] +"\"");
           sb.append("}");
           first = false;
       }
       sb.append("]");
       this.setDivDetails(sb.toString());
	}
	
	public int callback(){
		logger.info("进入债权转让投标回调验证");
		String merKeyFile = createPubKeyFile();
		SecureLink sl = new SecureLink( ) ;
		logger.info("Chinapnr callback:"+this.getCallbackMerData().toString());
		logger.info("pubKeyFile:"+merKeyFile);
		logger.info("CallbackMerData:"+this.getCallbackMerData().toString());
		logger.info("getChkValue:"+getChkValue());
		int ret = sl.VeriSignMsg(merKeyFile , getCallbackMerData().toString(), getChkValue());
		logger.info("债权转让投标ret" + ret);
		return ret;
	}
	
	public StringBuffer getMerData() throws UnsupportedEncodingException{
		StringBuffer MerData = super.getMerData();
		            MerData.append(getSellCustId())
		            .append(getCreditAmt())
		            .append(getCreditDealAmt())
		            .append(getBidDetails())
		            .append(getFee())
		            .append(getDivDetails())
		            .append(getBuyCustId())
		            .append(getOrdId())
		            .append(getOrdDate())
		            .append(getRetUrl())
		            .append(getBgRetUrl())
		            .append(getMerPriv())
		            .append(getReqExt());
		return MerData;	
	}
	
	 //用户债权转让投标回调参数拼接
	 @Override
	 public StringBuffer getCallbackMerData() {
		 StringBuffer merData = new StringBuffer();
		 try {
			merData.append(StringUtil.isNull(getCmdId()))
			 		.append(StringUtil.isNull(getRespCode()))
			 		.append(StringUtil.isNull(getMerCustId()))
			 		.append(StringUtil.isNull(getSellCustId()))
			 		.append(StringUtil.isNull(getCreditAmt()))
			 		.append(StringUtil.isNull(getCreditDealAmt()))
	 				.append(StringUtil.isNull(getFee()))
					.append(StringUtil.isNull(getBuyCustId()))
					.append(StringUtil.isNull(getOrdId()))
			 		.append(StringUtil.isNull(getOrdDate()))
			 		.append(URLDecoder.decode(StringUtil.isNull(getRetUrl()),"utf-8"))
			 		.append(URLDecoder.decode(StringUtil.isNull(getBgRetUrl()),"utf-8"))
			 		.append(URLDecoder.decode(StringUtil.isNull(getMerPriv()),"utf-8"))
			 		.append(StringUtil.isNull(getRespExt()));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		}
	    logger.info("用户投标回调参数拼接" + merData.toString());
		return merData;
	}

	/**
	 * 转让人客户号
	 */
	private String sellCustId;
	
	/**
	 * 转让金额
	 */
	private String creditAmt;
	
	/**
	 * 承接金额
	 */
	private String creditDealAmt;
	
	/**
	 * 债权转让明细
	 */
	private String bidDetails;
	
	/**
	 * 被转让的投标订单号
	 */
	private String bidOrdId;
	
	/**
	 * 被转让的投标订单日期
	 */
	private String bidOrdDate;
	
	/**
	 * 转让金额
	 */
	private String bidCreditAmt;
	
	/**
	 * 借款人客户号
	 */
	private String borrowerCustId;
	
	/**
	 * 明细转让金额
	 */
	private String borrowerCreditAmt;
	
	/**
	 * 已还款金额
	 */
	private String prinAmt;
	
	/**
	 * 项目ID
	 */
	private String proId;
	
	/**
	 * 扣款手续费
	 */
	private String fee;
	
	/**
	 * 分账账户号
	 */
	private String divAcctId;
	
	/**
	 * 分账金额
	 */
	private String divAmt;
	
	/**
	 * 承接人客户号
	 */
	private String buyCustId;
	
	/**
	 * 返参扩展域
	 */
	private String respExt;

	public String getSellCustId() {
		return sellCustId;
	}

	public void setSellCustId(String sellCustId) {
		this.sellCustId = sellCustId;
	}

	public String getCreditAmt() {
		return creditAmt;
	}

	public void setCreditAmt(String creditAmt) {
		this.creditAmt = creditAmt;
	}

	public String getCreditDealAmt() {
		return creditDealAmt;
	}

	public void setCreditDealAmt(String creditDealAmt) {
		this.creditDealAmt = creditDealAmt;
	}

	public String getBidDetails() {
		return bidDetails;
	}

	public void setBidDetails(String bidDetails) {
		this.bidDetails = bidDetails;
	}

	public String getBidOrdId() {
		return bidOrdId;
	}

	public void setBidOrdId(String bidOrdId) {
		this.bidOrdId = bidOrdId;
	}

	public String getBidOrdDate() {
		return bidOrdDate;
	}

	public void setBidOrdDate(String bidOrdDate) {
		this.bidOrdDate = bidOrdDate;
	}

	public String getBidCreditAmt() {
		return bidCreditAmt;
	}

	public void setBidCreditAmt(String bidCreditAmt) {
		this.bidCreditAmt = bidCreditAmt;
	}

	public String getBorrowerCustId() {
		return borrowerCustId;
	}

	public void setBorrowerCustId(String borrowerCustId) {
		this.borrowerCustId = borrowerCustId;
	}

	public String getBorrowerCreditAmt() {
		return borrowerCreditAmt;
	}

	public void setBorrowerCreditAmt(String borrowerCreditAmt) {
		this.borrowerCreditAmt = borrowerCreditAmt;
	}

	public String getPrinAmt() {
		return prinAmt;
	}

	public void setPrinAmt(String prinAmt) {
		this.prinAmt = prinAmt;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getDivAcctId() {
		return divAcctId;
	}

	public void setDivAcctId(String divAcctId) {
		this.divAcctId = divAcctId;
	}

	public String getDivAmt() {
		return divAmt;
	}

	public void setDivAmt(String divAmt) {
		this.divAmt = divAmt;
	}

	public String getBuyCustId() {
		return buyCustId;
	}

	public void setBuyCustId(String buyCustId) {
		this.buyCustId = buyCustId;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getRespExt() {
		return respExt;
	}

	public void setRespExt(String respExt) {
		this.respExt = respExt;
	}
	
}
