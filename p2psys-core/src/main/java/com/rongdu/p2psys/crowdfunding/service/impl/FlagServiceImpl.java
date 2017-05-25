package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.FlagDao;
import com.rongdu.p2psys.crowdfunding.domain.Flag;
import com.rongdu.p2psys.crowdfunding.service.FlagService;

@Service("flagService")
public class FlagServiceImpl implements FlagService{

	@Resource
	private FlagDao flagDao;
	
	public void saveFlag(Flag flag) {
		flagDao.save(flag);
	}

	public List<Flag> findAll() {
		return flagDao.findAll();
	}

	public void delete(Integer id) {
		flagDao.delete(id);
	}

	public Flag findById(int flagId) {
		return flagDao.find(flagId);
	}

}
