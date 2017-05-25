package com.rongdu.p2psys.user.model;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.common.util.Page;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 上传资料model类
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月5日
 */
public class UserCertificationModel extends UserCertification {
	private static final long serialVersionUID = 7054696862498865303L;
	/** 当前页码 */
	private int page = 1;

	/** 每页数据条数 */
	private int size = Page.ROWS;
	private String typeName;
	private long typeId;
	private long userId;
	private String userName;
	private String realName;
	private String jifenVal;
	private String validCode;

	public static UserCertificationModel instance(UserCertification attestation) {
		UserCertificationModel attestationModel = new UserCertificationModel();
		BeanUtils.copyProperties(attestation, attestationModel);
		return attestationModel;
	}

	public UserCertification prototype(User user, String picPath, CertificationType attestationType) {
		UserCertification attestation = new UserCertification(user, picPath, attestationType);
		BeanUtils.copyProperties(this, attestation, new String[] { "user", "attestationType", "status", "addTime",
				"addIp", "picPath" });
		return attestation;
	}

	public void validAttestationCommit(File file) {
		if (file == null) {
			throw new UserException("你上传的图片为空！", 1);
		}
		/*if (this.validCode.isEmpty()) {
			throw new UserException("你输入的校验码为空！", 1);
		}
		if (!ValidateUtil.checkValidCode(this.validCode)) {
			throw new UserException("你输入的校验码错误！", 1);
		}*/
		if (!ImageUtil.fileIsImage(file)) {
			throw new UserException("您上传的图片无效，请重新上传！", 1);
		}
	}

	public String getFilePath(User user, String sep, File file) throws Exception {
		Date d1 = new Date();
		String upfiesDir = ServletActionContext.getServletContext().getRealPath("/data/upfiles/images/");
		String realPath = ServletActionContext.getServletContext().getRealPath("");
		String destFileName = upfiesDir + DateUtil.dateStr2(d1) + sep + user.getUserId() + "_attestation" + "_"
				+ d1.getTime() + ".jpg";
		File imageFile1 = new File(destFileName);
		FileUtils.copyFile(file, imageFile1);
		String filePath = destFileName.replace(realPath, "");
		return filePath.replace(sep, "/");
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getJifenVal() {
		return jifenVal;
	}

	public void setJifenVal(String jifenVal) {
		this.jifenVal = jifenVal;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
