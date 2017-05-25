package com.rongdu.p2psys.core.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.Rule;

/**
 * 规则model类
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月5日
 */
public class RuleModel extends Rule {

	private static final long serialVersionUID = 8971537069152829541L;

	private Logger logger = Logger.getLogger(RuleModel.class);

	public RuleModel(Rule rule) {
		if (rule != null) {
			this.setId(rule.getId());
			this.setName(rule.getName());
			this.setAddTime(rule.getAddTime());
			this.setNid(rule.getNid());
			this.setRemark(rule.getRemark());
			this.setRuleCheck(rule.getRuleCheck());
		}
	}

	public RuleModel() {
		super();
	}

	/**
	 * 根据JSON key 提取rule对象
	 * 
	 * @param key JSON Key
	 * @return Object
	 */
	public RuleModel getRuleByKey(String key) {
		if (key == null) {
			return null;
		}
		String json = getValueStrByKey(key);
		RuleModel model = new RuleModel();
		model.setRuleCheck(json);
		return model;
	}

	/**
	 * 根据JSON key 提取对象
	 * 
	 * @param key JSON Key
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public Object getValueByKey(String key) {
		if (key == null) {
			return null;
		}
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) JSON.parse(this.getRuleCheck());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (map == null)
			map = new HashMap<String, Object>();
		Object obj = map.get(key);
		return obj;
	}

	/**
	 * 根据JSON key 提取List
	 * 
	 * @param key JSON Key
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List getValueListByKey(String key) {
		Object obj = this.getValueByKey(key);
		if (obj == null)
			return new ArrayList();
		List list = new ArrayList();
		try {
			list = (List) obj;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 根据JSON key 提取Double类型值
	 * 
	 * @param key JSON Key
	 * @return Double
	 */
	public double getValueDoubleByKey(String key) {
		return BigDecimalUtil.round(StringUtil.isNull(getValueByKey(key)));
	}

	/**
	 * 根据JSON key 提取Integer类型值
	 * 
	 * @param key JSON Key
	 * @return Integer
	 */
	public int getValueIntByKey(String key) {
		return StringUtil.toInt(StringUtil.isNull(getValueByKey(key)));
	}

	/**
	 * 根据JSON key 提取Long类型值
	 * 
	 * @param key JSON Key
	 * @return Integer
	 */
	public long getValueLongByKey(String key) {
		return StringUtil.toLong(StringUtil.isNull(getValueByKey(key)));
	}

	/**
	 * 根据JSON key 提取Integer类型值
	 * 
	 * @param key JSON Key
	 * @return Integer
	 */
	public float getValueFloatByKey(String key) {

		if (key == null || key.length() <= 0)
			return 0;

		Object obj = getValueByKey(key);

		String val = "";
		if (obj != null) {
			val = obj.toString();
		}

		if (val != null && val.length() > 0) {
			return Float.parseFloat(val);
		}
		return 0;
	}

	/**
	 * 根据JSON key 提取String类型值
	 * 
	 * @param key JSON Key
	 * @return String
	 */
	public String getValueStrByKey(String key) {
		return StringUtil.isNull(getValueByKey(key));
	}

	/**
	 * 根据JSON key 提取byte类型值
	 * 
	 * @param key JSON Key
	 * @return Integer
	 */
	public byte getValueByteByKey(String key) {
		String str = StringUtil.isNull(getValueByKey(key));
		if (str == null || str.equals(""))
			return 0;
		byte ret = 0;
		try {
			ret = Byte.parseByte(str);
		} catch (NumberFormatException e) {
			ret = 0;
		}
		return ret;
	}

}
