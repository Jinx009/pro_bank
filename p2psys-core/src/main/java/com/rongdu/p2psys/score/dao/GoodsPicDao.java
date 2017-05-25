package com.rongdu.p2psys.score.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.score.domain.GoodsPic;

public interface GoodsPicDao extends BaseDao<GoodsPic> {

    /**
     * 根据商品ID查询商品图片
     * @param goodsId 商品ID
     * @return 商品图片
     */
    List<GoodsPic> getGoodsPic(long goodsId);
    
    /**
     * 根据商品ID查询商品的第一张图片
     * @param goodsId 商品ID
     * @return 商品第一张图片
     */
    GoodsPic getGoodsFirstPic(long goodsId);
    
}
