package com.rongdu.manage.action.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.service.RedPacketService;
import com.rongdu.p2psys.core.util.excel.ExcelUtils;
import com.rongdu.p2psys.core.util.excel.JsGridReportBase;
import com.rongdu.p2psys.core.util.excel.TableData;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 用户红包管理
 * @author zf
 * @version 2.0
 * @since 2014年11月4日
 */
public class ManageUserRedPacketAction extends BaseAction<UserRedPacketModel> implements ModelDriven<UserRedPacketModel>{

	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private RedPacketService redPacketService;
	@Resource
	private UserService userService;

	private Map<String, Object> data;

	/**
	 * 获得用户红包清单页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketManager")
	public String userRedPacketManager() throws Exception {
		List<RedPacket> list = redPacketService.findAll();
		request.setAttribute("list", list);
		return "userRedPacketManager";
	}
	
	/**
	 * 后台发标 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/user/redPacket/addRedPacket")
	public void addRedPacket() throws Exception
	{
		
		try
		{
			User user = userService.getUserById(paramInt("userId"));
			int redPacketCount = paramInt("redPacketNo");
			// 红包
			RedPacket redPacket = redPacketService
					.find(paramInt("redPacketType"));
			if(redPacketCount>0)
			{
				for(int i=0;i<redPacketCount;i++){
					userRedPacketService.giveUserRedPacket(redPacket, user);
				}
			}

			printResult("添加成功", true);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}


	/**
	 * 获得用户红包清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketList")
	public void userRedPacketList() throws Exception {
		data = new HashMap<String, Object>();
		PageDataList<UserRedPacketModel> pagaDataList = userRedPacketService.findByModel(model);
		int total = pagaDataList.getPage().getTotal();// 总记录数
		data.put("total", total);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 获得用户红包统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketStatisticsManager")
	public String userRedPacketStatisticsManager() throws Exception {
		List<RedPacket> list = redPacketService.findAll();
		request.setAttribute("list", list);
		return "userRedPacketStatisticsManager";
	}
	
	/**
	 * 获得用户红包清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketStatisticsList")
	public void userRedPacketStatisticsList() throws Exception {
		data = new HashMap<String, Object>();
		List<UserRedPacketModel> list = userRedPacketService.statisticsByModel(model);
		data.put("rows", list);
		long total = userRedPacketService.getTotalByModel(model);
		data.put("total", total);
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 导出用户红包清单列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/exportUserRedPacketStatisticsList")
	public void exportUserRedPacketStatisticsList() throws Exception {
		response.setContentType("application/msexcel;charset=UTF-8");
		// 第一页开始
		model.setPage(1);
		// 最多出5000条记录
		model.setRows(5000);
		PageDataList<UserRedPacketModel> pagaDataList = userRedPacketService.findByModel(model);
		
		String title = "红包记录Excel表";
		String[] hearders = new String[] { "编号", "用户名", "真实姓名", "红包金额（元）", "红包名称", "领取时间", "过期时间","是否兑换","兑换时间" };// 表头数组
		String[] fields = new String[] { "id", "userName", "realName", "amount", "name", "addTime", "expiredTime","statusStr","usedTime"};// 对象属性数组
		TableData td = ExcelUtils.createTableData(pagaDataList.getList(), ExcelUtils.createTableHeader(hearders), fields);
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportToExcel(title, getOperator().getUserName(), td);
	}
	/**
	 * 获得用户红包ID获取红包明细
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketDetail")
	public String userRedPacketDetail() throws Exception {
		return "userRedPacketDetail";
	}
	
	/**
	 * 获得用户清单页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketSendManager")
	public String userManager() throws Exception {
		return "userRedPacketSendManager";
	}
	
	/**
	 * 修改用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketEditPage")
	public String userEditPage() throws Exception {
		long userId = paramLong("id");
		User user = userService.getUserById(userId);
		List<RedPacket> redPackets = redPacketService.findFixedActiveRedPacket();
		request.setAttribute("redPackets", redPackets);
	
		request.setAttribute("user", user);

		return "userRedPacketEditPage";
	}
	
	/**
	 * 获得用户清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userList")
	public void userList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = request.getParameter("status");
		if (status == null) {
			model.setStatus(99);
		}
		UserModel uModel = new UserModel();
		uModel.setSearchName(model.getSearchName());
		uModel.setRealNameStatus(1);
		PageDataList<UserModel> pagaDataList = userService.userList(pageNumber,
				pageSize, uModel);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
}
