package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import chinapnr.SecureLink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 汇付企业开户接口
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月29日
 */
public class ChinapnrCorpRegister extends ChinapnrModel {
	/**
	 * 用户开户，现在统一使用页面开户模式
	 * */
	private static final Logger logger = Logger
			.getLogger(ChinapnrCorpRegister.class);
	/**
	 * 用户名
	 */
	private String usrId;
	/**
	 * 1. 填写企业用户的全称 2. 可在请求参数中传入，也可在汇付页面填写
	 */
	private String usrName;
	/**
	 * 组织机构代码
	 */
	private String instuCode;
	/**
	 * 营业执照编号 1. 必填，作为商户下企业用户的唯一标识 2. 可使用该字段作为查询企业用户开户状态 3.
	 * 如果审核未通过或需要分多次提交图片等，可再次传入该字段，页面会显示审核拒绝的原因，并回显已经填写过的信息。
	 */
	private String busiCode; // 营业执照编号
	/**
	 * 税务登记号(可在请求参数中传入，也可在汇付页面填写)
	 */
	private String taxCode; // 税务登记号
	/**
	 * 1. 此参数用户限定该企业用户是否为担保用户的角色 2. 如不传，则默认“否” 3. 格式为：Y - 是；N - 否
	 */
	private String guarType;

	// 返回参数
	/**
	 * 审核过程中的状态： I： 初始 T：提交 P：审核中 R：审核拒绝 F：开户失败 K： 开户中 Y：开户成功
	 */
	private String auditStat;

	private String auditDesc;

	/**
	 * 开户银行代号
	 */
	private String openBankId;

	/**
	 * 开户银行账号
	 */
	private String cardId;

	public ChinapnrCorpRegister() {
		super();
		this.setCmdId("CorpRegister");
		this.setBgRetUrl(TPPWay.URL_WEBS2S + "/public/chinapnr/corpRegisterNotify.html");

	}

	private String[] paramNames = new String[] { "version", "cmdId",
			"merCustId", "usrId", "usrName", "instuCode", "busiCode",
			"taxCode", "merPriv", "guarType", "bgRetUrl", "chkValue" };

	public StringBuffer getMerData() throws UnsupportedEncodingException {
		StringBuffer MerData = super.getMerData();
		MerData.append(StringUtil.isNull(this.getUsrId()))
				.append(StringUtil.isNull(this.getUsrName()))
				.append(StringUtil.isNull(this.getInstuCode()))
				.append(StringUtil.isNull(this.getBusiCode()))
				.append(StringUtil.isNull(this.getTaxCode()))
				.append(StringUtil.isNull(this.getMerPriv()))
				.append(StringUtil.isNull(this.getGuarType()))
				.append(StringUtil.isNull(this.getBgRetUrl()));
		return MerData;
	}

	@Override
	public ChinapnrRegister response(String res) throws IOException {
		logger.info(res);
		try {
			JSONObject json = JSON.parseObject(res);
			setCmdId(json.get("CmdId").toString());
			setRespCode(json.get("RespCode").toString());
			setRespDesc(json.getString("RespDesc"));
			setMerCustId(json.get("MerCustId").toString());
			setUsrCustId(json.get("UsrCustId").toString());
			setUsrId(json.getString("UsrId"));
			setMerPriv(json.getString("MerPrk"));
		} catch (Exception e) {
			logger.info("企业开户汇付账号获取回调参数json转换出错" + e.getMessage());
		}
		return null;
	}

	@Override
	public StringBuffer getCallbackMerData() {
		StringBuffer merData = new StringBuffer();
		try {
			merData.append(StringUtil.isNull(getCmdId()))
					.append(StringUtil.isNull(getRespCode()))
					.append(StringUtil.isNull(getMerCustId()))
					.append(StringUtil.isNull(getUsrId()))
					.append(StringUtil.isNull(getUsrName()))
					.append(StringUtil.isNull(getUsrCustId()))
					.append(StringUtil.isNull(getAuditStat()))
					.append(StringUtil.isNull(getTrxId()))
					.append(StringUtil.isNull(getOpenBankId()))
					.append(StringUtil.isNull(getCardId()))
					.append(URLDecoder.decode(StringUtil.isNull(getBgRetUrl()),
							"utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.info("用户开户回调参数拼接" + merData.toString());
		return merData;
	}

	// 用户开户验签操作
	public int callback() {
		logger.info("进入用户开户验签回调验证………………");
		String merKeyFile = createPubKeyFile();
		SecureLink sl = new SecureLink();
		logger.info("Chinapnr callback:" + this.getCallbackMerData().toString());
		logger.info("pubKeyFile:" + merKeyFile);
		logger.info("CallbackMerData:" + this.getCallbackMerData().toString());
		logger.info("getChkValue:" + getChkValue());
		int ret = sl.VeriSignMsg(merKeyFile, getCallbackMerData().toString(),
				getChkValue());
		return ret;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getInstuCode() {
		return instuCode;
	}

	public void setInstuCode(String instuCode) {
		this.instuCode = instuCode;
	}

	public String getBusiCode() {
		return busiCode;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getGuarType() {
		return guarType;
	}

	public void setGuarType(String guarType) {
		this.guarType = guarType;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getAuditStat() {
		return auditStat;
	}

	public void setAuditStat(String auditStat) {
		this.auditStat = auditStat;
	}

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getAuditDesc() {
		return auditDesc;
	}

	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
}
