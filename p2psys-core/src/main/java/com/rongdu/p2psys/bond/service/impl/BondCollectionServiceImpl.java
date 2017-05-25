package com.rongdu.p2psys.bond.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.bond.dao.BondCollectionDao;
import com.rongdu.p2psys.bond.domain.BondCollection;
import com.rongdu.p2psys.bond.model.BondCollectionModel;
import com.rongdu.p2psys.bond.service.BondCollectionService;

/**
 * 债权待收Service
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Service(value = "bondCollectionService")
@Transactional
public class BondCollectionServiceImpl implements BondCollectionService {
    
	@Resource
    private BondCollectionDao bondCollectionDao;

    @Override
    public BondCollection addBondCollection(BondCollection bondCollection) {
       return bondCollectionDao.save(bondCollection);

    }

    @Override
    public BondCollection getBondCollectionById(long id) {
        return bondCollectionDao.getBondCollectionById(id);
    }

    @Override
    public BondCollection bondCollectionUpdate(BondCollection bondCollection) {
        return  bondCollectionDao.update(bondCollection);

    }

    @Override
    public void deleteBondCollection(long id) {
        bondCollectionDao.delete(id);
    }

    @Override
    public PageDataList<BondCollection> getCollectionPage(QueryParam param) {
        return bondCollectionDao.findPageList(param);
    }

    @Override
    public PageDataList<BondCollectionModel> getCollectionModelPage(BondCollectionModel model) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Object[] getSumBondCollection(long userId) {
		return bondCollectionDao.getSumBondCollection(userId);
	}
}
