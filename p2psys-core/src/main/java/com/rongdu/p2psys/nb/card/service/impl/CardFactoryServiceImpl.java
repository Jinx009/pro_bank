package com.rongdu.p2psys.nb.card.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.card.dao.CardFactoryDao;
import com.rongdu.p2psys.nb.card.domain.CardFactory;
import com.rongdu.p2psys.nb.card.service.CardFactoryService;

@Service("cardFactoryService")
public class CardFactoryServiceImpl implements CardFactoryService 
{

	@Resource
	private CardFactoryDao cardFactoryDao;
	
	public void update(CardFactory cardFactory)
	{
		cardFactoryDao.update(cardFactory);
	}

	public CardFactory getCardFactory(Long id)
	{
		return cardFactoryDao.find(id);
	}

	public List<CardFactory> getUnusedCard()
	{
		return cardFactoryDao.getUnusedCard();
	}

}
