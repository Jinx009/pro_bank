package com.rongdu.manage.action.payment;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.model.SupportBankModel;
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;

public class SupportBankAction extends BaseAction<SupportBankModel> implements ModelDriven<SupportBankModel> {

	@Resource
	private ISupportBankService theSupportBankService;
	
	private Map<String, Object> data;
	
	private File upload;
	
    private String uploadFileName;  
    
    private String uploadContentType; 
    
    private SupportBankModel model = new SupportBankModel();

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

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	@Action(value="/modules/payment/supportBankManager")
	public String supportBankManager(){
		return "supportBankManager";
	}
	
	@Action(value="/modules/payment/supportBankAdd")
	public String supportBankAdd(){
		return "supportBankAdd";
	}
	
	/**
	 * 分页展示
	 * @throws IOException
	 */
	@Action(value="/modules/payment/supportBankList")
	public void getSupportBankList() throws IOException{
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String searchName = paramString("searchName");

		PageDataList<SupportBankModel> recordList = theSupportBankService.findSupportBankByItem(pageNumber, pageSize, searchName);
		data.put("total",recordList.getPage().getTotal()); // 总行数
		data.put("rows", recordList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 编辑-页面
	 * @return
	 */
	@Action(value="/modules/payment/supportBankEdit")
	public String editSupportBank(){
		long id = paramLong("id");
		SupportBankModel model = theSupportBankService.loadSupportBankById(id);
		request.setAttribute("supportBankModel", model);
		return "supportBankEdit";
	}
	
	/**
	 * 编辑-处理
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/supportBankUpdate")
	public void updateSupportBank() throws IOException{
		data = new HashMap<String, Object>();
		theSupportBankService.updateSupportBank(model);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 通道添加
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/supportBankSave")
	public void saveSupportBank() throws IOException{
		data = new HashMap<String, Object>();
		theSupportBankService.saveSupportBank(model);
		data.put("result", true);
		data.put("msg", "添加成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/modules/payment/supportBankDelete")
	public void deleteSupportBank() throws IOException{
		data = new HashMap<String, Object>();
		long id = this.paramLong("id");
		theSupportBankService.deleteSupportBankById(id);
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 文件上传
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String getFilePath(File file) throws IOException{
		String realPath = "";
        try {  
            //服务器的路径  
//        	String path = ServletActionContext.getServletContext().getRealPath("/SupportBankLogo"); 
        	String path = "D:\\SupportBankLogo";
        	realPath = path+"\\"+uploadFileName; 
            if (file != null) {              
                File savefile = new File(new File(path), uploadFileName); 
                if (!savefile.getParentFile().exists()) 
                    savefile.getParentFile().mkdirs();
                FileUtils.copyFile(file, savefile);           
                ActionContext.getContext().put("message", "文件上传成功");          
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        return realPath;
	}

	

	public SupportBankModel getModel() {
		return model;
	}

	public void setModel(SupportBankModel model) {
		this.model = model;
	}
}
