package cn.sunshine.o2o.service;

import cn.sunshine.o2o.dto.ImageHolder;
import cn.sunshine.o2o.dto.ProductExecution;
import cn.sunshine.o2o.entity.Product;
import cn.sunshine.o2o.exceptions.ProductOperationException;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 23:53
 */
public interface ProductService {

    /**
     * 通过productId查询唯一的商品信息
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 添加商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> imageHolderList) throws ProductOperationException;

    /**
     * 修改商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail,List<ImageHolder> imageHolderList) throws ProductOperationException;

}
