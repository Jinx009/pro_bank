package com.rongdu.p2psys.nb.ppfund.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.nb.ppfund.dao.ExperienceGoldDao;
import com.rongdu.p2psys.nb.ppfund.domain.ExperienceGold;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

@Service("theExperienceGoldService")
public class ExperienceGoldServiceImpl implements ExperienceGoldService {

	@Resource
	private ExperienceGoldDao theExperienceGoldDao;
	@Resource
	private UserDao userDao;

	@Override
	public PageDataList<ExperienceGoldModel> findExperienceGoldByItem(
			int pageNumber, int pageSize, String searchName) {
		return theExperienceGoldDao.findExperienceGoldByItem(pageNumber, pageSize,
				searchName);
	}

	@Override
	public ExperienceGoldModel loadExperienceGoldById(Long id) {
		ExperienceGold experienceGold = theExperienceGoldDao
				.loadExperienceGoldById(id);
		ExperienceGoldModel model = ExperienceGoldModel
				.instance(experienceGold);
		return model;
	}

	@Override
	public void deleteExperienceGoldById(Long id) {
		theExperienceGoldDao.deleteExperienceGoldById(id);
	}

	@Override
	public void updateExperienceGold(ExperienceGoldModel model) {
		ExperienceGold experienceGold = ExperienceGoldModel.instance(model);
		theExperienceGoldDao.updateExperienceGold(experienceGold);
	}

	@Override
	public void saveExperienceGold(ExperienceGoldModel model) {
		ExperienceGold experienceGold = ExperienceGoldModel.instance(model);
		theExperienceGoldDao.save(experienceGold);
	}

	@Override
	public void addExperienceGold(User user) {
		// 注册成功发放体验金
		ExperienceGoldModel egmodel = new ExperienceGoldModel();
		egmodel.setDays(Global.getInt("EXPERIENCE_GOLD_DAY"));// 体验金有效天数
		egmodel.setMoney(Global.getDouble("EXPERIENCE_GOLD"));// 体验金额
		egmodel.setUser(user);// 用户
		egmodel.setStatus(0);// 体验金有效状态0：正常，1：失效
		egmodel.setAddTime(new Date());// 体验金发放时间
		saveExperienceGold(egmodel);
	}

	@Override
	public PageDataList<ExperienceGoldModel> findByModel(
			ExperienceGoldModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addPage(model.getPage(), model.getRows());
		if (model.getId() != 0) {
			param.addOrder("addTime");
			param.addParam("user.userId", model.getId());
		} else {
			param.addOrder(OrderType.DESC, "id");
		}
		PageDataList<ExperienceGold> pageDataList = theExperienceGoldDao
				.findPageList(param);
		PageDataList<ExperienceGoldModel> pageDataList_ = new PageDataList<ExperienceGoldModel>();
		List<ExperienceGoldModel> list = new ArrayList<ExperienceGoldModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (ExperienceGold eg : pageDataList.getList()) {
				ExperienceGoldModel egModel = ExperienceGoldModel.instance(eg);
				egModel.setRealName(eg.getUser().getRealName());
				egModel.setUser(eg.getUser());
				list.add(egModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public List<ExperienceGold> getExperienceGoldByUserId(Long userId) {
		return theExperienceGoldDao.list(userId);
	}

	@Override
	public Boolean checkUserIsUseGold(Long userId) {
		return theExperienceGoldDao.checkUserIsUseGold(userId);
	}

	@Override
	public ExperienceGoldModel getEGByUserId(Long userId) {
		ExperienceGold eg = theExperienceGoldDao.getEGByUserId(userId);
		if (eg != null) {
			ExperienceGoldModel egModel = ExperienceGoldModel.instance(eg);
			return egModel;
		}
		return null;
	}

	@Override
	public Double getGoldByUserId(Long userId) {
		ExperienceGold eg = theExperienceGoldDao.getEGByUserId(userId);
		if (eg != null) {
			return eg.getMoney();
		}
		return 0.0;
	}
	
	@Override
	public boolean getCanExperienceGold(User user)
	{
		if(checkData(user))
		{
			ExperienceGoldModel experienceGoldModel = getEGByUserId(user.getUserId());
			
			if(null==experienceGoldModel)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkData(User user)
	{
		Date userDate= user.getAddTime();
		String myString = ConstantUtil.USER_ADD_TIME;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Date d;
		boolean flag = false;
		try
		{
			d = sdf.parse(myString);
			flag = d.before(userDate);
		} 
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		return flag;
	}
	
	

	@Override
	public void addPpfundExperienceGold(User user, Ppfund ppfund)
	{
		// 注册成功发放体验金
		ExperienceGoldModel egmodel = new ExperienceGoldModel();
		egmodel.setDays(0);//体验金有效天数
		egmodel.setMoney(0);//体验金额
		egmodel.setUser(user);//用户
		egmodel.setStatus(0);//体验金有效状态0：正常，1：失效
		egmodel.setAddTime(new Date());//体验金发放时间
		egmodel.setInvestTime(new Date());
		egmodel.setPpfund(ppfund);
		saveExperienceGold(egmodel);
	}

}
