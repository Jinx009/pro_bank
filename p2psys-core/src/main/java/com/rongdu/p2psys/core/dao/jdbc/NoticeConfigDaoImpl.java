package com.rongdu.p2psys.core.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.NoticeConfigDao;
import com.rongdu.p2psys.core.domain.NoticeConfig;

@Repository
public class NoticeConfigDaoImpl extends BaseDaoImpl<NoticeConfig> implements NoticeConfigDao {

	@SuppressWarnings("rawtypes")
	RowMapper mapper = new RowMapper() {
		@Override
		public Object mapRow(ResultSet rs, int num) throws SQLException {
			NoticeConfig c = new NoticeConfig();
			c.setId(rs.getLong("id"));
			c.setType(rs.getString("type"));
			c.setEmail(rs.getInt("email"));
			c.setLetters(rs.getInt("letters"));
			c.setSms(rs.getInt("sms"));
			return c;
		}
	};

	@Override
	public PageDataList<NoticeConfig> list(int page) {
		QueryParam param = QueryParam.getInstance();
		param.addOrder(OrderType.DESC, "id");
		param.addPage(page);
		return super.findPageList(param);
	}

}
