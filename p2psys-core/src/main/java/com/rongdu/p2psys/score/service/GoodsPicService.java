package com.rongdu.p2psys.score.service;

import java.util.List;

import com.rongdu.p2psys.score.domain.GoodsPic;

public interface GoodsPicService {
    
    /**
     * 添加图片
     * @param pic 图片
     * @return 图片
     */
    GoodsPic addGoodsPic(GoodsPic pic);

    /**
     * 删除图片
     * @param id ID
     */
    void deleteGoodsPic(long id);
    
    /**
     * 根据商品ID查询商品图片
     * @param goodsId
     * @return
     */
    List<GoodsPic> getGoodsPicByGoodsId(long goodsId);
}
