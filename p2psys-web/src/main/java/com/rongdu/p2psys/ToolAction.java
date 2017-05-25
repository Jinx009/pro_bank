package com.rongdu.p2psys;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.service.UserCacheService;

/**
 * 工具类Action,验证码、生产图片
 * 
 * @author fuxingxing
 */
public class ToolAction extends BaseAction {
	private static Logger logger = Logger.getLogger(ToolAction.class);
	@Resource
	private UserCacheService userCacheService;
	
	private String userId;
	private String size;

	private double account;
	private double lilv;
	private int times;
	private int time_limit_day;
	private String type;

	private File upload;
	private File localUrl;
	private String uploadFileName;
	private String sep = File.separator;

	// 裁剪后的图像大小
	private double cropX;
	private double cropY;
	private double cropW;
	private double cropH;

	private String plugintype;

	/**
	 * 验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/validimg")
	public String validimg() throws Exception {
		genernateCaptchaImage();
		return null;
	}

	/**
	 * 异步校验验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/valicode")
	public String valicode() throws Exception {
		String valicode = paramString("valicode");
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtil.isBlank(valicode) || !ValidateUtil.checkValidCode(valicode)) {
			throw new UserException("验证码错误!", 1);
		}
		data.put("result", true);
		data.put("msg", "验证码正确!");
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 动态输出图像Action
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/imgurl")
	public String imgurl() throws Exception {
		if (StringUtil.toInt(userId) < 1) {
			userId = StringUtil.toInt(userId) + "";
		}
		userId = (userId == null || userId.equals("")) ? "" : userId;
		size = (size == null || size.equals("")) ? "" : size;
		String[] sizes = { "big", "middle", "small" };
		List<String> sizelist = Arrays.asList(sizes);
		size = sizelist.contains(size) ? size : "big";

		String url = "data" + sep + "avatar" + sep + userId + "_avatar_" + size + ".jpg";
		String contextPath = ServletActionContext.getServletContext().getRealPath("/");
		if (contextPath.lastIndexOf(sep) < contextPath.length() - 1) {
			contextPath += sep;
		}
		url = contextPath + url;
		File avatarFile = new File(url);
		if (!avatarFile.exists()) {
			//获取用户类型
			UserCache cache = userCacheService.findByUserId(NumberUtil.getLong(userId));
			if(cache != null && (cache.getUserType() == 2)){
				url = contextPath + "data" + sep + "avatar" + sep + "noavatar_" + size + "_borrow.gif";
			}else{
				url = contextPath + "data" + sep + "avatar" + sep + "noavatar_" + size + ".gif";
			}
		}
		String urlFile = contextPath + "data" + sep + "avatar" + sep;
		File file = new File(urlFile);
		if (!file.exists()) {
			file.mkdirs();
		}
		logger.debug("IMG_URL:" + url);
		cteateImg(url);
		return null;
	}

	public String editorUploadImg() throws Exception {
		String newFileName = "";
		String retMsg = "";
		if (upload == null) {
			retMsg = getRetMsg(1, "上传图片失败！", "");
		}
		if (retMsg.isEmpty()) {
			String imgUrl = upload(upload, uploadFileName, "/data/upload", newFileName + ".jpg");
			retMsg = getRetMsg(0, "上传图片成功！", imgUrl);
		}

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(retMsg);
		out.flush();
		out.close();
		return null;
	}

	@Action("/uploadImg")
	public void uploadImg() throws Exception {
		String url = ServletActionContext.getServletContext().getRealPath("/") + "/data/upfiles/images/";
		File url_file = new File(url);
		if (!url_file.exists()) {
			url_file.mkdir();
		}
		String file_name = System.currentTimeMillis() + ".jpg";
		File imgFile = new File(url + file_name);
		FileUtils.copyFile(upload, imgFile);
		response.getWriter().print(JSONArray.toJSON(file_name));
	}

	private String getRetMsg(int flag, String msg, String imgUrl) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", flag);
		map.put("message", msg);
		map.put("url", request.getContextPath() + imgUrl);
		return JSON.toJSONString(map);
	}

	public String upload() throws Exception {
		Map map = ServletActionContext.getContext().getSession();
		User user = (User) map.get(Constant.SESSION_USER);
		logger.debug("文件：" + this.upload);
		logger.debug("文件名：" + uploadFileName);
		String destfilename2 = ServletActionContext.getServletContext().getRealPath("/data") + sep + "temp" + sep
				+ user.getUserId() + ".jpg";
		logger.debug(destfilename2);
		File imageFile2 = null;
		try {
			imageFile2 = new File(destfilename2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String retMsg = "";
		if (!ImageUtil.fileIsImage(upload)) {
			retMsg = getRetMsg(1, "您上传的图片无效，请重新上传！", "");
			out.print(retMsg);
			out.flush();
			out.close();
			return null;
		}
		FileUtils.copyFile(upload, imageFile2);

		// 保存上传的临时图片名称
		String src = request.getContextPath() + "/data/temp/" + user.getUserId() + ".jpg";
		BufferedImage srcImage = null;
		BufferedImage destImage = null;
		File file = new File(destfilename2);
		int newWi = 0;
		int newHi = 0;
		try {
			srcImage = ImageIO.read(file);
			int w = srcImage.getWidth();
			int h = srcImage.getHeight();
			int minW = (w > h) ? w : h;
			double newWd = (300.0 / minW) * w;
			double newHd = (300.0 / minW) * h;
			newWi = (int) newWd;
			newHi = (int) newHd;
			destImage = new BufferedImage(newWi, newHi, BufferedImage.TYPE_3BYTE_BGR);
			destImage.getGraphics().drawImage(srcImage.getScaledInstance(newWi, newHi, Image.SCALE_SMOOTH), 0, 0, null);
			ImageIO.write(destImage, "jpg", new File(destfilename2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Print img:" + src);
		out.print("{");
		out.print("error: '',\n");
		out.print("msg: '" + src + "',\n");
		out.print("width: " + newWi + ",\n");
		out.print("height: " + newHi + "\n");
		out.print("}");
		out.flush();
		out.close();
		return null;
	}

	// 生成需要裁剪的照片
	public String cropimg() throws Exception {
		try {
			Map map = ServletActionContext.getContext().getSession();
			User user = (User) map.get(Constant.SESSION_USER);
			String destfilename = ServletActionContext.getServletContext().getRealPath("/data") + sep + "temp" + sep
					+ user.getUserId() + ".jpg";
			logger.info(destfilename);
			this.cteateImg(destfilename);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// 保存头像
	public String saveAvatar() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=UTF-8");
			int x = 0, y = 0, w = 0, h = 0;
			if (cropX >= 0) {
				x = (int) cropX;
			}
			if (cropY >= 0) {
				y = (int) cropY;
			}
			if (cropW >= 0) {
				w = (int) cropW;
			}
			if (cropH >= 0) {
				h = (int) cropH;
			}

			logger.debug("X=" + x + ",Y=" + y + ",W=" + w + ",H=" + h);
			@SuppressWarnings("unused")
			boolean re = operateImg(x, y, w, h);
			PrintWriter out = response.getWriter();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", 1);
			map.put("msg", "success");
			map.put("useravatar", "");
			out.print(JSON.toJSON(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean operateImg(int x, int y, int w, int h) throws IOException {
		Map map = ServletActionContext.getContext().getSession();
		User user = (User) map.get(Constant.SESSION_USER);
		String dataPath = ServletActionContext.getServletContext().getRealPath("/data");
		String avatarDir = dataPath + sep + "avatar";
		String dest = avatarDir + sep + user.getUserId() + "_avatar_middle.jpg";
		String smalldest = avatarDir + sep + user.getUserId() + "_avatar_small.jpg";
		String src = ServletActionContext.getServletContext().getRealPath("/data") + sep + "temp" + sep
				+ user.getUserId() + ".jpg";
		BufferedImage srcImage = null;
		try {
			srcImage = ImageIO.read(new File(src));
			BufferedImage destImage = srcImage.getSubimage(x, y, w, h);
			BufferedImage lastImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			lastImage.getGraphics().drawImage(destImage, 0, 0, null);
			BufferedImage compressImage = new BufferedImage(98, 98, BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage compressImage48 = new BufferedImage(48, 48, BufferedImage.TYPE_3BYTE_BGR);
			compressImage.getGraphics().drawImage(lastImage.getScaledInstance(98, 98, Image.SCALE_SMOOTH), 0, 0, null);
			compressImage48.getGraphics()
					.drawImage(lastImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH), 0, 0, null);
			File avatarDirFile = new File(avatarDir);
			if (!avatarDirFile.exists()) {
				avatarDirFile.mkdir();
			}
			File newFile = new File(dest);
			File smallNewFile = new File(smalldest);
			logger.info("Avatar dest:" + dest);
			ImageIO.write(compressImage, "jpg", newFile);
			ImageIO.write(compressImage48, "jpg", smallNewFile);
			FileUtils.forceDelete(new File(src));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 以图片流形式输出
	 * 
	 * @param url
	 * @throws IOException
	 */
	private void cteateImg(String url) throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		OutputStream output = res.getOutputStream();// 得到输出流
		if (url.toLowerCase().endsWith(".jpg")) {
			// 表明生成的响应是图片
			res.setContentType("image/jpeg");
		} else if (url.toLowerCase().endsWith(".gif")) {
			res.setContentType("image/gif");
		}
		File file = new File(url);
		if (!file.exists()) {
			url = ServletActionContext.getServletContext().getRealPath("/data") + sep + "avatar" + sep
					+ "noavatar_middle.gif";
		}
		InputStream imageIn = new FileInputStream(new File(url));
		BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流
		BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流
		byte data[] = new byte[4096];// 缓冲字节数
		int size = 0;
		size = bis.read(data);
		while (size != -1) {
			bos.write(data, 0, size);
			size = bis.read(data);
		}
		bis.close();
		bos.flush();// 清空输出缓冲流
		bos.close();
		output.flush();
		output.close();
	}

	/**
	 * 显示插件Action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String plugin() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		return null;
	}

	public String actionNotFound() throws Exception {
		return SUCCESS;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getLilv() {
		return lilv;
	}

	public void setLilv(double lilv) {
		this.lilv = lilv;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTime_limit_day() {
		return time_limit_day;
	}

	public void setTime_limit_day(int time_limit_day) {
		this.time_limit_day = time_limit_day;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public double getCropX() {
		return cropX;
	}

	public void setCropX(double cropX) {
		this.cropX = cropX;
	}

	public double getCropY() {
		return cropY;
	}

	public void setCropY(double cropY) {
		this.cropY = cropY;
	}

	public double getCropW() {
		return cropW;
	}

	public void setCropW(double cropW) {
		this.cropW = cropW;
	}

	public double getCropH() {
		return cropH;
	}

	public void setCropH(double cropH) {
		this.cropH = cropH;
	}

	public String getPlugintype() {
		return plugintype;
	}

	public void setPlugintype(String plugintype) {
		this.plugintype = plugintype;
	}

	public File getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(File localUrl) {
		this.localUrl = localUrl;
	}

}
