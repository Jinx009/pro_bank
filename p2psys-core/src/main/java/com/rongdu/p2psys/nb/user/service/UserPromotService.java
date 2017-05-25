package com.rongdu.p2psys.nb.user.service;

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;

public interface UserPromotService
{
	public void updateUserPromot(UserPromot userPromot);
	
	public UserPromot saveUserPromot(UserPromot userPromot);
	
	public UserPromot getUserPromotByCode(String code);
	
	public boolean hasDuplicateCode(String code);

	public void checkUserPromot(User mainUser);

	public UserPromot saveWechatUserPromot(UserPromot userPromot);

	public UserPromot findUserPromotByUserId(long userId);
}
