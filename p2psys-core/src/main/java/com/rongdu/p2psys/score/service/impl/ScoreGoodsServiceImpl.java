package com.rongdu.p2psys.score.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.score.dao.GoodsDao;
import com.rongdu.p2psys.score.dao.GoodsPicDao;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.dao.ScoreGoodsDao;
import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.domain.GoodsPic;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.domain.ScoreGoods;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.score.model.ScoreGoodsModel;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertGoodsApplyLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertGoodsFailLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertGoodsSuccessLog;
import com.rongdu.p2psys.score.service.ScoreGoodsService;

@Service("scoreGoodsService")
public class ScoreGoodsServiceImpl implements ScoreGoodsService {

    @Resource
    private ScoreGoodsDao scoreGoodsDao;
    @Resource
    private GoodsPicDao goodsPicDao;
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private ScoreDao scoreDao;
    
    @Override
    public PageDataList<ScoreGoodsModel> getScoreGoodsPage(ScoreGoodsModel model) {
        QueryParam param = QueryParam.getInstance();
        if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
    		SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("goods.name", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1, orFilter2);
    	}else{ //精确查询条件
	        if (model.getName() != null && model.getName().length() > 0) {
	            param.addParam("goods.name", Operators.EQ, model.getName());
	        }
	        if (model.getUserName() != null && model.getUserName().length() > 0) {
	            param.addParam("user.userName", Operators.EQ, model.getUserName());
	        }
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
	            param.addParam("addTime", Operators.GTE, start);
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
	            param.addParam("addTime", Operators.LTE, end);
	        }
	        if (model.getStatus() > -99) {
	            param.addParam("status", model.getStatus());
	        }
    	}
        param.addOrder(OrderType.DESC, "id");
        if(model.getRows() == 0){
            param.addPage(model.getPage());
        }else {
            param.addPage(model.getPage(), model.getRows());
        }
        PageDataList<ScoreGoods> itemPage = scoreGoodsDao.findPageList(param);
        List<ScoreGoodsModel> modelList = new ArrayList<ScoreGoodsModel>();
        PageDataList<ScoreGoodsModel> modelPage = new PageDataList<ScoreGoodsModel>();
        if (itemPage != null && itemPage.getList() != null && itemPage.getList().size() > 0) {
            modelPage.setPage(itemPage.getPage());
            for (int i = 0; i < itemPage.getList().size(); i++) {
                ScoreGoods item = itemPage.getList().get(i);
                ScoreGoodsModel model_ = ScoreGoodsModel.instance(item);
                Goods goods = item.getGoods();
                GoodsPic pic = goodsPicDao.getGoodsFirstPic(goods.getId());
                model_.setImgSrc(pic.getPicUrl());
                model_.setName(goods.getName());
                model_.setUserName(item.getUser().getUserName());
                model_.setGoodsId(goods.getId());
                modelList.add(model_);
            }
        }
        modelPage.setList(modelList);
        return modelPage;
    }

    @Override
    public List<ScoreGoodsModel> getScoreGoodsList(int limit) {
        QueryParam param = QueryParam.getInstance();
        param.addOrder(OrderType.DESC, "verifyTime");
        param.addOrFilter(
                new SearchFilter("status", Operators.EQ, ScoreGoods.IS_DELIVERY),
                new SearchFilter("status", Operators.EQ, ScoreGoods.IS_RECEIVE),
                new SearchFilter("status", Operators.EQ, ScoreGoods.PASS_AUDIT)
                );
        List<ScoreGoods> itemList = scoreGoodsDao.findByCriteria(param, 0, limit);
        List<ScoreGoodsModel> modelList = new ArrayList<ScoreGoodsModel>();
        if (itemList != null && itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                ScoreGoods item = itemList.get(i);
                ScoreGoodsModel model_ = ScoreGoodsModel.instance(item);
                Goods goods = item.getGoods();
                GoodsPic pic = goodsPicDao.getGoodsFirstPic(goods.getId());
                model_.setImgSrc(pic.getPicUrl());
                model_.setName(goods.getName());
                model_.setUserName(item.getUser().getUserName());
                model_.setGoodsId(goods.getId());
                modelList.add(model_);
            }
        }
        return modelList;
    }

    @Override
    public ScoreGoods getScoreGoodsById(long id) {
        // TODO Auto-generated method stub
        return scoreGoodsDao.find(id);
    }

    @Override
    public Boolean editScoreGoods(ScoreGoodsModel model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean addScoreGoods(ScoreGoodsModel model) {
        if(model == null){
           return false; 
        }
        Goods goods = goodsDao.find(model.getGoodsId());
        Score score = scoreDao.findObjByProperty("user.userId", model.getUserId());
        model.checkGoods(goods);
        model.checkScore(score.getValidScore());
        ScoreGoods item = model.prototype();
        item.setMoney(goods.getCost() * item.getConvertNum());
        item.setAddTime(new Date());
        item.setGoods(new Goods(model.getGoodsId()));
        item = scoreGoodsDao.save(item);
        //会员积分日志记录信息
        int value = goods.getScore() * item.getConvertNum();
        Global.setTransfer("score", value);
        Global.setTransfer("goodsName", goods.getName());
        Global.setTransfer("convertNum", item.getConvertNum());
        BaseScoreLog bLog = new ScoreConvertGoodsApplyLog(model.getUserId(), value);
        bLog.doEvent();
        Boolean result = goodsDao.updateStore(model.getGoodsId(), 0, item.getConvertNum(), 0);
        if(item.getId() <= 0 || !result){
            throw new ScoreException("积分兑换失败！");
        }
        return true;
    }

    @Override
    public Boolean verifyScoreGoods(ScoreGoodsModel model) {
        if(model == null || model.getId() <= 0){
            return false;
        }
        if(model.getStatus() == ScoreGoodsModel.NOT_PASS_AUDIT || model.getStatus() == ScoreGoodsModel.PASS_AUDIT){
            ScoreGoods item = scoreGoodsDao.find(model.getId());
            if(item.getGoods() == null || item.getUser() == null){
                return false;
            }
            if(item.getStatus() != ScoreGoodsModel.WAIT_AUDIT){
                return false;
            }
            item.setVerifyRemark(model.getVerifyRemark());
            item.setVerifyTime(new Date());
            item.setVerifyUser(model.getVerifyUser());
            item.setVerifyUserId(model.getVerifyUserId());
            item.setStatus(model.getStatus());
            Global.setTransfer("score", item.getScore());
            Global.setTransfer("goodsName", item.getGoods().getName());
            Global.setTransfer("convertNum", item.getConvertNum());
            if(model.getStatus() == ScoreGoodsModel.NOT_PASS_AUDIT){
                BaseScoreLog bLog = new ScoreConvertGoodsFailLog(item.getUser().getUserId(), item.getScore());
                bLog.doEvent();
                Boolean result = goodsDao.updateStore(item.getGoods().getId(), 0, -item.getConvertNum(), 0);
                if(!result){
                    throw new ScoreException("积分兑换商品审核失败！");
                }
            }else if(model.getStatus() == ScoreGoodsModel.PASS_AUDIT){
                BaseScoreLog bLog = new ScoreConvertGoodsSuccessLog(item.getUser().getUserId(), item.getScore());
                bLog.doEvent();
                Boolean result = goodsDao.updateStore(item.getGoods().getId(), 0, -item.getConvertNum(), item.getConvertNum());
                if(!result){
                    throw new ScoreException("积分兑换商品审核失败！");
                }
            }
            scoreGoodsDao.update(item);
            return true;
        }
        return false;
    }

}
