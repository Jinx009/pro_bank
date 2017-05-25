package com.rongdu.p2psys.nb.card.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.card.domain.CardFactory;

public interface CardFactoryDao extends BaseDao<CardFactory>
{
	public List<CardFactory> getUnusedCard();
}
