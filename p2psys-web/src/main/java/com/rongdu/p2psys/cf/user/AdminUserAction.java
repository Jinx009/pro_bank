package com.rongdu.p2psys.cf.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.util.HttpData;
import com.rongdu.p2psys.util.HttpUtil;

public class AdminUserAction extends BaseAction<UserModel> implements ModelDriven<UserModel>{

	@Resource
	private UserService theUserService;
	@Resource
	private NoticeService noticeService;
	
	private Map<String,Object> data;
	
	/**
	 * 造用户操作
	 * @throws Exception 
	 */
	@Action(value = "/cf/user/create")
	public void create() throws Exception{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,400);
		String userName = paramString("userName");
		String realName = paramString("realName");
		User user2 = getNBSessionUser();
		if(null!=user2){
			if("13167262228"==user2.getUserName()||"18217700275"==user2.getUserName()||"15921623099"==user2.getUserName()){
				data.put(ConstantUtil.DATA,"该用户无权限操作！");
			}else{
				if (StringUtil.isPhone(userName)) {
					User user = theUserService.getByUserName(userName);
					if (null != user) {
						data.put(ConstantUtil.DATA,"用户"+userName+"已经存在！");
					} else {
						user = new User();
						user.setMobilePhone(userName);
						user.setUserName(userName);
						user.setPwd(MD5.encode("zf888888"));
						user.setAddTime(new Date());
						user = theUserService.savePcUser(user, "");
						user.setBindState(3);
						user.setWeiboId(user2.getWeiboId());
						theUserService.updateUser(user);
						String registerUrl = HttpData.registerUrl(userName,"zf888888");
						HttpUtil.postParams(registerUrl);
						sendNotice(userName, realName);
						data.put(ConstantUtil.CODE,200);
						data.put(ConstantUtil.DATA,"用户"+userName+ "注册成功!");
					}
				} else {
					data.put(ConstantUtil.DATA, "手机号码格式不正确!");
				}
			}
		}else{
			data.put(ConstantUtil.DATA,"该用户无权限操作！");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 造用户页面
	 * @return
	 */
	@Action(value = "/cf/user/createUser",results={ 
			@Result(name = "create",type="ftl", location = "/nb/cf/user/create.html")})
	public String createPage(){
		return "create";
	}
	
	private void sendNotice(String mobilePhone,String realName){
		Map<String, Object> sendData = new HashMap<String, Object>();
		sendData.put("addTime",new Date());
		sendData.put("realName",mobilePhone);
		Notice sms = new Notice();
		sms.setType(NoticeConstant.NOTICE_SMS + "");
		sms.setReceiveAddr(mobilePhone);
		sms.setNid("cf_register");   
		sms.setContent("尊敬的"+realName+"先生/女士，您已成功注册”800众服“，登录账号为："+mobilePhone+"初始登陆密码为zf888888，为了您的账户安全，请尽快登陆修改初始登录密码，"+String.valueOf("http://t.cn/Rqjt6jx"));
		noticeService.sendNotice(sms);
	}
}
