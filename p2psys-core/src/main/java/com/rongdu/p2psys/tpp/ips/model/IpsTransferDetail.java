package com.rongdu.p2psys.tpp.ips.model;


/**
 * 转账的所有业务类型（投资、代偿、代偿还款、债权转让、担保收益）
 * 均调用本接口迚行转账。
 * @author wujing
 *
 */
public class IpsTransferDetail extends IpsModel {
	
	
	
	
	
		private String oriMerBillNo;  //原商户订单号
		private String trdAmt;  //转账金额  :0.00
		
		private String fAcctType;  //转出方账户类型  :  0#机构；1#个人 
		
		private String fIpsAcctNo;  //转出方IPS 托管 账户号
		
		private String fTrdFee;  //转出方明细手续 费
		
		private String  tAcctType;  //转入方账户类型
		
		private String tIpsAcctNo; //转入方IPS 托管 账户号 
		
		private String tTrdFee;  //转入方明细手续 费

		private String[] paramNames=new String[]{"OriMerBillNo","TrdAmt","FAcctType","FIpsAcctNo","TAcctType","TIpsAcctNo","TTrdFee","FTrdFee"};
		
		
		public String[] getParamNames() {
			return paramNames;
		}

		public void setParamNames(String[] paramNames) {
			this.paramNames = paramNames;
		}

		public String getOriMerBillNo() {
			return oriMerBillNo;
		}

		public void setOriMerBillNo(String oriMerBillNo) {
			this.oriMerBillNo = oriMerBillNo;
		}

		public String getTrdAmt() {
			return trdAmt;
		}

		public void setTrdAmt(String trdAmt) {
			this.trdAmt = trdAmt;
		}

		public String getFAcctType() {
			return fAcctType;
		}

		public void setfAcctType(String fAcctType) {
			this.fAcctType = fAcctType;
		}

		public String getFIpsAcctNo() {
			return fIpsAcctNo;
		}

		public void setfIpsAcctNo(String fIpsAcctNo) {
			this.fIpsAcctNo = fIpsAcctNo;
		}

		public String getFTrdFee() {
			return fTrdFee;
		}

		public void setfTrdFee(String fTrdFee) {
			this.fTrdFee = fTrdFee;
		}

		

		public void settAcctType(String tAcctType) {
			this.tAcctType = tAcctType;
		}

		public String getTIpsAcctNo() {
			return tIpsAcctNo;
		}

		public void settIpsAcctNo(String tIpsAcctNo) {
			this.tIpsAcctNo = tIpsAcctNo;
		}

		public String getTTrdFee() {
			return tTrdFee;
		}

		public void settTrdFee(String tTrdFee) {
			this.tTrdFee = tTrdFee;
		}

		public String getTAcctType() {
			return tAcctType;
		}
		
		
	
	

}
