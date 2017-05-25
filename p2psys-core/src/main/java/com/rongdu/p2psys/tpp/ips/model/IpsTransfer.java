package com.rongdu.p2psys.tpp.ips.model;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;

/**
 * 转账的所有业务类型（投资、代偿、代偿还款、债权转让、担保收益）
 * 均调用本接口迚行转账。
 * @author wujing
 *
 */
public class IpsTransfer extends IpsModel {
	
	private String merBillNo; //商户订单号
	
	private String bidNo;    //标的号 
	
	private String date;    //商户日期 格式：YYYYMMDD
	
	/**
	 * 转账类型    1：投资（报文提交关系，转出方：转入方=N：1），
	 *   2：代偿（报文提交关系，转出方：转入方=1：N），
	 *     3：代偿还款（报文提交关系，转出方：转入方=1：1），  
	 *     4：债权转让（报文提交关系，转出方：转入方=1：1）， 
	 *     5：结算担保收益（报文提交关系，转出方：转入方=1：1）
	 */
	private String transferType;  
	
	/**
	 * 转账方式，1：逐笔入账；
	 * 2：批量入账 逐笔入账：丌将转账款项汇总，而是按明细
	 * 交易一笔一笔计入账户 批量入帐：针对投资，
	 * 将明细交易按1笔汇总本金和1笔汇总手续费记入借款人
	 * 帐户 当转账类型为“1：投资”时，可选择1戒2。其余交易只能选1
	 */
	private String transferMode;  
	
	private String  s2SUrl;  // 状态返回地址2
	
	private String memo1;
	
	private String memo2;
	
	private String memo3;
	
	private String details;   //转账明细
	
    /** IPS总手续费 */
    private String ipsFee;
    
    /** IPS还款订单号 */
    private String ipsBillNo;
    /** IPS处理时间 */
    private String ipsTime;    




    private String[] paramNames = new String[]{"MerBillNo","BidNo","Date","TransferType","TransferMode","S2SUrl","Details",
			"Memo1","Memo2","Memo3"};
	
	
	@Override
	public String[] getParamNames() {
		return paramNames;
	}
	
    /**
     * 回调xml解析封装成对象
     * @param xml
     * @return ipsRepayment
    */
    public IpsTransfer doTransferCreate(String xml) {
        IpsTransfer ipsTransfer = new IpsTransfer();
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        ipsTransfer.setMerBillNo(tool.getNodeValue("pMerBillNo"));
        ipsTransfer.setBidNo(tool.getNodeValue("pBidNo"));
        ipsTransfer.setDate(tool.getNodeValue("pDate"));
        ipsTransfer.setTransferType(tool.getNodeValue("pTransferType"));
        ipsTransfer.setTransferMode(tool.getNodeValue("pTransferMode"));
        ipsTransfer.setIpsBillNo(tool.getNodeValue("pIpsBillNo"));
        ipsTransfer.setIpsTime(tool.getNodeValue("pIpsTime"));
        ipsTransfer.setIpsFee("0.00");
        ipsTransfer.setMemo1(tool.getNodeValue("pMemo1"));
        ipsTransfer.setMemo2(tool.getNodeValue("pMemo2"));
        ipsTransfer.setMemo3(tool.getNodeValue("pMemo3"));
        return ipsTransfer;
    }
	
	  
	/**
	 * 返回回來參數
	 *
	 */
	  /**
	   * 由 IPS 系统生成的唯一流水号
	   */
	  private String pIpsBillNo;
	  /**
	   * IPS 处理时间  
	   */
	  private String pIpsTime;
	  
	public String getpIpsBillNo() {
		return pIpsBillNo;
	}

	public void setpIpsBillNo(String pIpsBillNo) {
		this.pIpsBillNo = pIpsBillNo;
	}

	public String getpIpsTime() {
		return pIpsTime;
	}

	public void setpIpsTime(String pIpsTime) {
		this.pIpsTime = pIpsTime;
	}

	@Override
	public XmlTool response(String str) {
		// TODO Auto-generated method stub
		return super.response(str);
	}
	
	

	@Override
	public void createSign() {
		
		
		super.createSign();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	
    public String getIpsFee() {
        return ipsFee;
    }

    public void setIpsFee(String ipsFee) {
        this.ipsFee = ipsFee;
    }
    
    public String getIpsBillNo() {
        return ipsBillNo;
    }

    public void setIpsBillNo(String ipsBillNo) {
        this.ipsBillNo = ipsBillNo;
    }    
    
    public String getIpsTime() {
        return ipsTime;
    }

    public void setIpsTime(String ipsTime) {
        this.ipsTime = ipsTime;
    }
}
