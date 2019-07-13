package cn.sunshine.o2o.service;

import cn.sunshine.o2o.dto.ProductCategoryExecution;
import cn.sunshine.o2o.entity.ProductCategory;
import cn.sunshine.o2o.exceptions.ProductCategoryOperationException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 9:37
 */
public interface ProductCategoryService {

    /**
     * 将此类别下的商品的类别id置为空，再删除掉商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategoryOperationException;

    /**
     * 批量添加
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    /**
     * 获取指定店铺下的商品类别列表
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

}
