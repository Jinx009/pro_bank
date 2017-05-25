package com.rongdu.p2psys.score.service;

import java.util.List;

import com.rongdu.p2psys.score.domain.GoodsCategory;
import com.rongdu.p2psys.score.model.GoodsCategoryModel;

public interface GoodsCategoryService {

    /**
     * 查询所有的商品分类
     * @return 商品分类
     */
    List<GoodsCategory> getCategoryAll();
    
    /**
     * 查询商品分类
     * @param model 分类
     * @return 商品分类
     */
    List<GoodsCategory> getCategoryList(GoodsCategoryModel model);
    
    /**
     * 添加商品分类
     * @param model 商品分类model
     * @return 是否成功
     */
    Boolean addCategory(GoodsCategoryModel model);
    
    /**
     * 根据ID查询商品分类信息
     * @param id ID
     * @return 商品分类
     */
    GoodsCategory getCategoryById(long id);
    
    /**
     * 修改商品分类
     * @param model 商品分类model
     * @return 是否成功
     */
    Boolean editCategory(GoodsCategoryModel model);
}
