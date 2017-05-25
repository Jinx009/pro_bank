package com.rongdu.p2psys.tpp.ips.model;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.ips.security.utility.IpsCrypto;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.ReflectUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.tpp.yjf.tool.HttpHelper;


public class IpsModel {
	private static Logger logger = Logger.getLogger(IpsModel.class);
	
    //签名md5加密字符串
    private String cert_md5;
    //参数加密字符串
    private String des_key ;
    //请求参数
    private String des_iv ;
    
    public String WS_URL ;
	
    /**
     * 由IPS颁发的商户号
     */
    private String merCode;
    
    public String getCert_md5() {
        return cert_md5;
    }

    public void setCert_md5(String cert_md5) {
        this.cert_md5 = cert_md5;
    }

    public String getDes_key() {
        return des_key;
    }

    public void setDes_key(String des_key) {
        this.des_key = des_key;
    }

    public String getDes_iv() {
        return des_iv;
    }

    public void setDes_iv(String des_iv) {
        this.des_iv = des_iv;
    }

    /**
     * XML 格式的字符串(请求信息)
     */
    private String desXmlPara;
    
    /**
     * 签名参数MD5(pMerCode+p3DesXmlPara+Ips 证书) 取 32 位小写
     */
    private String sign;
    /**
     * 接口提交地址
     */
    private String submitUrl;
    /**
     * 状态返回地址 :同步
     */
    private String webUrl;
	/**
	 * 状态返回地址 ：异步
	 */
	private String s2SUrl;
    
	/**
	 * 校验订单号
	 */
	private String merBillNo;
	
    /**
     * 参数列表
     */
    private String[] paramNames=new String[]{};
    
    /**
     * 提交参数
     */
    private String[][] commitParams = new String[][]{{"pMerCode","p3DesXmlPara","pSign"},{"","",""}};
    
    //回调参数
    /**
     * 处理返回状态
     */
    private String errCode;
    /**
     * 返回信息
     */
    private String errMsg;
    
	public IpsModel(){
		init();
	}
	
