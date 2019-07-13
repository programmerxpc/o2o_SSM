package cn.sunshine.o2o.service.impl;

import cn.sunshine.o2o.dao.ProductCategoryDao;
import cn.sunshine.o2o.dto.ProductCategoryExecution;
import cn.sunshine.o2o.entity.ProductCategory;
import cn.sunshine.o2o.enums.ProductCategoryStateEnum;
import cn.sunshine.o2o.exceptions.ProductCategoryOperationException;
import cn.sunshine.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 9:40
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        //TODO 将此类别下的商品的类别id置为空，再删除掉商品类别
        try {
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (ProductCategoryOperationException e) {
            throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
        }
    }

    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList != null && productCategoryList.size() > 0){
            try {
                int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectedNum <= 0){
                    throw new ProductCategoryOperationException("批量添加店铺失败");
                }else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (ProductCategoryOperationException e) {
                throw new ProductCategoryOperationException("batchAddProductCategory error:" + e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY);
        }
    }

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }
}
