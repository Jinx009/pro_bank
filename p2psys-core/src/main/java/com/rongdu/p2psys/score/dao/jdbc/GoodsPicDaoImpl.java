package com.rongdu.p2psys.score.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.score.dao.GoodsPicDao;
import com.rongdu.p2psys.score.domain.GoodsPic;

@Repository("goodsPicDao")
public class GoodsPicDaoImpl extends BaseDaoImpl<GoodsPic> implements GoodsPicDao {

    @Override
    public List<GoodsPic> getGoodsPic(long goodsId) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("goodsId", goodsId);
        param.addOrder(OrderType.ASC, "id");
        return this.findByCriteria(param);
    }

    @Override
    public GoodsPic getGoodsFirstPic(long goodsId) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("goodsId", goodsId);
        param.addOrder(OrderType.ASC, "id");
        List<GoodsPic> picList = this.findByCriteria(param, 0, 1);
        if (picList != null) {
            return picList.get(0);
        }
        return new GoodsPic();
    }
}
