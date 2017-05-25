package com.rongdu.p2psys.core.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.common.util.RSAUtil;
import com.rongdu.common.util.ReflectUtil;
import com.rongdu.common.web.action.CommonAction;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.rule.RsaFormEncryptRuleCheck;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

public class BaseAction<T> extends CommonAction {

	@SuppressWarnings("unchecked")
	protected Class<T> entityClass = ReflectUtil
			.getSuperClassGenricType(getClass());

	private final static Logger logger = Logger.getLogger(BaseAction.class);
	
	/**
	 * 实例对象
	 */
	protected T model;

	protected String searchName;

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	/**
	 * 实现ModelDriven接口getModel方法供子类继承
	 * 
	 * @return T 对象实例
	 */
	public T getModel() {
		try {
			model = entityClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return model;
	}

	private File file;
	private String fileFileName;
	private String fileContentType;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	/**
	 * 上传图片
	 *
	 * @return 图片路径
	 * @throws IOException
	 *             if has error
	 */
	protected String imgUpload() throws IOException {
		String picPath = null;
		if (file != null) {
			if (!ImageUtil.fileIsImage(file)) {
				throw new BussinessException("您上传的图片无效，请重新上传！",
						BussinessException.TYPE_JSON);
			} else {
				Date d = new Date();
				String upfiesDir = ServletActionContext.getServletContext()
						.getRealPath("/data/upfiles/images/");
				String realPath = ServletActionContext.getServletContext()
						.getRealPath("");
				String destFileName = upfiesDir + DateUtil.dateStr2(d) + "/"
						+ entityClass.getSimpleName() + "/" + UUID.randomUUID()
						+ fileFileName.substring(fileFileName.lastIndexOf("."));
				File imageFile = new File(destFileName);
				FileUtils.copyFile(file, imageFile);
				picPath = destFileName.replace(realPath, "").replace(
						File.separator, "/");
			}
		}
		return picPath;
	}
	
	/**
	 * 前台向后台上传图片
	 * 
	 * @return 图片路径
	 * @throws IOException
	 *             if has error
	 */
	protected String manageImgUpload() throws IOException {
		String picPath = null;
		if (file != null) {
				Date d = new Date();
				String upfiesDir = Global.getValue("manage_img_url");
				String realPath = Global.getValue("manage_img_real_url");
				String destFileName = upfiesDir + DateUtil.dateStr2(d) + "/"
						+ entityClass.getSimpleName() + "/" + UUID.randomUUID()
						+ fileFileName.substring(fileFileName.lastIndexOf("."));
				File imageFile = new File(destFileName);
				FileUtils.copyFile(file, imageFile);
				picPath = destFileName.replace(realPath, "").replace(
						File.separator, "/");
		}
		return picPath;
	}

	/**
	 * 上传TXT
	 * 
	 * @return String
	 * @throws IOException
	 */
	protected File txtUpload() throws IOException {
		File txtFile = null;
		if (file != null) {
			Date d = new Date();
			String upfiesDir = ServletActionContext.getServletContext()
					.getRealPath("/data/upfiles/txt/");
			String destFileName = upfiesDir + "/" + DateUtil.dateStr2(d) + "/"
					+ UUID.randomUUID()
					+ fileFileName.substring(fileFileName.lastIndexOf("."));
			txtFile = new File(destFileName);
			FileUtils.copyFile(file, txtFile);

		}
		return txtFile;
	}
	
	/**
	 * 上传文件
	 * 
	 * @return String
	 * @throws IOException
	 */
	protected String singleFileUpload() throws IOException {
		String filePath = "";
		if (file != null) {
			Date d = new Date();
			String upfiesDir = ServletActionContext.getServletContext()
					.getRealPath("/data/upfiles/images/");
			String realPath = ServletActionContext.getServletContext()
					.getRealPath("");
			String destFileName = upfiesDir + DateUtil.dateStr2(d) + "/"
					+ entityClass.getSimpleName() + "/" + UUID.randomUUID()
					+ fileFileName.substring(fileFileName.lastIndexOf("."));
			File imageFile = new File(destFileName);
			FileUtils.copyFile(file, imageFile);
			filePath = destFileName.replace(realPath, "").replace(
					File.separator, "/");
		}
		return filePath;
	}

	private File[] files;
	private String[] filesFileName;

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String[] getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	/**
	 * 上传多图片
	 * 
	 * @return 图片路径数组
	 * @throws IOException
	 *             if has error
	 */
	protected String[] imgsUpload() throws IOException {
		String fileIndex = paramString("fileIndex");
		int fileSize = paramInt("fileSize");
		String[] indexs = fileIndex.split(",");
		String[] picPath = new String[fileSize];
		if (files != null && files[0] != null) {
			for (int i = 0; i < files.length; i++) {
				if (!ImageUtil.fileIsImage(files[i])) {
					printResult("您上传的图片无效，请重新上传！", true);
				} else {
					int index = Integer.parseInt(indexs[i]);
					Operator oper = getOperator();
					Date d = new Date();
					String upfiesDir = ServletActionContext.getServletContext()
							.getRealPath("/data/upfiles/images/");
					String realPath = ServletActionContext.getServletContext()
							.getRealPath("");
					String destFileName = upfiesDir
							+ DateUtil.dateStr2(d)
							+ "/"
							+ oper.getId()
							+ entityClass.getSimpleName()
							+ "/"
							+ DateUtil.dateStr(d, "HHmmss")
							+ i
							+ filesFileName[i].substring(filesFileName[i]
									.lastIndexOf("."));
					File imageFile = new File(destFileName);
					FileUtils.copyFile(files[i], imageFile);
					picPath[index] = destFileName.replace(realPath, "")
							.replace(File.separator, "/");
				}
			}
		}
		return picPath;
	}

	/**
	 * pc2.0 session中隐藏手机号
	 * 
	 * @return
	 */
	protected void setSessionHideUserName(User user) {
		String userName = user.getUserName().substring(0, 3) + "****"
				+ user.getUserName().substring(7, user.getUserName().length());
		request.getSession().setAttribute(ConstantUtil.HIDE_SESSION_USERNAME,
				userName);
	}

	/**
	 * pc2.0
	 * 
	 * @return
	 */
	protected User getNBSessionUser() {
		User user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		return user;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	protected HashMap<String, Object> getErrorMap() {
		Map<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put(ConstantUtil.RESULT, ConstantUtil.NO_LOGIN_USER);
		hashMap.put(ConstantUtil.ERRORMSG, ConstantUtil.NO_LOGIN_USER_MSG);

		return (HashMap<String, Object>) hashMap;
	}

	/**
	 * pc2.0
	 * 
	 * @return
	 */
	protected boolean hasSessionUser() {
		User user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		if (null != user) {
			return true;
		}
		return false;
	}

	/**
	 * 取session中值
	 * 
	 * @param sessionKey
	 * @return
	 */
	protected Object getSessionObject(String sessionKey) {
		return request.getSession().getAttribute(sessionKey);
	}

	/**
	 * 获取session中字符串值
	 * 
	 * @param sessionKey
	 * @return
	 */
	protected String getSessionString(String sessionKey) {
		if (null != request.getSession().getAttribute(sessionKey)) {
			return request.getSession().getAttribute(sessionKey).toString();
		} else {
			return "";
		}
	}

	/**
	 * pc投资防重复提交
	 */
	@SuppressWarnings("static-access")
	protected boolean checkInvestToken() {
		User user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);

		String userIdStr = String.valueOf(user.getUserId());

		if (null == context.getAttribute(userIdStr)) {
			Date date = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.SECOND, 5);
			date = calendar.getTime();

			context.setAttribute(userIdStr, date);

			return true;
		} else {
			Date date2 = (Date) context.getAttribute(userIdStr);
			Date date = new Date();

			if (date2.before(date)) {
				Calendar calendar2 = new GregorianCalendar();
				calendar2.setTime(date);
				calendar2.add(calendar2.SECOND, 5);
				date = calendar2.getTime();

				context.setAttribute(userIdStr, date);

				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * pc2.0
	 * 
	 * @return
	 */
	protected void setAttr(String objectStr, Object object) {
		request.getSession().setAttribute(objectStr, object);
	}

	/**
	 * 获取Session中的用户对象
	 * 
	 * @return
	 */
	protected User getSessionUser() {
		User user = (User) session.get(Constant.SESSION_USER);
		return user;
	}

	/**
	 * 获取Session中的用户对象的ID
	 * 
	 * @return
	 */
	protected long getSessionUserId() {
		User user = (User) session.get(Constant.SESSION_USER);
		return user.getUserId();
	}

	/**
	 * 获取Session中的用户对象
	 * 
	 * @return
	 */
	protected String getSessionUserName() {
		User user = (User) session.get(Constant.SESSION_USER);
		return user.getUserName();
	}

	/**
	 * 获取Session中的用户认证信息
	 * 
	 * @return
	 */
	protected UserIdentify getSessionUserIdentify() {
		UserIdentify userdiIdentify = (UserIdentify) session
				.get(Constant.SESSION_USER_IDENTIFY);
		return userdiIdentify;
	}

	/**
	 * 封装获取Session中的用户对象
	 * 
	 * @return
	 */
	protected Operator getOperator() {
		return (Operator) session.get(Constant.SESSION_OPERATOR);
	}

	/**
	 * 封装获取Session中的用户对象的用户名
	 * 
	 * @return
	 */
	protected String getOperatorUserName() {
		Operator operator = (Operator) session.get(Constant.SESSION_OPERATOR);
		return operator.getUserName();
	}

	/**
	 * 封装获取Session中的用户对象的ID
	 * 
	 * @return
	 */
	protected long getOperatorId() {
		Operator operator = (Operator) session.get(Constant.SESSION_OPERATOR);
		if (operator == null)
			return 49l;
		return operator.getId();
	}

	public boolean isSession() {
		User sessionUser = this.getSessionUser();
		if (sessionUser == null)
			return false;
		return true;
	}

	/**
	 * 重定向
	 * 
	 * @param url
	 */
	protected void redirect(String url) {
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 前台重定向
	 * 
	 * @param url
	 */
	protected void frontRedirect(String url) {
		try {
			response.sendRedirect(Global.getValue("weburl") + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * RSA 公钥的Modulus与PublicExponent的hex编码形式
	 * 
	 * @throws Exception
	 */
	protected void initRSAME() throws Exception {
		RsaFormEncryptRuleCheck rsaFormEncrypt = (RsaFormEncryptRuleCheck) Global
				.getRuleCheck("rsaFormEncrypt");
		if (rsaFormEncrypt.enable_formEncrypt) {
			RSAPublicKey rsap;
			rsap = (RSAPublicKey) RSAUtil.getKeyPair().getPublic();
			String module = rsap.getModulus().toString(16);
			String empoent = rsap.getPublicExponent().toString(16);
			request.setAttribute("m", module);
			request.setAttribute("e", empoent);
		}
		request.setAttribute("rsaFormEncrypt", rsaFormEncrypt);
	}

	/**
	 * 是否开启托管
	 * 
	 * @return
	 */
	protected boolean isOpenApi() {
		return BaseTPPWay.isOpenApi();
	}

	/**
	 * 得到开启托管类型
	 * 
	 * @return
	 */
	protected int apiCode() {
		return BaseTPPWay.API_CODE;
	}

	// TGPROJECT-376 满标手机验证合同是否同意 2014-7-22 end
	/**
	 * 回调参数拼接共用方法
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getRequestParams() {
		String params = "";
		try {
			Enumeration e = (Enumeration) request.getParameterNames();
			while (e.hasMoreElements()) {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				params += parName + "=" + value + "&";
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return params;
	}

	/**
	 * 文件下载
	 * 
	 * @param inFile
	 * @param downloadFile
	 * @throws IOException
	 */
	public void generateDownloadFile(String inFile, String downloadFile)
			throws IOException {
		InputStream ins = new BufferedInputStream(new FileInputStream(inFile));
		byte[] buffer = new byte[ins.available()];
		ins.read(buffer);
		ins.close();
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(downloadFile.getBytes("GBK"), "ISO_8859_1"));
		response.addHeader("Content-Length", "" + new File(inFile).length());
		OutputStream ous = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream;charset=GBK");
		ous.write(buffer);
		ous.flush();
		ous.close();
	}

}
