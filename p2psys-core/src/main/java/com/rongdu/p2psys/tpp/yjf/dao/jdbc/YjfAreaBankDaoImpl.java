package com.rongdu.p2psys.tpp.yjf.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.tpp.domain.YjfAreaBank;
import com.rongdu.p2psys.tpp.yjf.dao.YjfAreaBankDao;

@Repository("yjfAreaBankDao")
public class YjfAreaBankDaoImpl extends BaseDaoImpl<YjfAreaBank> implements YjfAreaBankDao {

	@Override
	public List<YjfAreaBank> getYjfAreaBankListByPid(String pid) {
		return findByProperty("pid", pid);
	}

}
