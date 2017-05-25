package com.rongdu.p2psys.web.borrow;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowBespeak;
import com.rongdu.p2psys.borrow.domain.BorrowBespeakPic;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.service.BorrowBespeakService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 预约借款
 * @author sj
 * @since 2014-8-20
 *
 */

@SuppressWarnings("rawtypes")
public class BorrowBespeakAction  extends BaseAction implements ModelDriven<BorrowBespeak> {

	@Resource
	private BorrowBespeakService borrowBespeakService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserCacheService userCacheService;
	
	private BorrowBespeak borrowBespeak = new BorrowBespeak();
	
	private Map<String, Object> data;
	
	@Override
	public BorrowBespeak getModel() {
		return borrowBespeak;
	}
	
	/**
	 * 预约借款页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/borrowBespeak/bespeak")
	public String bespeak() throws Exception {
		
		return "bespeak";
	}
	
	/**
	 * 预约借款操作
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/borrowBespeak/doBespeak")
	public void doBespeak() throws Exception {
		User user = userService.getUserByUserName(paramString("userName"));
		if(StringUtil.isBlank(paramString("userName"))){
			throw new BorrowException("请填写用户名！", 1);
		}
		if(user == null){
			throw new BorrowException("用户名不存在！", 1);
		}else {
			UserCache uc = userCacheService.findByUserId(user.getUserId());
			if(uc.getUserType() != 2){
				throw new BorrowException("投资人不能申请预约借款！", 1);
			}
			borrowBespeak.setUser(user);
		}
		
		if(borrowBespeak.getMoney() <= 0){
			throw new BorrowException("借款金额应大于零！", 1);
		}else if(StringUtil.isBlank(borrowBespeak.getBorrowUse())){
			throw new BorrowException("借款用途不能为空！", 1);
		}
		//获得上传图片字符串数组
		String[] picPaths = request.getParameterValues("picPath");
		if (picPaths == null) {
			throw new UserException("至少上传一张图片！", 1);
		}
		
		borrowBespeak = borrowBespeakService.doBespeak(borrowBespeak);
		
		List<BorrowBespeakPic> list = new ArrayList<BorrowBespeakPic>(); 
		for (String picPath : picPaths) {
			BorrowBespeakPic bbp = new BorrowBespeakPic();
			bbp.setBorrowBespeak(borrowBespeak);
			bbp.setPicPath(picPath);
			bbp.setAddTime(new Date());
			bbp.setAddIp(Global.getIP());
			list.add(bbp);
		}
		borrowBespeakService.addBorrowBespeakPic(list);
		printWebSuccess();
	}
	
	/**
	 * 上传图片
	 */
	@Action("/borrowBespeak/fileUpload")
	public void fileUpload() throws Exception {
		data = new HashMap<String, Object>();
		String path = imgUpload();
		data = new HashMap<String, Object>();
		data.put("picPath", path);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除上传的图片
	 * 
	 * @throws Exception
	 */
	@Action("/borrowBespeak/deletePic")
	public void deletePic() throws Exception {
		String pathPic = paramString("pathPic");
		String realPath = ServletActionContext.getServletContext().getRealPath(pathPic);
		new File(realPath).delete();
		printWebSuccess();
	}
	
}
