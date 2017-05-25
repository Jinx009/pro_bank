package com.rongdu.p2psys.score.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.score.dao.GoodsDao;
import com.rongdu.p2psys.score.dao.GoodsPicDao;
import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.domain.GoodsPic;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.score.model.GoodsModel;
import com.rongdu.p2psys.score.service.GoodsService;

@Service("goodsSerive")
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsPicDao goodsPicDao; 
    @Resource
    private VerifyLogDao verifyLogDao;
    
    @Override
    public PageDataList<GoodsModel> getGoodsPage(GoodsModel model) {
        QueryParam param = QueryParam.getInstance();
        if(StringUtil.isNotBlank(model.getSearchName())){//模糊查询
        	 param.addParam("name", Operators.LIKE,model.getSearchName());
        }else{//精确查询
	        if (model.getName() != null && model.getName().length() > 0) {
	            param.addParam("name", Operators.EQ, model.getName());
	        }
	        if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
	            param.addParam("addTime", Operators.GTE, start);
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
	            param.addParam("addTime", Operators.LTE, end);
	        }
	        if (model.getStartScore() > 0) {
	            param.addParam("score", Operators.GTE, model.getStartScore());
	        }
	        if (model.getEndScore() > 0) {
	            param.addParam("score", Operators.LTE, model.getEndScore());
	        }
	        if (model.getStatus() > -99) {
	            param.addParam("status", model.getStatus());
	        }
	        if (model.getType() > -99) {
	            param.addParam("status", model.getStatus());
	        }
        }
        param.addOrder(OrderType.DESC, "id");
        if(model.getRows() == 0){
            param.addPage(model.getPage());
        }else {
            param.addPage(model.getPage(), model.getRows());
        }
        PageDataList<Goods> itemPage = goodsDao.findPageList(param);
        List<GoodsModel> modelList = new ArrayList<GoodsModel>();
        PageDataList<GoodsModel> modelPage = new PageDataList<GoodsModel>();
        if (itemPage != null && itemPage.getList() != null && itemPage.getList().size() > 0) {
            modelPage.setPage(itemPage.getPage());
            for (int i = 0; i < itemPage.getList().size(); i++) {
                Goods item = itemPage.getList().get(i);
                GoodsModel model_ = GoodsModel.instance(item);
                GoodsPic pic = goodsPicDao.getGoodsFirstPic(item.getId());
                model_.setImgSrc(pic.getPicUrl());
                modelList.add(model_);
            }
        }
        modelPage.setList(modelList);
        return modelPage;
    }

    @Override
    public Boolean addGoods(GoodsModel model) {
        if (model == null || model.getGoodsCategory() == null || model.getFiles() == null) {
            return false;
        }
        Goods item = model.prototype();
        item.setStatus(GoodsModel.WAIT_AUDIT);
        item.setType(GoodsModel.WAIT_SHELVES);
        item.setAddTime(new Date());
        item = goodsDao.save(item);
        if (item == null || item.getId() <= 0) {
            throw new ScoreException();
        }
        for (String imgUrl : model.getFiles()) {
            GoodsPic pic = new GoodsPic();
            pic.setPicUrl(imgUrl);
            pic.setGoodsId(item.getId());
            pic.setAddTime(new Date());
            goodsPicDao.save(pic);
        }
        return true;
    }

    @Override
    public Goods getGoodsById(long id) {
        return goodsDao.find(id);
    }

    @Override
    public Boolean editGoods(GoodsModel model) {
        if (model == null || model.getGoodsCategory() == null) {
            return false;
        }
        Goods item = model.prototype();
        item.setStatus(GoodsModel.WAIT_AUDIT);
        item.setType(GoodsModel.WAIT_SHELVES);
        item = goodsDao.update(item);
        if (item == null || item.getId() <= 0) {
            throw new ScoreException();
        }
        if (model.getFiles() == null) {
            return true;
        }
        for (String imgUrl : model.getFiles()) {
            GoodsPic pic = new GoodsPic();
            pic.setPicUrl(imgUrl);
            pic.setGoodsId(item.getId());
            pic.setAddTime(new Date());
            goodsPicDao.save(pic);
        }
        return true;
    }

    @Override
    public Boolean verifyGoods(GoodsModel model) {
        if (model == null || model.getId() <= 0) {
            return false;
        }
        if (model.getStatus() == GoodsModel.NOT_PASS_AUDIT || model.getStatus() == GoodsModel.PASS_AUDIT) {
            Goods item = goodsDao.find(model.getId());
            item.setStatus(model.getStatus());
            // 审核日志
            VerifyLog log = new VerifyLog(new Operator(model.getOperatorId()),
                 "goodsVerify", model.getId(), 1, model.getStatus(), model.getOperatorRemark());
            verifyLogDao.save(log);
            item = goodsDao.update(item);
            return true;
        }
        return false;
    }

    @Override
    public Boolean shelvesGoods(GoodsModel model) {
        if (model == null || model.getId() <= 0) {
            return false;
        }
        boolean result = false;
        Goods item = goodsDao.find(model.getId());
        // 如果现在是上架操作，并且以前是下架类型，并状态为审核通过，则将状态更新为未审核，待上架状态
        if (model.getType() == GoodsModel.UP_SHELVES && item.getType() == GoodsModel.DOWN_SHELVES && 
                item.getStatus() == GoodsModel.PASS_AUDIT) {
            
            item.setType(GoodsModel.WAIT_SHELVES);
            item.setStatus(GoodsModel.WAIT_AUDIT);
            item.setShelvesTime(new Date());
            
            // 上下架日志
            VerifyLog log1 = new VerifyLog(new Operator(model.getOperatorId()),
                 "goodsShelves", model.getId(), 1, GoodsModel.WAIT_SHELVES, model.getOperatorRemark());
            verifyLogDao.save(log1);
            
            // 审核日志
            VerifyLog log2 = new VerifyLog(new Operator(model.getOperatorId()),
                 "goodsVerify", model.getId(), 1, GoodsModel.WAIT_AUDIT, model.getOperatorRemark());
            verifyLogDao.save(log2);
            result = true;
        // 如果是上架操作，并且以前为待上架审核通过，则直接跟新类型
        } else if (model.getType() == GoodsModel.UP_SHELVES && item.getType() == GoodsModel.WAIT_SHELVES && 
                item.getStatus() == GoodsModel.PASS_AUDIT) {
            
            VerifyLog log = new VerifyLog(new Operator(model.getOperatorId()),
                    "goodsShelves", model.getId(), 1, GoodsModel.UP_SHELVES, model.getOperatorRemark());
            verifyLogDao.save(log);
            item.setShelvesTime(new Date());
            item.setType(GoodsModel.UP_SHELVES);
            result = true;
        // 如果是下架操作，并且以前是上架类型，审核通过，则直接更新类型
        } else if (model.getType() == GoodsModel.DOWN_SHELVES && item.getType() == GoodsModel.UP_SHELVES &&
                item.getStatus() == GoodsModel.PASS_AUDIT) {
            
            VerifyLog log = new VerifyLog(new Operator(model.getOperatorId()),
                    "goodsShelves", model.getId(), 1, GoodsModel.DOWN_SHELVES, model.getOperatorRemark());
            verifyLogDao.save(log);
            item.setShelvesTime(new Date());
            item.setType(GoodsModel.DOWN_SHELVES);
            result = true;
        }
        if (result) {
            item.setUpdateTime(new Date());
            goodsDao.update(item);
        }
        
        return result;
    }

}
