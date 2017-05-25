package com.rongdu.manage.action.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;
import com.rongdu.p2psys.core.model.RedPacketModel;
import com.rongdu.p2psys.core.service.RedPacketDetailService;
import com.rongdu.p2psys.core.service.RedPacketService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 红包Action
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
public class RedPacketAction extends BaseAction<RedPacketModel> implements
		ModelDriven<RedPacketModel> {
	@Resource
	private RedPacketService redPacketService;
	@Resource
	private RedPacketDetailService redPacketDetailService;

	private Map<String, Object> map = new HashMap<String, Object>();
	private RedPacketModel model = new RedPacketModel();

	public RedPacketModel getModel() {
		return model;
	}

	/**
	 * 红包管理页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketManage")
	public String redPacketManage() throws Exception {
		return "redPacketManage";
	}

	/**
	 * 红包列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketList")
	public void redPacketList() throws Exception {
		PageDataList<RedPacket> pageList = redPacketService.list(model);
		int totalPage = pageList.getPage().getTotal(); // 总页数
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 填充借款标附属信息
	 * 
	 * @param redPacket
	 * @return
	 */
	public List<RedPacketDetail> fillRedPacketDetail(RedPacket redPacket) {
		List<RedPacketDetail> list = new ArrayList<RedPacketDetail>();
		String[] minArray = request.getParameterValues("min");// 下限
		String[] maxArray = request.getParameterValues("max");// 上限
		String[] moneyArray = request.getParameterValues("detailMoney");// 金额
		String[] ratioArray = request.getParameterValues("detailRatio");// 金额
		// 检查是否值成对
		if (minArray.length != maxArray.length
				&& moneyArray.length != minArray.length) {
			throw new UserException("最小值与最大值以及发放金额不匹配，参数有误", 1);
		}
		if (minArray.length != maxArray.length
				&& ratioArray.length != minArray.length) {
			throw new UserException("最小值与最大值以及兑换金额比例不匹配，参数有误", 1);
		}

		if (minArray.length > 0) {
			for (int i = 0; i < minArray.length; i++) {
				double min = NumberUtil.getDouble(minArray[i]);
				double max = NumberUtil.getDouble(maxArray[i]);
				if (redPacket.getFloatType() == 1) {// 固定金额浮动
					double money = NumberUtil.getDouble(moneyArray[i]);
					RedPacketDetail packetDetail = new RedPacketDetail(
							redPacket, redPacket.getFloatType(), min, max,
							money, 0);
					list.add(packetDetail);
				} else if (redPacket.getFloatType() == 2) {// 金额比例浮动
					double ratio = NumberUtil.getDouble(ratioArray[i]);
					RedPacketDetail packetDetail = new RedPacketDetail(
							redPacket, redPacket.getFloatType(), min, max, 0,
							ratio);
					list.add(packetDetail);
				} else {
					throw new UserException("浮动方式不正确", 1);
				}

			}
		}
		return list;
	}

	/**
	 * 红包修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketEditPage")
	public String redPacketEditPage() throws Exception {
		// 读取原始红包信息
		RedPacket redPacket = redPacketService.find(model.getId());

		// 发放方式及浮动方式
		JSONObject json = JSONObject.parseObject(redPacket.getRule());
		if(json != null){
			JSONArray paymentType = json.getJSONArray("paymentType");
			JSONArray floatType = json.getJSONArray("floatType");

			request.setAttribute("paymentTypes", paymentType);
			request.setAttribute("floatTypes", floatType);
		}
		// 读取原始红包附属信息
		List<RedPacketDetail> list = new ArrayList<RedPacketDetail>();
		if (redPacket.getPaymentType() == 3) {
			list = redPacketDetailService.getList(redPacket);
		}
		request.setAttribute("redPacket", redPacket);
		request.setAttribute("detail", list);
		return "redPacketEditPage";
	}

	/**
	 * 红包修改
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketEdit")
	public void redPacketEdit() throws Exception {
		// 检测数据
		model.checkModel();
		if(model.getEndTime() != null){//结束时间特殊处理
			Date end = DateUtil.valueOf(DateUtil.dateStr2(model.getEndTime()) + " 23:59:59");
			model.setEndTime(end);
		}
		model.setUpdateTime(new Date());
		model.setUpdateUser(getOperatorUserName());
		redPacketService.updateRedPacket(model);
		
		/**
		 * 为浮动金额方式发放及浮动方式时需设置区间信息
		 */
		if (model.getPaymentType() == 3) {
			//先删除之前记录
			redPacketDetailService.deleteRedPacketDetail(model.prototype());
			List<RedPacketDetail> list = fillRedPacketDetail(model.prototype());
			if (list == null || list.size() == 0) {
				printResult("参数错误，请添加红包附属信息", false);
				return;
			}
			redPacketDetailService.addRedPacketDetail(list);
		}
		printResult(MessageUtil.getMessage("I10002"), true);
	}

	/**
	 * 红包详情
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketDetailPage")
	public String redPacketDetailPage() throws Exception {
		// 读取红包信息
		RedPacket redPacket = redPacketService.find(model.getId());
		// 读取红包附属信息
		List<RedPacketDetail> list = new ArrayList<RedPacketDetail>();
		if (redPacket.getPaymentType() == 3) {
			list = redPacketDetailService.getList(redPacket);
		}
		request.setAttribute("redPacket", redPacket);
		request.setAttribute("detail", list);
		return "redPacketDetailPage";
	}
	
	/**
	 * 添加红包 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketAddPage")
	public String redPacketAddPage() throws Exception {
		return "redPacketAddPage";
	}
	
	/**
	 * 添加红包
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketAdd")
	public void redPacketAdd() throws Exception {
		// 检测数据
		model.checkModel();
		if (model.getEndTime() != null) {// 结束时间特殊处理
			Date end = DateUtil.valueOf(DateUtil.dateStr2(model.getEndTime()) + " 23:59:59");
			model.setEndTime(end);
		}
		model.setServiceType(RedPacket.ACTIVITE);
		model.setServiceName("活动红包");
		model.setUpdateTime(new Date());
		model.setUpdateUser(getOperatorUserName());
		//redPacketService.addRedPacket(model.prototype());
		RedPacket redPacket = redPacketService.addRedPacket(model.prototype());

		/**
		 * 为浮动金额方式发放及浮动方式时需设置区间信息
		 */
		if (model.getPaymentType() == 3) {
			List<RedPacketDetail> list = fillRedPacketDetail(redPacket);
			if (list == null || list.size() == 0) {
				printResult("参数错误，请添加红包附属信息", false);
				return;
			}
			redPacketDetailService.addRedPacketDetail(list);
		}
		printResult(MessageUtil.getMessage("I10001"), true);
	}
	
	/**
	 * 删除红包
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/redPacket/redPacketDelete")
	public void redPacketDelete() throws Exception {
		RedPacket redPacket = redPacketService.find(model.getId());
		redPacket.setIsDelete(1);
		redPacketService.updateRedPacket(RedPacketModel.instance(redPacket));
		printResult(MessageUtil.getMessage("I10003"), true);
	}
}
