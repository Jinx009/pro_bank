package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.UserUploadDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserUpload;
import com.rongdu.p2psys.user.service.UserUploadService;

@Service("userUploadService")
public class UserUploadServiceImpl implements UserUploadService {

    @Resource
    private UserUploadDao userUploadDao;

	@Override
	public UserUpload findById(long id) {
		
		return userUploadDao.find(id);
	}

    @Override
    public List<UserUpload> findByUser(User user) {
        QueryParam param = QueryParam.getInstance().addParam("user", user);
        List<UserUpload> list = new ArrayList<UserUpload>();
        for (UserUpload uu : userUploadDao.findByCriteria(param)) {
            UserUpload userUpload = new UserUpload();
            userUpload.setPicPath(uu.getPicPath());
            userUpload.setId(uu.getId());
            list.add(userUpload);
        }
        return list;
    }
}
