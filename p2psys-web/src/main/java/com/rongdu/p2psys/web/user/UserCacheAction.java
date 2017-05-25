package com.rongdu.p2psys.web.user;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.OperatorRole;
import com.rongdu.p2psys.core.service.OperatorRoleService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 个人资料
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月18日
 */
/**
 * @author xx
 * @version 2.0
 * @since 2014年4月28日
 */
public class UserCacheAction extends BaseAction<UserCacheModel> implements ModelDriven<UserCacheModel> {

	@Resource
	private UserCacheService userCacheService;
	@Resource
	private OperatorRoleService operatorRoleService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserService userService;

	private Map<String, Object> data;
	private User user;
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 个人资料
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/info/detail")
	public String detail() throws Exception {
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		request.setAttribute(Constant.SESSION_USER, user);
		UserIdentify ui = userIdentifyService.findByUserId(user.getUserId());
		if(ui.getMobilePhoneStatus() == 1 && ui.getRealNameStatus() == 1){
			request.setAttribute("percentage", 100);
		}else if(ui.getMobilePhoneStatus() == 1 || ui.getRealNameStatus() == 1){
			request.setAttribute("percentage", 67);
		}else if(ui.getMobilePhoneStatus() == 0 && ui.getRealNameStatus() == 0){
			request.setAttribute("percentage", 33);
		}
		return "detail";
	}

	/**
	 * vip申请
	 * 
	 * @throws Exception
	 */
	@Action("/member/info/vipApply")
	public void vipApply() throws Exception {
		printWebSuccess();
	}

	/**
	 * 设置专属客服页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/info/customerService")
	public String customerService() throws Exception {
		String adminurl = Global.SYSTEMINFO.getValue("adminurl");
		request.setAttribute("adminurl", adminurl);
		List<OperatorRole> list = operatorRoleService.getKefuList();
		request.setAttribute("list", list);
		try {
			user = getSessionUser();
			UserCache userCache = userCacheService.findByUserId(user.getUserId());
			OperatorRole operatorRole = operatorRoleService.getOperatorRole(userCache.getCustomerUserId());
			request.setAttribute("operatorRole", operatorRole);
		} catch (Exception e) {

		}
		return "customerService";
	}

	/**
	 * 设置专属客服
	 * 
	 * @throws Exception
	 */
	@Action("/member/info/setCustomerService")
	public void setCustomerService() throws Exception {
		user = getSessionUser();
		UserCache userCache = userCacheService.findByUserId(user.getUserId());
		userCache.setCustomerUserId(paramInt("customerUserId"));
		userCacheService.update(userCache);
		printWebSuccess();
	}

	/**
	 * 头像
	 * 
	 * @return
	 */
	@Action("/member/info/face")
	public String face() throws Exception {

		return "face";
	}

	/**
	 * 设置头像
	 */
	@Action("/member/info/setFace")
	public void setFace() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		int x = 0, y = 0, w = 0, h = 0;
		if (model.getCropX() >= 0) {
			x = (int) model.getCropX();
		}
		if (model.getCropY() >= 0) {
			y = (int) model.getCropY();
		}
		if (model.getCropW() >= 0) {
			w = (int) model.getCropW();
		}
		if (model.getCropH() >= 0) {
			h = (int) model.getCropH();
		}
		operateImg(x, y, w, h);
		printWebSuccess();
	}

	@Action("/member/info/upload")
	public void upload() throws Exception {
		data = new HashMap<String, Object>();
		if (!ImageUtil.fileIsImage(file)) {
			data.put("error", true);
		} else {
			user = getSessionUser();
			String url = ServletActionContext.getServletContext().getRealPath("/") + "/data/face/";
			File url_file = new File(url);
			if (!url_file.exists()) {
				url_file.mkdir();
			}
			String file_name = url + user.getUserId() + ".jpg";
			File imgFile = new File(file_name);
			FileUtils.copyFile(file, imgFile);
			File file = new File(file_name);
			BufferedImage srcImage = ImageIO.read(file);
			int w = srcImage.getWidth();
			int h = srcImage.getHeight();
			int minW = (w > h) ? w : h;
			double newWd = (300.0 / minW) * w;
			double newHd = (300.0 / minW) * h;
			BufferedImage destImage = new BufferedImage((int) newWd, (int) newHd, BufferedImage.TYPE_3BYTE_BGR);
			destImage.getGraphics().drawImage(srcImage.getScaledInstance((int) newWd, (int) newHd, Image.SCALE_SMOOTH),
					0, 0, null);
			ImageIO.write(destImage, "jpg", new File(file_name));
			data.put("msg", request.getContextPath() + "/data/face/" + user.getUserId() + ".jpg");
			data.put("width: ", newWd);
			data.put("height", newHd);
		}
		printWebJsonHtml(JSON.toJSONString(data));
	}

	private boolean operateImg(int x, int y, int w, int h) {
		user = getSessionUser();
		String src = ServletActionContext.getServletContext().getRealPath("/") + "/data/face/" + user.getUserId()
				+ ".jpg";
		BufferedImage srcImage = null;
		try {
			srcImage = ImageIO.read(new File(src));
			BufferedImage destImage = srcImage.getSubimage(x, y, w, h);
			BufferedImage lastImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			lastImage.getGraphics().drawImage(destImage, 0, 0, null);
			BufferedImage compressImage = new BufferedImage(98, 98, BufferedImage.TYPE_3BYTE_BGR);
			compressImage.getGraphics().drawImage(lastImage.getScaledInstance(98, 98, Image.SCALE_SMOOTH), 0, 0, null);
			File file = new File(src);
			ImageIO.write(compressImage, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void printWebJsonHtml(String json) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(data));
		out.flush();
		out.close();
	}

}
