package com.rongdu.p2psys.account.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.AccountRecordeDao;
import com.rongdu.p2psys.account.domain.AccountRecorde;

/**
 * 资金汇总记录
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月4日
 */
@Repository("accountRecordeDao")
public class AccountRecordeDaoImpl extends BaseDaoImpl<AccountRecorde>
		implements AccountRecordeDao {

}
