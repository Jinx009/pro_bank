package com.rongdu.manage.action.finance;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.ImageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.FinanceArticle;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceArticleModel;
import com.rongdu.p2psys.core.service.FinanceArticleService;
import com.rongdu.p2psys.core.service.FinanceSiteService;
import com.rongdu.p2psys.core.web.BaseAction;

public class ManageFinanceArticleAction extends BaseAction<FinanceArticleModel> implements ModelDriven<FinanceArticleModel> {

	@Resource
	private FinanceArticleService financeArticleService;
	@Resource
	private FinanceSiteService financeSiteService;
	
	private Map<String, Object> data;
	
	private File wechatFile;

	private String wechatFileName;
	
	public File getWechatFile() {
		return wechatFile;
	}

	public void setWechatFile(File wechatFile) {
		this.wechatFile = wechatFile;
	}

	public String getWechatFileName() {
		return wechatFileName;
	}

	public void setWechatFileName(String wechatFileName) {
		this.wechatFileName = wechatFileName;
	}

	/**
	 * 理财商学院文章列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleManager")
	public String financeArticleManager() throws Exception {
		return "financeArticleManager";
	}
	
	/**
	 * 理财商学院文章列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleList")
	public void financeArticleList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<FinanceArticleModel> financeArticleList = financeArticleService.financeArticleList(pageNumber, pageSize, model);
		data.put("total", financeArticleList.getPage().getTotal()); //总行数
		data.put("rows", financeArticleList.getList()); //集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 添加理财商学院文章页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleAddPage")
	public String financeArticleAddPage() throws Exception {
		saveToken("financeArticleAddToken");
		List<FinanceSite> financeSiteList = financeSiteService.getFinanceSiteByStatus();
		request.setAttribute("financeSiteList", financeSiteList);
		List<FinanceArticleExpert> financeArticleExpertList = financeArticleService.getFinanceArticleExpertByStatus();
		request.setAttribute("financeArticleExpertList", financeArticleExpertList);
		return "financeArticleAddPage";
	}
	
	/**
	 * 添加理财商学院文章
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleAdd")
	public void financeArticleAdd() throws Exception {
		data = new HashMap<String, Object>();
		checkToken("financeArticleAddToken");
		FinanceArticle financeArticle = model.prototype();
		String picPath = imgUpload();
		if (picPath != null) {
			financeArticle.setPicPath(picPath);
		}
		long financeSiteId = paramLong("financeSiteId");
		if(financeSiteId <= 0){
			throw new BussinessException("请选择文章所属栏目，如果没有栏目，请到栏目管理中添加！", BussinessException.TYPE_JSON);
		}
		financeArticle.setFinanceSite(financeSiteService.find(financeSiteId));
		long financeArticleExpertId = paramLong("financeArticleExpertId");
		if(financeArticleExpertId <= 0){
			throw new BussinessException("请选择文章所属作者，如果没有作者，请到文章作者管理中添加！", BussinessException.TYPE_JSON);
		}
		financeArticle.setFinanceArticleExpert(financeArticleService.getFinanceArticleExpertById(financeArticleExpertId));
		financeArticle.setAddTime(new Date());
		financeArticle.setAddIp(Global.getIP());
		financeArticleService.financeArticleAdd(financeArticle);
		data.put("result", true);
		data.put("msg", "添加商学院文章成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 修改理财商学院文章页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleEditPage")
	public String financeArticleEditPage() throws Exception {
		long id = paramLong("id");
		FinanceArticle financeArticle = financeArticleService.find(id);
		List<FinanceSite> financeSiteList = financeSiteService.getFinanceSiteByStatus();
		List<FinanceArticleExpert> financeArticleExpertList = financeArticleService.getFinanceArticleExpertByStatus();
		request.setAttribute("financeArticle", financeArticle);
		request.setAttribute("financeSiteList", financeSiteList);
		request.setAttribute("financeArticleExpertList", financeArticleExpertList);
		return "financeArticleEditPage";
	}

	/**
	 * 修改理财商学院文章
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleEdit")
	public void financeArticleEdit() throws Exception {
		data = new HashMap<String, Object>();
		FinanceArticle financeArticle = model.prototype();
		long financeSiteId = paramLong("financeSiteId");
		financeArticle.setFinanceSite(financeSiteService.find(financeSiteId));
		long financeArticleExpertId = paramLong("financeArticleExpertId");
		financeArticle.setFinanceArticleExpert(financeArticleService.getFinanceArticleExpertById(financeArticleExpertId));
		String picPath = imgUpload();
		if (picPath != null) {
			financeArticle.setPicPath(picPath);
		}
		financeArticleService.financeArticleEdit(financeArticle);
		data.put("result", true);
		data.put("msg", "修改理财商学院文章成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除理财商学院文章
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleDelete")
	public void financeArticleDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		FinanceArticle financeArticle = financeArticleService.find(id);
		financeArticle.setIsDelete((byte) 1);
		financeArticleService.financeArticleEdit(financeArticle);
		data.put("result", true);
		data.put("msg", "删除理财商学院文章成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 文章作者页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertManager")
	public String financeArticleExpertManager() throws Exception {
		return "financeArticleExpertManager";
	}
	
	/**
	 *  文章作者列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertList")
	public void financeArticleExpertList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<FinanceArticleExpert> financeArticleExpertList = financeArticleService.financeArticleExpertList(pageNumber, pageSize, model);
		data.put("total", financeArticleExpertList.getPage().getTotal()); //总行数
		data.put("rows", financeArticleExpertList.getList()); //集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 添加文章作者页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertAddPage")
	public String financeArticleExpertAddPage() throws Exception {
		saveToken("financeArticleExpertAddToken");
		return "financeArticleExpertAddPage";
	}
	
	/**
	 * 添加文章作者
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertAdd")
	public void financeArticleExpertAdd() throws Exception {
		data = new HashMap<String, Object>();
		//checkToken("financeArticleExpertAddToken");
		FinanceArticleExpert financeArticleExpert = new FinanceArticleExpert();
		String autorName = paramString("autorName");
		financeArticleExpert.setAutorName(autorName);
		String position = paramString("position");
		financeArticleExpert.setPosition(position);
		String picPath = imgUpload();
		if (picPath != null) {
			financeArticleExpert.setPicPath(picPath);
		}
		String wechatPath = wechatImgUpload();
		if(wechatPath != null) {
			financeArticleExpert.setWechatPath(wechatPath);
		}
		financeArticleExpert.setBlogUrl(paramString("blogUrl"));
		financeArticleExpert.setContent(paramString("content"));
		financeArticleExpert.setAddTime(new Date());
		financeArticleExpert.setAddIp(Global.getIP());
		financeArticleService.financeArticleExpertAdd(financeArticleExpert);
		data.put("result", true);
		data.put("msg", "添加文章作者成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	public String wechatImgUpload() throws Exception{
		String picPath = "";
		if (wechatFile != null) {
			if (!ImageUtil.fileIsImage(wechatFile)) {
				throw new BussinessException("您上传的图片无效，请重新上传！", BussinessException.TYPE_JSON);
			} else {
				Date d = new Date();
				String upfiesDir = ServletActionContext.getServletContext().getRealPath("/data/upfiles/images/");
				String realPath = ServletActionContext.getServletContext().getRealPath("");
				String destFileName = upfiesDir + DateUtil.dateStr2(d) + "/"  
						+ entityClass.getSimpleName() + "/" + UUID.randomUUID() + ".jpg";
				File imageFile = new File(destFileName);
				FileUtils.copyFile(wechatFile, imageFile);
				picPath = destFileName.replace(realPath, "").replace(File.separator, "/");
			}
		}
		return picPath;
	}
	/**
	 * 修改文章作者页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertEditPage")
	public String financeArticleExpertEditPage() throws Exception {
		long id = paramLong("id");
		FinanceArticleExpert financeArticleExpert = financeArticleService.getFinanceArticleExpertById(id);
		request.setAttribute("financeArticleExpert", financeArticleExpert);
		return "financeArticleExpertEditPage";
	}

	/**
	 * 修改文章作者
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertEdit")
	public void financeArticleExpertEdit() throws Exception {
		data = new HashMap<String, Object>();
		FinanceArticleExpert financeArticleExpert = new FinanceArticleExpert();
		String autorName = paramString("autorName");
		financeArticleExpert.setAutorName(autorName);
		String position = paramString("position");
		financeArticleExpert.setPosition(position);
		String picPath = imgUpload();
		if (picPath != null) {
			financeArticleExpert.setPicPath(picPath);
		}else{
			picPath = paramString("picPath");
			financeArticleExpert.setPicPath(picPath);
		}
		String wechatPath = wechatImgUpload();
		if (StringUtil.isNotBlank(wechatPath)) {
			financeArticleExpert.setWechatPath(wechatPath);
		} else {
			wechatPath = paramString("wechatPath");
			financeArticleExpert.setWechatPath(wechatPath);
		}
		financeArticleExpert.setBlogUrl(paramString("blogUrl"));
		financeArticleExpert.setContent(paramString("content"));
		financeArticleExpert.setAddTime(new Date());
		financeArticleExpert.setAddIp(Global.getIP());
		financeArticleExpert.setId(paramLong("id"));
		financeArticleService.financeArticleExpertEdit(financeArticleExpert);
		data.put("result", true);
		data.put("msg", "修改文章作者成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除文章作者
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/article/financeArticleExpertDelete")
	public void financeArticleExpertDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		FinanceArticleExpert financeArticleExpert = financeArticleService.getFinanceArticleExpertById(id);
		financeArticleExpert.setIsDelete(1);
		financeArticleService.financeArticleExpertEdit(financeArticleExpert);
		data.put("result", true);
		data.put("msg", "删除文章作者成功！");
		printJson(getStringOfJpaObj(data));
	}
}
