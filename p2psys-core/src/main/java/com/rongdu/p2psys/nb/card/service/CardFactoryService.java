package com.rongdu.p2psys.nb.card.service;

import java.util.List;

import com.rongdu.p2psys.nb.card.domain.CardFactory;

public interface CardFactoryService
{
	public void update(CardFactory cardFactory);
	
	public CardFactory getCardFactory(Long id);
	
	public List<CardFactory> getUnusedCard();
}
