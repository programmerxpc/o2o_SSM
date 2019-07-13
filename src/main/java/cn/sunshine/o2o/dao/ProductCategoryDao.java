package cn.sunshine.o2o.dao;

import cn.sunshine.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 9:21
 */
public interface ProductCategoryDao {

    /**
     * 删除指定指定商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);

    /**
     * 通过shopId查询商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 批量添加商品类别
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

}
