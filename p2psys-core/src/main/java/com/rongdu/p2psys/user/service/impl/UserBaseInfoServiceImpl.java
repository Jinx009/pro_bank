package com.rongdu.p2psys.user.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.service.UserBaseInfoService;

/**
 * 用户基本信息
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年11月4日
 */
@Service("userBaseInfoService")
public class UserBaseInfoServiceImpl implements UserBaseInfoService {

	@Resource
	private UserBaseInfoDao userBaseInfoDao;
	
	@Override
	public UserBaseInfo findByUserId(long userId) {
		return userBaseInfoDao.findObjByProperty("user.userId", userId);
	}
	
	@Override
	public void save(UserBaseInfo userBaseInfo) {
		
        QueryParam param = QueryParam.getInstance();
        param.addParam("user.userId", userBaseInfo.getUser().getUserId());
        UserBaseInfo baseInfo = userBaseInfoDao.findByCriteriaForUnique(param);
        if(baseInfo == null){
        	userBaseInfoDao.save(userBaseInfo);
        }else{
        	userBaseInfo.setId(baseInfo.getId());
        	userBaseInfo.setUser(baseInfo.getUser());
        	userBaseInfoDao.update(userBaseInfo);
        }
	}

	@Override
	public void save(UserCache uc) {
		User user = uc.getUser();
		UserBaseInfo baseInfo = new UserBaseInfo(user);
		if ("".equals(uc.getProvince()) && !"".equals(uc.getCity()) && !"".equals(uc.getArea())) {// 直辖市类型（上海-黄浦区）
			baseInfo.setProvince(uc.getCity());
			baseInfo.setCity(uc.getArea());
		} else if (!"".equals(uc.getProvince()) && !"".equals(uc.getCity())) {
			baseInfo.setProvince(uc.getProvince());
			baseInfo.setCity(uc.getCity());
		}
		if(user.getCardId() != null){
			baseInfo.setBirthday(StringUtil.getBirthdayByCardid(user.getCardId()));
		}
		userBaseInfoDao.save(baseInfo);
	}

}
