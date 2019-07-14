package cn.sunshine.o2o.dao;

import cn.sunshine.o2o.entity.ProductImg;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 17:53
 */
public interface ProductImgDao {

    /**
     * 删除指定商品下的所有详情图
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

    /**
     * 批量添加商品详情图
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 通过productId查询商品详情图片列表
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgByProductId(long productId);

}
