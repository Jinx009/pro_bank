package com.rongdu.p2psys.web.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.ppfund.domain.ExperienceGold;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserService;

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
	private UserService userService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private PpfundService thePpfundService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private AccountService theAccountService;
	
	private Map<String, Object> data;
	
	private User user;

	/**
	 * 我的体验金页面
	 * @return
	 * @throws Exception
	 */
	@Action("/member/experienceGold/experienceGold")
	public String experienceGold() {
		return "experienceGold";
	}
	
	/**
	 * 我的红包数据
	 * @throws IOException 
	 */
	@Action("/member/experienceGold/experienceGoldJSON")
	public void availableRedPacketJSON() throws IOException {
		model.setId(getSessionUserId());
		data = new HashMap<String, Object>();
		PageDataList<ExperienceGoldModel> pagaDataList = theExperienceGoldService.findByModel(model);
		int total = pagaDataList.getPage().getTotal();// 总记录数
		data.put("total", total);
		data.put("data", pagaDataList);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的体验金
	 * 
	 * @return
	 */
	@Action(value="/member/experienceGold/experienceGold",results = { @Result(name = "gold", type = "ftl", location = "/member/experienceGold/experienceGold.html")})
	public String egPage() throws Exception {
		user = getSessionUser();
		User u = userService.getUserById(user.getUserId());
		List<ExperienceGold> egList = theExperienceGoldService.getExperienceGoldByUserId(user.getUserId());
		request.setAttribute("egList", egList);
		List<ProductBasic> prodList = productBasicService.getExperienceProductList();
		if(prodList!=null && prodList.size()>0){
			ProductBasic pb = prodList.get(0);
			Ppfund pf = thePpfundService.getById(pb.getRelatedId());
			Long id = pf.getId();
			String startTime = pf.getStartTime();
			Long flagId = pb.getProductTypeFlag().getId();
			String egUrl = "/ppfund/detail.html?id="+id+"&startTime="+startTime+"&flagId="+flagId;
			request.setAttribute("egUrl", egUrl);
		}
		List<AccountBank> ablist = accountBankService.list(user.getUserId());
		if(ablist==null || ablist.size()<=0){
			//该用户未绑卡，是否线下充值认证？
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			Account account = theAccountService.getAccountByUserId(user.getUserId());
			if(userIdentify.getRealNameStatus() == 1 || account.getTotal()>0){
				request.setAttribute("isBindC", 1);
			}else{
				request.setAttribute("isBindC", 0);
			}
		}else{
			request.setAttribute("isBindC", 1);
		}
		return "gold";
	}
	
}
