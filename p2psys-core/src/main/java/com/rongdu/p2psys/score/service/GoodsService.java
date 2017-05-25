package com.rongdu.p2psys.score.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.model.GoodsModel;

public interface GoodsService {

    /**
     * 商品查询分页.
     * @param model 查询条件分装类
     * @return page
     */
    PageDataList<GoodsModel> getGoodsPage(GoodsModel model);
    
    /**
     * 添加商品
     * @param model 商品model
     * @return 是否成功
     */
    Boolean addGoods(GoodsModel model);
    
    /**
     * 根据ID查询商品信息
     * @param id ID
     * @return 商品
     */
    Goods getGoodsById(long id);
    
    /**
     * 修改商品
     * @param model 商品model
     * @return 是否成功
     */
    Boolean editGoods(GoodsModel model);
    
    /**
     * 审核商品
     * @param model 商品model
     * @return 是否成功
     */
    Boolean verifyGoods(GoodsModel model);
    
    /**
     * 商品上下架操作
     * @param model 商品model
     * @return 是否成功
     */
    Boolean shelvesGoods(GoodsModel model);
}
