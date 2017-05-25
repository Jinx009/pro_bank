package com.rongdu.p2psys.tpp.ips.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.domain.TppIpsConfig;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsConfigDao;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.user.domain.User;


/**
 * 环迅：还款处理Model
 * @author zhangyz
 */
public class IpsRepayment extends IpsModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /** 还款类型：手动还款 */
    public static final byte REPAY_HAND = 1;
    /** 还款类型：自动还款 */
    public static final byte REPAY_AUTO = 2;
    

    /** 标号 */
    private String bidNo;
    
    /** 还款日期 */
    private String repaymentDate;
    
    /** 商户还款订单号 */
    private String merBillNo;
    
    /** IPS还款订单号 */
    private String ipsBillNo;
    
    /** IPS受理日期 */
    private String ipsDate;
    
    /** 转出方编号 */
    private String outTenderNo;
    
    /** 还款类型：1手动，2自动 */
    private String repayType;
    
    /** 授权号 */
    private String ipsAuthNo;
    
    /** 转出方IPS授权账号 */
    private String outAcctNo;
    
    /** 转出金额 */
    private String outAmt;
    
    /** 转出方总手续费 */
    private String outFee;
    
    /** 转出方手续费 */
    private String outIpsFee;

    /** 状态返回地址1 */
    private String webUrl;

    /** 状态返回地址2 */
    private String s2SUrl;
    
    private String memo1;
    
    private String memo2;
    
    private String memo3;

    /** 转入方明细 */
    private String details;

    /** 输入参数 */
    private String[] paramNames = new String[]{"MerBillNo","BidNo","RepaymentDate","RepayType",
                                               "IpsAuthNo","OutAcctNo","OutAmt","OutFee","Details",
                                               "WebUrl","S2SUrl","Memo1","Memo2","Memo3",};
    
    /**
     * 回调xml解析封装成对象
     * @param xml
     * @return ipsRepayment
    */
    public IpsRepayment doReturnCreate(String xml) {
        IpsRepayment ipsRepayment = new IpsRepayment();
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        ipsRepayment.setBidNo(tool.getNodeValue("pBidNo"));
        ipsRepayment.setRepaymentDate(tool.getNodeValue("pRepaymentDate"));
        ipsRepayment.setMerBillNo(tool.getNodeValue("pMerBillNo"));
        ipsRepayment.setIpsBillNo(tool.getNodeValue("pIpsBillNo"));
        ipsRepayment.setIpsDate(tool.getNodeValue("pIpsDate"));
        ipsRepayment.setOutTenderNo(tool.getNodeValue("pOutTenderNo"));
        ipsRepayment.setOutAcctNo(tool.getNodeValue("pOutAcctNo"));
        ipsRepayment.setOutAmt(tool.getNodeValue("pOutAmt"));
        ipsRepayment.setOutFee(tool.getNodeValue("pOutFee"));
        ipsRepayment.setOutIpsFee(tool.getNodeValue("pOutIpsFee"));
        ipsRepayment.setMemo1(tool.getNodeValue("pMemo1"));
        ipsRepayment.setMemo2(tool.getNodeValue("pMemo2"));
        ipsRepayment.setMemo3(tool.getNodeValue("pMemo3"));
        ipsRepayment.setDetails(tool.getNodeValue("pDetails"));
        return ipsRepayment;
    } 
    
    public IpsRepayment() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 环迅还款接口：有参构造方法
     * @param repay 还款信息
     * @param collList 待收信息
     * @param repayType 还款类型：1手动，2自动
     */
	public IpsRepayment(BorrowRepayment repay, List<BorrowCollection> collList,
			byte repayType) {
		super();
		User user = repay.getUser();
		this.setBidNo(repay.getBorrow().getBidNo());
		this.setRepaymentDate(DateUtil.dateStr7(new Date()));
		// this.setMerBillNo("R"+OrderNoUtils.getSerialNumber());
		this.setRepayType(repayType + "");
		if (repayType == IpsRepayment.REPAY_AUTO) {
			TppIpsConfigDao tppIpsConfigDao = (TppIpsConfigDao) BeanUtil
					.getBean("tppIpsConfigDao");
			TppIpsConfig config = tppIpsConfigDao.findObjByProperty("userId",
					user.getUserId());
			this.setIpsAuthNo(config.getAutoRepayNo());// 自动，IPS支付授权号不为空
		}
		// TODO zjj
		// this.setOutAcctNo(user.getApiId());
		this.setOutAcctNo(String.valueOf(user.getUserId()));

		String webUrl = Global.getValue("weburl");
		String webs2surl = Global.getValue("webs2surl");
		this.setWebUrl(webUrl + "/public/ips/ipsDoRepaymentReturn.html");
		this.setS2SUrl(webs2surl + "/public/ips/ipsDoRepaymentNotify.html");
		this.setSubmitUrl(this.getSubmitUrl()
				+ "/CreditWeb/RepaymentNewTrade.aspx");
		double outAmt = 0;
		StringBuffer details = new StringBuffer();
		for (BorrowCollection coll : collList) {
			User inUser = coll.getUser();
			BorrowTender tender = coll.getTender();
			IpsCollection ipsC = new IpsCollection();
			ipsC.setCreMerBillNo(tender.getTenderBilNo());
			// TODO zjj
			// ipsC.setInAcctNo(inUser.getApiId());
			ipsC.setInAcctNo(String.valueOf(inUser.getUserId()));
			ipsC.setInFee(XmlTool.format2Str(coll.getManageFee()));
			ipsC.setOutInfoFee(XmlTool.format2Str(0));
			double inAmt = coll.getCapital() + coll.getInterest();
			if (coll.getRepayAwardStatus() == 0) {
				inAmt = inAmt + coll.getRepayAwardStatus();
			}
			inAmt = inAmt + coll.getLateInterest()
					+ coll.getExtensionInterest();
			ipsC.setInAmt(XmlTool.format2Str(inAmt));
			outAmt = outAmt + inAmt;
			details.append(ipsC.createSign(ipsC.getParamNames()));
		}
		this.setOutFee(XmlTool.format2Str(0));
		// outAmt = repay.getCapital()+ repay.getInterest() +
		// repay.getLateInterest() + repay.getExtensionInterest();
		this.setDetails(details.toString());
		this.setOutAmt(XmlTool.format2Str(outAmt));
	}

    public String getIpsBillNo() {
        return ipsBillNo;
    }

    public void setIpsBillNo(String ipsBillNo) {
        this.ipsBillNo = ipsBillNo;
    }

    public String getMerBillNo() {
        return merBillNo;
    }

    public void setMerBillNo(String merBillNo) {
        this.merBillNo = merBillNo;
    }

    public String getBidNo() {
        return bidNo;
    }

    public void setBidNo(String bidNo) {
        this.bidNo = bidNo;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getIpsAuthNo() {
        return ipsAuthNo;
    }

    public void setIpsAuthNo(String ipsAuthNo) {
        this.ipsAuthNo = ipsAuthNo;
    }

    public String getOutAcctNo() {
        return outAcctNo;
    }

    public void setOutAcctNo(String outAcctNo) {
        this.outAcctNo = outAcctNo;
    }

    public String getOutAmt() {
        return outAmt;
    }

    public void setOutAmt(String outAmt) {
        this.outAmt = outAmt;
    }

    public String getOutFee() {
        return outFee;
    }

    public void setOutFee(String outFee) {
        this.outFee = outFee;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getS2SUrl() {
        return s2SUrl;
    }

    public void setS2SUrl(String s2sUrl) {
        s2SUrl = s2sUrl;
    }

    public String getMemo1() {
        return memo1;
    }

    public void setMemo1(String memo1) {
        this.memo1 = memo1;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public String getMemo3() {
        return memo3;
    }

    public void setMemo3(String memo3) {
        this.memo3 = memo3;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public String getIpsDate() {
        return ipsDate;
    }

    public void setIpsDate(String ipsDate) {
        this.ipsDate = ipsDate;
    }

    public String getOutTenderNo() {
        return outTenderNo;
    }

    public void setOutTenderNo(String outTenderNo) {
        this.outTenderNo = outTenderNo;
    }

    public String getOutIpsFee() {
        return outIpsFee;
    }

    public void setOutIpsFee(String outIpsFee) {
        this.outIpsFee = outIpsFee;
    }
}