	private void init(){
		this.merCode = Global.getString("tpp_base_account");
		this.cert_md5 = Global.getString("ips_cert_md5");
	    this.des_key = Global.getString("ips_des_key");
	    this.des_iv = Global.getString("ips_des_iv");
		if(BaseTPPWay.isOnlineConfig()){
			this.submitUrl = Global.getString("tpp_service_url");
			this.WS_URL = Global.getString("tpp_service_url") + "/CreditWS/Service.asmx";
		}else{
			this.submitUrl = Global.getString("tpp_service_test_url");
			this.WS_URL = Global.getString("tpp_service_test_url") + "/CreditWS/Service.asmx";
		}
	}
	
	
    public void createSign() {
		String[] paramNames = getParamNames();
		StringBuffer buf = new StringBuffer();
		if (paramNames.length > 0) {
			buf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><pReq>");
			for (int i=0; i<paramNames.length; i++) {
				String name = paramNames[i];
				Object result = ReflectUtil.invokeGetMethod(getClass(), this, name);
				String value = (result == null ? "" : result.toString());
				buf.append("<p" + name + ">");
				buf.append(value);
				buf.append("</p"+name+">");
			}
			buf.append("</pReq>");
			String str3DesXmlPana = buf.toString();
			logger.info("str3DesXmlPana  :  " + str3DesXmlPana);
			str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "");
			//参数加密
			this.desXmlPara = IpsCrypto.triDesEncrypt(StringEscapeUtils.unescapeXml(str3DesXmlPana), this.des_key, this.des_iv).replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "");
			this.sign = IpsCrypto.md5Sign(this.merCode + StringEscapeUtils.unescapeXml(this.desXmlPara) + this.cert_md5);
			logger.info("sign :  " + this.sign);
			//提交参数设置
			commitParams[1][0] = this.merCode;
			commitParams[1][1] = this.desXmlPara;
			commitParams[1][2] = this.sign;
		} else {
			this.sign = IpsCrypto.md5Sign(this.merCode + this.cert_md5);
			commitParams = new String[][]{{"argMerCode", "argSign"}, {this.merCode, this.sign}};
		}
	}
    
    public static String getUtf8Str(String str){
    	try {
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
    }
    
    public String createSign(String[] paramNames) {
        StringBuffer buf = new StringBuffer();
        if (paramNames.length > 0) {
            buf.append("<pRow>");
            for (int i=0; i<paramNames.length; i++) {
                String name = paramNames[i];
                Object result = ReflectUtil.invokeGetMethod(getClass(), this, name);
                String value = (result == null ? "" : result.toString());
                buf.append("<p" + name + ">");
                buf.append(value);
                buf.append("</p"+name+">");
            }
            buf.append("</pRow>");
        }
        return buf.toString();
    }
    
    /**
     * 服务器点对点请求时，处理同步回调参数
     * @param str
     * @return
     */
    public XmlTool response(String str) {
    	XmlTool Tool = new XmlTool();
		Tool.SetDocument(str);
    	return Tool;
    }

    /**
     * 后台验签方法
     * @param resultStr
     * @return
     */
    public String checkSign() {
		String signPlainText = this.merCode + this.errCode + this.errMsg + this.desXmlPara+ this.cert_md5;
		logger.info("pMerCode = " + this.merCode +",pErrCode = " + this.errCode+ ",pErrMsg = " + this.errMsg+",p3DesXmlPara = " + this.desXmlPara
				+",pSign = " + this.sign +",signPlainText==="+signPlainText);
		String localSign = IpsCrypto.md5Sign(signPlainText);
		if (localSign.equals(this.sign)) {
			logger.info("MD5验签通过！");
			String str3XmlParaInfo = IpsCrypto.triDesDecrypt(this.desXmlPara, this.des_key, this.des_iv);
			if("".equals(str3XmlParaInfo)||str3XmlParaInfo==null){
				logger.info("3DES解密失败");
				throw new BussinessException("3DES解密失败");
			}else{
				logger.info("解密后参数：str3XmlParaInfo = " + str3XmlParaInfo);
				return str3XmlParaInfo;
			}
		}else{
			logger.info("验证签名失败：本地签名localSign" + localSign + "，环迅签名:pSign"+this.sign+",CERT_MD5:"+this.cert_md5);
			throw new BussinessException("验证签名失败！");
		}
    }
    
    /**
     * @param params 请求参数数组 ：
     * @param value  参数值数组：必须是参数和值对应
     * @param url  请求url
     * @param OperationName  接口名
     * @return result:接口返回数据
     * @throws Exception 
     */
    public String doNotifySubmit(String url,String OperationName) throws Exception{
		String[] names = XmlTool.getArrayByIndex(this.getCommitParams(), 0);
		String[] values = XmlTool.getArrayByIndex(this.getCommitParams(), 1);
		String result = "";
		// 创建一个服务(service)调用(call)
		Service service = new Service();
		Call call = (Call) service.createCall();// 通过service创建call对象
		// 设置service所在URL
		call.setTargetEndpointAddress(new URL(url));
		call.setOperationName(new QName("http://tempuri.org/", OperationName));
		// Add 是net 那边的方法 "http://tempuri.org/" 这个也要注意Namespace 的地址,不带也会报错
		for (int i = 0; i < names.length; i++) { // 动态拼接请求参数
			call.addParameter(new QName("http://tempuri.org/", names[i]),
					XMLType.XSD_STRING, ParameterMode.IN);
		}
		call.setUseSOAPAction(true);
		call.setReturnType(XMLType.SOAP_STRING); // 返回参数的类型
		call.setSOAPActionURI("http://tempuri.org/" + OperationName); // 这个也要注意
																		// 就是要加上要调用的方法Add,不然也会报错
		// Object 数组封装了参数，参数为"This is Test!",调用processService(String arg)
		result = (String) call.invoke(values);
		return result;
    }
    
    public String httpSubmit(){
    	String resp = "";
    	//IpsHttpHelper http=new IpsHttpHelper(getSubmitUrl(),getCommitParams(),"UTF-8");
    	
    	HttpHelper http=new HttpHelper(getSubmitUrl(),getCommitParams(),"UTF-8");
    	try {
    		resp=http.execute();
		} catch (Exception e) {
			logger.info("提交异常！"+e.getMessage());
		}
    	return resp;
    	
    }

	public String getMerCode() {
		return merCode;
	}

	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}

	public String getDesXmlPara() {
		return desXmlPara;
	}

	public void setDesXmlPara(String desXmlPara) {
		this.desXmlPara = desXmlPara;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String[][] getCommitParams() {
		return commitParams;
	}

	public void setCommitParams(String[][] commitParams) {
		this.commitParams = commitParams;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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

	public String getMerBillNo() {
		return merBillNo;
	}

	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}

}
