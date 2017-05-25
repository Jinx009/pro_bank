package com.rongdu.p2psys.score.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.score.domain.Goods;

public interface GoodsDao extends BaseDao<Goods> {

    /**
     * 修改商品库存(修改库存的时候小心，不管是那种库存，都只增不减)
     * @param goodsId 商品ID
     * @param store 总库存
     * @param freezeStore 冻结库存
     * @param sellAcount 消费库存
     * @return 修改是否成功
     */
    public boolean updateStore(long goodsId, int store, int freezeStore, int sellAcount);
    
}
