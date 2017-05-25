package com.rongdu.manage.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

public class UploaderAction {

	private static final Logger log = Logger.getLogger(UploaderAction.class);
	private String url = "";

	private String fileName = "";

	private String fieldName = "";
	
	private String fileExt = "";

	private String state = "";

	private String type = "";

	private String originalName = "";

	private String size = "";

	private HttpServletRequest request = null;

	private String title = "";

	private String savePath = "";

	private String fileTypeCode = "";

	private long tmpId;
	
	private String[] allowFiles = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };

	private int maxSize = 10000;

	private HashMap<String, String> errorInfo = new HashMap<String, String>();
	
	public UploaderAction(HttpServletRequest request) {
		this.request = request;
		HashMap<String, String> tmp = this.errorInfo;
		tmp.put("SUCCESS", "SUCCESS");
		tmp.put("NOFILE", "未包含文件上传域");
		tmp.put("TYPE", "不允许的文件格式");
		tmp.put("SIZE", "文件大小超出限制");
		tmp.put("ENTYPE", "请求类型ENTYPE错误");
		tmp.put("REQUEST", "上传请求异常");
		tmp.put("IO", "IO异常");
		tmp.put("DIR", "目录创建失败");
		tmp.put("UNKNOWN", "未知错误");
		tmp.put("LIMITCONTENT", "文件内容受限");
	}

	public void upload() throws Exception {
		try {
			boolean isMultipart = ServletFileUpload
					.isMultipartContent(this.request);
			if (!isMultipart) {
				this.state = ((String) this.errorInfo.get("NOFILE"));
				return;
			}
			DiskFileItemFactory dff = new DiskFileItemFactory();
			String savePath = getFolder(this.savePath);
			dff.setRepository(new File(savePath));
			ServletFileUpload sfu = new ServletFileUpload(dff);
			sfu.setSizeMax(this.maxSize * 1024);
			sfu.setHeaderEncoding("UTF-8");
			FileItemIterator fii = sfu.getItemIterator(this.request);
			while (fii.hasNext()) {
				FileItemStream fis = fii.next();
				if (!fis.isFormField()) {
					this.originalName = fis.getName().substring(
							fis.getName().lastIndexOf(
									System.getProperty("file.separator")) + 1);
					if (!checkFileType(this.originalName)) {
						this.state = ((String) this.errorInfo.get("TYPE"));
						break;
					}
					this.fileName = getName(this.originalName);
					this.fileExt = getFileExt(this.fileName);
					this.type = this.fileExt.substring(this.fileExt.lastIndexOf(".")+1);
					this.url = (savePath + "/" + this.fileName);
					this.title = fileName;
					File tmpFile = new File(getPhysicalPath(this.url));
					BufferedInputStream in = new BufferedInputStream(
							fis.openStream());
					FileOutputStream out = new FileOutputStream(tmpFile);
					BufferedOutputStream output = new BufferedOutputStream(out);
					Streams.copy(in, output, true);
					this.state = ((String) this.errorInfo.get("SUCCESS"));
				} else {
					String fname = fis.getFieldName();

					if (!fname.equals("pictitle")) {
						continue;
					}
					BufferedInputStream in = new BufferedInputStream(
							fis.openStream());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuffer result = new StringBuffer();
					while (reader.ready()) {
						result.append((char) reader.read());
					}
					this.title = new String(result.toString().getBytes(),
							"utf-8");
					reader.close();
				}
			}
		} catch (FileUploadBase.SizeLimitExceededException e) {
			this.state = ((String) this.errorInfo.get("SIZE"));
			log.error("SizeLimitExceededException",e);
		} catch (FileUploadBase.InvalidContentTypeException e) {
			this.state = ((String) this.errorInfo.get("ENTYPE"));
			log.error("InvalidContentTypeException",e);
		} catch (FileUploadException e) {
			this.state = ((String) this.errorInfo.get("REQUEST"));
			log.error("FileUploadException",e);
		} catch (Exception e) {
			e.printStackTrace();
			this.state = ((String) this.errorInfo.get("UNKNOWN"));
			log.error("Exception",e);
		}
	}

	private boolean checkFileType(String fileName) {
		Iterator type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = (String) type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private String getName(String fileName) {
		Random random = new Random();
		return this.fileName = random.nextInt(10000)
				+ System.currentTimeMillis() + getFileExt(fileName);
	}

	private String getFolder(String path) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		path = path + "/" + formater.format(new Date());
		File dir = new File(getPhysicalPath(path));
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				this.state = ((String) this.errorInfo.get("DIR"));
				return "";
			}
		}
		return path;
	}
	
	@Action("/plugins/ueditor/dialogs/image/image")
	public String image(){
		return "image";
	}

	private String getPhysicalPath(String path) {
		String servletPath = this.request.getServletPath();
		String realPath = this.request.getSession().getServletContext()
				.getRealPath(servletPath);
		return new File(realPath).getParent() + "/" + path;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public void setMaxSize(int size) {
		this.maxSize = size;
	}

	public String getSize() {
		return this.size;
	}

	public String getUrl() {
		return this.url;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getState() {
		return this.state;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getOriginalName() {
		return this.originalName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileTypeCode() {
		return fileTypeCode;
	}

	public void setFileTypeCode(String fileTypeCode) {
		this.fileTypeCode = fileTypeCode;
	}

	public long getTmpId() {
		return tmpId;
	}

	public void setTmpId(long tmpId) {
		this.tmpId = tmpId;
	}

}
