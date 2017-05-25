package com.rongdu.p2psys.user.model.identify;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 手动审核方式
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月21日
 */
public class ManualUserIdentifyWay extends BaseUserIdentifyWay {
	private final static Logger logger=Logger.getLogger(ManualUserIdentifyWay.class);
	
	private UserCacheService userCacheService;
	private UserService userService;
	private UserIdentifyService userIdentifyService;
	private String sep = File.separator;

	private long userId;
	private UserModel model;

	public ManualUserIdentifyWay(long userId, UserModel model) {
		this.userId = userId;
		this.model = model;
	}

	@Override
	public Object doRealname() throws Exception {
		userCacheService = (UserCacheService) BeanUtil.getBean("userCacheService");
		userService = (UserService) BeanUtil.getBean("userService");
		userIdentifyService = (UserIdentifyService) BeanUtil.getBean("userIdentifyService");
		// 验证上传的文件
		model.validAttestationCommit(model.getCardPositive());
		model.validAttestationCommit(model.getCardOpposite());
		// 上传图片并保存图片路径
		String filePath1 = getFilePath(userId, sep, model.getCardPositive());
		String filePath2 = getFilePath(userId, sep, model.getCardOpposite());
		userCacheService.modify(userId, model, filePath1, filePath2);
		
		UserIdentify userIdentify = userIdentifyService.findByUserId(userId);
		userIdentifyService.modifyRealnameStatus(userId, 2, userIdentify.getRealNameStatus());
		return null;
	}

	public String getFilePath(long userId, String sep, File file) throws Exception {
		Date d1 = new Date();
		String upfiesDir = ServletActionContext.getServletContext().getRealPath("/data/upfiles/images/");
		String realPath = ServletActionContext.getServletContext().getRealPath("");
		String destFileName = upfiesDir + DateUtil.dateStr2(d1) + sep + userId + "_attestation" + "_"
				+ d1.getTime() + ".jpg";
		File imageFile1 = null;
		imageFile1 = new File(destFileName);
		FileUtils.copyFile(file, imageFile1);
		logger.error("文件："+imageFile1.getPath());
		String filePath = destFileName.replace(realPath, "");
		return filePath.replace(sep, "/");
	}

	private String truncatUrl(String old, String truncat, String sep) {
		String url = "";
		url = old.replace(truncat, "");
		url = url.replace(sep, "/");
		return url;
	}

}
