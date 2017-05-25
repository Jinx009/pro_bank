package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.VirtualAccountDao;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;
import com.rongdu.p2psys.crowdfunding.service.VirtualAccountService;

@Service("virtualAccountService")
public class VirtualAccountServiceImpl implements VirtualAccountService{

	@Resource
	private VirtualAccountDao virtualAccountDao;
	
	public VirtualAccount save(VirtualAccount virtualAccount) {
		return virtualAccountDao.save(virtualAccount);
	}

	public void delete(int id) {
		virtualAccountDao.delete(id);
	}

	public void update(VirtualAccount virtualAccount) {
		virtualAccountDao.update(virtualAccount);
	}

	public VirtualAccount find(int id) {
		return virtualAccountDao.find(id);
	}

	public VirtualAccount findByUserId(long userId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  FROM VirtualAccount WHERE user.userId = ");
		buffer.append(userId);
		List<VirtualAccount> list = virtualAccountDao.getByHql(buffer.toString());
		if(null!=list){
			return list.get(0);
		}
		return null;
	}

}
