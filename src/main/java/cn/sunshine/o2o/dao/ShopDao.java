package cn.sunshine.o2o.dao;

import cn.sunshine.o2o.entity.Shop;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 17:20
 */
public interface ShopDao {

    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

}
