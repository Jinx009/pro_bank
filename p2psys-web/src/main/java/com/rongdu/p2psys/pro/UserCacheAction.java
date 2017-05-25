package com.rongdu.p2psys.pro;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;

/**
 * 后台 控制中心
 * @author Jinx
 *
 */
public class UserCacheAction extends BaseAction<UserCache> implements ModelDriven<UserCache>{

	@Resource
	private UserCacheService theUserCahceService;
	
	private Map<String,Object> data;

	/**
	 * 后台身份验证首页
	 * @return
	 */
	@Action(value = "/manage/code/index",results = {@Result(name = "index", type = "ftl", location = "/nb/pro/dream.html")})
	public String manageIndex(){
		return "index";
	}
	
	/**
	 * 用户项目方认证状态
	 * @throws IOException
	 */
	@Action(value="/manage/approvalStatus")
	public void approvalStatus() throws IOException{
		User user = getNBSessionUser();
		UserCache userCache = theUserCahceService.getByUserId(user.getUserId());
		data = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG, "用户项目方相关信息");
		map.put("idCardPicPositive",userCache.getIdCardPicPositive());//身份证正面路径
		map.put("idCardPicOther",userCache.getIdCardPicOther());//身份证背面照片路径
		map.put("approvalStatus",userCache.getApprovalStatus());//项目发起人状态
		map.put("businessLicense",userCache.getBusinessLicense());//申请人营业执照
		map.put("userIdentity",userCache.getUserIdentity());
		data.put(ConstantUtil.DATA,map);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 保存用户申请条件
	 * @throws IOException
	 */
	@Action(value="/manage/saveCache")
	public void save() throws IOException{
		User user = getNBSessionUser();
		UserCache userCache = theUserCahceService.getByUserId(user.getUserId());
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG,"保存用户申请项目方资料");
		
		String idCardPicPositive = paramString("idCardPicPositive");
		String idCardPicOther = paramString("idCardPicOther");
		String businessLicense = paramString("businessLicense");
		String userIdentity = paramString("userIdentity");
		userCache.setIdCardPicOther(idCardPicOther);
		userCache.setIdCardPicPositive(idCardPicPositive);
		userCache.setBusinessLicense(businessLicense);
		userCache.setUserIdentity(userIdentity);
		userCache.setApprovalStatus(-1);
		theUserCahceService.updateUserCache(userCache);
		
		data.put(ConstantUtil.DATA,"保存成功！");
		user.setUserCache(userCache);
		setAttr(ConstantUtil.SESSION_USER,user);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 更新用户申请项目方资料
	 */
	@Action("/manage/code/updateCache")
	public void update(){
		User user = getNBSessionUser();
		UserCache userCache = theUserCahceService.getByUserId(user.getUserId());
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG,"更新用户项目人申请资料");
		
		String idCardPicPositive = paramString("idCardPicPositive");
		String idCardPicOther = paramString("idCardPicOther");
		String businessLicense = paramString("businessLicense");
		String userIdentity = paramString("userIdentity");
		userCache.setIdCardPicOther(idCardPicOther);
		userCache.setIdCardPicPositive(idCardPicPositive);
		userCache.setBusinessLicense(businessLicense);
		userCache.setUserIdentity(userIdentity);
		theUserCahceService.updateUserCache(userCache);
		
		data.put(ConstantUtil.DATA,"修改成功！");
		user.setUserCache(userCache);
		setAttr(ConstantUtil.SESSION_USER,user);
	}
	
	
	
	
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
