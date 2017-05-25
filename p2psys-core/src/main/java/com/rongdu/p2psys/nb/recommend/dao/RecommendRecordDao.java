package com.rongdu.p2psys.nb.recommend.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;

public interface RecommendRecordDao extends BaseDao<RecommendRecord>
{
	RecommendRecord findRecord(User inviteUser, User user);
	
	public void savePcRecommendRecord(User user, UserRedPacket userRedPacket, User inviteUser);
}
