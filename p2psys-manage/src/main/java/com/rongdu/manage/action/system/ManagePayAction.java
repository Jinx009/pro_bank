package com.rongdu.manage.action.system;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.p2psys.account.domain.Pay;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 支付接口配置
 * 
 * @author qj
 * @version 2.0
 * @since 2014-5-20
 */
public class ManagePayAction extends BaseAction implements ModelDriven<Pay> {

	@Resource
	private PayService payService;

	private Pay model = new Pay();
	private Map<String, Object> map = new HashMap<String, Object>();

	private File file;
	private String sep = File.separator;

	@Override
	public Pay getModel() {
		return model;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 支付列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payManager")
	public String payManager() throws Exception {
		return "payManager";
	}

	/**
	 * 支付配置列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payList")
	public void payList() throws Exception {
		int page = paramInt("page");
		int rows = paramInt("rows");
		PageDataList<Pay> pageList = payService.list(page, rows, model);
		int totalPage = pageList.getPage().getTotal();
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 新增配置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payAddPage")
	public String payAddPage() throws Exception {
		return "payAddPage";
	}

	/**
	 * 修改配置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payEditPage")
	public String payEditPage() throws Exception {
		Pay pay = payService.findById(paramLong("id"));
		request.setAttribute("pay", pay);
		return "payEditPage";
	}

	/**
	 * 修改配置
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payEdit")
	public void payEdit() throws Exception {
		Operator oper = getOperator();
		// 校验是否为图片
		validAttestationCommit(file);
		// 上传图片并保存图片路径
		String imageUrl = this.getFilePath(oper, sep, file);
		model.setImageUrl(imageUrl);
		payService.update(model);
		printResult("修改成功！", true);
	}

	/**
	 * 新增配置
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payAdd")
	public void payAdd() throws Exception {
		Operator oper = getOperator();
		// 校验是否为图片
		validAttestationCommit(file);
		// 上传图片并保存图片路径
		String imageUrl = this.getFilePath(oper, sep, file);
		model.setImageUrl(imageUrl);
		payService.save(model);
		printResult("保存成功！", true);
	}

	/**
	 * 删除配置
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/pay/payDelete")
	public void payDelete() throws Exception {
		Pay pay = payService.findById(paramLong("id"));
		payService.delete(pay);
		printResult("删除成功！", true);
	}

	public void validAttestationCommit(File file) {
		if (file == null) {
			throw new UserException("你上传的图片为空！", 1);
		}
		if (!ImageUtil.fileIsImage(file)) {
			throw new UserException("您上传的图片无效，请重新上传！", 1);
		}
	}

	/**
	 * 获取图片路径
	 * 
	 * @param user
	 * @param sep
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private String getFilePath(Operator user, String sep, File file) throws Exception {
		String filePath;
		String dataPath = ServletActionContext.getServletContext().getRealPath("/") + sep + "data";
		// 临时解决linux下面路径不对问题
		if (!dataPath.startsWith(sep)) {
			dataPath = sep + dataPath;
		}
		String contextPath = ServletActionContext.getServletContext().getRealPath("/");
		Date d1 = new Date();
		String upfiesDir = dataPath + sep + "upfiles" + sep + "images" + sep;
		String destfilename1 = upfiesDir + DateUtil.dateStr2(d1) + sep + user.getId() + "_attestation" + "_"
				+ d1.getTime() + ".jpg";
		filePath = destfilename1;
		filePath = this.truncatUrl(filePath, contextPath, sep);
		File imageFile1 = new File(destfilename1);
		FileUtils.copyFile(file, imageFile1);
		return filePath;
	}

	private String truncatUrl(String old, String truncat, String sep) {
		String url = "";
		url = old.replace(truncat, "");
		url = url.replace(sep, "/");
		return url;
	}

}
