package com.rongdu.p2psys.web.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.domain.UserCertificationApply;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserCertificationModel;
import com.rongdu.p2psys.user.service.CertificationTypeService;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserCreditApplyService;

/**
 * 证明材料
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月20日
 */
public class UserCertificationAction extends BaseAction<UserCertificationModel> implements ModelDriven<UserCertificationModel> {

	@Resource
	private UserCertificationService userCertificationService;
	@Resource
	private UserCertificationApplyService userCertificationApplyService;
	@Resource
	private UserCreditApplyService userCreditApplyService;
	@Resource
	private CertificationTypeService certificationTypeService;
	
	private User user;
	private Map<String, Object> data;

	/**
	 * 证明材料
	 * 
	 * @return
	 */
	@Action(value="/member/usercertification/files",results = { @Result(name = "files", type = "ftl", location = "/member/usercertification/files.html"),
			@Result(name = "files_firm", type = "ftl", location = "/member_borrow/usercertification/files.html")})
	public String files() throws Exception {
		user = getSessionUser();
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {
			return "files_firm";
		} 
		return "files";
	}

	/**
	 * 证明材料类型 ajax异步获取
	 * 
	 * @throws Exception
	 */
	@Action("/member/usercertification/fileType")
	public void fileType() throws Exception {
		List<CertificationType> list = certificationTypeService.findAll();
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	/**
	 * 证明材料 ajax异步获取
	 * 
	 * @throws Exception
	 */
	@Action("/member/usercertification/filesList")
	public void filesList() throws Exception {
		user = getSessionUser();
		data = new HashMap<String, Object>();
		long typeId = paramLong("tid");
		List<UserCertification> list = userCertificationService.findByUserAndTypeId(user, typeId);
		int status = userCertificationApplyService.getStatusByUserAndTypeId(user, typeId);
		data.put("status", status);
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 新增证明材料
	 */
	@Action("/member/usercertification/fileUpload")
	public void fileUpload() throws Exception {
		String path = imgUpload();
		data = new HashMap<String, Object>();
		data.put("picPath", path);
		printWebJson(getStringOfJpaObj(data));
	}
	/**
	 * 删除上传的图片
	 * 
	 * @throws Exception
	 */
	@Action("/member/usercertification/deletePic")
	public void deletePic() throws Exception {
		String pathPic = paramString("pathPic");
		String realPath = ServletActionContext.getServletContext().getRealPath(pathPic);
		new File(realPath).delete();
		printWebSuccess();
	}
	/**
	 * 删除证明材料
	 */
	@Action("/member/usercertification/fileDel")
	public void fileDel() throws Exception {
		UserCertification userCertification = userCertificationService.findById(model.getId());
		userCertificationService.delete(model.getId());
		String realPath = ServletActionContext.getServletContext().getRealPath(userCertification.getPicPath());
        new File(realPath).delete();
		printWebSuccess();
	}
	
	/**
	 * 新增证明材料
	 */
	@Action("/member/usercertification/certificationApply")	
	public void certificationApply() throws Exception {
		user = getSessionUser();
		//获得上传图片字符串数组
		String[] picPaths = request.getParameterValues("picPath");
		if (picPaths == null) {
			throw new UserException("至少上传一张认证信息", 1);
		}
		List<UserCertification> list = new ArrayList<UserCertification>();
		CertificationType type = certificationTypeService.findById(model.getTypeId());
		for (String picPath : picPaths) {
			UserCertification uc = new UserCertification();
			uc.setUser(user);
			uc.setCertificationType(type);
			uc.setPicPath(picPath);
			list.add(uc);
		}
		UserCertificationApply userCertificationApply = new UserCertificationApply();
		userCertificationApply.setAddIp(Global.getIP());
		userCertificationApply.setAddTime(new Date());
		userCertificationApply.setUser(user);
		userCertificationApply.setType(type);
		userCertificationApplyService.add(userCertificationApply, list);
		printWebSuccess();
	}
}
