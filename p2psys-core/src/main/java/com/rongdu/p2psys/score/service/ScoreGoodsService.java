package com.rongdu.p2psys.score.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.score.domain.ScoreGoods;
import com.rongdu.p2psys.score.model.ScoreGoodsModel;

public interface ScoreGoodsService {

    /**
     * 商品兑换查询分页
     * @param model 查询条件分装类
     * @return page
     */
    PageDataList<ScoreGoodsModel> getScoreGoodsPage(ScoreGoodsModel model);
    
    /**
     * 后去商品兑换最新N条信息
     * @param limit 条数
     * @return 商品兑换
     */
    List<ScoreGoodsModel> getScoreGoodsList(int limit);
    
    /**
     * 根据ID查询商品兑换
     * @param id 商品兑换ID
     * @return 商品兑换信息
     */
    ScoreGoods getScoreGoodsById(long id);
    
    /**
     * 修改商品兑换
     * @param model 商品兑换model
     * @return 是否成功
     */
    Boolean editScoreGoods(ScoreGoodsModel model);
    
    /**
     * 添加商品兑换
     * @param model 商品兑换model
     * @return 是否成功
     */
    Boolean addScoreGoods(ScoreGoodsModel model);
    
    /**
     * 审核商品兑换
     * @param model 商品兑换model
     * @return 是否成功
     */
    Boolean verifyScoreGoods(ScoreGoodsModel model);
}
