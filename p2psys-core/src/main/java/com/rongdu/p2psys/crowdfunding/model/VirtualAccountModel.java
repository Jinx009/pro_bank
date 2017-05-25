package com.rongdu.p2psys.crowdfunding.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;

public class VirtualAccountModel extends VirtualAccount{

	public static VirtualAccountModel instance(VirtualAccount virtualAccount) {
		VirtualAccountModel virtualAccountModel = new VirtualAccountModel();
		BeanUtils.copyProperties(virtualAccount, virtualAccountModel);
		return virtualAccountModel;
	}

	public VirtualAccount prototype() {
		VirtualAccount VirtualAccount = new VirtualAccount();
		BeanUtils.copyProperties(this, VirtualAccount);
		return VirtualAccount;
	}
	
}
