package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.Flag;

public interface FlagService {

	public void saveFlag(Flag flag);
	
	public List<Flag> findAll();
	
	public void delete(Integer id);

	public Flag findById(int flagId);
	
}
