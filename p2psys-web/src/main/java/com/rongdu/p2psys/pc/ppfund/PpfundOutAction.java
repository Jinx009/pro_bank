package com.rongdu.p2psys.pc.ppfund;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.model.PpfundOutModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundOutService;
import com.rongdu.p2psys.user.domain.User;

public class PpfundOutAction  extends BaseAction<PpfundOutModel> implements ModelDriven<PpfundOutModel> {
	
	@Resource
	private PpfundOutService ppfundOutService;
	
	@Resource
	private PpfundInService ppfundInService;
	
	@Resource
	private PpfundService thePpfundService;
	
	@Resource
	private SystemConfigService systemConfigService;

	@Action(value="/ppfund/ppfundOut")
	public void ppfundOut() throws IOException{
		User user = getNBSessionUser();
		if(null != user){
			if(checkInvestStatus(user)){
				long ppfundId = paramLong("ppfundId");
				double redeemMoney = paramDouble("redeemMoney");
				PpfundIn ppfundIn = ppfundInService.getById(ppfundId);//当前购买本金 
				SystemConfig configMax = systemConfigService.findByNid(Constant.REDEEM_MONEY);//每日最高赎回金额
				SystemConfig configMin   = systemConfigService.findByNid(Constant.REDEEM_MIN_MONEY);//赎回最低金额
				double parsentMoney = ppfundOutService.getPresentRedeem(user);//已经赎回总金额
				if(null !=ppfundIn){
					double minMoney =ppfundIn.getPpfund().getLowestAccount();//剩余金额最低限额
					if(redeemMoney <= ppfundIn.getAccount()){
						if(redeemMoney < Double.valueOf(configMin.getValue())){//必须大于最低赎回金额
							printWebResult("赎回金额必须大于 ［"+configMin.getValue()+"］ 元", false);
							return ;
						}
						double balancMoney = ppfundIn.getAccount() - redeemMoney;//剩余金额
						if(balancMoney !=0 || balancMoney >0){
							if(balancMoney < minMoney){
								printWebResult("赎回剩余金额需大于最低投资金额［"+minMoney+"］元，请重新输入赎回金额", false);
								return ;
							}
						}
					}
					
					if(ppfundIn.getAccount() < redeemMoney){
						printWebResult("赎回金额不能大于投入金额", false);
						return ;
					}else if(redeemMoney >( Double.valueOf(configMax.getValue()) - parsentMoney )){//赎回金额必须小于等于每天可赎回金额和当天已赎回金额的差
						double money = Double.valueOf(configMax.getValue()) - parsentMoney;
						printWebResult("您今天可赎回金额为［"+money+"］元", false);
						return ;
					}else if(parsentMoney ==  Double.valueOf(configMax.getValue()) ){
						printWebResult("您当天可赎回的金额已超限,请明天再续", false);
						return ;
					}
					double cash = ( Double.valueOf(configMax.getValue()) - parsentMoney - redeemMoney);
					String str =String.valueOf(cash);
					printWebResult(str, true);
					return ;
				}
				printWebResult("数据有误,请联系客服 ", false);
				return ;
			}else{
				printWebResult("您的操作速度太快，请先歇歇", false);
				return ;
			}
		}
	}
	
	@SuppressWarnings({ "static-access"})
	public boolean checkInvestStatus(User  user)
	{
		String userIdStr = String.valueOf(user.getUserId());
		
		if(null==context.getAttribute(userIdStr))
		{
			Date date=new Date();
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(date); 
			calendar.add(calendar.SECOND,5);
			date=calendar.getTime(); 
			
			context.setAttribute(userIdStr,date);
			
			return true;
		}
		else
		{
			Date date2 = (Date) context.getAttribute(userIdStr);
			Date date=new Date();
			
			if(date2.before(date))
			{
				Calendar calendar2 = new GregorianCalendar(); 
				calendar2.setTime(date); 
				calendar2.add(calendar2.SECOND,5);
				date=calendar2.getTime(); 
				
				context.setAttribute(userIdStr,date);
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
