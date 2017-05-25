package com.rongdu.p2psys.pc.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;


/**
 * 用户体验金信息
 * 
 * @author cgw
 * @version 1.0
 * @since 2015年7月23日
 */
public class ExperienceGoldAction extends BaseAction<ExperienceGoldModel> implements ModelDriven<ExperienceGoldModel> {

	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private UserService theUserService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private PpfundService thePpfundService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private AccountService theAccountService;
	
	private User user;
	private Map<String, Object> data;
	
	/**
	 * 我的体验金
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/egList",results = { @Result(name = "gold", type = "ftl", location = "/nb/pc/experienceGold/experienceGold.html")})
	public String egList() throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		request.setAttribute("randTime", sim.format(new Date()));
		return "gold";
	}
	
	/**
	 * 当前用户体验金信息
	 * @throws IOException 
	 */
	@Action("/nb/pc/goldInfo")
	public void goldInfo() throws IOException {
		data = new HashMap<String, Object>();
		/**
		 *如果已经登陆 
		 */
		if(hasSessionUser()){
			user = getNBSessionUser();
			/*List<ExperienceGold> egList = theExperienceGoldService.getExperienceGoldByUserId(user.getUserId());
			if(egList!=null && egList.size()>0){
				data.put("egModel", egList.get(0));
			}else{
				data.put("egModel", 0);
			}
			
			List<ProductBasic> prodList = productBasicService.getExperienceProductList();
			if(prodList!=null && prodList.size()>0){
				data.put("pid", prodList.get(0).getId());
				ProductBasic pb = prodList.get(0);
				Ppfund pf = thePpfundService.getById(pb.getRelatedId());
				Long id = pf.getId();
				Long flagId = pb.getProductTypeFlag().getId();
				String egUrl = "/nb/pc/product/ppfund_detail.html?productId="+pb.getId()+"&flagId="+flagId+"&ppfundId="+id;
				data.put("egUrl", egUrl);
			}*/
			boolean flag = theExperienceGoldService.getCanExperienceGold(user);
			data.put("egFlag", flag);
		
			List<AccountBank> ablist = theAccountBankService.list(user.getUserId());
			if(ablist==null || ablist.size()<=0){
				//该用户未绑卡，是否线下充值认证？
				UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				Account account = theAccountService.getAccountByUserId(user.getUserId());
				if(userIdentify.getRealNameStatus() == 1 || account.getTotal()>0){
					data.put("isBindC", 1);
				}else{
					data.put("isBindC", 0);
				}
			}else{
				data.put("isBindC", 1);
			}
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
}