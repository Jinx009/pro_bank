package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.score.dao.ScoreGoodsDao;
import com.rongdu.p2psys.score.domain.ScoreGoods;

@Repository("scoreGoodsDao")
public class ScoreGoodsDaoImpl extends BaseDaoImpl<ScoreGoods> implements ScoreGoodsDao {
    
}
