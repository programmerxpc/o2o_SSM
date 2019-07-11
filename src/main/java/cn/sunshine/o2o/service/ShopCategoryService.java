package cn.sunshine.o2o.service;

import cn.sunshine.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-11 12:54
 */
public interface ShopCategoryService {

    /**
     * 获取店铺列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategory(ShopCategory shopCategoryCondition);

}
