package cn.sunshine.o2o.dao;

import cn.sunshine.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-11 12:27
 */
public interface ShopCategoryDao {

    /**
     * 查询店铺类别类别,查询条件：parentId
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

}
