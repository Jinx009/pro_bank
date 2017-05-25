package com.rongdu.p2psys.core.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.VerifyLogModel;

@Repository("verifyLogDao")
public class VerifyLogDaoImpl extends BaseDaoImpl<VerifyLog> implements VerifyLogDao {

	@Override
	public VerifyLog findByType(long fid, String type, int verifyType) {
		QueryParam param = QueryParam.getInstance().addParam("fid", fid).addParam("type", type)
				.addParam("verifyType", verifyType);
		return findByCriteriaForUnique(param);
	}

	@Override
	public List<VerifyLogModel> list(String type, long fid) {
		QueryParam param = QueryParam.getInstance().addParam("fid", fid).addParam("type", type);
		List<VerifyLog> verifyLogList = this.findByCriteria(param);
		List<VerifyLogModel> list = new ArrayList<VerifyLogModel>();
		if (verifyLogList != null && verifyLogList.size() > 0) {
			for (int i = 0; i < verifyLogList.size(); i++) {
				VerifyLog log = verifyLogList.get(i);
				VerifyLogModel logModel = VerifyLogModel.instance(log);
				logModel.setOpName(log.getVerifyUser().getName());
				list.add(logModel);
			}
		}
		return list;
	}

}
