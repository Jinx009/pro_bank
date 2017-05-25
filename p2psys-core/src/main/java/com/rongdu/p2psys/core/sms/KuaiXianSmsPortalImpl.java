package com.rongdu.p2psys.core.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.esms.MessageData;
import com.esms.PostMsg;
import com.esms.common.entity.Account;
import com.esms.common.entity.AccountInfo;
import com.esms.common.entity.GsmsResponse;
import com.esms.common.entity.MTPack;
import com.esms.common.entity.MTPack.SendType;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.domain.Rule;
import com.rongdu.p2psys.core.rule.RuleModel;

/**
 * 玄武400快线短信接口
 * 
 * @author wcw
 */
public class KuaiXianSmsPortalImpl implements SmsPortal {
	private Logger logger = Logger.getLogger(KuaiXianSmsPortalImpl.class);

	@Override
	public String send(String phone, String content) {
		String result = "";
		Rule rule = Global.getRule(EnumRuleNid.KUAIXIAN_SMS.getValue());
		if (rule != null) {
			RuleModel ruleModel = new RuleModel(rule);
			RuleModel newRuleModel = ruleModel.getRuleByKey("account");
			String username = newRuleModel.getValueStrByKey("username");
			String password = newRuleModel.getValueStrByKey("password");
			newRuleModel = ruleModel.getRuleByKey("send");
			String send_ip = newRuleModel.getValueStrByKey("ip");
			int send_port = newRuleModel.getValueIntByKey("port");
			newRuleModel = ruleModel.getRuleByKey("receive");
			String receive_ip = newRuleModel.getValueStrByKey("ip");
			int receive_port = newRuleModel.getValueIntByKey("port");
			Account ac = new Account(username, password);//
			PostMsg pm = new PostMsg();
			pm.getCmHost().setHost(send_ip, send_port);// 设置网关的IP和port，用于发送信息
			pm.getWsHost().setHost(receive_ip, receive_port);// 设置网关的IP和port，用于获取账号信息、上行、状态报告等等
			MTPack pack = new MTPack();
			pack.setBatchID(UUID.randomUUID());
			pack.setBatchName("短信测试批次");
			pack.setMsgType(MTPack.MsgType.SMS);
			pack.setBizType(0);
			pack.setDistinctFlag(false);
			ArrayList<MessageData> msgs = new ArrayList<MessageData>();

			/** 单发，一号码一内容 */
			pack.setSendType(SendType.GROUP);
			msgs.add(new MessageData(phone, content));
			pack.setMsgs(msgs);

			GsmsResponse resp;
			try {
				resp = pm.post(ac, pack);
				int ret = resp.getResult();
				result = kuanxian_sms(ret);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;

	}

	public String kuanxian_sms(int ret) {
		// result:0 成功，-1 账号无效，-2 参数无效，-3 连接不上服务器，-5 无效的短信数据，号码格式不对
		// -6 用户名密码错误 ,-7 旧密码不正确,-9 资金账户不存在,-11 包号码数量超过最大限制,-12 余额不足,
		// -99 系统内部错误,-100 其它错误
		String result = "";
		if (ret == 0) {
			result = "ok";
		}
		if (ret < 0) {
			switch (ret) {
				case -1:
					result = "账户无效";
					break;
				case -2:
					result = "参数无效";
					break;
				case -3:
					result = "连接不上服务器";
					break;
				case -5:
					result = "无效的短信数据，号码格式不对";
					break;
				case -6:
					result = "用户名密码错误";
					break;
				case -7:
					result = "旧密码不正确";
					break;
				case -9:
					result = "资金账户不存在";
					break;
				case -11:
					result = "包号码数量超过最大限制";
					break;
				case -12:
					result = "余额不足";
					break;
				case -99:
					result = "系统内部错误";
					break;
				case -100:
					result = "其它错误";
					break;
				default:
					result = "其他异常错误";
					break;
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> getUseInfo() {

		if (null == Global.getRule(EnumRuleNid.KUAIXIAN_SMS.getValue())) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}

		Rule rule = Global.getRule(EnumRuleNid.KUAIXIAN_SMS.getValue());

		RuleModel ruleModel = new RuleModel(rule);
		RuleModel newRuleModel = ruleModel.getRuleByKey("account");
		String username = newRuleModel.getValueStrByKey("username");
		String password = newRuleModel.getValueStrByKey("password");
		newRuleModel = ruleModel.getRuleByKey("send");
		String send_ip = newRuleModel.getValueStrByKey("ip");
		int send_port = newRuleModel.getValueIntByKey("port");
		newRuleModel = ruleModel.getRuleByKey("receive");
		String receive_ip = newRuleModel.getValueStrByKey("ip");
		int receive_port = newRuleModel.getValueIntByKey("port");
		Account ac = new Account(username, password);//
		PostMsg pm = new PostMsg();
		pm.getCmHost().setHost(send_ip, send_port);// 设置网关的IP和port，用于发送信息
		pm.getWsHost().setHost(receive_ip, receive_port);// 设置网关的IP和port，用于获取账号信息、上行、状态报告等等

		int usenum = 0;
		int usednum = 0;

		try {
			AccountInfo acinfo = pm.getAccountInfo(ac);
			// 应该不会有余额多到超过整型的支持，所以这里直接转成了int值
			usenum = Integer.parseInt(String.valueOf(acinfo.getBalance()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);

		return map;
	}

	@Override
	public String getSPName() {
		return "KuanXian";
	}

}
