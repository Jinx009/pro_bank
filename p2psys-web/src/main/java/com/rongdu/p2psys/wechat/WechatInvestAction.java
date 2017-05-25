package com.rongdu.p2psys.wechat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.CouponStatusConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.nb.invest.service.FrozenProductService;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundInService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductSetService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserVipService;

public class WechatInvestAction extends BaseAction<BorrowModel> implements ModelDriven<BorrowModel>
{
	private Map<String, Object> map;

	private User user;

	@Resource
	private BorrowService borrowService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private FrozenUserService frozenUserService;
	@Resource
	private FrozenProductService frozenProductService;
	@Resource
	private PpfundService thePpfundService;
	@Resource
	private ProductSetService productSetService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private AccountLogService theAccountLogService;
	@Resource
	private CouponService couponService;
	@Resource
	private PpfundInService thePpfundInService;

	/**
	 * 新投资方法
	 * 
	 * @throws Exception
	 */
	@Action(value = "/nb/wechat/account/addInvest")
	public void addNbInvest() throws Exception
	{
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.SUCCESS, ConstantUtil.FAILURE);
		user = getNBSessionUser();
	    
	    if(checkInvestStatus(user))
	    {
			Long productBasicId = paramLong("productBasicId");
			double money = paramDouble("money");
			double subMoney = paramDouble("subMoney");
			double goldMoney = 0;
			String pwd = paramString("pwd");
			Long[] ids = ConstantUtil.getIds(paramString("ids"));
			Long[] cids = ConstantUtil.getIds(paramString("cids"));//加息券ID

			// 投资基本信息校验
			String checkResult = "";
			if(goldMoney>0)
			{
				
			}
			else
			{
				checkResult = productBasicService.checkInvest(productBasicId,user, money, pwd, subMoney);
			}
			if(ids!=null&&ids.length>0)
			{
				for(int i=0;i<ids.length;i++)
				{
					UserRedPacket uRedPacket = userRedPacketService.findUserRedPacketById(ids[i]);
					if(uRedPacket==null){
						checkResult = "含有非法的红包参数！";
					}else{
						if(uRedPacket.isUsed())
						{
							checkResult = "红包已经使用，请重新投资！";
						}
					}
				}
			}
			
			//校验加息卡
			if(cids!=null&&cids.length>0)
			{
				for(int i=0;i<cids.length;i++)
				{
					Coupon cp = couponService.findCouponInfoById(cids[0]);
					if(cp==null){
						checkResult = "含有非法的加息券参数！";
					}else{
						if(cp.getStatus()!=CouponStatusConstant.STATUS_UNUSED)
						{
							checkResult = "加息券不可用或已失效，请重新投资！";
						}
					}
				}
			}
			
			ProductBasic productBasic = productBasicService.findById(productBasicId);
			//验证该用户本日投资现金类产品的金额是否上限？
			//是则不能投资，提示明日再来
			//否则继续可投，并且投资额不能大于本日剩余额度
			if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(productBasic.getProductType().getTypeCategory())){
				//获取该用户今日已投现金金额
				double toDayInvestMoney = thePpfundInService.getTotalDayInvestMoney(user.getUserId());
				//获取今日该用户可以投的剩余金额
				double availablMoney = ProductTypeConstant.PRODUCT_CATEGORY__PPFUND_LIMIT-toDayInvestMoney;
				if(availablMoney==0){
					checkResult = "您今日该产品可投额度已满，建议您投资其他高收益固定产品！";
				}else	if(availablMoney<money){
					checkResult = "对不起，您的额度不足！今日可投额度"+availablMoney+"元。";
				}
			}
			
			// 校验不成功
			if (!"success".equals(checkResult))
			{
				map.put(ConstantUtil.ERRORMSG, checkResult);
			}
			// 校验成功
			else
			{
				if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(productBasic.getProductType().getTypeCategory())|| 
					ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE.equals(productBasic.getProductType().getTypeCategory()))
				{
					// 冻结用户可用金额和项目已投金额
					frozenUserService.saveFrozenUser(user, money, productBasic,productBasic.getProductType());
					frozenProductService.saveFrozenProduct(user, productBasic,productBasic.getProductType(), money);
					Ppfund ppfund = thePpfundService.getById(productBasic.getRelatedId());

					PpfundInModel ppfundInModel = new PpfundInModel();
					ppfundInModel.setMoney(money);
					ppfundInModel.setUser(user);
					ppfundInModel.setPpfund(ppfund);
					ppfundInModel.setProductBasicId(productBasicId);
					ppfundInModel.setProductType(ppfund.getProductType());
//					ppfundInModel.setCids(cids);//加息券
					
					//普通标
					ppfundInModel.setProductType(ppfund.getProductType());
					ConcurrentUtil.ppfundTender(ppfundInModel);
				}
				else if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW .equals(productBasic.getProductType().getTypeCategory())
						||ProductTypeConstant.PRODUCT_CATEGORY__VIP.equals(productBasic.getProductType().getTypeCategory()))
				{
					//非现金类产品 冻结用户可用金额和项目已投金额
					frozenUserService.saveFrozenUser(user, money, productBasic,productBasic.getProductType());
					frozenProductService.saveFrozenProduct(user, productBasic,productBasic.getProductType(), money);
					Borrow borrow = borrowService.find(productBasic.getRelatedId());

					BorrowModel borrowModel = BorrowModel.instance(borrow);
					borrowModel.setMoney(money);
					borrowModel.setUser(user);
					borrowModel.setAddIp(Global.getIP());
					borrowModel.setProductBasicId(productBasicId);
					borrowModel.setIds(ids);
					borrowModel.setCids(cids);//加息券
					ConcurrentUtil.tender(borrowModel, borrow);
				} 
				else if (ProductTypeConstant.PRODUCT_CATEGORY__SET.equals(productBasic.getProductType().getTypeCategory()))
				{
					//朱俊杰确认无用，故而删除 2015-11-17 18:12
				}
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG, "投资成功!");
			}
	    }
	    else
	    {
	    	map.put(ConstantUtil.ERRORMSG,"请勿重新提交!");
	    }
		
		printWebJson(getStringOfJpaObj(map));
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
	
	public Map<String, Object> getMap()
	{
		return map;
	}

	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}
