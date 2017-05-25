package com.rongdu.p2psys.account.model.payment.llPay.conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.json.JSONObject;

import com.rongdu.common.util.StringUtil;




public class CommonRealize {
	
	public static HashMap<String, String> bank_code_map = new HashMap<String, String>();//存储银行的编码信息
	static{
		bank_code_map.put("中国工商银行","01020000");
		bank_code_map.put("工商银行","01020000");
		bank_code_map.put("中国农业银行","01030000");
		bank_code_map.put("农业银行","01030000");
		bank_code_map.put("中国银行","01040000");
		bank_code_map.put("中国建设银行","01050000");
		bank_code_map.put("建设银行","01050000");
		bank_code_map.put("浦发银行","03100000");
		bank_code_map.put("邮储银行","01000000");
		bank_code_map.put("华夏银行","03040000");
		bank_code_map.put("民生银行","03050000");
		bank_code_map.put("广东发展银行","03060000");
		bank_code_map.put("广发银行","03060000");
		bank_code_map.put("平安银行","03070000");
		bank_code_map.put("光大银行","03030000");
		bank_code_map.put("招商银行","03080000");
		bank_code_map.put("兴业银行","03090000");
		bank_code_map.put("中信银行","03020000");
		bank_code_map.put("交通银行","03010000");
	}
	
	/**
	 * 解析参数并封装Map公共实现部分
	 * @param values
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HashMap<String, String> com_analyzeParam(String values)
			throws UnsupportedEncodingException {
		String decValues = "";
		decValues = URLDecoder.decode(values, "utf-8");
		String var[] = decValues.split("&");
		HashMap<String, String> vmap = new HashMap<String, String>();
		for (String vars : var) {
			String var2[] = vars.split("=");
			if(var2.length==1){
				vmap.put(var2[0], "");
			}
			if(var2.length>1){
				vmap.put(var2[0], var2[1]);
			}
		}
		return vmap;
	}
	
	/**
     * 将json格式的字符串解析成Map对象 <li>
     * json格式：{"name":"admin","test":"test"}
     */
    public static HashMap<String, String> toHashMap(String object){
//    	object = object.substring(1,object.length()-1);//去掉json两端的[]
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(object);
        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()){
            String key = String.valueOf(it.next());
            String value = (String) jsonObject.get(key);
            data.put(key, value);
        }
        return data;
    }
    
    /**
     * 将json格式的字符串解析成Map对象 <li>
     * json格式：{"name":"admin","test":"test"}
     */
    public static HashMap<String, String> toHashMapBanks(String object){
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(object);
        String bankJson = "";
		try {
			bankJson = jsonObject.getString("agreement_list");
		} catch (Exception e) {
			bankJson = "";
		}
		//发现上面的catch机制有时会失效，保险起见，通过String查找的方式再check一遍。
		if( !jsonObject.toString().contains("agreement_list") ){
			bankJson = "";
		}
		if(!StringUtil.isBlank(bankJson)){
			if(bankJson.startsWith("[") && bankJson.endsWith("]")){				
				bankJson = bankJson.substring(1,bankJson.length()-1);//去掉json两端的[]
			}
			// 将json字符串转换成jsonObject
			JSONObject jsonObject2 = JSONObject.fromObject(bankJson);
			Iterator it = jsonObject2.keys();
			// 遍历jsonObject2数据，添加到Map对象
			while (it.hasNext()){
				String key = String.valueOf(it.next());
				String value = (String) jsonObject2.get(key);
				data.put(key, value);
			}
			return data;
		}
		return null;
    }
    
    /**
     * 将json格式的字符串解析成Map对象 
     * json格式：{"ret_code":"0000","ret_msg":"交易成功","sign":"C2KGixCDyCAqD9/fjU06WBog4MLrKOAZfDkhPkmDIJbQ2mN2/ykdSIX3OiKxcNkouvLwjHUsKDc5EHET/CERDqyWIcvtad9KPIdK0sX67teXw2Rqv2gf5ebVEjXpr+wI33L1efOqMQSh8jBheuzhM0lgio/XUXNoXHNwtKK4nxU=","sign_type":"RSA"}

     */
    public static HashMap<String, String> toHashMap2(String object){
    	 HashMap<String, String> data = new HashMap<String, String>();
    	 object = object.substring(2,object.length()-2);//去掉json两端的{""}
    	 object = object.replaceAll(":", ",");
    	 String[] jsons = object.split("\",\"");
    	 for (int i = 0; i < jsons.length; i+=2) {
    		 data.put(jsons[i], jsons[i+1]);
		 }
    	 return data;
    }
    
    /**
     * 将银行名称大于四个字去掉中国 
     */
    public static String subString(String object){
    	if(object!=null){    		
    		if(object.length()>4){
    			if(object.equals("广东发展银行")){
    				object = "广发银行";
    			}else{
    				object = object.replaceAll("中国", "");
    			}
    		}
    		return object;
    	}else{
    		return "test";
    	}
    }
    
    public static void main(String args[]){
    	String s = "{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"}";//,\"sign\":\"C2KGixCDyCAqD9/fjU06WBog4MLrKOAZfDkhPkmDIJbQ2mN2/ykdSIX3OiKxcNkouvLwjHUsKDc5EHET/CERDqyWIcvtad9KPIdK0sX67teXw2Rqv2gf5ebVEjXpr+wI33L1efOqMQSh8jBheuzhM0lgio/XUXNoXHNwtKK4nxU=\",\"sign_type\":\"RSA\"}";
    	HashMap<String, String> data = toHashMapBanks(s);
//    	if("0000".equals(data.get("ret_code"))){
//			System.out.println("=======================================提现成功==="+data.get("ret_msg"));
//		}else{
//			System.out.println("=======================================提现失败==="+data.get("ret_msg"));
//		}
//    	System.out.println(subString("中国银行"));
//    	
//    	String cardno = "6221882900059706556";//"";6227001219780083414
//    	String bankJosn = LianlPayHelper.queryCardBin(cardno);
//    	System.out.println(bankJosn);
    	
//    	OrderResultsBean orderResult = new OrderResultsBean();
//		orderResult.setOid_partner(PartnerConfig.OID_PARTNER);
//		orderResult.setQuery_version(PartnerConfig.VERSION);
//		orderResult.setSign_type(PartnerConfig.SIGN_TYPE);
////		orderResult.setNo_order("20150527181010");
//		// 加签名
//        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
//                .toJSONString(orderResult)), PartnerConfig.TRADER_PRI_KEY,
//                PartnerConfig.MD5_KEY);
//        orderResult.setSign(sign);
//		OrderResultsRetBean ret = LianlPayHelper.queryOrder("20150527181010");
//    	double d = 0.047;
//    	long l = (long)(d*100);
//    	System.out.println(l);
    }

	
	/***
	 * InputStream转字符串
	 * @param is
	 * @return
	 */
	public static String inputStream2String(InputStream is){
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
			while ((line = in.readLine()) != null){
			  buffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return buffer.toString();
    
       }
}
