package com.rongdu.p2psys.core.util;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.core.Global;


/**
 * 易极付验证签名
 * v1.8.0.4_u1  TGPROJECT-263  qj 2014-05-05  
 * @author qj
 *
 */
public class YjfUtil {
	/**
	 * 从返回的数据，校验签名是否正确
	 * 
	 * @param map
	 * @param securityCheckKey
	 * @param de
	 *            签名算法,如果request中有SIGN_TYPE_KEY，则以SIGN_TYPE_KEY指定的摘要算法摘要
	 * @param charset
	 *            编码
	 * @return
	 */
	public static void check(Map<String, String> map) {
		String securityCheckKey = Global.getValue("yjf_key");
		if (securityCheckKey == null) {
			throw new BussinessException("安全校验码不能为空");
		}
		if (map == null) {
			throw new BussinessException("返回map集合不能为空");
		}
		String sign = map.get("sign");
		TreeMap<String, String> treeMap = new TreeMap<String, String>(map);
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : treeMap.entrySet()) {
			if (entry.getValue() == null) {
				throw new BussinessException(entry.getKey() + " 待签名值不能为空");
			}
			if (entry.getKey().equals("sign")) {
				continue;
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue())
					.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);

		sb.append(securityCheckKey);

		byte[] toDigest;
		String digest;
		try {
			String str = sb.toString();
			toDigest = str.getBytes("utf-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(toDigest);
			digest = new String(Hex.encodeHex(md.digest()));
		} catch (Exception e) {
			throw new BussinessException("签名失败");
		}
		if (!sign.equals(digest)) {
			throw new BussinessException("签名校验失败");
		}
	}

}
