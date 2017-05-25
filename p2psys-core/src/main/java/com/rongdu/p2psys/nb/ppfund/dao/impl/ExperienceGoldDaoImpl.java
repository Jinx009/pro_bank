package com.rongdu.p2psys.nb.ppfund.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.ppfund.dao.ExperienceGoldDao;
import com.rongdu.p2psys.nb.ppfund.domain.ExperienceGold;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

@Repository("theExperienceGoldDao")
public class ExperienceGoldDaoImpl extends BaseDaoImpl<ExperienceGold> implements ExperienceGoldDao {

	@Resource
	private ExperienceGoldService theExperienceGoldService;
	
	
	@Override
	@SuppressWarnings("unchecked")
	public PageDataList<ExperienceGoldModel> findExperienceGoldByItem(
			int pageNumber, int pageSize, String searchName) {
		PageDataList<ExperienceGoldModel> pageDataList = new PageDataList<ExperienceGoldModel>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT                                   ");
		sql.append("    eg.*                                 ");
		sql.append("FROM                                     ");
		sql.append("    nb_experience_gold AS eg             ");
		sql.append("        LEFT JOIN                        ");
		sql.append("    rd_user ru ON eg.user_id = ru.user_id");
		sql.append("        LEFT JOIN                        ");
		sql.append("    rd_ppfund rp ON eg.ppfund_id = rp.id ");
		sql.append("WHERE                                    ");
		sql.append("    1 = 1                                ");
		if (!StringUtil.isBlank(searchName)) {
			sql.append("AND ru.real_name LIKE '%" + searchName + "%'");
		}
		sql.append("ORDER BY eg.id DESC");
		Query query = em
				.createNativeQuery(sql.toString(), ExperienceGold.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ExperienceGold> list = query.getResultList();
		List<ExperienceGoldModel> list_ = new ArrayList<ExperienceGoldModel>();
		for (ExperienceGold eg : list) {
			ExperienceGoldModel model = ExperienceGoldModel.instance(eg);
			model.setName(eg.getPpfund().getName());
			model.setRealName(eg.getUser().getRealName());
			list_.add(model);
		}
		pageDataList.setList(list_);
		pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public ExperienceGold loadExperienceGoldById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<ExperienceGold> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (ExperienceGold) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void deleteExperienceGoldById(long id) {
		ExperienceGold eg = super.find(id);
		if (null != eg) {
			super.delete(id);
		}
	}

	@Override
	public void updateExperienceGold(ExperienceGold eg) {
		em.merge(eg);
	}

	@Override
	public List<ExperienceGold> list(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		return findByCriteria(param);
	}

	@Override
	public Boolean checkUserIsUseGold(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addParam("status", 0);
		List<ExperienceGold> list = findByCriteria(param);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public ExperienceGold getEGByUserId(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		List<ExperienceGold> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (ExperienceGold) list.get(0);
		} else {
			return null;
		}
	}

	public boolean getCanExperienceGold(User user)
	{
		if(checkData(user))
		{
			ExperienceGoldModel experienceGoldModel = theExperienceGoldService.getEGByUserId(user.getUserId());
			
			if(null==experienceGoldModel)
			{
				return true;
			}
		}
		
		return false;
	}
	/**
	 * 校验用户注册时间
	 * @param user
	 * @return
	 */
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return flag;
	}

}
