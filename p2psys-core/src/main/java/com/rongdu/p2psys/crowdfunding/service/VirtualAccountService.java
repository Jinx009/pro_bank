package com.rongdu.p2psys.crowdfunding.service;

import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;

public interface VirtualAccountService {

	public VirtualAccount save(VirtualAccount virtualAccount);
	
	public void delete(int id);
	
	public void update(VirtualAccount virtualAccount);
	
	public VirtualAccount find(int id);

	public VirtualAccount findByUserId(long userId);
	
}
