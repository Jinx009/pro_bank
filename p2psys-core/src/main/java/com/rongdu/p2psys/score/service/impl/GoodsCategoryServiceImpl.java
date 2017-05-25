package com.rongdu.p2psys.score.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.score.dao.GoodsCategoryDao;
import com.rongdu.p2psys.score.domain.GoodsCategory;
import com.rongdu.p2psys.score.model.GoodsCategoryModel;
import com.rongdu.p2psys.score.service.GoodsCategoryService;

@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Resource
    private GoodsCategoryDao goodsCategoryDao;
    
    @Override
    public List<GoodsCategory> getCategoryAll() {
        QueryParam param = QueryParam.getInstance();
        param.addParam("isDelete", false);
        param.addOrder(OrderType.ASC, "sort");
        return goodsCategoryDao.findByCriteria(param);
    }

    @Override
    public List<GoodsCategory> getCategoryList(GoodsCategoryModel model) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("isDelete", false);
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
       }
        param.addOrder(OrderType.DESC, "addTime");
        return goodsCategoryDao.findByCriteria(param);
    }

    @Override
    public Boolean addCategory(GoodsCategoryModel model) {
        if (model == null || model.getName() == null) {
            return false;
        }
        GoodsCategory item = model.prototype();
        item.setAddTime(new Date());
        item = goodsCategoryDao.save(item);
        if (item.getId() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public GoodsCategory getCategoryById(long id) {
        return goodsCategoryDao.find(id);
    }

    @Override
    public Boolean editCategory(GoodsCategoryModel model) {
        if (model == null || model.getName() == null || model.getId() <= 0) {
            return false;
        }
        GoodsCategory item = model.prototype();
        item = goodsCategoryDao.update(item);
        return true;
    }

}
