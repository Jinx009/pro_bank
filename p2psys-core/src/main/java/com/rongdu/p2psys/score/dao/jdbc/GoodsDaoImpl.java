package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.score.dao.GoodsDao;
import com.rongdu.p2psys.score.domain.Goods;

@Repository("goodsDao")
public class GoodsDaoImpl extends BaseDaoImpl<Goods> implements GoodsDao {

    @Override
    public boolean updateStore(long goodsId, int store, int freezeStore, int sellAcount) {
            StringBuffer sql = new StringBuffer("UPDATE Goods SET store = store + :store,");
            sql.append(" freezeStore = freezeStore + :freezeStore, sellAcount = sellAcount + :sellAcount");
            sql.append(" WHERE id = :goodsId AND");
            sql.append(" store + :store >= 0 AND freezeStore + :freezeStore >= 0 AND sellAcount + :sellAcount >= 0 ");
            sql.append(" status = 1 AND type = 1");
            int result = this.updateByJpql(sql.toString(), 
                    new String[]{"store", "freezeStore", "sellAcount", "goodsId", "store", "freezeStore", "sellAcount"}, 
                    new Object[] {store, freezeStore, sellAcount, goodsId, store, freezeStore, sellAcount});
            if (result < 1) {
                return false;
            }
            // 更新缓存
            em.refresh(this.find(goodsId));
            return true;
    }
	
}
