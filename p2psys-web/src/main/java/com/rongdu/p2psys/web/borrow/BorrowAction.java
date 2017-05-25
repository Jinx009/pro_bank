package com.rongdu.p2psys.web.borrow;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.service.UserCreditService;

public class BorrowAction extends BaseAction<BorrowModel> implements
		ModelDriven<BorrowModel> {
	@Resource
	private BorrowService borrowService;
	@Resource
	private UserCreditService userCreditService;

	private BorrowModel model = new BorrowModel();

	@Override
	public BorrowModel getModel() {
		return model;
	}

	private User user;

	/**
	 * 我要投资页/标种介绍页（个人借款）
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/borrow/index")
	public String index() throws Exception {
		request.setAttribute("borrowConfigList", Global.BORROW_CONFIG_LIST);
		user = getSessionUser();
		if (user != null) {
			request.setAttribute("userName", user.getUserName());
		} else {
			request.setAttribute("userName", "");
		}
		return "borrowIndex";
	}

	/**
	 * 我要投资页/标种介绍页（企业借款）
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/borrow/companyBorrowIndex")
	public String companyBorrowIndex() throws Exception {
		request.setAttribute("borrowConfigList", Global.BORROW_CONFIG_LIST);
		user = getSessionUser();
		if (user != null) {
			request.setAttribute("userName", user.getUserName());
		} else {
			request.setAttribute("userName", "");
		}
		return "companyBorrowIndex";
	}

	/**
	 * 发标页面
	 * 
	 * @return
	 * @throws Exception
	 *             if has error
	 */
	@Action(value = "/borrow/loan", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String loan() throws Exception {
		user = getSessionUser();
		String type = paramString("type");
		long borrowId = paramLong("borrowId");
		//// model.setType(model.getBorrowType(StringUtil.isNull(type)));
		if (borrowId == 0) { // 发借款标
			borrowService.allowPublish(user, model);
		} else { // 修改借款标
			Borrow borrow = borrowService.find(borrowId);
			BorrowModel model = BorrowModel.instance(borrow);
			request.setAttribute("borrow", model);
		}
		request.setAttribute("typeStr", type);
		request.setAttribute("btype", model.getType());
		return "borrow";
	}

	// /**
	// * 发标
	// *
	// * @return
	// * @throws Exception if has error
	// */
	// @Action("/borrow/doLoan")
	// public void doLoan() throws Exception {
	// user = getSessionUser();
	// model.validBorrowCode(paramString("valicode"));
	// model.validBorrowStyle();
	// //验证定向标
	// model.validDXB();
	// //新增奖励设置
	// model.checkAward();
	// //获得上传图片字符串数组
	// String[] picPaths = request.getParameterValues("picPath");
	// if (picPaths == null) {
	// throw new BorrowException("图片不能为空", BussinessException.TYPE_JSON);
	// }
	// List<BorrowUpload> list = new ArrayList<BorrowUpload>();
	// for (String picPath : picPaths) {
	// BorrowUpload bu = new BorrowUpload();
	// bu.setPicPath(picPath);
	// bu.setType(4);
	// list.add(bu);
	// }
	// if(model.getBorrowTimeType()==0){
	// model.setTimeLimit(paramInt("timeLimit"));
	// }else{
	// model.setTimeLimit(paramInt("dayLimit"));
	// }
	// borrowService.save(model, user, list);
	// printWebSuccess();
	// }

	// /**
	// * 需要跳转页面的发标
	// *
	// * @return
	// * @throws Exception
	// * if has error
	// */
	// @Action(value = "/borrow/doLoanSkip", results = { @Result(name =
	// "ipsBorrow", type = "ftl", location = "/tpp/ipsBorrow.html") })
	// public String doLoanSkip() throws Exception {
	// user = getSessionUser();
	// model.validBorrowCode(paramString("valicode"));
	// model.validBorrowStyle();
	// // 验证定向标
	// model.validDXB();
	// // 新增奖励设置
	// model.checkAward();
	// // 获得上传图片字符串数组
	// String[] picPaths = request.getParameterValues("picPath");
	// List<BorrowUpload> list = new ArrayList<BorrowUpload>();
	// for (String picPath : picPaths) {
	// BorrowUpload bu = new BorrowUpload();
	// bu.setPicPath(picPath);
	// list.add(bu);
	// }
	// model.setTimeLimit(paramInt("timeLimit"));
	// Borrow borrow = borrowService.save(model, user, list);
	// String name = Global.getValue("cooperation_interface");
	// TPPWay way = TPPFactory.getTPPWay(null, user, null, null, borrow);
	// Object depositBorrow = way.doBorrow();
	// request.setAttribute(name, depositBorrow);
	// return name + "Borrow";
	// }

	/**
	 * 上传文件到服务器
	 * 
	 * @throws Exception
	 */
	@Action("/borrow/uploadBorrowPic")
	public void uploadBorrowPic() throws Exception {
		String path = imgUpload();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("picPath", path);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 删除上传的图片
	 * 
	 * @throws Exception
	 */
	@Action("/borrow/deleteBorrowPic")
	public void deleteBorrowPic() throws Exception {
		String pathPic = paramString("pathPic");
		String realPath = ServletActionContext.getServletContext().getRealPath(
				pathPic);
		new File(realPath).delete();
		printWebSuccess();
	}

	/**
	 * 修改标
	 * 
	 * @return
	 */
	@Action("/borrow/updateLoan")
	public void updateLoan() throws Exception {
		user = getSessionUser();
		// Map<String, Object> data = new HashMap<String, Object>();
		// long borrowId = paramLong("borrowId");
		// Borrow oldBorrow = this.borrowService.find(borrowId);
		// model.validBorrowCode(paramString("valicode"));
		// model.validBorrowStyle();

		// borrowService.updateLoan(model, user, oldBorrow);

		printWebSuccess();
	}

	/**
	 * 校验可用信用额度
	 */
	@Action("/borrow/checkAccount")
	public void checkAccount() {
		UserCredit amount = userCreditService.findByUserId(getSessionUser()
				.getUserId());
		PrintWriter out = null;
		try {
			// 指定输出内容类型和编码
			ServletActionContext.getResponse().setContentType(
					"text/html;charset=utf-8");
			out = ServletActionContext.getResponse().getWriter();
			double creditUse = amount.getCreditUse();
			String borrowId = paramString("borrowId");
			if (!StringUtil.isBlank(borrowId)) {
				Borrow b = borrowService.find(Long.parseLong(borrowId));
				creditUse = BigDecimalUtil.add(b.getAccount(), creditUse);
			}
			if (amount == null || paramDouble("account") > creditUse) {
				out.print("您当前的可用信用额度为" + creditUse + "元<br/>");
			}
		} catch (Exception ex) {
			throw new BorrowException("校验可用信用额度异常", 1);
		} finally {
			out.close();
		}
	}

}
