package com.rongdu.manage.action.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.mail.Mail;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserUpload;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserUploadService;

/**
 * 
 * @author zf
 * @version 2.0
 * @since 2014年6月5日
 */
public class ManageUserCacheAction extends BaseAction<UserCache> implements ModelDriven<UserCache> {
    @Resource
    private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserUploadService userUploadService;

	private Map<String, Object> data;
	
	private long[] delIds;

	public long[] getDelIds() {
		return delIds;
	}

	public void setDelIds(long[] delIds) {
		this.delIds = delIds;
	}

	/**
     * 获得后台开户清单页面
     * @return String
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminManager")
    public String userAdminManager() throws Exception {
    	if (model.getUserNature() == 3){ //担保公司
    		return "userVouchManager";
    	} else if(model.getUserNature() == 1){//普通用户
    		return "userOrdinaryManager";
    	}else { //企业用户
            return "userAdminManager";
    	}
    }

    /**
     * 获得后台开户清单列表
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminList")
    public void userAdminList() throws Exception {
        data = new HashMap<String, Object>();
        int pageNumber = paramInt("page"); // 当前页数
        int pageSize = paramInt("rows"); // 每页总数
        String status = request.getParameter("status");
        if (status == null) {
        	model.setStatus(99);
        }
        PageDataList<UserCacheModel> pagaDataList = userCacheService.userList(pageNumber, pageSize, model);
        int totalPage = pagaDataList.getPage().getTotal(); // 总页数
        data.put("total", totalPage);
        data.put("rows", pagaDataList.getList());
        printJson(getStringOfJpaObj(data));
    }
    
    /**
     * 添加用户页面
     * @return String
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminAddPage")
    public String userAdminAddPage() throws Exception {
    	if (model.getId() == 3){ //担保公司
    		return "userVouchAddPage";
    	} else if(model.getId() == 1){
    		return "userOrdinaryAddPage";
    	}else { //企业用户
    		return "userAdminAddPage"; 
    	}
    }

    /**
     * 添加企业用户
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminAdd")
    public void userAdminAdd() throws Exception {
        data = new HashMap<String, Object>();
        try {
        	List<UserUpload> list = new ArrayList<UserUpload>();
        	if (request.getParameter("fileIndex") != null) {
        		String[] picPath = imgsUpload();
        		if (model.getCompanyType() == 4) {
        			if (picPath[0] == null) {
                    	throw new RuntimeException("经营执照必须上传");
                	}
        		} else {
        			if (picPath[0] == null || picPath[1] == null || picPath[2] == null) {
                    	throw new RuntimeException("经营执照，税务登记证，组织结构证都必须上传");
                	}
        		}
                model.setJyzzPicPath(picPath[0]);
                model.setSwdjPicPath(picPath[1]);
                model.setZzjgPicPath(picPath[2]);
                model.setKhxkPicPath(picPath[3]);
                model.setDkkPicPath(picPath[4]);
                model.setGrzxPicPath(picPath[5]);
                String[] picPaths = request.getParameterValues("picPath");
    			if (picPaths == null) {
    				throw new BussinessException("必须上传至少一张公司照！", 1);
    			}
    			for (String path : picPaths) {
    				UserUpload uu = new UserUpload();
    				uu.setPicPath(path);
    				list.add(uu);
    			}
        	}
            userCacheService.save(model, list);
            printResult("添加成功", true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 后台添加普通用户
     * @throws Exception
     */
    @Action(value = "/modules/user/user/user/userAdd")
    public void userAdd() throws Exception{
    	data = new HashMap<String, Object>();
    	try {
    		//校验用户名是否被使用
    		int u_count = userService.countByUserName(paramString("userName"));
    		if(u_count > 0){
    			printWebResult("用户名已被使用", false);
    			return;
    		}
    		//校验手机号是否被使用
    		int p_count = userService.countByMobilePhone(paramString("mobilePhone"));
    		if(p_count > 0){
    			printWebResult("该手机号已存在", false);
    			return;
    		}
    		User user = new User();
    		user.setUserName(paramString("userName"));
    		user.setMobilePhone(paramString("mobilePhone"));
    		if(!StringUtil.isBlank(paramString("realName"))){
    			user.setRealName(paramString("realName"));
    		}
	    	//生成随机8位密码
	    	String password = StringUtil.getRandomString(8);
	    	user.setPwd(password);
	    	UserModel userModel = UserModel.instance(user);
	    	userModel.setUserType(model.getUserType());
	    	user = userService.doRegister(userModel);
	    	if(user != null){
	    		//发送密码至用户
	    		Global.setTransfer("password", password);
	    		Global.setTransfer("user", user);
	    		AbstractExecuter executer = ExecuterHelper.doExecuter("userRegisterPwdExecuter");
	    		executer.execute(0, user);
	    	}
	    	printResult("添加成功", true);
    	} catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 锁定用户页面
     * 
     * @return String
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminLockPage")
    public String userAdminLockPage() throws Exception {
        UserCache userCache = userCacheService.findById(model.getId());
        request.setAttribute("userCache", userCache);
        return "userAdminLockPage";
    }

    /**
     * 锁定用户 1锁定 0未锁定
     * 
     * @throws Exception if has error
     */
	@Action(value = "/modules/user/user/user/userAdminLock")
    public void userAdminLock() throws Exception {
        data = new HashMap<String, Object>();
        Date date = new Date();
        UserCache userCache =  userCacheService.findById(model.getId());
        userCache.setStatus(model.getStatus());
        userCache.setRemark(model.getRemark());
        userCache.setLoginFailTimes(0);
        userCache.setLockTime(date);
        userCacheService.update(userCache);
        printResult("操作成功", true);
    }
    
    /**
     * 修改用户页面
     * 
     * @return String
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminEditPage")
    public String userAdminEditPage() throws Exception {
        UserCache userCache = userCacheService.findById(model.getId());
        request.setAttribute("userCache", userCache);
        if (model.getUserNature() == 3){ //担保公司
        	return "userVouchEditPage";
        } else if(model.getUserNature() == 1){//普通用户
        	return "usermessageEditPage";
        }else {
        	return "userAdminEditPage";
        }
    }

    /**
     * 修改用户 
     * 
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/userAdminEdit")
    public void userAdminEdit() throws Exception {
        data = new HashMap<String, Object>();
        try {
            UserCache userCache =  userCacheService.findById(model.getId());
            User user = userCache.getUser();
            List<UserUpload> list = new ArrayList<UserUpload>();
        	if (request.getParameter("fileIndex") != null) {
	            String[] picPath = imgsUpload();
	            if (picPath[0] != null) {
	            	userCache.setJyzzPicPath(picPath[0]);
	        	}
	            if (picPath[1] != null) {
	            	userCache.setSwdjPicPath(picPath[1]);
	            }
	            if (picPath[2] != null) {
	            	userCache.setZzjgPicPath(picPath[2]);
	            }
	            if (picPath[3] != null) {
	            	userCache.setKhxkPicPath(picPath[3]);
	            }
	            if (picPath[4] != null) {
	            	userCache.setDkkPicPath(picPath[4]);
	            }
	            if (picPath[5] != null) {
	            	userCache.setGrzxPicPath(picPath[5]);
	            }
				String[] picPaths = request.getParameterValues("picPath");
				if (picPaths != null) {
					for (String path : picPaths) {
						UserUpload uu = new UserUpload();
						uu.setPicPath(path);
						uu.setUser(user);
						list.add(uu);
					}
				}
        	}
            user.setRealName(model.getUser().getRealName());
            user.setMobilePhone(model.getUser().getMobilePhone());
            userCache.setUser(user);
            userCache.setProvince(model.getProvince());
            userCache.setCity(model.getCity());
            userCache.setArea(model.getArea());
            userCache.setCompanyName(model.getCompanyName());
            userCache.setCompanyType(model.getCompanyType());
            userCache.setCompanyRegNo(model.getCompanyRegNo());
            userCache.setTaxRegNo(model.getTaxRegNo());
            userCache.setAddress(model.getAddress());
            userCache.setDescription(model.getDescription());
            userCache.setFrdbName(model.getFrdbName());
            userCache.setFrdbNo(model.getFrdbNo());
            userCache.setRegCapital(model.getRegCapital());
            userCache.setZzjgCode(model.getZzjgCode());
            userCache.setCompanyEmail(model.getCompanyEmail());
            userCache.setCompanyPhone(model.getCompanyPhone());
            userCache.setCompanyFax(model.getCompanyFax());
            userCacheService.update(userCache, list, delIds);
            printResult("修改用户成功", true);
        } catch (Exception e) {
            throw new RuntimeException("修改用户异常", e);
        }
    }
    /**
     * 重新发送邮件
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/sendMail")
    public void sendMail() throws Exception {
    	User user = userCacheService.findById(model.getId()).getUser();
    	Global.setTransfer("activeUrl", "/user/doRegisterStep1.html?id=" + Mail.getInstance().getdecodeIdStr(user));
        Global.setTransfer("user", user);
        AbstractExecuter executer = ExecuterHelper.doExecuter("userRegisterExecuter");
        executer.execute(0, user);
    }
    /**
     * 获取后台开户用户实物照
     * @throws Exception if has error
     */
    @Action(value = "/modules/user/user/user/getUserPic")
    public void getUserPic() throws Exception {
    	User user = userService.getUserById(paramLong("id"));
    	List<UserUpload> list = userUploadService.findByUser(user);
    	data = new HashMap<String, Object>();
    	data.put("list", list);
		printJson(getStringOfJpaObj(data));
    }
}
