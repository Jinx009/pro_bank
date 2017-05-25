package com.rongdu.p2psys.score.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.score.dao.GoodsPicDao;
import com.rongdu.p2psys.score.domain.GoodsPic;
import com.rongdu.p2psys.score.service.GoodsPicService;

@Service("goodsPicService")
public class GoodsPicServiceImpl implements GoodsPicService {

    @Resource
    private GoodsPicDao goodsPicDao;
    
    @Override
    public GoodsPic addGoodsPic(GoodsPic pic) {
        return goodsPicDao.save(pic);
    }

    @Override
    public void deleteGoodsPic(long id) {
        goodsPicDao.delete(id);
    }

    @Override
    public List<GoodsPic> getGoodsPicByGoodsId(long goodsId) {
        // TODO Auto-generated method stub
        return goodsPicDao.findByProperty("goodsId", goodsId);
    }

}
